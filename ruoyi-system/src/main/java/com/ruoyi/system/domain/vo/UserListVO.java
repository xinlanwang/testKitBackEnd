package com.ruoyi.system.domain.vo;

import lombok.Data;

@Data
public class UserListVO {
    private Long userId;
    private String email;
    private String roleName;
    private String testGroupId;
    private String userCreateTime;
    private Long roleId;
}
