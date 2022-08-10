package com.wifosell.zeus;

import com.google.gson.Gson;
import com.wifosell.zeus.payload.provider.shopee.ResponseSendoProductItemPayload;
import com.wifosell.zeus.service.SendoProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumingThat;

@SpringBootTest
class ZeusApplicationTests {

    @Autowired
    SendoProductService sendoProductService;

    @Test
    void testSum() {
        String someString = "Just a string";
        assumingThat(
                someString.equals("Just a string"),
                () -> assertEquals(2 + 2, 4)
        );
    }

    @Test
    void testSubtract() {
        String someString = "Just a testSubtract";
        assumingThat(
                someString.equals("Just a testSubtract"),
                () -> assertEquals(2 - 2, 0)
        );
    }

    @Test
    public void testUpdateProduct() {


    }

}
