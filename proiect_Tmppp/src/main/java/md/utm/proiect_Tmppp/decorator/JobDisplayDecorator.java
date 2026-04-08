package md.utm.proiect_Tmppp.decorator;

public abstract class JobDisplayDecorator implements JobDisplay {

    protected JobDisplay wrappee;

    public JobDisplayDecorator(JobDisplay wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public String showJob() {
        return wrappee.showJob();
    }
}