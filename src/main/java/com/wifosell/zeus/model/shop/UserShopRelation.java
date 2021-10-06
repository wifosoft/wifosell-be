package com.wifosell.zeus.model.shop;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.user.User;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;


@ApiIgnore
@Getter
@Setter
@Entity
public class UserShopRelation {
    @Id
    @Column(name = "id")
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
