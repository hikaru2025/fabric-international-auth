package com.kt.fabric.international.fabricinternationalauth.config;

import com.kt.fabric.international.fabricinternationalauth.support.UserAuthenticationSuccessHandler;
import com.kt.fabric.international.fabricinternationalauth.support.UserTokenEnhancer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @Auther: xm
 * @Date: 2020/12/31/11:49
 * @Description:
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Autowired
    private UserAuthenticationSuccessHandler UserAuthenticationSuccessHandler;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {
        http.headers().frameOptions().disable();
        http.formLogin()
                .successHandler(UserAuthenticationSuccessHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/token/**", "/mobile/**", "/v2/api-docs", "/v2/api-docs-ext").permitAll()
                .anyRequest().authenticated().and()
                .csrf().disable();
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * @Description 自定义生成令牌token
     * @Date 2019/7/9 19:58
     * @Version  1.0
     */
    @Bean
    public TokenEnhancer tokenEnhancer(){
        return new UserTokenEnhancer();
    }
}
