package com.ruoyi.system.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;


import java.util.List;

@ApiModel
@Data
public class ImportGoldenInfoDTO {
    private List<ImportPartComponentDTO> componentDTOS;
    private String marketDetail;
    private String sheetName;
}
