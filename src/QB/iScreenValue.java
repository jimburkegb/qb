/*
 * 
 */
package QB;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
 /*
 *  Class for handling binding the ObservableProperty to the String attribute of a screen value.
 */
public class iScreenValue extends SimpleIntegerProperty
{
    //public int intValue = 0 ;
    ObservableValue<? extends Integer>  iValue ;
    //
    //  CONSTRUCTORs
    iScreenValue(ObservableValue val) { iValue = val  ; }
    //
    //  UPDATE
    public void update( Integer val ) 
    {      
        this.setValue( val ); 
    }
}
