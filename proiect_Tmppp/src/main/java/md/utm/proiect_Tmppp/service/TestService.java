package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.Test;

import java.util.List;

public interface TestService {
    Test createTest(String title, String questions, String difficulty, int duration, String domain);

    List<Test> getAllTests();
}
