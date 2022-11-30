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
import com.ruoyi.system.domain.po.TCarlineComponent;
import com.ruoyi.system.service.ITCarlineComponentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `设备-组件关联（n2n）`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/device/component")
public class TCarlineComponentController extends BaseController
{
    @Autowired
    private ITCarlineComponentService tCarlineComponentService;

    /**
     * 查询`设备-组件关联（n2n）`列表
     */
    @PreAuthorize("@ss.hasPermi('device:component:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCarlineComponent tCarlineComponent)
    {
        startPage();
        List<TCarlineComponent> list = tCarlineComponentService.selectTCarlineComponentList(tCarlineComponent);
        return getDataTable(list);
    }

    /**
     * 导出`设备-组件关联（n2n）`列表
     */
    @PreAuthorize("@ss.hasPermi('device:component:export')")
    @Log(title = "`设备-组件关联（n2n）`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCarlineComponent tCarlineComponent)
    {
        List<TCarlineComponent> list = tCarlineComponentService.selectTCarlineComponentList(tCarlineComponent);
        ExcelUtil<TCarlineComponent> util = new ExcelUtil<TCarlineComponent>(TCarlineComponent.class);
        util.exportExcel(response, list, "`设备-组件关联（n2n）`数据");
    }

    /**
     * 获取`设备-组件关联（n2n）`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:component:query')")
    @GetMapping(value = "/{componentUid}")
    public AjaxResult getInfo(@PathVariable("componentUid") String componentUid)
    {
        return success(tCarlineComponentService.selectTCarlineComponentByComponentUid(componentUid));
    }

    /**
     * 新增`设备-组件关联（n2n）`
     */
    @PreAuthorize("@ss.hasPermi('device:component:add')")
    @Log(title = "`设备-组件关联（n2n）`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCarlineComponent tCarlineComponent)
    {
        return toAjax(tCarlineComponentService.insertTCarlineComponent(tCarlineComponent));
    }

    /**
     * 修改`设备-组件关联（n2n）`
     */
    @PreAuthorize("@ss.hasPermi('device:component:edit')")
    @Log(title = "`设备-组件关联（n2n）`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCarlineComponent tCarlineComponent)
    {
        return toAjax(tCarlineComponentService.updateTCarlineComponent(tCarlineComponent));
    }

    /**
     * 删除`设备-组件关联（n2n）`
     */
    @PreAuthorize("@ss.hasPermi('device:component:remove')")
    @Log(title = "`设备-组件关联（n2n）`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{componentUids}")
    public AjaxResult remove(@PathVariable String[] componentUids)
    {
        return toAjax(tCarlineComponentService.deleteTCarlineComponentByComponentUids(componentUids));
    }
}
