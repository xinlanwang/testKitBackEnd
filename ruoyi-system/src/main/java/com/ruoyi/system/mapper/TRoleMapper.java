package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.domain.po.TRole;

/**
 * `角色`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface TRoleMapper extends BaseMapper<TRole>
{
    /**
     * 查询`角色`
     * 
     * @param uid `角色`主键
     * @return `角色`
     */
    public TRole selectTRoleByUid(String uid);

    /**
     * 查询`角色`列表
     * 
     * @param tRole `角色`
     * @return `角色`集合
     */
    public List<TRole> selectTRoleList(TRole tRole);

    /**
     * 新增`角色`
     * 
     * @param tRole `角色`
     * @return 结果
     */
    public int insertTRole(TRole tRole);

    /**
     * 修改`角色`
     * 
     * @param tRole `角色`
     * @return 结果
     */
    public int updateTRole(TRole tRole);

    /**
     * 删除`角色`
     * 
     * @param uid `角色`主键
     * @return 结果
     */
    public int deleteTRoleByUid(String uid);

    /**
     * 批量删除`角色`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTRoleByUids(String[] uids);
}
