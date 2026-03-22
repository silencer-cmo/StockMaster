package com.stockmaster.modules.system.service.impl;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.exception.BusinessException;
import com.stockmaster.modules.system.dto.RoleDTO;
import com.stockmaster.modules.system.entity.Menu;
import com.stockmaster.modules.system.entity.Role;
import com.stockmaster.modules.system.entity.RoleMenu;
import com.stockmaster.modules.system.repository.MenuRepository;
import com.stockmaster.modules.system.repository.RoleMenuRepository;
import com.stockmaster.modules.system.repository.RoleRepository;
import com.stockmaster.modules.system.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMenuRepository roleMenuRepository;
    private final MenuRepository menuRepository;

    @Override
    public RoleDTO getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        RoleDTO dto = convertToDTO(role);
        dto.setMenuIds(roleMenuRepository.findMenuIdsByRoleId(id));
        return dto;
    }

    @Override
    @Transactional
    public RoleDTO create(RoleDTO roleDTO) {
        if (roleRepository.existsByRoleCode(roleDTO.getRoleCode())) {
            throw new BusinessException("角色编码已存在");
        }

        Role role = convertToEntity(roleDTO);
        role = roleRepository.save(role);

        if (roleDTO.getMenuIds() != null && !roleDTO.getMenuIds().isEmpty()) {
            assignMenus(role.getId(), roleDTO.getMenuIds());
        }

        return convertToDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO update(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        if (!role.getRoleCode().equals(roleDTO.getRoleCode()) && roleRepository.existsByRoleCode(roleDTO.getRoleCode())) {
            throw new BusinessException("角色编码已存在");
        }

        role.setRoleCode(roleDTO.getRoleCode());
        role.setRoleName(roleDTO.getRoleName());
        role.setDescription(roleDTO.getDescription());
        role.setSortOrder(roleDTO.getSortOrder());
        role.setStatus(roleDTO.getStatus());

        role = roleRepository.save(role);

        if (roleDTO.getMenuIds() != null) {
            assignMenus(role.getId(), roleDTO.getMenuIds());
        }

        return convertToDTO(role);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        role.setDeleted(true);
        roleRepository.save(role);
        roleMenuRepository.deleteByRoleId(id);
    }

    @Override
    public PageResult<RoleDTO> getList(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.ASC, "sortOrder"));
        Page<Role> page = roleRepository.findByKeyword(keyword, pageable);

        List<RoleDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(list, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void assignMenus(Long roleId, List<Long> menuIds) {
        roleMenuRepository.deleteByRoleId(roleId);

        if (menuIds != null && !menuIds.isEmpty()) {
            List<RoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> new RoleMenu(roleId, menuId))
                    .collect(Collectors.toList());
            roleMenuRepository.saveAll(roleMenus);
        }
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAllActive();
        return roles.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));
        role.setStatus(status);
        roleRepository.save(role);
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setRoleCode(role.getRoleCode());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());
        dto.setSortOrder(role.getSortOrder());
        dto.setStatus(role.getStatus());
        return dto;
    }

    private Role convertToEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);
        role.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        return role;
    }
}
