-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pGetScore` ;
-- 
DELIMITER $$
-- 
CREATE  PROCEDURE qb.`pGetScore`(   IN prmLevel     INT, 
                                    IN prmObject_ID  INT , 
                                    IN prmMinBreak  INT )
BEGIN
DECLARE done            INT DEFAULT FALSE;
DECLARE vBreaks, vP1_breaks, vP2_breaks     VARCHAR(500) ;
DECLARE vLen, vLastID   INT ;
DECLARE vTemp, vBreak, vShottype, vShotnumber, vPreviousBreak, vPreviousShottype, vMatch_id INT ;
DECLARE vP1_name, vP2_name, vStatusDesc  VARCHAR(100) ;
DECLARE vP1_ID, vP2_ID, vP1_score, vP2_score, vPinp_ID, vPinp_score, vPinp_break, vVisits, vStatus INT ;
-- 
DECLARE curNextScore CURSOR FOR SELECT  `break`,  `shottype` ,  `shotnumber` 
                                FROM    tmpScores 
                                ORDER BY `shotnumber` ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE; 
    -- 
    --  Get the score of the frame.
    --  ----------------------------------------------------
    --  level = 1 :  get Score, including status of frame (prmObject_ID)
    --        = 2 :  get full scoreline of frame (prmObject_ID)
    --        = 3 :  get latest scores of tournament (prmObject_ID)
    -- 
    CALL qb.pLog( 'pGetScore> Object ID = ', prmObject_ID ) ;   --  DEBUG/log
    --
    CASE prmLevel   
       --
       WHEN 1  THEN     --  **********************************************************
                        --                     ****  Latest score - brief format  ****
                        --                     ***************************************
                        --                        
                        --  Get the ID of the last record for this frame in the RESULTS table.
                    SELECT MAX(ID) INTO vLastID FROM qb.qb_scores WHERE frame_id = prmObject_ID ;
                    --
                    SELECT  Q.match_ID, Q.frame_ID, M.name,
                            U1.playername AS Player1, Q.total_player1, ' - ', Q.total_player2, U2.playername AS Player2, 
                            S.description AS 'status' 
                    FROM    qb.qb_scores Q
                    JOIN    cuesports.user U1 ON U1.ID = Q.player1_ID
                    JOIN    cuesports.user U2 ON U2.ID = Q.player2_ID
                    JOIN    cuesports.frame  F ON F.id = Q.frame_ID
                    JOIN    cuesports.status S ON S.id = F.status
                    JOIN    cuesports.match  M ON M.id = Q.match_ID
                    WHERE   frame_id = prmObject_ID 
                    AND Q.ID = vLastID ;
                    --
        WHEN 2  THEN    --  **********************************************************
                        --                 ****  DETAILED results for the match   ****
                        --                 *******************************************
                        --
                    --                                        
                    --  Get the ID of the last record for this frame in the qb_scores table.
                    SELECT MAX(ID) INTO vLastID FROM qb.qb_scores WHERE frame_id = prmObject_ID ;
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
                            ,   M.status
                            ,   S.description
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
                            ,   vStatus
                            ,   vStatusDesc
                    FROM    qb.qb_scores Q
                    JOIN    cuesports.user U1 ON U1.ID = Q.player1_ID
                    JOIN    cuesports.user U2 ON U2.ID = Q.player2_ID
                    JOIN    cuesports.match M ON M.id  = Q.match_ID
                    JOIN    cuesports.status S ON S.id = M.status
                    WHERE   Q.frame_id = prmObject_ID 
                      AND   Q.ID = vLastID ;
                    --
                    --  Get list of breaks for each player.
                    CALL qb.`pGetBreaks`( 1, prmObject_ID, vP1_ID, prmMinBreak, 1, vP1_breaks) ; 
                    CALL qb.`pGetBreaks`( 1, prmObject_ID, vP2_ID, prmMinBreak, 1, vP2_breaks) ; 
                    --
                    --  Return the summary scoreline
                    SELECT      vP1_ID          AS player1_id
                            ,   vP1_name        AS player1_name
                            ,   vP1_score       AS player1_score
                            ,   vP1_breaks      AS player1_breaks
                            ,   vP2_ID          AS player2_id
                            ,   vP2_name        AS player2_name
                            ,   vP2_score       AS player2_score
                            ,   vP2_breaks      AS player2_breaks
                            ,   vVisits         AS visits
                            ,   vPinp_ID        AS current_player_id
                            ,   vPinp_break     AS current_player_break
                            ,   vShottype       AS shottype
                            ,   vStatus         AS 'status'
                            ,   IF (vStatus !=300, vStatusDesc, CONCAT( vStatusDesc, ' (', CAST(CURRENT_TIME as CHAR(8)), ')' ))
                                                AS 'statusDesc'
                            ;
       --
       WHEN 3  THEN     --  **********************************************************
                        --                     ****         Latest scores         ****
                        --                     ***************************************
                        --  Get the latest scores for all matches active or complete.
                        --
                        SET @counter = 0 ;
                        SELECT      @counter := @counter+1 AS ID
                                ,   M.table             AS 'table'
--                              ,   F.name              AS frameName
                                ,   R.name              AS 'round'
                                ,   U1.playername       AS player1_name
                                ,   F.pointsPlayer1     AS player1_score
                                ,   F.pointsPlayer2     AS player2_score
                                ,   U2.playername       AS player2_name
                                ,   S.description       AS statusDesc
                                ,   M.status            AS 'status'
                                ,   M.id                AS 'MID'
                        FROM    cuesports.frame F
                        JOIN    cuesports.match M ON M.id = F.match_id
                        JOIN    cuesports.user U1 ON U1.id = M.player1_user_ID
                        JOIN    cuesports.user U2 ON U2.id = M.player2_user_ID
                        JOIN    cuesports.status S ON S.id = M.status
                        JOIN    cuesports.round R ON R.id = M.round_id
                        WHERE   M.tournament_id = prmObject_ID 
                         AND    M.status NOT IN (302, 303) ;
                    --
       ELSE  
                    CALL qb.pLog( 'pGetScore> ERROR - invalid level = ', prmLevel ) ;   --  DEBUG/log
       
    END CASE ;
    -- 
    CALL qb.pLog( 'pGetScore> Finished. level = ', prmLevel ) ;   --  DEBUG/log
    --
END


