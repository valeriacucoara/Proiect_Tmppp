package md.utm.proiect_Tmppp.iterator;

import java.util.ArrayList;
import java.util.List;

public class CandidateList implements CandidateCollection {

    private List<String> candidates = new ArrayList<>();

    public void addCandidate(String name) {
        candidates.add(name);
    }

    @Override
    public CandidateIterator createIterator() {
        return new CandidateListIterator();
    }

    private class CandidateListIterator implements CandidateIterator {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < candidates.size();
        }

        @Override
        public String next() {
            return candidates.get(index++);
        }
    }
}