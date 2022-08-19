package com.wifosell.zeus.service.impl.ecom_sync;

import com.google.gson.Gson;
import com.wifosell.zeus.config.property.AppProperties;
import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProduct;
import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProductVariantShortInfo;
import com.wifosell.zeus.exception.ZeusGlobalException;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.ecom_sync.*;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.order.Payment;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.shop.SaleChannelShop;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.stock.Stock;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.payload.provider.shopee.SendoServiceResponseBase;
import com.wifosell.zeus.payload.provider.shopee.hook.ResponseSendoOrderCreateHookPayload;
import com.wifosell.zeus.payload.request.ecom_sync.SendoCreateOrUpdateProductPayload;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequestDTO;
import com.wifosell.zeus.payload.request.ecom_sync.SendoLinkAccountRequestDTOWithModel;
import com.wifosell.zeus.payload.request.order.AddOrderRequest;
import com.wifosell.zeus.payload.request.product.UpdateProductRequest;
import com.wifosell.zeus.payload.response.product.ProductResponse;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.repository.ecom_sync.*;
import com.wifosell.zeus.service.*;
import com.wifosell.zeus.specs.ProductSpecs;
import com.wifosell.zeus.specs.SendoProductSpecs;
import com.wifosell.zeus.taurus.core.TaurusBus;
import com.wifosell.zeus.taurus.core.payload.KafkaPublishProductSendoPayload;
import com.wifosell.zeus.taurus.sendo.SendoServiceClient;
import com.wifosell.zeus.utils.ZeusUtils;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.jboss.jdeparser.FormatPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SendoOrderServiceImpl implements SendoOrderService {

    private Logger logger = LoggerFactory.getLogger(SendoOrderServiceImpl.class);


    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SendoCategoryAndSysCategoryRepository sendoCategoryAndSysCategoryRepository;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    VariantRepository variantRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    OptionValueRepository optionValueRepository;

    @Autowired
    LazadaSwwAndEcomAccountRepository lazadaSwwAndEcomAccountRepository;

    @Autowired
    SendoVariantRepository sendoVariantRepository;

    @Autowired
    SendoProductRepository sendoProductRepository;

    @Autowired
    EcomAccountRepository ecomAccountRepository;

    @Autowired
    SendoVariantAndSysVarirantRepository sendoVariantAndSysVarirantRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    AppProperties appProperties;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    SendoProductService sendoProductService;

    @Autowired
    EcomService ecomService;

    @Autowired
    OrderService orderService;


    @Override
    public void consumeOrderService(String shopKey, ResponseSendoOrderCreateHookPayload hookCreated) {
        Optional<EcomAccount> ecomAccountOpt = ecomAccountRepository.findFirstByAccountNameAndEcomName(shopKey, EcomAccount.EcomName.SENDO);
        if (ecomAccountOpt.isEmpty()) {
            logger.info("Shop key khong ton tai " + shopKey);
            return;
        }

        SaleChannelShop saleChannelShop;
        Warehouse warehouse;
        Optional<LazadaSwwAndEcomAccount> relationSwwEAOpt = lazadaSwwAndEcomAccountRepository.findByEcomAccountId(ecomAccountOpt.get().getId());
        if (relationSwwEAOpt.isEmpty()) {
            logger.info("Khong ton tai relation");
            return;
        } else {
            saleChannelShop = relationSwwEAOpt.get().getSaleChannelShop();
            warehouse = saleChannelShop.getWarehouse();
        }
        Shop shop = saleChannelShop.getShop();
        User gm = shop.getGeneralManager();

        var orderData = hookCreated.getData();
        if (orderData == null) {
            logger.info("Data order is null");
            return;
        }
        //Build request order
        AddOrderRequest addOrderRequest = new AddOrderRequest();
        addOrderRequest.setShopId(shop.getId());
        addOrderRequest.setSaleChannelId(saleChannelShop.getId());

        var paymentRequest = new AddOrderRequest.PaymentRequest();
        if (hookCreated.getData().getPayment_method() == 1) {
            paymentRequest.setMethod(Payment.METHOD.COD);
        } else {
            paymentRequest.setMethod(Payment.METHOD.BANKING);
        }

        if (hookCreated.getData().getPayment_status() == 1) {
            paymentRequest.setStatus(Payment.STATUS.UNPAID);
        } else {
            paymentRequest.setStatus(Payment.STATUS.PAID);
        }
        paymentRequest.setInfo(hookCreated.getData().getNote());
        addOrderRequest.setPayment(paymentRequest);
        // Process sale order detail
        Optional.of(orderData.getShipping_fee()).ifPresent(e -> {
            addOrderRequest.setShippingFee(new BigDecimal(e));
        });
        addOrderRequest.setIsActive(true);
        //Customer id
        //find by phone
        Customer customer = customerRepository.getCustomerByPhone(orderData.getBuyer_phone());
        if (customer == null) {
            customer = new Customer();
            Optional.of(orderData.getBuyer_name()).ifPresent(customer::setFullName);
            Optional.of(orderData.getBuyer_address()).ifPresent(customer::setAddressDetail);
            Optional.of(orderData.getBuyer_phone()).ifPresent(customer::setPhone);
            customer.setGeneralManager(gm);
            customer = customerRepository.save(customer);
        }
        AddOrderRequest.ShippingDetail shippingDetail = new AddOrderRequest.ShippingDetail();
        Optional.of(orderData.getShipping_contact_phone()).ifPresent(shippingDetail::setContactPhone);
        Optional.of(orderData.getShip_to_address()).ifPresent(shippingDetail::setContactAddress);
        Optional.of(orderData.getShip_to_contact_email()).ifPresent(shippingDetail::setContactEmail);
        Optional.of(orderData.getShip_to_contact_name()).ifPresent(shippingDetail::setContactName);

        addOrderRequest.setShippingDetail(shippingDetail);
        //
        addOrderRequest.setCustomerId(customer.getId());
        addOrderRequest.setOrderSource(1);

        //process order items

        List<AddOrderRequest.OrderItem> listOrderItems = new ArrayList<>();
        for (var saleItem : orderData.getSales_order_details()) {
            SendoProduct sendoProduct = sendoProductRepository.findFirstByItemId((long) saleItem.getProduct_variant_id());
            if (sendoProduct == null) {
                logger.info("[-] Sendo Product chưa tồn tại {}", saleItem.getProduct_variant_id());
                continue;
            }

            String saleOrderItemProductSku = saleItem.getStore_sku();
            String prefixProductIdentify = sendoProduct.getProductIdentify();
            String identifyVariantSku = saleOrderItemProductSku.replaceAll(prefixProductIdentify + "-", "");


            Variant variant = variantRepository.getBySku(identifyVariantSku);
            if (variant == null) {
                logger.info("[-] Alert variant sku not found {}", saleItem.getStore_sku());
                continue;
            }
            AddOrderRequest.OrderItem orderItem = new AddOrderRequest.OrderItem();
            orderItem.setVariantId(variant.getId());
            orderItem.setQuantity(saleItem.getQuantity());
            listOrderItems.add(orderItem);
        }
        addOrderRequest.setOrderItems(listOrderItems);

        //use service to add new order
        OrderModel order = orderService.addOrder(gm.getId(), addOrderRequest);


    }
}
