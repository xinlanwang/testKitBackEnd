package com.ruoyi.system.domain.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicListParam {
        private Integer pageNum;
        private Integer pageSize;

}
