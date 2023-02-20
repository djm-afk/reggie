package com.example.reggie_take_out;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
class ReggieTakeOutApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void deleteAllCache(){
        Boolean delete = redisTemplate.delete("*");
        log.info(delete.toString());
    }

}
