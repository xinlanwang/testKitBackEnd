package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ImportPartComponentDTO {
    @ApiModelProperty("组件类型：mu代表mu，gw代表gw，hud代表hud，km代表kombi等")
    @Excel(name = "COM", type = Excel.Type.IMPORT)
    private String COMPONENTS;
    @ApiModelProperty("序列号")
    @Excel(name = "PART", type = Excel.Type.IMPORT)
    private String PARTNUMBER;
    //这三可能是一个玩意儿 Start
    @ApiModelProperty("版本名称，对应SOP")
    @Excel(name = "SOP", type = Excel.Type.IMPORT)
    private String SOP;
    @ApiModelProperty("车型名称")
    @Excel(name = "CARLINE", type = Excel.Type.IMPORT)
    private String CARLINE;
    @ApiModelProperty("车型名称")
    @Excel(name = "SOPCARLINE", type = Excel.Type.IMPORT)
    private String SOPCARLINE;
    //这三可能是一个玩意儿 End
    @Excel(name = "HW", type = Excel.Type.IMPORT)
    @ApiModelProperty("最低配置")
    private String HWVERSION;
    @Excel(name = "SW", type = Excel.Type.IMPORT)
    private String SWVERSION;
    @Excel(name = "DB", type = Excel.Type.IMPORT)
    private String DBVERSION;
    @Excel(name = "MINI", type = Excel.Type.IMPORT)
    private String MINIMALHW;
    @Excel(name = "COMMENT", type = Excel.Type.IMPORT)
    private String NOTESCOMMENTS;

    private Integer rowNum;

    @Override
    public String toString() {
        return "\nExportPartComponentDTO{" +
                "COMPONENTS='" + COMPONENTS + '\'' +
                ", PARTNUMBER='" + PARTNUMBER + '\'' +
                ", SOP='" + SOP + '\'' +
                ", HWVERSION='" + HWVERSION + '\'' +
                ", SWVERSION='" + SWVERSION + '\'' +
                ", DBVERSION='" + DBVERSION + '\'' +
                ", MINIMALHW='" + MINIMALHW + '\'' +
                ", NOTESCOMMENTS='" + NOTESCOMMENTS + '\'' +
                '}';
    }
}
