/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;

import static QB.qboard.dbg;
import static QB.qboard.qd ;
import static QB.qboard.IRctl  ;
import static QB.qboard.BTctl  ;
import QB.SaveScore ;
import java.awt.AWTException;
import javafx.embed.swing.SwingFXUtils;
//
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javax.imageio.ImageIO;
/**
 * FXML Controller class
 *
 * @author Jim
 */
public class QB_Qscreen_Controller implements Initializable 
{
    // static { System.setProperty("java.awt.headless", "false"); }  //  Won't work without X11 server
    //
    // @FXML    private Scene      qbScene ;
    //
    @FXML    private GridPane   gpMenu_BT ;     //  Menu screens for BT and IR.
    @FXML    private GridPane   gpMenu_IR ;
    //
    @FXML    private Label      lbTournament;
    @FXML    private Label      lbMatch;
    @FXML    private Label      lbMatchType;
    @FXML    private Label      lbPause;
    @FXML    private Label      lbTimer;
    @FXML    private Label      lbBreak;
    @FXML    private Label      lbTimeNow;
    @FXML    private Label      lbP1score;
    @FXML    private Label      lbP2score;   
    @FXML    private Label      lbP1name;
    @FXML    private Label      lbP2name;
    @FXML    private Label      lbReferee;
    @FXML    private Label      lbRefereeLabel;
    @FXML    private Label      lbMessage;
    @FXML    private Label      lbP1breaks;
    @FXML    private Label      lbP2breaks;
    @FXML    private ImageView  imP1flagFront;
    @FXML    private ImageView  imP2flagFront;
    @FXML    private ImageView  imP1flagRear;
    @FXML    private ImageView  imP2flagRear;
    @FXML    private ImageView  imRefereeflag;
    @FXML    private ImageView  imPlain;
    @FXML    private ImageView  imSpot;
//
    @FXML    private ImageView  imCountdown_15;
    @FXML    private ImageView  imCountdown_14;
    @FXML    private ImageView  imCountdown_13;
    @FXML    private ImageView  imCountdown_12;
    @FXML    private ImageView  imCountdown_11;
    @FXML    private ImageView  imCountdown_10;
    @FXML    private ImageView  imCountdown_9;
    @FXML    private ImageView  imCountdown_8;
    @FXML    private ImageView  imCountdown_7;
    @FXML    private ImageView  imCountdown_6;
    @FXML    private ImageView  imCountdown_5;
    @FXML    private ImageView  imCountdown_4;
    @FXML    private ImageView  imCountdown_3;
    @FXML    private ImageView  imCountdown_2;
    @FXML    private ImageView  imCountdown_1;
    @FXML    private ImageView  imCountdown_0;
    @FXML    private ImageView  imWifi;
    @FXML    private ImageView  imSponsor1;
    @FXML    private ImageView  imSponsor2;
    @FXML    private ImageView  imLogo;
    //
    @FXML    private TextField  tfKey_BT ; 
    @FXML    private TextField  tfKey_IR ; 
    //
    @FXML    private Label      lbPauseScreen ;
    //
    @FXML    private Label      lbDebugInfoIR ;
    @FXML    private Label      lbDebugInfoBT ;
    // 
    //=============================================================================
    public      String          ScreenLayoutFile = "" ;
    //
    private boolean DebugMode = false ;
    //
    static SaveScore scorer  ;    //  The SCORER class
    //
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {     
        gpMenu_BT.setVisible(false);           //  Hide menu panes for BT and IR.
        gpMenu_IR.setVisible(false);           //  
        //
        lbDebugInfoIR.textProperty().bind(  qd.DebugInfo  );
        lbDebugInfoBT.textProperty().bind(  qd.DebugInfo  );
        //
        //  Bind the DYNAMIC screen values.
        lbP1name.textProperty().bind(       qd.Player1Name    );
        lbP1name.styleProperty().bind(      qd.Player1Name_setStyle    );
        lbP1score.textProperty().bind(      qd.Player1Score   );
        lbP1score.styleProperty().bind(     qd.Player1Score_setStyle   );
        lbP2name.textProperty().bind(       qd.Player2Name    );
        lbP2name.styleProperty().bind(      qd.Player2Name_setStyle    );
        lbP2score.textProperty().bind(      qd.Player2Score   );
        lbP2score.styleProperty().bind(     qd.Player2Score_setStyle   );
        lbReferee.textProperty().bind(      qd.RefereeName    );
        //
        lbP1breaks.textProperty().bind(     qd.Player1Breaks   );
        lbP2breaks.textProperty().bind(     qd.Player2Breaks   );
        //
        //  The BREAK box
        lbBreak.setTranslateX( 0 );                          //  These are for animation
        lbBreak.setTranslateY( 0 );
        lbBreak.textProperty().bind(        qd.PlayerBreak    );
        lbBreak.visibleProperty().bind(     qd.PlayerBreak_setVisibility );
        qd.PlayerBreak_setVisibility.update(true);      //  Break box is always visible
        //lbBreak.translateYProperty().bind(     qd.PlayerBreakYcoord ) ;
        //
        //  Player flags. These may switch depending on the string.
        imP1flagFront.imageProperty().bind(       qd.Player1flag_image );
        imP2flagFront.imageProperty().bind(       qd.Player2flag_image );
        imP1flagRear.imageProperty().bind(        qd.Player2flag_image );  //  Reverse the flags for rear images
        imP2flagRear.imageProperty().bind(        qd.Player1flag_image );
        //
        imP1flagFront.visibleProperty().bind(     qd.Player1flagFront_setVis );
        imP2flagFront.visibleProperty().bind(     qd.Player2flagFront_setVis );
        imP1flagRear.visibleProperty().bind(      qd.Player1flagRear_setVis );
        imP2flagRear.visibleProperty().bind(      qd.Player2flagRear_setVis );
        //
        //  Countdown images
        imCountdown_0.visibleProperty().bind(     qd.Countdown_setVisibility[0] );
        imCountdown_1.visibleProperty().bind(     qd.Countdown_setVisibility[1] );
        imCountdown_2.visibleProperty().bind(     qd.Countdown_setVisibility[2] );
        imCountdown_3.visibleProperty().bind(     qd.Countdown_setVisibility[3] );
        imCountdown_4.visibleProperty().bind(     qd.Countdown_setVisibility[4] );
        imCountdown_5.visibleProperty().bind(     qd.Countdown_setVisibility[5] );
        imCountdown_6.visibleProperty().bind(     qd.Countdown_setVisibility[6] );
        imCountdown_7.visibleProperty().bind(     qd.Countdown_setVisibility[7] );
        imCountdown_8.visibleProperty().bind(     qd.Countdown_setVisibility[8] );
        imCountdown_9.visibleProperty().bind(     qd.Countdown_setVisibility[9] );
        imCountdown_10.visibleProperty().bind(    qd.Countdown_setVisibility[10] );
        imCountdown_11.visibleProperty().bind(    qd.Countdown_setVisibility[11] );
        imCountdown_12.visibleProperty().bind(    qd.Countdown_setVisibility[12] );
        imCountdown_13.visibleProperty().bind(    qd.Countdown_setVisibility[13] );
        imCountdown_14.visibleProperty().bind(    qd.Countdown_setVisibility[14] );
        imCountdown_15.visibleProperty().bind(    qd.Countdown_setVisibility[15] );

        //  Spot & Plain balls
        imSpot.visibleProperty().bind(      qd.Spot_setVisibility );
        imPlain.visibleProperty().bind(     qd.Plain_setVisibility );
        //
        //  Info fields
        lbTournament.textProperty().bind(   qd.TournamentName );
        lbMatch.textProperty().bind(        qd.MatchName      );
        lbMatchType.textProperty().bind(    qd.MatchType      );
        lbTimeNow.textProperty().bind(      qd.TimeNow        );
        //
        //  Message Box bindings
        lbMessage.textProperty().bind(      qd.MsgBox.msgProperty_fmt() );
        lbMessage.visibleProperty().bind(   qd.MsgBox.msgbox_setVis );
        //
        lbPauseScreen.visibleProperty().bind(   qd.PauseScreen_setVisibility );
        qd.PauseScreen_setVisibility.update(false );
        //
        //  Setup animation
        //  - Plain & spot balls
        qd.anim_spot = new ScaleTransition( Duration.millis(500), imSpot  );
        qd.anim_plain = new ScaleTransition( Duration.millis(500), imPlain  );
        //  - Break textbox
        qd.anim_break  = new TranslateTransition( Duration.millis(500), lbBreak )   ;
        //  - Set up the 'timer' object for handling animations.
        setupAnimation( true) ;
        //
        //  If game type 1 (timed billiards) then we need timers
        //  Timers - match timer and pause timer.
        if (qd.GameType == qd.GAMETYPE_BILLIARDS_TIMED)
        {
            setupClockTimer( qd.MatchDuration * 60, false, true )  ;        //  false => count DOWN, false => Don't show secs
            setupPauseTimer( qd.MAX_PAUSE_TIMER, true, true )  ;            //  true => count UP, true =>  show secs
            lbTimer.visibleProperty().bind(  qd.ClockTimer_setVisibility );
            lbPause.visibleProperty().bind(  qd.PauseTimer_setVisibility );
            lbTimer.styleProperty().bind(    qd.ClockTimer_setStyle ) ;
            lbPause.styleProperty().bind(    qd.PauseTimer_setStyle ) ;
        }
        //
        lbRefereeLabel.setVisible(false);    //  Referee display 
        lbReferee.setVisible(false);         // 
        imRefereeflag.setVisible(false);     
        //
        //  If a ref is specified then show it.
        if (qd.RefereeName.get() != "")
        {
            lbReferee.textProperty().bind( qd.RefereeName );
            lbReferee.setVisible( true );  
            lbRefereeLabel.setVisible( true ); 
            imRefereeflag.setVisible(true);
        }
        //
        File diskFile ;                 //  On screen status icons - network and server config. The default  
        if (qd.WANup)               //  for both is FALSE so we don't need to bother with those.
        {                               //  Network UP
            if (qd.UsingServerConfig)   //  Using config downloaded from server
            {
                diskFile = new File( qd.FILEPATH_IMAGES + "wifi_green.jpg");    //  Green icon -> Network and server up & running
            } else                      //                 ----------                             -------------------------------
            {                           //  Using local config file
                diskFile = new File( qd.FILEPATH_IMAGES + "wifi_green_X.jpg");    //  Amber icon -> Network OK but no server config        
            }                           //                 ------------                             -------------------------------
        } else                          //  Network DOWN
        {                               //  Must be using local config therefore.               
            if (qd.UsingServerConfig)   //  Using config downloaded from server
            {
                diskFile = new File( qd.FILEPATH_IMAGES + "wifi_amber.jpg");    //  Amber icon -> No WAN but server access (eg. LAN in use ?)  
            } else                      //                 ----------                             -------------------------------------------
            {                           //  Using local config file
                diskFile = new File( qd.FILEPATH_IMAGES + "wifi_red.jpg");      //  Red icon with red X -> no LAN or WAN        
            }                           //                 --------                                        --------------
        } 
        Image refimage = new Image(diskFile.toURI().toString());       
        imWifi.setImage( refimage ); 
        //
        //  If we have no network then we have no (accurate) time, so HIDE the clock. We rely
        //  on the WANup boolean for this.
        lbTimeNow.setVisible(qd.WANup);
        //
        LoadSponsorImages() ;
        //
        setupMetakeyTimer(  qd.TIMEOUT_MENU_KEY, false, false )  ;        //  true => count UP, true =>  show secs
        setupStandoffTimer( qd.TIMEOUT_STANDOFF, false, false )  ;        //  true => count UP, true =>  show secs
        //
        //  Remote Control setup. 
        switch (qd.RemoteControlType)
        {
            case Qdata.RC_INFRARED :  
                        dbg.log( "Qctl",  "Remote controller is INFRARED. Disabling tfKey_BT callback" ) ;
                        //  The MENU pane for IR control
//                        gpMenu_BT.visibleProperty().bind(qd.MenuPane_setVisibility);
                        gpMenu_IR.visibleProperty().bind(qd.MenuPane_setVisibility);
                        tfKey_BT.setDisable(Boolean.FALSE);
                        // tfKeyPressed_BT.setDisable(Boolean.TRUE);
                        // tfKeyPressed_BT.setEventDispatcher(null);
                        // tfKeyReleased_IR.setDisable(Boolean.FALSE);
                        // tfKeyReleased_IR.setEventDispatcher(null);
                        //
                        // This RunLater is essential to initialise the textfield with focus.
                        // A palaver I know, but this seems the ONLY WAY to bring the app up with focus here.
                        Platform.runLater(new Runnable() { @Override public void run() { tfKey_IR.requestFocus(); } });
                        //
                    break ;
            case Qdata.RC_BLUETOOTH: 
                        dbg.log( "Qctl",  "Remote controller is BLUETOOTH. Disabling tfKey_IR callback" ) ;
                        //  The MENU pane for BT control
                        gpMenu_BT.visibleProperty().bind(qd.MenuPane_setVisibility);
                        tfKey_IR.setDisable(Boolean.TRUE);
                        //
                        Platform.runLater(new Runnable() { @Override public void run() { tfKey_BT.requestFocus(); } });
                        //
                        break ;
            case Qdata.RC_WIRELESS:   
//                        qboard.dbg.log( "Qctl",  "Remote controller is WIRELESS - UNSUPPORTED - switching to BLUETOOTH " ) ;
//                        gpMenu_BT.visibleProperty().bind(qd.MenuPane_setVisibility);
//                        qd.RemoteControlType = Qdata.RC_BLUETOOTH ; 
//                        Platform.runLater(new Runnable() { @Override public void run() { tfKeyPressed_BT.requestFocus(); } });
                        qboard.dbg.log( "Qctl",  "Remote controller is WIRELESS - UNSUPPORTED - switching to INFRARED " ) ;
                        gpMenu_IR.visibleProperty().bind(qd.MenuPane_setVisibility);
                        qd.RemoteControlType = Qdata.RC_INFRARED ; 
                        Platform.runLater(new Runnable() { @Override public void run() { tfKey_IR.requestFocus(); } });
                        break ;
            default:
        }             
        //   
        initScores() ;    //  Initialise scores, AND scoring db connection.
        //
    }   
        // 
    // =================================================================================================

    //  ************************************************************************************************
    //                                                                    ****   BEGIN / END Match   ***
    //                                                                    ******************************
    //  Match END
    static public void BeginEndMatch()
    {
        dbg.log( "QS Ctlr", "GAME pressed");
        //       
        if (qd.MatchInProgress)  { endMatch() ; } else  { beginMatch() ; }               
    }
    //
    //  ************************************************************************************************
    //                                                                         ****   BEGIN  Match   ***
    //                                                                         *************************
    static public void beginMatch()
    {
        int status = 0;
        //
        dbg.log( "QS Ctlr", "BEGIN MATCH pressed");
        //
        qd.GameOver         = false ;               //  Match now started
        qd.MatchInProgress  = true ;                //
        //
        initScores();       //  Ensure scores at zeroed.
        //
        qd.PlayerVisits.update( 1 );                //  'Zero' the visits.
        qd.PlayerBreak_setVisibility.update(true);  //  Show the Break box    
        //
        status = scorer.record( 
                    0,                          //  Current player is player ONE at start
                    0,                          //  Score
                    0,                          //  Break = 0
                    Qdata.SHOTTYPE_BEGINFRAME, 
                    "Begin match", 
                    0 ) ;                       //  Player Visit
        if (status != 1)  { dbg.log( "QS Ctlr", "Begin Match message ERROR" ); } ;
        //
        qd.MsgBox.show("Match Started", 5);         //  Briefly show the message
        //
        qd.MatchStatusScreen = qd.STATUS_STARTED ;                
        //
        //  ONLY IF BILLIARDS !!!
            qd.ClockTimer.start();
            qd.ClockTimer_setVisibility.update(true);                   //  Set clock to visible 
            qd.PauseTimer_setVisibility.update(false);                  //  Set Pause timer to INvisible           
            qd.ClockTimer_setStyle.update(qd.box_highlight);            //  Highlight the Clock timer
            qd.PauseTimer_setStyle.update(qd.box_unhighlight);          //  UN_highlight the Pause ....       

        //  Ensure the ball images are showing, and also the red break box.        
        if (qd.PlayerAtTable == 1)
        {
            qd.Plain_setVisibility.update(true);                    //  Show SPOT and plain images
            qd.Spot_setVisibility.update(true);    
        } else
        {
            qd.Plain_setVisibility.update(true);                    //  Hide SPOT, show plain images
            qd.Spot_setVisibility.update(true);  
        }    
    }
    //
    //  ************************************************************************************************
    //                                                                         ****   END of Match   ***
    //                                                                         *************************
    static public void endMatch()
    {
        int status = 0, Player_ID = 0, Player, BreakSize = 0;
        String BreakList = "" ;
        //
        dbg.log( "QS Ctlr", "END MATCH pressed");
        //
        BreakSize = qd.PlayerBreak.getIntVal() ;   //  Get any final break
        //
        EndBreak( qd.PlayerAtTable, true ) ;          //  End any break UNFINISHED. 
        //
        qd.MatchStatusScreen = qd.STATUS_COMPLETE ;
        //
        qd.PlayerBreak.update(0) ;
        qd.ClockTimer.stop();                            //  Stop both timers and hide them
        //
        // qd.PauseTimer  = new TimeCounterDisplay( qd.max_pause_timer, true, true)  ;  //  Create a new Pause timer
        qd.PauseTimer.init(qd.MAX_PAUSE_TIMER, true, true) ;
        qd.PauseTimer.stop();
        
        qd.ClockTimer_setVisibility.update(false);      //  Set clocks to invisible 
        qd.PauseTimer_setVisibility.update(false);  
        
        qd.Player2Name_setStyle.update(   qd.box_unhighlight ) ;
        qd.Player1Name_setStyle.update(   qd.box_unhighlight ) ;
        qd.Player2Score_setStyle.update(  qd.box_unhighlight ) ;
        qd.Player1Score_setStyle.update(  qd.box_unhighlight ) ;

        qd.Plain_setVisibility.update(false);                   //  Show / hide the spot / plain images
        qd.Spot_setVisibility.update(false);                    //  Show / hide the spot / plain images
        //
        qd.PlayerBreak_setVisibility.update(false);             //  Hide the Break box
        // 
        //  Record END OF MATCH message.
        //
        //  Update the player score onscreen
        if (qd.PlayerAtTable == 1)  
        {  
            if (qd.PlayersSwapped)  { Player_ID = qd.Player2ID ; } else  { Player_ID = qd.Player1ID ; }            
        } else 
        {  
            if (qd.PlayersSwapped)  { Player_ID = qd.Player1ID ; } else  { Player_ID = qd.Player2ID ; }            
        }
        status = scorer.record( 
                    Player_ID,                  //  Current player at table
                    0,                          //  Score
                    BreakSize,    // 
                    Qdata.SHOTTYPE_ENDFRAME, 
                    "End of match", 
                    qd.PlayerVisits.getIntVal() ) ;
        if (status != 1)  { dbg.log( "QS Ctlr", "End Match message ERROR" ); } ;
        //
        qd.GameOver         = true ;                    //  Match / frame is now OVER !
        qd.MatchInProgress  = false ;                   //
        qd.ScoreboardLocked = true ;                    //  LOCK the scoreboard
        //
        qd.MsgBox.show("End of match", 1000);           //  Show the points for long period
        //
    }
    //
    //
    //  ************************************************************************************************
    //                                                                  ****  Switch active player   ***
    //                                                                  ********************************
    //   The Name and Score of the active player are highlighted and the other unhighlighted (greyed).
    //
    static public void SwitchPlayer ( )
    {
        dbg.log( "QS Ctlr", "Switch Player");
        //
        //  Only execute this if match is in progress.
        //
        if (qd.GameOver)
        {
            qd.MsgBox.show("Game over - scoreboard LOCKED !", 1000);                      //  Show the points for long period
            dbg.log( "QS Ctlr", "Game over - scoreboard LOCKED !");
            hideCountdown() ;   //  Hide the stopwatch so that they can see player 2's breaks.
            return ;
        }
        //
        qd.PlayerBreak_setVisibility.update(true);                  //  Ensure the Break box is visible
        qd.PlayerBreak.update(0) ;                                  //  Zero the break
        //
        if (qd.PlayerAtTable == 1)  
        {  
            qd.PlayerAtTable = 2 ; 
            qd.Player1Name_setStyle.update(   qd.box_unhighlight ) ;
            qd.Player2Name_setStyle.update(   qd.box_highlight ) ;
            qd.Player1Score_setStyle.update(  qd.box_unhighlight ) ;
            qd.Player2Score_setStyle.update(  qd.box_highlight ) ;
            qd.transition_direction = false ;
         } 
        else 
        { 
            qd.PlayerAtTable = 1 ;
            qd.Player1Name_setStyle.update(   qd.box_highlight ) ;
            qd.Player2Name_setStyle.update(   qd.box_unhighlight ) ;
            qd.Player1Score_setStyle.update(  qd.box_highlight ) ;
            qd.Player2Score_setStyle.update(  qd.box_unhighlight ) ;
            qd.transition_direction = true ;
            //
            qd.PlayerVisits.update( 1 + qd.PlayerVisits.getIntVal() );//  Increment the VISITS counter only if it's NOW player 1.
        }
        //
        qd.SwitchAnimation.start( qd.transition_direction ) ;  
        //
/* 
        dbg.log( "QS Ctlr", "trans dir = "+qd.transition_direction );
        dbg.log( "QS Ctlr", "player at table = "+qd.PlayerAtTable );
        dbg.log( "QS Ctlr", "transitUP = "+qd.SwitchAnimation.transitUP ) ;
        dbg.log( "QS Ctlr", "TL status = "+qd.SwitchAnimation.timeline.getStatus()) ;
        dbg.log( "QS Ctlr", "getFromY= "+qd.anim_break.getFromY()); 

        dbg.log( "QS Ctlr", "--------------------------------------------" ) ;
  /*/       
    }
    //  ************************************************************************************************
    //                                                                          ****  Swap players   ***
    //                                                                          ************************
    //   This may be needed after the string, eg. if the 'old' Spot wins the string and wants to go Plain.
    //
    static public void SwapPlainAndSpot ( )
    {
        int iTmp = 0 ;
        String sTmp = "" ;
        //
        dbg.log( "QS Ctlr", "Swap Players");
        //
        if (qd.GameOver)
        {
            qd.MsgBox.show("Game over - scoreboard LOCKED !", 1000);         //  Show the message
            dbg.log( "QS Ctlr", "Game over - scoreboard LOCKED !");
            hideCountdown() ;   //  Hide the stopwatch so that they can see player 2's breaks.
            return ;
        }
        //
        //  This is permitted only when no break is in progress. 
        //                 
        if ( qd.PlayerBreak.getIntVal() == 0 )  
        {  

//            iTmp = qd.Player1ID ;                       //  SWAP Player IDs
//            qd.Player1ID = qd.Player2ID ;
//            qd.Player2ID = iTmp ;

            qd.PlayersSwapped = !qd.PlayersSwapped ;    //  Flip the flag
            //
            sTmp = qd.Player1flag ;                     //  SWAP Player FLAGS
            qd.Player1flag = qd.Player2flag;
            qd.Player2flag = sTmp;
            //
            sTmp = qd.Player1Score.getValue() ;         //  SWAP Player SCORES 
            qd.Player1Score.setValue( qd.Player2Score.getValue() );
            qd.Player2Score.setValue( sTmp );           
            //
            sTmp = qd.Player1Breaks.getValue() ;        //  SWAP Player BREAKS
            qd.Player1Breaks.setValue( qd.Player2Breaks.getValue() );
            qd.Player2Breaks.setValue( sTmp );           
            //
            sTmp = qd.Player1Name.getValue() ;          //  SWAP Player NAMEs
            qd.Player1Name.setValue( qd.Player2Name.getValue() );
            qd.Player2Name.setValue( sTmp );           
            //
            //  Reverse the visibiliy of the front and rear flags. This effectively swaps them over.
            qd.Player1flagFront_setVis.update( !qd.Player1flagFront_setVis.getValue() );      
            qd.Player2flagFront_setVis.update( !qd.Player2flagFront_setVis.getValue() );     
            qd.Player1flagRear_setVis.update(  !qd.Player1flagRear_setVis.getValue() );       
            qd.Player2flagRear_setVis.update(  !qd.Player2flagRear_setVis.getValue() );      
            //
            dbg.log( "QS Ctlr", "Players swapped");
            qd.MsgBox.show("Players swapped", 3);   //  Briefly show the message 
        } 
        else 
        {   
            dbg.log( "QS Ctlr", "Cannot swap  - break in progress");
            qd.MsgBox.show( "Cannot swap  - break in progress", 2);  //  Briefly show the message
        }  
    }
    //
    //  ************************************************************************************************
    //                                                                      ****    Score Player    ****
    //                                                                      ****************************
    //   record score for player
    static public void ScorePlayer ( int activePlayer, int score, String msg, int shotType )
    {
        int status = 0, Player_ID = 0 ;
        dbg.log( "QS Ctlr", "Score Player (" + score + " pts)");
        //
        if (qd.GameOver)
        {
            qd.MsgBox.show("Game over - scoreboard LOCKED !", 1000);         //  Show the message
            dbg.log( "QS Ctlr", "Game over - scoreboard LOCKED !");
            hideCountdown() ;   //  Hide the stopwatch so that they can see player 2's breaks.
            return ;
        }
        //  Update the player score onscreen
        if (activePlayer == 1)  
        {  
            qd.Player1Score.update( score + qd.Player1Score.getIntVal() );            
            if (qd.PlayersSwapped)  { Player_ID = qd.Player2ID ; } else  { Player_ID = qd.Player1ID ;  }            
        } else 
        {  
            qd.Player2Score.update( score + qd.Player2Score.getIntVal() );            
            if (qd.PlayersSwapped)  { Player_ID = qd.Player1ID ; } else  { Player_ID = qd.Player2ID ;  }            
        }
        qd.MsgBox.show( msg, 1);   //  Briefly show the message 
        //
        //  Update the break onscreen and score in the db..
        qd.PlayerBreak.update(  score + qd.PlayerBreak.getIntVal() );
        //
        scorer.record(  Player_ID, 
                        score, 
                        qd.PlayerBreak.getIntVal(), 
                        shotType, 
                        msg, 
                        qd.PlayerVisits.getIntVal() ) ; 
        //
    }
    //
    //  ************************************************************************************************
    //                                                                      ****     END break      ****
    //                                                                      ****************************
    //   record score for player
    static public void EndBreak ( int activePlayer, Boolean unfinished )
    {
        int status = 0, Player_ID = 0 ;
        int BreakSize = 0 ;
        String BreakList = "" ;
        dbg.log( "QS Ctlr", "End break");
        //
        if (qd.GameOver)
        {
            qd.MsgBox.show("Game over - scoreboard LOCKED !", 1000);         //  Show the message
            dbg.log( "QS Ctlr", "Game over - scoreboard LOCKED !");
            hideCountdown() ;   //  Hide the stopwatch so that they can see player 2's breaks.
            return ;
        }
        //        
        BreakSize = qd.PlayerBreak.getIntVal() ;
        //  Was the break UNFINISHED (ie. end of frame) ?
        if (unfinished)  { status =  qd.SHOTTYPE_ENDBREAKUNFINISHED ;} else { status =  qd.SHOTTYPE_ENDBREAK ; }
        //
        //  Update the score        
        //       
        if (activePlayer == 1)  
        {  
            if (qd.PlayersSwapped)  {   Player_ID = qd.Player2ID ; } else  {   Player_ID = qd.Player1ID ;} 
            scorer.record(  Player_ID, 0, BreakSize, status, "End of break", qd.PlayerVisits.getIntVal() ) ; 
            //  Is this break worth displaying ?
            if (BreakSize >= qd.BreaksLimit)    
            {          
                BreakList = scorer.getBreaks( Player_ID, qd.BREAKSLIMITVERBOSE ) ;
                qd.Player1Breaks.set( BreakList ) ;
           } 
        } else 
        {  
            if (qd.PlayersSwapped)  
            {   Player_ID = qd.Player1ID ; } else   { Player_ID = qd.Player2ID ;}            
            scorer.record(  Player_ID, 0, BreakSize, status, "End of break", qd.PlayerVisits.getIntVal() ) ; 
            if (BreakSize >= qd.BreaksLimit)       //  Is this break worth displaying ?
            {          
                BreakList = scorer.getBreaks( Player_ID, qd.BREAKSLIMITVERBOSE ) ;
                qd.Player2Breaks.set( BreakList ) ;
            } 
        }
        //
        SwitchPlayer() ;                //  ...and SWITCH PLAYER since the break has ended.
        //
    }
    //
    //  ************************************************************************************************
    //                                                                   ****    Penalise Player    ****
    //                                                                   *******************************
    //   record score for player
    static public void PenalisePlayer ( int activePlayer, int penalty, String msg )
    {
        int status = 0, Player_ID = 0  ;
        dbg.log( "QS Ctlr", "Penalise Player (" + penalty + " pts)");
        //
        if (qd.GameOver)
        {
            qd.MsgBox.show("Game over - scoreboard LOCKED !", 1000);         //  Show the message
            dbg.log( "QS Ctlr", "Game over - scoreboard LOCKED !");
            hideCountdown() ;   //  Hide the stopwatch so that they can see player 2's breaks.
           return ;
        }
        //      
        EndBreak( activePlayer, false ) ; //  End the break first. This'll handle break recording & display, etc.      
        // 
        //  Score the penalty points and zero the break since a foul doesn't count in a break.
        //  Remember that the OTHER player gets the points.       
        // 
        if (activePlayer == 1)  
        {  
            qd.Player2Score.update( penalty + qd.Player2Score.getIntVal() ); 
            if (qd.PlayersSwapped)  { Player_ID = qd.Player2ID ;  } else  { Player_ID = qd.Player1ID ;  }            
        } else 
        {  
            qd.Player1Score.update( penalty + qd.Player1Score.getIntVal() ); 
            if (qd.PlayersSwapped)  { Player_ID = qd.Player1ID ;  } else  { Player_ID = qd.Player2ID ;  }            
        }
        //
        status = scorer.record( Player_ID, penalty, 0, Qdata.SHOTTYPE_FOUL, msg, qd.PlayerVisits.getIntVal() ) ;
        //
        qd.MsgBox.show( msg, 3);   //  Briefly show the message 
    }
    //
    //  ************************************************************************************************
    //                                                                  ****    DELETE Last Score   ****
    //                                                                  ********************************
    //   Delete last score recorded
    //   This is tricky. We need to switch player as we track back.
    //
    static public void DeleteLastScore ( )
    {
        int status = 0, Player_ID = 0 ;
        dbg.log( "DeleteLastScore", "...");
        //        
        if (qd.GameOver)
        {
            qd.MsgBox.show("Game over - scoreboard LOCKED !", 1000);         //  Show the message
            dbg.log( "QS Ctlr", "Game over - scoreboard LOCKED !");
            hideCountdown() ;   //  Hide the stopwatch so that they can see player 2's breaks.
            return ;
        }
        //
        status = scorer.correct() ;
        //
        if (status == qd.ERR_CORRECTION_TIMEOUT)
        {
            qd.MsgBox.show( "Correction not allowed (time)", 2);   //  Briefly show the message                        
       
        } else if (status < 0)
        {
            qd.MsgBox.show( "ERROR " + status, 2);   //  Briefly show the message             
            dbg.log( "DeleteLastScore", "Error returned - " + status);
        } else
        {
            //  We need to determine if we must SWITCH PLAYER

            if (qd.PlayerAtTable == 1)  
            {  
                if (qd.PlayersSwapped)
                {   if (qd.Player2ID != scorer.Current_Player_ID)  {   SwitchPlayer() ;  } } else
                {   if (qd.Player1ID != scorer.Current_Player_ID)  {   SwitchPlayer() ;  } }            
            } else 
            {  
                if (qd.PlayersSwapped)
                {   if (qd.Player1ID != scorer.Current_Player_ID)  {   SwitchPlayer() ;  } } else
                {   if (qd.Player2ID != scorer.Current_Player_ID)  {   SwitchPlayer() ;  } }            
            }
        }
        //
        qd.PlayerBreak.update( scorer.Current_Player_Break ); 
        qd.MsgBox.show( "Correction", 2);   //  Briefly show the message             
        //  
    }
    //
    //  ************************************************************************************************
    //                                                                     ****    CLOCK Clicked    ****
    //                                                                     *****************************
    //   The CLOCK button has been clicked
    //
    static public void ClockClicked ( )
    { 
        //
        if (qd.MatchInProgress)
        {
            //  The match is in progress, so pause/resume as appropriate.
            if (qd.MatchPaused) 
            {  ResumeMatch() ; dbg.log( "QS Ctlr", "Match resumed"); } else 
            {  PauseMatch() ;  dbg.log( "QS Ctlr", "Match paused");  }
        }
    }
    //
    //
    static private void PauseMatch()
    {
        int status = 0 ;
        //
        qd.MatchPaused = true ;
        qd.MsgBox.show( "Match paused - hit any key to resume...", 0);     //  Show the message and leave it up (until next message)
        qd.ClockTimer_setStyle.update(qd.box_unhighlight);      //  UN_highlight the Clock ....       
        //
        //  If the main clock timer has expired then the pause timer is currently counting DOWN.
        //  In this special case, just PAUSE the counter.
        if (qd.ClockTimer.running) { qd.PauseTimer.restart(); } else {  qd.PauseTimer.pause(); }
        //
        qd.PauseTimer_setVisibility.update(true);               //  Set Pause timer to visible           
        qd.PauseTimer_setStyle.update(qd.box_highlight_RED);    //  highlight the Pause ....   
        //
        qd.PauseScreen_setVisibility.update(true );
        //
        status = scorer.record( 0, 0, 0, Qdata.SHOTTYPE_MATCH_PAUSED, "Match Paused", 0 ) ;
        //
    }
    //
    static private void ResumeMatch()
    {
        int status = 0 ;
        String tmp = "" ;
        //
        qd.MatchPaused = false ;
        qd.MsgBox.show( "Match resumed", 3);   //  Briefly show the message 
        // qd.ClockTimer_setVisibility.update(true);               //  Set clock to visible 
        qd.ClockTimer_setStyle.update(qd.box_highlight);        //  Highlight th+e Clock timer
        //
        //  If the main clock timer has expired then the pause timer is currently counting DOWN but STOPPED.
        //  In this special case, just RESUME the counter.
        if (qd.ClockTimer.running)  { qd.PauseTimer.pause(); } else  { qd.PauseTimer.restart(); }
        //
        // qd.PauseTimer_setVisibility.update(true);           //  Set Pause timer to visible           
        qd.PauseTimer_setStyle.update(qd.box_unhighlight);  //  UN_highlight the Pause .... 
        //
        qd.PauseScreen_setVisibility.update(false );
        //
        tmp = "Resumed. timer = " + qd.PauseTimer.str_timeLeft.getValue() ;
        status = scorer.record( 0, 0, 0, Qdata.SHOTTYPE_MATCH_RESUMED, tmp, 0 ) ;
        //
    }
    //
    //  Expand the message box. This happens at the end of the match.
    //
    private void ExpandMessageBox()
    {
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), lbMessage);
        st.setByX(1.5f);  st.setByY(1.5f);
        st.setToX(2.5f);  st.setToY(2.5f);
        st.setCycleCount((int) 2f);
        st.setAutoReverse(true);
        st.play();   
    }
    //  ************************************************************************************************
    //  
    //  Sets up the animation when the ENTER key is pressed (ie. end of break).
    //  The break box slides up/down to the next player, and the ball (plain/spot) expand or contracts to nothing.
    //
    static public  void setupAnimation( boolean transitUP )
    {
        qd.SwitchAnimation  = new Transition( false )  ;  
        //
        qd.parallelTransition = new ParallelTransition( )   ;
        //  Assemble the element transitions into the one parallel transition.
        qd.parallelTransition.getChildren().addAll( qd.anim_break, qd.anim_spot, qd.anim_plain ) ;
        //
        qd.anim_break.setByY( 5.0 );
        qd.anim_break.setCycleCount( 1 );
        qd.anim_break.setRate( 1.0 );
        // qd.anim_break.setAutoReverse( true );
        //
        qd.anim_spot.setCycleCount( 1 );
        qd.anim_spot.setRate( 1.0 );
        //
        qd.anim_plain.setCycleCount( 1 );
        qd.anim_plain.setRate( 1.0 );

        //  Set up a callback for timer expiry.
        qd.SwitchAnimation.timeline.setOnFinished(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            { 
                if (qd.SwitchAnimation.transitUP)
                {                  
                    qd.anim_spot.setFromX( 0.0 );
                    qd.anim_spot.setFromY( 0.0 );
                    qd.anim_spot.setToX( 1.0 );
                    qd.anim_spot.setToY( 1.0 );
                    qd.anim_plain.setFromX( 1.0 );
                    qd.anim_plain.setFromY( 1.0 );
                    qd.anim_plain.setToX( 0.0 );
                    qd.anim_plain.setToY( 0.0 );
                    qd.anim_break.setFromY(100); 
                    qd.anim_break.setToY(0);
                } else
                {
                    qd.anim_spot.setFromX( 1.0 );
                    qd.anim_spot.setFromY( 1.0 );
                    qd.anim_spot.setToX( 0 );
                    qd.anim_spot.setToY( 0 );
                    qd.anim_plain.setFromX( 0.0 );
                    qd.anim_plain.setFromY( 0.0 );
                    qd.anim_plain.setToX( 1.0 );
                    qd.anim_plain.setToY( 1.0 );
                    qd.anim_break.setFromY(0); 
                    qd.anim_break.setToY(110);         //  The new Y coordinate for player 2
                } 
                qd.parallelTransition.play();
                //
           }       //  end of - (class) handle 
         }   ) ;     //  end of - callback for timer expiry.     
    }
    //
    /*****************************************
    static public  void setupAnimation( boolean transitUP )
    {
        qd.SwitchAnimation  = new Transition( false )  ;  
        //
        qd.parallelTransition = new ParallelTransition( )   ;
        //  Assemble the element transitions into the one parallel transition.
        qd.parallelTransition.getChildren().addAll( qd.anim_break, qd.anim_spot, qd.anim_plain ) ;
        //
        qd.anim_break.setByY( 5.0 );
        qd.anim_break.setCycleCount( 1 );
        qd.anim_break.setRate( 1.0 );
        // qd.anim_break.setAutoReverse( true );
        //
        qd.anim_spot.setCycleCount( 1 );
        qd.anim_spot.setRate( 1.0 );
        //
        qd.anim_plain.setCycleCount( 1 );
        qd.anim_plain.setRate( 1.0 );

        //  Set up a callback for timer expiry.
        qd.SwitchAnimation.timeline.setOnFinished(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            { 
                if (qd.SwitchAnimation.transitUP)
                {                  
                    qd.anim_spot.setFromX( 0.0 );
                    qd.anim_spot.setFromY( 0.0 );
                    qd.anim_spot.setToX( 1.0 );
                    qd.anim_spot.setToY( 1.0 );
                    qd.anim_plain.setFromX( 1.0 );
                    qd.anim_plain.setFromY( 1.0 );
                    qd.anim_plain.setToX( 0.0 );
                    qd.anim_plain.setToY( 0.0 );
                    qd.anim_break.setFromY(100); 
                    qd.anim_break.setToY(0);
                } else
                {
                    qd.anim_spot.setFromX( 1.0 );
                    qd.anim_spot.setFromY( 1.0 );
                    qd.anim_spot.setToX( 0 );
                    qd.anim_spot.setToY( 0 );
                    qd.anim_plain.setFromX( 0.0 );
                    qd.anim_plain.setFromY( 0.0 );
                    qd.anim_plain.setToX( 1.0 );
                    qd.anim_plain.setToY( 1.0 );
                    qd.anim_break.setFromY(0); 
                    qd.anim_break.setToY(110);         //  The new Y coordinate for player 2
                } 
                qd.parallelTransition.play();
                //
           }       //  end of - (class) handle 
         }   ) ;     //  end of - callback for timer expiry.     
    }
    * *************************************************/
    //
    //  ************************************************************************************************
    //
    //  Init the scores to ZERO
    //  Also set up the db connection for scoring.
    //  
    static public void initScores( )
    {
        int status ;
        dbg.log( "QS Ctlr", "INIT SCORES requested");
        //
/*
        SwitchPlayer() ;
        SwitchPlayer() ;
*/        
        //  Setup connection to LOCAL MySQL
        scorer = new SaveScore( qd.ConnStr, qd.LocalUsername, qd.LocalPassword ) ;
        status = scorer.initScores();
        //
        //  Assemble debug info, viewable on the menu pages.
        //  ------------------------------------------------    
        qd.DebugInfo.update( "Version "+qd.QB_VERSION+", IP = " + qd.piIP + ", Name = " + qd.piNAME + ", SID = "+ qd.piSSID + ", MAC = " + qd.piMACADDR+ ", nw = " + qd.piNI + ", Frame ID = " + qd.FrameID );
        //
        qd.Player1Score.update( 0 );                //  Zero out the two scores
        qd.Player2Score.update( 0 );
        qd.PlayerBreak.update( 0 );
        qd.Player2Name_setStyle.update(   qd.box_highlight ) ;
        qd.Player1Name_setStyle.update(   qd.box_highlight ) ;
        qd.Player2Score_setStyle.update(  qd.box_highlight ) ;
        qd.Player1Score_setStyle.update(  qd.box_highlight ) ;
        qd.Plain_setVisibility.update(true);                   //  Show / hide the spot / plain images
        qd.Spot_setVisibility.update(true);                    //  Show / hide the spot / plain images
        qd.PlayerVisits.update( 1 );
        qd.Player1Breaks.update( 0 ); qd.Player1Breaks.update("") ;
        qd.Player2Breaks.update( 0 ); qd.Player2Breaks.update("") ;
        //
        qd.PauseTimer.init( qd.MAX_PAUSE_TIMER, true, true ) ;  
        qd.ClockTimer.init( qd.MatchDuration * 60, false, true ) ;
        setupClockHandler() ;
        //
        qd.ClockTimer_setVisibility.update(false);  //  Set clocks to invisible 
        qd.PauseTimer_setVisibility.update(false); 
        //
        // 
        hideCountdown() ;  //  ...just in case it's still showing.
        //
        qd.MsgBox.show("Scores ZEROED", 3);             //  Briefly show the points 
    }
    //  ************************************************************************************************
    //
    //  Init the system
    //  
//    public void initSystem( )
//    {
//        int i ;
//        dbg.log( "QS Ctlr", "INIT requested");
//        try 
//        {
//            i = qd.init() ;
//        } catch (IOException ex) { Logger.getLogger(QB_Qscreen_Controller.class.getName()).log(Level.SEVERE, null, ex);  }
//        //
//        endMatch() ;
//        initScores() ;
//        //
//        lbTimer.setStyle(   qd.box_highlight ) ;
//        lbPause.setStyle(   qd.box_unhighlight );
//        lbPause.setVisible(false);                  //  Hide the Pause timer ! 
//        lbP1name.setStyle(  qd.box_highlight ) ;    //  Unhighlight players and their scores
//        lbP1score.setStyle( qd.box_highlight ) ;
//        lbP2name.setStyle(  qd.box_highlight ) ;
//        lbP2score.setStyle( qd.box_highlight ) ;                  
//        imPlain.setVisible( true );                 //  Show the spot / plain images
//        imSpot.setVisible( true );           
//        lbRefereeLabel.setVisible(false);    //  Referee display 
//        lbReferee.setVisible(false);         //  
//        //
//        qd.MsgBox.show("System initialised",2);             //  Briefly show the points 
//    }
    //
/******************************************************************************* */
    public void LoadSponsorImages() 
    {
        //
        //  Load the image for the two sponsors)
        //   
        String imgFile1 = "", imgFile2 = "" ;
        File diskFile1, diskFile2 ;
        //
        diskFile1 = new File( qd.FILEPATH_IMAGES + qd.LogoSponsor1);      //  Red icon with red X -> no LAN or WAN        
        Image refimage1 = new Image(diskFile1.toURI().toString());       
        imSponsor1.setImage( refimage1 );     
        
        diskFile2 = new File( qd.FILEPATH_IMAGES + qd.LogoSponsor2);      //  Red icon with red X -> no LAN or WAN        
        Image refimage2 = new Image(diskFile2.toURI().toString());       
        imSponsor2.setImage( refimage2 );     
    }
    //
    //  Test if a string contains a +ve integer.
    //
    public static boolean isPositiveInteger(String str) 
    {
        int i = 0 ;
        try 
        {       
            i = Integer.parseInt(str);  
            if (i > 0)  return true; else return false ;
        } catch (NumberFormatException nfe) { return false; }
    }
//    //
//    //  Show a message
//    //
//    static public void ShowMessage( String msg, int secs )
//    {
//        if (qd.MsgBox.running) { qd.MsgBox.unshow() ; } //  Terminate any running message
//        qd.MsgBox.show(msg, secs) ;                     //  Briefly show the points  
//    }
    //
    //              ***********************************************
    //              **************  EVENT HANDLERS   **************
    //              ***********************************************
    //
    //  This is the handler for "key pressed". When a button on the Bluetooth remote (or keyboard) is pressed, this fires.
    //
    //  The problemette is that this callback also handles IR keypress events as well as Bluetooth. The IR
    //  codes are keys - not codes. Consequently the keypresses are not numbered and you can get 6 events firing for e
    //  a singl keypress no problem. Therefore we CANNOT use this for handling IR input.
    //
   @FXML
    void tfKeyEvent_BT(KeyEvent ke)
    {
        int stat = 0 ;
        String chr = "" ;
        //
        chr = ke.getText().toLowerCase() ;
        dbg.log( "QS Ctlr", "BT key pressed - "+chr);
        //
        if (qd.MetakeyTimer.running) qd.MetakeyTimer.stop();         //  Stop if already running.
        //
        //  If the previous char was MENU mode key then execute the menu function specified by this char.
        if (qd.lastchr.equals(Qdata.BLUETOOTH_MENU_KEY))
        {
            dbg.log( "QS Ctlr", "MENU key pressed"+", this chr = "+chr);
            //     ------------------------
            stat = BTctl.exec_special( chr ) ;
            //     ------------------------
            qd.lastchr = "" ;

        } else if (chr.equals(Qdata.BLUETOOTH_MENU_KEY))
        {   //  It's the MENU key. Start a timer and give the punter one sec to hit the function key.
            qd.lastchr = chr ;  
            //  ---------------------------------------------
            //  Start a timer here to switch out of menu mode
            //  ---------------------------------------------            
            qd.MetakeyTimer.start();        //  Start the timer  
            qd.MenuPane_setVisibility.setValue(Boolean.TRUE);
        } else
        { //       -----------------
            stat = BTctl.exec( chr ) ;
        } //       -----------------
    }
    //
    //  The keypresses are not numbered and you can get 6 events firing for e
    //  a singl keypress no problem. The way round this is to use the Ket RELEASED event, not
    //  the Pressed event.
    //
    //  The IR control is set up on Linux to use ONLY alphanumeric keys (0-9, a-z). This avoids the
    //  extra handling needed to cope with control chars such as CR, FN, etc.
    //
   @FXML
    void tfKeyEvent_IR(KeyEvent ke)
    {
        int stat = 0 ;
        String chr = "" ;
        //
        //  ANY keypress takes us out of pause mode
        if (qd.MatchPaused)  {  ResumeMatch() ;  return ;  }
        //
        // The 'standoff' period is a short time (one  sec) when an ENTER keypress 
        // will be ignored. This is to fix the (horrible0 animation problem.
        if (qd.StandoffTimer.running) { return ; }  ;                                           
//
//      KeyCode  code ;
//      code = ke.getCode() ; 
        //
        chr = ke.getText().toLowerCase() ;   //  Get the char from the keyboard
        dbg.log( "IR Ctlr", "key = "+chr);
        //
        //  Check if this is the ENTER char. If so START the standoff timer.
        if (chr.equalsIgnoreCase("p"))  {  qd.StandoffTimer.start();  }
        //
        //  If the metakey timer is running then stop it now.
        if (qd.MetakeyTimer.running) qd.MetakeyTimer.stop();
        //
        //  If the PREVIOUS char was MENU mode key then execute the menu function specified by this char.
        if (qd.lastchr.equals(Qdata.INFRARED_MENU_KEY))
        {
            dbg.log( "IR Ctlr", "MENU key pressed"+", this chr = "+chr);
            //     ------------------------
            stat = IRctl.exec_special( chr ) ;
            //     ------------------------
            qd.lastchr = "" ;

        } else if (chr.equals(Qdata.INFRARED_MENU_KEY))
        {   //  It's the MENU key.
            //
            qd.lastchr = chr ;            
            qd.MenuPane_setVisibility.setValue(Boolean.TRUE);
            //  ---------------------------------------------
            //  Start a timer here to switch out of menu mode
            //  ---------------------------------------------            
            qd.MetakeyTimer.start(); 
            //
            hideCountdown() ;           //  Hide any coundown images

        } else
        { 
            //     -----------------
            stat = IRctl.exec( chr ) ;
            //     -----------------
        }
    }
    //
    //
    // *****************************************************************************************
    // ******************************  Setup TIMERS  *******************************************
    // *****************************************************************************************
    //
    //  This countdown method is called by the TimerDisplay class when the remaining time is
    //  15 or less seconds to run.
    //
    static public void CountDown( int secs ) 
    {
        //  If BOTH timers are running, then we need to use only the Pause timer. We therefore  
        //  need to wait on the expiry of the main timer before using the Pause timer.
        //
        if  ( ((qd.ClockTimer.running) && (qd.PauseTimer.savedSecs ==0)) || ((qd.PauseTimer.running) && (!qd.PauseTimer.countUP) && (!qd.ClockTimer.running)) )
        {
//          dbg.log( "QS Ctlr", "Showing countdown - "+secs);
            qd.Countdown_setVisibility[secs].setValue(Boolean.TRUE);
        } 
    }
    //
    public static void hideCountdown()
    {
        for (int i=0; i < qd.COUNTDOWN_TIME; i++ )
        {
            qd.Countdown_setVisibility[i].setValue(Boolean.FALSE);           
        }
    }
    //
    //  Setup the PAUSE timer. 
    //
    private void setupPauseTimer( int secs, boolean countUp, boolean ShowSecs)
    {
        //  This consists of just instantiating the timer and binding it to the screen element.
        // qd.PauseTimer  = null ;
        // qd.PauseTimer  = new TimeCounterDisplay( secs, countUp, ShowSecs)  ;      //  true => count UP; true => show secs; max pause = 8 hours
        qd.PauseTimer_setVisibility.update(false);
        lbPause.textProperty().bind( qd.PauseTimer.timeLeftProperty_fmt() );               
    }
    //
    //  Setup the METAKEY timer. 
    //
    private void setupMetakeyTimer( int msecs, boolean countUp, boolean ShowSecs)
    {
        //  Set up the timer for keyboard metakey action.
        //  This consists of instantiating the timer and declaring a callback handler for timeout.
        qd.MetakeyTimer  = new TimeCounter( msecs, countUp, ShowSecs)  ;    //  false => count DOWN, false => Don't show secs
        //  Set up a callback for timer expiry.
        qd.MetakeyTimer.timeline.setOnFinished(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                qd.lastchr = "" ;
                qd.MenuPane_setVisibility.setValue(Boolean.FALSE);
            }       //  end of - (class) handle 
        }   ) ;     //  end of - callback for timer expiry.  
        // 
    }
    //
    //  Setup the 'standoff' key timer. 
    //  This is to prevent double hits on a key.
    //
    private void setupStandoffTimer( int msecs, boolean countUp, boolean ShowSecs)
    {
        //  Set up the timer for keyboard metakey action.
        //  This consists of instantiating the timer and declaring a callback handler for timeout.
        qd.StandoffTimer  = new TimeCounter( msecs, countUp, ShowSecs)  ;    //  false => count DOWN, false => Don't show secs
        //  Set up a callback for timer expiry.
        qd.StandoffTimer.timeline.setOnFinished(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                qd.StandoffTimer.stop();
            }       //  end of - (class) handle 
        }   ) ;     //  end of - callback for timer expiry.  
        // 
    }
    //
    //  The setup the CLOCK timer. This is tricky.
    //  Once the main (count-down) timer expires, the pause timer should start to count DOWN, defraying any 
    //  accumulated time for toilet breaks, etc.  Remember that the pause timer normally counts UP.
    //  
    public void setupClockTimer( int secs, boolean countUP, boolean ShowSecs)
    {
        qd.ClockTimer.init(secs, countUP, ShowSecs) ;
        lbTimer.textProperty().bind( qd.ClockTimer.timeLeftProperty_fmt() );
        //
    }
    //
    public static void setupClockHandler()
    {
        //  Set up a callback for timer expiry.
        //
        qd.ClockTimer.timeline.setOnFinished(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent event) 
            {
                int s = 0 ;
                s = qd.PauseTimer.timeLeft() ;
                if ( (s == qd.MAX_PAUSE_TIMER) || (s == 0) )
                {
 //                 ExpandMessageBox() ;
                    endMatch() ;                   
                } else
                {
                    qd.MsgBox.show( "Now using Pause timer", 4 );   //  show the message
                    //
                    //  Restart the pause timer, but counting DOWN this time.
                    qd.ClockTimer_setStyle.update(qd.box_unhighlight);      //  UN-Highlight the Clock timer
                    qd.PauseTimer_setStyle.update(qd.box_highlight);        //  highlight the Pause .... 
                    qd.ClockTimer.running = false ;
                    int i = 0 ;
                    i = qd.PauseTimer.timeLeft() ;  //  First get what time has been clocked, then start the countDOWN with this time.
                    qd.PauseTimer.init(i, false, true) ;
                    qd.PauseTimer.start();
                    if (qd.MatchPaused) { PauseMatch() ; }  //  If it was already paused, then PAUSE the timer.                          
                    //
                    // ALWAYS end the match if the PAUSE timer expires (ie. hits zero) 
                    qd.PauseTimer.timeline.setOnFinished(new EventHandler<ActionEvent>() 
                    { 
                        @Override   
                        public void handle(ActionEvent event) 
                        {   //ExpandMessageBox() ;  
                            endMatch() ;  
                        }  }   ) ;
                }
            }       //  end of - (class) handle 
        }   ) ;     //  end of - callback for timer expiry.  
    }
    //
    //  ************************************************************************************************
}

