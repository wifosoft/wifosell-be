package com.wifosell.zeus.database.seeder;

import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.CustomerRepository;
import com.wifosell.zeus.repository.UserRepository;

import java.util.Calendar;
import java.util.Date;

public class CustomerSeeder extends BaseSeeder implements ISeeder {
    private CustomerRepository customerRepository;
    private UserRepository userRepository;

    @Override
    public void prepareJpaRepository() {
        this.customerRepository = this.factory.getRepository(CustomerRepository.class);
        this.userRepository = this.factory.getRepository(UserRepository.class);
    }

    @Deprecated
    @Override
    public void run() {
        User gm = userRepository.getUserByName("manager1").getGeneralManager();
        Customer customer1 = Customer.builder()
                .fullName("Kieu Cong Hau")
                .phone("0987654321")
                .dob(new Date(122, Calendar.JANUARY, 12, 0, 0, 0))
                .email("hau@gmail.com")
                .city("HCM")
                .ward("1")
                .district("1")
                .addressDetail("1 Le Duan")
                .sex(Customer.Sex.MALE)
                .generalManager(gm)
                .build();
        customerRepository.save(customer1);

        Customer customer2 = Customer.builder()
                .fullName("Kieu Cong Hau")
                .phone("0987654321")
                .dob(new Date(122, Calendar.JANUARY, 12, 0, 0, 0))
                .email("hau@gmail.com")
                .city("HCM")
                .ward("1")
                .district("1")
                .addressDetail("1 Le Duan")
                .sex(Customer.Sex.MALE)
                .generalManager(gm)
                .build();
        customerRepository.save(customer2);

        Customer customer3 = Customer.builder()
                .fullName("Kieu Cong Hau")
                .phone("0987654321")
                .dob(new Date(122, Calendar.JANUARY, 12, 0, 0, 0))
                .email("hau@gmail.com")
                .city("HCM")
                .ward("1")
                .district("1")
                .addressDetail("1 Le Duan")
                .sex(Customer.Sex.MALE)
                .generalManager(gm)
                .build();
        customerRepository.save(customer3);
    }
}
