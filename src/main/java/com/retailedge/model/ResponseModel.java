package com.retailedge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel<T> {
    private String message;
    private boolean success;
    private Integer statusCode;
    private T data;
    public ResponseModel(boolean success, String message, Integer statusCode) {
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;
    }

    public ResponseModel(boolean success, String message, Integer statusCode,  T data) {
        this.message = message;
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
    }
}
