package com.subrat.statemachine.domain;

public enum PaymentState {
    NEW,VALIDATE,DEBIT,DEBIT_FAIL,CREDIT,CREDIT_FAIL,COMPLETE
}
