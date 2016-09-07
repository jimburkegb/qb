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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
//
//  !!!!!!!!!!!!!!!!!!!!!!!!!!!!  TIDY THIS UP !!!!!!!!!!!!!!!!!!!!!!!!
//
public class Transition 
{
    private ReadOnlyIntegerWrapper timeLeft;
    private ReadOnlyDoubleWrapper  timeLeftDouble;
    private SimpleStringProperty   str_timeLeft = new SimpleStringProperty("????");
    public  Timeline               timeline;
    public  boolean                running = false ;
    //
    public int duration = 0;
    public boolean transitUP = true ;
    //
    //  Constructor
    //      transitUP -   Transit UP or DOWN
    //
    public Transition( boolean transitUP  ) 
    {
        this.transitUP  = transitUP ;
        //
        timeLeft       = new ReadOnlyIntegerWrapper( 0 );
        timeLeftDouble = new ReadOnlyDoubleWrapper( 0 );
        str_timeLeft.setValue( "0" );

        if (this.transitUP)       //  It's a transition UP the way (Player 2 to Player 1)
        {
            this.timeline =   new Timeline(
                                new KeyFrame(   Duration.seconds( this.duration ),          
                                                new KeyValue(timeLeftDouble, this.duration) ),
                                new KeyFrame(   Duration.ZERO, 
                                                new KeyValue(timeLeftDouble, 0) )
            );
        }
        else                    //   It's a transition DOWN the way (Player 1 to Player 2)
        {
            this.timeline =  new Timeline(
                                new KeyFrame(   Duration.ZERO,          
                                                new KeyValue(timeLeftDouble, this.duration) ),
                                new KeyFrame(   Duration.seconds( this.duration ), 
                                                new KeyValue(timeLeftDouble, 0) )
            );
        }
        //  Hook up the callback for timer expiry
        //
        //  When value changes, this listener is called
        //
        timeLeftDouble.addListener(new InvalidationListener() 
            {
                @Override public void invalidated(Observable o) 
                {     
                    timeLeft.set((int) Math.ceil(timeLeftDouble.get()));
                    str_timeLeft.setValue( timeLeft.getValue().toString() ) ;                
                } ;
            }
        );
    }
    //
    //  Initialse the timer
    //
    public int init( final int time )
    {
        timeline.stop();
        running = false ;
        timeLeft.set( 0 );
        timeLeftDouble.set( 0 );
        str_timeLeft.setValue( "0" );
        return 1 ;
    }
    //
    //  Timer control methods
    //
    public void start( boolean transitUP )     { this.transitUP = transitUP ; timeline.playFromStart(); running = true ; }
    public void stop()      { timeline.stop();          running = false ; } 
    public void restart()   { timeline.play();          running = true ; }
      //
}
