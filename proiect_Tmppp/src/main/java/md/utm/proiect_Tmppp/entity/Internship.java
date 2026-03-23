package md.utm.proiect_Tmppp.entity;

public class Internship extends JobListing {

    private int duration;

    public Internship() {}

    public Internship(Internship prototype) {
        super(prototype);
        this.duration = prototype.duration;
    }

    @Override
    public Internship clone() {
        return new Internship(this);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}