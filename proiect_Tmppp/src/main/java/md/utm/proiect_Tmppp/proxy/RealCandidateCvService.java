package md.utm.proiect_Tmppp.proxy;

public class RealCandidateCvService implements CandidateCvAccess {

    private final String cvContent;

    public RealCandidateCvService() {
        this("CV complet al candidatului: experienta, skill-uri, educatie, proiecte.");
    }

    public RealCandidateCvService(String cvContent) {
        this.cvContent = cvContent;
    }

    @Override
    public String viewCv() {
        return cvContent;
    }
}
