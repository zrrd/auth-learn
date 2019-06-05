package cn.learn.oauth2server.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

/**
 * @author shaoyijiong
 * @date 2019/6/5
 */
@Component
public class ClientDetailsServiceImpl implements ClientDetailsService {

  private final PasswordEncoder passwordEncoder;
  private static final String DEMO_RESOURCE_ID = "order";
  private Map<String, ClientDetails> detailsMap;

  public ClientDetailsServiceImpl(
      PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * 方便使用直接放在内存中了  生成环境请放在数据库中
   */
  @Override
  public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
    return detailsMap.get(clientId);
  }


  /**
   * 初始化3个账户 分别对应
   */
  @PostConstruct
  public void init() {
    detailsMap = new HashMap<>(3);

    //client 模式
    BaseClientDetails details1 = new BaseClientDetails();
    details1.setClientId("a");
    details1.setClientSecret(passwordEncoder.encode("a"));
    details1.setAuthorizedGrantTypes(Arrays.asList("client_credentials", "refresh_token"));
    details1.setResourceIds(Collections.singletonList(DEMO_RESOURCE_ID));
    details1.setScope(Collections.singletonList("select"));
    detailsMap.put(details1.getClientId(), details1);

    //password 模式
    BaseClientDetails details2 = new BaseClientDetails();
    details2.setClientId("b");
    details2.setClientSecret(passwordEncoder.encode("b"));
    details2.setAuthorizedGrantTypes(Arrays.asList("password", "refresh_token"));
    details2.setResourceIds(Collections.singletonList(DEMO_RESOURCE_ID));
    details2.setScope(Collections.singletonList("select"));
    detailsMap.put(details2.getClientId(), details2);

    //authorization_code 授权码模式
    BaseClientDetails details3 = new BaseClientDetails();
    details3.setClientId("c");
    details3.setClientSecret(passwordEncoder.encode("c"));
    details3.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "refresh_token"));
    details3.setResourceIds(Collections.singletonList(DEMO_RESOURCE_ID));
    details3.setScope(Collections.singletonList("select"));
    detailsMap.put(details3.getClientId(), details3);
  }


}
