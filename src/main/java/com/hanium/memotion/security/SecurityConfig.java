package com.hanium.memotion.security;

import com.hanium.memotion.security.JwtAuthenticationFilter;
import com.hanium.memotion.security.JwtExceptionFilter;
import com.hanium.memotion.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 필터 체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final JwtExceptionFilter jwtExceptionFilter;

    // 스프링시큐리티 앞단 설정
    @Override
    public void configure(WebSecurity web) {
        // 로그인 개발 끝나면 "/**" 경로에서 삭제
        web.ignoring().antMatchers("/member/signup", "/member/login" , "/member/check-password",
                "/member/check-email", "/member/logout", "/member/kakao",
                "/h2-console/**", "/sample", "/sentiment",
                "/gpt-sentiment", "/s3" , "/v3/api-docs", "/swagger-resources/**",
                "/swagger-ui/**", "/webjars/**", "/swagger/**");
    }

    // 스프링시큐리티 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource()) // 크로스 오리진 정책 안씀 (인증 X) , 시큐리티 필터에 등록 인증 O
                .and()
                // 세션을 사용하지 않기 때문에 STATELESS 로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProvider))
                .authorizeRequests()
                .antMatchers("/member/signup").permitAll()
                .antMatchers("/member/login").permitAll()
                .antMatchers("/member/check-password").permitAll()
                .antMatchers("/member/check-email").permitAll()
                .antMatchers("/member/logout").permitAll()
                .antMatchers("/member/kakao").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/sample").permitAll()
                .antMatchers("/sentiment").permitAll()
                .antMatchers("/gpt-sentiment").permitAll()
                .antMatchers("/s3").permitAll()
                .antMatchers("/v3/api-docs").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // JwtAuthenticationFilter 보다 jwtExceptionFilter를 먼저 실행
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", getDefaultCorsConfiguration());

        return source;
    }

    private CorsConfiguration getDefaultCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:9000"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 header 에 응답을 허용
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 get,post,patch,put,delete 요청 허용
        configuration.setAllowedOrigins(Arrays.asList("*")); // 모든 ip 응답을 허용
        configuration.setAllowCredentials(true); // 내 서버가 응답할 때 json 을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        configuration.setMaxAge(7200L);

        return configuration;
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}