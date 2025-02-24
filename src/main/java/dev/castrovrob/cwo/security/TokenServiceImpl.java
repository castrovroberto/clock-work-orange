package dev.castrovrob.cwo.security;

import dev.castrovrob.cwo.config.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    /**
     * Referenced ConfigService class instance.
     */
    private final ConfigService configService;

    /**
     * Rudimentary token validation using config default value.
     * @param token Token in the request
     * @return true if token is correct.
     */
    @Override
    public boolean validateToken(final String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return configService.getToken().equals(token);
    }
}
