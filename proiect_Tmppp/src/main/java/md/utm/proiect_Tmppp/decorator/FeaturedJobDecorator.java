package md.utm.proiect_Tmppp.decorator;

public class FeaturedJobDecorator extends JobDisplayDecorator {

    public FeaturedJobDecorator(JobDisplay wrappee) {
        super(wrappee);
    }

    @Override
    public String showJob() {
        return super.showJob() + "\n[FEATURED JOB]";
    }
}