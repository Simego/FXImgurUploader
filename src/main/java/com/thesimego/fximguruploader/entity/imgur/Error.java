package com.thesimego.fximguruploader.entity.imgur;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author drafaelli
 */
public class Error {

    private String error;
    
    private String request;
    
    private String method;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
}
