/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travellingsalesman;

import java.util.ArrayList;
import java.util.Random;
import static travellingsalesman.GA.crossover;

/**
 *
 * @author apple
 */
public class MPGA{
    
    private static double mutationRate = 0.015;   //How often numbers change randomly
    private static final int tournamentSize = 10; //Pool from which to see who is good for breeding
    private static final boolean elitism = true;  //Whether or not to clone from the previous generation
    private static final double migrationRate = 0.05; //The chance of a tour moving from one population to another
    public static String neighborhoodStyle = "FullCross"; //Either Full Cross or Ring
    
    /*
    Full Cross is a 2d rectangle starting from the top left corner in rows
    Ring goes clockwise as the borders of a 2d rectangle
    */
    
    public static ArrayList<Population> evolvePopulation(ArrayList<Population> pops, int length, int width){
        //Make new Population
        ArrayList<Population> newPopulations;
        
        //Check if the dimensions match the neighborhood style
        if (neighborhoodStyle.equals("FullCross") && pops.size() != length * width){
            return null;
        }
        else{
            newPopulations = new ArrayList<>(length * width);
        }
        if (neighborhoodStyle.equals("Ring") && pops.size() != length * width - (length - 2) * (width - 2)){
            return null;
        }
        else{
            newPopulations = new ArrayList<>(length * width - (length - 2) * (width - 2));
        }        
        //Add Elite Organisms
        if (elitism){
            for (int i = 0; i < newPopulations.size(); i++){
                newPopulations.get(i).saveTour(0, pops.get(i).getFittest());
            }
        }

        int area = 0;
        
        //Migrate (Max one per population)
        if (neighborhoodStyle.equals("FullCross")){
            area = length * width;
            //Constants for neighborhood point styles
            int topRightCorner = area/width - 1;
            int bottomLeftCorner = (area/length) * width - 1;
            int bottomRightCorner = pops.size() - 1 ;
            int offset = 0; //Array distance from migration target
            
            for (int i = 0; i < pops.size(); i++){
                
                //Classification of types of point on the 2d map of the population        
                if (i == 0){ //Population in the top left corner 
                    if (Math.random() >= 0.5){ //Migrate to population on the right
                        offset = 1;
                    }
                    else{ //Migrate to population under
                        offset = length;
                    }
                }
                else if (i == topRightCorner ){ //Population in the top right corner
                    if (Math.random() >= 0.5){ //Migrate to population on the left
                        offset = -1;
                    }
                    else{ //Migrate to population under
                        offset = length;
                    }
                }
                else if (i == bottomLeftCorner){ //Population in the bottom left corner
                    if (Math.random() >= 0.5){ //Migrate to population on the right
                        offset = 1;
                    }
                    else{ //Migrate to population above
                        offset = -length;
                    }
                }
                else if (i == bottomRightCorner){ //Population in the bottom right corner
                    if (Math.random() >= 0.5){ //Migrate to population on the left
                        offset = -1;
                    }
                    else{ //Migrate to population above
                        offset = -length;
                    }
                }
                else if (i > 0 && i < topRightCorner){ //Populations on the top edge
                    Random random = new Random();
                    int randomInt = random.nextInt(3);
                    switch(randomInt){
                        case(0)://Migrate right
                            offset = 1;
                            break;
                        case(1)://Migrate left
                            offset = -1;
                            break;
                        case(2)://Migrate down
                            offset = length;
                            break;
                    }
                }
                else if (i % length == 0){ //Populations on the left edge
                    Random random = new Random();
                    int randomInt = random.nextInt(3);
                    switch(randomInt){
                        case(0)://Migrate right
                            offset = 1;
                            break;
                        case(1)://Migrate up
                            offset = -length;
                            break;
                        case(2)://Migrate down
                            offset = length;
                            break;
                    }
                }
                else if ((i + 1)/length == (int)(i + 1)/length && area % (i + 1)/length == 0){ //Populations on the right edge
                    Random random = new Random();
                    int randomInt = random.nextInt(3);
                    switch(randomInt){
                        case(0)://Migrate left
                            offset = -1;
                            break;
                        case(1)://Migrate up
                            offset = -length;
                            break;
                        case(2)://Migrate down
                            offset = length;
                            break;
                    }
                }
                else if (i > bottomLeftCorner && i < bottomRightCorner){ //Populations on the bottom edge
                    Random random = new Random();
                    int randomInt = random.nextInt(3);
                    switch(randomInt){
                        case(0)://Migrate right
                            offset = 1;
                            break;
                        case(1)://Migrate left
                            offset = -1;
                            break;
                        case(2)://Migrate up
                            offset = -length;
                            break;
                    }
                }
                else{ //Populations in the middle
                    Random random = new Random();
                    int randomInt = random.nextInt(4);
                    switch(randomInt){
                        case(0)://Migrate right
                            offset = 1;
                            break;
                        case(1)://Migrate left
                            offset = -1;
                            break;
                        case(2)://Migrate up
                            offset = -length;
                            break;
                        case(3)://Migrate down
                            offset = length;
                            break;
                    }
                }
                
                for (int j = 0; j < pops.get(i).populationSize(); j++){
                        //Check whether to migrate
                        if (Math.random() > migrationRate){
                            continue;
                        }
                        
                        newPopulations.get(i + offset).saveTour(1, pops.get(i).getTour(j));
                        
                }
                
            }
        }
        else if (neighborhoodStyle.equals("Ring")) {
            area = pops.size();
            //Constants for neighborhood point styles
            int topRightCorner = length - 1;
            int bottomLeftCorner = 2 * (length - 1) + width;
            int bottomRightCorner = length + width - 1 ;
            int offset = 0; //Array distance from migration target
            
            for (int i = 0; i < pops.size(); i++){
                if(i == 0){ //Population in the top left corner
                    if (Math.random() >= 0.5){ //Migrate to population on the right
                        offset = 1;
                    }
                    else{ //Migrate to population below
                        offset = pops.size() - 1;
                    }
                }
                else if (i == topRightCorner){ //Population in the top right corner
                    if (Math.random() >= 0.5){ //Migrate to population on the left
                        offset = -1;
                    }
                    else{ //Migrate to population below
                        offset = 1;
                    }
                }
                else if (i == bottomRightCorner){ //Population in the bottom right corner
                    if (Math.random() >= 0.5){ //Migrate to population above
                        offset = -1;
                    }
                    else{ //Migrate to population left
                        offset = 1;
                    }
                }
                else if (i == bottomLeftCorner){ //Population in the bottom left corner
                    if (Math.random() >= 0.5){ //Migrate to population on the right
                        offset = -1;
                    }
                    else{ //Migrate to population above
                        offset = 1;
                    }
                }
                else if (i < topRightCorner){ //Populations on the top edge
                    if (Math.random() >= 0.5){ //Migrate to population on the right
                        offset = 1;
                    }
                    else{ //Migrate to population on the left
                        offset = -1;
                    }
                }
                else if (i > topRightCorner && i < bottomRightCorner){ //Populations on the right edge
                    if (Math.random() >= 0.5){ //Migrate to population above
                        offset = -1;
                    }
                    else{ //Migrate to population below
                        offset = 1;
                    }
                }
                else if (i > bottomRightCorner && i < bottomLeftCorner){ //Populations on the bottom edge
                    if (Math.random() >= 0.5){ //Migrate to population on the right
                        offset = -1;
                    }
                    else{ //Migrate to population left
                        offset = 1;
                    }
                }
                else{ //Populations on the left edge
                    if (Math.random() >= 0.5){ //Migrate to population below
                        offset = -1;
                    }
                    else{ //Migrate to population above
                        if (i == pops.size() - 1){
                            offset = -pops.size();
                        }
                        else{
                            offset = 1;
                        }
                    }
                }
                
                //Migrate to new population based on offset
                for (int j = 0; j < pops.get(i).populationSize(); j++){
                        //Check whether to migrate
                        if (Math.random() > migrationRate){
                            continue;
                        }
                        
                        newPopulations.get(i + offset).saveTour(1, pops.get(i).getTour(j));
                        
                }
                
            }
        }
        
        //Crossover
        for (int i = 0; i < pops.size(); i++){
            for (int j = 2; j < pops.get(i).populationSize(); j++) {
                // Select parents
                Tour parent1 = tournamentSelection(pops.get(i));
                Tour parent2 = tournamentSelection(pops.get(i));
                // Crossover parents
                Tour child = crossover(parent1, parent2);
                // Add child to new population
                newPopulations.get(i).saveTour(j, child);
            }   
        }
        
        //Mutate
        for (int i = 0; i < pops.size(); i++){
            for (int j = 1; j < newPopulations.get(i).populationSize(); j++){
                mutate(newPopulations.get(i).getTour(j));
            }
        }
        
        return null;
    }
        
    //Select who is worthy to breed
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
    
}
