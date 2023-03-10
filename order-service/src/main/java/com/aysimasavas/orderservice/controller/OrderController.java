package com.aysimasavas.orderservice.controller;

import com.aysimasavas.orderservice.dto.OrderRequest;
import com.aysimasavas.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "order place succesfully";
    }

    public String fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException){
        return "something went wrong, please order after some time!!!!!";
    }
}
