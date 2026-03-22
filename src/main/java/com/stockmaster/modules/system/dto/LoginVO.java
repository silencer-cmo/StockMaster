package com.stockmaster.modules.system.dto;

import lombok.Data;

import java.util.List;

@Data
public class LoginVO {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private UserInfoVO userInfo;
}
