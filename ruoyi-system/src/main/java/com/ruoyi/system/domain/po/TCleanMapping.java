package com.ruoyi.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.LocalBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * `BIGINT(32)`对象 t_matrix
 * 
 * @author ruoyi
 * @date 2022-11-21
 */
@TableName
@ApiModel
@Data
public class TCleanMapping extends LocalBaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Long uid;

    /** 混乱值 */
    @Excel(name = "混乱值")
    private String confuseData;

    /** 猜想值 */
    @Excel(name = "猜想值")
    private String guessValue;

    /** 使用场景 */
    @Excel(name = "使用场景")
    private String useScene;

    /** 创建者 */
    @Excel(name = "创建者")
    private String createByUid;

    /** 更新者 */
    @Excel(name = "更新者")
    private String updateByUid;

}
