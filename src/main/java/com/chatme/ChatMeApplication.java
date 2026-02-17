package com.chatme;

import com.fast.cqrs.autoconfigure.EnableFast;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFast
public class ChatMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMeApplication.class, args);
    }
}
