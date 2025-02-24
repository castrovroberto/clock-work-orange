package dev.castrovrob.cwo.rules;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.function.Predicate;

@Slf4j
public class Rule {

    /**
     * Condition to be met for the rule to be applied.
     * e.g. dateTime -> dateTime.getSecond() == 0
     */
    private final transient Predicate<LocalDateTime> conditionExpression;

    /**
     * Priority of the rule.
     * The lower the number, the higher the priority.
     * And the higher the number, the lower the priority.
     * i.e: 0 -> the highest priority
     * 100^100 -> the lowest priority in the universe
     */
    @Getter
    private final int priority;

    /**
     * Type of the rule to be applied.
     * e.g. "hour" or "minute" or "second" depending on the condition.
     */
    @Getter
    private final String type;

    /**
     * Message to be displayed when the rule is applied.
     */
    @Getter
    @Setter
    private String message;

    /**
     * All arguments Constructor for the Rule.
     * Not used lombok because of Json Parse and serialization.
     * @param newPriority Priority of the rule.
     * @param newType Type of the rule. i.e: hour, minute.
     * @param newMessage Message to be displayed.
     */
    public Rule(
            @JsonProperty("priority") final int newPriority,
            @JsonProperty("type") final String newType,
            @JsonProperty("message") final String newMessage
    ) {
        this.priority = newPriority;
        this.type = newType;
        this.message = newMessage;
        this.conditionExpression = parseCondition(type);
    }

    /**
     * Method to check if the rule matches the given time.
     * @param time LocalDateTime to check.
     * @return True if the rule matches the time, false otherwise.
     */
    public boolean matches(final LocalDateTime time) {
        return conditionExpression.test(time);
    }

    /**
     * Method to assign the condition string to a Predicate.
     * @param conditionString Condition string to evaluate.
     * @return Predicate for the condition.
     */
    private Predicate<LocalDateTime> parseCondition(
            final String conditionString
    ) {
        return switch (conditionString) {
            case "hour" -> dateTime -> dateTime
                    .withNano(0)
                    .getMinute() == 0
                    && dateTime
                    .withNano(0)
                    .getSecond() == 0;
            case "minute" -> dateTime -> dateTime
                    .withNano(0)
                    .getSecond() == 0;
            case "second" -> dateTime -> true;
            default -> dateTime -> false;
        };
    }

}
