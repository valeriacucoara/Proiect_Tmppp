package md.utm.proiect_Tmppp.flyweight;

public class TestTypeFlyweight {

    private String repeatingState;

    public TestTypeFlyweight(String repeatingState) {
        this.repeatingState = repeatingState;
    }

    public String operation(String uniqueState) {
        return "Tip test comun: " + repeatingState + " | Detalii candidat: " + uniqueState;
    }
}