package cn.learn.oauth2server.config;

import java.util.Collections;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author shaoyijiong
 * @date 2019/6/5
 */
public class CustomProvider implements AuthenticationProvider {

  private UserDetailsService userDetailsService;

  public CustomProvider(
      UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
    return new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
  }

  /**
   *
   */
  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
  }
}
