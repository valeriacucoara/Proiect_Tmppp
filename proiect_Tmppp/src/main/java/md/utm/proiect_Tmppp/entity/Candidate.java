package md.utm.proiect_Tmppp.entity;

public class Candidate implements User {
    @Override
    public void showRole() {
        System.out.println("Sunt un candidat.");
    }
}