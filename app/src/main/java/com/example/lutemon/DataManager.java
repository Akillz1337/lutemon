package com.example.lutemon;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.lutemon.type.White;
import com.example.lutemon.type.Green;
import com.example.lutemon.type.Pink;
import com.example.lutemon.type.Orange;
import com.example.lutemon.type.Black;

public class DataManager {
    private static final String TAG = "DataManager";
    private static final String PREFS_NAME = "LutemonPrefs";
    private static final String HOME_LUTEMONS = "home_lutemons";
    private static final String TRAINING_LUTEMONS = "training_lutemons";
    private static final String BATTLE_LUTEMONS = "battle_lutemons";
    private static final String ID_COUNTER = "id_counter";

    private final Context context;
    private final Gson gson;

    // Serialized Lutemon class that stores everything we need
    private static class SerializedLutemon {
        String name;
        String color;
        int attack;
        int defense;
        int experience;
        int health;
        int maxHealth;
        int id;
        // New stats fields
        int battlesParticipated;
        int battlesWon;
        int trainingDays;
        int damageDealt;
        int damageTaken;
        int kills;

        SerializedLutemon(Lutemon lutemon) {
            this.name = lutemon.getName();
            this.color = lutemon.getColor();
            this.attack = lutemon.getAttack();
            this.defense = lutemon.getDefense();
            this.experience = lutemon.getExperience();
            this.health = lutemon.getHealth();
            this.maxHealth = lutemon.getMaxHealth();
            this.id = lutemon.getId();
            // Save stats
            this.battlesParticipated = lutemon.getBattlesParticipated();
            this.battlesWon = lutemon.getBattlesWon();
            this.trainingDays = lutemon.getTrainingDays();
            this.damageDealt = lutemon.getDamageDealt();
            this.damageTaken = lutemon.getDamageTaken();
            this.kills = lutemon.getKills();
        }

        Lutemon toLutemon() {
            Lutemon lutemon;

            // Create the correct Lutemon type
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
                    throw new IllegalArgumentException("Unknown Lutemon color: " + color);
            }

            // Apply all the values
            lutemon.setName(name);
            lutemon.setAttack(attack - lutemon.getExperience()); // Adjust for initial experience
            lutemon.setDefense(defense);
            lutemon.setHealth(health);
            lutemon.setMaxHealth(maxHealth);
            lutemon.setExperience(experience);
            lutemon.setId(id);

            // Restore stats
            lutemon.setBattlesParticipated(battlesParticipated);
            lutemon.setBattlesWon(battlesWon);
            lutemon.setTrainingDays(trainingDays);
            lutemon.setDamageDealt(damageDealt);
            lutemon.setDamageTaken(damageTaken);
            lutemon.setKills(kills);

            return lutemon;
        }
    }

    public DataManager(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().create();
    }

    public void saveData() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        try {
            // Convert Lutemons to SerializedLutemons first
            List<SerializedLutemon> homeSerializedLutemons = new ArrayList<>();
            List<SerializedLutemon> trainingSerializedLutemons = new ArrayList<>();
            List<SerializedLutemon> battleSerializedLutemons = new ArrayList<>();

            for (Lutemon lutemon : Home.getInstance().getLutemons()) {
                homeSerializedLutemons.add(new SerializedLutemon(lutemon));
            }

            for (Lutemon lutemon : TrainingArea.getInstance().getLutemons()) {
                trainingSerializedLutemons.add(new SerializedLutemon(lutemon));
            }

            for (Lutemon lutemon : BattleField.getInstance().getLutemons()) {
                battleSerializedLutemons.add(new SerializedLutemon(lutemon));
            }

            // Save to SharedPreferences
            editor.putString(HOME_LUTEMONS, gson.toJson(homeSerializedLutemons));
            editor.putString(TRAINING_LUTEMONS, gson.toJson(trainingSerializedLutemons));
            editor.putString(BATTLE_LUTEMONS, gson.toJson(battleSerializedLutemons));
            editor.putInt(ID_COUNTER, Lutemon.getNumberOfCreatedLutemons());

            editor.apply();

            Log.d(TAG, "Saved: Home=" + homeSerializedLutemons.size() +
                    ", Training=" + trainingSerializedLutemons.size() +
                    ", Battle=" + battleSerializedLutemons.size());
        } catch (Exception e) {
            Log.e(TAG, "Error saving data", e);
        }
    }

    public boolean loadData() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        try {
            // Clear existing data
            Home.getInstance().getLutemons().clear();
            TrainingArea.getInstance().getLutemons().clear();
            BattleField.getInstance().getLutemons().clear();

            // Define type for List<SerializedLutemon>
            Type listType = new TypeToken<List<SerializedLutemon>>(){}.getType();

            // Load home Lutemons
            String homeJson = prefs.getString(HOME_LUTEMONS, "");
            if (!homeJson.isEmpty()) {
                List<SerializedLutemon> homeSerialized = gson.fromJson(homeJson, listType);
                for (SerializedLutemon serialized : homeSerialized) {
                    Lutemon lutemon = serialized.toLutemon();
                    Home.getInstance().addLutemon(lutemon);
                }
                Log.d(TAG, "Loaded " + homeSerialized.size() + " Lutemons to Home");
            }

            // Load training Lutemons
            String trainingJson = prefs.getString(TRAINING_LUTEMONS, "");
            if (!trainingJson.isEmpty()) {
                List<SerializedLutemon> trainingSerialized = gson.fromJson(trainingJson, listType);
                for (SerializedLutemon serialized : trainingSerialized) {
                    Lutemon lutemon = serialized.toLutemon();
                    TrainingArea.getInstance().addLutemon(lutemon);
                }
                Log.d(TAG, "Loaded " + trainingSerialized.size() + " Lutemons to Training");
            }

            // Load battle Lutemons
            String battleJson = prefs.getString(BATTLE_LUTEMONS, "");
            if (!battleJson.isEmpty()) {
                List<SerializedLutemon> battleSerialized = gson.fromJson(battleJson, listType);
                for (SerializedLutemon serialized : battleSerialized) {
                    Lutemon lutemon = serialized.toLutemon();
                    BattleField.getInstance().addLutemon(lutemon);
                }
                Log.d(TAG, "Loaded " + battleSerialized.size() + " Lutemons to Battle");
            }

            // Set the ID counter
            int idCounter = prefs.getInt(ID_COUNTER, 0);
            if (idCounter > 0) {
                Lutemon.setIdCounter(idCounter);
                Log.d(TAG, "Set ID counter to " + idCounter);
            }

            return !homeJson.isEmpty() || !trainingJson.isEmpty() || !battleJson.isEmpty();
        } catch (Exception e) {
            Log.e(TAG, "Error loading data", e);
            return false;
        }
    }
}