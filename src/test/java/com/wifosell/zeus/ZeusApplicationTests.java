package com.wifosell.zeus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZeusApplicationTests {

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
                () -> assertEquals(2 - 2, 1)
        );
    }


}
