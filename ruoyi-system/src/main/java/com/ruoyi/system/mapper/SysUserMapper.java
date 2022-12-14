package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.system.domain.dto.UserInfoDTO;
import com.ruoyi.system.domain.param.DesktopRegisterParam;
import com.ruoyi.system.domain.vo.GoldenInfoVO;
import com.ruoyi.system.domain.vo.UserInfoVO;
import com.ruoyi.system.domain.vo.UserListVO;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Select;

/**
 * 用户表 数据层
 * 
 * @author ruoyi
 */
public interface SysUserMapper extends BaseMapper<SysUser>
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectConrrentUserList(SysUser sysUser);

    /**
     * 根据条件分页查询已配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public SysUser checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);


    String selectWebUserList = "select su.user_id as userId,su.email as email,sr.role_name as roleName,sr.role_id as roleId,su.test_group_id as testGroupId,su.create_time as userCreateTime\n" +
            "from sys_user su\n" +
            "     join sys_user_role ur on ur.user_id = su.user_id\n" +
            " join sys_role sr on ur.role_id = sr.role_id\n where (sr.role_id = 2 or sr.role_id = 3) order by su.update_time desc" ;
    @Select(selectWebUserList)
    public List<UserListVO> selectWebUserList();


    String selectUserInfoByUserId = "select su.user_id,su.test_group_id,su.user_name,su.nick_name,su.email,su.phonenumber,su.sex,su.login_ip,sr.role_id\n" +
            "from sys_user_role sr\n" +
            "right join sys_user su on su.user_id = sr.user_id\n" +
            "where sr.user_id = #{userId};" ;
    @Select(selectUserInfoByUserId)
    List<UserInfoDTO> selectUserInfoByUserId(@Param("userId") Long userId);
}
