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
import com.ruoyi.system.domain.po.TCategoryMenu;
import com.ruoyi.system.service.ITCategoryMenuService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `菜单权限`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/device/menu")
public class TCategoryMenuController extends BaseController
{
    @Autowired
    private ITCategoryMenuService tCategoryMenuService;

    /**
     * 查询`菜单权限`列表
     */
    @PreAuthorize("@ss.hasPermi('device:menu:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCategoryMenu tCategoryMenu)
    {
        startPage();
        List<TCategoryMenu> list = tCategoryMenuService.selectTCategoryMenuList(tCategoryMenu);
        return getDataTable(list);
    }

    /**
     * 导出`菜单权限`列表
     */
    @PreAuthorize("@ss.hasPermi('device:menu:export')")
    @Log(title = "`菜单权限`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCategoryMenu tCategoryMenu)
    {
        List<TCategoryMenu> list = tCategoryMenuService.selectTCategoryMenuList(tCategoryMenu);
        ExcelUtil<TCategoryMenu> util = new ExcelUtil<TCategoryMenu>(TCategoryMenu.class);
        util.exportExcel(response, list, "`菜单权限`数据");
    }

    /**
     * 获取`菜单权限`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:menu:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tCategoryMenuService.selectTCategoryMenuByUid(uid));
    }

    /**
     * 新增`菜单权限`
     */
    @PreAuthorize("@ss.hasPermi('device:menu:add')")
    @Log(title = "`菜单权限`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCategoryMenu tCategoryMenu)
    {
        return toAjax(tCategoryMenuService.insertTCategoryMenu(tCategoryMenu));
    }

    /**
     * 修改`菜单权限`
     */
    @PreAuthorize("@ss.hasPermi('device:menu:edit')")
    @Log(title = "`菜单权限`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCategoryMenu tCategoryMenu)
    {
        return toAjax(tCategoryMenuService.updateTCategoryMenu(tCategoryMenu));
    }

    /**
     * 删除`菜单权限`
     */
    @PreAuthorize("@ss.hasPermi('device:menu:remove')")
    @Log(title = "`菜单权限`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids)
    {
        return toAjax(tCategoryMenuService.deleteTCategoryMenuByUids(uids));
    }
}
