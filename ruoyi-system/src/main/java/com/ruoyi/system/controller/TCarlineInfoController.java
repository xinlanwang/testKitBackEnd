package com.ruoyi.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.po.TCarlineInfo;
import com.ruoyi.system.service.ITCarlineInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `虚拟设备参数`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/device/info")
public class TCarlineInfoController extends BaseController
{
    @Autowired
    private ITCarlineInfoService tCarlineInfoService;

    /**
     * 查询`虚拟设备参数`列表
     */
    @PreAuthorize("@ss.hasPermi('device:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCarlineInfo tCarlineInfo)
    {
        startPage();
        List<TCarlineInfo> list = tCarlineInfoService.selectTCarlineInfoList(tCarlineInfo);
        return getDataTable(list);
    }

    /**
     * 导出`虚拟设备参数`列表
     */
    @PreAuthorize("@ss.hasPermi('device:info:export')")
    @Log(title = "`虚拟设备参数`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCarlineInfo tCarlineInfo)
    {
        List<TCarlineInfo> list = tCarlineInfoService.selectTCarlineInfoList(tCarlineInfo);
        ExcelUtil<TCarlineInfo> util = new ExcelUtil<TCarlineInfo>(TCarlineInfo.class);
        util.exportExcel(response, list, "`虚拟设备参数`数据");
    }

    /**
     * 获取`虚拟设备参数`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:info:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tCarlineInfoService.selectTCarlineInfoByUid(uid));
    }

    /**
     * 新增`虚拟设备参数`
     */
    @PreAuthorize("@ss.hasPermi('device:info:add')")
    @Log(title = "`虚拟设备参数`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCarlineInfo tCarlineInfo)
    {
        return toAjax(tCarlineInfoService.insertTCarlineInfo(tCarlineInfo));
    }

    /**
     * 修改`虚拟设备参数`
     */
    @PreAuthorize("@ss.hasPermi('device:info:edit')")
    @Log(title = "`虚拟设备参数`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCarlineInfo tCarlineInfo)
    {
        return toAjax(tCarlineInfoService.updateTCarlineInfo(tCarlineInfo));
    }

    /**
     * 删除`虚拟设备参数`
     */
    @PreAuthorize("@ss.hasPermi('device:info:remove')")
    @Log(title = "`虚拟设备参数`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids)
    {
        return toAjax(tCarlineInfoService.deleteTCarlineInfoByUids(uids));
    }
}
