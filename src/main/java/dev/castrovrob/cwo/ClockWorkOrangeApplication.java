package dev.castrovrob.cwo;

import dev.castrovrob.cwo.config.ClockProperties;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Instant;

@Getter
@SpringBootApplication
@EnableConfigurationProperties(ClockProperties.class)
@EnableScheduling
public class ClockWorkOrangeApplication {

    /**
     * Start timestamp of the application.
     * Used to calculate the duration of the application.
     * Populated during @PostConstruct.
     */
    private Instant startTimestamp;

    /**
     * Protected default constructor.
     */
    protected ClockWorkOrangeApplication() {
        // no-arg constructor.
    }

    /**
     * Clock Work Orange Application main method.
     * @param args Command line arguments, if any.
     */
    public static void main(final String[] args) {
        SpringApplication.run(ClockWorkOrangeApplication.class, args);
    }

    /**
     * Method to initialize the start timestamp.
     * The purpose of this method here is to get an
     * accurate start time of the bean construction.
     */
    @PostConstruct
    public void init() {
        startTimestamp = Instant.now();
    }

}
