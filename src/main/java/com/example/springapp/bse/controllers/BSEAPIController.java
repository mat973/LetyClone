package com.example.springapp.bse.controllers;

import com.example.springapp.Util;
import com.example.springapp.bse.messages.*;
import com.example.springapp.bse.model.*;
import com.example.springapp.bse.services.OrderService;
import com.example.springapp.letyclone.messages.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bse/api")
public class BSEAPIController {
    @Autowired
    OrderService orderService;

    @GetMapping("products")
    public GetProductsResponse getProducts() {
        return GetProductsResponse.ok(orderService.getProducts());
    }

    @PostMapping("order")
    public CreateOrderResponse createOrder(HttpServletRequest req, @RequestBody CreateOrderRequest reqBody) {
        try {
            return CreateOrderResponse.ok(orderService.createOrder(reqBody.productIds, reqBody.lcUserId, Util.resolveUrl(req, "/partner_api/user_created_order")));
        } catch (RuntimeException ex) {
            return CreateOrderResponse.error(ex.getLocalizedMessage());
        }
    }

    @GetMapping("order")
    public GetOrderInfoResponse getOrderInfo(@RequestParam(required = false) Integer id) {
        try {
            return GetOrderInfoResponse.ok(orderService.getOrderInfo(id));
        } catch (RuntimeException ex) {
            return GetOrderInfoResponse.error(ex.getLocalizedMessage());
        }
    }

    @PostMapping("refund")
    public RefundResponse getRefund(HttpServletRequest req, @RequestBody RefundRequest reqBody) {
        try {
            OrderedItem item = orderService.getRefund(reqBody.itemId, reqBody.lcUserId, Util.resolveUrl(req, "/partner_api/item_refunded"));
            return RefundResponse.ok(item, item.getOrder().getId());
        } catch (RuntimeException ex) {
            return RefundResponse.error(ex.getLocalizedMessage());
        }
    }

    @PostMapping("nonrefundable")
    public NonRefundableResponse makeNonRefundable(HttpServletRequest req, @RequestBody NonRefundableRequest reqBody) {
        try {
            orderService.makeNonRefundable(reqBody.orderId, Util.resolveUrl(req, "/partner_api/items_nonrefundable"));
            return NonRefundableResponse.ok(reqBody.orderId);
        } catch (RuntimeException ex) {
            return NonRefundableResponse.error(ex.getLocalizedMessage());
        }
    }

    @GetMapping("orders")
    public GetOrdersResponse getOrders() {
        try {
            return GetOrdersResponse.ok(orderService.getOrders());
        } catch (RuntimeException ex) {
            return GetOrdersResponse.error(ex.getLocalizedMessage());
        }
    }
}
