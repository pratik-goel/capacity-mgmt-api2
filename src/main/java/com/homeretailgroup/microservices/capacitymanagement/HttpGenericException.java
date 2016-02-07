package com.homeretailgroup.microservices.capacitymanagement;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author pratik.goel
 *
 */
public class HttpGenericException {


    private long timestamp;
    private String status;
    private String message;
    private Object requestData;

    /**
     * Creates a simple exception object to respond with a formatted error
     * @param status HttpStatus code to send in the response
     * @param message Error message to display in the response
     * @param requestData Data used in the original request
     */
    public HttpGenericException(HttpStatus status, String message, Object requestData) {
        this.timestamp = System.currentTimeMillis()/1000;
        this.status = status.toString();
        this.message = message;
        this.requestData = requestData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getRequestData() {
        return requestData;
    }
}
