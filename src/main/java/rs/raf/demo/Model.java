package rs.raf.demo;

import java.util.ArrayList;

public class Model {

    private String day;
    private ArrayList<String> meals;
    public static ArrayList<Model> model = new ArrayList<>();

    public Model(String day) {
        this.day = day;
        meals = new ArrayList<>();
    }

    public String getDay() {
        return day;
    }

    public ArrayList<String> getMeals() {
        return meals;
    }

}
