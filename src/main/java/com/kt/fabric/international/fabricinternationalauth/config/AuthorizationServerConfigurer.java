package com.kt.fabric.international.fabricinternationalauth.config;

import com.kt.fabric.international.fabricinternationalauth.support.OAuthTokenAuthenticationFilter;
import com.kt.fabric.international.fabricinternationalauth.support.UserTokenEnhancer;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @Auther: xm
 * @Date: 2020/12/31/9:43
 * @Description:
 */
@SuppressWarnings("all")
@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {


    private TokenStore tokenStore;

    private TokenEnhancer tokenEnhancer;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authenticationManagerBean;

    private OAuthTokenAuthenticationFilter oAuthTokenAuthenticationFilter;

    private WebResponseExceptionTranslator webResponseExceptionTranslator;




    /**
     * 用来配置授权（authorization）以及令牌（token)的访问端点和令牌服务（token services）
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //token持久化容器
        tokenServices.setTokenStore(tokenStore);
        //客户端信息
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        //自定义token生成
        tokenServices.setTokenEnhancer(tokenEnhancer);
        //access_token 的有效时长 (秒), 默认 12 小时
        tokenServices.setAccessTokenValiditySeconds(60*1);
        //refresh_token 的有效时长 (秒), 默认 30 天
        tokenServices.setRefreshTokenValiditySeconds(60*2);
        //是否支持refresh_token，默认false
        tokenServices.setSupportRefreshToken(true);
        //是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
        tokenServices.setReuseRefreshToken(false);

        endpoints
                //通过authenticationManager开启密码授权
                .authenticationManager(authenticationManagerBean)
                //自定义refresh_token刷新令牌对用户信息的检查，以确保用户信息仍然有效
                .userDetailsService(userDetailsService)
                //token相关服务
                .tokenServices(tokenServices)
                .exceptionTranslator(webResponseExceptionTranslator);
                /**
                 pathMapping用来配置端点URL链接，第一个参数是端点URL默认地址，第二个参数是你要替换的URL地址
                 上面的参数都是以“/”开头，框架的URL链接如下：
                 /oauth/authorize：授权端点。----对应的类：AuthorizationEndpoint.java
                 /oauth/token：令牌端点。----对应的类：TokenEndpoint.java
                 /oauth/confirm_access：用户确认授权提交端点。----对应的类：WhitelabelApprovalEndpoint.java
                 /oauth/error：授权服务错误信息端点。
                 /oauth/check_token：用于资源服务访问的令牌解析端点。
                 /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
                 */
//                .pathMapping("/oauth/confirm_access", "/custom/confirm_access");


    }



    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                /**
                 * 主要是让/oauth/token支持client_id和client_secret做登陆认证
                 * 如果开启了allowFormAuthenticationForClients，那么就在BasicAuthenticationFilter之前
                 * 添加ClientCredentialsTokenEndpointFilter,使用ClientDetailsUserDetailsService来进行
                 * 登陆认证
                 */
                .allowFormAuthenticationForClients();
                //oauth/token端点过滤器
//                .addTokenEndpointAuthenticationFilter(oAuthTokenAuthenticationFilter);
    }

}
