package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.model.customer.Customer;
import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import com.wifosell.zeus.repository.CustomerRepository;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

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

        ObjectMapper mapper = new ObjectMapper();
        InputStream file = (new FileUtils()).getFileAsIOStream("data/customer.json");

        try {
            CustomerRequest[] requests = mapper.readValue(file, CustomerRequest[].class);
            for (CustomerRequest request : requests) {
                this.updateCustomerByRequest(request, gm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateCustomerByRequest(CustomerRequest request, User gm) {
        Customer customer = new Customer();
        Optional.ofNullable(request.getFullName()).ifPresent(customer::setFullName);
        Optional.ofNullable(request.getDob()).ifPresent(customer::setDob);
        Optional.ofNullable(request.getSex()).ifPresent(customer::setSex);
        Optional.ofNullable(request.getPhone()).ifPresent(customer::setPhone);
        Optional.ofNullable(request.getEmail()).ifPresent(customer::setEmail);
        Optional.ofNullable(request.getCin()).ifPresent(customer::setCin);
        Optional.ofNullable(request.getNation()).ifPresent(customer::setNation);
        Optional.ofNullable(request.getCity()).ifPresent(customer::setCity);
        Optional.ofNullable(request.getDistrict()).ifPresent(customer::setDistrict);
        Optional.ofNullable(request.getWard()).ifPresent(customer::setWard);
        Optional.ofNullable(request.getAddressDetail()).ifPresent(customer::setAddressDetail);
        Optional.ofNullable(request.getIsActive()).ifPresent(customer::setIsActive);
        customer.setGeneralManager(gm);
        customerRepository.save(customer);
    }
}
