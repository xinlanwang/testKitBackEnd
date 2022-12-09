package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * devicelist
 *
 * @author ruoyi
 */
@ApiModel(value = "DeviceInfoVo",description = "设备信息详情展示")
@Data
public class DeviceInfoVo
{
   private static final long serialVersionUID = 1L;
   @ApiModelProperty("当前设备uid，新增不填，修改必填")
   private Long carlineInfoUid;
   private Long originalCarlineInfoUid;
   @ApiModelProperty("设备类型：bc代表bench，car代表car，gd代表目标goldeninfo")
   private String carlineType;
   @ApiModelProperty("版本规范车族名称，对应devicesName")
   private String goldenCarName;
   @ApiModelProperty
   private String versionCode;
   private String deviceName;
   @ApiModelProperty("版本名称，如cl34，cl37等")
   private String clusterName;
   @ApiModelProperty("工程名称")
   private String projectType;
   @ApiModelProperty("平台类型")
   private String platformType;
   @ApiModelProperty("市场类型，cn代表中国，jp代表日本")
   private String marketType;
   @ApiModelProperty("车型名称")
   private String carlineModelType;
   @ApiModelProperty("variantType")
   private String variantType;
   @ApiModelProperty("vin码")
   private String vinCode;
   @ApiModelProperty("数据库版本")
   private String dbVersion;
   @ApiModelProperty("clusterLastUpdateName")
   private String clusterLastUpdateName;
   @ApiModelProperty("basicType")
   private String basicType;


   @ApiModelProperty("设备组件列表")
   private List<DeviceInfoComponent> deviceInfoComponents;
   private Map<String,DeviceInfoComponent> deviceInfoComponentMap;


}
