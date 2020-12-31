package com.kt.fabric.international.fabricinternationalauth.config;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Auther: xm
 * @Date: 2020/12/31/9:56
 * @Description:
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {



    /**
     * 授权管理器
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    @SneakyThrows
    protected void configure(HttpSecurity http) {
        http.httpBasic().and().csrf().disable();
    }

}
