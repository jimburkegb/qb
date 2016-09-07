/*
 * 
 */
package QB;

import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.beans.property.SimpleObjectProperty;

 /*
 *  Class for handling binding the ObservableProperty to the String attribute of a screen value.
 */
public class imScreenValue extends SimpleObjectProperty
{
    ObservableValue<? extends Image>  imValue ;
    //
    //  CONSTRUCTORs
    imScreenValue(ObservableValue val) { imValue = val  ; }
    //
    //  UPDATE
    public void update( String val )  {  this.setValue( val ); }
}