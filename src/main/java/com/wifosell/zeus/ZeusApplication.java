package com.wifosell.zeus;

import com.wifosell.zeus.config.property.AppProperties;
import com.wifosell.zeus.database.DatabaseSeeder;
import com.wifosell.zeus.security.JwtAuthenticationFilter;
import com.wifosell.zeus.service.impl.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
@EnableConfigurationProperties({StorageProperties.class, AppProperties.class})
@RequiredArgsConstructor
public class ZeusApplication implements CommandLineRunner {
    private final Environment env;

    private final ApplicationContext context;

    @PersistenceContext
    private final EntityManager entityManager;

    public static void main(String[] args) {
        SpringApplication.run(ZeusApplication.class, args);
    }

    //
//    @Bean
//    public JobScheduler initJobRunr(DataSource dataSource, JobActivator jobActivator) {
//        return JobRunr.configure()
//                .useJobActivator(jobActivator)
//                .useStorageProvider(SqlStorageProviderFactory
//                        .using(dataSource))
//                .useBackgroundJobServer()
//                .useDashboard()
//                .initialize().getJobScheduler();
//    }
    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

//    @Bean
//    CommandLineRunner init(StorageService storageService) {
//        return (args) -> {
//            storageService.deleteAll();
//            storageService.init();
//        };
//    }

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
        String enableMigration = env.getProperty("app.migration");
        if (enableMigration == null || enableMigration.equals("true")) {
            DatabaseSeeder databaseSeeder = new DatabaseSeeder(context, entityManager);
            databaseSeeder.prepare();
            databaseSeeder.run();
        }
    }
}
