-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pTidy` ;
-- 
DELIMITER $$
-- 
CREATE  PROCEDURE qb.`pTidy`( IN prmLevel INT, IN prmFrame_ID INT, use_server  INT )
BEGIN
DECLARE vTemp    INT ;
    -- 
    --  The TIDY function allows multiple levels of db tidy.
    --  ----------------------------------------------------
    --  call qb.`pTidy`( 1, 0 )
    --  call qb.`pTidy`( 2, 0 )
    --  call qb.`pTidy`( 3, 0 )
    --  call qb.`pTidy`( 4, 0 )
    --  call qb.`pTidy`( 0, 0 )
    --
    -- 
    CALL qb.pLog( 'pTidy> db tidying. Use Svr = ', use_server ) ;   --  DEBUG/log
    --
    CASE prmLevel   
       --
       WHEN 1  THEN     --  Anything older than ONE WEEK from today is deleted
                        --
                DELETE FROM qb.log          WHERE `Timestamp` < DATE_ADD( NOW(), INTERVAL -7 DAY);
                DELETE FROM qb.matchparams  WHERE `datestart` < DATE_ADD( NOW(), INTERVAL -7 DAY);
                DELETE FROM qb.serverparams WHERE `when`      < DATE_ADD( NOW(), INTERVAL -7 DAY);
                DELETE FROM qb.score        WHERE `recorded`  < DATE_ADD( NOW(), INTERVAL -7 DAY);
       --
       WHEN 2  THEN     --  Retain only the most recent setup, including all scores NOT dispathed
                        --  Delete all log entries older than one day
                        --
                DELETE FROM qb.log          WHERE `Timestamp` > DATE_ADD( NOW(), INTERVAL -1 DAY);
                SELECT ( SELECT MAX(ID) FROM qb.serverparams ) INTO vTemp ;
                DELETE FROM qb.serverparams WHERE ID  < vTemp;
                SELECT ( SELECT MAX(ID) FROM qb.matchparams ) INTO vTemp ;
                DELETE FROM qb.matchparams  WHERE ID  < vTemp;
                DELETE FROM qb.score        WHERE `dispatched` IS NOT NULL ;
       --
       WHEN 3  THEN     --  Clear out only the current match score both locally and remote (if in use).
                        --  
                DELETE FROM qb.score        WHERE frame_id = prmFrame_ID;
                IF use_server = 1 THEN
                    DELETE FROM qb.qb_scores WHERE frame_id = prmFrame_ID;
                END IF ;
       --
       WHEN 4  THEN     --  Clear out anything to do with the current FRAME both locally and remote (if in use).
                        --
                IF (prmFrame_ID > 0) THEN
                    DELETE FROM qb.matchparams  WHERE frame_ID = prmFrame_ID ;
                    DELETE FROM qb.score        WHERE frame_ID = prmFrame_ID ;
                    IF use_server = 1 THEN
                        DELETE FROM qb.qb_scores WHERE frame_id = prmFrame_ID;
                    END IF ;                    
                END IF ;
       --
       ELSE             --  Completely clear ALL tables 
                        --
                DELETE FROM qb.log    ;      
                DELETE FROM qb.matchparams  ;
                DELETE FROM qb.serverparams ;
                DELETE FROM qb.score  ;
    END CASE ;
    --
    -- 
    CALL qb.pLog( 'pTidy> db tidied - level = ', prmLevel ) ;   --  DEBUG/log
    --
END


