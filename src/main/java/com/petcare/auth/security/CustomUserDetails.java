package com.petcare.auth.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.petcare.domain.user.User;
import com.petcare.enums.Role;
import com.petcare.utils.RoleUtils;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public Role getRole() {
        return user.getRole();
    }

    // Métodos delegados con RoleChecker
    public boolean isAdmin() {
        return RoleUtils.isAdmin(user);
    }

    public boolean isClient() {
        return RoleUtils.isClient(user);
    }

    public boolean isEmployee() {
        return RoleUtils.isEmployee(user);
    }
}