package com.javainuse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {
    //Here we specify to allow the request to the url /user/getEmployeesList with valid access token and scope read 
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.requestMatchers().antMatchers("/user/getEmployeesList/**").and().authorizeRequests().anyRequest()
				.access("#oauth2.hasScope('read')");
	}
}
