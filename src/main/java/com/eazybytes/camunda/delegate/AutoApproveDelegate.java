package com.eazybytes.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AutoApproveDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(AutoApproveDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String applicantId = (String) execution.getVariable("applicantId");
        Integer creditScore = (Integer) execution.getVariable("creditScore");

        log.info("Auto-approving, applicantId={} creditScore={}", applicantId, creditScore);

        execution.setVariable("approved", true);
    }
}
