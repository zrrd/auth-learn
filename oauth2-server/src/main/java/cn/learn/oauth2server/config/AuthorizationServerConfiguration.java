package cn.learn.oauth2server.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


/**
 * 认证服务器设置
 *
 * @author shaoyijiong
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

  private static final String DEMO_RESOURCE_ID = "order";
  private final JwtAccessTokenConverter jwtAccessTokenConverter;
  private final ClientDetailsServiceImpl clientDetailsService;
  private final JwtTokenStore jwtTokenStore;
  private final PasswordEncoder passwordEncoder;

  public AuthorizationServerConfiguration(
      JwtAccessTokenConverter jwtAccessTokenConverter,
      ClientDetailsServiceImpl clientDetailsService,
      JwtTokenStore jwtTokenStore,
      PasswordEncoder passwordEncoder) {
    this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    this.clientDetailsService = clientDetailsService;
    this.jwtTokenStore = jwtTokenStore;
    this.passwordEncoder = passwordEncoder;
  }


  /**
   * 用户放在内存中 生成环境一般不用
   */
  public void configureInMemory(ClientDetailsServiceConfigurer clients) throws Exception {

    //password 方案一：明文存储，用于测试，不能用于生产
    //String finalSecret = "123456";

    //password 方案二：用 BCrypt 对密码编码
    //String finalSecret = new BCryptPasswordEncoder().encode("123456");

    //password 方案三：支持多种编码，通过密码的前缀区分编码方式
    String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");

    //配置两个客户端,一个用于password认证一个用于client认证
    clients.inMemory().withClient("client_1")
        .resourceIds(DEMO_RESOURCE_ID)
        .authorizedGrantTypes("client_credentials", "refresh_token")
        .scopes("select")
        .authorities("oauth2")
        .secret(finalSecret)
        .and().withClient("client_2")
        .resourceIds(DEMO_RESOURCE_ID)
        .authorizedGrantTypes("password", "refresh_token")
        .scopes("select")
        .authorities("oauth2")
        .secret(finalSecret);
  }


  /**
   * 拿到oauth的登陆
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientDetailsService);
  }

  /**
   * <pre>
   * 设置token 生成和存储的方式 常用的有
   * InMemoryTokenStore  保存到redis
   * JdbcTokenStore 保存到数据库
   * JwtTokenStore jwt的方式生成
   * RedisTokenStore 保存到redis
   *
   * 这里使用jwt的方式 无状态保存token
   * </pre>
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints.tokenStore(jwtTokenStore)
        .accessTokenConverter(jwtAccessTokenConverter)
        .userDetailsService(userDetailsService())
        //获得用户的详细信息
        .authenticationManager(customProvider());
  }

  /**
   * 一些初始化配置 如是否只允许https 是否允许表单请求等
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    //允许表单认证
    oauthServer.allowFormAuthenticationForClients()
    //公开/oauth/check_token  /oauth/token_key endpoints 默认是是关闭的
        .tokenKeyAccess("isAuthenticated()")
        .checkTokenAccess("permitAll()");

  }


  /**
   * <pre>
   * UserDetailsService 用户password 模式校验用户的 username 与 password
   * 这里将用户放在内存中 生成环境一般放在数据库中 可以自己实现 UserDetailsService 接口
   * </pre>
   */
  @Bean
  protected UserDetailsService userDetailsService() {
    //password 方案一：明文存储，用于测试，不能用于生产
    //String finalPassword = "123456";

    //password 方案二：用 BCrypt 对密码编码
    //String finalPassword = bCryptPasswordEncoder.encode("123456");

    // password 方案三：支持多种编码，通过密码的前缀区分编码方式
    //BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    //String finalPassword = "{bcrypt}" + bCryptPasswordEncoder.encode("123456");

    String finalPassword = passwordEncoder.encode("123456");
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
        User.withUsername("user_1").password(finalPassword).authorities("USER").build());
    manager.createUser(
        User.withUsername("user_2").password(finalPassword).authorities("USER").build());
    return manager;
  }


  private ProviderManager customProvider() {
    CustomProvider customProvider = new CustomProvider(userDetailsService());
    return new ProviderManager(Collections.singletonList(customProvider));
  }
}

