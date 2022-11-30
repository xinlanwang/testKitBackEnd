package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("goldenInfo配件列表")
public class GoldenInfoComponentVO {


    @ApiModelProperty("序列号")
    private String partNumber;
    @ApiModelProperty("版本名称，对应SOP_Carline")
    private String clusterName;
    @ApiModelProperty("软件配件名称，对应SW version")
    private String hwComponentVersion;
    @ApiModelProperty("硬件配件名称，对应HW version")
    private String swComponentVersion;
    private String temporaryVariable;
    @ApiModelProperty("最低配置")
    private String minimalHW;

    private String componentType;


    public GoldenInfoComponentVO(String partNumber, String clusterName, String hwComponentVersion, String swComponentVersion, String minimalHW) {
        this.partNumber = partNumber;
        this.clusterName = clusterName;
        this.hwComponentVersion = hwComponentVersion;
        this.swComponentVersion = swComponentVersion;
        this.minimalHW = minimalHW;
    }

    public String getHwComponentVersion() {
        return hwComponentVersion;
    }

    public void setHwComponentVersion(String hwComponentVersion) {
        this.hwComponentVersion = hwComponentVersion;
    }

    public String getSwComponentVersion() {
        return swComponentVersion;
    }

    public void setSwComponentVersion(String swComponentVersion) {
        this.swComponentVersion = swComponentVersion;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }


    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }


    public String getMinimalHW() {
        return minimalHW;
    }

    public void setMinimalHW(String minimalHW) {
        this.minimalHW = minimalHW;
    }

    public GoldenInfoComponentVO() {
    }



}
