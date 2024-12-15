package com.retailedge.utils.ExceptionHandler;

import org.springframework.stereotype.Component;

@Component
public class ExceptionHandlerUtil {

    public String sanitizeErrorMessage(String errorMessage) {
        int dbDetailsIndex = errorMessage.indexOf("[insert into products");
        if (dbDetailsIndex != -1) {

            return errorMessage.substring(0, dbDetailsIndex).trim();
        }

        return errorMessage;
    }
}
