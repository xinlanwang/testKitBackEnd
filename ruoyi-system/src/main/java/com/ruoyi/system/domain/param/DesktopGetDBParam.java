package com.ruoyi.system.domain.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel(value = "DesktopSubmitParam",description = "桌面云端同步参数")
@Data
public class DesktopGetDBParam {
//private String tableName;
private String[] tableNames;
}
