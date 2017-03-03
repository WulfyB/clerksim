import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;

public class Customer implements Runnable
{
   private int value;  //stores customer number
   private Random rand; //instance of Random for RNG
   private int mServ; // maximum service time
   private int mArrive; //maximum arrival time
   private WaitingRoom waitingRoom; //instnace of WaitingRoom
   private ArrayList<Clerk> clerks = new ArrayList<Clerk>(); //Iteratable list of the clerks
   private Clerk clerk; //istance of clerk
   private Semaphore waitService = new Semaphore(0); //semaphore to control available seating in edge cases
   /*
   * Constructor of Customer.
   *
   */
   public Customer (int val, Random rand, int mServ, int mArrive, WaitingRoom waitingRoom, ArrayList<Clerk> clerks)
   {
      this.value = val;
      this.rand = rand;
      this.mServ = mServ;
      this.mArrive = mArrive;
      this.waitingRoom = waitingRoom;
      this.clerks = clerks;
      Thread thread = new Thread(this);
      thread.setDaemon(true);
      thread.start();
   
   
   }
   /*
   * Sets that a clerk is with the customer
   */
   public void withClerk(Clerk c)
   {
      clerk = c;
      waitService.release();
   }
   /*
   * Executes the function of the customer threads and prints out changes of its instances while navigating through
   * the loop infinitely until the main method ends all threads.
   */
   public void run()
   {
      while(true){
         int arrival = rand.nextInt(mArrive * 1000);
         System.out.println ("time=" + Napper.getTime() +", Customer " + value + " scheduled to arrived at " + arrival);
         Napper.nap(arrival);
         boolean requested = false;
         System.out.println ("time=" + Napper.getTime() +", Customer " + value + " is arriving.");
         for (Clerk clerk : clerks)
         {
            if (clerk.requestService(this)) 
            {
               System.out.println ("time=" + Napper.getTime() + ", Customer " + value + "is meeting with clerk " + clerk.getValue()); 
               requested = true;
               break;
            }
         }
         if (!requested)
            if(!waitingRoom.occupyChair(this))
            {
               System.out.println ("time=" + Napper.getTime() + ", Customer " + value + " is leaving, no open chairs."); 
            
               continue;
               
            }
           
      
         waitService.acquireUninterruptibly();
         int serv = rand.nextInt(mServ * 1000);
         System.out.println("time=" + Napper.getTime() + ", Customer " + value + " is being served for " + serv + " ms");
         Napper.nap(serv);
         System.out.println("time=" + Napper.getTime() + ", Customer " + value + " finished getting serviced.");
         clerk.notifyServiced();
      }
   }
   public int getValue()
   {
      return value;
   }
}