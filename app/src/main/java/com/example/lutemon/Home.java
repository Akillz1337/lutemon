package com.example.lutemon;
import com.example.lutemon.type.White;
import com.example.lutemon.type.Green;
import com.example.lutemon.type.Pink;
import com.example.lutemon.type.Orange;
import com.example.lutemon.type.Black;
public class Home extends Storage {
    private static Home instance = null;

    private Home() {
        super("Home");
    }

    public static Home getInstance() {
        if (instance == null) {
            instance = new Home();
        }
        return instance;
    }

    public Lutemon createLutemon(String color, String name) {
        Lutemon lutemon;

        switch (color.toLowerCase()) {
            case "white":
                lutemon = new White(name);
                break;
            case "green":
                lutemon = new Green(name);
                break;
            case "pink":
                lutemon = new Pink(name);
                break;
            case "orange":
                lutemon = new Orange(name);
                break;
            case "black":
                lutemon = new Black(name);
                break;
            default:
                throw new IllegalArgumentException("Invalid color: " + color);
        }

        addLutemon(lutemon);
        return lutemon;
    }
}