package md.utm.proiect_Tmppp.proxy;

public class CandidateCvProxy implements CandidateCvAccess {

    private RealCandidateCvService realService;
    private String role;

    public CandidateCvProxy(RealCandidateCvService realService, String role) {
        this.realService = realService;
        this.role = role;
    }

    public boolean checkAccess() {
        return role.equalsIgnoreCase("recruiter");
    }

    @Override
    public String viewCv() {
        if (checkAccess()) {
            return realService.viewCv();
        }
        return "Acces interzis: doar recruiterul poate vizualiza CV-ul complet.";
    }
}