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
import com.ruoyi.system.domain.po.TSysDictData;
import com.ruoyi.system.service.ITSysDictDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `字典数据`Controller
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/dict/data")
public class TSysDictDataController extends BaseController
{
    @Autowired
    private ITSysDictDataService tSysDictDataService;

    /**
     * 查询`字典数据`列表
     */
    @PreAuthorize("@ss.hasPermi('dict:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(TSysDictData tSysDictData)
    {
        startPage();
        List<TSysDictData> list = tSysDictDataService.selectTSysDictDataList(tSysDictData);
        return getDataTable(list);
    }

    /**
     * 导出`字典数据`列表
     */
    @PreAuthorize("@ss.hasPermi('dict:data:export')")
    @Log(title = "`字典数据`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TSysDictData tSysDictData)
    {
        List<TSysDictData> list = tSysDictDataService.selectTSysDictDataList(tSysDictData);
        ExcelUtil<TSysDictData> util = new ExcelUtil<TSysDictData>(TSysDictData.class);
        util.exportExcel(response, list, "`字典数据`数据");
    }

    /**
     * 获取`字典数据`详细信息
     */
    @PreAuthorize("@ss.hasPermi('dict:data:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tSysDictDataService.selectTSysDictDataByUid(uid));
    }

    /**
     * 新增`字典数据`
     */
    @PreAuthorize("@ss.hasPermi('dict:data:add')")
    @Log(title = "`字典数据`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TSysDictData tSysDictData)
    {
        return toAjax(tSysDictDataService.insertTSysDictData(tSysDictData));
    }

    /**
     * 修改`字典数据`
     */
    @PreAuthorize("@ss.hasPermi('dict:data:edit')")
    @Log(title = "`字典数据`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TSysDictData tSysDictData)
    {
        return toAjax(tSysDictDataService.updateTSysDictData(tSysDictData));
    }

    /**
     * 删除`字典数据`
     */
    @PreAuthorize("@ss.hasPermi('dict:data:remove')")
    @Log(title = "`字典数据`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids)
    {
        return toAjax(tSysDictDataService.deleteTSysDictDataByUids(uids));
    }
}
