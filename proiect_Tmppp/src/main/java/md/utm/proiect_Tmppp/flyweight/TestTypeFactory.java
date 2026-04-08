package md.utm.proiect_Tmppp.flyweight;

import java.util.HashMap;
import java.util.Map;

public class TestTypeFactory {

    private Map<String, TestTypeFlyweight> cache = new HashMap<>();

    public TestTypeFlyweight getFlyweight(String repeatingState) {
        if (cache.get(repeatingState) == null) {
            cache.put(repeatingState, new TestTypeFlyweight(repeatingState));
        }
        return cache.get(repeatingState);
    }

    public int getCacheSize() {
        return cache.size();
    }
}