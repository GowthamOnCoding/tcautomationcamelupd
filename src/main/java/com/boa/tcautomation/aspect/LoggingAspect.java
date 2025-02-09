package com.boa.tcautomation.aspect;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.TcSteps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String LOG_STEP_EXECUTING = "{}=> Step -> {}, {} is executing";
    private static final String LOG_STEP_COMPLETED = "{}=> Step -> {}, {} is completed";
    private static final String LOG_STEP_ERROR = "{}=> Step - {}, {} Error running command: {}";
    private static final String LOG_STEP_VALIDATION_FAILED = "{}=> Step - {}, {} Failed to get and validate StepConfig";

    @Before("execution(* com.boa.tcautomation.service.TcMasterService.*(..)) && args(tcMaster, tcStep)")
    public void logBefore(JoinPoint joinPoint, TcMaster tcMaster, TcSteps tcStep) {
        log.info(LOG_STEP_EXECUTING, tcMaster.getTcId(), tcStep.getSequenceNo(), tcStep.getStepName());
    }

    @AfterReturning("execution(* com.boa.tcautomation.service.TcMasterService.*(..)) && args(tcMaster, tcStep)")
    public void logAfterReturning(JoinPoint joinPoint, TcMaster tcMaster, TcSteps tcStep) {
        log.info(LOG_STEP_COMPLETED, tcMaster.getTcId(), tcStep.getSequenceNo(), tcStep.getStepName());
    }

    @AfterThrowing(pointcut = "execution(* com.boa.tcautomation.service.TcMasterService.*(..)) && args(tcMaster, tcStep)", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, TcMaster tcMaster, TcSteps tcStep, Throwable error) {
        log.error(LOG_STEP_ERROR, tcMaster.getTcId(), tcStep.getSequenceNo(), tcStep.getStepName(), error.getMessage());
    }
}
