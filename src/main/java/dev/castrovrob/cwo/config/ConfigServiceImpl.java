package dev.castrovrob.cwo.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    /**
     * The clock properties.
     */
    private final ClockProperties clockProperties;

    /**
     * Get the duration of the clock application run.
     *
     * @return the duration of the clock
     */
    @Override
    public int getDuration() {
        log.debug("Calling service interface to get clock's run duration.");
        return clockProperties.getRun().getDuration();
    }

    /**
     * Gets the rudimentary validation for auth token.
     *
     * @return the default bearer token value.
     */
    @Override
    public String getToken() {
        log.debug("Getting token from configuration");
        return clockProperties.getRun().getToken();
    }
}
