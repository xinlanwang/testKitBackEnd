package com.ruoyi.system.domain.enums;


public enum DeviceSelectMapping {


    clusterName("clusterName","cluster_name"),
    platformType("platformType","platform_type"),


    projectType("projectType","project_type"),


    variantType("variantType","variant_type"),
    deviceType("deviceType","device_type"),

    carlineModelType("carlineModelType","carline_model_type"),

    marketType("marketType","market_type");


    private final String code;
    private final String name;

    DeviceSelectMapping(String code, String name) {
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