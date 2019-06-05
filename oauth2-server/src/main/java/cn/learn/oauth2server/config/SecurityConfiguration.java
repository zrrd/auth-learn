package cn.learn.oauth2server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;


/**
 * @author shaoyijiong
 */
@Order(10)
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends AuthorizationServerSecurityConfiguration {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http.requestMatchers().anyRequest()
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**").permitAll();
    // @formatter:on
  }
}
