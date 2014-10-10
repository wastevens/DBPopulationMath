package com.dstevens.exalted;

import java.util.Random;

public enum Career {

    BUREAUCRAT,
    SOLDIER,
    MONK {
        @Override
        public boolean isCelebate() {
            return random.nextDouble() <= .1;
        }
    },
    SORCERER;
    
    private static final Random random = new Random();
    
    public static Career pickCareer() {
        //The four schools range from 320 to 80, in order: Bureaucrat, Soldier, Monk, Sorcerer.
        //Only a small number of Monks actually take vows of celebacy.
        //This gives us an approximate value for the proportions of the careers;
        //320 : 240 : 160 : 80
        //4:3:2:1
        //1: Sorcerer
        //2-3: Monk
        //4-6: Soldier
        //7-10: Bureaucrat
        int choose = random.nextInt(10)+1;
        if(choose == 1) {
            return SORCERER;
        }
        if(choose <= 3) {
            return MONK;
        }
        if(choose <= 6) {
            return SOLDIER;
        }
        return BUREAUCRAT;
    }

    public boolean isCelebate() {
        return false;
    }
}
