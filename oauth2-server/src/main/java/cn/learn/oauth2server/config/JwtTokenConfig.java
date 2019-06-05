package cn.learn.oauth2server.config;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * jwt生成配置
 *
 * @author shaoyijiong
 * @date 2019/6/5
 */
@Slf4j
@Configuration
public class JwtTokenConfig {

  @Value("${jwt.secret}")
  private String defaultJwtSigningKey;


  public JwtTokenConfig() {
    log.info("Loading JwtTokenConfig ...");
  }

  @Bean
  public JwtTokenStore jwtTokenStore() {
    return new JwtTokenStore(jwtAccessTokenConverter());
  }

  /**
   * token创建
   */
  @Bean
  public JwtAccessTokenConverter jwtAccessTokenConverter() {
    JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
    //如果拿到用户信息生成token的话  继承 JwtAccessTokenConverter 实现  enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication)方法
    //从authentication 拿到用户信息
    //从OAuth2AccessToken 修改token 参数 如过期事件

    //密钥
    jwtAccessTokenConverter.setSigningKey(defaultJwtSigningKey);
    return jwtAccessTokenConverter;
  }
}
