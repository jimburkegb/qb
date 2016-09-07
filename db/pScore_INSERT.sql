-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pScore_INSERT` ;
-- 
DELIMITER $$
--
--  The parameter order here is optimised to allow the Java code to preconstruct much of
--  the parameter list.
--
CREATE  PROCEDURE qb.`pScore_INSERT`(
                        IN prmPlayer_ID     INT,
                        IN prmScore         INT,
                        IN prmBreak         INT,
                        IN prmShotType      INT,
                        IN prmComments      VARCHAR(45),
                        IN prmTotalPlayer1  INT,
                        IN prmTotalPlayer2  INT,
                        IN prmVisit         INT, 
                        IN prmPlayer1_ID    INT,
                        IN prmPlayer2_ID    INT,
                        IN prmMatch_ID      INT,
                        IN prmFrame_ID      INT,
                        IN prmMACADDR       CHAR(16),
                        IN use_server       INT  )
BEGIN
DECLARE vShotNumber    INT ;
DECLARE vLastInsertID  INT ;
DECLARE vTimeNow       DATETIME ;
-- 
--  This function creates a new SHOT record.
--
--  call qb.`pScore_INSERT`( '00112233AABBCCDD', 1, 3, 1234, 4321, 123, NULL, NULL, 0 ) ;
--  select * from qb.score ;
--  select * from qb.log ;
-- 
DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
    ROLLBACK;
    CALL qb.pLog( 'pScore_INSERT> ERROR...', -1 ) ;   --  DEBUG/log
END;
    -- 
    --  SP to CREATE a new entry in the SCORE table
    --  -------------------------------------------
    CALL qb.pLog( 'pScore_INSERT> Inserting score for for Frame ID: ', prmFrame_ID ) ;   --  DEBUG/log
    --
    --  Get the last Shot Number recorded. If an informational 'shot' then zero it. eg - Pause timer, etc.
    --  'informational' shots have a value of OVER 100.
    IF (prmShotType <= 100) THEN
        --
        SELECT IFNULL(MAX(shotnumber), 0) INTO vShotNumber        
            FROM    qb.score S
            WHERE   S.match_ID = prmMatch_ID 
              AND   S.frame_ID = prmFrame_ID ;
        SET vShotNumber = vShotNumber + 1 ;
    ELSE
        SET vShotNumber = 0 ;
    END IF ;
    -- 
    set vTimeNow = NOW() ;
    --
    START TRANSACTION  ;     --  <<<<---- TRANSACTION Active <<<<----
        --
        INSERT `qb`.`score`
        (
            MACADDR,
            player_ID,
            score,
            break,
            match_ID,
            frame_ID,
            comments,
            shottype,
            recorded,
            shotnumber,
            total_player1,
            total_player2,
            player1_ID,
            player2_ID,
            visit
        )
        VALUES
        (
            prmMACADDR,
            prmPlayer_ID,
            prmScore,
            prmBreak,
            prmMatch_ID,
            prmFrame_ID,
            COALESCE(prmComments, 'none'),
            COALESCE(prmShotType, 0),
            vTimeNow,
            vShotNumber,
            prmTotalPlayer1,
            prmTotalPlayer2,
            prmPlayer1_ID,
            prmPlayer2_ID,
            prmVisit
        ) ;
        SET vLastInsertID = LAST_INSERT_ID() ;  --  Note ID of that INSERT.
        --
        IF use_server = 1 THEN
            --  Fire this score up to the server. This just means - write it to the federated table 'qb_scores'.
            --
            -- CALL qb.pLog( 'pScore_INSERT> Sending score to server', prmFrame_ID ) ;   --  DEBUG/log
            INSERT `qb`.`qb_scores`
            (
                ID,
                MACADDR,
                player_ID,
                score,
                break,
                match_ID,
                frame_ID,
                recorded,
                comments,
                shottype,
                shotnumber,
                dispatched,
                total_player1,
                total_player2,
                player1_ID,
                player2_ID,
                visit
            )
            VALUES
            (
                vLastInsertID,
                prmMACADDR,
                prmPlayer_ID,
                prmScore,
                prmBreak,
                prmMatch_ID,
                prmFrame_ID,
                vTimeNow,
                COALESCE(prmComments, 'none'),
                COALESCE(prmShotType, 0),
                vShotNumber,
                NOW(),
                prmTotalPlayer1,
                prmTotalPlayer2,
                prmPlayer1_ID,
                prmPlayer2_ID,
                prmVisit
            ) ;
            --  Remember to update the 'recorded' flag since we've been successful.
            UPDATE qb.score  SET dispatched = vTimeNow WHERE ID = vLastInsertID ;
            --
        END IF ;
    --
    CALL qb.pLog( 'pScore_INSERT> Match score inserted for shot number ', vShotNumber ) ;   --  DEBUG/log
    --  
    COMMIT ;                --  <<<<---- TRANSACTION inactive <<<<----
    --
END


