package com.ruoyi.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "DashboardParam",description = "数据表盘参数")
@Data
public class DashboardParam extends BaseEntity {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty("设备类型：bench，car，或goldeninfo")
        private String deviceType;

        /** 版本号名称 */
        @ApiModelProperty("版本号名称")
        private String[] clusterNames;
        /** 平台类型 */
        @ApiModelProperty("平台类型")
        private String[] platformTypes;
        /** 区域中国（zn）台湾(z1)香港(z2)北美(na0)东南亚(as0)，逗号分割 */
        @ApiModelProperty("区域名称")
        private String[] marketTypes;
        /** 工程id */
        @ApiModelProperty("工程id")
        private String[] projectTypes;
        /** variant */
        @ApiModelProperty("variantTypes")
        private String[] variantTypes;
        /** 汽车名称 */
        @ApiModelProperty("汽车模型名称类型")
        private String[] carlineModelTypes;
        //模糊项
        @ApiModelProperty("Component模糊搜索必填字段：组件类型：mu代表mu，gw代表gw，hud代表hud，km代表kombi等")
        private String componentType;
        @ApiModelProperty("Component模糊搜索选填字段：软件版本")
        private String swVersion;
        @ApiModelProperty("Component模糊搜索选填字段：硬件版本")
        private String hwVersion;
        private String wareType;
        private String componentVersion;
        @ApiModelProperty("deviceName模糊搜索")
        private String deviceName;
        @ApiModelProperty("vin模糊搜索")
        private String vinCode;
        @ApiModelProperty("最近更新")
        private String lastUpdated;
        @ApiModelProperty("数据库版本")
        private String dbVersion;


        //record项
        /** functionGroupType */
        @Excel(name = "functionGroupType")
        private String functionGroupType;
        @ApiModelProperty
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date testStartDate;
        @ApiModelProperty
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date testEndDate;
}
