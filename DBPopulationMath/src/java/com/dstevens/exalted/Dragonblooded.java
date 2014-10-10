package com.dstevens.exalted;

public class Dragonblooded extends Person {

    private final Career career;
    private final boolean celebate;
    
    public Dragonblooded(int age, Career career, boolean celebate) {
        setYears(age);
        this.career = career;
        this.celebate = celebate;
    }

    public Career getCareer() {
        return career;
    }
    
    public boolean isCelebate() {
        return celebate;
    }
    
    public boolean isEligableForMarriage() {
        return getAge() == Age.ACTIVE && !celebate; 
    }
    
}
