/*
 *  Task to handle scoring to the db.
 */
package QB;
//
import QB.Qdata ;
import QB.qboard ;
import QB.QB_Qscreen_Controller;
import static QB.qboard.dbg;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
//import static QB.qboard.qd;
//
import javafx.concurrent.Task;

/**
 *
 */
public class dbScorer  extends Task<Integer> 
{
    public Connection conn = null;
    public Statement req = null;
    public ResultSet res = null;
    //
    dbScorer(int i) 
    {
    } ;

    @Override
    protected Integer call(  )  throws Exception 
    {  
        return 1 ;
    } ;
    //
    public Integer setup( String svr, String usr, String pw )  throws Exception 
    {
         //load jdbc driver for mysql database
        try  {  Class.forName("com.mysql.jdbc.Driver"); } 
        catch(Exception e) { dbg.log("db", "Unable to load (jdbc) driver. Exception = " + e.getMessage() );  } 
        //
        try 
        {
            this.conn = DriverManager.getConnection( svr, usr, pw );           
        } catch (SQLException e) 
        { 
            dbg.log("db", "Unable to connect to database. Exception = " + e.getMessage());
            e.printStackTrace(); 
        }
       
        return 1 ;
    } ;
    //
    //
    public ResultSet execute( String statement )  throws Exception 
    {
        if( conn != null ) 
        {
            try 
            {   req = conn.createStatement(); } 
            catch (SQLException e) { dbg.log("db", "execute - Exception = " + e.getMessage()); }
            //
            try 
            {
                res = req.executeQuery( statement ) ;
            } 
            catch (SQLException ex) {  dbg.log("db", "execute - Exception = " + ex.getMessage()); }
        } else
        {
             dbg.log("db", "ERROR - no connection");
        }
        return res ;
    } ;
    //
    //  This expects ONE output parameter, returning a STRING.
    //
    public String executeGetBreaks( String query, int p1, int p2, int p3, int p4, Boolean p5 )  throws Exception 
    {
        String res = "" ;
        CallableStatement stmt = null;
        //
        if( conn != null ) 
        {
            try 
            {   
                stmt = conn.prepareCall( query );
                stmt.setInt(1, p1);
                stmt.setInt(2, p2);
                stmt.setInt(3, p3);
                stmt.setInt(4, p4);
                stmt.setBoolean(5, p5);                

                stmt.registerOutParameter( 6, java.sql.Types.VARCHAR ) ;
            } 
            catch (SQLException e) { dbg.log("db", "prepare - Exception = " + e.getMessage()); }
            //
            try 
            {
                stmt.executeUpdate();
                res = stmt.getString( 6 );      //  Output param is no 6.
            } 
            catch (SQLException ex) {  dbg.log("db", "execute - Exception = " + ex.getMessage()); }
        } else  {   dbg.log("db", "ERROR - no connection");  }
        //
        return res ;
    } ;
    //
    //
    public int executeUpdate( String statement )
    {
        int status = 0 ;
        if( conn != null ) 
        {
            try 
            {
                req = conn.createStatement();
            } 
            catch (SQLException e) 
            {   dbg.log("db", "executeUpdate. Exception = " + e.getMessage()); }
            //
            try 
            {
                status = req.executeUpdate( statement ) ;
                status = 1 ;
            } 
            catch (SQLException ex) {  dbg.log("db", "executeUpdate. Exception = " + ex.getMessage()); }
        } else
        {
             dbg.log("db", "executeUpdate - ERROR - no connection");
        }
        return status ;
    } 
    //
    //
    public int executeTidy( int Level, int frame_ID, boolean TellServer )
    {
        int status = 0 ;
        String statement = "" ;
        statement = "CALL qb.pTidy(" + Level + ", " + frame_ID + ", " + TellServer + " ) ;";
        //
        dbg.log("db", "executeTidy. statement = " + statement);
        //
        if( conn != null ) 
        {
            try 
            {
                req = conn.createStatement();
            } 
            catch (SQLException e) 
            {   dbg.log("db", "executeTidy. Exception = " + e.getMessage()); }
            //
            try 
            {
                status = req.executeUpdate( statement ) ;
                status = 1 ;
                dbg.log("db", "executeTidy. Tidied - level = " + Level); 
            } 
            catch (SQLException ex) {  dbg.log("db", "executeTidy. Exception = " + ex.getMessage()); }
        } else
        {
             dbg.log("db", "executeTidy - ERROR - no connection");
        }
        return status ;
    } 
    //
    public void close()
    {
    	try  {  res.close();   }  catch ( SQLException ex) { dbg.log( "db close", "resultset. Exception = "  + ex.getMessage()); } ;
    	try  {  req.close(); }    catch ( SQLException ex) { dbg.log( "db close", "request. Exception = "    + ex.getMessage()); } ;
    	try  {  conn.close(); }   catch ( SQLException ex) { dbg.log( "db close", "connection. Exception = " + ex.getMessage()); } ;           
    }


}
