/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travellingsalesman;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
/**
 *
 * @author apple
 */
public class TravellingSalesman extends Frame{

    static ArrayList<City> cities = new ArrayList();
    static Population solvedPop;
    public static final int generations = 100;
    public static final int populationSize = 75;
    public static final int cityCount = 20;
    public static int solutionID = 0;

    public static void main(String[] args) {
        //new TravellingSalesman();
        Scanner scanner = new Scanner(System.in);
        FileWriter out;
        FileReader in;
        BufferedReader reader; 
        int choice;
        boolean quit = false;
        
        while(!quit){
        
            System.out.println("Make new country (1) \nDraw an individual country (2) \nDetermine a path (3)\nDetermine a path for countries in a continent (4)\nDraw a country in a continent (5)\nGenerate a new continent (6)\nGenerate a path for a continent (7)\nStats Breakdown (8)\nQuit (9)");
            choice = scanner.nextInt();
            String countryName;

            switch (choice){
                case 1:
                    //Create a new country file
                    System.out.println("What is the name of your new country?");
                    countryName = scanner.next();
                    try{
                        //Randomly generates a country with cityCount cities
                        cities.clear();
                        out = new FileWriter(countryName + ".txt");
                        for (int i = 0; i < cityCount; i++){
                            cities.add(new City());
                            out.write(cities.get(i).getX() + ",");
                            out.write(cities.get(i).getY() + " ");
                        }

                        out.close();
                    }
                    catch(Exception e){
                        System.out.println("Error");
                    }
                    break;
                case 2:
                    //Access an existing country
                    System.out.println("What is the name of the country you wish to access?");
                    countryName = scanner.next();
                    try{
                        //Reads from a country file
                        in = new FileReader(countryName + ".txt");
                        reader = new BufferedReader(in);
                        String country = reader.readLine();
                        String currentNum = "";
                        int tempX = 0;
                        int tempY = 0;
                        System.out.println(country);
                        
                        cities.clear();
                        
                        //Converts raw file data to cities
                        for (int i = 0; i < country.length(); i++){
                            if(country.charAt(i) == ',') {
                                tempX = Integer.valueOf(currentNum);
                                currentNum = "";
                            } 
                            else if(country.charAt(i) == ' '){
                                tempY = Integer.valueOf(currentNum);
                                currentNum = "";
                                cities.add(new City(tempX, tempY));
                            }
                            else{
                                currentNum += country.charAt(i);
                            }
                        }

                        in.close();
                    }
                    catch(Exception e){
                        System.out.println("Error");
                    }
                    break;
                case 3:
                    //Make a path
                    System.out.println("Which country do you want to make a path for?");
                    countryName = scanner.next();
                    System.out.println("What solution do you want?\nBasic GA (0)\nSimulated Annealing GA (1)\nNiching GA (2)\nMulti-population GA (3)\nInjection GA (4)");
                    solutionID = scanner.nextInt();
                    try{
                        //Reads from a country file
                        in = new FileReader(countryName + ".txt");
                        reader = new BufferedReader(in);
                        String country = reader.readLine();
                        String currentNum = "";
                        int tempX = 0;
                        int tempY = 0;
                        System.out.println(country);

                        //Converts raw file data to cities
                        for (int i = 0; i < country.length(); i++){
                            if(country.charAt(i) == ',') {
                                tempX = Integer.valueOf(currentNum);
                                currentNum = "";
                            } 
                            else if(country.charAt(i) == ' '){
                                tempY = Integer.valueOf(currentNum);
                                currentNum = "";
                                TourManager.addCity(new City(tempX, tempY));
                            }
                            else{
                                currentNum += country.charAt(i);
                            }
                        }

                        in.close();
                    }
                    catch(Exception e){
                        System.out.println("Error");
                    }

                    //Birth
                    Population pop = new Population(populationSize, true);
                    generatePath(pop, generations, countryName, false, -1, "");

                    try{
                        //Writes the solution to a new file
                        out = new FileWriter(countryName + "Solved.txt");
                        for (int i = 0; i < cityCount; i++){
                            out.write(cities.get(i).getX() + ",");
                            out.write(cities.get(i).getY() + " ");
                        }

                        out.close();
                    }
                    catch(Exception e){
                        System.out.println("Error");
                    }

                    break;
                case(4):
                    //Makes a set of countries and solves them
                    System.out.println("What do you want the name of the continent to be?");
                    countryName = scanner.next();
                    System.out.println("How many countries do you want in the continent?");
                    int numberOfCountries = scanner.nextInt();
                    
                    File newDirectory = new File(countryName);
                    newDirectory.mkdir();
                    
                    newDirectory = new File(countryName+"\\Fitness");
                    newDirectory.mkdir();
                    
                    newDirectory = new File(countryName+"\\Solutions");
                    newDirectory.mkdir();
                    
                    for (int j = 0; j < numberOfCountries; j++){
                        try{
                            TourManager.clearCities();
                            for (int k = 0; k < cityCount; k++){
                                TourManager.addCity(new City());
                            }
                            
                            pop = new Population(populationSize, true);
                            generatePath(pop, generations, countryName+j, true, j, "");
                            
                            out = new FileWriter(new File(countryName+"\\Solutions", countryName + "Solved" +j +".txt"));
                            for (int i = 0; i < cityCount; i++){
                                
                                out.write(solvedPop.getFittest().getCity(i).getX() + ",");
                                out.write(solvedPop.getFittest().getCity(i).getY() + " ");
                            }

                            out.close();
                        }
                        catch(Exception e){
                            System.out.println("Error");
                        }
                    }
                    
                    break;
                case(5):
                    System.out.println("What is the name of the continent you wish to access?");
                    countryName = scanner.next();
                    System.out.println("What is the country's number?");
                    String countryNum = scanner.next();
                    
                    try{
                        //Reads from a country file
                        in = new FileReader(new File(countryName +"\\Solutions\\", countryName + "Solved" +countryNum +".txt"));
                        reader = new BufferedReader(in);
                        String country = reader.readLine();
                        String currentNum = "";
                        int tempX = 0;
                        int tempY = 0;
                        System.out.println(country);
                        
                        cities.clear();
                        
                        //Converts raw file data to cities
                        for (int i = 0; i < country.length(); i++){
                            if(country.charAt(i) == ',') {
                                tempX = Integer.valueOf(currentNum);
                                currentNum = "";
                            } 
                            else if(country.charAt(i) == ' '){
                                tempY = Integer.valueOf(currentNum);
                                currentNum = "";
                                cities.add(new City(tempX, tempY));
                            }
                            else{
                                currentNum += country.charAt(i);
                            }
                        }

                        in.close();
                    }
                    catch(Exception e){
                        System.out.println("Error");
                    }
                    break;
                case(6):
                    System.out.println("What do you want the name of the continent to be?");
                    countryName = scanner.next();
                    System.out.println("How many countries do you want in the continent?");
                    numberOfCountries = scanner.nextInt();
                    
                    newDirectory = new File(countryName);
                    newDirectory.mkdir();
                    
                    newDirectory = new File(countryName+"\\Initial");
                    newDirectory.mkdir();
                    
                    for (int j = 0; j < numberOfCountries; j++){
                        try{
                            
                            System.out.println("Generating Country");
                            out = new FileWriter(new File(countryName+"\\Initial\\" +countryName +j +".txt"));
                            System.out.println("Folder File Made");
                            cities.clear();
                            
                            for (int i = 0; i < cityCount; i++){
                                cities.add(new City());
                                out.write(cities.get(i).getX() + ",");
                                out.write(cities.get(i).getY() + " ");
                            }
                            
                            System.out.println("Country Generated");

                            out.close();
                        }
                        catch(Exception e){
                            System.out.println("Error");
                        }
                    }
                    
                    break;
                case(7):
                    System.out.println("What continent would you like to solve?");
                    countryName = scanner.next();
                    System.out.println("What is this solution's identifier");
                    String identifier = scanner.next();
                    //numberOfCountries = scanner.nextInt();
                    
                    System.out.println("What solution do you want?\nBasic GA (0)\nSimulated Annealing GA (1)\nNiching GA (2)\nMulti-population GA (3)\nInjection GA (4)");
                    solutionID = scanner.nextInt();
                    
                    newDirectory = new File(countryName+"\\Initial");
                    numberOfCountries = newDirectory.listFiles().length;
                    
                    newDirectory = new File(countryName+"\\" +identifier +"Fitness");
                    newDirectory.mkdir();
                    
                    newDirectory = new File(countryName+"\\" +identifier +"Solutions");
                    newDirectory.mkdir();
                    
                    for (int j = 0; j < numberOfCountries; j++){
                        try{
                            TourManager.clearCities();
                                
                            //Reads from a country file
                            //System.out.println(countryName +"\\Initial\\" +countryName +Integer.toString(j) +".txt");
                            in = new FileReader(countryName +"\\Initial\\" +countryName +Integer.toString(j) +".txt");
                            reader = new BufferedReader(in);
                            String country = reader.readLine();
                            String currentNum = "";
                            int tempX = 0;
                            int tempY = 0;
                            System.out.println(country);
                            cities.clear();

                            //Converts raw file data to cities
                            for (int l = 0; l < country.length(); l++){
                                if(country.charAt(l) == ',') {
                                    tempX = Integer.valueOf(currentNum);
                                    currentNum = "";
                                } 
                                else if(country.charAt(l) == ' '){
                                    tempY = Integer.valueOf(currentNum);
                                    currentNum = "";
                                    TourManager.addCity(new City(tempX, tempY));
                                    cities.add(new City(tempX, tempY));
                                }
                                else{
                                    currentNum += country.charAt(l);
                                }
                            }

                            in.close();
                                
                                //TourManager.addCity(new City());
                           
                            
                            pop = new Population(populationSize, true);
                            generatePath(pop, generations, countryName+j, true, j, identifier);
                            
                            out = new FileWriter(new File(countryName+"\\" +identifier +"Solutions", countryName + "Solved" +j +".txt"));
                            for (int i = 0; i < cityCount; i++){
                                
                                out.write(solvedPop.getFittest().getCity(i).getX() + ",");
                                out.write(solvedPop.getFittest().getCity(i).getY() + " ");
                            }

                            out.close();
                        }
                        catch(Exception e){
                            System.out.println("Error");
                        }
                    }
                    break;
                case(8): //Stats breakdown
                    System.out.println("What continent would you like to break down the stats of?");
                    countryName = scanner.next();
                    
                    System.out.println("What is the solution's identifier?");
                    String statsIdentifier = scanner.next();
                    String breakdownDirectory = countryName+"\\"+statsIdentifier+"Fitness";
                    int numberOfSolutions = new File(breakdownDirectory).listFiles().length;
                    //System.out.println(numberOfSolutions);
                    int averageFinalFitness = 0;
                    
                    for(int i = 0; i < numberOfSolutions; i++){
                        try{
                            in = new FileReader(breakdownDirectory + "\\" +countryName + Integer.toString(i) + "Fitness.txt");
                            reader = new BufferedReader(in);
                            
                            String sCurrentLine;
                            String lastLine = "";

                            while ((sCurrentLine = reader.readLine()) != null){
                                lastLine = sCurrentLine;
                            }
                            
                            averageFinalFitness += Integer.parseInt(lastLine);
                            
                        } catch(Exception e){
                            System.out.println("Can't find files");
                        }
                    }
                    
                    averageFinalFitness /= numberOfSolutions;
                    System.out.println("Average Final Fitness: "+Integer.toString(averageFinalFitness));
                    
                    break;
                case(9):
                    quit = true;
                    break;
            }
            
            if (!quit){
                new TravellingSalesman();
            }
            
        }
        
        System.out.println("Quitting...");
    }
    
    public static void generatePath(Population pop, int generations, String countryName, boolean isContinent, int count, String identifier){
        System.out.println("Initial distance: " + pop.getFittest().getDistance());
        pop = GA.evolvePopulation(pop);
        
        FileWriter out;
        try{
            if(!isContinent){
                out = new FileWriter(countryName +"Fitness.txt");
            }
            else{
                String baseName = countryName.substring(0, countryName.length() - Integer.toString(count).length());
                //System.out.println(countryName + " " +baseName);
                out = new FileWriter(new File(baseName +"\\" +identifier +"Fitness", countryName +"Fitness.txt"));
                        }

            for (int i = 0; i < generations; i++) {
                pop = GA.evolvePopulation(pop);
                if(solutionID == 1){ //Simulated Annealing
                    pop = GA.simulateAnnealing(pop);
                }
                else if(solutionID == 2){ //Niche
                    pop = GA.niche(pop);
                }
                else if(solutionID == 4){ //Insertion
                    if (i == 0){ GA.insertionTour = pop.getFittest(); }
                    pop = GA.insert(pop);
                }
                out.write(pop.getFittest().getDistance()+ "\r\n");
            }
            
            out.close();
            
        }catch(IOException e){
            System.out.println("Error creating fitness data file");
        }

        System.out.println("Finished");
        System.out.println("Final distance: " + pop.getFittest().getDistance());
        System.out.println("Solution:");
        System.out.println(pop.getFittest());

        solvedPop = pop;
        
        cities.clear();
        
        for (int i = 0; i < pop.getFittest().tourSize(); i++){
            cities.add(pop.getFittest().getCity(i));
        }
    }
    
    public TravellingSalesman(){
        super("Travelling Salesman Problem");
        super.setSize(800, 600);
        super.setVisible(true);

        
        addWindowListener(new WindowAdapter()
       {@Override
       public void windowClosing(WindowEvent e)
          {dispose();}
       }
        );
        
    }
    
    @Override
    public void paint(Graphics g){
        
        drawAxes(g);
                
        for (int i = 0; i < cities.size(); i++){
            drawCity(cities.get(i), g);
        }
        
        //Draws all the paths in order according to their index
        for (int i = 0; i < cities.size() - 1; i++){
            drawPath(cities.get(i), cities.get(i + 1), g);
        }
        
    }
    
    //Draws any one city
    public void drawCity(City city, Graphics g){
        g.fillOval(city.getX() - 5, city.getY() - 5, 10, 10);
    }
    
    //Draws a line that connects any two cites
    public void drawPath(City city1, City city2, Graphics g){
        g.drawLine(city1.getX(), city1.getY(), city2.getX(), city2.getY());
    }
    
    //Draws the x and y axes for the map of the country
    public void drawAxes(Graphics g){
        g.drawLine(20, 50, 750, 50);
        g.drawLine(20, 50, 20, 550);
    }
    
}
