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
import com.ruoyi.system.domain.po.TSysOperLog;
import com.ruoyi.system.service.ITSysOperLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `操作日志记录`Controller
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/device/log")
public class TSysOperLogController extends BaseController
{
    @Autowired
    private ITSysOperLogService tSysOperLogService;

    /**
     * 查询`操作日志记录`列表
     */
    @PreAuthorize("@ss.hasPermi('device:log:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSysOperLog tSysOperLog)
    {
        startPage();
        List<TSysOperLog> list = tSysOperLogService.selectTSysOperLogList(tSysOperLog);
        return getDataTable(list);
    }

    /**
     * 导出`操作日志记录`列表
     */
    @PreAuthorize("@ss.hasPermi('device:log:export')")
    @Log(title = "`操作日志记录`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSysOperLog tSysOperLog)
    {
        List<TSysOperLog> list = tSysOperLogService.selectTSysOperLogList(tSysOperLog);
        ExcelUtil<TSysOperLog> util = new ExcelUtil<TSysOperLog>(TSysOperLog.class);
        util.exportExcel(response, list, "`操作日志记录`数据");
    }

    /**
     * 获取`操作日志记录`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:log:query')")
    @GetMapping(value = "/{operId}")
    public AjaxResult getInfo(@PathVariable("operId") Long operId)
    {
        return success(tSysOperLogService.selectTSysOperLogByOperId(operId));
    }

    /**
     * 新增`操作日志记录`
     */
    @PreAuthorize("@ss.hasPermi('device:log:add')")
    @Log(title = "`操作日志记录`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSysOperLog tSysOperLog)
    {
        return toAjax(tSysOperLogService.insertTSysOperLog(tSysOperLog));
    }

    /**
     * 修改`操作日志记录`
     */
    @PreAuthorize("@ss.hasPermi('device:log:edit')")
    @Log(title = "`操作日志记录`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSysOperLog tSysOperLog)
    {
        return toAjax(tSysOperLogService.updateTSysOperLog(tSysOperLog));
    }

    /**
     * 删除`操作日志记录`
     */
    @PreAuthorize("@ss.hasPermi('device:log:remove')")
    @Log(title = "`操作日志记录`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{operIds}")
    public AjaxResult remove(@PathVariable Long[] operIds)
    {
        return toAjax(tSysOperLogService.deleteTSysOperLogByOperIds(operIds));
    }
}
