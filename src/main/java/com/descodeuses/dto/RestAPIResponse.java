package com.descodeuses.dto;

import lombok.Data;

@Data
public class RestAPIResponse {
    private int status;
    private String message;

    public RestAPIResponse(int status, String msg) {
        this.status = status;
        this.message = msg;
    }
}
