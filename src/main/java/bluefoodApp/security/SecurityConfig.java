package bluefoodApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public AunthenticationSucessHandlerImpl aunthenticationSucessHandler() {
    return new AunthenticationSucessHandlerImpl();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/images/**", "/css/**", "/js/**", "/public", "/sbpay").permitAll()
            .antMatchers("/cliente/**").hasRole(Role.CLIENTE.toString())
            .antMatchers("/restaurante/**").hasRole(Role.RESTAURANTE.toString())
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .failureUrl("/login-error")
            .successHandler(aunthenticationSucessHandler())
            .permitAll()
            .and()
            .logout().logoutUrl("/logout")
            .permitAll();


  }

}
