package com.stockmaster.modules.system.service;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.modules.system.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO getById(Long id);

    RoleDTO create(RoleDTO roleDTO);

    RoleDTO update(Long id, RoleDTO roleDTO);

    void delete(Long id);

    PageResult<RoleDTO> getList(String keyword, Integer pageNum, Integer pageSize);

    void assignMenus(Long roleId, List<Long> menuIds);

    List<RoleDTO> getAllRoles();

    void updateStatus(Long id, Integer status);
}
