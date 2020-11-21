package com.putoet.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

class ApiErrorResponseTest {

    @Test
    void getStatusCode() {
        final ApiError apiError =new ApiError(SERVICE_UNAVAILABLE);
        final ApiErrorResponse response = ApiErrorResponse.of(apiError);
        assertThat(response.getStatusCode()).isEqualTo(SERVICE_UNAVAILABLE);
        assertThat(response.getEpiError()).isEqualTo(apiError);
    }
}