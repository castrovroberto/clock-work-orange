package dev.castrovrob.cwo.scheduler;

import dev.castrovrob.cwo.ClockWorkOrangeApplication;
import dev.castrovrob.cwo.config.ConfigService;
import dev.castrovrob.cwo.rules.RuleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@AllArgsConstructor
@Slf4j
public class ClockScheduler {

    /**
     * Magic Number FIXED_RATE
     * Fixed rate for the scheduler.
     * i.e. 1000 milliseconds.
     * Avoidance of magic numbers.
     */
    private static final int FIXED_RATE = 1000;

    /**
     * ZoneId for the clock usage.
     */
    private final ZoneId zone = ZoneId.systemDefault();

    /**
     * Reference to the application context.
     * Used to have access to SpringApplication.exit().
     */
    private final ApplicationContext applicationContext;

    /**
     * Reference to the ClockWorkOrangeApplication.
     * To gain access to the exact @PostConstruct start time timestamp.
     */
    private final ClockWorkOrangeApplication clockWorkOrangeApplication;

    /**
     * Reference to the ConfigService.
     * Used to get the duration of the clock to run.
     */
    private final ConfigService configService;

    /**
     * Reference to the RuleService.
     * Used to evaluate the rules.
     */
    private final RuleService ruleService;

    /**
     * Scheduled method.
     * It runs every second. i.e. 1000 milliseconds.
     * Fetches the current list of rules from the RuleProvider
     * Pass the rule and the current datetime to the PriorityRuleEvaluator
     * Prints the resulting message.
     */
    @Scheduled(fixedRate = FIXED_RATE)
    public void deltaStep() {
        LocalDateTime now = LocalDateTime.now();
        checkShutdown(now);

        String message = this.ruleService.evaluate(now);
        log.info("[{}]: {}", now, message);
    }

    /**
     * Check shutdown compares the start timestamp to the
     * hours expected to run for a graceful shutdown.
     * @param now local date time to evaluate.
     */
    private void checkShutdown(final LocalDateTime now) {
        Duration elapsedTime = Duration.between(
                this.clockWorkOrangeApplication
                    .getStartTimestamp()
                    .atZone(zone)
                    .toInstant(),
                now
                    .atZone(zone)
                    .toInstant()
        );
        long elapsedHours = elapsedTime.toHours();
        if (elapsedHours >= this.configService
                .getDuration()) {
            SpringApplication.exit(this.applicationContext, () -> 0);
        }
    }
}
