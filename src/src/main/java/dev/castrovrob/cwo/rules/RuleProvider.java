package dev.castrovrob.cwo.rules;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Getter
@Slf4j
@NoArgsConstructor
@Component
public final class RuleProvider {

    /**
     * List of rules to be fetched.
     */
    private List<Rule> rules;

    /**
     * Method to initialize the RuleProvider.
     * Fetches the rules from the file.
     */
    @PostConstruct
    public void init() {
        fetchRules();
    }

    /**
     * Method to get the rules for the clock.
     * This is called in @PostConstruct
     */
    public void fetchRules() {
        log.debug("Fetching rules from resources: rules.json");
        String filePath = "rules.json";
        this.rules = loadRulesFromResources(filePath);
    }

    /**
     * Method to load the rules from the resources file `rules.json`.
     * @param filePath Path to the file.
     * @return List of rules.
     */
    private List<Rule> loadRulesFromResources(final String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream inputStream =
                     getClassLoaderResource(filePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + filePath);
            }
            return objectMapper
                    .readValue(
                            inputStream,
                            new TypeReference<>() { }
                    );
        } catch (IOException e) {
            log.error("Failed to load rules from resources", e);
            throw new RuntimeException(
                    "Failed to load rules from resources", e);
        }
    }

    /**
     * Method to get the input stream from the class loader.
     * Easier to mock in test having this separate
     * @param filePath Path to the file.
     * @return InputStream of the file.
     */
    InputStream getClassLoaderResource(final String filePath) {
        return getClass().getClassLoader().getResourceAsStream(filePath);
    }

    /**
     * Updates the message of a rule dynamically.
     * Primarily used by the API layer.
     * @param type Rule type (e.g., "hour", "minute", "second").
     * @param newMessage New message to be set.
     * @return True if the rule was found and updated, false otherwise.
     */
    public boolean updateRuleMessage(final String type,
                                     final String newMessage) {
        Optional<Rule> ruleOpt = rules.stream()
                .filter(rule -> rule.getType().equalsIgnoreCase(type))
                .findFirst();

        if (ruleOpt.isPresent()) {
            Rule rule = ruleOpt.get();
            rule.setMessage(newMessage);
            log.info("Updated rule `{}` message to `{}`", type, newMessage);
            return true;
        } else {
            log.warn("No rule found with type `{}`", type);
            return false;
        }
    }
}
