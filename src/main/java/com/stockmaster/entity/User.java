package com.stockmaster.modules.system.entity;

import com.stockmaster.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String realName;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    @Column(columnDefinition = "text")
    private String avatar;

    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean admin = false;
}