import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

/*
* The following is the multi clerk form of the Banking simulation scenario.
* This project is homework 6 for Dr. Yilmaz Principles of Programing Languages
* Code by Wulfy Boothe clb0026
* 1 November 2016
* Sources: StackOverflow, various questons on finer details
* Java API for use of monitor and various methods
* Many articles on the differences and uses of Monitor vs Sephamore such as programcreek
*/

public class Simulation
{

/*
* Main method, assumed String[] args can be used for a command line argument.
* Time values are in thousands of ms.
*/
   public static void main(String[] args) 
   {
      int numChairs = 3;  //default number of waiting room chairs
      int numCustomers = 6; // default number of customers
      int serviceTime = 1; //default max service time
      int interarrivalTime = 3; //default arrival time
      int runTime = 5;  //default run time of simulation
      int numClerks = 1; //default number of clerks
      String temp; //string temp holds temporary values
      Random random = new Random(); //instance of random

      //the for loop checks for alterations to default values based on String[] args
      for (int i = 0; i < args.length; i++)
      {
         temp = args[i].substring(1, 1);
         if (temp == "w")
         {
            temp = args[i].substring(2);
            numChairs = Integer.parseInt(temp);
         }
         else if (temp == "C")
         {
            temp = args[i].substring(2);
            numCustomers = Integer.parseInt(temp);
         }
         else if (temp == "s")
         {
            temp = args[i].substring(2);
            serviceTime = Integer.parseInt(temp);
         }
         else if (temp == "i")
         {
            temp = args[i].substring(2);
            interarrivalTime = Integer.parseInt(temp);
         }
         else if (temp == "R")
         {
            temp = args[i].substring(2);
            runTime = Integer.parseInt(temp);
         }
         else if (temp == "c")
         {
            temp = args[i].substring(2);
            numClerks = Integer.parseInt(temp);
         }
         else 
         {
            System.out.println(temp + " is an invalid section of the command line.");
         }
      }
      WaitingRoom waitingRoom = new WaitingRoom(numChairs);  //waiting room instance
      ArrayList<Clerk> clerks = new ArrayList<Clerk>(); //list of clerks
      for(int i = 0; i < numClerks; i++)
      {
      Clerk clerk = new Clerk(waitingRoom, i); //adds i instances of clerks
      }
      for(int i = 0; i < numCustomers; i++)
      {
         Customer customer = new Customer(i, random, serviceTime, interarrivalTime, waitingRoom, clerks); 
         //adds i instnace of customers
      
      }
   
      Napper.nap(runTime * 1000); //runtime in ms
   }
}