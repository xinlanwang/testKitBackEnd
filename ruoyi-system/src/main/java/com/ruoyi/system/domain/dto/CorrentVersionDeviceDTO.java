package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * devicelist
 *
 * @author ruoyi
 */
@ApiModel(value = "DeviceInfoVo",description = "设备信息详情展示")
@Data
public class CorrentVersionDeviceDTO
{
   private static final long serialVersionUID = 1L;
   @ApiModelProperty("当前设备uid，新增不填，修改必填")
   private Long carlineInfoUid;
   private Long clusterUid;
   private Integer deviceCircleNum;
   private Date updateTime;

}
