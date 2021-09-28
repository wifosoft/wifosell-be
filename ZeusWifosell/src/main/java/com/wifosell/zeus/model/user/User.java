package com.wifosell.zeus.model.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.wifosell.zeus.model.audit.DateAudit;
import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.shop.Shop;
import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApiIgnore
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})})
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Builder
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    @Size(max = 40)
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    @Size(max = 40)
    private String lastName;


    @NotBlank
    @Column(name = "username", unique = true)
    @Size(min=3,  max = 15)
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 100)
    @Column(name = "password")
    private String password;

    @NotBlank
    @NaturalId
    @Size(max = 40)
    @Column(name = "email", unique = true)
    @Email
    private String email;


    @Column(name = "phone")
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;


    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@JsonBackReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_manage_shop",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id", referencedColumnName = "id")
    )
    private List<Shop> shops;


    @Type(type = "json")
    @Column(columnDefinition = "json")
    @Enumerated(EnumType.STRING)
    private List<UserPermission> userPermission;


    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private User parent;


    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> childrenUsers;


    public User(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public List<Role> getRoles() {

        return roles == null ? null : new ArrayList<>(roles);
    }

    public void setRoles(List<Role> roles) {
        if (roles == null) {
            this.roles = null;
        } else {
            this.roles = Collections.unmodifiableList(roles);
        }
    }

    public List<UserPermission> getUserPermission() {

        return this.userPermission == null ? new ArrayList<>() : new ArrayList<UserPermission>(this.userPermission);
    }

    public void setUserPermission(List<UserPermission> userPermissions) {
        if (userPermissions == null) {
            this.userPermission = new ArrayList<>();
        } else {
            this.userPermission = Collections.unmodifiableList(userPermissions);
        }
    }

    //tài khoản root = true, General_manager
    public boolean isRoot() {
        return this.parent == null;
    }

}
