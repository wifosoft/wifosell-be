package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.attribute.Attribute;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.option.OptionModel;
import com.wifosell.zeus.model.option.OptionValue;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.product.ProductImage;
import com.wifosell.zeus.model.product.Variant;
import com.wifosell.zeus.model.product.VariantValue;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.product.AddProductRequest;
import com.wifosell.zeus.payload.request.product.IProductRequest;
import com.wifosell.zeus.repository.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductSeeder extends BaseSeeder implements ISeeder {
    private ProductRepository productRepository;
    private ProductImageRepository productImageRepository;
    private AttributeRepository attributeRepository;
    private OptionRepository optionRepository;
    private OptionValueRepository optionValueRepository;
    private VariantRepository variantRepository;
    private VariantValueRepository variantValueRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Override
    public void prepareJpaRepository() {
        this.productRepository = this.factory.getRepository(ProductRepository.class);
        this.productImageRepository = this.factory.getRepository(ProductImageRepository.class);
        this.attributeRepository = this.factory.getRepository(AttributeRepository.class);
        this.optionRepository = this.factory.getRepository(OptionRepository.class);
        this.optionValueRepository = this.factory.getRepository(OptionValueRepository.class);
        this.variantRepository = this.factory.getRepository(VariantRepository.class);
        this.variantValueRepository = this.factory.getRepository(VariantValueRepository.class);
        this.categoryRepository = this.factory.getRepository(CategoryRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/java/com/wifosell/zeus/database/data/product.json");

        try {
            AddProductRequest[] requests = mapper.readValue(file, AddProductRequest[].class);
            for (AddProductRequest request : requests) {
                this.updateProductByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateProductByRequest(IProductRequest request, User gm) {
        Product product = new Product();

        Optional.ofNullable(request.getName()).ifPresent(product::setName);
        Optional.ofNullable(request.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(request.getCategoryId()).ifPresent(categoryId -> {
            Category category = categoryRepository.getById(categoryId);
            product.setCategory(category);
        });
        Optional.ofNullable(request.getWeight()).ifPresent(product::setWeight);
        Optional.ofNullable(request.getDimension()).ifPresent(product::setDimension);
        Optional.ofNullable(request.getState()).ifPresent(product::setState);
        Optional.ofNullable(request.getStatus()).ifPresent(product::setStatus);

        // Images
        Optional.ofNullable(request.getImages()).ifPresent(urls -> {
            productImageRepository.deleteAllByProductId(product.getId());
            product.getImages().clear();

            List<ProductImage> images = new ArrayList<>();
            for (String url : urls) {
                ProductImage image = ProductImage.builder()
                        .url(url)
                        .product(product).build();
                images.add(image);
            }
            product.getImages().addAll(images);

            productImageRepository.saveAll(images);
            productRepository.save(product);
        });

        // Attributes
        Optional.ofNullable(request.getAttributes()).ifPresent(attributeRequests -> {
            attributeRepository.deleteAllByProductId(product.getId());
            product.getAttributes().clear();

            List<Attribute> attributes = new ArrayList<>();
            for (IProductRequest.AttributeRequest attributeRequest : attributeRequests) {
                Attribute attribute = Attribute.builder()
                        .name(attributeRequest.getName())
                        .value(attributeRequest.getValue())
                        .product(product)
                        .build();
                attributes.add(attribute);
            }
            product.getAttributes().addAll(attributes);

            attributeRepository.saveAll(attributes);
            productRepository.save(product);
        });

        // Options & Variants
        Optional.ofNullable(request.getOptions()).ifPresent(optionRequests -> {
            optionRepository.deleteAllByProductId(product.getId());
            product.getOptions().clear();

            List<OptionModel> optionModels = new ArrayList<>();
            for (IProductRequest.OptionRequest optionRequest : optionRequests) {
                OptionModel optionModel = OptionModel.builder()
                        .name(optionRequest.getName())
                        .product(product).build();
                List<OptionValue> optionValues = new ArrayList<>();
                for (String value : optionRequest.getValues()) {
                    OptionValue optionValue = OptionValue.builder()
                            .value(value)
                            .option(optionModel).build();
                    optionValues.add(optionValue);
                }
                optionModel.setOptionValues(optionValues);

                optionModels.add(optionModel);

                optionValueRepository.saveAll(optionValues);
                optionRepository.save(optionModel);
            }
            product.getOptions().addAll(optionModels);

            productRepository.save(product);
        });

        // Variants
        Optional.ofNullable(request.getVariants()).ifPresent(variantRequests -> {
            variantRepository.deleteAllByProductId(product.getId());
            product.getVariants().clear();
            this.genVariants(product, product.getOptions(), variantRequests);
            productRepository.save(product);
        });

        Optional.ofNullable(request.getActive()).ifPresent(product::setIsActive);
        product.setGeneralManager(gm);

        productRepository.save(product);
    }

    private void genVariants(Product product, List<OptionModel> options, List<IProductRequest.VariantRequest> variantRequests) {
        List<OptionValue> combination = Arrays.asList(new OptionValue[options.size()]);
        int i = 0, j = 0, k = 0;
        this.genVariants(product, options, variantRequests, combination, i, j, k);
    }

    private int genVariants(Product product, List<OptionModel> options, List<IProductRequest.VariantRequest> variantRequests, List<OptionValue> combination, int i, int j, int k) {
        if (i == options.size()) {
            IProductRequest.VariantRequest variantRequest = variantRequests.get(j);
            Variant variant = Variant.builder()
                    .stock(variantRequest.getStock())
                    .cost(new BigDecimal(variantRequest.getCost()))
                    .sku(variantRequest.getSku())
                    .barcode(variantRequest.getBarcode())
                    .product(product).build();

            List<VariantValue> variantValues = new ArrayList<>();
            for (OptionValue optionValue : combination) {
                VariantValue variantValue = VariantValue.builder()
                        .optionValue(optionValue)
                        .variant(variant).build();
                variantValues.add(variantValue);
            }

            variant.setVariantValues(variantValues);
            product.getVariants().add(variant);

            variantValueRepository.saveAll(variantValues);
            variantRepository.save(variant);
            return ++j;
        }

        for (OptionValue optionValue : options.get(i).getOptionValues()) {
            combination.set(k, optionValue);
            j = genVariants(product, options, variantRequests, combination, i + 1, j, k + 1);
        }

        return j;
    }
}
