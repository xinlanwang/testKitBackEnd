package com.ruoyi.system.domain.enums;


public enum MatrixSelectMapping {


    clusterNames("clusterNames", "cluster_name","clusterName"),
    goldenCarTypes("goldenCarTypes", "golden_car_type","goldenCarType"),
    goldenClusterNameTypes("goldenClusterNameTypes", "golden_cluster_name_type","goldenClusterNameType"),


    platformTypes("platformTypes", "platform_type","platformType"),


    projectTypes("projectTypes", "project_type","projectType"),


    variantTypes("variantTypes", "variant_type","variantType"),

    carlineModelTypes("carlineModelTypes", "carline_model_type","carlineModelType"),

    marketTypes("marketTypes", "market_types","marketTypes"),
    ocuCboxTypes("ocuCboxTypes", "ocu_cbox_type","ocuCboxType"),
    gatewayTypes("gatewayTypes", "gateway_type","gatewayType");


    private final String code;
    private final String name;
    private final String singleName;

    MatrixSelectMapping(String code, String name, String singleName) {
        this.code = code;
        this.name = name;
        this.singleName = singleName;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    public String getSingleName() {
        return singleName;
    }
}