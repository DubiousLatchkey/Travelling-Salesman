/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travellingsalesman;

/**
 *
 * @author apple
 */

public class City {
    
    int xCoord;
    int yCoord;
    
    //Randomly generates a city
    public City(){
        xCoord = (int)(Math.random() * 750 + 20);
        yCoord = (int)(Math.random() * 550 + 50);
        System.out.println(xCoord + " , " +yCoord);
    }
    
    //Generates a city at given coordinates
    public City(int xCoord, int yCoord){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }
    
    public int getX(){
        return xCoord;
    }
    
    public int getY(){
        return yCoord;
    }
    
    //Finds the distance from this city from another
    public double distanceFrom(City city){
        int xDifference = Math.abs(xCoord - city.getX());
        int yDifference = Math.abs(yCoord - city.getY());
        double distance = Math.sqrt(xDifference * xDifference + yDifference * yDifference);
        
        return distance;
        
    }
    
}
