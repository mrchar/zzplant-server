package net.mrchar.zzplant.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceController {
    @Data
    public static class CommoditySchema {
        private String code;
        private String name;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal amount;
    }

    @Data
    public static class InvoiceSchema {
        private String code;
        private List<CommoditySchema> commodities;
        private BigDecimal amount;
        private String shopAccount;
        private String shop;
    }

    @GetMapping("/shop/{shopCode}/invoice")
    public List<InvoiceSchema> listInvoice(@PathVariable String shopCode) {
        // 列出订单
        return Collections.emptyList();
    }

    @Data
    public static class AddInvoiceRequest {
        private String accountCode;
        private List<CommoditySchema> commodities;
    }

    @GetMapping("/shop/{shopCode}/invoice")
    public InvoiceSchema addInvoice(@PathVariable String shopCode, @RequestBody AddInvoiceRequest request) {
        // 创建订单
        return null;
    }

    @PutMapping("/shop/{shopCode}/invoice/{invoiceCode}/commodities")
    public List<CommoditySchema> updateInvoice(@PathVariable String shopCode,
                                               @PathVariable String invoiceCode,
                                               @RequestBody List<CommoditySchema> request) {
        // 修改订单
        return Collections.emptyList();
    }

    @DeleteMapping("/shop/{shopCode}/invoice/{invoiceCode}")
    public void deleteInvoice(@PathVariable String shopCode, @PathVariable String invoiceCode) {
        // 删除订单
    }

    @Data
    public static class ConfirmInvoiceRequest {
        private String invoiceCode;
        private List<CommoditySchema> commodities;
        private BigDecimal amount;
    }

    // 确认订单
    @PostMapping("/shop/{shopCode}/payment")
    public InvoiceSchema confirmInvoice(@PathVariable String shopCode,
                                        @RequestBody ConfirmInvoiceRequest request) {
        // 确认支付
        return null;
    }

}
