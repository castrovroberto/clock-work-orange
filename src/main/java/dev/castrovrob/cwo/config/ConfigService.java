package dev.castrovrob.cwo.config;

public interface ConfigService {

    /**
     * Get the determined duration of the intended run duration of
     * the application.
     * @return the duration of the expected run.
     */
    int getDuration();

    /**
     * Gets the rudimentary security default configuration token.
     * @return the default auth token.
     */
    String getToken();
}
