package com.example.RestApiClient;

import com.example.RestApiClient.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
public class RestApiClientApplication {
	private static String url = "http://94.198.50.185:7081/api/users";

	public static void main(String[] args) {
		SpringApplication.run(RestApiClientApplication.class, args);

		var restTemplate = new RestTemplate();

//		get all users
		ResponseEntity<List<User>> users = restTemplate.exchange(
				url,
				HttpMethod.GET,
				new HttpEntity<>(new HttpHeaders()),
				new ParameterizedTypeReference<List<User>>() {}
		);
		System.out.println(users.getBody());

//		save session id
		var cookie = users.getHeaders().get("Set-Cookie");
		var cookieHeader = new HttpHeaders();
		cookieHeader.add("Cookie", cookie.get(0));
		System.out.println(cookie);

//		save user
		var user = new User(3L, "James", "Brown", (byte) 50);
		var createRequest = new HttpEntity<User>(user, cookieHeader);
		var createResponse = restTemplate.exchange(url, HttpMethod.POST, createRequest, String.class);
		var firstPartOfCode = createResponse.getBody();
		System.out.println(firstPartOfCode);

//		edit user
		var editedUser = new User(3L, "Thomas", "Shelby", (byte) 50);
		var editRequest = new HttpEntity<User>(editedUser, cookieHeader);
		var editResponse = restTemplate.exchange(url, HttpMethod.PUT, editRequest, String.class);
		var secondPartOfCode = editResponse.getBody();
		System.out.println(secondPartOfCode);

//		delete user
		var deleteRequest = new HttpEntity<Object>(cookieHeader);
		var deleteResponse = restTemplate.exchange(url + "/3", HttpMethod.DELETE, deleteRequest, String.class);
		var thirdPartOfCode = deleteResponse.getBody();
		System.out.println(thirdPartOfCode);


//		final code
		System.out.println(firstPartOfCode + secondPartOfCode + thirdPartOfCode);
	}

}
