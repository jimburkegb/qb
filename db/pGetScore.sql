-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pGetScore` ;
-- 
DELIMITER $$
-- 
CREATE  PROCEDURE qb.`pGetScore`(   IN prmLevel     INT, 
                                    IN prmFrame_ID  INT , 
                                    IN prmMinBreak  INT )
BEGIN
DECLARE done                 INT DEFAULT FALSE;
DECLARE vBreaks  VARCHAR(1000) ;
DECLARE vLen, vLastID INT ;
DECLARE vTemp, vBreak, vShottype, vShotnumber, vPreviousBreak, vPreviousShottype INT ;
DECLARE vP1_name, vP2_name, vP1_breaks, vP2_breaks  VARCHAR(100) ;
DECLARE vP1_ID, vP2_ID, vP1_score, vP2_score, vPinp_ID, vPinp_score, vPinp_break, vVisits INT ;
DECLARE curNextScore CURSOR FOR SELECT  `break`,  `shottype` ,  `shotnumber` 
                                FROM    tmpScores 
                                ORDER BY `shotnumber` ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE; 
    -- 
    --  Get the score of the frame.
    --  ----------------------------------------------------
    --  level = 1 :  get Score, including status
    --        = 2 :  get full scoreline
    -- 
    CALL qb.pLog( 'pGetScore> Frame ID = ', prmFrame_ID ) ;   --  DEBUG/log
    --
    --  Get the ID of the last record for this frame in the qb_scores table.
    SELECT MAX(ID) INTO vLastID FROM qb.qb_scores WHERE frame_id = prmFrame_ID ;
    --  Now we can get the last shot type. Should be end-of-frame, but could be end-of-break.
    -- SELECT shottype INTO prmShotType FROM qb.qb_scores WHERE ID = vLastID ;
    --
    CASE prmLevel   
       --
       WHEN 1  THEN     --  **********************************************************
                        --                     ****  Latest score - brief format  ****
                        --                     ***************************************
                    SELECT  Q.ID, Q.match_ID, Q.frame_ID, 
                            U1.playername AS Player1, Q.total_player1, 
                            U2.playername AS Player2, Q.total_player2, 
                            Q.player1_ID, Q.player2_ID, 
                            Q.visit AS 'Visits',
                            IF ( Q.shottype = 101, 101, 100 ) AS 'status' 
                    FROM    qb.qb_scores Q
                    JOIN    cuesports.user U1 ON U1.ID = Q.player1_ID
                    JOIN    cuesports.user U2 ON U2.ID = Q.player2_ID
                    WHERE   frame_id = prmFrame_ID 
                    AND Q.ID = vLastID ;
                    --
        WHEN 2  THEN    --  **********************************************************
                        --                 ****  DETAILED results for the match   ****
                        --                 *******************************************
                        --
                        --  frame_id, p1_ID, P2_ID, p1_Score, p2_score, visits, p1_breaks, p2_breaks, current_break, current_player
                    CALL qb.pLog( 'pGetScore> Detailed score', 0 ) ;   --  DEBUG/log
                    --                   
                    SELECT      U1.playername 
                            ,   Q.player1_ID  
                            ,   Q.total_player1 
                            ,   Q.player2_ID 
                            ,   U2.playername  
                            ,   Q.total_player2  
                            ,   Q.visit   
                            ,   Q.player_ID
                            ,   Q.break 
                            ,   Q.shottype
                    INTO
                                vP1_name 
                            ,   vP1_ID 
                            ,   vP1_score 
                            ,   vP2_ID 
                            ,   vP2_name 
                            ,   vP2_score  
                            ,   vVisits
                            ,   vPinp_ID
                            ,   vPinp_break
                            ,   vShottype
                    FROM    qb.qb_scores Q
                    JOIN    cuesports.user U1 ON U1.ID = Q.player1_ID
                    JOIN    cuesports.user U2 ON U2.ID = Q.player2_ID
                    WHERE   Q.frame_id = prmFrame_ID 
                      AND   Q.ID = vLastID ;
                    --
                    --  Get list of breaks for each player.
                    CALL qb.`pGetBreaks`( 1, prmFrame_ID, vP1_ID, prmMinBreak, 1, vP1_breaks) ; 
                    CALL qb.`pGetBreaks`( 1, prmFrame_ID, vP2_ID, prmMinBreak, 1, vP2_breaks) ; 
                    --
                    --  Return the summary scoreline
                    SELECT      vP1_ID 
                            ,   vP1_name 
                            ,   vP1_score 
                            ,   vP1_breaks
                            ,   vP2_ID 
                            ,   vP2_name 
                            ,   vP2_score 
                            ,   vP2_breaks
                            ,   vVisits
                            ,   vPinp_ID
                            ,   vPinp_break
                            ,   vShottype ;
       ELSE  
                    CALL qb.pLog( 'pGetScore> ERROR - invalid level = ', prmLevel ) ;   --  DEBUG/log
       
    END CASE ;
    -- 
    CALL qb.pLog( 'pGetScore> Finished. level = ', prmLevel ) ;   --  DEBUG/log
    --
END


