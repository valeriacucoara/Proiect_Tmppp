package md.utm.proiect_Tmppp.decorator;

public class BasicJobDisplay implements JobDisplay {

    private String title;
    private String company;
    private double salary;

    public BasicJobDisplay(String title, String company, double salary) {
        this.title = title;
        this.company = company;
        this.salary = salary;
    }

    @Override
    public String showJob() {
        return "Job: " + title +
                "\nCompanie: " + company +
                "\nSalariu: " + salary;
    }
}