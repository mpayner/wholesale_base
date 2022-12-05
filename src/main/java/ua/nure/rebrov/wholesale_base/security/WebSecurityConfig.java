package ua.nure.rebrov.wholesale_base.security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select email username, password, 1 from user where email = ?")
                .authoritiesByUsernameQuery(
                        "select u.email login, t.name role from user u join user_type t on u.type_id = t.id where u.email = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/courtCases", "/legalEntities", "/physicalEntities").authenticated()
                .antMatchers("/addCourtCase/**","/addLegalEntity/**", "/addPhysicalEntity/**", "/addCourtHearing/**").hasAuthority("секретар")
                .antMatchers("/priceList/**").hasAuthority("Distributor")
                .and().formLogin().permitAll()
                .loginPage("/login")
                .loginProcessingUrl("/perform-login")
                .usernameParameter("user")
                .passwordParameter("pass")
                .defaultSuccessUrl("/")
                .and()
                .logout().logoutUrl("/logout")
                .and().exceptionHandling().accessDeniedPage("/403")
                .and().sessionManagement()
                .invalidSessionUrl("/")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry());

    }


    @Bean(name = "sessionRegistry")
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}