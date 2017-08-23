/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travellingsalesman;

import java.util.ArrayList;

public class TourManager {

    // Holds our cities
    private static ArrayList destinationCities = new ArrayList<City>();

    // Adds a destination city
    public static void addCity(City city) {
        destinationCities.add(city);
    }
    
    // Get a city
    public static City getCity(int index){
        return (City)destinationCities.get(index);
    }
    
    public static void clearCities(){
        destinationCities.clear();
    }
    
    // Get the number of destination cities
    public static int numberOfCities(){
        return destinationCities.size();
    }
}