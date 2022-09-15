package com.example.springapp.bse.messages;

import com.example.springapp.bse.model.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetOrderInfoResponse {
    public String status;
    public String error;
    public Order order;

    public static GetOrderInfoResponse ok(Order order) {
        GetOrderInfoResponse r = new GetOrderInfoResponse();
        r.status = "ok";
        r.order = order;
        return r;
    }
    public static GetOrderInfoResponse error(String message) {
        GetOrderInfoResponse r = new GetOrderInfoResponse();
        r.status = "error";
        r.error = message;
        return r;
    }
}
