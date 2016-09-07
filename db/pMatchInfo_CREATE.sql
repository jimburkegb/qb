-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pMatchInfo_CREATE` ;
-- 
DELIMITER $$

CREATE  PROCEDURE qb.`pMatchInfo_CREATE`( 
                            IN prmMACADDR       CHAR(16),
                            IN prmMatchID       INT,
                            IN prmFrameID       INT,
                            IN prmDateStart     DATETIME,
                            IN prmComments      VARCHAR(120),
                            IN prmStatus        INT,
                            IN prmFrameNumber   INT,
                            IN prmRefereeName   VARCHAR(45),
                            IN prmRefereeFlag   VARCHAR(45)
                            )
BEGIN
DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
    ROLLBACK;
    CALL qb.pLog( 'pMatchInfo_CREATE> ERROR...', -1 ) ;   --  DEBUG/log
    SELECT -1  AS 'status';
END;
-- 
--  This is for the SERVDER, not the QB MySQL
--  -----------------------------------------
-- 
    START TRANSACTION  ;     --  <<<<---- TRANSACTION Active <<<<----
        --
        CALL qb.pLog( 'pMatchInfo_CREATE> Creating match data for match ID ' , prmMatchID) ;   --  DEBUG/log
        CALL qb.pLog( 'pMatchInfo_CREATE> ...Frame ID ' , prmFrameID) ;   --  DEBUG/log
        -- 
        --  SP to CREATE an entry in the qb MATCH table
        --  -------------------------------------------
        --  First delete if any exists already.
        DELETE FROM qb.`match`
        WHERE MACADDR  = prmMACADDR
        AND   match_ID = prmMatchID
        AND   frame_ID = prmFrameID ;
        --
        INSERT `qb`.`match`
        (
            MACADDR,
            match_ID,
            frame_ID,
            datestart,
            datestop,
            comments,
            `status`,
            frame_number,
            referee_name,
            referee_flag
        )
        VALUES
        (
            prmMACADDR,
            prmMatchID,
            prmFrameID,
            prmDateStart,
            NULL,
            COALESCE(prmComments, 'none'),
            prmStatus       ,
            prmFrameNumber  ,
            COALESCE( prmRefereeName , 'n/a'),
            COALESCE( prmRefereeFlag , '')
        ) ;
    CALL qb.pLog( 'pMatchInfo_CREATE> SUCCESS ', 0 ) ;   --  DEBUG/log
        --
    COMMIT ;                --  <<<<---- TRANSACTION inactive <<<<----
    SELECT +1 AS 'status';
    --  
END
