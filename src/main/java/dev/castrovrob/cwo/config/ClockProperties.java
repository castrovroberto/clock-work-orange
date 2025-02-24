package dev.castrovrob.cwo.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "clock")
@Data
@Slf4j
public class ClockProperties {

    /**
     * Reference to the run part of the
     * properties' configuration.
     */
    private Run run;

    /**
     * Default constructor with no arguments.
     */
    public ClockProperties() {
        log.debug("ClockProperties default constructor called.");
        this.run = new Run();
    }

    /**
     * Method to validate the configuration properties
     * after instantiation.
     * @throws IllegalArgumentException If any of the properties are invalid.
     */
    @PostConstruct
    public void validate() {
        if (this.run.getDuration() <= 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than 0.");
        }
        log.debug("Configuration properties: {}", this);
    }

    @Data
    @NoArgsConstructor
    public static class Run {

        /**
         * Configuration variable that will hold the duration of
         * the clock to run in hours.
         * For example: Default value is 3.
         */
        private int duration;

        /**
         * Configuration variable that will hold the default
         * secret token to use the update api.
         * For example: Default value is "Bearer valid-token"
         */
        private String token;
    }
}
