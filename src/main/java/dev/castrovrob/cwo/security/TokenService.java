package dev.castrovrob.cwo.security;

public interface TokenService {

    /**
     * Definition of validate token method in the
     * TokenService interface.
     * @param token the request token.
     * @return boolean value if whether the validation is correct or not.
     */
    boolean validateToken(String token);
}
