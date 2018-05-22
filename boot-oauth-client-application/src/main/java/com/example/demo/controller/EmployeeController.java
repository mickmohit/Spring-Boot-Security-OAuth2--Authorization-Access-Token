package com.example.demo.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class EmployeeController {

	@RequestMapping(value = "/getEmployees", method = RequestMethod.GET)
	public ModelAndView getEmployeeInfo() {
		return new ModelAndView("getEmployees");
	}
	
	@RequestMapping(value = "/showEmployees", method = RequestMethod.GET)
	public ModelAndView showEmployees(@RequestParam("code") String code) throws JsonProcessingException, IOException {
		ResponseEntity<String> response = null;
		System.out.println("Authorization Code------" + code);

		RestTemplate restTemplate = new RestTemplate();

		// According OAuth documentation we need to send the client id and secret key in the header for authentication
		String credentials = "javainuse:secret";
		
		System.out.println("credentials------"+credentials.getBytes());
		
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

		System.out.println("encodedCredentials"+encodedCredentials);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    	
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> request = new HttpEntity<String>(headers);
		//RequestEntity<String> request = new RequestEntity<String>("parameters", headers, null, null);
		
		//headers.add("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

		System.out.println("request==="+request);
		
		String access_token_url = "http://localhost:8080/oauth/token";
		access_token_url += "?code=" + code;
		access_token_url += "&grant_type=authorization_code";
		//access_token_url += "&redirect_uri=http://localhost:8090/showEmployees";

		   UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(access_token_url);
		   URI myUri=builder.buildAndExpand().toUri();
		
		   System.out.println("myUri==="+myUri);
		   
		response = restTemplate.exchange(myUri, HttpMethod.POST, request, String.class);

		
		
		System.out.println("access_token_url===="+access_token_url);
		
		System.out.println("response---"+response);
		System.out.println("response header---"+response.getHeaders());
		System.out.println("response response.toString()---"+response.toString());
		System.out.println("Access Token Response ---------" + response.getBody());

		return null;
		/*// Get the Access Token From the recieved JSON response
				ObjectMapper mapper = new ObjectMapper();
				JsonNode node = mapper.readTree(response.getBody());
				String token = node.path("access_token").asText();

				String url = "http://localhost:8080/user/getEmployeesList";

				// Use the access token for authentication
				HttpHeaders headers1 = new HttpHeaders();
				headers1.add("Authorization", "Bearer " + token);
				HttpEntity<String> entity = new HttpEntity<>(headers1);

				ResponseEntity<Employee[]> employees = restTemplate.exchange(url, HttpMethod.GET, entity, Employee[].class);
				System.out.println(employees);
				Employee[] employeeArray = employees.getBody();

				ModelAndView model = new ModelAndView("showEmployees");
				model.addObject("employees", Arrays.asList(employeeArray));
				return model;*/
			}


}

