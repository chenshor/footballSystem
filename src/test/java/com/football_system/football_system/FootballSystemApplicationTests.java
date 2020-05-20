//package com.football_system.football_system;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.football_system.football_system.FMserver.LogicLayer.League;
//import com.football_system.football_system.controllers.GuestController;
//import org.junit.jupiter.api.Test;
//
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
////@SpringBootTest
////class FootballSystemApplicationTests {
////
////	private String url = "http://localhost:8080/";
////
////	@Autowired
////	private TestRestTemplate restTemplate;
////
////	@Test
////	public void  getLeagues() throws Exception{
////		try{
////			String uri = url + "/Leagues";
////			String content = this.restTemplate.getForObject(uri ,String.class);
////
////			System.out.println("myContent"+content);
////		//	League[] Leagues = (content, League[].class);
////
////			//assertTrue(Leagues.length > 0);
////		}catch (Exception e){
////			assertTrue(false);
////		}
////	}
////
////}
//
//
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import org.junit.Assert;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = FootballSystemApplication.class)
//@AutoConfigureMockMvc
//public class FootballSystemApplicationTests
//{
//	@Autowired
//	private TestRestTemplate restTemplate;
//
//	@LocalServerPort
//	int randomServerPort;
//
//	@Test
//	public void testAddEmployeeSuccess() throws URISyntaxException
//	{
//		final String baseUrl = "http://localhost:"+randomServerPort+"/Leagues";
//		URI uri = new URI(baseUrl);
//	//	Employee employee = new Employee(null, "Adam", "Gilly", "test@email.com");
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("X-COM-PERSIST", "true");
//
//	//	HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
//
//		ResponseEntity<String> result = this.restTemplate.getForEntity(uri, String.class);
//
//		//Verify request succeed
//		Assert.assertEquals(201, result.getStatusCodeValue());
//		System.out.println(result.getBody());
//	}
//
////	@Test
////	public void testAddEmployeeMissingHeader() throws URISyntaxException
////	{
////		final String baseUrl = "http://localhost:"+randomServerPort+"/employees/";
////		URI uri = new URI(baseUrl);
////		Employee employee = new Employee(null, "Adam", "Gilly", "test@email.com");
////
////		HttpHeaders headers = new HttpHeaders();
////
////		HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
////
////		ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
////
////		//Verify bad request and missing header
////		Assert.assertEquals(400, result.getStatusCodeValue());
////		Assert.assertEquals(true, result.getBody().contains("Missing request header"));
////	}
//
//}