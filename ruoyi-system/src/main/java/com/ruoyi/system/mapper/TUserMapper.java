package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.po.TUser;

/**
 * `用户`Mapper接口
 * 
 * @author ruoyi
 * @date 2022-11-04
 */

public interface TUserMapper extends BaseMapper<TUser>
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
     * 删除`用户`
     * 
     * @param uid `用户`主键
     * @return 结果
     */
    public int deleteTUserByUid(String uid);

    /**
     * 批量删除`用户`
     * 
     * @param uids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTUserByUids(String[] uids);
}
