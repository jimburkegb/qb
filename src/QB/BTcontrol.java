/*
 *  Code for remote control BLUETOOTH device
 *  ========================================
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
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
/**
 *
 * @author Jim
 */
public class BTcontrol
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
    BTcontrol(int totalIterations) 
    {  
        this.totalIterations = totalIterations;
        this.init(); 
    }
    //
    private void init() 
    {
        this.secsWait = 12 ;
        this.status = 0 ;
        this.LastKey = "" ;
        //
        //  Initialise the hash map for converting Bluetooth commands to QB commands.
        //
        //          value    Qcmd                  Key Label   Action
        //          -----    ----                  ---------   ------
        cmdMap.put( "1",     "NUMBER_1") ;     //  (red)       Score 1 pt
        cmdMap.put( "2",     "NUMBER_2") ;     //  (yellow)    ..... 2 pts
        cmdMap.put( "3",     "NUMBER_3") ;     //  (green)
        cmdMap.put( "4",     "NUMBER_4") ;     //  (brown)
        cmdMap.put( "5",     "NUMBER_5") ;     //  (blue)
        cmdMap.put( "6",     "NUMBER_6") ;     //  (pink)
        cmdMap.put( "7",     "NUMBER_7") ;     //  (black)     ..... 7 pts  
        cmdMap.put( "f",     "FOUL") ;         //  "F"         Foul
        cmdMap.put( "w",     "GAME") ;         //  "FR"        Game / Match end
        cmdMap.put( "u",     "CLOCK") ;        //  "U"         Start / stop Clock
        cmdMap.put( "m",     "MENU") ;         //  "M          Delete last entry - ALSO MENU KEY !!!!
        cmdMap.put( "p",     "NEXTPLAYER") ;   //  "P"         End of break - switch to other player
    }  
    //
    // 
    // #####################################################################################################
    //                                                                       #####    EXEC Command     #####
    //                                                                       ###############################
    public int exec( String bt_key ) 
    {
        int status = 0 ;
        qCmd = "" ;  //  MUST zero this !
        //
        qCmd = cmdMap.get(bt_key) ;     //  Convert IR cmd to QB cmd. 
        if ((qCmd != null) && (qCmd.length() > 0))
        {           
            qboard.dbg.log( "exec_command: "+bt_key, " (Q cmd "+qCmd+")"); 
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
    public int exec_special( String bt_key ) 
    {    
        int status = 0 ;
        //
        qCmd = cmdMap.get(bt_key) ;    //  Convert IR cmd to QB cmd.
        qboard.dbg.log( "exec_special: "+bt_key, " (Q cmd "+qCmd+")"); 
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
            case "NUMBER_2":    // *****************  -2-  Advanced Scoring on/off ****************************
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
             case "NUMBER_3":   // *****************  -3-  Lock/Unlock Scoreboard   ********************************
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
                                    Process pRemote = Runtime.getRuntime().exec("sudo shutdown now ");
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
                                if (qd.PlayerAtTable == 2) { QB_Qscreen_Controller.EndBreak(qd.PlayerAtTable, false) ; }
                                ////
                                status = qd.init_config() ;         //  Init all data, including reload from db.
                                QB_Qscreen_Controller.initScores(); //  Init the screen data.
                                Platform.runLater(() -> { qd.MsgBox.show("MATCH RELOADED", 2); } ); 
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
                                // 
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
                qboard.dbg.log( "exec_command", "bt_key = " + bt_key + " <--- ERROR - BT KEY NOT RECOGNISED ");       
        }           
        //
        return status ;
    }  

}
