package com.example.springapp.bse.messages;

import com.example.springapp.bse.model.Order;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetOrdersResponse {
    public String status;
    public String error;
    public List<Order> orders;

    public static GetOrdersResponse ok(List<Order> orders) {
        GetOrdersResponse r = new GetOrdersResponse();
        r.status = "ok";
        r.orders = orders;
        return r;
    }
    public static GetOrdersResponse error(String message) {
        GetOrdersResponse r = new GetOrdersResponse();
        r.status = "error";
        r.error = message;
        return r;
    }
}
