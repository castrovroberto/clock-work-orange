package dev.castrovrob.cwo.security;

import dev.castrovrob.cwo.config.ClockProperties;
import dev.castrovrob.cwo.config.ConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private ClockProperties clockProperties;

    @Mock
    private ClockProperties.Run run;

    private TokenServiceImpl tokenService;

    private ConfigServiceImpl configService;

    private static final String VALID_TOKEN = "valid-token";
    private static final String INVALID_TOKEN = "invalid-token";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.configService = new ConfigServiceImpl(this.clockProperties);
        this.tokenService = new TokenServiceImpl(this.configService);
    }

    @Test
    void testValidateToken_ValidToken() {
        when(clockProperties.getRun()).thenReturn(run);
        when(configService.getToken()).thenReturn(VALID_TOKEN);
        boolean isValid = this.tokenService.validateToken(VALID_TOKEN);
        assertTrue(isValid, "Expected token to be valid");
    }

    @Test
    void testValidateToken_InvalidToken() {
        when(clockProperties.getRun()).thenReturn(run);
        when(configService.getToken()).thenReturn(VALID_TOKEN);
        boolean isValid = this.tokenService.validateToken(INVALID_TOKEN);
        assertFalse(isValid, "Expected token to be invalid");
    }

    @Test
    void testValidateToken_EmptyToken() {
        boolean isValid = this.tokenService.validateToken("");
        assertFalse(isValid, "Empty token should not be valid");
    }
}