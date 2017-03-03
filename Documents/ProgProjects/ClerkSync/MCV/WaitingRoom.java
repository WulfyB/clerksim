import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;
import java.util.LinkedList;


public class WaitingRoom
{
   private int maxChairs; //maximum chairs
   private Semaphore waitingCustomers = new Semaphore(0); //maintains chair access for Customers
   private final ReentrantLock rLock = new ReentrantLock(); //instnace of lock
   private LinkedList<Customer> cList = new LinkedList<Customer>(); //list of customers
   private volatile boolean clerkIdle = true; //checks if clerkIdle
   
   /*
   * The following is a waiting room constructor
   *
   */
   
   public WaitingRoom(int chair)
   {
      maxChairs = chair;
   }
      /*
   * checks if a chair can be occupied by a customer.
   *
   */

   public boolean occupyChair(Customer temp)
   {
      rLock.lock();
      if(waitingCustomers.availablePermits() < maxChairs) //If there's an empty chair
      {
         cList.add(temp);
         System.out.println ("time=" + Napper.getTime() +", Customer " + temp.getValue() + " is in waiting room, waiting= " + cList.size());    
         waitingCustomers.release();
         rLock.unlock();
         return true;
      }
      else
      {
         rLock.unlock();
         return false;
      }
   }
  /*
   * gets the next customer and returns it if it exists.
   *
   */

   public Customer getNextCustomer()throws InterruptedException
   {
      waitingCustomers.acquire();
      rLock.lock();
      Customer temp = cList.removeFirst();
      rLock.unlock();
      return temp;
   }
   
   /*
   * Returns the number of current customers in waiting room.
   */
   public int getNumCust()
   {
      return cList.size();
   }
}