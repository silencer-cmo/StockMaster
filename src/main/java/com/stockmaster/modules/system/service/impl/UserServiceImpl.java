package com.stockmaster.modules.system.service.impl;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.exception.BusinessException;
import com.stockmaster.common.security.JwtTokenProvider;
import com.stockmaster.common.security.SecurityUtils;
import com.stockmaster.modules.system.dto.*;
import com.stockmaster.modules.system.entity.*;
import com.stockmaster.modules.system.repository.*;
import com.stockmaster.modules.system.service.MenuService;
import com.stockmaster.modules.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MenuService menuService;

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));

        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        List<Role> roles = roleRepository.findByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(Role::getRoleCode).collect(Collectors.toList());

        String accessToken = jwtTokenProvider.generateToken(user.getUsername(), user.getId(), roleCodes);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setExpiresIn(86400000L);
        loginVO.setUserInfo(convertToVO(user));

        return loginVO;
    }

    @Override
    public void logout() {
        // 可以在这里处理token失效逻辑
    }

    @Override
    public UserInfoVO getCurrentUser() {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) {
            throw new BusinessException(401, "用户未登录");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return convertToVO(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    @Transactional
    public User createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        if (userDTO.getEmail() != null && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException("邮箱已被使用");
        }
        if (userDTO.getPhone() != null && userRepository.existsByPhone(userDTO.getPhone())) {
            throw new BusinessException("手机号已被使用");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRealName(userDTO.getRealName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAvatar(userDTO.getAvatar());
        user.setGender(userDTO.getGender() != null ? userDTO.getGender() : 0);
        user.setStatus(userDTO.getStatus() != null ? userDTO.getStatus() : 1);
        user.setAdmin(userDTO.getAdmin() != null ? userDTO.getAdmin() : false);
        user.setDeptId(userDTO.getDeptId());

        user = userRepository.save(user);

        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }

        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserDTO userDTO) {
        User user = getUserById(id);

        if (!user.getUsername().equals(userDTO.getUsername()) && userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new BusinessException("邮箱已被使用");
        }
        if (userDTO.getPhone() != null && !userDTO.getPhone().equals(user.getPhone()) && userRepository.existsByPhone(userDTO.getPhone())) {
            throw new BusinessException("手机号已被使用");
        }

        user.setUsername(userDTO.getUsername());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setRealName(userDTO.getRealName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAvatar(userDTO.getAvatar());
        user.setGender(userDTO.getGender());
        user.setStatus(userDTO.getStatus());
        user.setAdmin(userDTO.getAdmin());
        user.setDeptId(userDTO.getDeptId());

        user = userRepository.save(user);

        if (userDTO.getRoleIds() != null) {
            assignRoles(user.getId(), userDTO.getRoleIds());
        }

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        user.setDeleted(true);
        userRepository.save(user);
        userRoleRepository.deleteByUserId(id);
    }

    @Override
    @Transactional
    public void batchDeleteUsers(List<Long> ids) {
        for (Long id : ids) {
            deleteUser(id);
        }
    }

    @Override
    @Transactional
    public void changePassword(Long id, PasswordDTO passwordDTO) {
        User user = getUserById(id);

        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateAvatar(Long id, String avatarUrl) {
        User user = getUserById(id);
        user.setAvatar(avatarUrl);
        userRepository.save(user);
    }

    @Override
    public PageResult<UserInfoVO> getUserList(String keyword, Integer status, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<User> page = userRepository.findByKeywordAndStatus(keyword, status, pageable);

        List<UserInfoVO> list = page.getContent().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(list, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void assignRoles(Long userId, List<Long> roleIds) {
        userRoleRepository.deleteByUserId(userId);

        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoles = roleIds.stream()
                    .map(roleId -> new UserRole(userId, roleId))
                    .collect(Collectors.toList());
            userRoleRepository.saveAll(userRoles);
        }
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        User user = getUserById(id);
        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(Long id) {
        User user = getUserById(id);
        user.setPassword(passwordEncoder.encode("123456"));
        userRepository.save(user);
    }

    @Override
    public UserInfoVO convertToVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setAvatar(user.getAvatar());
        vo.setGender(user.getGender());
        vo.setStatus(user.getStatus());
        vo.setAdmin(user.getAdmin());
        vo.setDeptId(user.getDeptId());
        vo.setLastLoginTime(user.getLastLoginTime());

        List<Role> roles = roleRepository.findByUserId(user.getId());
        vo.setRoles(roles.stream().map(Role::getRoleCode).collect(Collectors.toList()));

        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        if (!roleIds.isEmpty()) {
            List<Menu> menus = menuRepository.findByRoleIds(roleIds);
            vo.setPermissions(menus.stream()
                    .filter(m -> m.getPermission() != null)
                    .map(Menu::getPermission)
                    .distinct()
                    .collect(Collectors.toList()));
        } else {
            vo.setPermissions(new ArrayList<>());
        }

        return vo;
    }
}
