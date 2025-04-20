package com.example.lutemon;
import java.util.ArrayList;
import java.util.HashMap;

public class Storage {
    private String name;
    private HashMap<Integer, Lutemon> lutemons = new HashMap<>();

    public Storage(String name) {
        this.name = name;
    }

    public void addLutemon(Lutemon lutemon) {
        lutemons.put(lutemon.getId(), lutemon);
    }

    public Lutemon getLutemon(int id) {
        return lutemons.get(id);
    }

    public void removeLutemon(int id) {
        lutemons.remove(id);
    }

    public ArrayList<Lutemon> getLutemons() {
        return new ArrayList<>(lutemons.values());
    }

    public String getName() {
        return name;
    }
}