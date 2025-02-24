package dev.castrovrob.cwo.rules;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriorityRuleEvaluatorTest {

    @Mock
    private RuleProvider ruleProvider;

    @InjectMocks
    private PriorityRuleEvaluator priorityRuleEvaluator;

    private static final LocalDateTime TEST_TIME = LocalDateTime.of(2025, 2, 25, 12, 0, 0);

    private Rule hourRule;
    private Rule minuteRule;
    private Rule secondRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hourRule = new Rule(0, "hour", "bong");
        minuteRule = new Rule(1, "minute", "tock");
        secondRule = new Rule(2, "second", "tick");

        this.priorityRuleEvaluator = new PriorityRuleEvaluator(this.ruleProvider);
        when(this.ruleProvider.getRules()).thenReturn(List.of(hourRule, minuteRule, secondRule));
    }

    @Test
    void testEvaluate_ShouldReturnHighestPriorityMatch() {
        String result = priorityRuleEvaluator.evaluate(TEST_TIME);
        assertEquals("bong", result);
        verify(ruleProvider, times(1)).getRules();
    }

    @Test
    void testEvaluate_ShouldReturnMinuteRuleWhenHourNotMatching() {
        LocalDateTime testTime = LocalDateTime.of(2025, 2, 25, 12, 30, 0);
        when(ruleProvider.getRules()).thenReturn(List.of(hourRule, minuteRule, secondRule));
        String result = priorityRuleEvaluator.evaluate(testTime);
        assertEquals("tock", result);
    }

    @Test
    void testEvaluate_ShouldReturnSecondRuleWhenOthersDontMatch() {
        LocalDateTime testTime = LocalDateTime.of(2025, 2, 25, 12, 30, 15);
        when(ruleProvider.getRules()).thenReturn(List.of(hourRule, minuteRule, secondRule));
        String result = priorityRuleEvaluator.evaluate(testTime);
        assertEquals("tick", result);
    }

    @Test
    void testEvaluate_NoMatchingRule_ShouldReturnEmptyString() {
        when(ruleProvider.getRules()).thenReturn(List.of());
        String result = priorityRuleEvaluator.evaluate(TEST_TIME);
        assertEquals("", result);
    }

}