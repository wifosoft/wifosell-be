package com.wifosell.zeus.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.permission.UserPermission;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.UserShopRelation;
import com.wifosell.zeus.model.warehouse.Warehouse;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApiIgnore
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Builder
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @GenericField
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
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 100)
    @Column(name = "password")
    private String password;

    @NotBlank
    //@NaturalId
    @Size(max = 40)
    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @NotBlank
    @Size(max = 255)
    private String address;


    @Column(name = "phone")
    @Size(max = 20)
    private String phone;

    /*
    v1_join table
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;
    */
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    Set<UserRoleRelation> userRoleRelation;

    //Danh sách shop tạo bởi GM
    @JsonIgnore
    @OneToMany(mappedBy = "generalManager", fetch = FetchType.LAZY)
    List<Shop> listCreatedShops;

    //Danh sách warehouse tạo bởi GM
    @JsonIgnore
    @OneToMany(mappedBy = "generalManager", fetch = FetchType.LAZY)
    List<Warehouse> listCreatedWarehouses;


    //Quan hệ giữa shop và user. User có quyền quản lý shop X
    //@JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    Set<UserShopRelation> userShopRelation;

    @JsonIgnore
    public List<Shop> getAccessShops() {
        if (userShopRelation == null) {
            return new ArrayList<>();
        }
        return this.userShopRelation.stream().map(UserShopRelation::getShop).collect(Collectors.toList());
    }


/*

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@JsonBackReference
    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_manage_shop",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "shop_id", referencedColumnName = "id")
    )
    private List<Shop> shops;
*/

    @Type(type = "json")
    @Column(columnDefinition = "json")
    @Enumerated(EnumType.STRING)
    private List<UserPermission> userPermission;


    @JsonIgnore
    @JoinColumn(name = "parent_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private User parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> childrenUsers;

    @JsonIgnore
    @OneToMany(mappedBy = "generalManager")
    private List<OptionModel> options;

    @JsonIgnore
    @OneToMany(mappedBy = "generalManager")
    private List<Customer> customers;

    @JsonIgnore
    @OneToMany(mappedBy = "generalManager")
    private List<Product> products;

    public User(String firstName, String lastName, String username, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

//    public List<Role> getRoles() {
//
//        return roles == null ? null : new ArrayList<>(roles);
//    }
//
//    public void setRoles(List<Role> roles) {
//        if (roles == null) {
//            this.roles = null;
//        } else {
//            this.roles = Collections.unmodifiableList(roles);
//        }
//    }

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

    @JsonIgnore
    public User getGeneralManager() {
        User gm = this;
        while (!gm.isRoot()) {
            gm = gm.getParent();
        }
        return gm;
    }
}
