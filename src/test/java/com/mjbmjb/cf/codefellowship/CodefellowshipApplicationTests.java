package com.mjbmjb.cf.codefellowship;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CodefellowshipApplicationTests {


	@Autowired
	AppUserController appUserController;

	@Autowired
	CodefellowshipController codefellowshipController;

	@Autowired
	MockMvc mockMvc;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testControllerIsAutowired() {
		assertNotNull(appUserController);
	}

	@Test
	public void testRequestToRootGivesStuff() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}

	public void testRequestToSignupGivesStuff() throws Exception {
		mockMvc.perform(get("/signup")).andExpect(status().isOk());

	}

}