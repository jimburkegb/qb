/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;
//
import static QB.qboard.qd;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.util.Duration;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
/**
 *
 *    Message Box class.
 *    This populates a message box with the specified message, and disappears after the specified seconds.
 * 
 * @author Jim
 */
public class MessageBox  extends SimpleStringProperty
{
    private ReadOnlyIntegerWrapper timeLeft;
    private ReadOnlyDoubleWrapper  timeLeftDouble;  
    public  SimpleStringProperty   text = new SimpleStringProperty("");
    //
    //  the background of the message box. Handle this with visibility.
    public  ObservableValue<? extends Boolean>   ov_msgbox_setVis    ; 
    public  bScreenValue  msgbox_setVis = new bScreenValue( ov_msgbox_setVis )   ;
    //
    public int      duration ;
    public String   msg ;
    public Timeline timeline ;
    public boolean  running = false ;
    //
    public int    timeLeft()                            {   return timeLeft.get();  }
    public ReadOnlyIntegerProperty timeLeftProperty()   {   return timeLeft.getReadOnlyProperty();  }
    public StringProperty msgProperty_fmt ()            {   return text ;   }
    //public ObservableValue visProperty_fmt ()           {   return msgbox_setVis ;   }
    //
    MessageBox ()
    {
        duration = 1 ;
        msg = "" ; 
    }
    //
    public void show( String msg, int secs )
    {
        //
        if (this.running) { this.unshow() ; }   //  Terminate any running message
        //
        //  A value of 0 for secs means - show indefinitely
        this.duration = secs ;
        this.msg = msg ;
        this.msgbox_setVis.update(true);
        this.text.setValue( msg ) ;
        //
        this.timeLeft       = new ReadOnlyIntegerWrapper(secs);
        this.timeLeftDouble = new ReadOnlyDoubleWrapper(secs);
        this.running = true ;
        //
        if ( secs > 0)
        {
            //  Set timer for the message's visibility.
            this.timeline =  new Timeline(                   
                                new KeyFrame(   Duration.seconds( this.duration ),          
                                                new KeyValue(timeLeftDouble, this.duration) ),
                                new KeyFrame(   Duration.ZERO, 
                                                new KeyValue(timeLeftDouble, 0) )
            );
            //  Timer expiry - set INvisible again.
            this.timeline.setOnFinished(new EventHandler<ActionEvent>() 
            {
                @Override
                public void handle(ActionEvent event)  {   unshow() ;   }  
            }   ) ; 
            //  Start the timer
            this.timeline.playFromStart();
        }       
    }
    //
    public void unshow()
    {
        this.timeline.stop(); 
        this.text.setValue( "" ) ;                  
        msgbox_setVis.update(false);
        this.duration = 0 ;      
        this.running = false ;
    }    
}
