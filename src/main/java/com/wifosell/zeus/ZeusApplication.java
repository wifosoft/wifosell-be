package com.wifosell.zeus;

import com.wifosell.zeus.database.DatabaseSeeder;
import com.wifosell.zeus.security.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
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
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.TimeZone;

@EnableSwagger2
@SpringBootApplication
@EntityScan(basePackageClasses = {ZeusApplication.class, Jsr310Converters.class})
@Transactional
public class ZeusApplication implements CommandLineRunner {
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
