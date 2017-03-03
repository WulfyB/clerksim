import java.util.ArrayList;
public class Napper
{
   static volatile int count = 0;  //count initiliazed to zero
/*
* The following attempts to iterate count at the "nap pace."
*
*/
   static
   {
      Thread thread = 
         new Thread(
         new Runnable()
         {
            public void run()   
            {
               try{
                  while (true)
                  {
                     Thread.sleep(10);
                     count += 10;
                  }
               }
               catch (InterruptedException e)
               {
               }
            }
         });
      thread.setDaemon(true);
      thread.start();
   };
   /*
   * Checks start time and finds if count exceeds ms. Allows other functions to track time
   * relative to start of program.
   */
   public static void nap(int ms)
   {
      int startTime = count;
      try{
         while (count - startTime < ms)
         {
            Thread.sleep(10);
         }
      }        
      catch (InterruptedException e)
      {
      }
   
   }
   
   /*
   * Returns relative time (to start of simulation) as determined by the earlier method.
   *
   */
   public static int getTime()
   {
      return count;
   }
}
