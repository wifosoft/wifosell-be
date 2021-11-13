package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.category.Category;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.CategoryRepository;
import com.wifosell.zeus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategorySeeder extends BaseSeeder implements ISeeder {
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public CategorySeeder(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void prepareJpaRepository() {
        this.categoryRepository = this.factory.getRepository(CategoryRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Override
    public void run() {
        User manager1 = userRepository.getUserByName("manager1");

        Category category1 = Category.builder()
                .name("Thoi trang")
                .description("Thoi trang cao cap")
                .shortName("TT")
                .generalManager(manager1).build();
        categoryRepository.save(category1);

        Category category11 = Category.builder()
                .name("Thoi trang nam")
                .description("Thoi trang nam cao cap")
                .shortName("TTN")
                .generalManager(manager1)
                .parent(category1).build();
        categoryRepository.save(category11);

        Category category111 = Category.builder()
                .name("Thoi trang nam tre em")
                .description("Thoi trang nam tre em cao cap")
                .shortName("TTNTE")
                .generalManager(manager1)
                .parent(category11).build();
        categoryRepository.save(category111);

        Category category112 = Category.builder()
                .name("Thoi trang nam nguoi lon")
                .description("Thoi trang nam nguoi lon cao cap")
                .shortName("TTNNL")
                .generalManager(manager1)
                .parent(category11).build();
        categoryRepository.save(category112);

        Category category12 = Category.builder()
                .name("Thoi trang nu")
                .description("Thoi trang nu cao cap")
                .shortName("TTNu")
                .generalManager(manager1)
                .parent(category1).build();
        categoryRepository.save(category12);

        Category category2 = Category.builder()
                .name("Cong nghe")
                .description("Mat hang cong nghe")
                .shortName("CN")
                .generalManager(manager1).build();
        categoryRepository.save(category2);

        Category category21 = Category.builder()
                .name("Dien thoai")
                .description("Dien thoai thong minh cao cap")
                .shortName("DT")
                .generalManager(manager1)
                .parent(category2).build();
        categoryRepository.save(category21);

        Category category22 = Category.builder()
                .name("Tai nghe")
                .description("Tai nghe, tai nghe khong day")
                .shortName("TN")
                .generalManager(manager1)
                .parent(category2).build();
        categoryRepository.save(category22);
    }
}
