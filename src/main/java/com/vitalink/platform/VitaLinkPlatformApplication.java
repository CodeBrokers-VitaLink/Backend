package com.vitalink.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(
    exclude =
        org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration.class)
@EnableJpaAuditing
public class VitaLinkPlatformApplication {
  public static void main(String[] args) {
    SpringApplication.run(VitaLinkPlatformApplication.class, args);
  }
}
