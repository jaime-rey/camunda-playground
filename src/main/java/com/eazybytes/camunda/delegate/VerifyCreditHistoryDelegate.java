package com.eazybytes.camunda.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class VerifyCreditHistoryDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(VerifyCreditHistoryDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String applicantId = (String) execution.getVariable("applicantId");
        int creditScore = ThreadLocalRandom.current().nextInt(300, 851);

        log.info("Verifying credit history, applicantId={} -> creditScore={}", applicantId, creditScore);

        execution.setVariable("creditScore", creditScore);
    }
}
