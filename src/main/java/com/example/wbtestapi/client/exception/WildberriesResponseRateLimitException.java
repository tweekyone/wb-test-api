package com.example.wbtestapi.client.exception;

public class WildberriesResponseRateLimitException extends WildberriesResponseException {

    private static final long serialVersionUID = 1L;
    private static final String RETRY_AFTER_HEADER = "Retry-After";
    private static final long DEFAULT_RETRY_AFTER = 60L;

    private Long retryAfter = DEFAULT_RETRY_AFTER;

    public WildberriesResponseRateLimitException(String resp) {
            super(resp);
        }

    public WildberriesResponseRateLimitException(WildberriesResponseRateLimitException e) {
            super(e);
            this.retryAfter = e.getRetryAfter();
        }

    public Long getRetryAfter() {
        return retryAfter;
    }
}
