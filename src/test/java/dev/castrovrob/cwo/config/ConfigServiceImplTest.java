package dev.castrovrob.cwo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfigServiceImplTest {

    @Mock
    private ClockProperties clockProperties;

    @Mock
    private ClockProperties.Run run;

    private ConfigServiceImpl configService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(clockProperties.getRun()).thenReturn(run);
        configService = new ConfigServiceImpl(clockProperties);
    }

    @Test
    void testGetDuration_ReturnsExpectedValue() {
        when(run.getDuration()).thenReturn(5);
        int duration = configService.getDuration();
        assertEquals(5, duration, "Duration should be correctly returned from ClockProperties.");
        verify(clockProperties, times(1)).getRun();
        verify(run, times(1)).getDuration();
    }
}