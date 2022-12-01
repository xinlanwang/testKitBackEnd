package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@ApiModel(value = "Platfrom",description = "平台")
@Data
public class GoldenListPlatfromVO {

    private String carlineInfoUid;
    private String goldenCarType;
    private String carlineModelType;
    private Integer marketType;
    private LinkedHashSet<Integer> marketTypes;

}
