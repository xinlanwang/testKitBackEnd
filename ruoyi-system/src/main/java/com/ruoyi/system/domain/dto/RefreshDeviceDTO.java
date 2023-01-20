package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.vo.DeviceInfoComponent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * devicelist
 *
 * @author ruoyi
 */
@ApiModel(value = "DeviceInfoVo",description = "设备信息详情展示")
@Data
public class RefreshDeviceDTO
{
   private static final long serialVersionUID = 1L;
   @ApiModelProperty("当前设备uid，新增不填，修改必填")
   private Long carlineInfoUid;
   private Date updateTime;

}
