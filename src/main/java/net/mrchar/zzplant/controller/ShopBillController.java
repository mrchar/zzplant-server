package net.mrchar.zzplant.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ForbiddenOperationException;
import net.mrchar.zzplant.exception.ResourceNotExistsException;
import net.mrchar.zzplant.model.ShopAssistant;
import net.mrchar.zzplant.model.ShopBill;
import net.mrchar.zzplant.model.ShopBillCommodity;
import net.mrchar.zzplant.model.ShopTransaction;
import net.mrchar.zzplant.repository.ShopBillRespository;
import net.mrchar.zzplant.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopBillController {
    private final ShopBillRespository shopBillRespository;
    private final ShopService shopService;

    @Data
    public static class ShopAssistantSchema {
        private String code;
        private String name;

        public ShopAssistantSchema(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static ShopAssistantSchema fromEntity(ShopAssistant entity) {
            return new ShopAssistantSchema(entity.getCode(), entity.getName());
        }
    }

    @Data
    public static class CommoditySchema {
        private String code;
        private String name;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal amount;

        public CommoditySchema(String code, String name, BigDecimal price, Integer quantity, BigDecimal amount) {
            this.code = code;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
            this.amount = amount;
        }

        public static CommoditySchema fromEntity(ShopBillCommodity entity) {
            return new CommoditySchema(
                    entity.getCode(),
                    entity.getName(),
                    entity.getPrice(),
                    entity.getQuantity(),
                    entity.getAmount()
            );
        }
    }

    @Data
    public static class ShopTransactionSchema {
        private String code;
        private BigDecimal amount;
        private BigDecimal previousBalance;
        private BigDecimal currentBalance;

        public ShopTransactionSchema(String code, BigDecimal amount, BigDecimal previousBalance, BigDecimal currentBalance) {
            this.code = code;
            this.amount = amount;
            this.previousBalance = previousBalance;
            this.currentBalance = currentBalance;
        }

        public static ShopTransactionSchema fromEntity(ShopTransaction entity) {
            return new ShopTransactionSchema(entity.getCode(),
                    entity.getAmount(),
                    entity.getPreviousBalance(),
                    entity.getCurrentBalance());
        }
    }

    @Data
    public static class ShopBillSchema {
        private String code;
        private String shop;
        private String shopAccount;
        private List<CommoditySchema> commodities;
        private BigDecimal amount;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ShopTransactionSchema transaction;
        private ShopAssistantSchema operator;
        private ZonedDateTime createDateTime;

        public ShopBillSchema(String code,
                              String shop, String shopAccount,
                              List<CommoditySchema> commodities, BigDecimal amount,
                              ShopTransactionSchema transaction,
                              ShopAssistantSchema operator, ZonedDateTime createDateTime) {
            this.code = code;
            this.shop = shop;
            this.shopAccount = shopAccount;
            this.commodities = commodities;
            this.amount = amount;
            this.transaction = transaction;
            this.operator = operator;
            this.createDateTime = createDateTime;
        }

        public static ShopBillSchema fromEntity(ShopBill entity) {
            List<CommoditySchema> commoditySchemas = entity.getCommodities().stream().map(CommoditySchema::fromEntity).collect(Collectors.toList());
            ShopTransactionSchema transaction = null;
            if (entity.getShopTransaction() != null) {
                transaction = ShopTransactionSchema.fromEntity(entity.getShopTransaction());
            }
            return new ShopBillSchema(
                    entity.getCode(),
                    entity.getShop().getCode(),
                    entity.getShopAccount().getCode(),
                    commoditySchemas,
                    entity.getAmount(),
                    transaction,
                    ShopAssistantSchema.fromEntity(entity.getOperator()),
                    entity.getCreateDateTime()
            );

        }
    }

    @GetMapping("/shops/{shopCode}/bill")
    @Transactional
    public Page<ShopBillSchema> listBills(@PathVariable String shopCode,
                                          @RequestParam(required = false) String keyword,
                                          Pageable pageable) {
        boolean exists = this.shopService.operatorIsAssistant(shopCode);
        if (!exists) {
            throw new ForbiddenOperationException("您没有权限执行这个操作");
        }

        if (StringUtils.hasText(keyword)) {
            Page<ShopBill> entities = this.shopBillRespository.searchBillByShopCodeAndKeyword(shopCode, keyword, pageable);
            return entities.map(ShopBillSchema::fromEntity);
        }

        Page<ShopBill> entities = this.shopBillRespository.findAllByShopCode(shopCode, pageable);
        return entities.map(ShopBillSchema::fromEntity);
    }

    @GetMapping("/shops/{shopCode}/bills/{billCode}")
    @Transactional
    public ShopBillSchema getBill(@PathVariable String shopCode, @PathVariable String billCode) {
        boolean exists = this.shopService.operatorIsAssistant(shopCode);
        if (!exists) {
            throw new ForbiddenOperationException("您没有权限执行这个操作");
        }

        ShopBill entity = this.shopBillRespository.findOneByShopCodeAndCode(shopCode, billCode)
                .orElseThrow(() -> new ResourceNotExistsException("订单不存在"));

        return ShopBillSchema.fromEntity(entity);
    }

    @GetMapping("/shops/{shopCode}/accounts/{accountCode}/bills")
    @Transactional
    public Page<ShopBillSchema> listBillsOfAccount(@PathVariable String shopCode,
                                                   @PathVariable String accountCode,
                                                   Pageable pageable) {
        Page<ShopBill> entities = this.shopBillRespository.findAllByShopCodeAndAccountCode(shopCode, accountCode, pageable);
        return entities.map(ShopBillSchema::fromEntity);
    }

    @Data
    public static class AddBillCommodityParams {
        private String code;
        private Integer quantity;
    }

    @Data
    public static class AddBillRequest {
        private String accountCode;
        private List<AddBillCommodityParams> commodities;
    }

    @PostMapping("/shops/{shopCode}/bills")
    public ShopBillSchema addBills(@PathVariable String shopCode, @RequestBody AddBillRequest request) {
        boolean exists = this.shopService.operatorIsAssistant(shopCode);
        if (!exists) {
            throw new ForbiddenOperationException("您没有权限执行这个操作");
        }

        Map<String, Integer> commodities = new HashMap<>();
        for (AddBillCommodityParams commodity : request.getCommodities()) {
            commodities.put(commodity.code, commodity.quantity);
        }

        ShopBill shopBill = this.shopService
                .addBill(shopCode, request.getAccountCode(), commodities);

        // 创建订单
        return ShopBillSchema.fromEntity(shopBill);
    }

    @PutMapping("/shops/{shopCode}/bills/{billCode}/commodities")
    public List<CommoditySchema> updateBill(@PathVariable String shopCode,
                                            @PathVariable String billCode,
                                            @RequestBody List<CommoditySchema> request) {
        // 修改订单
        return Collections.emptyList();
    }

    @DeleteMapping("/shops/{shopCode}/bills/{billCode}")
    public void deleteBill(@PathVariable String shopCode, @PathVariable String billCode) {
        this.shopService.deleteBill(shopCode, billCode);
    }

    @Data
    public static class ConfirmBillRequest {
        private String billCode;
        private List<AddBillCommodityParams> commodities;
        private BigDecimal amount;
    }

    // 确认订单
    @PostMapping("/shops/{shopCode}/payment")
    public ShopBillSchema confirmBill(@PathVariable String shopCode,
                                      @RequestBody ConfirmBillRequest request) {
        Map<String, Integer> commodities = new HashMap<>();
        for (AddBillCommodityParams commodity : request.getCommodities()) {
            commodities.put(commodity.code, commodity.quantity);
        }

        ShopBill entity = this.shopService.confirmBill(shopCode, request.getBillCode(), commodities, request.getAmount());
        // 确认支付
        return ShopBillSchema.fromEntity(entity);
    }
}
