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
import com.ruoyi.system.domain.po.TCluster;
import com.ruoyi.system.service.ITClusterService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * `版本管控`Controller
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@RestController
@RequestMapping("/device/cluster")
public class TClusterController extends BaseController
{
    @Autowired
    private ITClusterService tClusterService;

    /**
     * 查询`版本管控`列表
     */
    @PreAuthorize("@ss.hasPermi('device:cluster:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCluster tCluster)
    {
        startPage();
        List<TCluster> list = tClusterService.selectTClusterList(tCluster);
        return getDataTable(list);
    }

    /**
     * 导出`版本管控`列表
     */
    @PreAuthorize("@ss.hasPermi('device:cluster:export')")
    @Log(title = "`版本管控`", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCluster tCluster)
    {
        List<TCluster> list = tClusterService.selectTClusterList(tCluster);
        ExcelUtil<TCluster> util = new ExcelUtil<TCluster>(TCluster.class);
        util.exportExcel(response, list, "`版本管控`数据");
    }

    /**
     * 获取`版本管控`详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:cluster:query')")
    @GetMapping(value = "/{uid}")
    public AjaxResult getInfo(@PathVariable("uid") String uid)
    {
        return success(tClusterService.selectTClusterByUid(uid));
    }

    /**
     * 新增`版本管控`
     */
    @PreAuthorize("@ss.hasPermi('device:cluster:add')")
    @Log(title = "`版本管控`", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCluster tCluster)
    {
        return toAjax(tClusterService.insertTCluster(tCluster));
    }

    /**
     * 修改`版本管控`
     */
    @PreAuthorize("@ss.hasPermi('device:cluster:edit')")
    @Log(title = "`版本管控`", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCluster tCluster)
    {
        return toAjax(tClusterService.updateTCluster(tCluster));
    }

    /**
     * 删除`版本管控`
     */
    @PreAuthorize("@ss.hasPermi('device:cluster:remove')")
    @Log(title = "`版本管控`", businessType = BusinessType.DELETE)
	@DeleteMapping("/{uids}")
    public AjaxResult remove(@PathVariable String[] uids)
    {
        return toAjax(tClusterService.deleteTClusterByUids(uids));
    }
}
