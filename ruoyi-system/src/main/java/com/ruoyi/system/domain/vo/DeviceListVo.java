package com.ruoyi.system.domain.vo;

import com.ruoyi.common.utils.StringUtils;
import lombok.Data;

/**
 * devicelist
 * 
 * @author ruoyi
 */
@Data
public class DeviceListVo
{

   private String carlineType;

   private Long carlineInfoUid;
   private String carlineIcon;
   private String goldenCarName;

   private String clusterName;

   private String projectType;

   private String platformType;
   private String marketType;
   private String carlineModelType;
   private String variantType;
   private String vinCode;

}
