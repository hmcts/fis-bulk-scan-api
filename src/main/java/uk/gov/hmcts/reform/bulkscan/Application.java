package uk.gov.hmcts.reform.bulkscan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients(basePackages = {"uk.gov.hmcts.reform.bulkscan.*", "uk.gov.hmcts.reform.bulkscan.services"})
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {  "uk.gov.hmcts.reform.bulkscan", "uk.gov.hmcts.reform.bulkscan.services",
    "uk.gov.hmcts.reform.bulkscan.config"})
@SuppressWarnings("HideUtilityClassConstructor")
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
