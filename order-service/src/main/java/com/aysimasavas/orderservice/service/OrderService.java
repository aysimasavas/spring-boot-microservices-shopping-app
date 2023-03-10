package com.aysimasavas.orderservice.service;

import com.aysimasavas.orderservice.dto.InventoryResponse;
import com.aysimasavas.orderservice.dto.OrderLineItemsDto;
import com.aysimasavas.orderservice.dto.OrderRequest;
import com.aysimasavas.orderservice.model.Order;
import com.aysimasavas.orderservice.model.OrderLineItems;
import com.aysimasavas.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequest){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems=orderRequest.getOrderLineItemsDtoList()
            .stream()
            .map(this::mapToDto)
            .toList();

        order.setOrderLineItemsList(orderLineItems);
        List<String> skuCodes = order.getOrderLineItemsList().stream()
            .map(OrderLineItems::getSkuCode)
            .toList();

        //inventory service çağrılır,eğer ürün varsa sipariş verilir
        InventoryResponse[] inventoryResponses= webClientBuilder.build().get()
            .uri("http://inventory-service/api/inventory",uriBuilder -> uriBuilder
                .queryParam("skuCode",skuCodes).build())
                .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                        .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses)
            .allMatch(inventoryResponse -> inventoryResponse.isInStock());
        if(allProductsInStock){
            orderRepository.save(order);
        }
        else{
            throw new IllegalArgumentException("Ürün stokta yok");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
       OrderLineItems orderLineItems= new OrderLineItems();
       orderLineItems.setPrice(orderLineItemsDto.getPrice());
       orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
       orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
       return orderLineItems;
    }
}
