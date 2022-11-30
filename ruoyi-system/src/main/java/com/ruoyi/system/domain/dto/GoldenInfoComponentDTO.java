package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("goldenInfo配件列表")
@Data
public class GoldenInfoComponentDTO {


    private String temporaryVariable;
    @ApiModelProperty("软件配件名称，对应SW version")
    private String hwComponentVersion;
    @ApiModelProperty("硬件配件名称，对应HW version")
    private String swComponentVersion;

    @ApiModelProperty("序列号")
    private String partNumber;
    @ApiModelProperty("版本名称，对应SOP_Carline")
    private String clusterName;
    @ApiModelProperty("最低配置")
    private String minimalHW;

    private String componentType;

    private String componentModel;

    private String wareType;

    public GoldenInfoComponentDTO(String partNumber, String clusterName, String hwComponentVersion, String swComponentVersion, String minimalHW) {
        this.partNumber = partNumber;
        this.clusterName = clusterName;
        this.minimalHW = minimalHW;
    }


    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentModel() {
        return componentModel;
    }

    public void setComponentModel(String componentModel) {
        this.componentModel = componentModel;
    }

    public String getWareType() {
        return wareType;
    }

    public void setWareType(String wareType) {
        this.wareType = wareType;
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

    public GoldenInfoComponentDTO() {
    }



}
