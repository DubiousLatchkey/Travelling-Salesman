/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travellingsalesman;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author apple
 */
public class Tour {
    
    private ArrayList<City> tour = new ArrayList();
    public double fitness = 0;
    private int distance = 0;
    
    public Tour(ArrayList tour){
        this.tour = tour;
    }
    
    public Tour(){
        for (int i = 0; i < TourManager.numberOfCities(); i++) {
            tour.add(null);
        }
    }
    
    public void generateIndividual(){
        for(int i = 0; i < TourManager.numberOfCities(); i++){
            setCity(i, TourManager.getCity(i));
        }
        Collections.shuffle(tour);
    }
    
    // Gets a city from the tour
    public City getCity(int tourPosition) {
        return (City)tour.get(tourPosition);
    }

    // Sets a city in a certain position within a tour
    public void setCity(int tourPosition, City city) {
        tour.set(tourPosition, city);
        //tour[tourPosition] = city;
        // If the tours been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }
    
    // Gets the tours fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
        }
        return fitness;
    }
    
    // Gets the total distance of the tour
    public int getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex=0; cityIndex < tourSize(); cityIndex++) {
                // Get city we're travelling from
                City fromCity = getCity(cityIndex);
                // City we're travelling to
                City destinationCity;
                // Check we're not on our tour's last city, if we are set our 
                // tour's final destination city to our starting city
                if(cityIndex+1 < tourSize()){
                    destinationCity = getCity(cityIndex+1);
                }
                else{
                    destinationCity = getCity(0);
                }
                // Get the distance between the two cities
                tourDistance += fromCity.distanceFrom(destinationCity);
            }
            distance = tourDistance;
        }
        return distance;
    }

    // Get number of cities on our tour
    public int tourSize() {
        return tour.size();
    }
    
    // Check if the tour contains a city
    public boolean containsCity(City city){
        return tour.contains(city);
    }
    
    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < tourSize(); i++) {
            geneString += getCity(i)+"|";
        }
        return geneString;
    }
    
}
