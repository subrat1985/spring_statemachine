package com.subrat.statemachine.config;

import com.subrat.statemachine.domain.PaymentEvent;
import com.subrat.statemachine.domain.PaymentState;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
@Slf4j
@RequiredArgsConstructor
@EnableStateMachineFactory
@Configuration
public class PaymentMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {
    @Override
    public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
        states.withStates()
        .initial(PaymentState.NEW)
                .state(PaymentState.VALIDATE,stateContext -> stateContext.getStateMachine().sendEvent(PaymentEvent.VALIDATED))
                .state(PaymentState.DEBIT,stateContext -> stateContext.getStateMachine().sendEvent(PaymentEvent.DEBITED))
                .state(PaymentState.CREDIT,stateContext -> stateContext.getStateMachine().sendEvent(PaymentEvent.CREDITED))
                .state(PaymentState.DEBIT_FAIL,stateContext -> stateContext.getStateMachine().sendEvent(PaymentEvent.DEBIT_DECLINED))
                .state(PaymentState.CREDIT_FAIL,stateContext -> stateContext.getStateMachine().sendEvent(PaymentEvent.CREDIT_DECLINED))
        .end(PaymentState.COMPLETE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions.withExternal().source(PaymentState.NEW).target(PaymentState.VALIDATE).event(PaymentEvent.NEW_PAYMENT)
        .and()
        .withExternal().source(PaymentState.VALIDATE).target(PaymentState.DEBIT).event(PaymentEvent.VALIDATED)
        .and()
        .withExternal().source(PaymentState.DEBIT).target(PaymentState.CREDIT).event(PaymentEvent.DEBITED)
        .and()
        .withExternal().source(PaymentState.CREDIT).target(PaymentState.COMPLETE).event(PaymentEvent.CREDITED)
        .and()
        .withExternal().source(PaymentState.DEBIT).target(PaymentState.DEBIT_FAIL).event(PaymentEvent.DEBIT_DECLINED)
        .and()
        .withExternal().source(PaymentState.CREDIT).target(PaymentState.CREDIT_FAIL).event(PaymentEvent.CREDIT_DECLINED);
    }


    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
        StateMachineListenerAdapter adapter=new StateMachineListenerAdapter<PaymentState, PaymentEvent>(){
            @Override
            public void stateChanged(State from, State to) {
                log.info(String.format("stateChanged(from:%s,to:%s)",from,to));
            }
        };
        config.withConfiguration().listener(adapter);
    }
}

