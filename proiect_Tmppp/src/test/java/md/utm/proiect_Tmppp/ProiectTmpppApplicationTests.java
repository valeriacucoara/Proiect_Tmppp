package md.utm.proiect_Tmppp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProiectTmpppApplicationTests {

	private final MockMvc mockMvc;

	ProiectTmpppApplicationTests(WebApplicationContext context) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void publicPagesRender() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk());
		mockMvc.perform(get("/jobs")).andExpect(status().isOk());
	}

	@Test
	void userLoginAndDashboardWork() throws Exception {
		MvcResult login = mockMvc.perform(post("/login")
						.param("username", "user")
						.param("password", "user123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard"))
				.andReturn();

		MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
		mockMvc.perform(get("/user-dashboard").session(session))
				.andExpect(status().isOk());
	}

	@Test
	void adminLoginAndDashboardWork() throws Exception {
		MvcResult login = mockMvc.perform(post("/login")
						.param("username", "admin")
						.param("password", "admin123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard"))
				.andReturn();

		MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
		mockMvc.perform(get("/admin-dashboard").session(session))
				.andExpect(status().isOk());
	}

}
