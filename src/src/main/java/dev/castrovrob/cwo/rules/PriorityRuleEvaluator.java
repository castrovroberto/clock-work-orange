package dev.castrovrob.cwo.rules;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Component
public final class PriorityRuleEvaluator {

    /**
     * Reference to the rule provider to have
     * access to the fetched rules.
     */
    private final RuleProvider ruleProvider;

    /**
     * Contains the logic for sorting by priority and
     * finding the first rule that matches at any given time.
     * @param dateTime LocalDateTime to evaluate.
     * @return The message of the first rule that matches the given time.
     */
    public String evaluate(
            final LocalDateTime dateTime
    ) {
        String message = "";
        LocalDateTime evaluateDateTime = dateTime.withNano(0);

        List<Rule> mutableRules = new ArrayList<>(this.ruleProvider.getRules());
        mutableRules.sort(Comparator.comparingInt(Rule::getPriority));
        for (Rule rule : mutableRules) {
            if (rule.matches(evaluateDateTime)) {
                log.debug("Rule `{}` matched the time: {}",
                        rule.getType(), evaluateDateTime);
                message = rule.getMessage();
                break;
            }
        }
        if (message.isEmpty()) {
            log.debug("No rule matched the time: {}",
                    evaluateDateTime);
        }
        return message;
    }
}
