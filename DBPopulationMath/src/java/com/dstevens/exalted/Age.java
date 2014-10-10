package com.dstevens.exalted;

import java.util.Random;

public enum Age {
    FETUS(0),
    INFANT(5),
    CHILD(25),
    YOUTH(50),
    ACTIVE(250),
    RETIRED(300),
    DEAD(301);
    
    private final int maximumYears;
    private final Random random = new Random();

    private Age(int maximumYears) {
        this.maximumYears = maximumYears;
    }
    
    public static Age ageOf(Person p) {
        for (Age age : Age.values()) {
            if(p.getYears() <= age.maximumYears) {
                return age;
            }
        }
        return Age.DEAD;
    }
    
    public int maximumYears() {
        return maximumYears;
    }
    
    public int range() {
        int base = Age.values()[this.ordinal()-1].maximumYears;
        int range = random.nextInt(Age.values()[this.ordinal()].maximumYears - base);
        return base + range +1;
    }
    
}
