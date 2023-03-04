package net.mrchar.zzplant.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.mrchar.zzplant.exception.ForbiddenOperationException;
import net.mrchar.zzplant.exception.UnExpectedException;
import net.mrchar.zzplant.model.ShopInvoice;
import net.mrchar.zzplant.model.ShopInvoiceCommodity;
import net.mrchar.zzplant.model.User;
import net.mrchar.zzplant.repository.ShopInvoiceRepository;
import net.mrchar.zzplant.repository.ShopRepository;
import net.mrchar.zzplant.repository.UserRepository;
import net.mrchar.zzplant.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InvoiceController {
    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ShopInvoiceRepository invoiceRepository;
    private final ShopService shopService;

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

        public static CommoditySchema fromEntity(ShopInvoiceCommodity entity) {
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
    public static class InvoiceSchema {
        private String code;
        private List<CommoditySchema> commodities;
        private BigDecimal amount;
        private String shopAccount;
        private String shop;

        public InvoiceSchema(String code, List<CommoditySchema> commodities, BigDecimal amount, String shopAccount, String shop) {
            this.code = code;
            this.commodities = commodities;
            this.amount = amount;
            this.shopAccount = shopAccount;
            this.shop = shop;
        }

        public static InvoiceSchema fromEntity(ShopInvoice entity) {
            List<CommoditySchema> commoditySchemas = entity.getCommodities().stream().map(CommoditySchema::fromEntity).collect(Collectors.toList());
            return new InvoiceSchema(
                    entity.getCode(),
                    commoditySchemas,
                    entity.getAmount(),
                    entity.getAccount().getCode(),
                    entity.getShop().getCode());
        }
    }

    @GetMapping("/shops/{shopCode}/invoices")
    @Transactional
    public Page<InvoiceSchema> listInvoice(@PathVariable String shopCode, Pageable pageable) {
        boolean exists = ifOperatorIsShopOwner(shopCode);
        if (!exists) {
            throw new ForbiddenOperationException("只有当前店铺的所有着才可以进行操作");
        }

        Page<ShopInvoice> entities = this.invoiceRepository.findAllByShopCode(shopCode, pageable);
        return entities.map(InvoiceSchema::fromEntity);
    }


    @Data
    public static class AddInvoiceRequest {
        private String accountCode;
        private Map<String, Integer> commodities;
    }

    @PostMapping("/shops/{shopCode}/invoices")
    public InvoiceSchema addInvoice(@PathVariable String shopCode, @RequestBody AddInvoiceRequest request) {
        boolean exists = ifOperatorIsShopOwner(shopCode);
        if (!exists) {
            throw new ForbiddenOperationException("只有当前店铺的所有着才可以进行操作");


        }

        ShopInvoice shopInvoice = this.shopService
                .addInvoice(shopCode, request.getAccountCode(), request.getCommodities());

        // 创建订单
        return InvoiceSchema.fromEntity(shopInvoice);
    }

    @PutMapping("/shops/{shopCode}/invoices/{invoiceCode}/commodities")
    public List<CommoditySchema> updateInvoice(@PathVariable String shopCode,
                                               @PathVariable String invoiceCode,
                                               @RequestBody List<CommoditySchema> request) {
        // 修改订单
        return Collections.emptyList();
    }

    @DeleteMapping("/shops/{shopCode}/invoices/{invoiceCode}")
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
    @PostMapping("/shops/{shopCode}/payment")
    public InvoiceSchema confirmInvoice(@PathVariable String shopCode,
                                        @RequestBody ConfirmInvoiceRequest request) {
        // 确认支付
        return null;
    }

    private boolean ifOperatorIsShopOwner(String shopCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnExpectedException("获取不到当前正在操作的用户的信息");
        }

        String accountName = authentication.getName();
        User owner = this.userRepository.findOneByAccountName(accountName)
                .orElseThrow(() -> new UnExpectedException("获取不到当前正在操作的用户的信息"));

        boolean exists = this.shopRepository.existsByShopCodeAndOwner(shopCode, owner);
        return exists;
    }

}
