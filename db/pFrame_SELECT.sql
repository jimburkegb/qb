-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pFrame_SELECT` ;
-- 
DELIMITER $$

CREATE  PROCEDURE qb.`pFrame_SELECT`(
                        IN prmMatch_ID      INT,
                        IN prmFrameNumber   INT )
BEGIN
DECLARE vFrameNumber  INT ;
-- 
--  call qb.`pFrame_SELECT`( 01234, 0) ;
--  select * from qb.score ;
--  select * from qb.log ;
-- 
    --
    CALL qb.pLog( 'pFrame_SELECT> Getting frame data for for match ID: ', prmMatch_ID ) ;   --  DEBUG/log
    -- 
    --  SP to SELECT an entry in the MATCH table
    --  ----------------------------------------
    --
    --  If the FrameNumber is NULL or ZERO then this means - get the first entry.
    -- 
    --  SET vFrameNumber = IFNULL(prmFrameNumber, 0) ;
    -- 
    SELECT  * FROM `qb`.`match`
    WHERE   match_ID = prmmatch_ID AND FrameNumber = IFNULL(prmFrameNumber, 0) ;  
    --
    --
    CALL qb.pLog( 'pFrame_SELECT> Match score returned for shot number ', IFNULL(prmFrameNumber, 0) ) ;   --  DEBUG/log
    --  
END


