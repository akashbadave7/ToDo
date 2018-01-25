package com.bridgeit.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bridgeit.security.JwtAuthenticationEntryPoint;
import com.bridgeit.security.JwtAuthenticationProvider;
import com.bridgeit.security.JwtAuthenticationSuccessHandler;
import com.bridgeit.security.JwtAuthenticationTokenFilter;



@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	
	
	   @Autowired
	    private JwtAuthenticationEntryPoint unauthorizedHandler;

	    @Autowired
	    private JwtAuthenticationProvider authenticationProvider;
	    
	   

	   
	    @Override
	    public AuthenticationManager authenticationManager() throws Exception {
	    	List list=new ArrayList();
	    	list.add(authenticationProvider);
	        return new ProviderManager(list);

	    }

	    @Bean
	    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
	        JwtAuthenticationTokenFilter authenticationTokenFilter = new JwtAuthenticationTokenFilter();
	        authenticationTokenFilter.setAuthenticationManager(authenticationManager());
	        authenticationTokenFilter.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
	        return authenticationTokenFilter;
	    }

	    @Override
	    protected void configure(HttpSecurity httpSecurity) throws Exception {
	        httpSecurity
	                // we don't need CSRF because our token is invulnerable
	                .csrf().disable()
	               .authorizeRequests()
	               .antMatchers("/admin/**").hasAuthority("admin")
	               .anyRequest().authenticated()
	               .and()
	                // Call our errorHandler if authentication/authorisation fails
	                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
	                .and()
	                // don't create session
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //.and()
	        // Custom JWT based security filter
	        httpSecurity
	                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

	        // disable page caching
	        httpSecurity.headers().cacheControl();
	    }
	
	  /*  @Override
	    public void configure(WebSecurity web) throws Exception {
	        web.ignoring().antMatchers("/hello","/admin/hello");
	    }
	*/
	
/*		@Autowired
		 protected void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
			System.out.println("helllll...");
			auth.inMemoryAuthentication()
			.withUser("zian").password("zian").roles("USER");
		}
	
		@Override
	    protected void configure(HttpSecurity httpSecurity) throws Exception {
		    httpSecurity
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll().and()
            .httpBasic();

	}
*/
}
