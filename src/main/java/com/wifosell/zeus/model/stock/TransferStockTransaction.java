package com.wifosell.zeus.model.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferStockTransaction extends BasicEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Warehouse fromWarehouse;

    @ManyToOne
    private Warehouse toWarehouse;

    @Enumerated(EnumType.STRING)
    private TYPE type;

    private String source;

    @Enumerated(EnumType.STRING)
    private PROCESSING_STATUS processingStatus;

    @Column(columnDefinition = "LONGTEXT")
    private String processingNote;

    @Column(columnDefinition = "text")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "transaction", cascade = {CascadeType.ALL})
    private List<TransferStockTransactionItem> items = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;

    public enum PROCESSING_STATUS {
        DRAFT,
        QUEUED,
        PROCESSING,
        PROCESSED
    }

    public enum TYPE {
        MANUAL,
        EXCEL
    }
}
