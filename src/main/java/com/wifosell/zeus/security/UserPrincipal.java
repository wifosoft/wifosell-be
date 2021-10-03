package com.wifosell.zeus.security;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wifosell.zeus.model.user.User;
import io.swagger.annotations.ApiModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class UserPrincipal implements UserDetails {
    private static final long serialVersionUID = 5860776383746103616L;

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String phone;

    @JsonIgnore
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private User parent;

    

    public UserPrincipal( Long id, String firstName, String lastName, String username, String email, String password, String phone,
                         Collection<? extends GrantedAuthority> authorities, User parent) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.parent = parent;

        if (authorities == null) {
            this.authorities = null;
        } else {
            this.authorities = new ArrayList<>(authorities);
        }
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getUserRoleRelation().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getName().name())).collect(Collectors.toList());

        List<GrantedAuthority> permissions = user.getUserPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name())).collect(Collectors.toList());

        List<GrantedAuthority> allPermission = Stream.concat(authorities.stream(), permissions.stream())
                .collect(Collectors.toList());
        User _parent = user.getParent();

        return new UserPrincipal(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getPassword(), user.getPhone(), allPermission, _parent);
    }

    public User getParent() {
        return this.parent;
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Collection<String> getHumanAuthorities() {
        return authorities == null ? null : getAuthorities().stream().map(simple_grant -> simple_grant.getAuthority().toString()).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities == null ? null : new ArrayList<>(authorities);
    }


    public String getPhone() {
        return phone;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) object;
        return Objects.equals(id, that.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
