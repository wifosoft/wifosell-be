package com.wifosell.zeus.model.warehouse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Warehouse extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;
    private String shortName;
    @Size(max=255)

    private String address;
    @Size(max=20)
    @Column(name = "phone")
    private String phone;

    @Size(max=255)
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;



}
