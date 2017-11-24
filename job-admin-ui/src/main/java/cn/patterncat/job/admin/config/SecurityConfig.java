package cn.patterncat.job.admin.config;

import cn.patterncat.job.admin.security.AjaxAuthFailHandler;
import cn.patterncat.job.admin.security.AjaxAuthSuccessHandler;
import cn.patterncat.job.admin.security.UnauthorizedEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by patterncat on 2017-11-24.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())

                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login","/css/**", "/js/**","/fonts/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .usernameParameter("name")
                .passwordParameter("password")
                .successHandler(new AjaxAuthSuccessHandler())
                .failureHandler(new AjaxAuthFailHandler())
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll();
    }
}
