package com.example.demo.util;

import com.example.demo.model.transaction.Debt;

public class CommandUtil {
    public static void printDebt(Debt debt){
        if (debt != null && debt.getAmount() > 0) {
            System.out.println("Owing " + debt.getAmount() + " to " + debt.getRecipientName());
        } else if (debt != null && debt.getAmount() < 0) {
            System.out.println("Owing " + Math.abs(debt.getAmount()) + " from " + debt.getRecipientName());
        }
    }
}
