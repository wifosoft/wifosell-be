package com.wifosell.zeus.model.stock;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.supplier.Supplier;
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
public class ImportStockTransaction extends BasicEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String excelFile;

    @ManyToOne
    private Warehouse warehouse;

    @ManyToOne
    private Supplier supplier;

    @Enumerated(EnumType.STRING)
    private TYPE type;

    @Enumerated(EnumType.STRING)
    private PROCESSING_STATUS processingStatus = PROCESSING_STATUS.PROCESSED; // default processed

    @Lob
    private String description;

    @Column(columnDefinition="LONGTEXT")
    private String processingNote;

    @OneToMany(mappedBy = "transaction", cascade = {CascadeType.ALL})
    private List<ImportStockTransactionItem> items = new ArrayList<>();


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;

    public enum PROCESSING_STATUS{
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
