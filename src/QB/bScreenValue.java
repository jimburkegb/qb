/*
 *  Class for handling binding to the boolean attribute of a screen value.
 *
 *  Took me ages to solve this problem !!!
 */
package QB;
//
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
/**
 *
 */
public class bScreenValue extends SimpleBooleanProperty
{
    ObservableValue<? extends Boolean>  bValue ;
    //
    //  CONSTRUCTORs
    bScreenValue(ObservableValue val) { bValue = val  ; }
    //
    //  UPDATE
    public void update( boolean val ) { this.setValue( val ) ; }
}



