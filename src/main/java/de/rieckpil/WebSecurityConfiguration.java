package de.rieckpil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {
  @Bean
  public SecurityFilterChain mainSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .authorizeRequests(requests -> requests.anyRequest().permitAll());
    return httpSecurity.build();
  }
}
