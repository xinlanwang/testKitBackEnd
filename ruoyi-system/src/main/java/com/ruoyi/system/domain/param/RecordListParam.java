package com.ruoyi.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * `BIGINT(32) auto_increment`对象 t_desktop_log
 * 
 * @author ruoyi
 * @date 2022-12-08
 */
@Data
@ApiModel
public class RecordListParam
{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty
    private String singleOrderStr;//单项选择排序
    @ApiModelProperty
    private Boolean singleOrderByASC;//正序排序
    private String dictTypeTable;

    @ApiModelProperty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testStartDate;
    @ApiModelProperty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date testEndDate;
    @ApiModelProperty
    private String tester;
    @ApiModelProperty
    private String functionGroupType;
    @ApiModelProperty
    private String deviceType;
    private String deviceName;

    private String dictTypeUnderLine;
    private String dictTypeHump;


}
