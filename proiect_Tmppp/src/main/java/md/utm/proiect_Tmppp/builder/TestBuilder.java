package md.utm.proiect_Tmppp.builder;

import md.utm.proiect_Tmppp.entity.Test;

// Builder: defineste pasii obligatorii pentru construirea unui test tehnic.
public interface TestBuilder {
    void reset();

    void buildTitle(String title);

    void buildQuestions(String questions);

    void buildDifficulty(String difficulty);

    void buildDuration(int duration);

    void buildDomain(String domain);

    Test getResult();
}
