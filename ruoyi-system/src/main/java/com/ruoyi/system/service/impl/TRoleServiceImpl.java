package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TRoleMapper;
import com.ruoyi.system.domain.po.TRole;
import com.ruoyi.system.service.ITRoleService;

/**
 * `角色`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class TRoleServiceImpl implements ITRoleService 
{
    @Autowired
    private TRoleMapper tRoleMapper;

    /**
     * 查询`角色`
     * 
     * @param uid `角色`主键
     * @return `角色`
     */
    @Override
    public TRole selectTRoleByUid(String uid)
    {
        return tRoleMapper.selectTRoleByUid(uid);
    }

    /**
     * 查询`角色`列表
     * 
     * @param tRole `角色`
     * @return `角色`
     */
    @Override
    public List<TRole> selectTRoleList(TRole tRole)
    {
        return tRoleMapper.selectTRoleList(tRole);
    }

    /**
     * 新增`角色`
     * 
     * @param tRole `角色`
     * @return 结果
     */
    @Override
    public int insertTRole(TRole tRole)
    {
//        tRole.setCreateTime(DateUtils.getNowDate());
        return tRoleMapper.insertTRole(tRole);
    }

    /**
     * 修改`角色`
     * 
     * @param tRole `角色`
     * @return 结果
     */
    @Override
    public int updateTRole(TRole tRole)
    {
//        tRole.setUpdateTime(DateUtils.getNowDate());
        return tRoleMapper.updateTRole(tRole);
    }

    /**
     * 批量删除`角色`
     * 
     * @param uids 需要删除的`角色`主键
     * @return 结果
     */
    @Override
    public int deleteTRoleByUids(String[] uids)
    {
        return tRoleMapper.deleteTRoleByUids(uids);
    }

    /**
     * 删除`角色`信息
     * 
     * @param uid `角色`主键
     * @return 结果
     */
    @Override
    public int deleteTRoleByUid(String uid)
    {
        return tRoleMapper.deleteTRoleByUid(uid);
    }
}
