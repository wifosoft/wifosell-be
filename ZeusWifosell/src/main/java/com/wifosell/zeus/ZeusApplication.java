package com.wifosell.zeus;

import com.wifosell.zeus.constant.DefaultUserPermission;
import com.wifosell.zeus.exception.AppException;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.RoleRepository;
import com.wifosell.zeus.repository.ShopRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.security.JwtAuthenticationFilter;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@EnableSwagger2
@SpringBootApplication
@EntityScan(basePackageClasses = {ZeusApplication.class, Jsr310Converters.class})
@Transactional
public class ZeusApplication implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ShopRepository shopRepository;
    @PersistenceContext
    EntityManager entityManager;


    public static void main(String[] args) {
        SpringApplication.run(ZeusApplication.class, args);
    }


    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(this.getClass().getPackageName()))
                .build();
    }


    public static PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Transactional
    public void seedUserAndRoleData() {
        userRepository.deleteAllInBatch();
        roleRepository.deleteAllInBatch();
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleName.ROLE_ADMIN));
        roles.add(new Role(RoleName.ROLE_GENERAL_MANAGER));
        roles.add(new Role(RoleName.ROLE_SALE_STAFF));
        roles.add(new Role(RoleName.ROLE_ACCOUNTANT_STAFF));
        roles.add(new Role(RoleName.ROLE_WAREHOUSE_STAFF));
        roleRepository.saveAll(roles);

        String password = encoder().encode("admin123");
        User admin_user = User.builder()
                .username("admin")
                .password(password)
                .email("admin@wifosoft.com")
                .firstName("Duc Minh")
                .lastName("Tran")
                .address("Vị trí quản lý admin")
                .userPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER))
                .phone("0982259245")
                .build();
        User admin_user_1 = User.builder()
                .username("admin1")
                .password(password)
                .email("admin1@wifosoft.com")
                .firstName("Duc Minh")
                .lastName("Tran")
                .address("Vị trí quản lý admin")
                .phone("0982222222")
                .userPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER))
                .build();


        User manager1 = User.builder()
                .username("manager1")
                .password(password)
                .email("manager1@wifosoft.com")
                .firstName("Nguyễn Văn")
                .lastName("An 1")
                .address("Vị trí quản lý 1")
                .phone("0982259246")
                .parent(admin_user)
                .userPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER))
                .build();

        User manager2 = User.builder()
                .username("manager2")
                .password(password)
                .email("manager2@wifosoft.com")
                .firstName("Nguyễn Văn")
                .lastName("Thứ 2")
                .address("Vị trí quản lý 2")
                .phone("0982259247")
                .parent(admin_user)
                .userPermission(DefaultUserPermission.getDefaultPermissionFromRole(RoleName.ROLE_GENERAL_MANAGER))
                .build();


        List<User> users = new ArrayList<User>();
        users.add(admin_user);
        users.add(manager1);
        users.add(manager2);
        users.add(admin_user_1);

        users.forEach(e -> {
            List<Role> userRole = new ArrayList<>();


            entityManager.persist(e);
            UserRoleRelation userRoleRelation = new UserRoleRelation();
            userRoleRelation.setUser(e);
            userRoleRelation.setRole(roleRepository.getRoleByName(RoleName.ROLE_ADMIN));
            if (e.getUsername().equals("admin")) {
                UserRoleRelation userRoleRelationAdmin = new UserRoleRelation();
                userRoleRelationAdmin.setUser(e);
                userRoleRelationAdmin.setRole(roleRepository.getRoleByName(RoleName.ROLE_GENERAL_MANAGER));
                entityManager.persist(userRoleRelationAdmin);
            }
            entityManager.persist(userRoleRelation);
        });

        Shop shop1 = Shop.builder().name("Cửa hàng 1")
                .shortName("CH1")
                .address("Đông Hưng Thái Bình")
                .phone("123123")
                .businessLine("Mỹ phẩm")
                .generalManager(admin_user).build();
        Shop shop2 = Shop.builder()
                .name("Cửa hàng 2")
                .shortName("CH2")
                .address("Quận 5, Hồ Chí Minh")
                .phone("123123")
                .businessLine("Mỹ phẩm")
                .generalManager(admin_user).build();

        List<Shop> shops = new ArrayList<>();
        shops.add(shop1);
        shops.add(shop2);
        shopRepository.saveAll(shops);

        admin_user = userRepository.findById(admin_user.getId()).orElseThrow();
        admin_user.setShops(shops);

        userRepository.save(admin_user);
    }


    @Override
    public void run(String... args) throws Exception {
        seedUserAndRoleData();
    }
}
