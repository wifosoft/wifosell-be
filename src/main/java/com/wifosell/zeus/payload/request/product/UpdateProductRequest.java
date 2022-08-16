package com.wifosell.zeus.payload.request.product;

import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProduct;
import com.wifosell.zeus.consumer.payload.KafkaWrapperConsumeProductVariantShortInfo;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class UpdateProductRequest implements IProductRequest {
    private String name;

    private String description;

    private Long categoryId;

    private BigDecimal weight;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private Integer state;

    private Integer status;

    private List<ImageRequest> images;

    private List<AttributeRequest> attributes;

    private List<OptionRequest> options;

    private List<VariantRequest> variants;

    private Boolean isActive;

    public static KafkaWrapperConsumeProduct withResponseSendoProductItemPayload(ResponseSendoProductItemPayload e) {
        KafkaWrapperConsumeProduct kafkaWrapperConsumeProduct = new KafkaWrapperConsumeProduct();

        UpdateProductRequest m = new UpdateProductRequest();
        Optional.ofNullable(e.getName()).ifPresent(m::setName);
        Optional.ofNullable(e.getDescription()).ifPresent(m::setDescription);
        //Optional.ofNullable(null).ifPresent(m::setCategoryId);
        Optional.ofNullable(e.getWeight()).ifPresent(v -> {
            m.setWeight(new BigDecimal(v));
        });
        Optional.ofNullable(e.getHeight()).ifPresent(v -> {
            m.setHeight(new BigDecimal(v));
        });
        Optional.ofNullable(e.getLength()).ifPresent(v -> {
            m.setLength(new BigDecimal(v));
        });
        Optional.ofNullable(e.getWidth()).ifPresent(v -> {
            m.setWidth(new BigDecimal(v));
        });
        m.setState(0);
        m.setStatus(0);

        //list pictures
        ArrayList<ImageRequest> listImageRequest = new ArrayList<ImageRequest>();
        if (e.getPictures() != null) {
            for (var itemPicture : e.getPictures()) {
                ImageRequest imgRequest = new ImageRequest();
                imgRequest.setUrl(itemPicture.getPicture_url());
                listImageRequest.add(imgRequest);
            }
        }
        m.setImages(listImageRequest);
        //
        ArrayList<OptionRequest> optionRequestList = new ArrayList<OptionRequest>();
        ArrayList<VariantRequest> varirantRequests = new ArrayList<VariantRequest>();

        List<ResponseSendoProductItemPayload.Variant> listVariantAPI = e.getVariants();

        if (listVariantAPI.size() == 0) {
            //khong ton tai variant

            VariantRequest variantRequest = new VariantRequest();
            variantRequest.originalCost = e.getPrice().toString();
            variantRequest.cost = e.getPrice().toString();
            variantRequest.sku = e.getSku();
            variantRequest.barcode = e.getSku();
            varirantRequests.add(variantRequest);

        }
        HashMap<Long, List<Long>> hashMapOptions = null; //attribute_id, option_id
        for (ResponseSendoProductItemPayload.Variant variantAPIItem : listVariantAPI) {
            if (hashMapOptions == null) {
                hashMapOptions = new HashMap<>();
            }

            List<ResponseSendoProductItemPayload.VariantAttribute> listVariantAPIAttribute = variantAPIItem.getVariant_attributes();

            for (ResponseSendoProductItemPayload.VariantAttribute attr_item : listVariantAPIAttribute) {
                Long attr_id = attr_item.getAttribute_id();
                Long opt_id = attr_item.getOption_id();
                if (!hashMapOptions.containsKey(attr_id)) {
                    hashMapOptions.put(attr_id, new ArrayList<Long>());
                }
                if (!hashMapOptions.get(attr_id).contains(opt_id)) {
                    hashMapOptions.get(attr_id).add(opt_id);
                }
            }

            VariantRequest variantRequest = new VariantRequest();
            variantRequest.originalCost = variantAPIItem.getVariant_price().toString();
            variantRequest.cost = variantAPIItem.getVariant_price().toString();
            variantRequest.sku = variantAPIItem.getVariant_sku();
            variantRequest.barcode = variantAPIItem.getVariant_sku();
            varirantRequests.add(variantRequest);

            KafkaWrapperConsumeProductVariantShortInfo kafkaWrapperConsumeProductVariantShortInfo = new KafkaWrapperConsumeProductVariantShortInfo();
            kafkaWrapperConsumeProductVariantShortInfo.setSku(variantAPIItem.getVariant_sku());
            kafkaWrapperConsumeProductVariantShortInfo.setSpecialPrice(variantAPIItem.getVariant_price());
            kafkaWrapperConsumeProductVariantShortInfo.setPrice(variantAPIItem.getVariant_price());
            kafkaWrapperConsumeProductVariantShortInfo.setQuantity(variantAPIItem.getVariant_quantity());
            kafkaWrapperConsumeProduct.getListVariants().add(kafkaWrapperConsumeProductVariantShortInfo);

        }


        //build options
        ArrayList<ResponseSendoProductItemPayload.Attribute> varirantAPIAttributes = e.getAttributes();
        if (hashMapOptions != null) {
            for (ResponseSendoProductItemPayload.Attribute _itemVariantAPIAttribute : varirantAPIAttributes) {
                if (!hashMapOptions.containsKey(_itemVariantAPIAttribute.getAttribute_id())) {
                    continue;
                }
                //khởi tạo option request
                OptionRequest _optRequest = new OptionRequest();
                _optRequest.setName(_itemVariantAPIAttribute.getAttribute_name());
                ArrayList<OptionValueRequest> _optionValueRequestList = new ArrayList<>();
                //tồn tại
                for (ResponseSendoProductItemPayload.AttributeValue _attributeValueItem : _itemVariantAPIAttribute.getAttribute_values()) {
                    if (hashMapOptions.get(_itemVariantAPIAttribute.getAttribute_id()).contains(_attributeValueItem.getId())) {
                        //nếu tồn tại thì thêm option value
                        OptionValueRequest _optionValueRequest = new OptionValueRequest();
                        _optionValueRequest.setName(_attributeValueItem.getValue());
                        _optionValueRequestList.add(_optionValueRequest);
                    }
                }
                _optRequest.setValues(_optionValueRequestList);
                optionRequestList.add(_optRequest);
                //luu _optRequest
            }
        }
        //
        m.setAttributes(new ArrayList<>());
        m.setOptions(optionRequestList);
        m.setVariants(varirantRequests);
        m.setIsActive(true);
        kafkaWrapperConsumeProduct.setUpdateProductRequest(m);
        return kafkaWrapperConsumeProduct;
    }
}
