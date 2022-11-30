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
import com.ruoyi.system.domain.po.TSysLogininfor;
import com.ruoyi.system.service.ITSysLogininforService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `系统访问记录`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/device/logininfor")
public class TSysLogininforController extends BaseController
{
    @Autowired
    private ITSysLogininforService tSysLogininforService;

    /**
     * 查询`系统访问记录`列表
     */
    @PreAuthorize("@ss.hasPermi('device:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSysLogininfor tSysLogininfor)
    {
        startPage();
        List<TSysLogininfor> list = tSysLogininforService.selectTSysLogininforList(tSysLogininfor);
        return getDataTable(list);
    }

    /**
     * 导出`系统访问记录`列表
     */
    @PreAuthorize("@ss.hasPermi('device:logininfor:export')")
    @Log(title = "`系统访问记录`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSysLogininfor tSysLogininfor)
    {
        List<TSysLogininfor> list = tSysLogininforService.selectTSysLogininforList(tSysLogininfor);
        ExcelUtil<TSysLogininfor> util = new ExcelUtil<TSysLogininfor>(TSysLogininfor.class);
        util.exportExcel(response, list, "`系统访问记录`数据");
    }

    /**
     * 获取`系统访问记录`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:logininfor:query')")
    @GetMapping(value = "/{infoId}")
    public AjaxResult getInfo(@PathVariable("infoId") Long infoId)
    {
        return success(tSysLogininforService.selectTSysLogininforByInfoId(infoId));
    }

    /**
     * 新增`系统访问记录`
     */
    @PreAuthorize("@ss.hasPermi('device:logininfor:add')")
    @Log(title = "`系统访问记录`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSysLogininfor tSysLogininfor)
    {
        return toAjax(tSysLogininforService.insertTSysLogininfor(tSysLogininfor));
    }

    /**
     * 修改`系统访问记录`
     */
    @PreAuthorize("@ss.hasPermi('device:logininfor:edit')")
    @Log(title = "`系统访问记录`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSysLogininfor tSysLogininfor)
    {
        return toAjax(tSysLogininforService.updateTSysLogininfor(tSysLogininfor));
    }

    /**
     * 删除`系统访问记录`
     */
    @PreAuthorize("@ss.hasPermi('device:logininfor:remove')")
    @Log(title = "`系统访问记录`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        return toAjax(tSysLogininforService.deleteTSysLogininforByInfoIds(infoIds));
    }
}
