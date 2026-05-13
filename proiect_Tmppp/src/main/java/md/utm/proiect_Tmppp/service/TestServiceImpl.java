package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.builder.RecruitmentTestBuilder;
import md.utm.proiect_Tmppp.builder.TestDirector;
import md.utm.proiect_Tmppp.entity.Test;
import md.utm.proiect_Tmppp.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;

    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public Test createTest(String title, String questions, String difficulty, int duration, String domain) {
        RecruitmentTestBuilder builder = new RecruitmentTestBuilder();
        TestDirector director = new TestDirector(builder);
        Test test = director.construct(title, questions, difficulty, duration, domain);
        return testRepository.save(test);
    }

    @Override
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }
}
