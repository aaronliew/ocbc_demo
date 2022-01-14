package com.example.demo.service;

import com.example.demo.database.AuthDao;
import com.example.demo.database.DebtDao;
import com.example.demo.database.TransactionDao;
import com.example.demo.exception.InvalidArgumentException;
import com.example.demo.exception.InvalidRecipientException;
import com.example.demo.model.database.DebtEntity;
import com.example.demo.model.database.TransactionEntity;
import com.example.demo.model.database.UserEntity;
import com.example.demo.model.transaction.*;
import com.example.demo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class TransactionServiceImpl implements TransactionService{

    AuthDao authDao;

    TransactionDao transactionDao;
    DebtDao debtDao;
//
//    -- transaction_table// must be always 0
//-- alice topup 100
//            -- alice transaction amount 100, balance = 100
//            -- pay bob amount 210
//            -- alice transaction -100, balance = 0
//
//            --debt
//-- transaction_id=a, amount= -110
//
//            -- bob get all, list of debts, total balance in user

    @Autowired
    public TransactionServiceImpl(AuthDao authDao, TransactionDao transactionDao, DebtDao debtDao){
        this.authDao = authDao;
        this.transactionDao = transactionDao;
        this.debtDao = debtDao;
    }

    @Override
    public PaymentResponse makePayment(PaymentRequest paymentRequest) throws InvalidArgumentException, InvalidRecipientException{
        //check if recipient is empty else throw exception
        UserEntity senderEntity = getUserEntityByUsername(paymentRequest.getSenderUserName());
        UserEntity recipientEntity = getUserEntityByUsername(paymentRequest.getRecipientUserName());
        updateTransactionInDb(senderEntity.getId(), recipientEntity.getId(), -paymentRequest.getAmount());

        Long balance = senderEntity.getBalance();
        Long balanceAfterDebt = settleOwedFromRecipientDebt(senderEntity, recipientEntity, paymentRequest);
        PaymentResponse senderPaymentResponse = handleSender(senderEntity, balanceAfterDebt, paymentRequest);

        handleRecipient(balance, recipientEntity, balanceAfterDebt);
        return senderPaymentResponse;
    }

    public TopUpResponse topUpPayment(TopUpRequest topUpRequest) throws InvalidArgumentException{
        //check if recipient is empty else throw exception
        UserEntity userEntity = getUserEntityByUsername(topUpRequest.getUsername());
        updateTransactionInDb(userEntity.getId(), userEntity.getId(), topUpRequest.getAmount());
        Long balance = userEntity.getBalance() + topUpRequest.getAmount();
        UserEntity resultUserEntity =updateUserBalance(userEntity, balance);
        Payment payment = payDebt(userEntity.getId());

        Debt debt = getDebtSummary(userEntity.getId());

        TopUpResponse topUpResponse = new TopUpResponse();
        topUpResponse.setPayment(payment);
        topUpResponse.setBalance(resultUserEntity.getBalance());
        topUpResponse.setDebt(debt);
        return topUpResponse;
    }

    @Override
    public Debt getDebtSummary(Long userId){
        DebtEntity oweToDebtEntity = debtDao.getDebtEntityBySenderUserId(userId);
        DebtEntity oweFromDebtEntity = debtDao.getDebtEntityByRecipientUserId(userId);
        Debt debt = null;
        if (oweToDebtEntity != null){
            debt  = constructDebt(oweToDebtEntity.getSenderUserId(),
                    oweToDebtEntity.getRecipientUserId(),
                    oweToDebtEntity.getAmount());
        } else if (oweFromDebtEntity != null){
            debt  = constructDebt(oweFromDebtEntity.getRecipientUserId(),
                    oweFromDebtEntity.getSenderUserId(),
                    -oweFromDebtEntity.getAmount());
        }

        return debt;
    }

    private Debt constructDebt(Long senderUserId, Long recipientUserId, Long amount){
        Debt debt = new Debt();
        debt.setSenderName(authDao.getUserEntityById(senderUserId).getUsername());
        UserEntity recipientEntity = getUserEntityByUsername(authDao.getUserEntityById(recipientUserId).getUsername());
        debt.setRecipientName(recipientEntity.getUsername());
        debt.setAmount(amount);
        return debt;
    }

    private Payment constructPayment(Long senderUserId, Long recipientUserId, Long amount){
        Payment payment = new Payment();
        payment.setSenderName(authDao.getUserEntityById(senderUserId).getUsername());
        UserEntity recipientEntity = getUserEntityByUsername(authDao.getUserEntityById(recipientUserId).getUsername());
        payment.setRecipientName(recipientEntity.getUsername());
        payment.setAmount(amount);
        return payment;
    }

    private PaymentResponse handleSender(UserEntity senderEntity, long balanceAfterDebt, PaymentRequest paymentRequest) {
        if (balanceAfterDebt > 0) {
            Long balance = senderEntity.getBalance() - balanceAfterDebt;
            updateUserBalance(senderEntity, balance);
            UserEntity recipientEntity = getUserEntityByUsername(paymentRequest.getRecipientUserName());
            updateSenderDebt(senderEntity.getId(), recipientEntity.getId(), balance);
        }

        UserEntity userEntity = authDao.getUserEntitiesByUsername(senderEntity.getUsername());
        Debt debt = getDebtSummary(senderEntity.getId());
        return constructPaymentResponse(paymentRequest.getAmount(),
                userEntity.getBalance(), debt);
    }

    private Long settleOwedFromRecipientDebt(UserEntity senderEntity, UserEntity recipientEntity, PaymentRequest paymentRequest){
        DebtEntity debtEntity = debtDao.getDebtEntityBySenderUserIdAndRecipientUserId(recipientEntity.getId(), senderEntity.getId());
        Long balanceAfterDebt = paymentRequest.getAmount();
        if (debtEntity != null){
            balanceAfterDebt = paymentRequest.getAmount() - debtEntity.getAmount();
            if (balanceAfterDebt > 0) {
                debtEntity.setAmount(0L);
            } else {
                debtEntity.setAmount(Math.abs(balanceAfterDebt));
            }
            debtDao.save(debtEntity);
        }
        return balanceAfterDebt;
    }

    private void handleRecipient(Long balance, UserEntity recipientEntity, Long paymentAmount){
        Long balanceAfterPayment =  balance - paymentAmount;
        Long actualPaymentAmount = 0L;
        if (balanceAfterPayment > 0){
            actualPaymentAmount  = paymentAmount;
        } else {
            actualPaymentAmount = balance;
        }

        Long recipientBalance =  recipientEntity.getBalance() + actualPaymentAmount;
        updateUserBalance(recipientEntity, recipientBalance);
//        payDebt(recipientEntity.getId());
    }


    private UserEntity updateUserBalance(UserEntity userEntity, Long balance){
        userEntity.setBalance(balance);
        if (balance < 0) {
            userEntity.setBalance(0L);
        }
        return authDao.save(userEntity);
    }

    private DebtEntity updateSenderDebt(Long senderUserId, Long recipientUserId, Long balance){
        if (balance > 0) {
            return null;
        }

        balance = Math.abs(balance);
        DebtEntity debtEntity =
                debtDao.getDebtEntityBySenderUserIdAndRecipientUserId(senderUserId, recipientUserId);
        if (debtEntity != null){
            balance = balance + debtEntity.getAmount();
        } else {
            debtEntity = new DebtEntity();
        }
        debtEntity.setSenderUserId(senderUserId);
        debtEntity.setRecipientUserId(recipientUserId);
        debtEntity.setAmount(balance);
        debtEntity = debtDao.save(debtEntity);
        return debtEntity;
    }

    private Payment payDebt(Long senderUserId){
        UserEntity userEntity = authDao.getUserEntityById(senderUserId);
        Long balance = userEntity.getBalance();
        DebtEntity debtEntity = debtDao.getDebtEntityBySenderUserId(senderUserId);
        Long paymentAmount = 0L;
        if (debtEntity != null) {
            if (debtEntity.getAmount() > balance){
                paymentAmount = balance;
                Long debtAmount = debtEntity.getAmount() - balance;
                balance = 0L;
                debtEntity.setAmount(debtAmount);
            } else {
                paymentAmount = debtEntity.getAmount();;
                balance = balance - debtEntity.getAmount();
                debtEntity.setAmount(0L);
            }
            debtDao.save(debtEntity);
        }

        userEntity = authDao.getUserEntityById(senderUserId);
        updateUserBalance(userEntity, balance);

        if (paymentAmount > 0) {
            updateTransactionInDb(debtEntity.getSenderUserId(),
                    debtEntity.getRecipientUserId(),
                    paymentAmount);
            return constructPayment(debtEntity.getSenderUserId(),
                    debtEntity.getRecipientUserId(),
                    paymentAmount);
        }


        return null;
    }

    private PaymentResponse constructPaymentResponse(Long transactionAmount, Long balance, Debt debt){
        Calendar calendar = Calendar.getInstance();
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransaction(transactionAmount);
        paymentResponse.setBalance(balance);
        paymentResponse.setDebt(debt);
        paymentResponse.setAsOnDate(DateUtil.convertToStandardDateStringFormat(calendar.getTimeInMillis()));
        return  paymentResponse;
    }

    private UserEntity getUserEntityByUsername(String username) throws InvalidRecipientException{
        UserEntity userEntity = authDao.getUserEntitiesByUsername(username);
        if (userEntity == null) {
            throw new InvalidRecipientException();
        }
        return userEntity;
    }

    private TransactionEntity updateTransactionInDb(Long senderUserId, Long recipientUserId, Long amount){
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setSenderUserId(senderUserId);
        if (recipientUserId != null) {
            transactionEntity.setRecipientUserId(recipientUserId);
        }
        transactionEntity.setAmount(amount);
        return transactionDao.save(transactionEntity);
    }
}
