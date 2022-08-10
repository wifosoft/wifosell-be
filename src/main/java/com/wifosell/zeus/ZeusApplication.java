package com.wifosell.zeus;

import com.wifosell.zeus.config.KafkaConfiguration;
import com.wifosell.zeus.config.property.AppProperties;
import com.wifosell.zeus.consumer.SendoProductKafkaConsumer;
import com.wifosell.zeus.database.DatabaseSeeder;
import com.wifosell.zeus.security.JwtAuthenticationFilter;
import com.wifosell.zeus.service.impl.storage.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Properties;
import java.util.TimeZone;

@EnableSwagger2
@SpringBootApplication
@EntityScan(basePackageClasses = {ZeusApplication.class, Jsr310Converters.class})
@Transactional
@EnableConfigurationProperties({StorageProperties.class, AppProperties.class})
@RequiredArgsConstructor
public class ZeusApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ZeusApplication.class);

    private final Environment env;

    private final ApplicationContext context;

    @Autowired
    KafkaConfiguration kafkaConfiguration;

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
        try {
            //consumer sendo product
            Properties consumerProperties = new Properties();
            consumerProperties.put("bootstrap.servers", kafkaConfiguration.getKafkaBootstrapServers());
            consumerProperties.put("group.id", kafkaConfiguration.getZookeeperGroupId());
            consumerProperties.put("zookeeper.session.timeout.ms", "6000");
            consumerProperties.put("zookeeper.sync.time.ms","2000");
            consumerProperties.put("auto.commit.enable", "false");
            consumerProperties.put("auto.commit.interval.ms", "1000");
            consumerProperties.put("consumer.timeout.ms", "-1");
            consumerProperties.put("max.poll.records", "1");
            consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

            /*
             * Creating a thread to listen to the kafka topic
             */
            Thread kafkaConsumerThread = new Thread(() -> {
                logger.info("Start consumer thread sendo_product");
                SendoProductKafkaConsumer simpleKafkaConsumer = new SendoProductKafkaConsumer(
                        kafkaConfiguration.getSendoProductTopic(),
                        consumerProperties
                );

                simpleKafkaConsumer.runSingleWorker();
                logger.info("Run single worker thread sendo_product");

            });

            /*
             * Starting the first thread.
             */
            kafkaConsumerThread.start();
        } catch (Exception exception){
            logger.error("Run single worker thread sendo_product");
            exception.printStackTrace();
        }


        String enableMigration = env.getProperty("app.migration");
        if (enableMigration == null || enableMigration.equals("true")) {
            DatabaseSeeder databaseSeeder = new DatabaseSeeder(context, entityManager);
            databaseSeeder.prepare();
            databaseSeeder.run();
        }
    }
}
