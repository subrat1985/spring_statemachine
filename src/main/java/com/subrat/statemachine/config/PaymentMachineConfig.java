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
        .state(PaymentState.VALIDATE)
        .state(PaymentState.DEBIT)
        .state(PaymentState.CREDIT)
        .state(PaymentState.DEBIT_FAIL)
        .state(PaymentState.CREDIT_FAIL)
        .end(PaymentState.COMPLETE);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transitions) throws Exception {
        transitions.withExternal().source(PaymentState.NEW).target(PaymentState.VALIDATE).event(PaymentEvent.NEW_PAYMENT).action(action1())
        .and()
        .withExternal().source(PaymentState.VALIDATE).target(PaymentState.DEBIT).event(PaymentEvent.DEBITED).action(action2())
        .and()
        .withExternal().source(PaymentState.DEBIT).target(PaymentState.CREDIT).event(PaymentEvent.CREDITED).action(action3())
        .and()
        .withExternal().source(PaymentState.CREDIT).target(PaymentState.COMPLETE).event(PaymentEvent.COMPLETED).action(action4())
        .and()
        .withExternal().source(PaymentState.DEBIT).target(PaymentState.DEBIT_FAIL).event(PaymentEvent.DEBIT_DECLINED)
        .and()
        .withExternal().source(PaymentState.CREDIT).target(PaymentState.CREDIT_FAIL).event(PaymentEvent.CREDIT_DECLINED);
    }


    private Action<PaymentState, PaymentEvent> action1() {
        return stateContext->stateContext.getStateMachine().sendEvent(PaymentEvent.NEW_PAYMENT);
    }
    private Action<PaymentState, PaymentEvent> action2() {
        return stateContext->stateContext.getStateMachine().sendEvent(PaymentEvent.DEBITED);
    }
    private Action<PaymentState, PaymentEvent> action3() {
        return stateContext->stateContext.getStateMachine().sendEvent(PaymentEvent.CREDITED);
    }
    private Action<PaymentState, PaymentEvent> action4() {
        return stateContext->stateContext.getStateMachine().sendEvent(PaymentEvent.COMPLETED);
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

