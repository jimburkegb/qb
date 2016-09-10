/*
 *  Code for remote control INFRARED device
 *  =======================================
 */
package QB;
//
import QB.Qdata ;
import QB.qboard ;
import QB.QB_Qscreen_Controller;
import static QB.qboard.qcmd;
import static QB.qboard.qd;
// import static QB.qboard.qirc;
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
//import javafx.concurrent.Task;
//import javafx.util.Duration;
/**
 *
 */
public class IRcontrol extends Task<Integer> 
// public class IRcontrol 
{
    int status = 0 ;
    String LastKey = "" ;
    String qCmd = "" ;
    HashMap<String, String> cmdMap = new HashMap<String, String>() ;
    // 
    public  int totalIterations;
    public  int secsWait;
    //
    //  Constructor
    public IRcontrol(int totalIterations) 
    {  
        this.totalIterations = totalIterations;
        this.init(); 
    }
    //
    public void init() 
    {
        this.secsWait = 12 ;
        this.status = 0 ;
        this.LastKey = "" ;
        //  
        //  Initialise the hash map for converting IR commands to BT commands.
        //
        //          value       Qcmd                    Key Label   Key Char    Action
        //          -----       ----                    ---------   --------    -------
        cmdMap.put("KEY_1",     "NUMBER_1") ;       //  (red)                   Score 1 pt
        cmdMap.put("KEY_2",     "NUMBER_2") ;       //  (yellow)                ..... 2 pts
        cmdMap.put("KEY_3",     "NUMBER_3") ;       //  (green)      
        cmdMap.put("KEY_4",     "NUMBER_4") ;       //  (brown)         
        cmdMap.put("KEY_5",     "NUMBER_5") ;       //  (blue)          
        cmdMap.put("KEY_6",     "NUMBER_6") ;       //  (pink)       
        cmdMap.put("KEY_7",     "NUMBER_7") ;       //  (black)                 ..... 7 pts   
        cmdMap.put("KEY_f",     "FOUL") ;           //  "FOUL"                  Foul
        cmdMap.put("KEY_w",     "GAME") ;           //  "GAME"                  Game / Match end
        cmdMap.put("KEY_u",     "CLOCK") ;          //  "CLEAR"                 Start / stop Clock
        cmdMap.put("KEY_m",     "MENU") ;           //  "ESC"                   Delete last entry - ALSO MENU KEY !!!!
        cmdMap.put("KEY_p",     "NEXTPLAYER") ;     //  "ENTER"                 End of break - switch to other player
//        cmdMap.put("1",     "NUMBER_1") ;       //  (red)                   Score 1 pt
//        cmdMap.put("2",     "NUMBER_2") ;       //  (yellow)                ..... 2 pts
//        cmdMap.put("3",     "NUMBER_3") ;       //  (green)      
//        cmdMap.put("4",     "NUMBER_4") ;       //  (brown)         
//        cmdMap.put("5",     "NUMBER_5") ;       //  (blue)          
//        cmdMap.put("6",     "NUMBER_6") ;       //  (pink)       
//        cmdMap.put("7",     "NUMBER_7") ;       //  (black)                 ..... 7 pts   
//        cmdMap.put("f",     "FOUL") ;           //  "FOUL"                  Foul
//        cmdMap.put("w",     "GAME") ;           //  "GAME"                  Game / Match end
//        cmdMap.put("u",     "CLOCK") ;          //  "CLEAR"                 Start / stop Clock
//        cmdMap.put("m",     "MENU") ;           //  "ESC"                   Delete last entry - ALSO MENU KEY !!!!
//        cmdMap.put("p",     "NEXTPLAYER") ;     //  "ENTER"                 End of break - switch to other player
    }  

    // 
    // #####################################################################################################
    //                                                                       #####    EXEC Command     #####
    //                                                                       ###############################
    public int exec( String ir_key ) 
    {
        int status = 0 ;
        qCmd = "" ;  //  MUST zero this !
        //
        qCmd = cmdMap.get(ir_key) ;     //  Convert IR cmd to QB cmd.
        if ((qCmd != null) && (qCmd.length() > 0))
        {           
            qboard.dbg.log( "exec_command: "+ir_key, " (Q cmd "+qCmd+")"); 
            //
            //  0 = undefined, 1 = Billiards (timed), 2 = Billiards (frames), 10 = Snooker, 20 = Pool (9-ball), ...
            switch (qd.GameType)
            {  
                case Qdata.GAMETYPE_BILLIARDS_TIMED:    status = qcmd.exec_billiards_timed( qCmd ) ;  break ;
                case Qdata.GAMETYPE_BILLIARDS_FRAMES:   status = qcmd.exec_billiards_frames( qCmd ) ; break ;
                case Qdata.GAMETYPE_SNOOKER:            status = qcmd.exec_snooker( qCmd );           break ;
                case Qdata.GAMETYPE_POOL_9BALL:         status = qcmd.exec_pool_9ball( qCmd );        break ;
                case Qdata.GAMETYPE_POOL_ENGLISH:       status = qcmd.exec_pool_english( qCmd );      break ;        
                case Qdata.GAMETYPE_CAROM:              status = qcmd.exec_pool_carom( qCmd );        break ;        
                default:  qboard.dbg.log( "IRcommand", "Invalid GameType (" + qd.GameType + ") <--- ERROR  ");       
            }
            //
            if (status == 1)  this.LastKey = qCmd ; else this.LastKey = "" ;   //  Take note of last GOOD command
        }
       //
        return status ;
    }  
    // 
    // #####################################################################################################
    //                                                                       #####    EXEC SPECIAL     #####
    //                                                                       ###############################
    //  This class handles 'meta key' functions. These are non-game things such as
    //  REBOOT, SHUTDOWN, INIT, Swap spot / plain, etc.
    //  They should also be non-game specific. ie. these functions apply to ALL games, ALL game types.
    //
    public int exec_special( String ir_key ) 
    {    
        int status = 0 ;
        //
        qCmd = cmdMap.get(ir_key) ;    //  Convert IR cmd to QB cmd.
        qboard.dbg.log( "exec_special: "+ir_key, " (Q cmd "+qCmd+")"); 
        //
        if (qCmd == null)  return qd.ERR_NULL_COMMAND ;
        //
        switch ( qCmd )
        {
            case "NUMBER_1":    // *****************  -1-  ZERO Scores  *************************
                                //  ZERO scores
                                Platform.runLater(() -> { qd.MsgBox.show("Scores ZEROED", 2); } ); 
                                Platform.runLater(() -> { QB_Qscreen_Controller.initScores(); } );                                 
                                qd.MetakeyTimer.stop();         //  Stop if already running.
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ;  
                                // 
            case "NUMBER_2":    // *****************  -2-  ??? ***********************************
                                //  Flip Advanced Scoring indicator
                                if (qd.AdvancedScoring ==1)  
                                {
                                    Platform.runLater(() -> { qd.MsgBox.show("Advanced Scoring now OFF", 10); } );                               
                                    qd.AdvancedScoring = 0 ;
                                } else 
                                {
                                    Platform.runLater(() -> { qd.MsgBox.show("Advanced Scoring now ON", 10); } );                               
                                    qd.AdvancedScoring = 1 ;
                                }
                                qd.MetakeyTimer.stop();         //  Stop if already running.
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ; 
                                //
            case "NUMBER_3":    // *****************  -3-  Lock/Unlock Scoreboard   ********************************
                                //
                                if (qd.GameOver)
                                {   Platform.runLater(() -> { qd.MsgBox.show("Scoreboard unlocked", 3); } ); } 
                                else
                                {   Platform.runLater(() -> { qd.MsgBox.show("Scoreboard LOCKED", 3); } );                                                                   }
                                qd.GameOver = !qd.GameOver ;    //  FLIP the GameOver boolean 
                                qd.ClockTimer.stop();           //  Stop and hide the main timer
                                qd.ClockTimer_setVisibility.update(false); ; 
                                qd.MetakeyTimer.stop();         //  Stop and hide menu timr & pane
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ;   
                                //
            case "NUMBER_4":    // *****************  -4-  Show network info  ***********************************
                                //
                                Platform.runLater(() -> { qd.MsgBox.show(qd.piNAME + ";" + qd.piIP + ";" + qd.piMACADDR + ";" + qd.piSSID+ ";" + qd.piNI, 10); } );                               
                                qd.MetakeyTimer.stop();         //  Stop if already running.
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ;  
                                //
            case "NUMBER_5":    // *****************  -5-  SHUTDOWN  ***********************************
                                //
                                qboard.dbg.log( "exec_command", "SHUTOWN");
                                Platform.runLater(() -> { qd.MsgBox.show("!!!! SHUTTING DOWN !!!!", 5);} );   
                                try 
                                { 
                                    Process pRemote = Runtime.getRuntime().exec("sudo shutdown -H now ");
                                } catch (IOException ex) { Logger.getLogger(BTcontrol.class.getName()).log(Level.SEVERE, null, ex); }
                                break ;  
                                //
            case "NUMBER_6":    // *****************  -6-  REBOOT  ***********************************
                                //
                                qboard.dbg.log( "exec_command", "REBOOT");
                                Platform.runLater(() -> { qd.MsgBox.show("!!!! REBOOTING !!!!", 5);} );  
                                try 
                                { 
                                    Runtime.getRuntime().exec("sudo reboot ");
                                } catch (IOException ex) { Logger.getLogger(BTcontrol.class.getName()).log(Level.SEVERE, null, ex); }
                                break ;  
                                //
            case "NUMBER_7":    // *****************  -7-  Exit application  ***********************************
                                //
                                Platform.runLater(() -> { qd.MsgBox.show("EXIT APPLICATION", 2); } ); 
                                qboard.dbg.log( "App EXIT !", "App EXIT !");
                                System.exit( 1 );
                                break ;     //  
                                //
            case "CLOCK":       // *****************  -CLOCK- Begin / End Match  *************************
                                //  RELOAD match. This could load the NEXT match in the queue for this table.
                                //  Do we need to shift the Break textbox ?  This is a bug and this is the easiest way to synchronise
                                //  the break textbox position with the player-at-table.
                                if (qd.PlayerAtTable == 2) { QB_Qscreen_Controller.EndBreak(qd.PlayerAtTable, false) ;  }
                                ////
                                status = qd.init_config() ;         //  Init all data, including reload from db.
                                QB_Qscreen_Controller.initScores(); //  Init the screen data.
                                Platform.runLater(() -> { qd.MsgBox.show("Match LOADED", 2); } ); 
                                qd.MetakeyTimer.stop();             //  Stop metaKey timer if already running.
                                //
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ;     //  
                                //
            case "GAME":        // *****************  -GAME- reload match from scratch  *************************
                                //  Start / stop match
                                //  If there is no score so far, then interpret this as a START MATCH. Otherwise it's a END MATCH.
                                QB_Qscreen_Controller.BeginEndMatch();
                                qd.MetakeyTimer.stop();         //  Stop if already running.
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ;    
                                //
            case "FOUL":        // ****************  -CORRECTION-  **********************
                                //  A correction can only occur in a running match.
                                if (qd.MatchInProgress)
                                {
                                    QB_Qscreen_Controller.DeleteLastScore(); 
                                    qd.MetakeyTimer.stop();         //  Stop if already running.
                                    qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                }
                                break ;
                                //
            case "NEXTPLAYER":  // ****************  -SWAP PLAYERS-  Swap Plain and Spot players  **********************
                                //  
                                qboard.dbg.log( "exec_command", "Swap Plain and Spot round");
                                Platform.runLater(() -> { qd.MsgBox.show("SWAPPING PLAIN AND SPOT", 2); } ); 
                                Platform.runLater(() -> { QB_Qscreen_Controller.SwapPlainAndSpot();} );            //  Briefly show the points  
                                qd.MetakeyTimer.stop();         //  Stop if already running.
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ;  
                                //
            case "MENU":        // *****************  EXIT MENU MODE  *************************
                                //  Stopping the timer will set it to 'NOT running'.
                                qd.MetakeyTimer.stop();         //  Stop if already running.
                                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);     //  Hide menu pane
                                break ;  
                                //
            default:
                qboard.dbg.log( "exec_command", "ir_key = " + ir_key + " <--- ERROR - IR KEY NOT RECOGNISED ");       
        }           
        //
        return status ;
    }  
    //
    // ----------------------------------------------------------------------------------------------------------------------------------
    //  These methods are no longer required - 18-Jan-16.
    //  The reason is that this module is no longer run as a thread. It is now a list of methods,
    //  same as for Bluetooth control.
    //  Get a 'magic' command from IR remote control
    //  ----------------------------------------------
    //
    private String GetCommand( BufferedReader bri )
    {
        String cmd = "" ;
        int i ;
        //
        try 
        {
            qboard.dbg.log( "GetCommand", "Waiting for command number ..." );
            String tmp  ;
            tmp = bri.readLine() ;  //  FLUSH that last read
            //
            for (i=0; i < 5; i++)  //  5 attempts at 1 per 0.2 secs.
            {
                while ( bri.ready() )
                {
                    tmp = bri.readLine() ;
                    if (tmp != null)
                    {
                        qboard.dbg.log( "GetCommand", "---- Command line received = " + tmp); 
                        cmd = ParseIRline( tmp ) ;
                        qboard.dbg.log( "GetCommand", "---- Command to process = " + cmd);
                        return cmd ; 
                    } 
                }
                Thread.sleep(200L) ;  //  SLEEP for a wee bit
                i++ ;
            }
        } 
        catch (IOException ex)          { qboard.dbg.log( "GetCommand", "IO Exception = "  + ex.getMessage()); }
        catch (InterruptedException ex) { qboard.dbg.log( "GetCommand", "INT Exception = " + ex.getMessage()); }
        //
        return cmd ;    
    }
    //
    //  Parse the line sent from the IR remote control
    //  ----------------------------------------------
    //  eg. Press key 7 renders -   00000000c13e906f 00 KEY_7 jim.conf
    //
    private String ParseIRline( String line )
    {
        int i = 0 ;
        String key = "" ;
        StringTokenizer st ;
        String key_index = " 00 " ;  //  Only accept the first press of this key
        //
        //  First check for - MAGIC MODE -  hold down 7 key so that 7 (seven) keypresses are recorded
        //
        //
        //  Locate key index ' 00 ' and delete all before it on the line. If not there then we're finished.
        i = line.indexOf( key_index ) ;
        if (i > 0)  {  line = line.substring(i, line.length()) ;  }   else { return "" ; }  
        // 
        // Parse what's left on the line.
        // 
        st =  new StringTokenizer( line, key_index);
        key = st.nextToken().trim() ;     
        qboard.dbg.log( "parseline (IR key) = ", key); 
        //  
        return key ;
    }
    //
    //  Call (called ONCE at '.start'.
    @Override
    protected Integer call() throws Exception 
    {
        int i = 0, stat = 0 ;
        String ir_cmd = "" ;
        String tmp ;
        String irw_line;
        //
        // QB_Qscreen_Controller
        //
        //  If we're running on WINDOWS then we don't need IR control - use test buttons instead.
        if (qd.OperatingSystem.equals("LINUX"))
        {      
            qboard.dbg.log( "IRctl", "Entering main control loop");
            //
            //  If IR is NOT the chosen remote control type then EXIT
            // this.w.wait(1000) ; 
            if (qd.RemoteControlType != Qdata.RC_INFRARED) {  return -2 ;  }
            //
            //  Set up the irw process to listen for remote key presses.
            Process pRemote = Runtime.getRuntime().exec("irw");
            BufferedReader bri = new BufferedReader (new InputStreamReader(pRemote.getInputStream()));
            //
            while ( i == 0 )
            {
                try 
                {   //   *******************************************************************
                    //                                            ****  MAIN DISPATCHER  ***
                    //                                            **************************
                    //  This loop acts on IR keypresses ONLY
                    //  NB: readLine() is blocking, therefore if IR is nor used then it should never proceed past the call.
                    //
                    try 
                    {
                        while ((irw_line = bri.readLine()) != null) 
                        { 
                            tmp = "" ;
                            ir_cmd = ParseIRline( irw_line ) ;
                            // if nothing is returned then ignore the command.
                            if (ir_cmd.length() > 0)
                            {                              
                                qboard.dbg.log( "IR - ", irw_line + "; cmd = " + ir_cmd); 
                                //
                                //  If it's the ESC key (ie. ERROR) then wait to see if it's a COMMAND.
                                //  If another key is pressed within ONE SECOND then it's a 'special' command.
                                if ( ir_cmd.equalsIgnoreCase("KEY_PROG3") )    
                                {
                                    tmp = GetCommand( bri) ;
                                    qboard.dbg.log( "IR cmd = ",tmp ); 
                                    if (tmp != "")  ir_cmd = tmp ;
                                }
                                //
                                if (tmp.length() == 0)
                                {   //     *******************
                                    stat = exec( ir_cmd ) ; 
                                    //     *******************
                                } else
                                {   //     ***************************
                                    stat = exec_special( ir_cmd ) ;
                                }   //     ***************************
                            }
                         } 
                    }
                    catch (Exception err) { qboard.dbg.log( "IRctl", "Excsption = " + err.getMessage() ) ; }
                    // *********************************************************************
                } 
                // catch (InterruptedException ex) { Logger.getLogger(qboard.class.getName()).log(Level.SEVERE, null, ex); }
                catch (IllegalMonitorStateException ex) { qboard.dbg.log( "IRctl", "IMS Excsption = " + ex.getMessage() ); }
            }
        } else
        {
            qboard.dbg.log( "IRctl", "Not LINUX therefore thread NOT NEEDED - exiting now");            
        }
        return qd.ERR_IR_DISPATCHER;
    } ;
    // ----------------------------------------------------------------------------------------------------------------------------------
    //

}
