package com.spring.techserv.config;



import com.spring.techserv.exception.AccountException;
import com.spring.techserv.service.JwtAuthenticationFilter;
import com.spring.techserv.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * EnableWebSecurity - возможность использовать SecurityConfiguration
 * EnableMethodSecurity - возможность использовать аннотации Secured над методами контроллеров
 */
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfiguration {
    /**
     * отвечает за извлечение пользователя: из БД, файла и т.д
     * Пользователь, полученный из БД сравнивается с пользователем из формы или токена
     */
    private final UserDetailService userDetailService;
    /**
     * Запросы проходят фильтр, где происходит обработка токена,
     * который присылает клиент
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(UserDetailService userDetailService,
                                 JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailService = userDetailService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * шифрование паролей
     * проверка соответствия зашиврованного пароля и пароля в чистом виде
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * Основа процесса аутентификации spring security
     * Делегирует аутентификацию провайдевам - AuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws AccountException {
        try {
            return config.getAuthenticationManager();
        } catch (Exception e) {
            throw new AccountException("AuthenticationManager not configured: " + e.getMessage());
        }
    }

    /**
     * AuthenticationProvider - провайдер, которому делегируеся процесс аутентификации
     * DaoAuthenticationProvider - реализация AuthenticationProvider, занимающаяся аутентификацией
     */
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailService);
    //    provider.setUserDetailsService(userDetailService);  // для получения пользователя из хранилища
        provider.setPasswordEncoder(passwordEncoder()); // для работы с паролями
        return provider;
    }

    /**
     * Настройки spring security:
     * выбор провайдера
     * способа аутентификации
     * безопасновть запросов
     * управление доступами
     */
    /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/**").permitAll())
                *//*.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/techserv", "/api/v1/booking")// запросы
                        .permitAll()// разрешены всем
                        .requestMatchers("/task") // запросы
                        .hasAnyRole("USER", "SUPER_USER") // разрешены только пользователям с указанными ролями
                        .anyRequest().authenticated()
                )*//*
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                // запросы необходимо пропускать через фильтр
             //   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/**").permitAll()
        ).csrf(csrf -> csrf
                .ignoringRequestMatchers("/**") );


        return http.build();
    }

   /* @Bean
  //  @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain defaultSecurity(HttpSecurity http) throws Exception {
        http.csrf().d
                .authorizeHttpRequests(request -> request.requestMatchers("/api/**").permitAll())
        ;
        return http.build();
    }
*/
    // .requestMatchers("/task")
    // .requestMatchers(HttpMethod.GET, "/task")
    // .requestMatchers(HttpMethod.GET)
    // .hasAnyRole("USER", "SUPER_USER") // .hasRole("ADMIN")
   /* @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/account/registration", "/account/login")
                        .not().authenticated()
                        //.permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(form -> form
                        .usernameParameter("application_user_username") // значение атрибута name в html форме
                        .passwordParameter("application_user_password") // значение атрибута name в html форме
                        .loginPage("/account/login") // форма доступна по адресу
                        .loginProcessingUrl("/account/login") // обработчик, значение атрибута action тега form
                        .failureUrl("/account/login?failed") // ошибка авторизации
                        .defaultSuccessUrl("/account") // перенаправление после успешной авторизации
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/account/logout") // <a th:href="@{/account/logout}">Выйти</a>
                        .logoutSuccessUrl("/account/login") // перенаправление после /account/logout
                        .permitAll())  // [ВЫЙТИ] /account/logout
                .build();
    }*/


}