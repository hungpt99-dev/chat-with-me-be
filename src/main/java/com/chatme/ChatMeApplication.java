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

    @org.springframework.context.annotation.Bean
    public org.springframework.boot.CommandLineRunner inspectBeans(org.springframework.context.ApplicationContext ctx) {
        return args -> {
            System.out.println("--- BEAN INSPECTION ---");
            try {
                Class.forName("org.flywaydb.core.Flyway");
                System.out.println("Flyway class: FOUND");
            } catch (ClassNotFoundException e) {
                System.out.println("Flyway class: NOT FOUND");
            }

            try {
                Class.forName("org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration");
                System.out.println("FlywayAutoConfiguration class: FOUND");
            } catch (ClassNotFoundException e) {
                System.out.println("FlywayAutoConfiguration class: NOT FOUND");
            }

            System.out.println("DataSource present: " + ctx.containsBean("dataSource"));
            System.out.println("Flyway present: " + ctx.containsBean("flyway"));
            if (ctx.containsBean("dataSource")) {
                System.out.println("DataSource class: " + ctx.getBean("dataSource").getClass().getName());
            }
            System.out.println("-----------------------");
        };
    }
}
