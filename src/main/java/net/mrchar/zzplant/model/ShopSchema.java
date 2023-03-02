package net.mrchar.zzplant.model;

import lombok.Data;

@Data
public class ShopSchema {
    private String code;
    private String name;
    private String address;
    private String owner;
    private String company;

    public ShopSchema() {
    }

    public ShopSchema(String code, String name, String address) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.owner = owner;
    }

    public static ShopSchema fromEntity(Shop entity) {
        ShopSchema schema = new ShopSchema(
                entity.getCode(),
                entity.getName(),
                entity.getAddress());
        if (entity.getOwner() != null) {
            schema.setOwner(entity.getOwner().getName());
        }
        if (entity.getCompany() != null) {
            schema.setCompany(entity.getCompany().getName());
        }
        return schema;
    }
}
