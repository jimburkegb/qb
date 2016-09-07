-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pMatchInfo_SELECT` ;
-- 
DELIMITER $$

CREATE  PROCEDURE qb.`pMatchInfo_SELECT`( IN prmMACADDR CHAR(16) )
BEGIN
-- 
--  Get all match info for the specified qb scoreboard.
--  ---------------------------------------------------
--
--  The main table is the qb.
-- 
    --
    CALL qb.pLog( 'pMatchInfo_SELECT> Getting match data for device with MAC address - ' , 0) ;   --  DEBUG/log
    CALL qb.pLog(  prmMACADDR, 0) ;   --  DEBUG/log
    -- 
    --  SP to SELECT all info for a particular MATCH
    --  --------------------------------------------
    -- 
    --  The main table which drives this is qb.match. This contains the ID of the match(es) to be
    --  scored. From this, the frames, player names, ref, etc. is gathered and returned.
    --
    --  If the timelimit is non-zero then assume that this is a TIMED billiards match,
    --  else it is a FRAMES billiards.
    --
    SELECT  T.name                AS   'tournament', 
            M.datestart           AS   'date',
            M.player1_user_id     AS   'p1_ID' , 
            M.player2_user_id     AS   'p2_ID' ,
            U1.playername         AS   'p1_name', 
            U2.playername         AS   'p2_name',
            U1.NationalFlag       AS   'p1_flag', 
            U2.NationalFlag       AS   'p2_flag',
            RS.Description        AS   'round',
            S.name                AS   'session',
            M.`table`             AS   'table',
--          M.name                AS   'match',
            R.name                AS   'match',
            M.ID                  AS   'match_ID',
            M.`status`            AS   'matchstatus',
            F.ID                  AS   'frame_ID',
            F.`status`            AS   'framestatus',
            M.numframes           AS   'numframes',
            FF.TimeLimitMins      AS   'duration',
            FF.PointsLimit        AS   'pointslimit',
            QBM.referee_name      AS   'referee_name',
            QBM.referee_flag      AS   'referee_flag',
            CASE M.NumFrames WHEN 1 
                THEN CONCAT( 'Format: ', FF.Comments )
                ELSE CONCAT( 'Format: ', FF.Comments, ' (best of ', M.NumFrames, ')' )  END
                                  AS  'matchtype',
            CASE FF.TimeLimitMins WHEN 0 THEN 2 ELSE 1 END
                                  AS  'gametype'            
    FROM    qb.`rpi`              QB
    JOIN    qb.`match`            QBM  ON  QBM.MACADDR  = prmMACADDR
    JOIN    cuesports.`match`     M    ON  M.ID         = QBM.Match_ID
    JOIN    cuesports.frame       F    ON  F.match_ID   = M.ID
    JOIN    cuesports.frameformat FF   ON  FF.ID        = F.FrameFormat_ID
    JOIN    cuesports.tournament  T    ON  T.ID         = M.tournament_id
    JOIN    cuesports.user        U1   ON  U1.ID        = M.player1_user_id
    JOIN    cuesports.user        U2   ON  U2.ID        = M.player2_user_id
    LEFT JOIN cuesports.session   S    ON  S.ID         = M.session_id
    JOIN    cuesports.round       R    ON  R.ID         = M.Round_ID
    JOIN    cuesports.roundstage  RS   ON  RS.ID        = R.RoundStage_ID
    WHERE   QB.MACADDR = prmMACADDR 
     AND    M.status IN (300, 302, 303)     --  Active / Not started / not populated
     ORDER BY M.DateStart ASC   ;           --  Most 'imminent' first
    --
    CALL qb.pLog( 'pMatchInfo_SELECT> Match info returned', 0 ) ;   --  DEBUG/log
    --  
END
