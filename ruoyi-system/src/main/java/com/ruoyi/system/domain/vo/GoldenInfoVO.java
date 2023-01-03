package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("goldenInfo配件列表")
@Data
public class GoldenInfoVO {


    @ApiModelProperty("市场名称")
    private String marketType;
    @ApiModelProperty("数据库版本")
    private String dbVersion;

    private Long tCarlineInfoUid;

    @ApiModelProperty("配件列表")
    private Map<String, List<GoldenInfoComponentVO>> goldenInfoComponentMap;


}
