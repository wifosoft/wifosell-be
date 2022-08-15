package com.wifosell.lazada.modules.product.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;
import java.util.Map;

@JacksonXmlRootElement(localName = "Request")
public class LazadaCreateProductRequest {
    @JacksonXmlProperty(localName = "Product")
    public Product product;

    public static class Product {
        @JacksonXmlProperty(localName = "PrimaryCategory")
        public Long primaryCategory;

        @JacksonXmlElementWrapper(localName = "Images")
        @JacksonXmlProperty(localName = "Image")
        public List<String> images;

        @JacksonXmlProperty(localName = "Attributes")
        public Map<String, String> attributes;

        @JacksonXmlProperty(localName = "variation")
        public Map<String, Variation> variations;

        @JacksonXmlElementWrapper(localName = "Skus")
        @JacksonXmlProperty(localName = "Sku")
        public List<Map<String, Object>> skus;
    }

    public static class Attribute {
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String BRAND = "brand";
        public static final String BRAND_DEFAULT_VALUE = "No Brand";
    }

    public static class Variation {
        public static String TAG_PREFIX = "variation";

        @JacksonXmlProperty(localName = "name")
        public String name;

        @JacksonXmlProperty(localName = "hasImage")
        public boolean hasImage;

        @JacksonXmlProperty(localName = "customize")
        public boolean customize;

        @JacksonXmlElementWrapper(localName = "options")
        @JacksonXmlProperty(localName = "option")
        public List<String> options;
    }

    public static class Sku {
        public static final String SELLER_SKU = "SellerSku";
        public static final String QUANTITY = "quantity";
        public static final String PRICE = "price";
        public static final String SPECIAL_PRICE = "special_price";
        public static final String SPECIAL_FROM_DATE = "special_from_date";
        public static final String SPECIAL_TO_DATE = "special_to_date";
        public static final String PACKAGE_HEIGHT = "package_height";
        public static final String PACKAGE_LENGTH = "package_length";
        public static final String PACKAGE_WIDTH = "package_width";
        public static final String PACKAGE_WEIGHT = "package_weight";
        public static final String PACKAGE_CONTENT = "package_content";
        public static final String IMAGES = "Images";

        public static final String SPECIAL_FROM_DATE_DEFAULT_VALUE = "2022-01-01 00:00:00";
        public static final String SPECIAL_TO_DATE_DEFAULT_VALUE = "2122-01-01 00:00:00";
    }

    public String toXml() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return xmlMapper.writeValueAsString(this);
    }
}
