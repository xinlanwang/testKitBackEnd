package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * devicelist
 * 
 * @author ruoyi
 */

@ApiModel(value = "GoldenListVo",description = "版本信息列表")
public class GoldenListVo
{
   @ApiModelProperty("版本id")
   private Long clusterUid;
   @ApiModelProperty("版本名称，如cl35，cl37，cl39等")
   private String clusterName;


   @ApiModelProperty("平台列表")
   private Map<String,GoldenListPlatfromVO> platfromList;

   public GoldenListVo() {
   }

   public GoldenListVo(Long clusterUid, String clusterName, List<Map<String, List>> platfromList) {
      this.clusterUid = clusterUid;
      this.clusterName = clusterName;
   }



   public Long getClusterUid() {
      return clusterUid;
   }

   public void setClusterUid(Long clusterUid) {
      this.clusterUid = clusterUid;
   }

   public String getClusterName() {
      return clusterName;
   }

   public void setClusterName(String clusterName) {
      this.clusterName = clusterName;
   }

   public Map<String, GoldenListPlatfromVO> getPlatfromList() {
      return platfromList;
   }

   public void setPlatfromList(Map<String, GoldenListPlatfromVO> platfromList) {
      this.platfromList = platfromList;
   }
}
