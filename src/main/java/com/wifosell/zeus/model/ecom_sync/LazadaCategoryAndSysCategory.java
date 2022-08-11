package com.wifosell.zeus.model.ecom_sync;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lazada_category_and_sys_category",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lazada_category_id", "sys_category_id", "general_manager_id"})
)
public class LazadaCategoryAndSysCategory extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lazada_category_id", referencedColumnName = "id")
    LazadaCategory lazadaCategory;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sys_category_id", referencedColumnName = "id")
    Category sysCategory;

    @Transient
    @JsonProperty("merge_key")
    public String getMergeKey() {
        if (lazadaCategory == null || sysCategory == null) {
            return "";
        }
        return lazadaCategory.getId() + "_" + sysCategory.getId();
    }

    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    private User generalManager;
}
