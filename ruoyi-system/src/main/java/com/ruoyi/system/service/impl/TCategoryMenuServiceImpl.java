package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TCategoryMenuMapper;
import com.ruoyi.system.domain.po.TCategoryMenu;
import com.ruoyi.system.service.ITCategoryMenuService;

/**
 * `菜单权限`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
@Service
public class TCategoryMenuServiceImpl implements ITCategoryMenuService 
{
    @Autowired
    private TCategoryMenuMapper tCategoryMenuMapper;

    /**
     * 查询`菜单权限`
     * 
     * @param uid `菜单权限`主键
     * @return `菜单权限`
     */
    @Override
    public TCategoryMenu selectTCategoryMenuByUid(String uid)
    {
        return tCategoryMenuMapper.selectTCategoryMenuByUid(uid);
    }

    /**
     * 查询`菜单权限`列表
     * 
     * @param tCategoryMenu `菜单权限`
     * @return `菜单权限`
     */
    @Override
    public List<TCategoryMenu> selectTCategoryMenuList(TCategoryMenu tCategoryMenu)
    {
        return tCategoryMenuMapper.selectTCategoryMenuList(tCategoryMenu);
    }

    /**
     * 新增`菜单权限`
     * 
     * @param tCategoryMenu `菜单权限`
     * @return 结果
     */
    @Override
    public int insertTCategoryMenu(TCategoryMenu tCategoryMenu)
    {
//        tCategoryMenu.setCreateTime(DateUtils.getNowDate());
        return tCategoryMenuMapper.insertTCategoryMenu(tCategoryMenu);
    }

    /**
     * 修改`菜单权限`
     * 
     * @param tCategoryMenu `菜单权限`
     * @return 结果
     */
    @Override
    public int updateTCategoryMenu(TCategoryMenu tCategoryMenu)
    {
//        tCategoryMenu.setUpdateTime(DateUtils.getNowDate());
        return tCategoryMenuMapper.updateTCategoryMenu(tCategoryMenu);
    }

    /**
     * 批量删除`菜单权限`
     * 
     * @param uids 需要删除的`菜单权限`主键
     * @return 结果
     */
    @Override
    public int deleteTCategoryMenuByUids(String[] uids)
    {
        return tCategoryMenuMapper.deleteTCategoryMenuByUids(uids);
    }

    /**
     * 删除`菜单权限`信息
     * 
     * @param uid `菜单权限`主键
     * @return 结果
     */
    @Override
    public int deleteTCategoryMenuByUid(String uid)
    {
        return tCategoryMenuMapper.deleteTCategoryMenuByUid(uid);
    }
}
