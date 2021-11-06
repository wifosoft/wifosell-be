package com.wifosell.zeus;

import com.wifosell.zeus.constant.DefaultUserPermission;
import com.wifosell.zeus.database.DatabaseSeeder;
import com.wifosell.zeus.database.seeder.UserSeeder;
import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.model.role.UserRoleRelation;
import com.wifosell.zeus.model.shop.Shop;
import com.wifosell.zeus.model.shop.UserShopRelation;
import com.wifosell.zeus.model.shop.WarehouseShopRelation;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.model.warehouse.Warehouse;
import com.wifosell.zeus.repository.*;
import com.wifosell.zeus.security.JwtAuthenticationFilter;
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
import javax.persistence.*;
import javax.transaction.Transactional;
import javax.xml.crypto.Data;
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
    WarehouseShopRelationRepository warehouseShopRelationRepository;
    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    UserShopRelationRepository userShopRelationRepository;
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




    @Override
    public void run(String... args) throws Exception {
        //EntityManagerFactory emf = Persistence.createEntityManagerFactory("seederManager");
        //EntityManager em = emf.createEntityManager();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder(entityManager);
        databaseSeeder.prepare();
        databaseSeeder.run();
    }
}
