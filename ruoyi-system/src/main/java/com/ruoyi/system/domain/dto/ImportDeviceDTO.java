package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ImportDeviceDTO {
    private String sheetName;
    @ApiModelProperty("组件类型：mu代表mu，gw代表gw，hud代表hud，km代表kombi等")
    @Excel(name = "Device", type = Excel.Type.IMPORT)
    private String DeviceName;
    @ApiModelProperty("序列号")
    @Excel(name = "CLU", type = Excel.Type.IMPORT)
    private String CLU;
    @ApiModelProperty("版本名称，对应SOP_Carline")
    @Excel(name = "Project", type = Excel.Type.IMPORT)
    private String Project;
    @Excel(name = "Platform", type = Excel.Type.IMPORT)
    @ApiModelProperty("最低配置")
    private String Platform;
    @Excel(name = "Market", type = Excel.Type.IMPORT)
    private String Market;
    @Excel(name = "Carline", type = Excel.Type.IMPORT)
    private String Carline;
    @Excel(name = "Variant", type = Excel.Type.IMPORT)
    private String VariantType;
    @Excel(name = "VIN", type = Excel.Type.IMPORT)
    private String VIN;
    @Excel(name = "MU SW", type = Excel.Type.IMPORT)
    private String MUSW;
    @Excel(name = "MU HW", type = Excel.Type.IMPORT)
    private String MUHW;
    @Excel(name = "Asterix SW", type = Excel.Type.IMPORT)
    private String AsterixSW;
    @Excel(name = "Asterix HW", type = Excel.Type.IMPORT)
    private String AsterixHW;
    @Excel(name = "Kombi SW", type = Excel.Type.IMPORT)
    private String KombiSW;
    @Excel(name = "Kombi HW", type = Excel.Type.IMPORT)
    private String KombiHW;
    @Excel(name = "GatewayHW", type = Excel.Type.IMPORT)
    private String GatewayHW;
    @Excel(name = "GatewaySW", type = Excel.Type.IMPORT)
    private String GatewaySW;
    @Excel(name = "Conbox/OCUSW", type = Excel.Type.IMPORT)
    private String ConboxOCUSW;
    @Excel(name = "Conbox/OCU HW", type = Excel.Type.IMPORT)
    private String ConboxOCUHW;
    @Excel(name = "DB", type = Excel.Type.IMPORT)
    private String DB;
    @Excel(name = "Last Updated", type = Excel.Type.IMPORT)
    private String LastUpdated;
    @Excel(name = "Resp.", type = Excel.Type.IMPORT)
    private String resp;
}
