package com.dstevens.exalted;

public class ActuarialTable {

    private static final double INFANT_MORTALITY = 0.01;
    private static final double CHILDHOOD_MORTALITY = 0.0005;
    private static final double YOUTH_MORTALITY = 0.005;
    private static final double ACTIVE_MORTALITY = 0.0002;
    private static final double RETIRED_MORTALITY = 0.00005;

    public boolean kill(Person p) {
        if(Dragonblooded.class.isAssignableFrom(p.getClass())) {
            return dragonbloodedMortality(Dragonblooded.class.cast(p));
        }
        return mortalMortality(p);
    }
    private boolean dragonbloodedMortality(Dragonblooded db) {
        switch (db.getAge()) {
        case CHILD:
            return false;
        case YOUTH:
            return youthMortality(db);
        case ACTIVE:
            return activeMortality(db);
        case RETIRED:
            return retiredMortality(db);
        default:
            return true;
        }
    }
    
    public boolean mortalMortality(Person p) {
        switch (p.getAge()) {
        case FETUS:
            return false;
        case INFANT:
            return check(INFANT_MORTALITY);
        case CHILD:
            return check(CHILDHOOD_MORTALITY);
        default:
            return true;
        }
    }

    private boolean youthMortality(Dragonblooded dynast) {
        double trainingMortality = 0;
        switch (dynast.getCareer()) {
            case BUREAUCRAT:
                trainingMortality = 0;
            case MONK:
                trainingMortality = 0.001;
            case SORCERER:
                trainingMortality = 0.005;
            case SOLDIER:
                trainingMortality = 0.01;
        }
        return check(YOUTH_MORTALITY + trainingMortality);
    }
    
    private boolean activeMortality(Dragonblooded dynast) {
        double careerMortality = 0;
        switch (dynast.getCareer()) {
            case BUREAUCRAT:
                careerMortality = 0;
            case MONK:
                careerMortality = 0.001;
            case SORCERER:
                careerMortality = 0.005;
            case SOLDIER:
                careerMortality = 0.01;
        }
        return check(ACTIVE_MORTALITY + careerMortality);
    }
    
    private boolean retiredMortality(Dragonblooded dynast) {
        return check(RETIRED_MORTALITY);
    }
    
    private boolean check(double probability) {
        return Math.random() <= probability;
    }
    
}
