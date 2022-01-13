package com.example.demo.service;

import com.example.demo.database.AuthDao;
import com.example.demo.database.DebtDao;
import com.example.demo.database.TransactionDao;
import com.example.demo.exception.InvalidArgumentException;
import com.example.demo.model.auth.LoginResponse;
import com.example.demo.model.command.Username;
import com.example.demo.model.database.DebtEntity;
import com.example.demo.model.database.TransactionEntity;
import com.example.demo.model.database.UserEntity;
import com.example.demo.model.transaction.PaymentRequest;
import com.example.demo.model.transaction.PaymentResponse;
import com.example.demo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

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
    public PaymentResponse makePayment(PaymentRequest paymentRequest) throws InvalidArgumentException{
        //check if recipient is empty else throw exception
        UserEntity senderEntity = getUserEntityById(paymentRequest.getSenderUserId());
        UserEntity recipientEntity = getUserEntityById(paymentRequest.getRecipientUserId());
        updateTransactionInDb(paymentRequest);

        PaymentResponse senderPaymentResponse = handleSender(senderEntity, paymentRequest);
        handleRecipient(recipientEntity, paymentRequest);
        return senderPaymentResponse;
    }

    private PaymentResponse handleSender(UserEntity senderEntity, PaymentRequest paymentRequest) {
        Long balance =  senderEntity.getBalance() - paymentRequest.getAmount();
        UserEntity userEntity = updateUserBalance(senderEntity, balance);
        DebtEntity debtResult = updateDebt(senderEntity.getId(), paymentRequest.getRecipientUserId(), balance);

        return constructPaymentResponse(paymentRequest.getAmount(),
                userEntity.getBalance(),
                debtResult.getAmount() == null ? 0 : debtResult.getAmount());
    }

    private void handleRecipient(UserEntity recipientEntity, PaymentRequest paymentRequest){
        Long balance =  recipientEntity.getBalance() + paymentRequest.getAmount();
        updateUserBalance(recipientEntity, balance);

        Long senderId = recipientEntity.getId();
        Long recipientId = paymentRequest.getSenderUserId();
        payDebt(senderId, recipientId, balance);
    }

    private UserEntity updateUserBalance(UserEntity userEntity, Long balance){
        if (balance < 0) {
            userEntity.setBalance(0L);
        }
        userEntity.setBalance(balance);
        return authDao.save(userEntity);
    }

    private DebtEntity updateDebt(Long senderUserId, Long recipientUserId, Long balance){
        balance = Math.abs(balance);
        DebtEntity debtEntity =
                debtDao.getDebtEntityBySenderUserIdAndRecipientUserId(senderUserId, recipientUserId);
        if (debtEntity != null){
            balance = balance + debtEntity.getAmount();
        }
        if (balance < 0) {
            debtEntity.setSenderUserId(senderUserId);
            debtEntity.setRecipientUserId(recipientUserId);
            debtEntity.setAmount(balance);
            debtEntity = debtDao.save(debtEntity);
        }
        return debtEntity;
    }

    private void payDebt(Long senderUserId, Long recipientUserId, Long balance){
        List<DebtEntity> debtEntities = debtDao.getDebtEntityBySenderUserId(senderUserId);
        if (debtEntities != null && debtEntities.size() > 0) {
            for (DebtEntity debtEntity : debtEntities) {
                if (debtEntity.getAmount() > balance){
                    Long debtAmount = debtEntity.getAmount() - balance;
                    balance = 0L;
                    debtEntity.setAmount(debtAmount);
                    debtDao.save(debtEntity);
                    break;
                } else {
                    balance = balance - debtEntity.getAmount();
                    debtEntity.setAmount(0L);
                    debtDao.save(debtEntity);
                }
            }
        }

        UserEntity userEntity = getUserEntityById(senderUserId);
        updateUserBalance(userEntity, balance);
    }

    private PaymentResponse constructPaymentResponse(Long transactionAmount, Long balance, Long debtAmount){
        Calendar calendar = Calendar.getInstance();
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setTransaction(transactionAmount);
        paymentResponse.setBalance(balance);
        paymentResponse.setDebt(debtAmount);
        paymentResponse.setAsOnDate(DateUtil.convertToStandardDateStringFormat(calendar.getTimeInMillis()));
        return  paymentResponse;
    }

    private UserEntity getUserEntityById(Long userId) throws InvalidArgumentException{
        UserEntity userEntity = authDao.findUserEntityById(userId);
        if (userEntity == null) {
            throw new InvalidArgumentException();
        }
        return userEntity;
    }

    private TransactionEntity updateTransactionInDb(PaymentRequest paymentRequest){
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setSenderUserId(paymentRequest.getSenderUserId());
        transactionEntity.setRecipientUserId(paymentRequest.getRecipientUserId());
        transactionEntity.setAmount(-paymentRequest.getAmount());
        return transactionDao.save(transactionEntity);
    }
}
