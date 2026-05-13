package md.utm.proiect_Tmppp.builder;

import md.utm.proiect_Tmppp.entity.Test;

// Concrete Builder: construieste efectiv produsul Test, camp cu camp.
public class RecruitmentTestBuilder implements TestBuilder {
    private Test test;

    public RecruitmentTestBuilder() {
        reset();
    }

    @Override
    public void reset() {
        test = new Test();
    }

    @Override
    public void buildTitle(String title) {
        test.setTitle(title);
    }

    @Override
    public void buildQuestions(String questions) {
        test.setQuestions(questions);
    }

    @Override
    public void buildDifficulty(String difficulty) {
        test.setDifficulty(difficulty);
    }

    @Override
    public void buildDuration(int duration) {
        test.setDuration(duration);
    }

    @Override
    public void buildDomain(String domain) {
        test.setDomain(domain);
    }

    @Override
    public Test getResult() {
        return test;
    }
}
