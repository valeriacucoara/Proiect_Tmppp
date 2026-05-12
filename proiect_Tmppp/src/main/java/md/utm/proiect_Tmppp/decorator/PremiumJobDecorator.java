package md.utm.proiect_Tmppp.decorator;

public class PremiumJobDecorator extends JobDisplayDecorator {

    public PremiumJobDecorator(JobDisplay wrappee) {
        super(wrappee);
    }

    @Override
    public String showJob() {
        return wrappee.showJob() + " [PREMIUM] - Extra benefits included!";
    }
}