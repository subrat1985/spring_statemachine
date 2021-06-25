package com.subrat.statemachine.config;

import com.subrat.statemachine.domain.PaymentEvent;
import com.subrat.statemachine.domain.PaymentState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class PaymentMachineConfigTest {
    @Autowired
    StateMachineFactory<PaymentState,PaymentEvent> factory;
    @Test
    void testMachine(){
        StateMachine<PaymentState,PaymentEvent> sm = factory.getStateMachine(UUID.randomUUID());
        sm.start();
        System.out.println(sm.getState().toString());
        sm.sendEvent(PaymentEvent.NEW_PAYMENT);
    }

}