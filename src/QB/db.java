/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;

import static QB.qboard.dbg;
import java.sql.*;

/**
 *
 * @author Jim
 */
public class db 
{
    public Connection conn = null;
    public Statement req = null;
    public ResultSet res = null;
    //
    db( String svr, String usr, String pw )
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
        //
        //  return 1 ;
    }
    //
    public ResultSet execute( String statement )
    {
        if( conn != null ) 
        {
            try 
            {
                req = conn.createStatement();
            } 
            catch (SQLException e) 
            {   dbg.log("db", "execute. Exception = " + e.getMessage()); }
            //
            try 
            {
                res = req.executeQuery( statement ) ;
            } 
            catch (SQLException ex) {  dbg.log("db", "executeQuery. Exception = " + ex.getMessage()); }
        } else
        {
             dbg.log("db", "ERROR - no connection");
        }
        return res ;
    } 
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
