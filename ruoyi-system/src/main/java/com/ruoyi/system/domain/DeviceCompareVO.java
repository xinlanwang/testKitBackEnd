package com.ruoyi.system.domain;

import lombok.Data;

@Data
public class DeviceCompareVO {
    //1为小于最低版本，2为大于最低版本低于正常版本，3为正常
    private Integer compareNum;
    @Deprecated
    private String currentVersion;
    private String minimal;
    private Integer goldenSort;
    private String goldenVersion;
    private Integer deviceSort;
    private String variantType;
    private String deviceComponentVersion;
}
