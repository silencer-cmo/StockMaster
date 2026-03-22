package com.stockmaster.modules.system.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenuId implements Serializable {
    private Long roleId;
    private Long menuId;
}
