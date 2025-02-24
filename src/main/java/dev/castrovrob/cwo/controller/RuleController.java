package dev.castrovrob.cwo.controller;

import dev.castrovrob.cwo.rules.RuleService;
import dev.castrovrob.cwo.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rules")
@RequiredArgsConstructor
@Slf4j
public class RuleController {

    /**
     * Magic Number
     * HTTP Code 401 Unauthorized.
     */
    private static final int UNAUTHORIZED = 401;

    /**
     * Referencing the RuleService to simplify and
     * simplify dependency injection for tests.
     */
    private final RuleService ruleService;

    /**
     * Reference to the TokenService to simplify
     * tests.
     */
    private final TokenService tokenService;

    /**
     * Put HTTP method to update the rule message.
     * @param type Type of the rule i.e. hour, minute, second
     * @param newMessage Message to update. i.e. quak instead ot tick.
     * @param authHeader request token for our "super secure" API.
     * @return ResponseEntity with the status of the request.
     */
    @PutMapping("/{type}")
    public ResponseEntity<String> updateRuleMessage(
            @PathVariable final String type,
            @RequestParam final String newMessage,
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authHeader
    ) {
        log.debug("Updating rule message {} for type: {}", newMessage, type);

        if (!tokenService.validateToken(authHeader)) {
            log.warn("Invalid token: {}", authHeader);
            return ResponseEntity
                    .status(UNAUTHORIZED)
                    .body("Unauthorized: Invalid token");
        }

        boolean updated = ruleService.updateRule(type, newMessage);
        return updated
                ? ResponseEntity.ok("Rule updated successfully")
                : ResponseEntity.notFound().build();
    }
}
