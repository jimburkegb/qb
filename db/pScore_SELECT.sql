-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pScore_SELECT` ;
-- 
DELIMITER $$

CREATE  PROCEDURE qb.`pScore_SELECT`(
                        IN prmFrame_ID      INT,
                        IN prmShotNumber    INT )
BEGIN
DECLARE vShotNumber  INT ;
-- 
--  call qb.`pScore_SELECT`( '00112233AABBCCDD', 1, 3, 1234, 4321, 123, NULL, NULL) ;
--  select * from qb.score ;
--  select * from qb.log ;
-- 
    --
    CALL qb.pLog( 'pScore_SELECT> Getting score for for Frame ID: ', prmFrame_ID ) ;   --  DEBUG/log
    -- 
    --  SP to SELECT an entry in the SCORE table
    --  ----------------------------------------
    --
    --  If the ShotNumber is NULL or ZERO then this means - get the last entry.
    -- 
    SET vShotNumber = prmShotNumber ;
    -- 
    IF ( (vShotNumber IS NULL) OR (vShotNumber = 0) ) THEN
        --  Get the last Shot Number recorded
        SELECT IFNULL(MAX(shotnumber), 0) INTO vShotNumber        
           FROM    `qb`.`score` 
           WHERE   frame_ID = prmFrame_ID ;
    END IF ;

    --    
    --
    SELECT  player, score, break, shotnumber
    FROM    `qb`.`score`
    WHERE   frame_ID = prmFrame_ID AND shotnumber = vShotNumber ;
    --
    --
    CALL qb.pLog( 'pScore_SELECT> Match score returned for shot number ', IFNULL(prmShotNumber, 0) ) ;   --  DEBUG/log
    --  
END


