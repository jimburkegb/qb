/*
 *          ********************
 *          ****  Q Command  ***
 *          ********************
 * 
 *  The purpose of this module is to handle all the game-specific actions. For example, Billiards scoring, etc.
 *  The non-game-specific actions are handled by BTcontrol, IRcontrol, etc.
 */
package QB;
//
// import  QB.Qdata;
import static QB.qboard.qd;
/**
 *
 */
public class Qcommand 
{
    //
    public Qcommand() 
    {
        //
    }
    //
    //
    // ********************************************************************************
    //                                                  *****  BILLIARDS - TIMED  *****
    //                                                  *******************************
    //      Layout for Wiraka remote :-
    //
    //      Normal                      (1) RED
    //      ------                          unused
    //
    //              (2) YELLOW          (3) GREEN           (4) BROWN
    //                  2 Points            3 Points            unused
    //
    //              (5) BLUE            (6) PINK            (7) BLACK
    //                  unused              unused              unused
    //
    //      Advanced                    (1) RED
    //      --------                        Cannon [2]
    //
    //              (2) YELLOW          (3) GREEN           (4) BROWN
    //                  RED Pot [3]         unused              WHITE Pot [2]
    //
    //              (5) BLUE            (6) PINK            (7) BLACK
    //                  RED Loser [3]       unused              WHITE Loser [2]
    //
    public int exec_billiards_timed( String qCmd ) 
    {    
        int status = 0 ;
        //
        qboard.dbg.log( "exec_billiards_timed", qCmd); 
        //
        if (qCmd == null)  return qd.ERR_NULL_COMMAND ;
        //
        switch ( qCmd )
        {
            case "NUMBER_1":    // *****************  -1-  RED  ***********************************
                                //
                                if (qd.MatchInProgress)
                                {
                                    if (qd.AdvancedScoring==1) 
                                    {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts (cannon)", Qdata.SHOTTYPE_CANNON) ; } else
                                    {   /*  unused  */  }
                                } else
                                {   qd.MsgBox.show("Match not started", 2);    }
                                break ;     // 
                                //
            case "NUMBER_2":    // *****************  -2-  YELLOW  ***********************************
                                //
                                if (qd.MatchInProgress)
                                {
                                    if (qd.AdvancedScoring==1) 
                                    {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 3, "3 pts (pot red)", Qdata.SHOTTYPE_POTRED) ;  } else
                                    {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts",           Qdata.SHOTTYPE_SCORE) ;  }   
                                } else
                                {   qd.MsgBox.show("Match not started", 2);    }
                                break ;     // 
                                //
            case "NUMBER_3":    // *****************  -3-  GREEN  ***********************************
                                //
                                if (qd.MatchInProgress)
                                {
                                    if (qd.AdvancedScoring==1) 
                                    {   /*  unused   */  } else
                                    {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 3, "3 pts", Qdata.SHOTTYPE_SCORE) ;  }
                                } else
                                {   qd.MsgBox.show("Match not started", 2);    }
                                break ;     //  
                                //
            case "NUMBER_4":    // *****************  -1-  BROWN  ***********************************
                                //
                                if (qd.MatchInProgress)
                                {
                                    if (qd.AdvancedScoring==1) 
                                    {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts (pot baker)", Qdata.SHOTTYPE_POTBAKER) ; }else
                                    {   /*  unused  */  }   
                                } else
                                {   qd.MsgBox.show("Match not started", 2);    }
                                break ;     // 
                                //
            case "NUMBER_5":    // *****************  -1-  BLUE  ***********************************
                                //
                                if (qd.MatchInProgress)
                                {
                                    if (qd.AdvancedScoring==1) 
                                    {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 3, "3 pts (red loser)", Qdata.SHOTTYPE_INOFFRED) ; } else
                                    {   /*  unused  */  } 
                                } else
                                {   qd.MsgBox.show("Match not started", 2);    }
                                break ;     // 
                                //
            case "NUMBER_6":    // *****************  -1-  PINK  ***********************************
                                //  unused 
                                break ;  
                                //
            case "NUMBER_7":    // *****************  -1-  BLACK  ***********************************
                                //
                                if (qd.MatchInProgress)
                                {
                                    if (qd.AdvancedScoring==1) 
                                    {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts (baker loser)", Qdata.SHOTTYPE_INOFFBAKER) ; } else
                                    {   /*  unused  */  }
                                } else
                                {   qd.MsgBox.show("Match not started", 2);    }
                                break ;     // 
                                //
            case "FOUL":        // *****************  -FOUL-  Score 2 points to opponent  ****************
                                //
                                if (qd.MatchInProgress)
                                {
                                    QB_Qscreen_Controller.PenalisePlayer ( qd.PlayerAtTable, 2, "Foul - 2 pts" ) ;
                                } else
                                {   qd.MsgBox.show("Match not started", 2);    }                               
                                break ;     // 
                                //
            case "GAME":        // *****************  -GAME- Match  *************************
                                break ;     //  
                                //
            case "CLOCK":       // *****************  -CLOCK-  Start/stop CLOCK  *****************************************
                                //  Also Match start/pause
                                QB_Qscreen_Controller.ClockClicked() ;
                                break ;     // 
                                //
            case "NEXTPLAYER":  // *****************  ENTER - switch player  ****************************
                                //
                               //  end break (true) - next player  
                                QB_Qscreen_Controller.EndBreak( qd.PlayerAtTable, false ) ; 
                                break ;  //   
                                //
            default:
                qboard.dbg.log( "exec_billiards_timed", "qCmd = " + qCmd + " <--- ERROR - KEY NOT RECOGNISED ");       
        }           
        //
        return status ;
    }  
    //
    // *********************************************************************************
    //                                                  *****  BILLIARDS - FRAMES  *****
    //                                                  ********************************
    //
    public int exec_billiards_frames( String qCmd ) 
    {    
        int status = 0 ;
        //
        qboard.dbg.log( "exec_billiards_frames", qCmd); 
        //
        switch ( qCmd )
        {
            case "NUMBER_1":    // *****************  -1-  RED  ***********************************
                                //
                                if (qd.AdvancedScoring==1) 
                                {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts (cannon)", Qdata.SHOTTYPE_CANNON) ; } else
                                {   /*  unused  */  } 
                                break ;     // 
                                //
            case "NUMBER_2":    // *****************  -2-  YELLOW  ***********************************
                                //
                                if (qd.AdvancedScoring==1) 
                                {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 3, "3 pts (pot red)", Qdata.SHOTTYPE_POTRED) ;  } else
                                {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts",           Qdata.SHOTTYPE_SCORE) ;  }   
                                break ;     // 
                                //
            case "NUMBER_3":    // *****************  -3-  GREEN  ***********************************
                                //
                                if (qd.AdvancedScoring==1) 
                                {   /*  unused   */  } else
                                {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 3, "3 pts", Qdata.SHOTTYPE_SCORE) ;  }
                                break ;     //  
                                //
            case "NUMBER_4":    // *****************  -1-  BROWN  ***********************************
                                //
                                if (qd.AdvancedScoring==1) 
                                {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts (pot baker)", Qdata.SHOTTYPE_POTBAKER) ; }else
                                {   /*  unused  */  }   
                                break ;     // 
                                //
            case "NUMBER_5":    // *****************  -1-  BLUE  ***********************************
                                //
                                if (qd.AdvancedScoring==1) 
                                {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 3, "3 pts (red loser)", Qdata.SHOTTYPE_INOFFRED) ; } else
                                {   /*  unused  */  } 
                                break ;     // 
                                //
            case "NUMBER_6":    // *****************  -1-  PINK  ***********************************
                                //  unused 
                                break ;  
                                //
            case "NUMBER_7":    // *****************  -1-  BLACK  ***********************************
                                //
                                if (qd.AdvancedScoring==1) 
                                {   QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts (baker loser)", Qdata.SHOTTYPE_INOFFBAKER) ; } else
                                {   /*  unused  */  }
                                break ;     // 
                                //
            case "FOUL":        // *****************  -FOUL-  Score 2 points to opponent  ****************
                                //
                                QB_Qscreen_Controller.PenalisePlayer ( qd.PlayerAtTable, 2, "Foul - 2 pts" ) ;
                                break ;     // 
                                //
            case "GAME":        // *****************  -GAME- Match  *************************
                                break ;     //  
                                //
            case "CLOCK":       // *****************  -CLOCK-  Start/stop CLOCK  *****************************************
                                //  Also Match start/pause
                                // ?? 
                                break ;     // 
                                //
            case "NEXTPLAYER":  // *****************  ENTER - switch player  ****************************
                                //
                               //  end break (true) - next player  
                                QB_Qscreen_Controller.EndBreak( qd.PlayerAtTable, false ) ; 
                                break ;  //   
                                //
            default:
                qboard.dbg.log( "exec_billiards_frames", "qCmd = " + qCmd + " <--- ERROR - IR KEY NOT RECOGNISED ");       
        }           
        //        //
        return status ;
    }  
    //
    // ********************************************************************************
    //                                                  *****        SNOOKER      *****
    //                                                  *******************************
    //
    public int exec_snooker( String qCmd ) 
    {    
        int status = 0 ;
        //
        qboard.dbg.log( "exec_snooker", qCmd); 
        //
        switch ( qCmd )
        {
            case "NUMBER_1":    // *****************  -1-  RED      - Score 1 point   ***********************************    
                                //
                                QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "1 pt", Qdata.SHOTTYPE_POTRED) ;   
                                break ; 
            case "NUMBER_2":    // *****************  -2-  YELLOW - Score 2 points  ***********************************
                                //
                                QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 2, "2 pts", Qdata.SHOTTYPE_POTYELLOW) ;   
                                break ; 
            case "NUMBER_3":    // *****************  -3-  GREEN    - Score 3 points  ***********************************
                                //
                                QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 3, "3 pts", Qdata.SHOTTYPE_POTGREEN) ;   
                                break ; 
            case "NUMBER_4":    // *****************  -4-  BROWN    - Score 4 points  ***********************************
                                //
                                QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 4, "4 pts", Qdata.SHOTTYPE_POTBROWN) ;   
                                break ; 
            case "NUMBER_5":    // *****************  -5-  BLUE     - Score 5 points  ***********************************
                                //
                                QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 5, "5 pts", Qdata.SHOTTYPE_POTBLUE) ;   
                                break ; 
            case "NUMBER_6":    // *****************  -6-  PINK     - Score 6 points  ***********************************
                                //
                                QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 6, "6 pts", Qdata.SHOTTYPE_POTPINK) ;   
                                break ; 
            case "NUMBER_7":    // *****************  -7-  BLACK    - Score 7 points  ***********************************
                                //
                                QB_Qscreen_Controller.ScorePlayer( qd.PlayerAtTable, 7, "7 pts", Qdata.SHOTTYPE_POTBLACK) ;   
                                break ; 
            case "FOUL":        // *****************  -FOUL-  Score ??? points to opponent  ****************
                                //
                                QB_Qscreen_Controller.PenalisePlayer ( qd.PlayerAtTable, 4, "Foul" ) ;
                                break ;     // 
                                //
//            case "GAME":        // *****************  -GAME- Match  *************************
//                                //  Start / stop match
//                                //  If there is no score so far, then interpret this as a START MATCH. Otherwise it's a END MATCH.
//                                QB_Qscreen_Controller.BeginEndMatch();
//                                break ;     //  
                                //
            case "CLOCK":       // *****************  -CLOCK-  Start/stop CLOCK  *****************************************
                                //  Also Match start/pause
                                QB_Qscreen_Controller.ClockClicked() ;
                                break ;     //   
                                //
            case "NEXTPLAYER":  // *****************  ENTER - switch player  ****************************
                                //
                                //  end break (true) - next player  
                                QB_Qscreen_Controller.EndBreak( qd.PlayerAtTable, false ) ; 
                                // QB_Qscreen_Controller.SwitchPlayer() ;        //  end break - next player
                                break ;  //   
                                //
            default:
                qboard.dbg.log( "exec_snooker", "qCmd = " + qCmd + " <--- ERROR - IR KEY NOT RECOGNISED "); 
        }
        //
        status = -999 ;        //         UNSUPPORTED  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //
        return status ;
    }  
    //
    // ********************************************************************************
    //                                                  *****    POOL - 9 BALL    *****
    //                                                  *******************************
    //
    public int exec_pool_9ball( String qCmd ) 
    {    
        int status = 0 ;
        //
        status = -999 ;        //         UNSUPPORTED  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //
        return status ;
    }  
    //
    // ********************************************************************************
    //                                                  *****    POOL - ENGLISH   *****
    //                                                  *******************************
    //
    public int exec_pool_english( String qCmd ) 
    {    
        int status = 0 ;
        //
        status = -999 ;        //         UNSUPPORTED  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //
        return status ;
    }  
    //
    // ********************************************************************************
    //                                                  *****         CAROM       *****
    //                                                  *******************************
    //
    public int exec_pool_carom( String qCmd ) 
    {    
        int status = 0 ;
        //
        status = -999 ;        //         UNSUPPORTED  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //
        return status ;
    }  
    //
}
