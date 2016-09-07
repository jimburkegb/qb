/*
 * 
 */
package QB;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
 /*
 *  Class for handling binding the ObservableProperty to the String attribute of a screen value.
 */
public class sScreenValue extends SimpleStringProperty
{
    ObservableValue<? extends String>  sValue ;
    //
    //  CONSTRUCTORs
    sScreenValue(ObservableValue val) { sValue = val  ; }
    //
    //  UPDATE
    public void update( String val )  { this.setValue( val );  }
}