/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;
//
import QB.Qdata ;
import QB.qboard ;
import static QB.qboard.qd;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 *
 * @author Jim
 */
public class Qscreen extends Task<Integer>
// public class Qscreen extends Task<Integer> 
{
    public   int            GameType = 0 ;
    public   Stage          Qstage ;
    public   String         ScreenLayoutFile = "" ;
    public   Timeline       ClockTimeline;  
    public   Timeline       CountDownTimeline;  
    public   Timeline       CountUpTimeline;  
    //
    //
    //  Constructor
    public Qscreen(int GameType) throws IOException 
    { 
        this.GameType = GameType;
        //
        // Get the proper FXML file for the selected game type
        //
        switch (GameType)
        {
            case 0:                                 this.ScreenLayoutFile = "default.fxml" ;               break ;
            case Qdata.GAMETYPE_BILLIARDS_TIMED:    this.ScreenLayoutFile = "qb_billiards_timed.fxml" ;    break ;
            case Qdata.GAMETYPE_BILLIARDS_FRAMES:   this.ScreenLayoutFile = "qb_billiards_frames.fxml" ;   break ;
            case Qdata.GAMETYPE_SNOOKER:            this.ScreenLayoutFile = "qb_snooker.fxml" ;            break ;
            case Qdata.GAMETYPE_POOL_9BALL:         this.ScreenLayoutFile = "qb_pool_9ball.fxml" ;         break ;
            case Qdata.GAMETYPE_POOL_ENGLISH:       this.ScreenLayoutFile = "qb_pool_english.fxml" ;       break ;        
            case Qdata.GAMETYPE_CAROM:              this.ScreenLayoutFile = "qb_carom.fxml" ;              break ;        
            default:  qboard.dbg.log( "Qscreen", "Invalid GameType (" + GameType + ") <--- ERROR  ");       
        }
        qboard.dbg.log( "Qscreen", "Loading screen fonts...");
        //           Load up the screen fonts
        Font myFontCircularStd = Font.loadFont(getClass().getResourceAsStream("/fonts/CircularStd-Bold.otf"), 20);
        Font myFontMyriadPro   = Font.loadFont(getClass().getResourceAsStream("/fonts/MyriadPro-Regular.otf"), 20);
        //
        //  Set up and initialise TIME NOW display.
        SetupClock() ;
        //
        //
        //           Load up the screen
        qboard.dbg.log( "Qscreen", "Loading screen file - " + this.ScreenLayoutFile);
        Parent root = FXMLLoader.load(getClass().getResource(this.ScreenLayoutFile));      
        Scene scene = new Scene(root); 
        scene.setCursor(Cursor.NONE);  //  DOESN'T WORK !!!!!!!!!!!!!!!!
//        try {
//            // scene.setCursor(Cursor.NONE);
//            new Robot().mouseMove( 10,10);
//        } catch (AWTException ex) {
//            Logger.getLogger(Qscreen.class.getName()).log(Level.SEVERE, null, ex);
//        }
        qboard.qd.qscene = scene ;
        qboard.qd.Qstage.setScene(scene);
        qboard.qd.Qstage.show(); 
    }
    /**
     * Initializes the controller class.
     */
    //public void initialize(URL url, ResourceBundle rb) 
    //{
    //    this.lbP1name.setText("QS (class) init");
    //}  
    //
    @Override
    protected Integer call() throws Exception 
    {
        return 11;
    }
    //
    public void SetupClock()
    {
        //  Show clock in format - hh:mm, count in MINUTES
        //
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.ClockTimeline = new Timeline(
                        new KeyFrame(Duration.seconds(1), 
                                     new EventHandler<ActionEvent>() 
                                    { @Override
                                    public void handle(ActionEvent event) 
                                    {   
                                        final Calendar cal = Calendar.getInstance();
                                        qd.TimeNow.update(sdf.format( cal.getTime()) );
                                    } } )  );
        this.ClockTimeline.setCycleCount(Animation.INDEFINITE);  
        this.ClockTimeline.play();      
    }
    //
}