package com.dstevens.exalted;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RealmSociety {

    private static final ActuarialTable MORTALITY = new ActuarialTable();
    
    public int realmYear;
    public List<Person> unExalted = list();
    public List<Dragonblooded> dynasts = list();
    public List<Couple> marriedCouples = list();

    Map<Age, Integer> deathsSinceLastCensus = map();
    int exaltsSinceLastCensus = 0;
    int birthsSinceLastCensus = 0;
    
    private static <E> List<E> list() {
        return new ArrayList<>();
    }
    
    private static <K, V> Map<K, V> map() {
        return new HashMap<>();
    }
    
    public void seedSociety(int realmYear, int youths, int actives, int retired) {
        this.realmYear = realmYear;
        initialExalts(youths, actives, retired);
        marriages();
        initialChildren();
    }
    
    public void anotherYearPasses() {
        realmYear++;
        ageEveryone();
        exalt();
        marriages();
        haveChildren();
        reap();
    }
    
    public void printFullCensus() {
        System.out.println("---");
        System.out.println("Census for RY " + realmYear);
        System.out.println("Exalted:\t\t" + dynasts.size());
        System.out.println("By age");
        System.out.println("\tYouths:\t\t" + dynasts.stream().filter(p -> p.getAge() == Age.YOUTH).count());
        System.out.println("\tActive:\t\t" + dynasts.stream().filter(p -> p.getAge() == Age.ACTIVE).count());
        System.out.println("\tRetired:\t" + dynasts.stream().filter(p -> p.getAge() == Age.RETIRED).count());
        System.out.println("By career");
        System.out.println("\tBureaucrats:\t" + dynasts.stream().filter(p -> p.getCareer() == Career.BUREAUCRAT).count());
        System.out.println("\tSoldiers:\t" + dynasts.stream().filter(p -> p.getCareer() == Career.SOLDIER).count());
        System.out.println("\tSorcerer\t" + dynasts.stream().filter(p -> p.getCareer() == Career.SORCERER).count());
        System.out.println("\tWordly Monks:\t" + dynasts.stream().filter(p -> p.getCareer() == Career.MONK && !p.isCelebate()).count());
        System.out.println("\tImmaculate:\t" + dynasts.stream().filter(p -> p.getCareer() == Career.MONK && p.isCelebate()).count());
        System.out.println("Couples:\t\t" + marriedCouples.size());
        System.out.println("UnExalted:\t\t" + unExalted.size());
        System.out.println("\tNewborns:\t" + unExalted.stream().filter(p -> p.getAge() == Age.FETUS).count());
        System.out.println("\tInfants:\t" + unExalted.stream().filter(p -> p.getAge() == Age.INFANT).count());
        System.out.println("\tChildren:\t" + unExalted.stream().filter(p -> p.getAge() == Age.CHILD).count());
        
        System.out.println("\nExalts since last census: " + exaltsSinceLastCensus);
        System.out.println("Births since last census: " + birthsSinceLastCensus);
        System.out.println("Deaths since last census: " + totalDeaths());
        System.out.println("\t" + Age.INFANT + ":\t\t" + deathsSinceLastCensus.getOrDefault(Age.INFANT, 0));
        System.out.println("\t" + Age.CHILD + ":\t\t" + deathsSinceLastCensus.getOrDefault(Age.CHILD, 0));
        System.out.println("\t" + Age.YOUTH + ":\t\t" + deathsSinceLastCensus.getOrDefault(Age.YOUTH, 0));
        System.out.println("\t" + Age.ACTIVE + ":\t\t" + deathsSinceLastCensus.getOrDefault(Age.ACTIVE, 0));
        System.out.println("\t" + Age.RETIRED + ":\t" + deathsSinceLastCensus.getOrDefault(Age.RETIRED, 0));
        
        exaltsSinceLastCensus = 0;
        birthsSinceLastCensus = 0;
        deathsSinceLastCensus = map();
    }
    
    public void printAbbreviatedCensus() {
        System.out.println("---");
        System.out.println("Census for RY " + realmYear);
        System.out.println("Exalted:\t\t" + dynasts.size());
        System.out.println("UnExalted:\t\t" + unExalted.size());
    }
    
    private int totalDeaths() {
        return deathsSinceLastCensus.values().stream().collect(Collectors.summingInt(i -> i));
    }

    private void ageEveryone() {
        unExalted.forEach(p -> p.growOlder());
        dynasts.forEach(d -> d.growOlder());
        marriedCouples.forEach(c -> c.happyAniversary());
    }

    private void exalt() {
        List<Dragonblooded> findNewExalts = findNewExalts();
        exaltsSinceLastCensus += findNewExalts.size();
        dynasts.addAll(findNewExalts);
    }
    
    private List<Dragonblooded> findNewExalts() {
        return unExalted.stream().
          filter(p -> p.theBloodRunsTrue()).
          map(p -> exalt(p)).
          collect(Collectors.toList());
    }
    
    private void haveChildren() {
        for (Couple couple : marriedCouples) {
            if(couple.wantChild()) {
                unExalted.add(couple.haveChild());
                birthsSinceLastCensus++;
            }
        }
    }
    
    private void reap() {
        List<Person> deadPeople = unExalted.stream().filter(p -> MORTALITY.kill(p)).collect(Collectors.toList());
        addDeathsFor(Age.INFANT, deadPeople);
        addDeathsFor(Age.CHILD, deadPeople);
        unExalted.removeAll(deadPeople);
        List<Dragonblooded> deadDynasts = dynasts.stream().
                filter(p -> MORTALITY.kill(p)).
                collect(Collectors.toList());

        deadDynasts.forEach(p -> marriedCouples.removeIf(c -> c.contains(p)));
        addDeathsFor(Age.YOUTH, deadDynasts);
        addDeathsFor(Age.ACTIVE, deadDynasts);
        addDeathsFor(Age.RETIRED, deadDynasts);
        addDeathsFor(Age.DEAD, Age.RETIRED, deadDynasts);
        dynasts.removeAll(deadDynasts);
    }
    
    private void addDeathsFor(Age age, List<? extends Person> deadPeople) {
        int oldCount = deathsSinceLastCensus.getOrDefault(age, 0);
        deathsSinceLastCensus.put(age, (int) (oldCount + deadPeople.stream().filter(p -> p.getAge() == age).count()));
    }
    
    private void addDeathsFor(Age age, Age ageToSaveAs, List<? extends Person> deadPeople) {
        int oldCount = deathsSinceLastCensus.getOrDefault(ageToSaveAs, 0);
        deathsSinceLastCensus.put(ageToSaveAs, (int) (oldCount + deadPeople.stream().filter(p -> p.getAge() == age).count()));
    }

    private void initialExalts(int youths, int actives, int retired) {
        for(int i=0;i<youths;i++) {
            dynasts.add(exalt(Age.YOUTH));
        }
        for(int i=0;i<actives;i++) {
            dynasts.add(exalt(Age.ACTIVE));
        }
        for(int i=0;i<retired;i++) {
            dynasts.add(exalt(Age.RETIRED));
        }
    }

    private Dragonblooded exalt(Age age) {
        Career career = Career.pickCareer();
        return new Dragonblooded(age.range(), career, career.isCelebate());
    }
    
    private Dragonblooded exalt(Person p) {
        Career career = Career.pickCareer();
        return new Dragonblooded(p.getYears(), career, career.isCelebate());
    }


    private int marriages() {
        List<Dragonblooded> bachelors = bachelors();
        for(int i=0;i<bachelors.size()-1;i+=2) {
            Couple couple = new Couple(bachelors.get(i), bachelors.get(i+1));
            marriedCouples.add(couple);
        }
        return bachelors.size() / 2;
    }

    private List<Dragonblooded> bachelors() {
        Predicate<? super Dragonblooded> married = (db -> marriedCouples.stream().anyMatch(c -> c.contains(db)));
        Predicate<? super Dragonblooded> unmarried = married.negate();
        return dynasts.stream().
                       filter(unmarried).
                       filter((db -> db.isEligableForMarriage())).
                       sorted().
                       collect(Collectors.toList());
    }
    
    private void initialChildren() {
        for (Couple couple : marriedCouples) {
            int yearsMarried = couple.youngestSpouse().getYears() - Age.YOUTH.maximumYears();
            List<Person> children = list();
            for(int i=0;i<yearsMarried;i++) {
                couple.happyAniversary();
                for (Person child : children) {
                    child.growOlder();
                }
                if(couple.wantChild()) {
                    children.add(couple.haveChild());
                }
            }
            children.removeIf(c -> c.getAge().ordinal() >= Age.YOUTH.ordinal());
            unExalted.addAll(children);
        }
    }
}
