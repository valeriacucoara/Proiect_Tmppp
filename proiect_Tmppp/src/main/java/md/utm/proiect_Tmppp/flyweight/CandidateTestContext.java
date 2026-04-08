package md.utm.proiect_Tmppp.flyweight;

public class CandidateTestContext {

    private String uniqueState;
    private TestTypeFlyweight flyweight;

    public CandidateTestContext(String repeatingState, String uniqueState, TestTypeFactory factory) {
        this.uniqueState = uniqueState;
        this.flyweight = factory.getFlyweight(repeatingState);
    }

    public String operation() {
        return flyweight.operation(uniqueState);
    }
}