package com.example.lutemon;
public class TrainingArea extends Storage {
    private static TrainingArea instance = null;

    private TrainingArea() {
        super("Training Area");
    }

    public static TrainingArea getInstance() {
        if (instance == null) {
            instance = new TrainingArea();
        }
        return instance;
    }

    public void train() {
        for (Lutemon lutemon : getLutemons()) {
            lutemon.addExperience();
            lutemon.recordTrainingDay(); // Record training day for stats
        }
    }
}