package com.thesimego.fximguruploader.entity.imgur;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Basic {

    private Object data;
    
    private Boolean success;
    
    private Integer status;
    
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}