-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pScore_UPDATE` ;
-- 
DELIMITER $$

CREATE  PROCEDURE qb.`pScore_UPDATE`(
                        IN prmMACADDR       CHAR(16),
                        IN prmPlayer        INT,
                        IN prmScore         INT,
                        IN prmMatch_ID      INT,
                        IN prmFrame_ID      INT,
                        IN prmBreak         INT,
                        IN prmShotType      INT,
                        IN prmComments      VARCHAR(45),
                        IN prmShotNumber    INT  )
BEGIN
-- 
--  call qb.`pScore_UPDATE`( '00112233AABBCCDD', 1, 3, 1234, 4321, 123, NULL, NULL, 2) ;
--  select * from qb.score ;
--  select * from qb.log ;
-- 
-- This UPDATE simply updates the specified shot.
-- 
-- 
DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
    ROLLBACK;
    CALL qb.pLog( 'pScore_UPDATE> ERROR...', -1 ) ;   --  DEBUG/log
END;
    -- 
    --  SP to UPDATE an existing entry in the SCORE table
    --  -------------------------------------------------
    --
    CALL qb.pLog( 'pScore_UPDATE> Updating score for for frame ID: ', prmFrame_ID ) ;   --  DEBUG/log
    --
    START TRANSACTION  ;     --  <<<<---- TRANSACTION Active <<<<----
        UPDATE `qb`.`score`
        SET MACADDR  = COALESCE(prmMACADDR, MACADDR),
            player   = COALESCE(prmPlayer, player),
            score    = COALESCE(prmScore, score),
            break    = COALESCE(prmBreak, break),
            comments = COALESCE(prmComments, comments),
            shottype = COALESCE(prmShotType, shottype),
            recorded = NOW()
        WHERE
            Frame_ID = prmFrame_ID AND shotnumber = prmShotNumber ;
    --
    --
    CALL qb.pLog( 'pScore_UPDATE> Match score updated', 0 ) ;   --  DEBUG/log
    --  
    COMMIT ;                --  <<<<---- TRANSACTION inactive <<<<----
    --
END


