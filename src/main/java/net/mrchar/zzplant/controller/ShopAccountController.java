package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.service.ShopAccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopAccountController {
    private final ShopAccountService shopAccountService;

    @Data
    public static class TopUpRequest {
        private BigDecimal amount;
    }

    @PostMapping("/shop/{shopCode}/account/{accountCode}/top-up")
    public void topUp(@PathVariable String shopCode, @PathVariable String accountCode, @RequestBody TopUpRequest topUpRequest) {
        this.shopAccountService.topUp(shopCode, accountCode, topUpRequest.getAmount());
    }
}
