/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
//
import QB.Qdata ;
import QB.qboard ;
/**
 *
 * @author Jim
 */
public class Qdispatcher extends Task<Integer> 
{
    // 
    public  int totalIterations;
    public  int secsWait; 
    //
    //  Constructor
    public Qdispatcher(int totalIterations)  
    {  
        qboard.dbg.log( "Qdispatcher", "Qinit instantiated");
    }
    //
    //  Call (called at '.start'.
    @Override
    protected Integer call() throws Exception 
    { 
        boolean KeepGoing = true ;
        int secsWait = 14 ;
        //
        qboard.dbg.logThreads();
        qboard.dbg.log( "Qdispatcher", "Entering main dispatch loop");
        //  INFINITE LOOP
        while ( KeepGoing )
        {
            try 
            {
                //   *******************************************************************
                //                                             ****  MAIN DISPATHER  ***
                //                                             *************************
                //  Wait for the IR Ctl task to complete
//                qboard.dbg.log( "Qdispatcher", "Waiting on Qcontrol thread ...");
//                synchronized (qboard.IRctl) { qboard.IRctl.wait();( secsWait * 1000 ); }
//                qboard.dbg.log( "Qdispatcher", "Notification (Ctl) received !");
                //
                //
                //
//                qboard.dbg.log( "Qdispatcher", "Waiting 5 secs on Qscreen thread ...");
//                synchronized (qboard.qscreen) { qboard.qscreen.wait( 5000 ); }
//                qboard.dbg.log( "Qdispatcher", "Notification (Scrn) received !");
               
                // *********************************************************************
            }         
            // catch (InterruptedException ex) { Logger.getLogger(qboard.class.getName()).log(Level.SEVERE, null, ex); }  
            catch (IllegalMonitorStateException ex) { Logger.getLogger(qboard.class.getName()).log(Level.SEVERE, null, ex); }
        }            
        //
        return Qdata.ERR_DISPATCHER ;
    } ;
    //
    //  
    @Override protected void succeeded() { super.succeeded(); updateMessage("Qdispatcher - Succeeded !");   }
    @Override protected void cancelled() { super.cancelled(); updateMessage("Qdispatcher - Cancelled !"); }
    @Override protected void failed()    { super.failed();    updateMessage("Qdispatcher - Failed !");   }  
}

