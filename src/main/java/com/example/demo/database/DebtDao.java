package com.example.demo.database;

import com.example.demo.model.database.DebtEntity;
import com.example.demo.model.database.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtDao extends JpaRepository<DebtEntity, Long> {
    DebtEntity getDebtEntityBySenderUserIdAndRecipientUserId(long senderId, long recipientId);

    @Query("select debt from DebtEntity debt " +
            "where debt.senderUserId = : senderId and debt.amount > 0 " +
            "order by debt.recordCreateDate")
    List<DebtEntity> getDebtEntityBySenderUserId(@Param("senderId") Long senderId);
}
