package md.utm.proiect_Tmppp.decorator;

public class UrgentJobDecorator extends JobDisplayDecorator {

    public UrgentJobDecorator(JobDisplay wrappee) {
        super(wrappee);
    }

    @Override
    public String showJob() {
        return super.showJob() + "\n[URGENT JOB]";
    }
}