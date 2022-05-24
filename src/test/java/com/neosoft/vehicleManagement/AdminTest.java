package com.neosoft.vehicleManagement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neosoft.vehicleManagement.controllers.AdminController;
import com.neosoft.vehicleManagement.models.Role;
import com.neosoft.vehicleManagement.models.User;
import com.neosoft.vehicleManagement.repo.UserRepo;
import com.neosoft.vehicleManagement.serviceImp.UserServiceImpl;

@WebMvcTest(AdminController.class)
class AdminTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@MockBean
	private UserRepo userRepo;
	
	@MockBean
	private UserServiceImpl userServ;
	
	Set<Role> USER_ADMIN = new HashSet<>();
	Set<Role> USER_RENTER = new HashSet<>();
	Set<Role> USER_CUSTOMER = new HashSet<>();
	
	User admin;
	User renter;
	User customer;
	
	Principal P_ADMIN = new Principal() {
        @Override
        public String getName() {
            return "ROLE_ADMIN";
        }
    };
	
	public AdminTest() {
		USER_ADMIN.add(new Role("ROLE_ADMIN"));
		admin = new User( 1L, "Jeff", "Bezos", "admin@admin.com", "password", USER_ADMIN);
		
		USER_RENTER.add(new Role("ROLE_RENTER"));
		renter = new User( 2L, "Barry", "Allen", "barry@renter.com", "password", USER_RENTER);
		
		USER_CUSTOMER.add(new Role("ROLE_CUSTOMER"));
		customer = new User( 3L, "Stephen", "Strange", "stephen@gmail.com", "password", USER_CUSTOMER);
	}
	
	@Test
	public void testGetLogin() {
		try {
			mockMvc.perform(get("/admin").principal(P_ADMIN))
			.andExpect(status().is3xxRedirection())
			.andDo(print());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPostLogin() {
		try {
			mockMvc.perform(post("/login")
			.param("username", "john").param("password", "s3cr3t")).andDo(print()).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
