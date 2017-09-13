package me.koenn.blockrpg.data;

import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class SkillPoints {

    private int Strength;
    private int Luck;
    private int Endurance;
    private int Intelligence;
    private int Charisma;

    public SkillPoints() {
    }

    public SkillPoints(JSONObject skillPoints) {
        this(
                Math.toIntExact((long) skillPoints.get("strength")),
                Math.toIntExact((long) skillPoints.get("luck")),
                Math.toIntExact((long) skillPoints.get("endurance")),
                Math.toIntExact((long) skillPoints.get("intelligence")),
                Math.toIntExact((long) skillPoints.get("charisma"))
        );
    }

    public SkillPoints(int strength, int luck, int endurance, int intelligence, int charisma) {
        Strength = strength;
        Luck = luck;
        Endurance = endurance;
        Intelligence = intelligence;
        Charisma = charisma;
    }

    public int getStrength() {
        return Strength;
    }

    public void setStrength(int strength) {
        Strength = strength;
    }

    public int getLuck() {
        return Luck;
    }

    public void setLuck(int luck) {
        Luck = luck;
    }

    public int getEndurance() {
        return Endurance;
    }

    public void setEndurance(int endurance) {
        Endurance = endurance;
    }

    public int getIntelligence() {
        return Intelligence;
    }

    public void setIntelligence(int intelligence) {
        Intelligence = intelligence;
    }

    public int getCharisma() {
        return Charisma;
    }

    public void setCharisma(int charisma) {
        Charisma = charisma;
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("strength", this.Strength);
        jsonObject.put("luck", this.Luck);
        jsonObject.put("endurance", this.Endurance);
        jsonObject.put("intelligence", this.Intelligence);
        jsonObject.put("charisma", this.Charisma);
        return jsonObject;
    }
}
