/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;
//
import QB.Qdata ;
import QB.IRcontrol ;
import QB.BTcontrol ;
import QB.Qscreen;
import QB.dbScorer;
import QB.debug;
import QB.Qcommand ;
//
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jim
 */
public class qboard extends Application 
{  
    //
    static public Qdata         qd   ; 
    static public debug         dbg  ;  
    //
    static public Stage         Qstage ;
    //
    //static public Qdispatcher   qmain   ;
    static public IRcontrol     IRctl  ;
    static public BTcontrol     BTctl  ;
    static public Qscreen       qscreen   ;
    static public Qcommand      qcmd ;
    static public dbScorer      Scorer ;
    //
    //static Thread               thMain    ;
    static Thread               thScorer  ;
    static Thread               thScreen  ;
    // static Thread               thIRControl ;
    //
    @Override
    public void start(Stage stage)
    {
        this.Qstage = stage ;
        dbg         = new debug();
        qd          = new Qdata();
        qcmd        = new Qcommand();
        BTctl       = new BTcontrol(0) ;
        IRctl       = new IRcontrol(0) ;
        Scorer      = new dbScorer(0) ;
        qd.Qstage   = this.Qstage ;
        //
        Qinit() ;
        //
        qboard.dbg.log("Qinit", "Start");
     }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        int i = 0 ;
        //
        launch(args); 
       //
    }    
    //
    // @SuppressWarnings("empty-statement") ;
    //
    private static void Qinit( ) 
    {  
        try 
        {
            // Create the TWO tasks
            //qmain    = new Qdispatcher(0) ;
            //IRctl    = new IRcontrol(0) ;
            qscreen  = new Qscreen(qd.GameType) ;
            // qcmd     = new Qcommand();
            // Scorer     = new dbScorer(0);
            //
            //thMain    = new Thread((Runnable) qmain);
            //thIRControl = new Thread((Runnable) IRctl);
            thScreen  = new Thread((Runnable) qscreen);

            //thMain.setName("QB Main");
            //thMain.setDaemon(true);
            //thMain.start();
            // Fire up the tasks in their threads.
            //
            //
            thScreen.setName("QB Screen");
            thScreen.setDaemon(true);
            thScreen.start();
            //
            //Scorer  = new dbScorer( 0 ) ;
            thScorer  = new Thread((Runnable) Scorer);
            thScorer.setName("QB Scorer");
            thScorer.setDaemon(true);
            thScorer.start();
            //
            //thIRControl.setName("QB IR Controller");
            //thIRControl.setDaemon(true);
            //thIRControl.start(); 
            //
            //  Log environment variables (info / debug only)
            //
//            Map<String, String> env = System.getenv();    
//            dbg.log("QS Ctlr", "----------------<  ENV variables  >-----------------");
//            for (String envName : env.keySet()) 
//            {   dbg.log( "QS Ctlr", envName + " = " + env.get(envName));  }
//            dbg.log("QS Ctlr", "----------------------------------------------------");
            //                     
            qboard.dbg.log("Qinit", "EXIT");
            //
        } catch (IOException ex) { Logger.getLogger(qboard.class.getName()).log(Level.SEVERE, null, ex); }
    }
}

