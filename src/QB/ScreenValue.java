/*
 * 
 */
package QB;

import javafx.beans.property.SimpleStringProperty;
/**
 *  Class (extended from SimpleStringProperty for modifying value displayed on screen.
 *  Handles both string and int values. Both are treated as strings for display.
 * @author Jim
 */
public class ScreenValue extends SimpleStringProperty
{
    public int intValue = 0 ;
    public String strValue = "" ;
    //
    //  CONSTRUCTORs
    ScreenValue(String sVal)     { strValue  = sVal ; }
    ScreenValue(int iVal)        { intValue  = iVal ; }
    //
    //  UPDATEs
    public void update( String sVal )  
    {   strValue = sVal ;   this.setValue( sVal ); }
   
    public void update( int iVal )    
    {   intValue = iVal ;  this.setValue( String.valueOf( iVal ) );  }
   
    public int getIntVal(  )    
    {   return Integer.parseInt(this.getValue()) ; }
}

