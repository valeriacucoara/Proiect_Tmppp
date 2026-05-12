package md.utm.proiect_Tmppp.strategy;

import java.util.ArrayList;
import java.util.List;

public class CompositeEvaluationStrategy implements CandidateEvaluationStrategy {

    private List<CandidateEvaluationStrategy> strategies = new ArrayList<>();

    public void addStrategy(CandidateEvaluationStrategy strategy) {
        strategies.add(strategy);
    }

    public void removeStrategy(CandidateEvaluationStrategy strategy) {
        strategies.remove(strategy);
    }

    @Override
    public String evaluate(String candidateName, int score, int experienceYears, int skillCount) {
        StringBuilder result = new StringBuilder();
        result.append("Evaluare compozita pentru ").append(candidateName).append(":\n");

        for (CandidateEvaluationStrategy strategy : strategies) {
            result.append("- ").append(strategy.evaluate(candidateName, score, experienceYears, skillCount)).append("\n");
        }

        // Overall decision based on average or something
        int totalScore = score + experienceYears * 10 + skillCount * 5;
        String overall = totalScore > 150 ? "ACCEPTAT" : totalScore > 100 ? "POSIBIL" : "RESPINS";
        result.append("Decizie overall: ").append(overall);

        return result.toString();
    }
}