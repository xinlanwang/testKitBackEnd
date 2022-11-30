package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TCategoryMenu;

/**
 * `菜单权限`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-03
 */
public interface ITCategoryMenuService 
{
    /**
     * 查询`菜单权限`
     * 
     * @param uid `菜单权限`主键
     * @return `菜单权限`
     */
    public TCategoryMenu selectTCategoryMenuByUid(String uid);

    /**
     * 查询`菜单权限`列表
     * 
     * @param tCategoryMenu `菜单权限`
     * @return `菜单权限`集合
     */
    public List<TCategoryMenu> selectTCategoryMenuList(TCategoryMenu tCategoryMenu);

    /**
     * 新增`菜单权限`
     * 
     * @param tCategoryMenu `菜单权限`
     * @return 结果
     */
    public int insertTCategoryMenu(TCategoryMenu tCategoryMenu);

    /**
     * 修改`菜单权限`
     * 
     * @param tCategoryMenu `菜单权限`
     * @return 结果
     */
    public int updateTCategoryMenu(TCategoryMenu tCategoryMenu);

    /**
     * 批量删除`菜单权限`
     * 
     * @param uids 需要删除的`菜单权限`主键集合
     * @return 结果
     */
    public int deleteTCategoryMenuByUids(String[] uids);

    /**
     * 删除`菜单权限`信息
     * 
     * @param uid `菜单权限`主键
     * @return 结果
     */
    public int deleteTCategoryMenuByUid(String uid);
}
