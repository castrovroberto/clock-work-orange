package dev.castrovrob.cwo.rules;

import java.time.LocalDateTime;

public interface RuleService {

    /**
     * Get the rule message for a given type.
     *
     * @param type The type of the rule.
     * @param message The message of the rule.
     * @return The message of the rule.
     */
    boolean updateRule(String type, String message);

    /**
     * Evaluate the rule.
     *
     * @param now The current time.
     * @return The message of the rule.
     */
    String evaluate(LocalDateTime now);
}
