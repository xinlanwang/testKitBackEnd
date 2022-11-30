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
import com.ruoyi.system.domain.po.TRole;
import com.ruoyi.system.service.ITRoleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `角色`Controller
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/device/role")
public class TRoleController extends BaseController
{
    @Autowired
    private ITRoleService tRoleService;

    /**
     * 查询`角色`列表
     */
    @PreAuthorize("@ss.hasPermi('device:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(TRole tRole)
    {
        startPage();
        List<TRole> list = tRoleService.selectTRoleList(tRole);
        return getDataTable(list);
    }

    /**
     * 导出`角色`列表
     */
    @PreAuthorize("@ss.hasPermi('device:role:export')")
    @Log(title = "`角色`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TRole tRole)
    {
        List<TRole> list = tRoleService.selectTRoleList(tRole);
        ExcelUtil<TRole> util = new ExcelUtil<TRole>(TRole.class);
        util.exportExcel(response, list, "`角色`数据");
    }

    /**
     * 获取`角色`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:role:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tRoleService.selectTRoleByUid(uid));
    }

    /**
     * 新增`角色`
     */
    @PreAuthorize("@ss.hasPermi('device:role:add')")
    @Log(title = "`角色`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TRole tRole)
    {
        return toAjax(tRoleService.insertTRole(tRole));
    }

    /**
     * 修改`角色`
     */
    @PreAuthorize("@ss.hasPermi('device:role:edit')")
    @Log(title = "`角色`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TRole tRole)
    {
        return toAjax(tRoleService.updateTRole(tRole));
    }

    /**
     * 删除`角色`
     */
    @PreAuthorize("@ss.hasPermi('device:role:remove')")
    @Log(title = "`角色`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids)
    {
        return toAjax(tRoleService.deleteTRoleByUids(uids));
    }
}
