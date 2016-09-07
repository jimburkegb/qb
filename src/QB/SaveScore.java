/*
 * Class for handling the score of the current match.
 */
package QB;

import static QB.qboard.qd;
import java.sql.ResultSet;
import QB.qboard ;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jim
 */
public class SaveScore
{
    db qbdb_local ;
    Date LastScoreRecordedTime ;
    int Current_Player_ID ;
    int Current_Player_Break ;
    String  StaticParams ;
    //
    public SaveScore( String connectionString, String usr, String pw  ) // throws Exception
    {
//      qbdb_local = new db( connectionString, usr, pw ) ;
        int status = 0;
        try
        {
            qboard.Scorer.setup( connectionString, usr, pw ) ;
        } catch (Exception ex)
        {
            Logger.getLogger(SaveScore.class.getName()).log(Level.SEVERE, null, ex);
        }
         status = 1 ;
         //
         //  Now assemble the 'static' params into a string. This is for efficiency of the main scoring query string (see record method below)
         //
         StaticParams = "" ;
         StaticParams += ""  + qd.Player1ID  + ", " ;
         StaticParams += ""  + qd.Player2ID  + ", " ;
         StaticParams += ""  + qd.MatchID + ", " ;
         StaticParams += ""  + qd.FrameID + ", " ;
         StaticParams += "'" + qd.piMACADDR + "', " ;
         StaticParams += ""  + qd.UsingServerConfig + ") " ; //  Terminate the query
         //
    }
    //
    public int record( int Player_ID, int Score, int Break, int ShotType, String Comments, int visits )
    {
        StringBuilder sb = new StringBuilder(1000);
        int status = 1, p1, p2;
        //
        this.Current_Player_ID = Player_ID ;
        this.Current_Player_Break = Break;
        //
        // Check if the players have been swapped around.
        if (qd.PlayersSwapped)
        {   p1 = qd.Player2Score.getIntVal() ;
            p2 = qd.Player1Score.getIntVal() ;
        } else
        {   p1 = qd.Player1Score.getIntVal() ;
            p2 = qd.Player2Score.getIntVal() ;
        }
        //
        qboard.dbg.log( "db record", "Player_ID "+ Player_ID + ", score " + Score + ", break = " + Break + ", use srv = " + qd.UsingServerConfig );
        //
        sb.append("CALL qb.pScore_INSERT( ") ;
        sb.append(Player_ID) ;                  sb.append(", ") ;
        sb.append(Score) ;                      sb.append(", ") ;
        sb.append(Break) ;                      sb.append(", ") ;
        sb.append(ShotType) ;                   sb.append(", ") ;
        sb.append("'") ; sb.append(Comments) ;  sb.append("', ") ;
        sb.append( p1 );                        sb.append(", ") ;
        sb.append( p2 );                        sb.append(", ") ;
        sb.append(visits) ;                     sb.append(", ") ;
        sb.append( StaticParams ) ;     //  For processing efficiency, the following params are preconstructed
        //
        qboard.dbg.log( "db record", "Issuing query - " + sb.toString());
        //
        ResultSet res = null ;
//        res = qbdb_local.execute( sb.toString() ) ;
        try
        {
            res = qboard.Scorer.execute( sb.toString() ) ;

        } catch (Exception ex)  {  Logger.getLogger(SaveScore.class.getName()).log(Level.SEVERE, null, ex); } ;
        //
        if (res == null)
        {
            status = qd.ERR_NO_DB_CONNECTION ;
            qboard.dbg.log( "db record", "ERROR - no database connection possible !");
            return status ;  //  <<<<<<---------------------
        }
        //
        this.LastScoreRecordedTime = new Date() ;  //  Record the time of this score
        //
        qboard.dbg.log( "db record", "Success!");
        //
        return status ;
    }
    //
    public String getBreaks( int PlayerID, int BreaksListVerbose )
    {
        // StringBuilder sb = new StringBuilder(1000);
        String BreaksList = "" ;
        //
        qboard.dbg.log( "db getBreaks", "Player_ID = " + PlayerID );
        //
        try
        {
            BreaksList = qboard.Scorer.executeGetBreaks( "CALL qb.pGetBreaks( ?, ?, ?, ?, ?, ? ) ", 
                                                    BreaksListVerbose, 
                                                    qd.FrameID, 
                                                    PlayerID, 
                                                    qd.BreaksLimit, 
                                                    qd.UsingServerConfig ) ;

        } catch (Exception ex)  {  Logger.getLogger(SaveScore.class.getName()).log(Level.SEVERE, null, ex); } ;
        //
        if (BreaksList == null)
        {
            // status = qd.ERR_NO_DB_CONNECTION ;
            qboard.dbg.log( "db getBreaks", "ERROR - no breaklist returned !");
            return "" ;  //  <<<<<<---------------------
        }
        //
        qboard.dbg.log( "db getBreaks", "Success  - breaks list = " + BreaksList );
        //
        return BreaksList ;
    }
    //
    //
    //  CLEAR the current scores in the current frame
    //
    public int initScores(  )
    {
        int status = 0;
        //  Level 3 TIDY !!!!
        String st = "CALL qb.pTidy( 3, " + qd.FrameID + ", " + qd.UsingServerConfig + " ) ;" ;
        qboard.dbg.log( "initScores", "Clearing out scores");
        //
        this.Current_Player_ID = 0 ;
        this.Current_Player_Break = 0;
        //
        ResultSet res = null ;
        try
        {
            res = qboard.Scorer.execute( st) ;
        } catch (Exception ex) { qboard.dbg.log( "initScores", "Exception - ex = " + ex.getMessage()); }
        //
        if (res == null)
        {
            status = qd.ERR_NO_DB_CONNECTION ;
            qboard.dbg.log( "initScores", "ERROR - no database connection possible !");
            return status ;  //  <<<<<<---------------------
        } else { status = 1 ; }
        //
        this.LastScoreRecordedTime = null ;  //  clear the time of this score
        //
        qboard.dbg.log( "initScores", "Success!");
        //
        return status ;
    }
    //
    //  Correct the last scoring shot.
    //  ------------------------------
    //  This can happen multiple times - it just reverses one shot and clears it.
    //  Note that you cannot do a correction if there is none to correct !
    //  Return the ID of the player - IN PLAY. ie. the last scorer.
    //  Also, note that there is a timeout factor: if the last recorded score is older that 'n' mins,
    //  then NO CORRECTION is permitted.
    //
    public int correct( )
    {
        int status = 0;
        int p, p1, p2, t1, t2, v ;
        String st ;
        Date  CorrectionTime  = new Date() ; //  Get the current time for comparison against last score recorded.
        //
        //  Have corrections been disabled due to time elapsed ?
        long msDiff = CorrectionTime.getTime() - this.LastScoreRecordedTime.getTime() ;
        if ( msDiff  > qd.CORRECTION_TIMEOUT )
        {
            status = qd.ERR_CORRECTION_TIMEOUT ;
            qboard.dbg.log( "db correct", "No correction allowed - time limit exceeded");
            return status ;  //  <<<<<<---------------------
        }
        //
        st = "CALL qb.pScore_CORRECT( " + qd.FrameID + ", " + qd.UsingServerConfig + ")";
        //
        qboard.dbg.log( "db correct", "Issuing query - " + st);
        //
        ResultSet res = null ;
//        res = qbdb_local.execute( st ) ;
        try {
            res = qboard.Scorer.execute( st) ;
        } catch (Exception ex)
        {
            status = qd.ERR_EXCEPTION ;
            qboard.dbg.log( "db correct", "Exception - " + ex.getMessage());
            return status ;  //  <<<<<<---------------------
        }
        //
        if (res == null)
        {
            status = qd.ERR_NO_DB_CONNECTION ;
            qboard.dbg.log( "db correct", "ERROR - no database connection possible !");
            return status ;  //  <<<<<<---------------------
        }
        //  The latest score is returned.
        try
        {
            if ( !res.next())        //  Test if we have ZERO rows returned. Means that there is no score / break.
            {
                qboard.dbg.log( "db correct", "No data to return!");
                qd.Player1Score.update( 0);
                qd.Player2Score.update( 0);
                qd.PlayerBreak.update( 0 );
                this.Current_Player_Break = 0 ;
                this.Current_Player_ID = 0 ;
                status = 1 ;
            } else
            {
                qboard.dbg.log( "db correct", "Retrieving corrected score from resultset...");
                //
                try
                {
                    this.Current_Player_Break = Integer.parseInt(res.getString(1) ) ;   //  current Break
                    this.Current_Player_ID    = Integer.parseInt(res.getString(6)) ;    //  current Player ID (last shot)
                    p1 = Integer.parseInt(res.getString(2)) ;                           //  Player1 ID
                    p2 = Integer.parseInt(res.getString(3)) ;                           //  Player2 ID
                    t1 = Integer.parseInt(res.getString(4)) ;                           //  Player1 total
                    t2 = Integer.parseInt(res.getString(5)) ;                           //  Player2 total
                    v  = Integer.parseInt(res.getString(7)) ;                           //  visit number
                    status = 1 ;  //  SUCCESS
                    //
                    qd.PlayerVisits.intValue = v ;  // Reset the visit number back.
                    //
                    if (qd.PlayersSwapped)          //  We need to do this in case Spot and Plain
                    {                               //  have been switched.
                        qd.Player1Score.update( t2 );
                        qd.Player2Score.update( t1 );
                    } else
                    {
                        qd.Player1Score.update( t1 );
                        qd.Player2Score.update( t2 );
                    }
//                    if (p1 == qd.Player1ID)         //  We need to do this in case Spot and Plain
//                    {                               //  have been switched.
//                        qd.Player1Score.update( t1 );
//                        qd.Player2Score.update( t2 );
//                    } else
//                    {
//                        qd.Player1Score.update( t2 );
//                        qd.Player2Score.update( t1 );
//                    }
                } catch (SQLException e)
                    {
                        qboard.dbg.log( "db correct", "ERROR - cannot get db access - exception = " + e.getMessage());
                        status = qd.ERR_NO_DB_ACCESS ; //  <<<<<<---------------------
                    }
            }
        } catch (SQLException ex)
            {
                qboard.dbg.log( "db correct", "ERROR - no data returned !  Exception = " + ex.getMessage());
                status = qd.ERR_NO_DB_DATA_EX ;  //  <<<<<<---------------------
            }
        //
        qboard.dbg.log( "db correct", "Success!");
        //
        return status ;
    }
}
//

