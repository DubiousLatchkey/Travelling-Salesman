/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travellingsalesman;

import java.util.Random;

/**
 *
 * @author apple
 */

public class GA {
    
    private static double mutationRate = 0.015;   //How often numbers change randomly
    private static final int tournamentSize = 10; //Pool from which to see who is good for breeding
    private static final boolean elitism = true;  //Whether or not to clone from the previous generation
    private static final int crossoverSwitchovers = 2;  //How many splices of parents' DNA gets passed on
    private static double temperature = 10000;
    private static final double coolingRate = 0.01;
    private static final double crowdingConstant = 0.001;
    private static final double insertionChance = 0.2;
    
    public static Tour insertionTour;
    
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.populationSize(), false);

        // Keep our best individual if elitism is enabled
        int elitismOffset = 0;
        if (elitism) {
            newPopulation.saveTour(0, pop.getFittest());
            elitismOffset = 1;
        }

        // Crossover population
        // Loop over the new population's size and create individuals from
        // Current population
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            // Select parents
            Tour parent1 = tournamentSelection(pop);
            Tour parent2 = tournamentSelection(pop);
            // Crossover parents
            Tour child = crossover(parent1, parent2);
            // Add child to new population
            newPopulation.saveTour(i, child);
        }

        // Mutate the new population a bit to add some new genetic material
        for (int i = elitismOffset; i < newPopulation.populationSize(); i++) {
            mutate(newPopulation.getTour(i));
        }

        return newPopulation;
    }
    
    public static Population simulateAnnealing(Population pop){
    
        Tour currentSolution;
        
        for(int i = 0; i < pop.populationSize(); i++){
            currentSolution = pop.getTour(i);
            
            // Create new neighbour tour
            Tour newSolution = currentSolution;

            // Get a random positions in the tour
            int tourPos1 = (int) (newSolution.tourSize() * Math.random());
            int tourPos2 = (int) (newSolution.tourSize() * Math.random());
            
            //System.out.println(tourPos1 + " " +tourPos2);
            
            // Get the cities at selected positions in the tour
            City citySwap1 = newSolution.getCity(tourPos1);
            City citySwap2 = newSolution.getCity(tourPos2);

            // Swap them
            newSolution.setCity(tourPos2, citySwap1);
            newSolution.setCity(tourPos1, citySwap2);
            
            if(newSolution == currentSolution){
                System.out.println("WHYYY");
            }
            //System.out.println(newSolution.getCity(tourPos1).xCoord + "\n" +currentSolution.getCity(tourPos1).xCoord);
            
            // Get "energy" (fitness) of solutions
            int currentEnergy = currentSolution.getDistance();
            int neighbourEnergy = newSolution.getDistance();
            
            //Decide if we should use the new neighbor
            //System.out.println(acceptanceProbability(currentEnergy, neighbourEnergy));
            System.out.println(currentEnergy-neighbourEnergy);
            if (acceptanceProbability(currentEnergy, neighbourEnergy) > Math.random()) {
                pop.saveTour(i, newSolution);
            }
            
        }
        temperature *= 1-coolingRate;
        
        return pop;
    }
    
    public static Population niche(Population pop){
        
        int crowdingPenalty = 0;
        
        for (int i = 0; i < pop.populationSize(); i++){
            //Loop through other tours
            for (int j = 0; j < pop.populationSize(); j++){
                
                if(i == j){continue;} //Doesn't get a penalty from self
                
                for (int k = 0; k < TourManager.numberOfCities(); k++){
                    if (pop.getTour(i).getCity(k) == pop.getTour(j).getCity(k)){ //If the city is in the same order
                        crowdingPenalty++;
                    }
                }
                
            }
            
            //Apply penalty
            pop.getTour(i).fitness -= pop.getTour(i).fitness * (crowdingPenalty * crowdingConstant);
                
            crowdingPenalty = 0;
            
        }
        
        return pop;
    }
    
    public static Population insert(Population pop){
        
        //Determine if insertion will happen
        if(Math.random() > insertionChance){
            return pop;
        }
        
        //Displace random tour with inserted one
        Random random = new Random();
        int randomInt = random.nextInt(pop.populationSize());
        pop.saveTour(randomInt, insertionTour);
        
        //Save fittest in generation to be inserted later
        insertionTour = pop.getFittest();
        
        return pop;
    }
    
    
    
    //Find the probability of accepting a neighbor city based on their fitness/energy (SA)
    public static double acceptanceProbability(int energy, int newEnergy) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        System.out.println(Math.exp((energy - newEnergy) / temperature));
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }
    
    public static Tour crossover(Tour parent1, Tour parent2){
        Tour child = new Tour();
        // Get start and end sub tour positions for parent1's tour
        int startPos = (int) (Math.random() * parent1.tourSize());
        int endPos = (int) (Math.random() * parent1.tourSize());

        // Loop and add the sub tour from parent1 to our child
        for (int i = 0; i < child.tourSize(); i++) {
            // If our start position is less than the end position
            if (startPos < endPos && i > startPos && i < endPos) {
                child.setCity(i, parent1.getCity(i));
            } // If our start position is larger
            else if (startPos > endPos) {
                if (!(i < startPos && i > endPos)) {
                    child.setCity(i, parent1.getCity(i));
                }
            }
        }

        // Loop through parent2's city tour
        for (int i = 0; i < parent2.tourSize(); i++) {
            // If child doesn't have the city add it
            if (!child.containsCity(parent2.getCity(i))) {
                // Loop to find a spare position in the child's tour
                for (int ii = 0; ii < child.tourSize(); ii++) {
                    // Spare position found, add city
                    if (child.getCity(ii) == null) {
                        child.setCity(ii, parent2.getCity(i));
                        break;
                    }
                }
            }
        }
        return child;
    }
    
    // Mutate a tour using swap mutation
    private static void mutate(Tour tour) {
        // Loop through tour cities
        for(int tourPos1=0; tourPos1 < tour.tourSize(); tourPos1++){
            // Apply mutation rate
            if(Math.random() < mutationRate){
                // Get a second random position in the tour
                int tourPos2 = (int) (tour.tourSize() * Math.random());

                // Get the cities at target position in tour
                City city1 = tour.getCity(tourPos1);
                City city2 = tour.getCity(tourPos2);

                // Swap them around
                tour.setCity(tourPos2, city1);
                tour.setCity(tourPos1, city2);
            }
        }
    }
    
    private static Tour tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.populationSize());
            tournament.saveTour(i, pop.getTour(randomId));
        }
        // Get the fittest tour
        Tour fittest = tournament.getFittest();
        return fittest;
    }
    
}
