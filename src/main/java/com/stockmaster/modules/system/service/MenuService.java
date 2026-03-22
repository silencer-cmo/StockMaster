package com.stockmaster.modules.system.service;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.modules.system.dto.MenuDTO;
import com.stockmaster.modules.system.dto.MenuTreeVO;

import java.util.List;

public interface MenuService {

    MenuDTO getById(Long id);

    MenuDTO create(MenuDTO menuDTO);

    MenuDTO update(Long id, MenuDTO menuDTO);

    void delete(Long id);

    List<MenuTreeVO> getMenuTree();

    List<MenuTreeVO> getUserMenus(Long userId);

    List<MenuTreeVO> convertToTree(List<MenuDTO> menus);

    List<String> getUserPermissions(Long userId);
}
