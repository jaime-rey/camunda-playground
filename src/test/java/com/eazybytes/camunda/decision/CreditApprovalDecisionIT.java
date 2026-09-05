package com.eazybytes.camunda.decision;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.DecisionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CreditApprovalDecisionIT {

    private static final String DECISION_KEY = "credit-approval";

    private final DecisionService decisionService;

    @Test
    void withHighScoreAndLowLimit_returnsAutoApproved() {
        DmnDecisionTableResult result = evaluate(800, 5000);

        assertThat(result).hasSize(1);
        assertThat(result.getFirstResult().<Boolean>getEntry("approved")).isTrue();
        assertThat(result.getFirstResult().<String>getEntry("reason")).isEqualTo("AUTO_APPROVED");
    }

    @ParameterizedTest(name = "score={0}, limit={1} -> reason={2}")
    @CsvSource({
        "720, 25000, LIMIT_TOO_HIGH_FOR_SCORE",
        "600,  5000, MANUAL_REVIEW_REQUIRED",
        "400,  5000, REJECTED_LOW_SCORE"
    })
    void deniedScenarios_returnExpectedReason(int creditScore, int requestedLimit, String expectedReason) {
        DmnDecisionTableResult result = evaluate(creditScore, requestedLimit);

        assertThat(result).hasSize(1);
        assertThat(result.getFirstResult().<Boolean>getEntry("approved")).isFalse();
        assertThat(result.getFirstResult().<String>getEntry("reason")).isEqualTo(expectedReason);
    }

    @Test
    void withUncoveredInputs_returnsEmpty() {
        DmnDecisionTableResult result = evaluate(720, 5000);

        assertThat(result).isEmpty();
    }

    private DmnDecisionTableResult evaluate(int creditScore, int requestedLimit) {
        Map<String, Object> variables = Map.of(
                "creditScore", creditScore,
                "requestedLimit", requestedLimit
        );
        return decisionService
                .evaluateDecisionTableByKey(DECISION_KEY)
                .variables(variables)
                .evaluate();
    }


}
