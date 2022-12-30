package com.ruoyi.system.domain.enums;


public enum VariantTypeMapping {


    BASIC("BASIC", "Basic"),
    MEDIUM("MEDIUM", "Medium"),
    HIGH("HIGH", "High"),
    PREMIUM("PREMIUM", "Premium"),
    VARIANT1("VARIANT1", "Variant 1"),
    BASE("BASE", "Base"),
    ADVANCE("ADVANCE", "Advance");

    private final String code;
    private final String name;

    VariantTypeMapping(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}