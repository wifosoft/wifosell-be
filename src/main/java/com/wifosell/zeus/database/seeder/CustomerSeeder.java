package com.wifosell.zeus.database.seeder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wifosell.zeus.database.BaseSeeder;
import com.wifosell.zeus.database.ISeeder;
import com.wifosell.zeus.database.SeederConst;
import com.wifosell.zeus.payload.request.customer.CustomerRequest;
import com.wifosell.zeus.service.CustomerService;
import com.wifosell.zeus.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;

public class CustomerSeeder extends BaseSeeder implements ISeeder {
    private CustomerService customerService;

    @Override
    public void prepareJpaRepository() {
        customerService = context.getBean(CustomerService.class);
    }

    @Deprecated
    @Override
    public void run() {
        try {
            InputStream file = (new FileUtils()).getFileAsIOStream("data/customer.json");
            CustomerRequest[] requests = new ObjectMapper().readValue(file, CustomerRequest[].class);
            file.close();
            for (CustomerRequest request : requests) {
                customerService.addCustomer(SeederConst.USER_ID, request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
