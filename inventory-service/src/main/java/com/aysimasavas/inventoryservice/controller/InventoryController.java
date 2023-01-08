package com.aysimasavas.inventoryservice.controller;

import com.aysimasavas.inventoryservice.dto.InventoryResponse;
import com.aysimasavas.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }
}
