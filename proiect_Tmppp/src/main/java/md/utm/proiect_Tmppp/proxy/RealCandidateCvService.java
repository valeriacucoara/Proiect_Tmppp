package md.utm.proiect_Tmppp.proxy;

public class RealCandidateCvService implements CandidateCvAccess {

    @Override
    public String viewCv() {
        return "CV complet al candidatului: experienta, skill-uri, educatie, proiecte.";
    }
}