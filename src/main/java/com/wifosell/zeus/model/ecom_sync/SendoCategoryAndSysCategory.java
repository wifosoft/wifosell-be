package com.wifosell.zeus.model.ecom_sync;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "sendo_category_and_sys_category",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sendo_category_id", "sys_category_id", "general_manager_id"}))
public class SendoCategoryAndSysCategory extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sendo_category_id", referencedColumnName = "id")
    SendoCategory sendoCategory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sys_category_id", referencedColumnName = "id")
    Category sysCategory;

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
