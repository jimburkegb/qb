/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;
//
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.util.Duration;
//
//  This timer handles counters and timers which have no screen display.
//  --------------------------------------------------------------------
//
public class TimeCounter 
{
    public  Timeline               timeline;
    public  boolean                running = false ;
    private ReadOnlyDoubleWrapper  timeLeftDouble;
    //
    public int duration = 0;
    public boolean countUP = true ;
    public boolean showSecs = true ;
    //
    //  Constructor
    //      time -      the time to count up.down from, in MILLISECS
    //      countUP -   sic
    //      showSecs -  show the seconds  [NOT USED]
    //
    public TimeCounter(final int time_msecs, boolean countUP , boolean showSecs ) 
    {
        this.duration = time_msecs ;
        this.countUP  = countUP ;
        timeLeftDouble = new ReadOnlyDoubleWrapper(time_msecs);
        //
        if (this.countUP)       //  It's a count UP timer
        {
            this.timeline =   new Timeline(
                                new KeyFrame(   Duration.millis(this.duration ),          
                                                new KeyValue(timeLeftDouble, this.duration) ),
                                new KeyFrame(   Duration.ZERO, 
                                                new KeyValue(timeLeftDouble, 0) )
            );
        }
        else                    //  It's a count DOWN timer
        {
            this.timeline =  new Timeline(
                                new KeyFrame(   Duration.ZERO,          
                                                new KeyValue(timeLeftDouble, this.duration) ),
                                new KeyFrame(   Duration.millis(this.duration ), 
                                                new KeyValue(timeLeftDouble, 0) )
            );
        }
    }
    //
    //  Initialse the timer
    //
    public int init( final int time )
    {
        timeline.stop();
        running = false ;
        return 1 ;
    }
    //
    //  Timer control methods
    //
    public void start()     { timeline.playFromStart(); running = true ; }
    public void stop()      { timeline.stop();          running = false ; } 
    public void restart()   { timeline.play();          running = true ; }
    public void pause()     { timeline.pause();         running = false ; }
      //
}
