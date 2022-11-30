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
import com.ruoyi.system.domain.po.TUser;
import com.ruoyi.system.service.ITUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `用户`Controller
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@RestController
@RequestMapping("/device/user")
public class TUserController extends BaseController
{
    @Autowired
    private ITUserService tUserService;

    /**
     * 查询`用户`列表
     */
    @PreAuthorize("@ss.hasPermi('device:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(TUser tUser)
    {
        startPage();
        List<TUser> list = tUserService.selectTUserList(tUser);
        return getDataTable(list);
    }

    /**
     * 导出`用户`列表
     */
    @PreAuthorize("@ss.hasPermi('device:user:export')")
    @Log(title = "`用户`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TUser tUser)
    {
        List<TUser> list = tUserService.selectTUserList(tUser);
        ExcelUtil<TUser> util = new ExcelUtil<TUser>(TUser.class);
        util.exportExcel(response, list, "`用户`数据");
    }

    /**
     * 获取`用户`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:user:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tUserService.selectTUserByUid(uid));
    }

    /**
     * 新增`用户`
     */
    @PreAuthorize("@ss.hasPermi('device:user:add')")
    @Log(title = "`用户`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TUser tUser)
    {
        return toAjax(tUserService.insertTUser(tUser));
    }

    /**
     * 修改`用户`
     */
    @PreAuthorize("@ss.hasPermi('device:user:edit')")
    @Log(title = "`用户`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TUser tUser)
    {
        return toAjax(tUserService.updateTUser(tUser));
    }

    /**
     * 删除`用户`
     */
    @PreAuthorize("@ss.hasPermi('device:user:remove')")
    @Log(title = "`用户`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids)
    {
        return toAjax(tUserService.deleteTUserByUids(uids));
    }
}
