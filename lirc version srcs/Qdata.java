/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;

import QB.qboard ;
import QB.MessageBox ;
import static QB.QB_Qscreen_Controller.SwapPlainAndSpot;
import static QB.qboard.qd;
// import static QB.qboard.qd;
//
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
/** *
 * @author Jim
 */
//
public class Qdata
{
// ************************************************
    public final static String QB_VERSION = "V1.24" ; 
// ************************************************
    public  static Scene qscene ; 

    public  static String   OperatingSystem         = "";       //  the host op sys
    public  static String   FILEPATH                = "";       //  Paths depending on OS
    public  static String   FILEPATH_IMAGES         = "" ;             
    public  static String   FILEPATH_FLAGS          = "" ;       
    public  static String   IMAGE_EXT               = ".png" ;  //  default image extension     
    public final static int MAXCHARS_PLAYERNAME     = 21 ;      //  Max chars allowed in a player name.          
    public final static int MIN_BREAK               = 50 ;      //  Min break for display         
    //
    //  ERROR constants
    public final static int ERR_NO_DB_CONNECTION    = -100  ; 
    public final static int ERR_NO_DB_DATA          = -101  ; 
    public final static int ERR_NO_DB_ACCESS        = -102 ;
    public final static int ERR_FEDERATED_TABLE     = -103 ;
    public final static int ERR_NO_REMOTE_DB_ACCESS = -104 ;
    public final static int ERR_NULL_COMMAND        = -109 ;
    public final static int ERR_CORRECTION_TIMEOUT  = -120 ;
    public final static int ERR_FILENOTFOUND        = -130 ;
    public final static int ERR_NO_NETWORK          = -140 ;
    public final static int ERR_DISPATCHER          = -150 ;
    public final static int ERR_IR_DISPATCHER       = -151 ;
    public final static int ERR_EXCEPTION           = -900 ;
    public final static int ERR_NO_DB_DATA_EX       = -901 ;
    //
    //  Shot type constants
    public final static int SHOTTYPE_SCORE          = 1 ;       //  General scoring shot
    public final static int SHOTTYPE_POT            = 5 ;      //  We'll use these in Phase 3 (maybe)
    public final static int SHOTTYPE_INOFF          = 6 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_POTBAKER       = 12 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_INOFFRED       = 15 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_INOFFBAKER     = 16 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_CANNON         = 19 ;      // ...Phase 3 ...
    //
    public final static int SHOTTYPE_POTRED         = 21 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_POTYELLOW      = 22 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_POTGREEN       = 23 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_POTBROWN       = 24 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_POTBLUE        = 25 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_POTPINK        = 26 ;      // ...Phase 3 ...
    public final static int SHOTTYPE_POTBLACK       = 27 ;      // ...Phase 3 ...
    
    public final static int SHOTTYPE_FOUL           = 99 ;
    public final static int SHOTTYPE_ENDBREAK       = 100 ;     //  End of break.
    //  ------------------  Values OVER 100 are not countable 'shots'  ---------------------------
    public final static int SHOTTYPE_ENDBREAKUNFINISHED = 101 ;     //  End of UNFINISHED break.
    public final static int SHOTTYPE_BEGINFRAME     = 105 ;     //  Begin frame / match.
    public final static int SHOTTYPE_ENDFRAME       = 106 ;     //  End of frame / match. Corresponds to cuesports status.
    public final static int SHOTTYPE_MATCH_PAUSED   = 108 ;
    public final static int SHOTTYPE_MATCH_RESUMED  = 109 ;
    //
    //  Game/match globals
    public int      GameType            ;
    public final static int GAMETYPE_BILLIARDS_TIMED  = 1 ;
    public final static int GAMETYPE_BILLIARDS_FRAMES = 2 ;
    public final static int GAMETYPE_SNOOKER          = 10 ;
    public final static int GAMETYPE_POOL_9BALL       = 20 ;
    public final static int GAMETYPE_POOL_ENGLISH     = 21 ;    
    public final static int GAMETYPE_CAROM            = 30 ;
    //
    public final static String BLUETOOTH_MENU_KEY     = "m" ;   // Bluetooth remote handset - MENU Key   
    public final static String INFRARED_MENU_KEY      = "m" ;   // Infrared remote handset - MENU Key   
    //
    // Time (MILLIsecs) for expiry of Metakey press.
    public final int TIMEOUT_MENU_KEY                   = 20*1000 ;  
    // Time (MILLIsecs) for preventing double keypresses.
    public final int TIMEOUT_STANDOFF                   = 1000 ;  
    // No corrections allowed if previous score is older than this value (4 mins).
    public final int CORRECTION_TIMEOUT               = 240*1000 ;  //  This is in SECS.
    //
    //  !!!!!!!!!!!!!!!!!!!  IMPORTANT GLOBALS HERE  !!!!!!!!!!!!!!!!!!!!!
    public boolean  WANup               = false  ;      //  These two global booleans are 
    public boolean  UsingServerConfig   = false  ;      //  used to show on screen icons.
    public boolean  GameOver            = false  ;      //  Goes to TRUE when match / frame ends.
    public boolean  MatchPaused         = false  ;      //  
    public boolean  MatchInProgress     = false  ;      //  
    public int      AdvancedScoring     = 0      ;      //  Advanced Scoring - count hazards, cannons, etc.
    public boolean  PlayersSwapped      = false  ;      //  Indicates when Plain and Spot are swapped.
    //
    public int      MatchStatusScreen = 0      ;        //  0 = not started; 1 = started; 2 = complete
    public final static int   STATUS_NOTSTARTED  = 0  ;
    public final static int   STATUS_STARTED     = 1  ;
    public final static int   STATUS_COMPLETE    = 2  ;
    //
    public int      PlayerAtTable       ;   //  0 = undefined, 1 = Player 1 at table,...
    // public int      SpotPlayer          ;   //  0 = undefined, 1 = Player 1 is Spot, 2 = Player 2 is Spot 
    public int      MatchDuration       ;   //  minutes  [loadable]
    public int      PointsLimit         ;   //  points  [loadable]
    public int      BreaksLimit         ;   //  Min break size  [loadable]
    public final static int BREAKSLIMITVERBOSE = 1 ; //  1 = NORMAL, 2 = VERBOSE

    //  db details
    public String   ServerIP            ;   // [loadable]
    public String   ServerPort          ;   // [loadable]
    public String   LocalIP             ;   // [loadable]
    public String   ServerUsername      ;   // [loadable]
    public String   ServerPassword      ;   // [loadable]
    public String   LocalUsername       ;   // [loadable]
    public String   LocalPassword       ;   // [loadable]
    public final static String   ConnStr = "jdbc:mysql://localhost:3306/qb" ;  //  NOT loadable
    //
    public String   piIP         = ""   ;   // IP address of rPi
    public String   piNAME       = ""   ;   // hostname ...
    public String   piMACADDR    = ""   ;   // MAC address ...
    public String   piNI         = ""   ;   // Network Interface ("eth0", "lo") ...
    public String   piSSID       = ""   ;   // SID name, if at all
    //
    public Stage    Qstage              ;   
    //  Player IDs
    public int      Player1ID           ;   // [loadable]
    public int      Player2ID           ;   // [loadable]
    public String   Player1flag         ;  
    public String   Player2flag         ; 
    public int      RefereeID           ;   // [loadable]
    public String   Refereeflag         ;   // [loadable] 
    public String   Round               ; 
    public int      MatchStatus         ;
    public int      FrameStatus         ;
    public int      FrameID             ;
    public int      FrameNumber         ;   //  Current fraem number
    public int      MatchID             ;
   //
    public  country_flags flags         ;   //  Declare flag class.
    //
    public  int  DebugLog = 0           ;   //  Log to db ?
    //
    //  Type of remote control - IR, Bluetooth, Wireless, ???
    public static final int RC_INFRARED    = 1  ;       
    public static final int RC_BLUETOOTH   = 2 ;       
    public static final int RC_WIRELESS    = 3 ;             
    public  int   RemoteControlType   = RC_INFRARED ;  //  Set up Bluetooth as the default controller type
    //
    //  Screen variables
    //  ----------------
    //  Tournament/match details
    public ScreenValue   TournamentName = new ScreenValue("")   ;   // [loadable]
    public ScreenValue   MatchName      = new ScreenValue("")   ;   // [loadable]
    public ScreenValue   MatchType      = new ScreenValue("")   ;   // [loadable]
    //      Player 1 ...
    public ScreenValue   Player1Name    = new ScreenValue("")   ;   // [loadable] 
    public ObservableValue<? extends String>   ov_Player1Name_setStyle    ; 
    public sScreenValue  Player1Name_setStyle = new sScreenValue( ov_Player1Name_setStyle )   ;
    public ScreenValue   Player1Score   = new ScreenValue("")   ;  
    public ScreenValue   Player1Frames  = new ScreenValue("")   ;
    public ObservableValue<? extends String>   ov_Player1Score_setStyle    ; 
    public sScreenValue  Player1Score_setStyle = new sScreenValue( ov_Player1Score_setStyle )   ;
    //
    public ScreenValue   Player1Breaks   = new ScreenValue("")   ;  
    public ScreenValue   Player2Breaks   = new ScreenValue("")   ;  

    //      Player 2 ...
    public ScreenValue   Player2Name    = new ScreenValue("")   ;   // [loadable]
    public ObservableValue<? extends String>   ov_Player2Name_setStyle    ; 
    public sScreenValue  Player2Name_setStyle = new sScreenValue( ov_Player2Name_setStyle )   ;
    public ScreenValue   Player2Score   = new ScreenValue("")   ; 
    public ScreenValue   Player2Frames  = new ScreenValue("")   ;
    public ObservableValue<? extends String>   ov_Player2Score_setStyle    ; 
    public sScreenValue  Player2Score_setStyle = new sScreenValue( ov_Player2Score_setStyle )   ;
    //
    public ScreenValue   RefereeName    = new ScreenValue("")   ;   // [loadable] 
    //
    public ScreenValue   PlayerBreak    = new ScreenValue("")   ;    
    public ObservableValue<? extends Boolean>   ov_PlayerBreak_setVisibility    ; 
    public bScreenValue  PlayerBreak_setVisibility = new bScreenValue( ov_PlayerBreak_setVisibility )   ;
    //
    // public ScreenValue   MenuPane    = new ScreenValue("")   ;    
    public ObservableValue<? extends Boolean>   ov_MenuPane_setVisibility    ; 
    public bScreenValue  MenuPane_setVisibility = new bScreenValue( ov_MenuPane_setVisibility )   ;
//    public ObservableValue<? extends Number>   ov_PlayerBreakXcoord   ;    
//    public iScreenValue   PlayerBreakXcoord    = new iScreenValue( ov_PlayerBreakXcoord )   ;    
    public ObservableValue<? extends Integer>   ov_PlayerBreakYcoord   ;    
    public iScreenValue   PlayerBreakYcoord    = new iScreenValue( ov_PlayerBreakYcoord )   ;       
    //
    public ScreenValue   PlayerVisits   = new ScreenValue("")   ;  
    //
    //  Player (& ref) FLAG properties 
    public ObservableValue<? extends Image>   ov_Player1flag_setImage    ; 
    public imScreenValue  Player1flag_image = new imScreenValue( ov_Player1flag_setImage )   ;
    public ObservableValue<? extends Image>   ov_Player2flag_setImage    ; 
    public imScreenValue  Player2flag_image = new imScreenValue( ov_Player2flag_setImage )   ;
    public ObservableValue<? extends Image>   ov_RefereeFlag_setImage    ; 
    public imScreenValue  RefereeFlag_image = new imScreenValue( ov_RefereeFlag_setImage )   ;
    //
    //  Visibility properties
    //     - Front -
    public ObservableValue<? extends Boolean>   ov_Player1flagFront_setVis    ; 
    public bScreenValue  Player1flagFront_setVis = new bScreenValue( ov_Player1flagFront_setVis )   ;
    public ObservableValue<? extends Boolean>   ov_Player2flagFront_setVis    ; 
    public bScreenValue  Player2flagFront_setVis = new bScreenValue( ov_Player2flagFront_setVis )   ;
    //      - REAR -
    public ObservableValue<? extends Boolean>   ov_Player1flagRear_setVis    ; 
    public bScreenValue  Player1flagRear_setVis = new bScreenValue( ov_Player1flagRear_setVis )   ;
    public ObservableValue<? extends Boolean>   ov_Player2flagRear_setVis    ; 
    public bScreenValue  Player2flagRear_setVis = new bScreenValue( ov_Player2flagRear_setVis )   ;
    //
    //  Timer stuff...
    public String        MatchDate      ;
    public ScreenValue   MatchTimer     = new ScreenValue("")   ;
    public ScreenValue   MatchPauses    = new ScreenValue("")   ;   //  hh:mm.ss  
    public ScreenValue   TimeNow        = new ScreenValue("")   ;   //  hh:mm 
    //  Table & Session ...
    public ScreenValue   Table          = new ScreenValue("")   ;   // [loadable] 
    public ScreenValue   Session        = new ScreenValue("")   ;   // [loadable]
    //
    public int      NumFrames           ;   // [loadable]
    // public ScreenValue   NumFrames      = new ScreenValue("")   ;   // [loadable]  
    // 
    //  Spot and Plain balls...
    public ObservableValue<? extends Boolean>   ov_Spot_setVisibility    ; 
    public bScreenValue  Spot_setVisibility = new bScreenValue( ov_Spot_setVisibility )   ;
    public ObservableValue<? extends Boolean>   ov_Plain_setVisibility    ; 
    public bScreenValue  Plain_setVisibility = new bScreenValue( ov_Plain_setVisibility )   ;
     //
    //  Animation - triggered by end-of-break.
    public Transition           SwitchAnimation ;
    public ParallelTransition   parallelTransition ;
    public ScaleTransition      anim_spot  ;
    public ScaleTransition      anim_plain  ;
    public TranslateTransition  anim_break  ;
    //
    //  Main and Pause Clocks
    public final int MAX_PAUSE_TIMER    = 8 * 3600 ; //  8 hours max pauses.
    public TimeCounterDisplay   ClockTimer     = new TimeCounterDisplay( 0, false, true );
    public TimeCounterDisplay   PauseTimer     = new TimeCounterDisplay( MAX_PAUSE_TIMER, true, true );
    public TimeCounter          MetakeyTimer   = new TimeCounter( 0, true, true );
//  public TimeCounter          StandoffTimer  = new TimeCounter( 0, true, true );
    public String        lastchr = "" ;        //  Used by MetakeyTimer expiry.
   // 
    public ObservableValue<? extends Boolean>   ov_ClockTimer_setVisibility    ; 
    public bScreenValue  ClockTimer_setVisibility = new bScreenValue( ov_ClockTimer_setVisibility )   ;
    public ObservableValue<? extends Boolean>   ov_PauseTimer_setVisibility    ; 
    public bScreenValue  PauseTimer_setVisibility = new bScreenValue( ov_PauseTimer_setVisibility )   ;
    //  Bind STYLE property
    public ObservableValue<? extends Boolean>   ov_ClockTimer_setStyle    ; 
    public sScreenValue  ClockTimer_setStyle  = new sScreenValue( ov_ClockTimer_setStyle )   ;
    public ObservableValue<? extends Boolean>   ov_PauseTimer_setStyle    ; 
    public sScreenValue  PauseTimer_setStyle  = new sScreenValue( ov_PauseTimer_setStyle )   ;
    //
    public ObservableValue<? extends Boolean>   ov_PauseScreen_setVisibility    ; 
    public bScreenValue  PauseScreen_setVisibility  = new bScreenValue( ov_PauseScreen_setVisibility )   ;    
    //
    //public  ObservableValue<? extends Boolean>   ov_msgbox_setVis    ; 
    //public  bScreenValue  msgbox_setVis = new bScreenValue( ov_msgbox_setVis )   ;
    public MessageBox    MsgBox  = new MessageBox() ;
    //
    public ScreenValue   DebugInfo    = new ScreenValue("")   ;   // [loadable] 
    //  Styles for player name and score boxes, also timers.
    public final static String box_highlight     = "-fx-background-color: #FFFFFF;" ;  //  White
    public final static String box_highlight_RED = "-fx-background-color: #e30613;" ;  //  Red
    public final static String box_unhighlight   = "-fx-background-color: #AAAAAA;" ;  //  Grey
   //  
    //  Countdown images
    //
    public final int COUNTDOWN_TIME     = 16 ;       //  15 sec countdown
    public ObservableValue<? extends Boolean>[]   ov_Countdown_setVisibility = new ObservableValue[COUNTDOWN_TIME]  ; 
    public bScreenValue[]  Countdown_setVisibility = new bScreenValue[COUNTDOWN_TIME];
    //
    public db qbdb_local  ;  //  db connection
    //
    //  Constructor
    //
    Qdata(  ) 
    {
        int i ;
        try {  i = init() ;  } //  Initialise globals - all in object Qdata     
        catch (IOException ex) { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex); }
    }
    //
    public int init() throws IOException 
    {
        int stat = 0 ;
        //
        //
        //  Initialise the array for the countdown images
        for ( int i=0; i < COUNTDOWN_TIME; i++ )
        {   Countdown_setVisibility[i] =  new bScreenValue( ov_Countdown_setVisibility[i] ) ; }
        //
        OperatingSystem = getOS() ;        //  Determine OS platform.
        //
        if (OperatingSystem.equals("LINUX"))
        {   //                  ****************  Linux platform  *******************
            //
            FILEPATH        = "/qb/qb.txt";                 //  Linux platform (Rpi2b), in top-level folder of file system
            FILEPATH_FLAGS  = "/qb/flags/" ;                //  Linux platform (Rpi2b), ...     
            FILEPATH_IMAGES = "/qb/images/" ;                //  Linux platform (Rpi2b), ...     
        }
        else if (OperatingSystem.equals("WINDOWS"))
        {   //                  ****************  Windows Platform ******************
            //  
            FILEPATH        = "build/classes/QB/qb.txt";    //  Windows platform, alongside distribution files
            FILEPATH_FLAGS  = "build/classes/QB/flags/" ;   //  Windows platform, ...       
            FILEPATH_IMAGES = "build/classes/QB/images/" ;  //  Linux platform (Rpi2b), ...     
        }  //  If it's not either of these then - "Houston - we have a problem !"  
        //       
        flags = new country_flags() ;     //  Instantiate flag class
        //
        stat = init_config() ;  //  Setup our configuration, whether from file of db.
        //
        return stat ;
    }
    // ---------------------------------------------------------------------------------------------------------------
    //
    //  Read in params from file - qb.txt
    //
    public int getFileParams() throws IOException
    {
        int i = 0, stat = 1 ;
        String strLine;

        qboard.dbg.log( "parseline",  "Getting params from file - " + FILEPATH) ;  

        //  Load params from data file
        FileInputStream fstream = null;
        DataInputStream in  = null;
        BufferedReader br  = null;
        //
        try 
        {
            fstream = new FileInputStream(FILEPATH);
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)
            {
                i = parseLine(strLine) ;
            }
            //  Log values read in
            qboard.dbg.log( "parseline",  "Player1Name    = " + Player1Name.strValue) ;  
            qboard.dbg.log( "parseline",  "RefereeName    = " + RefereeName.strValue) ;  
            qboard.dbg.log( "parseline",  "RefereeFlag    = " + Refereeflag) ;  
            qboard.dbg.log( "parseline",  "Player1flag    = " + Player1flag ) ;    
            qboard.dbg.log( "parseline",  "Player2Name    = " + Player2Name.strValue) ;  
            qboard.dbg.log( "parseline",  "Player2flag    = " + Player2flag ) ;    
            qboard.dbg.log( "parseline",  "TournamentName = " + TournamentName.strValue) ;  
            qboard.dbg.log( "parseline",  "MatchName      = " + MatchName.strValue) ;  
            qboard.dbg.log( "parseline",  "MatchType      = " + MatchType.strValue) ;  
            qboard.dbg.log( "parseline",  "Player1ID      = " + Player1ID) ; 
            qboard.dbg.log( "parseline",  "Player2ID      = " + Player2ID) ;  
            qboard.dbg.log( "parseline",  "GameType       = " + GameType) ;   
            qboard.dbg.log( "parseline",  "numFrames      = " + NumFrames) ;   
            qboard.dbg.log( "parseline",  "MatchDuration  = " + MatchDuration) ;  
            qboard.dbg.log( "parseline",  "LocalUsername  = " + LocalUsername) ;  
//          qboard.dbg.log( "parseline",  "LocalPassword  = " + LocalPassword) ;   
            qboard.dbg.log( "parseline",  "ServerIP       = " + ServerIP) ;  
            qboard.dbg.log( "parseline",  "ServerPort     = " + ServerPort) ;  
            qboard.dbg.log( "parseline",  "LocalIP        = " + LocalIP) ;  
            qboard.dbg.log( "parseline",  "ServerUsername = " + ServerUsername) ; 
//          qboard.dbg.log( "parseline",  "ServerPassword = " + ServerPassword) ;  
            qboard.dbg.log( "parseline",  "Session        = " + Session.strValue ) ;  
            qboard.dbg.log( "parseline",  "Table          = " + Table.strValue ) ;  
            qboard.dbg.log( "parseline",  "DebugLog       = " + DebugLog ) ;  
            qboard.dbg.log( "parseline",  "BreaksLimit    = " + BreaksLimit ) ;  
            qboard.dbg.log( "parseline",  "RemoteControl  = " + RemoteControlType ) ; 
            qboard.dbg.log( "parseline",  "AdvancedScoring= " + AdvancedScoring ) ; 
       } 
       catch (FileNotFoundException ex) { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex); stat = ERR_FILENOTFOUND ; }
       catch (IOException ex)           { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex); stat = ERR_EXCEPTION ; } 
       finally {       try { if  (br != null) br.close(); } 
            finally {  try { if (in != null) in.close(); } 
                finally {    if (fstream != null) fstream.close(); } } }
         //
        return stat ;
    }
    //
    //  Parse a single line, expecting token / value pair.
    //  Comment char = "!"   - ignore rest of line where found.
    //
    private int parseLine( String line )
    {
        int i = -1 ;
        String token ;
        String value ;
        StringTokenizer st ;
        String commentChar = "!" ;
        String assignmentChar = "=" ;
        //
        //  Locate comment char (!) and delete rest of line.
        i = line.indexOf( commentChar ) ;
        if (i > 0)  {  line = line.substring(0, i) ;  } 
        else if ((i == 0) || (line.length() == 0) ) { return i ; }   //  If that was a whole comment line or a blank one then we're finished.
        //
        // If anything left in string then parse it.
        // 
        st =  new StringTokenizer( line, assignmentChar);
        token = st.nextToken().trim() ;
        value = st.nextToken().trim() ;
        //
        //  Do NOT show passwords.
        if (token.contains("PASSWORD")) 
        {   qboard.dbg.log( "parseline", token  + " = ***** "  ); } 
        else
        {   qboard.dbg.log( "parseline", token  + " = " + value.toString() );  }
        //
        switch (token)
        {
            case "REFEREENAME":         RefereeName.setValue(value);                    break ;
            case "REFEREEFLAG":         Refereeflag     = value;                        break ;
            case "PLAYER1NAME":         Player1Name.setValue(value);                    break ;
            case "PLAYER2NAME":         Player2Name.setValue(value);                    break ;
            case "TOURNAMENTNAME":      TournamentName.setValue(value) ;                break ;
            case "MATCHNAME":           MatchName.setValue(value) ;                     break ;
            case "MATCHTYPE":           MatchType.setValue(value) ;                     break ;
            case "SESSION":             Session.setValue(value);                        break ;
            case "TABLE":               Table.setValue(value);                          break ;
            case "PLAYER1FLAG":         Player1flag       = value;                      break ;
            case "PLAYER2FLAG":         Player2flag       = value;                      break ;
            case "SERVERIP":            ServerIP          = value.replace("\"", "") ;   break ; //  Strip the embedded double quotes that
            case "SERVERPORT":          ServerPort        = value.replace("\"", "") ;   break ; //  MySQL sticks in.
            case "LOCALIP":             LocalIP           = value.replace("\"", "") ;   break ; //  
            case "SERVERUSERNAME":      ServerUsername    = value.replace("\"", "") ;   break ; // 
            case "SERVERPASSWORD":      ServerPassword    = value.replace("\"", "") ;   break ;  
            case "LOCALUSERNAME":       LocalUsername     = value.replace("\"", "") ;   break ;
            case "LOCALPASSWORD":       LocalPassword     = value.replace("\"", "") ;   break ;
            case "PLAYER1ID":           Player1ID         = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;
            case "PLAYER2ID":           Player2ID         = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;
            case "GAMETYPE":            GameType          = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;
            case "NUMFRAMES":           NumFrames         = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;
            case "MATCHDURATION":       MatchDuration     = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;            
            case "DEBUGLOG":            DebugLog          = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;           
            case "BREAKSLIMIT":         BreaksLimit       = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;           
            case "REMOTECONTROLTYPE":   RemoteControlType = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;            
            case "ADVANCEDSCORING":     AdvancedScoring   = (value.length() == 0) ? 0 : Integer.parseInt( value );  break ;            
            
            default:    qboard.dbg.log( "parseline", value  + " = " + token + " <--- ERROR - PARAM NOT RECOGNISED ");       
        }
 
        return i ;
    }
    //
    //          ********************** 
    //          *** Get db params  ***
    //          **********************
    //  The call to the server returns a lot of info re the vurrent frame/match.
    //  Data returned from the SP is ...
                   /*
                      column    data
                      ------    ----
                        1       tournament
                        2       date
                        3       p1_ID 
                        4       p2_ID
                        5       p1_name 
                        6       p2_name
                        7       p1_flag 
                        8       p2_flag
                        9       round
                        10      session
                        11      table
                        12      match
                        13      match_ID
                        14      matchstat
                        15      frame_ID
                        16      framestat
                        17      numframes
                        18      duration
                        19      pointslimit
                        20      referee_name
                        21      referee_flag
                        22      match_type
                        23      game type (eg. billiards timed, pool english,...)
                */
    //
    public int getDBParams()
    {   
        String query  ;
        int stat = 0, i = 0 ;
        //
        //  If no network then we can't access the remote server.
        //  The server IP address should be at least 8 chars. This is just a quick sanitary check.
//      if ((!WANup) || (ServerIP.isEmpty()) || (ServerIP.length() < 8) )   
        if ((ServerIP.isEmpty()) || (ServerIP.length() < 8) )   
        {  return ERR_NO_REMOTE_DB_ACCESS;  }
        //
        //  Note that this query executes ON THE SERVER, not on the QB.
        query = "call qb.`pMatchInfo_SELECT`( '" + piMACADDR + "' ) ;" ;
        //
        db qbdb_remote = new db( "jdbc:mysql://" + ServerIP + ":3306/qb", ServerUsername, ServerPassword) ;
        //
        qboard.dbg.log( "getDBParams", "Issuing query..." + query);       
        //
        ResultSet res  ;
        res = qbdb_remote.execute( query ) ;
        if (res == null)
        {
            stat = ERR_NO_DB_CONNECTION ;
            qboard.dbg.log( "getDBParams", "ERROR - no database connection possible !");  
            return stat ;  //  <<<<<<---------------------
        }
        try {
            //
            if ( !res.isBeforeFirst() )        //  Test if we have ZERO rows returned
            {
                qboard.dbg.log( "getDBParams", "ERROR - no data returned !");  
                stat = ERR_NO_DB_DATA ;
            } else
            {
                qboard.dbg.log( "getDBParams", "Retrieving params from resultset..." + query);
                res.next() ;
                //
                try
                {
                    stat = 1 ;          //   Looked good - declare SUCCESS
                    //
                    do                  //  Loop round records in Resultset.
                    {
                        TournamentName.set( res.getString(1) ) ;
                        MatchDate =         res.getString(2) ;
                        Player1ID =         Integer.parseInt(res.getString(3)) ;
                        Player2ID =         Integer.parseInt(res.getString(4) );
                        Player1Name.set(    res.getString(5) ) ;
                        Player2Name.set(    res.getString(6) ) ;
                        Player1flag =       res.getString(7) ;
                        Player2flag =       res.getString(8) ;
                        Round  =            res.getString(9) ;
                        Session.set(        res.getString(10));
                        Table.set(          res.getString(11));
                        MatchName.set(      res.getString(12));
                        MatchID =           Integer.parseInt(res.getString(13) );
                        MatchStatus =       Integer.parseInt(res.getString(14) );
                        FrameID =           Integer.parseInt(res.getString(15) );
                        FrameStatus =       Integer.parseInt(res.getString(16) );
//                      NumFrames.set(      res.getString(17) );
                        NumFrames =         Integer.parseInt(res.getString(17) );
                        MatchDuration =     Integer.parseInt(res.getString(18) );
                        PointsLimit   =     Integer.parseInt(res.getString(19) );
                        RefereeName.set(    res.getString(20) );
                        Refereeflag =       res.getString(21);
                        MatchType.set(      res.getString(22));
                        GameType =          Integer.parseInt(res.getString(23)) ;
                        //
                        qboard.dbg.log( "getDBParams", " Record # " + ++i );
                        qboard.dbg.log( "getDBParams", "      TournamentName = " + TournamentName.getValue());
                        qboard.dbg.log( "getDBParams", "      MatchDate      = " + MatchDate);
                        qboard.dbg.log( "getDBParams", "      Player1ID      = " + Player1ID);
                        qboard.dbg.log( "getDBParams", "      Player2ID      = " + Player2ID);
                        qboard.dbg.log( "getDBParams", "      Player1Name    = " + Player1Name.getValue());
                        qboard.dbg.log( "getDBParams", "      Player2Name    = " + Player2Name.getValue());
                        qboard.dbg.log( "getDBParams", "      Player1flag    = " + Player1flag);
                        qboard.dbg.log( "getDBParams", "      Player2flag    = " + Player2flag);
                        qboard.dbg.log( "getDBParams", "      Round          = " + Round);
                        qboard.dbg.log( "getDBParams", "      Table          = " + Table.getValue());
                        qboard.dbg.log( "getDBParams", "      Session        = " + Session.getValue());
                        qboard.dbg.log( "getDBParams", "      MatchName      = " + MatchName.getValue());
                        qboard.dbg.log( "getDBParams", "      MatchID        = " + MatchID);
                        qboard.dbg.log( "getDBParams", "      MatchStatus    = " + MatchStatus);
                        qboard.dbg.log( "getDBParams", "      FrameID        = " + FrameID);
                        qboard.dbg.log( "getDBParams", "      FrameStatus    = " + FrameStatus);
                        qboard.dbg.log( "getDBParams", "      NumFrames      = " + NumFrames);
                        qboard.dbg.log( "getDBParams", "      MatchDuration  = " + MatchDuration);
                        qboard.dbg.log( "getDBParams", "      PointsLimit    = " + PointsLimit);
                        qboard.dbg.log( "getDBParams", "      RefereeName    = " + RefereeName.getValue());
                        qboard.dbg.log( "getDBParams", "      Refereeflag    = " + Refereeflag);
                        qboard.dbg.log( "getDBParams", "      MatchType      = " + MatchType.getValue());
                        qboard.dbg.log( "getDBParams", "      GameType      = "  + GameType);
                        //
                    //  ************************************************************
                    //  Uncomment this only if you want to download multiple matches
                    //  ************************************************************
//                  } while(res.next()) ;
                    } while(1==2) ;
                    //
                } catch (SQLException e)
                {
                    qboard.dbg.log( "getDBParams", "ERROR - cannot get db access - exception = " + e.getMessage());
                    stat = ERR_NO_DB_ACCESS ;
                }             
            }
        } catch (SQLException ex) 
        {
            qboard.dbg.log( "getDBParams", "ERROR - no data returned !  Exception = " + ex.getMessage());  
            stat = ERR_NO_DB_DATA_EX ;
        }
        //
        qbdb_remote.close(); 
        return stat ;
    }
    //
    //  Save server params to db.
    //  We need these data saved in the db for the federated table.
    //
    public int SetupDatabase()
    {   
        String query = "", sTemp = "" ;
        int stat = 0, i = 0, iTemp = 0 ;
        Date  CreateTime  = new Date() ; 
        //
        // db qbdb_local = new db( "jdbc:mysql://" + LocalIP + ":3306/qb", LocalUsername, LocalPassword) ;
        //
        //  Save the SERVER specific params to a separate table (params).
        //
        query = "INSERT INTO qb.serverparams (serverIP, `when`, serverUser, serverPassword) VALUES (" ;
        query = query + "'" + ServerIP + "', " ;
        query = query + "'" + CreateTime.toString() + "', " ;
        query = query + "'" + ServerUsername + "', " ;
        query = query + "'" + ServerPassword + "' ) " ;
        //
        qboard.dbg.log( "SetupDatabase", "(server) Issuing query..." + query);       
        //
        stat = qbdb_local.executeUpdate( query ) ;
        if (stat <= 0)
        {
            stat = ERR_NO_DB_CONNECTION;
            qboard.dbg.log( "SetupDatabase", "ERROR - no database connection possible !");  
            return stat ;
        } else
        {
            stat = 1 ;
            qboard.dbg.log( "SetupDatabase", "SUCCESS - server params saved to local db !");             
        }
        //
        //  Now that we know that the database is fine, we can set up the federated table for live scoring.
        //  BUT ONLY IF WE HAVE NETWORK AND SERVER CONNECTIVITY !
        //
//      if ( (UsingServerConfig) && (WANup) )
        if (UsingServerConfig) 
        {
            query = "CALL qb.pCreateFederatedTable (  " ;
            query = query + UsingServerConfig + ", " ;
            query = query + "'" + ServerIP + "', " ;
            query = query + "'" + ServerPort + "', " ;
            query = query + "'" + ServerUsername + "' , " ;
            query = query + "'" + ServerPassword + "' ) " ;                                              
            qboard.dbg.log( "SetupDatabase", "(server) Issuing query..." + query);       
            //
            stat = qbdb_local.executeUpdate( query ) ;
            if (stat <= 0)
            {
                stat = ERR_FEDERATED_TABLE ;
                qboard.dbg.log( "SetupDatabase", "ERROR - CreateFederatedTable !");  
            } else
            {
                stat = 1 ;
                qboard.dbg.log( "SetupDatabase", "SUCCESS - server params saved to local db !");             
            }
        } else
        {
            qboard.dbg.log( "SetupDatabase", "WARNING - no federated table due to connectivity");             
        }
        //
        //  First ensure that no records for this frame exist (local & remote).
        //
        stat = qbdb_local.executeTidy( 3, FrameID, UsingServerConfig) ;
        // stat = qbdb_local.executeTidy( 2, FrameID, UsingServerConfig) ;
        //
        if (stat <= 0)
        {
            stat = ERR_NO_DB_ACCESS ;
            qboard.dbg.log( "SetupDatabase", "ERROR - no database connection possible !");  
            return stat ;
        } else
        {
            stat = 1 ;
            qboard.dbg.log( "SetupDatabase", "SUCCESS - Frame data deleted");             
        }
        //
        //  Save the MATCH specific params to a separate table (match).
        //  ==========================================================
        // 
        if ( (MatchDate.isEmpty() || MatchDate.length() < 10) ) { MatchDate = "2020-1-1 00:00:00"; }
        query = "INSERT INTO qb.matchparams (`MACADDR`,`match_ID`,`frame_ID`,`datestart`,`datestop`,`comments`,`status`," ;
        query = query + "`frame_number`,`referee_name`,`referee_flag`,`TournamentName`,`Player1ID`,`Player2ID`,`Player1Name`,`Player2Name`," ;
        query = query + "`Round`,`Session`,`Table`,`MatchName`,`MatchStatus`,`FrameStatus`," ;
        query = query + "`NumFrames`,`MatchDuration`,`PointsLimit`,`MatchType`,`GameType`) VALUES (" ;
        query = query + "'" + piMACADDR + "', " ;
        query = query + "" +  MatchID + ", " ;
        query = query + "" +  FrameID + ", " ;
        query = query + "'" + MatchDate + "', " ;
        query = query + "NULL, " ;
        query = query + "'no comments', " ;      
        query = query + "0, " ;
        
        query = query + "" +  FrameID+ ", " ;
        query = query + "'" + RefereeName.strValue + "', " ;
        query = query + "'" + Refereeflag + "', " ;
        query = query + "'" + TournamentName.strValue + "', " ;
        query = query + "" +  Player1ID + ", " ;
        query = query + "" +  Player2ID + ", " ;
        query = query + "'" + Player1Name.strValue + "', " ;
        query = query + "'" + Player2Name.strValue + "', " ;
        
        query = query + "'" + Round + "', " ;        
        query = query + "'" + Session.strValue + "', " ;        
        query = query + "'" + Table.strValue + "', " ;        
        query = query + "'" + MatchName.strValue + "', " ;
        query = query + "" +  MatchStatus + ", " ;
        query = query + "" +  FrameStatus + ", " ;
        query = query + "" +  NumFrames + ", " ;
        query = query + "" +  MatchDuration + ", " ;
        query = query + "" +  PointsLimit + ", " ;      
        query = query + "'" + MatchType.strValue + "', " ;
        query = query + "" +  GameType + " ) " ;
        //
        qboard.dbg.log( "SetupDatabase", "(match) Issuing query..." + query);       
        //
        stat = qbdb_local.executeUpdate( query ) ;
        if (stat <= 0)
        {
            stat = ERR_NO_DB_CONNECTION ;
            qboard.dbg.log( "SetupDatabase", "ERROR - no database connection possible !");  
        } else
        {
            stat = 1 ;
            qboard.dbg.log( "SetupDatabase", "SUCCESS - match params saved to local db !");             
        }
        // qbdb_local.close(); 
        return stat ;
    }
    //
    //
    //  Get MAC address from running system
    //  -----------------------------------
    //  Should work fine with Windows/Linux/...
    //  The only problem is that it returns the MAC address only of the ACTIVE circuit.
    //  This could be a problem if you switch from WiFi adapter to cabled Ethernet - that would be a different
    //  MAC address.
    //
    public int getNetworkInfo() throws IOException 
    {
        int status = 0 ;
        //
        String osName = getOS() ;
        qboard.dbg.log( "getNetworkInfo", "Operating System is " + osName);
        //
        //  ---------------------------------------------------
        //  Test if network is up (then set global accordingly)
        //  ---------------------------------------------------
        WANup = false ;     //  Assume the worst !
        try 
        {
            URL url = new URL("http://www.google.com");
            //  open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
            //  trying to retrieve data from the source. If there
            //  is no connection, this line will fail
            Object objData = urlConnect.getContent();
            //  If that works then the network is UP !
            WANup = true ;
            //
        } catch (Exception ex) 
        {  qboard.dbg.log( "Check Network Access", "EXCEPTION = " + ex.getMessage()); } 
        //
        //  Get IP address, MAC address (of Enet), name of active interface and hostname.
        //
        status = ListNets() ;
        //
        if (status != 1)  {  qboard.dbg.log( "Network> ", "ERROR     = " + status); }
        qboard.dbg.log( "Network> ", "Host name     = " + piNAME);
        qboard.dbg.log( "Network> ", "Host IP       = " + piIP);
        qboard.dbg.log( "Network> ", "Host NI       = " + piNI);
        qboard.dbg.log( "Network> ", "Host MAC addr = " + piMACADDR);
        //
        return status ;
    }
    //
    //  Not so simple to get IP address of active network card.
    //
    //  We need to get FOUR pieces of data :-
    //      - IP address of running network (WiFi or Ethernet)
    //      - MAC address of ethernet network
    //      - Network NAME (eg. "eth0", "wlan0").
    //      - Name of the computer
    //
    //  We need to iterate through all network interfaces, ignoring any beginning with
    //  the loopback address (127...).
    // 
    public int ListNets ()
    {
        piNAME = "";        //  Computer name
        piIP = "";          //  IP Address
        piNI = "" ;         //  Interface name (eg. "eth0")
        piMACADDR = "" ;    //  MAC address of Ethernet interface
        //
        try 
        {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets))  getInterfaceInformation(netint);
        
        } catch (SocketException ex) { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex); }
        //
        return 1 ;
    }
    //
    private void getInterfaceInformation(NetworkInterface netint) throws SocketException 
    {
        StringBuilder sb = new StringBuilder();
        String nwDisplayName, nwMAC, nwinetaddr ;
        nwMAC = "" ;
        StringTokenizer st ;
        String key = "" ;
        String key_index = "ESSID:" ;
        int i = 0 ;
        //
        //  Get the name of the SID we're connected to, if at all.
        ProcessBuilder builder = new ProcessBuilder ( "iwconfig", "wlan0") ;
        builder.redirectErrorStream(true) ;
        Process p ;
        try 
        {
            p = builder.start();
            BufferedReader r = new BufferedReader( new InputStreamReader(p.getInputStream())) ;
            String ln = "";
            do
            {
                ln = r.readLine() ; if (ln == null) { break ; } ;
                ln = ln.replaceAll( "\"", "") ;
                if (ln.contains( key_index ))
                {
                    //  Parse out the SID name
                    qboard.dbg.log("Network> ", "SID line = " + ln);
                    // ln = ln.trim() ;
                    i = ln.indexOf( key_index ) ;
                    piSSID = ln.substring( i + key_index.length() , ln.length() ).trim() ;                  
                    qboard.dbg.log("Network> ", "parsed SSID = " + piSSID);
                }
            } while (1==1);
        } catch (IOException ex)  { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex); }
        //
        //
        nwDisplayName = netint.getDisplayName().toString() ;
        if ( nwDisplayName.indexOf("lo") == 0 )             //  Ignore the loopback adapter
        {
            qboard.dbg.log("Network> ", "Ignoring loopback interface (lo)");
        } else
        {
            qboard.dbg.log("Network> ", "Display name: " + nwDisplayName);
            //  Need to reformat the h/w address as it starts as a byte array.
            byte[] mac = netint.getHardwareAddress();
            if (mac != null) 
            {
                for (i = 0; i < mac.length; i++) 
                {  sb.append(String.format("%02X", mac[i])); }
    //          {  sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "")); }
                nwMAC = sb.toString() ;
                //  If this is the Ethernet interface then get the MAC address. This is actually the UID,
                //  of the device regardless of whether we're using Ethernet or WiFi.
                if ( nwDisplayName.indexOf("eth0") == 0)  
                { piMACADDR =  nwMAC ; }
                //
            }
            qboard.dbg.log("Network> ", " name, h/w addr:      " + nwDisplayName + ", " + nwMAC);
            //
            //  If the display name (eg. "eth0") is present in the inet addr then it is an ipv6 address
            //  in which we're NOT interested. It's the ipv4 one we need.
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses)) 
            {
                nwinetaddr = inetAddress.toString() ;
                if ( nwinetaddr.contains(nwDisplayName) ) 
                {
                    qboard.dbg.log("Network> ", "Ignoring ipV6 address - " + inetAddress);                   
                } else
                {  
                    // If we get here then we have the ipv4 IP address of the active network interface.
                    qboard.dbg.log("Network> ", "IP:  " + inetAddress); 
                    piIP = inetAddress.toString().replace("/", "") ;    //  IP Address
                    piNI = nwDisplayName ;                              //  Name of active interface
                    InetAddress tmp = null ;
                    try 
                    {
                        tmp = InetAddress.getLocalHost();
                    } catch (UnknownHostException ex) { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    piNAME = tmp.getHostName();                         //  Computer name
                }
            }
        }
     }
    //
    //
    //  Routine to determine running Operating System. 
    //  Returns either WINDOWS or LINUX.
    //
    private String getOS()
    {
        String osName = System.getProperty("os.name");
        String os ;
       //
        if        (osName.startsWith("Windows")) { os =  osName.substring(0, "Windows".length()).toUpperCase() ; }
        else   if (osName.startsWith("Linux") )  { os =  osName.substring(0, "Linux".length()).toUpperCase() ; }
        else                                     { os = "UNKNOWN" ; }
        //
        return os ;
    }
    //
    //  CLASS:  country flags
    //
    static class country_flags
    {
        private int             num_flags = 0 ;
        private List<String>    country_name = new ArrayList();
        private List<String>    country_code = new ArrayList();   
        private List<String>    flag_path = new ArrayList();  
        //
        // When instantiated, create the array of flags, codes +filespecs for the flags.
        country_flags(  ) 
        {
            int stat = 0 ;
            try {
                //
                stat = setup_country_flags() ;
                //
            } catch (IOException ex) { qboard.dbg.log( "setup_flags", "ERROR - exception = " + ex.getMessage()); }
            //
            qboard.dbg.log( "setup_flags", "Flags set up.");
        }
        //
        //  Return the filepath to the image file for the specified country.
        //
        public String getFlagPath( String country ) 
        {
            int i = 0 ;
            String fpath = "" ;
            //
            if (country != null)
            {
                String uc = country ;
                //
                //  Scan array of country NAMES. Once a match is found get the corresponding
                //  CODE and PATH to the flag image.
               for (i=0; i < this.num_flags; i++) 
                {
                    fpath = country_name.get(i) ;
                    if (country.equalsIgnoreCase(country_name.get(i)))
                    {
                        qboard.dbg.log( "getFlagPath", "Flag for " +uc + " is " + flag_path.get(i));
                        return flag_path.get(i);  
                    }
                }
            }
            return "";   //  Return blank string        
        }
        //
        private int setup_country_flags(  ) throws IOException 
        {
            // When instantiated, create the array of flags, codes +filespecs for the flags.
            //
            int i = 0, stat = 1 ;
            String strLine;
            String SeparatorChar = "," ;
            String country ;
            String code ;
            String path ;
            StringTokenizer st ;
            //
            qboard.dbg.log( "setup_flags",  "Getting codes and names from file") ;  
            //  Load params from data file 
            FileInputStream fstream = null;
            DataInputStream in  = null;
            BufferedReader br  = null; 
            //
            try 
            {         
                fstream = new FileInputStream(FILEPATH_FLAGS + "country-codes.txt");
                in = new DataInputStream(fstream);
                br = new BufferedReader(new InputStreamReader(in));
                //Read File Line By Line
                while ((strLine = br.readLine()) != null)
                {
                    // 
                    st =  new StringTokenizer( strLine, SeparatorChar);
                    country = st.nextToken().trim() ;               //  Country NAME (<40 chars)
                    code = st.nextToken().trim().toLowerCase() ;    //  Country CODE (2 chars)
                    path = FILEPATH_FLAGS + code + IMAGE_EXT ;
                    //
                    country_name.add( country );                    //  Add to country NAMES array
                    country_code.add( code );                       //  Add to country CODES array  
                    flag_path.add( path );                          //  Add to country FLAGS array 
//                  qboard.dbg.log( "setup_flags", "Flag= " + country + ", code = " + code);    //  DEBUG

                }
                stat = 1 ;
                this.num_flags = country_name.size() ;
            }        
            catch (FileNotFoundException ex) { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex); stat = ERR_FILENOTFOUND ; }
            catch (IOException ex)           { Logger.getLogger(Qdata.class.getName()).log(Level.SEVERE, null, ex); stat = ERR_EXCEPTION ; } 
            finally {       try { if (br != null) br.close(); } 
                finally {   try { if (in != null) in.close(); } 
                    finally {     if (fstream != null) fstream.close(); } } }
            //
            qboard.dbg.log( "setup_flags", "Flags set up. Total = " + num_flags );
            return stat ;
        }
    }
    //  =================================================================================================================
    //  Initialise our configuration
    //  ----------------------------
    //  This could be from local config file or over the network.
    //
    public int init_config()
    {
        int stat = 0 ;
        //
        GameOver            = false  ;      //  Initialise these GLOBAL flags
        MatchPaused         = false  ;      //  
        MatchInProgress     = false  ;      //  
        PlayersSwapped      = false  ;
        //
        //  Init screen variables, etc.
        //
        //  Game/match globals
        //  0 = undefined, 1 = Billiards (timed), 2 = Billiards (frames), 10 = Snooker, 20 = Pool (9-ball), ...
        GameType        = GAMETYPE_BILLIARDS_TIMED ;    //  Default game type
        PlayerAtTable   = 1 ;                   //  0 = undefined, 1 = Player 1 at table,...
        PlayerVisits.update( 0 );               //  This increments each time player 1 visits the table.
        // SpotPlayer      = 0 ;                   //  0 = undefined, 1 = Player 1 is Spot, 2 = Player 2 is Spot 
        // numFrames       = 0 ;                // [loadable]
        FrameNumber     = 0 ;                   // 
        MatchDuration   = 60 ;                  //  minutes  [loadable]
        //  db details
        ServerIP        = "0.0.0.0" ;           // [loadable]
        ServerPort      = "" ;                  // [loadable]
        ServerUsername  = "jim" ;               // [loadable]
        ServerPassword  = "tennent6" ;          // [loadable]
        LocalUsername   = "jim" ;               // [loadable]
        LocalPassword   = "tennent6" ;          // [loadable]
        //  Player IDs
        Player1ID       = 0 ;                   // [loadable]
        Player2ID       = 0 ;                   // [loadable]
        //
        DebugLog = 0 ;
        //  Screen variables
        // 
        Table.set("");
        Session.set("") ;
        RefereeName.set("") ;
        //  Tournament/match details
        TournamentName.set( "" );               // [loadable]
        MatchName.set("" );                     // [loadable]
        MatchType.set("" );                     // [loadable]
        //      Player 1 ...
        Player1Name.set("Player 1") ;           // [loadable] 
        Player1Score.set("0");  
        Player1Frames.set("0"); 
        //      Player 2 ...
        Player2Name.set("Player 2");            // [loadable]
        Player2Score.set( "0" ); 
        Player2Frames.set("0");
        //
        PlayerBreak.set("0");
        //  Timer stuff
        MatchDate = "";
        MatchTimer.set("00:00");                // [loadable]
        MatchPauses.set("00:00");
        //
        TimeNow.set( "00:00" );                 //  fmt = hh:mm  
        //
        //  Next, load config either from file or server.
        //
        //  ====================  Go get params from file
        //
        try 
        { 
            qboard.dbg.log( "init",  "Loading config file") ;
            stat = getFileParams(); 
            if (stat < 0)  
                 { qboard.dbg.log( "init",  "ERROR - getFileParams returns = " + stat) ; }
            else { UsingServerConfig = false  ; }
            //
            //  Database stuff 
            //  -> cause db error ->   db qbdb = new db( "jdbc:mysql://10.0.999.137:3306/qb", "jim", "tennent6") ;  
            //
            qbdb_local = new db( ConnStr, LocalUsername, LocalPassword ) ;
            int i = qbdb_local.executeTidy( 1, 0, UsingServerConfig ) ;   //  Tidy up the db before we start
            //
            //  Set up debug logging if selected (in params file).
            //  --------------------------------------------------
            if (DebugLog > 0) {  qboard.dbg.setQbdb_local( ConnStr, LocalUsername, LocalPassword ) ; }
        } 
        catch (IOException ex) {  qboard.dbg.log( "init",  "ERROR - db exception =  " + ex.getMessage()) ;  }
        //
        //  ======================  Set up and configure the network.
        //
        try 
        {
            //  Set up and test the network. Get the (unique) MAC address, IP, name, etc. on the running system.
            stat = getNetworkInfo() ; 
            //
        } catch (IOException ex)  {  qboard.dbg.log( "init",  "EXCEPTION (networkinfo) - exc: "+ex.getMessage()) ; }
        //
        //
        //  ======================  Go get params from database. These will override file params (most of).
        //
//        if ((stat != 1) || (!WANup))
//        {
//            qboard.dbg.log( "init",  "ERROR - No network") ;
//            UsingServerConfig = false  ;
//            stat = ERR_NO_NETWORK ;   //  This will force use of config file.
//        } else
//        {   //  We have a MAC Address so can attempt connection with external db.
//            //  Get match & tournament info assigned to this rpi device.
            stat = getDBParams() ;
            if (stat != 1)  
            { 
                qboard.dbg.log( "init",  "ERROR - getDBParams returns error = " + stat) ; 
                UsingServerConfig = false  ;
            } 
            else 
            {  
                UsingServerConfig = true  ; 
                qboard.dbg.log( "init",  "SUCCESS - using server config ") ; 
            }
//        }
        //
        //  Horrible fix here. Add a space to the end of the player name to look better on screen.
        //  (sorry)
        Player1Name.update( Player1Name.get() + " ");
        Player2Name.update( Player2Name.get() + " ");
        //
        //  If the player IDs are not specified (can happen - eg. no n/w), then assign values.
        if (Player1ID == 0)  Player1ID = 1 ;
        if (Player2ID == 0)  Player2ID = 2 ;
        //
        //  Which remote control are we using ?
        switch (RemoteControlType)
        {
            case RC_INFRARED :  qboard.dbg.log( "init",  "Remote controller is INFRARED" ) ;
                                break ;
            case RC_BLUETOOTH:  qboard.dbg.log( "init",  "Remote controller is BLUETOOTH" ) ;
                                break ;
            case RC_WIRELESS:   qboard.dbg.log( "init",  "Remote controller is WIRELESS - USUPPORTED !!! ERROR !!!! " ) ;
                                break ;
            default:
        }        
        //
        //  Now that we are loaded with params (from disk or server). we can write them to the
        //  local db.
//        if (WANup)
//        {
            stat = SetupDatabase() ;
//        }
        if (stat != 1)   
        {   qboard.dbg.log( "init",  "ERROR - SetupDatabase returns error = " + stat) ;  }  else             
        {   qboard.dbg.log( "init",  "SUCCESS - Params saved to local db") ; }
        //
        //
        //  If flags are defined then use them (ref, player1, player2).
        //
        if (Refereeflag == "")    //  --  Referee --    RefereeFlag_image 
            {   qboard.dbg.log( "LoadFlagImage",  "No flag specified for Referee" ) ;  } else
            {   LoadFlagImage( Refereeflag, RefereeFlag_image ) ;  }
        if (Player1flag == "")    //  --  Player 1 (front)-- 
            {   qboard.dbg.log( "LoadFlagImage",  "No flag specified for Player 1" ) ;  } else
            {   LoadFlagImage( Player1flag, Player1flag_image ) ;  }
        if (Player2flag == "")    //  --  Player 2 (front)-- 
            {   qboard.dbg.log( "LoadFlagImage",  "No flag specified for Player 2" ) ;  } else
            {   LoadFlagImage( Player2flag, Player2flag_image ) ;  }
        //
        Player1flagFront_setVis.update(true);
        Player2flagFront_setVis.update(true);
        Player1flagRear_setVis.update(false);
        Player2flagRear_setVis.update(false);     
        //
        return stat ;
    }
    //
    //  Load the image for a particular flag (eg. "sc", "en",...)
    //   
    private void LoadFlagImage( String flag, imScreenValue iv )
    {
        String imgFile = "" ;
        File diskFile ;
        //
        if ((flag != "") && (flag != null))
        {
            imgFile = flags.getFlagPath( flag ) ;
            if (imgFile.length() > 0)        //  If no flag image found then ignore.
            {
                diskFile = new File( imgFile );
                Image refimage = new Image(diskFile.toURI().toString());       
                // Image refimage = new Image( "file://" +  imgFile ); //  The image class needs a URL-type filespec.
                iv.set(refimage);           
            } 
        }       
    }      

    //
}
