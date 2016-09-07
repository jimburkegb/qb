/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QB;
//
import static QB.qboard.qd;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static sun.misc.ThreadGroupUtils.getRootThreadGroup;
/**
 *
 * @author Jim
 */
public class debug 
{
    //  Switch debug messages on/off
    public Boolean debug_active = true ;
    public Boolean debug_log = false ;
    //
    private String dt, stmp, query ;
    private DateFormat dateFormat ;
    private Calendar cal ;
    private db qbdb_local ;
    private ResultSet res = null ;
    private int itmp ;
    //
    //
    //  Constructor (not used ?)
    debug() {  }
    //
    public void setQbdb_local( String ConnStr, String LocalUsername, String LocalPassword ) 
    {
        this.debug_log = true ;
        this.qbdb_local = new db( ConnStr, LocalUsername, LocalPassword ) ;
    }   
    //
    public void log ( String caller, String msg )
    {
        if (debug_active)
        {
            //  Construct timestamp
            
            this.dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            this.cal = Calendar.getInstance();
            this.dt = dateFormat.format( this.cal.getTime() );
            //  Log the message
            this.stmp = this.dt + " " + caller + "> " + msg ;
            System.out.println( this.stmp ) ;
            if (debug_log)
            {
                //  Log the message to the log table.
                //  Ensure it doesn't exceed the db column length
                itmp = msg.length() ;
                if (itmp > 400) itmp = 400 ;    //  400 chars should cover it. The field is declared as 512 anyway, but other data gets prepended.
                //
                this.query = "CALL qb.plog( \"" + caller + "> " + msg.substring(0, itmp)  + "\", -1) ;";
                int i = this.qbdb_local.executeUpdate( this.query ) ;
            }
        }
    }
    //
    public void  logThreads( ) 
    {
        //  NOT WORKING PROPERLY !!!
        //  ONLY SHOWING ONE THREAD (finalizer).
        if (debug_active)
        {
            final ThreadGroup root = getRootThreadGroup( );
            final ThreadMXBean thbean = ManagementFactory.getThreadMXBean( );
            int nAlloc = thbean.getThreadCount( );
            int n = 0, i=0;
            Thread[] threads;
            do {
                nAlloc *= 2;
                threads = new Thread[ nAlloc ];
                i++;
                n = root.enumerate( threads, true );
                log ("Thread " + i + ": " , threads[i].getName() ) ;
            } while ( n == nAlloc );
            //
        }
    }
    //   
}
