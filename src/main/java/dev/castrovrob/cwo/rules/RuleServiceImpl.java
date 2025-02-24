package dev.castrovrob.cwo.rules;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j
public final class RuleServiceImpl implements RuleService {

    /**
     * The rule provider reference.
     */
    private final RuleProvider ruleProvider;

    /**
     * The priority rule evaluator reference.
     */
    private final PriorityRuleEvaluator priorityRuleEvaluator;

    /**
     * Update the rule message.
     * This is the method exposed to the API.
     * The Controller does not have direct access to the
     * rule provider itself.
     *
     * @param type The type of the rule.
     * @param message The message of the rule.
     * @return True if the rule was updated, false otherwise.
     */
    @Override
    public boolean updateRule(
            final String type,
            final String message) {
        log.debug("Updating rule message {} for type: {}", message, type);
        return this.ruleProvider.updateRuleMessage(type, message);
    }

    /**
     * This evaluation method is exposed to the scheduler.
     * Easier to test and safer because the Scheduler does not
     * interact with the rule provide class itself.
     * @param now The current time.
     * @return String for the message to show.
     */
    @Override
    public String evaluate(final LocalDateTime now) {
        log.debug("Evaluating rule for time: {}", now.withNano(0));
        return this.priorityRuleEvaluator.evaluate(now);
    }
}
