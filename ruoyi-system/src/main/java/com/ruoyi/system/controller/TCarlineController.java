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
import com.ruoyi.system.domain.po.TCarline;
import com.ruoyi.system.service.ITCarlineService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `车型基本数据`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/device/carline")
public class TCarlineController extends BaseController
{
    @Autowired
    private ITCarlineService tCarlineService;

    /**
     * 查询`车型基本数据`列表
     */
    @PreAuthorize("@ss.hasPermi('device:carline:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCarline tCarline)
    {
        startPage();
        List<TCarline> list = tCarlineService.selectTCarlineList(tCarline);
        return getDataTable(list);
    }

    /**
     * 导出`车型基本数据`列表
     */
    @PreAuthorize("@ss.hasPermi('device:carline:export')")
    @Log(title = "`车型基本数据`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCarline tCarline)
    {
        List<TCarline> list = tCarlineService.selectTCarlineList(tCarline);
        ExcelUtil<TCarline> util = new ExcelUtil<TCarline>(TCarline.class);
        util.exportExcel(response, list, "`车型基本数据`数据");
    }

    /**
     * 获取`车型基本数据`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:carline:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tCarlineService.selectTCarlineByUid(uid));
    }

    /**
     * 新增`车型基本数据`
     */
    @PreAuthorize("@ss.hasPermi('device:carline:add')")
    @Log(title = "`车型基本数据`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCarline tCarline)
    {
        return toAjax(tCarlineService.insertTCarline(tCarline));
    }

    /**
     * 修改`车型基本数据`
     */
    @PreAuthorize("@ss.hasPermi('device:carline:edit')")
    @Log(title = "`车型基本数据`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCarline tCarline)
    {
        return toAjax(tCarlineService.updateTCarline(tCarline));
    }

    /**
     * 删除`车型基本数据`
     */
    @PreAuthorize("@ss.hasPermi('device:carline:remove')")
    @Log(title = "`车型基本数据`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable Long[] uids)
    {
        return toAjax(tCarlineService.deleteTCarlineByUids(uids));
    }
}
