package com.putoet.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiErrorResponse {
    private ApiError epiError;

    public static ApiErrorResponse of(ApiError apiError) {
        final ApiErrorResponse response =  new ApiErrorResponse();
        response.epiError = apiError;

        return response;
    }

    public HttpStatus getStatusCode() {
        return epiError.getStatus();
    }
}
