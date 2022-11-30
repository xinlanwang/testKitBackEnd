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
import com.ruoyi.system.domain.po.TSysDictType;
import com.ruoyi.system.service.ITSysDictTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `字典类型`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/device/type")
public class TSysDictTypeController extends BaseController
{
    @Autowired
    private ITSysDictTypeService tSysDictTypeService;

    /**
     * 查询`字典类型`列表
     */
    @PreAuthorize("@ss.hasPermi('device:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSysDictType tSysDictType)
    {
        startPage();
        List<TSysDictType> list = tSysDictTypeService.selectTSysDictTypeList(tSysDictType);
        return getDataTable(list);
    }

    /**
     * 导出`字典类型`列表
     */
    @PreAuthorize("@ss.hasPermi('device:type:export')")
    @Log(title = "`字典类型`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSysDictType tSysDictType)
    {
        List<TSysDictType> list = tSysDictTypeService.selectTSysDictTypeList(tSysDictType);
        ExcelUtil<TSysDictType> util = new ExcelUtil<TSysDictType>(TSysDictType.class);
        util.exportExcel(response, list, "`字典类型`数据");
    }

    /**
     * 获取`字典类型`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:type:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tSysDictTypeService.selectTSysDictTypeByUid(uid));
    }

    /**
     * 新增`字典类型`
     */
    @PreAuthorize("@ss.hasPermi('device:type:add')")
    @Log(title = "`字典类型`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSysDictType tSysDictType)
    {
        return toAjax(tSysDictTypeService.insertTSysDictType(tSysDictType));
    }

    /**
     * 修改`字典类型`
     */
    @PreAuthorize("@ss.hasPermi('device:type:edit')")
    @Log(title = "`字典类型`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSysDictType tSysDictType)
    {
        return toAjax(tSysDictTypeService.updateTSysDictType(tSysDictType));
    }

    /**
     * 删除`字典类型`
     */
    @PreAuthorize("@ss.hasPermi('device:type:remove')")
    @Log(title = "`字典类型`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids)
    {
        return toAjax(tSysDictTypeService.deleteTSysDictTypeByUids(uids));
    }
}
