package com.dstevens.exalted;

public class Person implements Comparable<Person> {

    private static final double CHANCE_TO_EXALT = 0.375;
    
    private int years;
    
    public Person() {
        this.years = 0;
    }

    public int getYears() {
        return years;
    }
    
    public void growOlder() {
        this.years++;
    }
    
    protected void setYears(int years) {
        this.years = years;
    }

    public Age getAge() {
        return Age.ageOf(this);
    }

    public boolean theBloodRunsTrue() {
        return years == 25 && Math.random() <= CHANCE_TO_EXALT;
    }
    
    @Override
    public int compareTo(Person o) {
        return this.years - o.years;
    }
}
