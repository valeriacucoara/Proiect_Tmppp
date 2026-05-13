package md.utm.proiect_Tmppp.builder;

import md.utm.proiect_Tmppp.entity.Test;

// Director: coordoneaza ordinea pasilor de construire a testului.
public class TestDirector {
    private final TestBuilder builder;

    public TestDirector(TestBuilder builder) {
        this.builder = builder;
    }

    public Test construct(String title, String questions, String difficulty, int duration, String domain) {
        builder.reset();
        builder.buildTitle(title);
        builder.buildQuestions(questions);
        builder.buildDifficulty(difficulty);
        builder.buildDuration(duration);
        builder.buildDomain(domain);
        return builder.getResult();
    }
}
