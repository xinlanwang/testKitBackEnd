package com.ruoyi.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TUserMapper;
import com.ruoyi.system.domain.po.TUser;
import com.ruoyi.system.service.ITUserService;

/**
 * `用户`Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-11-04
 */
@Service
public class TUserServiceImpl implements ITUserService 
{
    @Autowired
    private TUserMapper tUserMapper;

    /**
     * 查询`用户`
     * 
     * @param uid `用户`主键
     * @return `用户`
     */
    @Override
    public TUser selectTUserByUid(String uid)
    {
        return tUserMapper.selectTUserByUid(uid);
    }

    /**
     * 查询`用户`列表
     * 
     * @param tUser `用户`
     * @return `用户`
     */
    @Override
    public List<TUser> selectTUserList(TUser tUser)
    {
        return tUserMapper.selectTUserList(tUser);
    }

    /**
     * 新增`用户`
     * 
     * @param tUser `用户`
     * @return 结果
     */
    @Override
    public int insertTUser(TUser tUser)
    {
//        tUser.tUsersetCreateTime(DateUtils.getNowDate());
        return tUserMapper.insertTUser(tUser);
    }

    /**
     * 修改`用户`
     * 
     * @param tUser `用户`
     * @return 结果
     */
    @Override
    public int updateTUser(TUser tUser)
    {
//        tUser.setUpdateTime(DateUtils.getNowDate());
        return tUserMapper.updateTUser(tUser);
    }

    /**
     * 批量删除`用户`
     * 
     * @param uids 需要删除的`用户`主键
     * @return 结果
     */
    @Override
    public int deleteTUserByUids(String[] uids)
    {
        return tUserMapper.deleteTUserByUids(uids);
    }

    /**
     * 删除`用户`信息
     * 
     * @param uid `用户`主键
     * @return 结果
     */
    @Override
    public int deleteTUserByUid(String uid)
    {
        return tUserMapper.deleteTUserByUid(uid);
    }
}
