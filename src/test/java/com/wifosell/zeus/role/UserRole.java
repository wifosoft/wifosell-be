package com.wifosell.zeus.role;

import com.wifosell.zeus.model.role.Role;
import com.wifosell.zeus.model.role.RoleName;
import com.wifosell.zeus.repository.RoleRepository;
import com.wifosell.zeus.repository.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@SpringBootTest
public class UserRole {
    static Logger log = Logger.getLogger(UserRole.class.getName());


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    static void setup() {
        log.info("@BeforeAll - executes once before all test methods in this class");

    }

    @BeforeEach
    void init() {
        log.info("@BeforeEach - executes before each test method in this class");
    }

    @Test
    void testSaveRole(){
        Role role_admin = new Role(RoleName.ROLE_ADMIN);
        roleRepository.save(role_admin);
        Role role_user = new Role(RoleName.ROLE_GENERAL_MANAGER);
        roleRepository.save(role_user);
        Long count_role = roleRepository.count();
        assertEquals(2L, count_role);
    }

    @Test
    void trueAssumption() {
        assumeTrue(5 > 1);
        assertEquals(5 + 2, 7);
    }

    @Test
    void falseAssumption() {
        assumeFalse(5 < 1);
        assertEquals(5 + 2, 7);
    }

    @Test
    void assumptionThat() {
        String someString = "Just a string";
        assumingThat(
                someString.equals("Just a string"),
                () -> assertEquals(2 + 2, 4)
        );
    }
}
