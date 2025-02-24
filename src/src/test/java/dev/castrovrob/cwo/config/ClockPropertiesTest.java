package dev.castrovrob.cwo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class ClockPropertiesTest {

    @Autowired
    private ClockProperties clockProperties;

    @BeforeEach
    void setUp() {
        assertNotNull(clockProperties, "ClockProperties should be loaded by Spring.");
    }

    @Test
    void testValidConfigurationProperties() {
        assertNotNull(clockProperties.getRun(), "Run properties should not be null");
        assertEquals(3, clockProperties.getRun().getDuration(), "Duration should be correctly bound to 3");
    }

    @Test
    void testInvalidDurationThrowsException() {
        ClockProperties invalidProperties = new ClockProperties();
        invalidProperties.getRun().setDuration(0);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, invalidProperties::validate);
        assertEquals("Duration must be greater than 0.", thrown.getMessage());
    }

    @Test
    void testDefaultConstructorInitializesRun() {
        ClockProperties properties = new ClockProperties();
        assertNotNull(properties.getRun(), "Run should not be null after default constructor");
        assertEquals(0, properties.getRun().getDuration(), "Default duration should be 0");
    }
  
}