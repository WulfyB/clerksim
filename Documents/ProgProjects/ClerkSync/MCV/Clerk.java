import java.lang.Thread;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Clerk implements Runnable
{

   private Thread clerkThread; //instance of clerkThread
   private WaitingRoom waitingRoom; //instance of WaitingRoom
   private final ReentrantLock interruptedLock = new ReentrantLock(); //lock for clerk class
   private volatile Customer requestingCustomer; //the changing customer instance, notes of possible change while in use
   private Semaphore servicingCustomer = new Semaphore(0);  //Semaphore to handle edge cases of two cust arriving at idle clerk
   private int value; //stores clerk number. Unused in single version.
   private volatile boolean servicing = false; //determines if serviced (clerk availability)
  
  
  /*
  * Constructor of Clerk.
  *
  */
   public Clerk(WaitingRoom waitingRoom, int val)
   {
      clerkThread = new Thread(this);
      this.waitingRoom = waitingRoom;
      this.value = val;
      clerkThread.setDaemon(true);
      clerkThread.start();
   }
/*
* This method returns the next available customer in a fifo manner. 
*
*/
   
   public Customer getNextCustomer()
   {
      try
      {
         return waitingRoom.getNextCustomer();
      }
      catch (InterruptedException e)
      {
         Customer c = this.requestingCustomer;
         this.requestingCustomer = null;
         return c;
      }
   }
   /*
    Checks to see if clerk is available to service a customer. If so, it locks
     out other attempts immediately before servicing the customer. If false, the  
     customers will go sit down and the lock is unlocked.
   */
   public boolean requestService(Customer c)
   {
      if (servicing || clerkThread.getState() == Thread.State.RUNNABLE)
         return false;
   
      interruptedLock.lock();
      try
      {
         if (clerkThread.isInterrupted())
         {
            return false;
         }
      
         requestingCustomer = c;
         clerkThread.interrupt();
      
         return true;
      }
      finally
      {
         interruptedLock.unlock();
      }
   }
   
   /*
   * Executes the function of the clerk thread and prints out changes of its instances while navigating through
   * the loop infinitely until the main method ends all threads.
   */
   
   public void run()
   {
      while (true)
      {
         Customer c = getNextCustomer();
         System.out.println("time=" + Napper.getTime() + ", Clerk "  + value  
            + " has customer, waiting=" + waitingRoom.getNumCust());
         System.out.println("time=" + Napper.getTime() + ", Clerk "  + value  
            + " is servicing.");
         c.withClerk(this); 
         servicing = true;
         servicingCustomer.acquireUninterruptibly();
         servicing = false;
         System.out.println("time=" + Napper.getTime() + ", Clerk "  + value  
            + " free, waiting on customer");
         
      }
   
   }
   /*
   * Releases servicingCustomer sephamore
   * 
   */
   public void notifyServiced()
   {
      servicingCustomer.release();
   }
   /* 
   * This method returns the value associated with an instance of Clerk as to track which clerk is dong what function.
   */
   public int getValue()
   {
      return value;
   }
}