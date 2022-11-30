package com.ruoyi.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel("goldenInfo配件列表")
public class GoldenInfoVO {


    @ApiModelProperty("市场名称")
    private String marketType;
    @ApiModelProperty("数据库版本")
    private String dbVersion;

    private Long tCarlineInfoUid;

    @ApiModelProperty("配件列表")
    private Map<String, List<GoldenInfoComponentVO>> goldenInfoComponentMap;

    public GoldenInfoVO() {
    }

    public GoldenInfoVO(String marketType, String dbVersion, List<Map<String, List<GoldenInfoComponentVO>>> goldenInfoComponents) {
        this.marketType = marketType;
        this.dbVersion = dbVersion;
    }

    public Map<String, List<GoldenInfoComponentVO>> getGoldenInfoComponentMap() {
        return goldenInfoComponentMap;
    }

    public void setGoldenInfoComponentMap(Map<String, List<GoldenInfoComponentVO>> goldenInfoComponentMap) {
        this.goldenInfoComponentMap = goldenInfoComponentMap;
    }

    public Long gettCarlineInfoUid() {
        return tCarlineInfoUid;
    }

    public void settCarlineInfoUid(Long tCarlineInfoUid) {
        this.tCarlineInfoUid = tCarlineInfoUid;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

}
