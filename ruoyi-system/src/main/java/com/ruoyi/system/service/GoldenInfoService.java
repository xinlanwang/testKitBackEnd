package com.ruoyi.system.service;

import com.ruoyi.system.domain.dto.ImportGoldenInfoDTO;
import com.ruoyi.system.domain.dto.ImportPartComponentDTO;
import com.ruoyi.system.domain.param.BasicListParam;
import com.ruoyi.system.domain.vo.GoldenInfoVO;
import com.ruoyi.system.domain.vo.GoldenListVo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * 字典 业务层
 * 
 * @author ruoyi
 */
public interface GoldenInfoService
{

    public Map<String, Object> querygoldenList();

    public List<GoldenInfoVO> queryClusterInfo(String clusterName, String carlineModelType);

    public String importGoldenInfoDevice(List<ImportGoldenInfoDTO> importPartComponentDTOS, boolean b, String operName);
}
