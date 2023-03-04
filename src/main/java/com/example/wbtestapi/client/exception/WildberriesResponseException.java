package com.example.wbtestapi.client.exception;

public class WildberriesResponseException extends WildberriesException {

    private static final long serialVersionUID = 1L;

    private int statusCode;
    private String statusText;
    private String body;

    public WildberriesResponseException(String resp) {
        super(resp);
    }

    public WildberriesResponseException(WildberriesResponseException cause) {
        super(cause.getMessage(), cause);
        this.statusCode = cause.getStatusCode();
        this.statusText = cause.getStatusText();
        this.body = cause.getBody();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getBody() {
        return body;
    }
}
