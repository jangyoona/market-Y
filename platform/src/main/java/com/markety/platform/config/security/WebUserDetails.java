package com.markety.platform.config.security;

import com.markety.platform.dto.RoleDto;
import com.markety.platform.dto.UserDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WebUserDetails implements UserDetails {

    @Getter
    private UserDto user;

    private List<RoleDto> roles;

    public WebUserDetails() {}
    public WebUserDetails(UserDto user) {
        this.user = user;
    }
    public WebUserDetails(UserDto user, List<RoleDto> roles) {
        this.user = user;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // 권한 목록을 주는 오버라이딩 메서드

        ArrayList<SimpleGrantedAuthority> grants = new ArrayList<>();
        for (RoleDto role : roles) {
            grants.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return grants;
    }



    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

}
