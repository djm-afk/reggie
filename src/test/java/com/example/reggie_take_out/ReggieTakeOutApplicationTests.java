package com.example.reggie_take_out;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class ReggieTakeOutApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDate.now());
    }

}
