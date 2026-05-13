package md.utm.proiect_Tmppp;

import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.JobListing;
import md.utm.proiect_Tmppp.entity.User;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.repository.JobListingRepository;
import md.utm.proiect_Tmppp.repository.SkillRepository;
import md.utm.proiect_Tmppp.repository.TestRepository;
import md.utm.proiect_Tmppp.repository.UserRepository;
import md.utm.proiect_Tmppp.state.CandidateContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ProiectTmpppApplicationTests {

	private final MockMvc mockMvc;
	private final JobListingRepository jobListingRepository;
	private final CandidateRepository candidateRepository;
	private final TestRepository testRepository;
	private final UserRepository userRepository;
	private final SkillRepository skillRepository;
	private final AppUserRepository appUserRepository;

	@Autowired
	ProiectTmpppApplicationTests(WebApplicationContext context,
								 JobListingRepository jobListingRepository,
								 CandidateRepository candidateRepository,
								 TestRepository testRepository,
								 UserRepository userRepository,
								 SkillRepository skillRepository,
								 AppUserRepository appUserRepository) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		this.jobListingRepository = jobListingRepository;
		this.candidateRepository = candidateRepository;
		this.testRepository = testRepository;
		this.userRepository = userRepository;
		this.skillRepository = skillRepository;
		this.appUserRepository = appUserRepository;
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
		mockMvc.perform(get("/user-dasboard").session(session))
				.andExpect(status().isOk());
	}

	@Test
	void registeredCandidateCanLogoutAndLoginAgain() throws Exception {
		MvcResult register = mockMvc.perform(post("/register")
						.param("fullName", "Login Candidate")
						.param("email", "Login.Candidate@Example.com ")
						.param("userType", "candidate")
						.param("password", "pass123")
						.param("confirmPassword", "pass123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard"))
				.andReturn();

		MockHttpSession registeredSession = (MockHttpSession) register.getRequest().getSession(false);
		mockMvc.perform(get("/logout").session(registeredSession))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));

		MvcResult login = mockMvc.perform(post("/login")
						.param("username", "login.candidate@example.com")
						.param("password", "pass123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard"))
				.andReturn();

		MockHttpSession loginSession = (MockHttpSession) login.getRequest().getSession(false);
		assertThat(loginSession.getAttribute("currentRole")).isEqualTo("CANDIDATE");
		mockMvc.perform(get("/user-dashboard").session(loginSession))
				.andExpect(status().isOk());
	}

	@Test
	void registeredRecruiterCanLogoutAndLoginAgain() throws Exception {
		MvcResult register = mockMvc.perform(post("/register")
						.param("fullName", "Login Recruiter")
						.param("email", "login.recruiter@example.com")
						.param("userType", "recruiter")
						.param("password", "pass123")
						.param("confirmPassword", "pass123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard"))
				.andReturn();

		MockHttpSession registeredSession = (MockHttpSession) register.getRequest().getSession(false);
		mockMvc.perform(get("/logout").session(registeredSession))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));

		MvcResult login = mockMvc.perform(post("/login")
						.param("username", "login.recruiter@example.com")
						.param("password", "pass123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard"))
				.andReturn();

		MockHttpSession loginSession = (MockHttpSession) login.getRequest().getSession(false);
		assertThat(loginSession.getAttribute("currentRole")).isEqualTo("RECRUITER");
		mockMvc.perform(get("/user-dashboard").session(loginSession))
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

	@Test
	void adminCanUpdateJob() throws Exception {
		JobListing job = new JobListing();
		job.setTitle("Old title");
		job.setDescription("Old description");
		job.setDomain("Java");
		job.setLocation("Chisinau");
		job.setExperienceLevel("Junior");
		job.setSalary(1000);
		job.setApproved(false);
		job = jobListingRepository.save(job);

		mockMvc.perform(post("/admin/jobs/save")
						.session(adminSession())
						.param("id", job.getId().toString())
						.param("title", "Updated title")
						.param("description", "Updated description")
						.param("domain", "Backend")
						.param("location", "Remote")
						.param("experienceLevel", "Senior")
						.param("salary", "2500")
						.param("approved", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard#jobs-admin"));

		JobListing updated = jobListingRepository.findById(job.getId()).orElseThrow();
		assertThat(updated.getTitle()).isEqualTo("Updated title");
		assertThat(updated.getDescription()).isEqualTo("Updated description");
		assertThat(updated.getDomain()).isEqualTo("Backend");
		assertThat(updated.getLocation()).isEqualTo("Remote");
		assertThat(updated.getExperienceLevel()).isEqualTo("Senior");
		assertThat(updated.getSalary()).isEqualTo(2500);
		assertThat(updated.isApproved()).isTrue();
	}

	@Test
	void adminCanPreparePrototypeCloneAndSaveItAsNewJob() throws Exception {
		JobListing original = new JobListing();
		original.setTitle("Original job");
		original.setDescription("Prototype source");
		original.setDomain("Java");
		original.setLocation("Chisinau");
		original.setExperienceLevel("Middle");
		original.setSalary(2200);
		original.setApproved(true);
		original = jobListingRepository.save(original);
		Long originalId = original.getId();

		mockMvc.perform(get("/admin/jobs/clone/" + originalId).session(adminSession()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isEmpty())
				.andExpect(jsonPath("$.title").value("Original job"))
				.andExpect(jsonPath("$.description").value("Prototype source"))
				.andExpect(jsonPath("$.domain").value("Java"))
				.andExpect(jsonPath("$.location").value("Chisinau"))
				.andExpect(jsonPath("$.experienceLevel").value("Middle"))
				.andExpect(jsonPath("$.salary").value(2200))
				.andExpect(jsonPath("$.approved").value(true));

		mockMvc.perform(post("/admin/jobs/save")
						.session(adminSession())
						.param("title", "Cloned job")
						.param("description", original.getDescription())
						.param("domain", original.getDomain())
						.param("location", "Remote")
						.param("experienceLevel", "Senior")
						.param("salary", "3000")
						.param("approved", "true"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard#jobs-admin"));

		JobListing unchangedOriginal = jobListingRepository.findById(originalId).orElseThrow();
		assertThat(unchangedOriginal.getTitle()).isEqualTo("Original job");
		assertThat(unchangedOriginal.getLocation()).isEqualTo("Chisinau");
		assertThat(unchangedOriginal.getExperienceLevel()).isEqualTo("Middle");
		assertThat(unchangedOriginal.getSalary()).isEqualTo(2200);
		assertThat(jobListingRepository.findAll())
				.anySatisfy(job -> {
					assertThat(job.getId()).isNotEqualTo(originalId);
					assertThat(job.getTitle()).isEqualTo("Cloned job");
					assertThat(job.getDescription()).isEqualTo("Prototype source");
					assertThat(job.getDomain()).isEqualTo("Java");
					assertThat(job.getLocation()).isEqualTo("Remote");
					assertThat(job.getExperienceLevel()).isEqualTo("Senior");
					assertThat(job.getSalary()).isEqualTo(3000);
				});
	}

	@Test
	void adminCanGenerateTechnicalTestWithBuilder() throws Exception {
		mockMvc.perform(post("/admin/tests/create")
						.session(adminSession())
						.param("title", "Java Backend Test")
						.param("questions", "1. Explain REST.\n2. Build a repository method.")
						.param("difficulty", "Middle")
						.param("duration", "75")
						.param("domain", "Backend Java"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard#technical-tests"));

		assertThat(testRepository.findAll())
				.anySatisfy(test -> {
					assertThat(test.getTitle()).isEqualTo("Java Backend Test");
					assertThat(test.getQuestions()).contains("Explain REST");
					assertThat(test.getDifficulty()).isEqualTo("Middle");
					assertThat(test.getDuration()).isEqualTo(75);
					assertThat(test.getDomain()).isEqualTo("Backend Java");
				});
	}

	@Test
	void registerCreatesSelectedUserWithFactoryMethod() throws Exception {
		String email = "factory.recruiter@example.com";

		MvcResult register = mockMvc.perform(post("/register")
						.param("fullName", "Factory Recruiter")
						.param("email", email)
						.param("userType", "recruiter")
						.param("password", "secret123")
						.param("confirmPassword", "secret123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard"))
				.andReturn();

		MockHttpSession session = (MockHttpSession) register.getRequest().getSession(false);
		mockMvc.perform(get("/user-dashboard").session(session))
				.andExpect(status().isOk());

		User createdUser = userRepository.findByEmail(email).orElseThrow();
		assertThat(createdUser.getName()).isEqualTo("Factory Recruiter");
		assertThat(createdUser.getUserType()).isEqualTo("Recruiter");
		assertThat(createdUser.getClass().getSimpleName()).isEqualTo("Recruiter");
	}

	@Test
	void adminCanRejectJobAndCandidate() throws Exception {
		JobListing job = new JobListing();
		job.setTitle("Approved job");
		job.setDescription("Description");
		job.setDomain("QA");
		job.setLocation("Chisinau");
		job.setExperienceLevel("Middle");
		job.setSalary(1800);
		job.setApproved(true);
		job = jobListingRepository.save(job);

		Candidate candidate = new Candidate("Reject Me", "reject.me@example.com");
		candidate.setSkill("Java");
		candidate = candidateRepository.save(candidate);

		mockMvc.perform(post("/admin/jobs/approval/" + job.getId())
						.session(adminSession())
						.param("approved", "false"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard#jobs-admin"));

		mockMvc.perform(post("/candidates/reject/" + candidate.getId())
						.session(adminSession()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard#applications"));

		assertThat(jobListingRepository.findById(job.getId()).orElseThrow().isApproved()).isFalse();
		assertThat(candidateRepository.findById(candidate.getId()).orElseThrow().getStatus()).isEqualTo("Rejected");
	}

	@Test
	void recruiterCanAcceptAndRejectCandidateThroughCommand() throws Exception {
		AppUser acceptedProfile = appUserRepository.save(new AppUser(
				"COMMAND.ACCEPT@example.com",
				"pass",
				"Command Accept",
				"CANDIDATE"));
		AppUser rejectedProfile = appUserRepository.save(new AppUser(
				"command.reject@example.com",
				"pass",
				"Command Reject",
				"CANDIDATE"));

		Candidate candidateToAccept = new Candidate("Command Accept", "command.accept@example.com");
		candidateToAccept.setAppliedJobTitle("Backend Java");
		candidateToAccept = candidateRepository.save(candidateToAccept);

		Candidate candidateToReject = new Candidate("Command Reject", "command.reject@example.com");
		candidateToReject.setAppliedJobTitle("QA Engineer");
		candidateToReject = candidateRepository.save(candidateToReject);

		mockMvc.perform(post("/candidates/approve/" + candidateToAccept.getId())
						.session(recruiterSession()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		mockMvc.perform(post("/candidates/reject/" + candidateToReject.getId())
						.session(recruiterSession()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		Candidate accepted = candidateRepository.findById(candidateToAccept.getId()).orElseThrow();
		Candidate rejected = candidateRepository.findById(candidateToReject.getId()).orElseThrow();
		assertThat(accepted.getStatus()).isEqualTo("Accepted");
		assertThat(accepted.getRecruiterMessage()).contains("acceptata");
		assertThat(rejected.getStatus()).isEqualTo("Rejected");
		assertThat(rejected.getRecruiterMessage()).contains("respinsa");
		assertThat(appUserRepository.findById(acceptedProfile.getId()).orElseThrow().getNotifications())
				.contains("Your application was accepted");
		assertThat(appUserRepository.findById(rejectedProfile.getId()).orElseThrow().getNotifications())
				.contains("Your application was rejected");
	}

	@Test
	void candidateStatusChangesThroughStateContext() {
		Candidate candidate = new Candidate("State Candidate", "state.candidate@example.com");

		CandidateContext context = new CandidateContext(candidate);
		assertThat(context.getCurrentStatus()).isEqualTo("Applied");
		assertThat(context.request()).contains("aplicat");

		context.interview();
		assertThat(candidate.getStatus()).isEqualTo("Interviewed");

		context.accept();
		assertThat(candidate.getStatus()).isEqualTo("Accepted");

		context.reject();
		assertThat(candidate.getStatus()).isEqualTo("Rejected");

		context.accept();
		assertThat(candidate.getStatus()).isEqualTo("Accepted");
	}

	@Test
	void recruiterCanEvaluateCandidateThroughAdapter() throws Exception {
		Candidate candidate = new Candidate("Adapter Candidate", "adapter.candidate@example.com");
		candidate.setSkill("Java, Spring");
		candidate.setAppliedJobTitle("Backend Java");
		candidate = candidateRepository.save(candidate);

		mockMvc.perform(post("/candidates/evaluate/" + candidate.getId())
						.session(recruiterSession())
						.param("testScore", "85"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		Candidate evaluated = candidateRepository.findById(candidate.getId()).orElseThrow();
		assertThat(evaluated.getStatus()).isEqualTo("Interviewed");
		assertThat(evaluated.getRecruiterMessage()).isEqualTo("Candidate evaluated successfully. Result: PASS");
	}

	@Test
	void recruiterTestScoreEvaluationUsesAdapterAndExternalService() throws Exception {
		Candidate candidate = new Candidate("Adapter UI Candidate", "adapter.ui.candidate@example.com");
		candidate.setSkill("Java");
		candidate.setAppliedJobTitle("Technical Test");
		candidate = candidateRepository.save(candidate);

		mockMvc.perform(post("/candidates/evaluate/" + candidate.getId())
						.session(recruiterSession())
						.param("evaluationMethod", "testScore")
						.param("testScore", "65")
						.param("experienceYears", "5")
						.param("recruiterNotes", "Verificare prin serviciu extern."))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		Candidate evaluated = candidateRepository.findById(candidate.getId()).orElseThrow();
		assertThat(evaluated.getStatus()).isEqualTo("Rejected");
		assertThat(evaluated.getRecruiterMessage()).contains("Candidate evaluated successfully. Result: FAIL");
		assertThat(evaluated.getRecruiterMessage()).doesNotContain("Evaluation method:");
	}

	@Test
	void recruiterWithMixedCaseRoleCanEvaluateCandidateThroughAdapter() throws Exception {
		Candidate candidate = new Candidate("Mixed Role Recruiter Candidate", "mixed.role.candidate@example.com");
		candidate.setSkill("Java");
		candidate = candidateRepository.save(candidate);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentRole", "Recruiter");
		session.setAttribute("currentUser", "Recruiter Test");
		session.setAttribute("currentUsername", "recruiter");

		mockMvc.perform(post("/candidates/evaluate/" + candidate.getId())
						.session(session)
						.param("testScore", "85"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		Candidate evaluated = candidateRepository.findById(candidate.getId()).orElseThrow();
		assertThat(evaluated.getRecruiterMessage()).isEqualTo("Candidate evaluated successfully. Result: PASS");
	}

	@Test
	void recruiterCanEvaluateCandidateWithSelectedStrategy() throws Exception {
		Candidate candidate = new Candidate("Strategy Candidate", "strategy.candidate@example.com");
		candidate.setSkill("Java, SQL");
		candidate = candidateRepository.save(candidate);

		mockMvc.perform(post("/candidates/evaluate/" + candidate.getId())
						.session(recruiterSession())
						.param("evaluationMethod", "experience")
						.param("testScore", "40")
						.param("experienceYears", "4")
						.param("recruiterNotes", "Experienta relevanta pentru rol."))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		Candidate evaluated = candidateRepository.findById(candidate.getId()).orElseThrow();
		assertThat(evaluated.getStatus()).isEqualTo("Interviewed");
		assertThat(evaluated.getRecruiterMessage()).contains("Evaluation method: Experience Evaluation");
		assertThat(evaluated.getRecruiterMessage()).contains("Result: PASS");
		assertThat(evaluated.getRecruiterMessage()).contains("Experienta relevanta pentru rol.");
	}

	@Test
	void candidateCanAddSkillThroughCompositeProfile() throws Exception {
		mockMvc.perform(post("/skills/add")
						.session(candidateSession())
						.param("category", "Backend Skills")
						.param("name", "Spring Boot"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#my-skills"));

		assertThat(skillRepository.findByOwnerUsernameOrderByCategoryAscNameAsc("user"))
				.anySatisfy(skill -> {
					assertThat(skill.getCategory()).isEqualTo("Backend Skills");
					assertThat(skill.getName()).isEqualTo("Spring Boot");
				});
	}

	@Test
	void recruiterCanProcessRecruitmentThroughFacade() throws Exception {
		Candidate candidate = new Candidate("Facade Candidate", "facade.candidate@example.com");
		candidate.setAppliedJobTitle("Backend Java");
		candidate = candidateRepository.save(candidate);

		mockMvc.perform(post("/recruitment/process/" + candidate.getId())
						.session(recruiterSession())
						.param("testScore", "88"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		Candidate processed = candidateRepository.findById(candidate.getId()).orElseThrow();
		assertThat(processed.getRecruiterMessage()).contains("Candidate verified");
		assertThat(processed.getRecruiterMessage()).contains("Job analyzed");
		assertThat(processed.getRecruiterMessage()).contains("Test evaluated");
		assertThat(processed.getRecruiterMessage()).contains("Final report generated");
		assertThat(processed.getRecruiterMessage()).contains("Recruitment process completed successfully");
		assertThat(processed.getStatus()).isEqualTo("Interviewed");
	}

	@Test
	void observerNotificationsAreSavedForRecruiterAndCandidate() throws Exception {
		AppUser candidateProfile = appUserRepository.save(new AppUser(
				"observer.candidate@example.com",
				"pass",
				"Observer Candidate",
				"CANDIDATE"));
		AppUser recruiterProfile = appUserRepository.save(new AppUser(
				"observer.recruiter@example.com",
				"pass",
				"Observer Recruiter",
				"RECRUITER"));

		JobListing job = new JobListing();
		job.setTitle("Java Developer");
		job.setDescription("Backend role");
		job.setDomain("Java");
		job.setLocation("Chisinau");
		job.setExperienceLevel("Junior");
		job.setSalary(1400);
		job.setApproved(true);
		job = jobListingRepository.save(job);

		MockHttpSession candidateSession = new MockHttpSession();
		candidateSession.setAttribute("currentRole", "CANDIDATE");
		candidateSession.setAttribute("currentUser", "Observer Candidate");
		candidateSession.setAttribute("currentUsername", "observer.candidate@example.com");

		mockMvc.perform(post("/jobs/apply/" + job.getId()).session(candidateSession))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/jobs"));

		AppUser notifiedRecruiter = appUserRepository.findById(recruiterProfile.getId()).orElseThrow();
		assertThat(notifiedRecruiter.getNotifications()).contains("New candidate applied for Java Developer");

		Candidate application = candidateRepository.findByEmail("observer.candidate@example.com").get(0);
		mockMvc.perform(post("/candidates/approve/" + application.getId()).session(recruiterSession()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user-dashboard#recruiter-candidates"));

		AppUser notifiedCandidate = appUserRepository.findById(candidateProfile.getId()).orElseThrow();
		assertThat(notifiedCandidate.getNotifications()).contains("Your application was accepted");
	}

	@Test
	void registeredCandidateCanApplyWithoutDuplicateEmailError() throws Exception {
		AppUser profile = appUserRepository.save(new AppUser(
				"registered.candidate@example.com",
				"pass",
				"Registered Candidate",
				"CANDIDATE"));
		Candidate registeredCandidate = new Candidate("Registered Candidate", "registered.candidate@example.com");
		registeredCandidate = candidateRepository.save(registeredCandidate);

		JobListing job = new JobListing();
		job.setTitle("QA Engineer");
		job.setDescription("Testing role");
		job.setDomain("QA");
		job.setLocation("Chisinau");
		job.setExperienceLevel("Junior");
		job.setSalary(1200);
		job.setApproved(true);
		job = jobListingRepository.save(job);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentRole", "CANDIDATE");
		session.setAttribute("currentUser", "Registered Candidate");
		session.setAttribute("currentUsername", "registered.candidate@example.com");

		mockMvc.perform(post("/jobs/apply/" + job.getId()).session(session))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/jobs"));

		Candidate application = candidateRepository.findById(registeredCandidate.getId()).orElseThrow();
		assertThat(application.getAppliedJobTitle()).isEqualTo("QA Engineer");
		assertThat(application.getStatus()).isEqualTo("Applied");
		assertThat(appUserRepository.findById(profile.getId()).orElseThrow().getNotifications())
				.contains("Ai aplicat la jobul: QA Engineer");
	}

	@Test
	void adminCanDeleteJobAndCandidate() throws Exception {
		JobListing job = new JobListing();
		job.setTitle("Delete job");
		job.setDescription("Description");
		job.setDomain("DevOps");
		job.setLocation("Balti");
		job.setExperienceLevel("Junior");
		job.setSalary(1200);
		job = jobListingRepository.save(job);

		Candidate candidate = new Candidate("Delete Me", "delete.me@example.com");
		candidate.setSkill("Testing");
		candidate = candidateRepository.save(candidate);

		mockMvc.perform(post("/admin/jobs/delete/" + job.getId()).session(adminSession()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard#jobs-admin"));

		mockMvc.perform(post("/admin/candidates/delete/" + candidate.getId()).session(adminSession()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin-dashboard#applications"));

		assertThat(jobListingRepository.existsById(job.getId())).isFalse();
		assertThat(candidateRepository.existsById(candidate.getId())).isFalse();
	}

	private MockHttpSession adminSession() {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentRole", "ADMIN");
		session.setAttribute("currentUser", "Admin Test");
		session.setAttribute("currentUsername", "admin");
		return session;
	}

	private MockHttpSession recruiterSession() {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentRole", "RECRUITER");
		session.setAttribute("currentUser", "Recruiter Test");
		session.setAttribute("currentUsername", "recruiter");
		return session;
	}

	private MockHttpSession candidateSession() {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("currentRole", "CANDIDATE");
		session.setAttribute("currentUser", "User Test");
		session.setAttribute("currentUsername", "user");
		return session;
	}

}
