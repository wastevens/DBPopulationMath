package com.dstevens.exalted;


public class Couple {

    private final Dragonblooded husband;
    private final Dragonblooded wife;
    private int yearsBetweenChildren;
    private int numberOfChildren;
    
    public Couple(Dragonblooded husband, Dragonblooded wife) {
        this.husband = husband;
        this.wife = wife;
        this.yearsBetweenChildren = 0;
        this.numberOfChildren = 0;
    }
    
    public void happyAniversary() {
        this.yearsBetweenChildren++;
    }
    
    public boolean wantChild() {
        return ifYoungEnough() && 
               itsBeenLongEnough();
    }
    
    private boolean ifYoungEnough() {
        return husband.getAge() != Age.RETIRED && wife.getAge() != Age.RETIRED;
    }

    private boolean itsBeenLongEnough() {
        return yearsBetweenChildren % (Math.min(6 + (numberOfChildren*2), 12)) == 0;
    }
    
    public Person haveChild() {
        this.yearsBetweenChildren = 0;
        this.numberOfChildren++;
        return new Person();
    }
    
    public boolean contains(Dragonblooded spouse) {
        return husband == spouse || wife == spouse;
    }
    
    public Dragonblooded youngestSpouse() {
        if(husband.getYears() < wife.getYears()) {
            return husband;
        }
        return wife;
    }
    
}
