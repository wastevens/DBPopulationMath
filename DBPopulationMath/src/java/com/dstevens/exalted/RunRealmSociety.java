package com.dstevens.exalted;

public class RunRealmSociety {

    public static void main(String a[]) {
        RealmSociety realmSociety = new RealmSociety();
        realmSociety.seedSociety(167, 2500, 4500, 3000);
        
        for(int i=0;i<900;i++) {
            realmSociety.anotherYearPasses();
            if(i%125 == 0) {
                realmSociety.printLongCensus();
            } else if (i%25 == 0){
                realmSociety.printAbbreviatedCensus();
            }
        }
    }
    
}
