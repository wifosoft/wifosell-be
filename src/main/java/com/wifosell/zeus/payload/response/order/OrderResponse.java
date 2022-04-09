package com.wifosell.zeus.payload.response.order;

import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.order.OrderItem;
import com.wifosell.zeus.model.order.OrderModel;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.ProductImage;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.sale_channel.SaleChannel;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.payload.response.BasicEntityResponse;
import com.wifosell.zeus.payload.response.category.CategoryResponse;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse extends BasicEntityResponse {
    private final List<OrderItemResponse> orderItems;
    private final ShopResponse shop;
    private final SaleChannelResponse saleChannel;
    private final CustomerResponse customer;
    private final BigDecimal subtotal;

    public OrderResponse(OrderModel order) {
        super(order);
        this.orderItems = order.getOrderItems().stream().map(OrderItemResponse::new).collect(Collectors.toList());
        this.shop = new ShopResponse(order.getShop());
        this.saleChannel = new SaleChannelResponse(order.getSaleChannel());
        this.customer = new CustomerResponse(order.getCustomer());
        this.subtotal = order.getSubtotal();
    }

    @Getter
    private static class OrderItemResponse extends BasicEntityResponse {
        private final BigDecimal price;
        private final Integer quantity;
        private final VariantResponse variant;

        public OrderItemResponse(OrderItem orderItem) {
            super(orderItem);
            this.price = orderItem.getPrice();
            this.quantity = orderItem.getQuantity();
            this.variant = new VariantResponse(orderItem.getVariant());
        }

        @Getter
        private static class VariantResponse extends BasicEntityResponse {
            private final Long stock;
            private final String cost;
            private final String sku;
            private final String barcode;
            private final List<OptionValueResponse> options;
            private final ProductResponse product;

            public VariantResponse(Variant variant) {
                super(variant);
                this.stock = variant.getStock();
                this.cost = variant.getCost().toString();
                this.sku = variant.getSku();
                this.barcode = variant.getBarcode();
                this.options = variant.getVariantValues().stream()
                        .map(VariantValue::getOptionValue)
                        .map(OptionValueResponse::new).collect(Collectors.toList());
                this.product = new ProductResponse(variant.getProduct());
            }

            @Getter
            private static class OptionValueResponse extends BasicEntityResponse {
                private final String value;

                public OptionValueResponse(OptionValue optionValue) {
                    super(optionValue);
                    this.value = optionValue.getValue();
                }
            }

            @Getter
            private static class ProductResponse extends BasicEntityResponse {
                private final String name;
                private final String description;
                private final Integer weight;
                private final String dimension;
                private final Integer state;
                private final Integer status;
                private final CategoryResponse category;
                private final List<String> images;
                private final List<AttributeResponse> attributes;
                private final List<OptionResponse> options;

                public ProductResponse(Product product) {
                    super(product);
                    this.name = product.getName();
                    this.description = product.getDescription();
                    this.weight = product.getWeight();
                    this.dimension = product.getDimension();
                    this.state = product.getState();
                    this.status = product.getStatus();
                    this.category = new CategoryResponse(product.getCategory());
                    this.images = product.getImages().stream().map(ProductImage::getUrl).collect(Collectors.toList());
                    this.attributes = product.getAttributes().stream().map(AttributeResponse::new).collect(Collectors.toList());
                    this.options = product.getOptions().stream().map(OptionResponse::new).collect(Collectors.toList());
                }

                @Getter
                private static class AttributeResponse extends BasicEntityResponse {
                    private final String name;
                    private final String value;

                    public AttributeResponse(Attribute attribute) {
                        super(attribute);
                        this.name = attribute.getName();
                        this.value = attribute.getValue();
                    }
                }

                @Getter
                private static class OptionResponse extends BasicEntityResponse {
                    private final String name;
                    private final List<OptionValueResponse> values;

                    OptionResponse(OptionModel option) {
                        super(option);
                        this.name = option.getName();
                        this.values = option.getOptionValues().stream().map(OptionValueResponse::new).collect(Collectors.toList());
                    }

                    @Getter
                    private static class OptionValueResponse extends BasicEntityResponse {
                        private final String value;

                        public OptionValueResponse(OptionValue optionValue) {
                            super(optionValue);
                            this.value = optionValue.getValue();
                        }
                    }
                }
            }
        }
    }

    @Getter
    private static class ShopResponse extends BasicEntityResponse {
        private final String name;
        private final String address;
        private final String phone;

        public ShopResponse(Shop shop) {
            super(shop);
            this.name = shop.getName();
            this.address = shop.getAddress();
            this.phone = shop.getPhone();
        }
    }

    @Getter
    private static class SaleChannelResponse extends BasicEntityResponse {
        private final String name;

        public SaleChannelResponse(SaleChannel saleChannel) {
            super(saleChannel);
            this.name = saleChannel.getName();
        }
    }

    @Getter
    private static class CustomerResponse extends BasicEntityResponse {
        private final String fullName;
        private final String dob;
        private final Integer sex;
        private final String phone;
        private final String email;
        private final String cin;
        private final String nation;
        private final String city;
        private final String district;
        private final String ward;
        private final String addressDetail;

        public CustomerResponse(Customer customer) {
            super(customer);
            this.fullName = customer.getFullName();
            this.dob = customer.getDob().toString();
            this.sex = customer.getSex().ordinal();
            this.phone = customer.getPhone();
            this.email = customer.getEmail();
            this.cin = customer.getCin();
            this.nation = customer.getNation();
            this.city = customer.getCity();
            this.district = customer.getDistrict();
            this.ward = customer.getWard();
            this.addressDetail = customer.getAddressDetail();
        }
    }
}
