package com.wifosell.zeus.model.ecom_sync;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.wifosell.zeus.model.audit.BasicEntity;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.provider.lazada.ResponseSellerInfoPayload;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoSellerInfoPayload;
import com.wifosell.zeus.payload.request.ecom_sync.EcomAccountLazadaCallbackPayload;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcomAccount extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private EcomName ecomName;

    @Column(name = "account_name")
    private String accountName;

    @Column(columnDefinition = "TEXT")
    private String accountInfo; //token , etc ...

    @Column(columnDefinition = "TEXT")
    private String authResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @Column(name = "expiredAt")
    LocalDateTime expiredAt;


    @Column(columnDefinition = "TEXT")
    private String description;

    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "ecomAccount", fetch = FetchType.LAZY)
    private List<LazadaProduct> lazadaProduct;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "general_manager_id", referencedColumnName = "id")
    private User generalManager;

    public enum EcomName {
        LAZADA,
        SHOPEE,
        TIKI,
        SENDO
    }

    public enum AccountStatus {
        NONE,
        NO_AUTH,
        AUTH,
        EXPIRED,
        BLOCKED
    }

    public EcomAccountLazadaCallbackPayload parseLazadaAuthPayload(){
        return (new Gson()).fromJson(this.getAuthResponse(), EcomAccountLazadaCallbackPayload.class);
    }

    public ResponseSellerInfoPayload parseLazadaSellerInfoPayload(){
        return (new Gson()).fromJson(this.getAccountInfo(), ResponseSellerInfoPayload.class);
    }

    public ResponseSendoSellerInfoPayload parseSendoSellerInfoPayload() {
        return (new Gson()).fromJson(this.getAccountInfo(), ResponseSendoSellerInfoPayload.class);

    }
    public String getShopName() {
        switch(this.getEcomName()){
            case LAZADA:
                ResponseSellerInfoPayload payload = this.parseLazadaSellerInfoPayload();
                if(payload == null) return null;
                return payload.getData().name;
            case SENDO:
                ResponseSendoSellerInfoPayload payloadSendo = this.parseSendoSellerInfoPayload();
                if(payloadSendo == null) return null;
                return payloadSendo.getData().name;
        }
        return null;
    }


    public String getShopId() {
        switch(this.getEcomName()){
            case LAZADA:
                ResponseSellerInfoPayload payload = this.parseLazadaSellerInfoPayload();
                return payload.getData().seller_id;
        }
        return null;
    }


    @JsonIgnore
    public String getAccessToken() {
        switch(this.getEcomName()){
            case LAZADA:
                EcomAccountLazadaCallbackPayload payload = this.parseLazadaAuthPayload();
                return payload.getTokenAuthResponse().getAccess_token();
        }
        return null;
    }
}
