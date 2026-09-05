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
class CardTierDecisionIT {

    private final DecisionService decisionService;

    @ParameterizedTest(name = "score={0}, limit={1} -> {2}")
    @CsvSource({
        "800, 20000, PLATINUM",   // rule 1
        "800,  5000, GOLD",       // rule 2
        "780,  5000, GOLD",       // rule 3
        "720,  5000, SILVER",     // rule 4
        "500,  5000, BRONZE", // rule 5 (catch-all)
        "800, 15000, PLATINUM"
    })
    void allTiersReturnExpectedValue(int creditScore, int requestedLimit, String expectedTier) {
        DmnDecisionTableResult result = evaluate(creditScore, requestedLimit);

        assertThat(result).hasSize(1);
        assertThat(result.getFirstResult().<String>getEntry("cardTier")).isEqualTo(expectedTier);
    }

    private DmnDecisionTableResult evaluate(int creditScore, int requestedLimit) {
        Map<String, Object> variables = Map.of(
            "creditScore", creditScore,
            "requestedLimit", requestedLimit
        );
        return decisionService
            .evaluateDecisionTableByKey("card-tier")
            .variables(variables)
            .evaluate();
    }

}
