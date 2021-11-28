package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.product.Product;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.ProductRepository;
import com.wifosell.zeus.repository.UserRepository;

public class ProductSeeder extends BaseSeeder implements ISeeder {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Override
    public void prepareJpaRepository() {
        this.productRepository = this.factory.getRepository(ProductRepository.class);
        this.categoryRepository = this.factory.getRepository(CategoryRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();

        Product product1 = Product.builder()
                .name("Ao thun mua dong nam")
                .sku("AT001A2X")
                .barcode("1011120019")
                .category(categoryRepository.findCategoryById(3L))
                .weight(850)
                .dimension("50 x 100 (cm)")
                .state(0)
                .status(0)
                .stock(10)
                .generalManager(gm)
                .build();
        productRepository.save(product1);

        Product product2 = Product.builder()
                .name("Ao thun mua he nam")
                .sku("AT112Y2Z")
                .barcode("1281200121")
                .category(categoryRepository.findCategoryById(3L))
                .weight(850)
                .dimension("60 x 100 (cm)")
                .state(0)
                .status(0)
                .stock(50)
                .generalManager(gm)
                .build();
        productRepository.save(product2);

        Product product11 = Product.builder()
                .name("Ao thun mua dong nam tay dai")
                .sku("AT011A2X")
                .barcode("1021120019")
                .category(categoryRepository.findCategoryById(3L))
                .weight(850)
                .dimension("50 x 100 (cm)")
                .state(0)
                .status(0)
                .stock(30)
                .parent(product1)
                .generalManager(gm)
                .build();
        productRepository.save(product11);
    }
}
