package org.sopt.homework.global.security.config;

import org.sopt.homework.global.common.util.RedisUtil;
import org.sopt.homework.global.security.filter.JwtAuthenticationFilter;
import org.sopt.homework.global.security.util.jwt.JwtParser;
import org.sopt.homework.global.security.util.jwt.JwtValidator;
import org.sopt.homework.global.security.util.response.JsonResponseWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtValidator jwtValidator;
	private final JwtParser jwtParser;
	private final JsonResponseWriter jsonResponseWriter;
	private final RedisUtil redisUtil;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtParser, jwtValidator,
				jsonResponseWriter, redisUtil);

		return http.csrf(AbstractHttpConfigurer::disable)        // jwt 사용을 위해 csrf 토큰 사용 비활성화
				.formLogin(AbstractHttpConfigurer::disable)        // api 로그인을 위해 기본 제공 form 로그인 사용 비활성화
				.logout(AbstractHttpConfigurer::disable)        // api 로그아웃을 위해 기본 제공 logout 사용 비활성화
				.httpBasic(AbstractHttpConfigurer::disable)        // jwt 기반 인증 사용을 위해 httpBasic 인증 비활성화
				.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
						.requestMatchers("/**").permitAll().anyRequest().authenticated())        // 모든 인가 권한에 대해 허용
				.sessionManagement((session) -> session
						.sessionCreationPolicy(
								SessionCreationPolicy.STATELESS))        // jwt 기반 인증을 사용하므로 세션을 stateless 설정
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
}
