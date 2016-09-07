-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pScore_CORRECT` ;
-- 
DELIMITER $$

CREATE  PROCEDURE qb.`pScore_CORRECT`(  IN prmFrame_ID  INT, 
                                        IN use_server   INT  )
BEGIN
DECLARE vShotNumber  INT ;
DECLARE vID  INT ;
-- 
--  The functionality of the CORRECT is simple - delete the last SCORING shot, by zeroing the score
--  and break values.
--  The most common reason for stepping back and correcting is that a score has been assigned to the 
--  wrong player.
-- 
DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
    ROLLBACK;
    CALL qb.pLog( 'pScore_CORRECT> ERROR...', -1 ) ;   --  DEBUG/log
END;
    -- 
    --  SP to CORRECT an existing entry in the SCORE table
    --  --------------------------------------------------
    --
    CALL qb.pLog( 'pScore_CORRECT> Correcting score for for frame ID: ', prmFrame_ID ) ;   --  DEBUG/log
    --
    START TRANSACTION  ;     --  <<<<---- TRANSACTION Active <<<<----
        --
        --  Get the latest shot number.
        --
        SELECT      IFNULL(MAX(shotnumber), 0) INTO vShotNumber        
           FROM    `qb`.`score` 
           WHERE    frame_ID = prmFrame_ID AND score != 0;
        -- 
        --  If the last shotnumber is ZERO then we must have rewound right to the beginning.
        --  Or, we have not located a non-zero score at all.
        --  In either case, CLEAR OUT THE SCORES FOR THIS FRAME !!!
        --
        IF vShotNumber = 0 THEN 
            
            CALL qb.pLog( 'pScore_CORRECT> Hit table-start. Truncating...', 0 ) ;   --  DEBUG/log

            DELETE FROM `qb`.`score` WHERE frame_ID = prmFrame_ID ;
            --
            SELECT  0, 0, 0, 0, 0, 0 ;  --  And return zeros.
            --
        ELSE
/*
            --
            --  Zero out ('correct') this record.
            --
            UPDATE  `qb`.`score`
            SET     score = 0, 
                    break = 0, 
                    comments = 'Correction',
                    player_ID = 0,
                    total_player1 = 0,
                    total_player2 = 0,
                    shottype = 99,
                    shotnumber = 0,
                    player1_id = 0,
                    player2_id = 0,                   
                    recorded = NOW()
            WHERE   frame_ID = prmFrame_ID AND shotnumber = vShotNumber ;
            --
            CALL qb.pLog( 'pScore_CORRECT> Match score updated', 0 ) ;   --  DEBUG/log
 */
            --
            DELETE FROM `qb`.`score` WHERE frame_ID = prmFrame_ID AND shotnumber = vShotNumber ;
            IF use_server = 1 THEN
                --
                DELETE FROM `qb`.`qb_scores` WHERE frame_ID = prmFrame_ID AND shotnumber = vShotNumber ;
                --
            END IF ;
            -- 
            --  Get the NEW latest SCORING shot number (ie. not corrected).
            --
            SELECT      IFNULL(MAX(shotnumber), 0) INTO vShotNumber      
               FROM    `qb`.`score` 
               WHERE    frame_ID = prmFrame_ID AND score != 0;
            --
            --  Get the ID of the last scoring shot
            SELECT      ID INTO vID        
               FROM    `qb`.`score` 
               WHERE    frame_ID = prmFrame_ID AND shotnumber = vShotNumber;
            --
            --  DELETE all records after the last scoring shot !  eg. Fouls, end-of-breaks.
            --
            DELETE FROM `qb`.`score` WHERE frame_ID = prmFrame_ID AND ID > vID ;
            --
            --  Now to use the correction up on the server.
            --
            IF use_server = 1 THEN
                --
                DELETE FROM `qb`.`qb_scores` WHERE frame_ID = prmFrame_ID AND ID > vID ;
                --
            END IF ;
            -- 
            --  Return the details of the last scoring shot. These details are simply
            --  the match score to date, including break.
            --
            SELECT  break, player1_ID, player2_ID, total_player1, total_player2, player_ID, visit
               FROM `qb`.`score` 
               WHERE  ID = vID;
        END IF ;
        -- 
    COMMIT ;                --  <<<<---- TRANSACTION inactive <<<<----
    --
END


