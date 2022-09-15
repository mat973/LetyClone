package com.example.springapp.letyclone.messages;

import com.example.springapp.letyclone.model.Shop;
import com.example.springapp.letyclone.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserResponse {
    public String status;
    public String error;

    public static GetUserResponse ok() {
        GetUserResponse r = new GetUserResponse();
        r.status = "ok";
        return r;
    }

    public static GetUserResponse error(String message) {
        GetUserResponse r = new GetUserResponse();
        r.status = "error";
        r.error = message;
        return r;
    }
}
