package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.po.TUser;

/**
 * `用户`Service接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
public interface ITUserService 
{
    /**
     * 查询`用户`
     * 
     * @param uid `用户`主键
     * @return `用户`
     */
    public TUser selectTUserByUid(String uid);

    /**
     * 查询`用户`列表
     * 
     * @param tUser `用户`
     * @return `用户`集合
     */
    public List<TUser> selectTUserList(TUser tUser);

    /**
     * 新增`用户`
     * 
     * @param tUser `用户`
     * @return 结果
     */
    public int insertTUser(TUser tUser);

    /**
     * 修改`用户`
     * 
     * @param tUser `用户`
     * @return 结果
     */
    public int updateTUser(TUser tUser);

    /**
     * 批量删除`用户`
     * 
     * @param uids 需要删除的`用户`主键集合
     * @return 结果
     */
    public int deleteTUserByUids(String[] uids);

    /**
     * 删除`用户`信息
     * 
     * @param uid `用户`主键
     * @return 结果
     */
    public int deleteTUserByUid(String uid);
}
