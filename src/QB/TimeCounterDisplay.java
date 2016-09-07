/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;
import QB.QB_Qscreen_Controller;
import static QB.qboard.qd;
//
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
//
//  This timer handles on-screen counters and timers.
//  -------------------------------------------------
public class TimeCounterDisplay 
{
    //  Constructor
    //      time -      the time to count up.down from, in SECS
    //      countUP -   sic
    //      showSecs -  show the seconds
    //
    private ReadOnlyIntegerWrapper  timeLeft;
    private ReadOnlyDoubleWrapper   timeLeftDouble;

    public  SimpleStringProperty    str_timeLeft = new SimpleStringProperty("");
    public  Timeline                timeline ;
    public  boolean                 running = false ;
    //
    public int                      duration = 0;
    public boolean                  countUP = true ;
    public boolean                  showSecs = true ;
    //
    public int    timeLeft()                            {   return timeLeft.get();  }
    public ReadOnlyIntegerProperty timeLeftProperty()   {   return timeLeft.getReadOnlyProperty();  }
    public StringProperty timeLeftProperty_fmt ()       {   return str_timeLeft ;   }
    public int savedSecs ;    
    //
    KeyValue kv_zero, kv_duration ;
    KeyFrame kf_start_COUNTDOWN, kf_stop_COUNTDOWN, kf_start_COUNTUP, kf_stop_COUNTUP ;
    //
    Timeline timelineUP = new Timeline();
    Timeline timelineDOWN = new Timeline();
    //
    public TimeCounterDisplay(final int time_secs, boolean countUP , boolean showSecs ) 
    {
        int status ;
        this.duration = time_secs ;
        this.countUP  = countUP ;
        this.showSecs = showSecs ;
        this.savedSecs = time_secs ;
        //
        timeLeft        = new ReadOnlyIntegerWrapper(time_secs);
        timeLeftDouble  = new ReadOnlyDoubleWrapper(time_secs);
        str_timeLeft.setValue(FormatHHMM(time_secs, showSecs) );
        //
        kv_zero            = new KeyValue(timeLeftDouble, 0) ;
        kv_duration        = new KeyValue(timeLeftDouble, this.duration) ;
        kf_start_COUNTDOWN = new KeyFrame( Duration.seconds( this.duration ), kv_zero  ) ;
        kf_stop_COUNTDOWN  = new KeyFrame( Duration.seconds( 0.0 ),           kv_duration ) ;
        kf_start_COUNTUP   = new KeyFrame( Duration.seconds( this.duration ), kv_duration  ) ;
        kf_stop_COUNTUP    = new KeyFrame( Duration.seconds( 0.0 ),           kv_zero ) ;
        //
        if (this.countUP)  { timeline = new Timeline(  kf_start_COUNTUP,  kf_stop_COUNTUP ) ; } 
        else               { timeline = new Timeline(  kf_stop_COUNTDOWN, kf_start_COUNTDOWN ) ; } ;
        //
        //  When value changes, this listener is called. 
        //
        timeLeftDouble.addListener(new InvalidationListener() 
            {
                @Override public void invalidated(Observable o) 
                {   
                    if(qd.GameType == qd.GAMETYPE_BILLIARDS_TIMED)
                    {
                        int newSecs = (int) Math.ceil(timeLeftDouble.get());
                        //
                        //  Check that at least one whole second has elapsed since the last call. 
                        //  If not then IGNORE the rest.
                        if ( newSecs != savedSecs )  
                        {
                            timeLeft.set(newSecs) ;
                            savedSecs = newSecs ;       //  Save the new value.
                            //  Note the leading space - better for cosmetics !
                            str_timeLeft.setValue(" " + FormatHHMM( newSecs, showSecs ) ) ;  
                            //
                            //  The countdown display. 
                            //  If 10 or less seconds left, then call the CountDown method.
                            if (savedSecs <= qd.COUNTDOWN_TIME)  {  QB_Qscreen_Controller.CountDown( newSecs );  }
                        } 
                    } ; 
               } ; 
            } ) ;
    }
    //
    //  Initialse the timer
    //  This is called each time a match is (re)loaded
    //
    public int init( final int time_secs, boolean countUP, boolean showSecs)
    {
        this.timeline.stop();        
        this.duration = time_secs ;
        this.countUP  = countUP ;
        this.showSecs = showSecs ;
        this.savedSecs = 0 ;
        this.timeLeft.set( time_secs );
        this.timeLeftDouble.set( time_secs );
        this.str_timeLeft.setValue(FormatHHMM(time_secs, showSecs) );
        //
        kv_duration        = new KeyValue(timeLeftDouble, this.duration) ;
        kf_start_COUNTDOWN = new KeyFrame( Duration.seconds( this.duration ), kv_zero  ) ;
        kf_stop_COUNTDOWN  = new KeyFrame( Duration.seconds( 0.0 ),           kv_duration ) ;
        kf_start_COUNTUP   = new KeyFrame( Duration.seconds( this.duration ), kv_duration  ) ;
        // kf_stop_COUNTUP    = new KeyFrame( Duration.seconds( 0.0 ),           kv_zero ) ;
        //
        if (this.countUP)  { timeline = new Timeline(  kf_start_COUNTUP,  kf_stop_COUNTUP ) ; } 
        else               { timeline = new Timeline(  kf_stop_COUNTDOWN, kf_start_COUNTDOWN ) ; } ;
        //
        return 1 ;
    }
    //
    //  Timer control methods
    //
    public void start()     { timeline.playFromStart(); running = true ; }
    public void stop()      { timeline.stop();          running = false ; } 
    public void restart()   { timeline.play();          running = true  ;}
    public void pause()     { timeline.pause();         running = false ; }
      //
      //   Method to convert from secs to formatted time spec - "hh:mm" or "hh:mm.ss"
      //
    public String FormatHHMM( int secs, boolean includeSecs )
    {
        StringBuilder  sb = new StringBuilder();
        int hh = 0, mm = 0, ss = 0;
        String s_mm = "" , s_ss = "" , s_hh = "" ;
        // sb.delete(0, 1000) ;
        //
        hh = secs / 3600 ;
        mm = (secs / 60) - (hh * 60) ;
        s_hh = Integer.toString(hh) ;       //  Convert into string
        s_mm = Integer.toString(mm) ;
        if (s_hh.length() < 2)  { s_hh = '0' + s_hh ; } ; //  Prepend '0' for single digits
        if (s_mm.length() < 2)  { s_mm = '0' + s_mm ; } ;
        //
        sb.append(s_hh); sb.append(":"); sb.append(s_mm) ;
        //
        if (includeSecs) 
        { 
            ss = secs - ((hh*3600) + (mm*60)) ; 
            s_ss = Integer.toString(ss) ;
            //  Prepend '0' for single digit
            if (s_ss.length() < 2)  { s_ss = '0' + s_ss ; } ;
            sb.append("."); sb.append(s_ss) ;
        } 
        //
        return sb.toString() ;
    }
}
