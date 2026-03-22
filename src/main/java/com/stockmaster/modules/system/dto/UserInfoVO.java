package com.stockmaster.modules.system.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserInfoVO {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer status;
    private Boolean admin;
    private Long deptId;
    private LocalDateTime lastLoginTime;
    private List<String> roles;
    private List<String> permissions;
}
