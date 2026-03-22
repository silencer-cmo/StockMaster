package com.stockmaster.modules.system.repository;

import com.stockmaster.modules.system.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.deleted = false AND m.status = 1 ORDER BY m.sortOrder")
    List<Menu> findAllActive();

    @Query("SELECT m FROM Menu m WHERE m.deleted = false AND m.parentId = :parentId ORDER BY m.sortOrder")
    List<Menu> findByParentId(@Param("parentId") Long parentId);

    @Query("SELECT m FROM Menu m WHERE m.deleted = false ORDER BY m.sortOrder")
    List<Menu> findAllOrderBySortOrder();

    @Query("SELECT DISTINCT m FROM Menu m JOIN RoleMenu rm ON m.id = rm.menuId WHERE rm.roleId IN :roleIds AND m.deleted = false AND m.status = 1 ORDER BY m.sortOrder")
    List<Menu> findByRoleIds(@Param("roleIds") List<Long> roleIds);

    @Query("SELECT m FROM Menu m WHERE m.deleted = false AND m.menuType = :menuType ORDER BY m.sortOrder")
    List<Menu> findByMenuType(@Param("menuType") Integer menuType);
}
