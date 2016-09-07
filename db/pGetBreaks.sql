-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pGetBreaks` ;
-- 
DELIMITER $$
-- 
CREATE  PROCEDURE qb.`pGetBreaks`(  IN prmVerbose   INT,
                                    IN prmFrame_ID  INT , 
                                    IN prmPlayer_ID INT , 
                                    IN prmMinBreak  INT, 
                                    IN prmUseServer INT,
                                    OUT prmResults  VARCHAR(500) )
BEGIN
DECLARE done        INT DEFAULT FALSE;
DECLARE vShottype   INT ;
DECLARE vBreaks     VARCHAR(500) ;
DECLARE vRecorded   DATETIME ;
DECLARE vLen, vTemp, vBreak, vVisit   INT ;
DECLARE curNextScore CURSOR FOR SELECT  `break` ,  `visit`,  `recorded`,  `shottype` 
                                FROM    tmpScores 
                                ORDER BY `recorded` ASC;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE; 
    -- 
    --  --------------------------------------------------------------
    --  Get the breaks for the specified player in the specified frame.
    --  --------------------------------------------------------------
    -- 
    --  prmVerbose = 1      Return a list of breaks, comma separated. An unfinished break is followed by "unf".
    --
    --             = 2      Return a list of breaks, comma separated enclosed in brackets.
    --                      This is special in that an unfinished break is NEGATIVE - this is for
    --                      the special parsing of the php frame scoring routine on the server.
    --
    --             = 3      Return a recordset with a list containing :-
    --                          break, recorded time, visit number 
    --                      An unfinished break is followed by "unf".
    --
    --
    CALL qb.pLog( 'pGetBreaks> Frame ID = ', prmFrame_ID ) ;   --  DEBUG/log
    -- 
    --  Need to return this as a comma-separated list of numbers.
    --
    --  Create the temp table 
    DROP TEMPORARY TABLE IF EXISTS tmpScores ;
    CREATE TEMPORARY TABLE `tmpScores` (
                `ID`            INT(11)   NOT NULL ,
                `break`         INT(11) ,  
                `recorded`      DATETIME , 
                `visit`         INT(11),
                `shottype`      INT(11) ) ;
    --
    --  Filter on the end-of-break [100, 101] (and end-of-frame [106]) scores for this player in the 
    --  specified frame.  NB - 101 => UNFINISHED break.
    -- 
    IF (prmUseServer = 1) THEN          --  If the UseServer boolean is set (=1) then use the qb_scores   
        --                                  table, otherwise use the un-federated (score) table.
        CALL qb.pLog( 'pGetBreaks> Using table - qb_scores ', -1 ) ;   --  DEBUG/log
        INSERT INTO `tmpScores`
        SELECT  ID, `break`, `recorded`, `visit`, `shottype`
        FROM    qb.qb_scores
        WHERE   player_id   = prmPlayer_ID 
          AND   frame_ID    = prmFrame_ID 
          AND   break      >= prmMinBreak
          AND (`shottype` IN (100, 101, 106)) ;  --  End Break, End Unf Break, End Frame
        --
    ELSE
        CALL qb.pLog( 'pGetBreaks> Using local table - score ', -1 ) ;   --  DEBUG/log
        --
        INSERT INTO `tmpScores`
        SELECT  ID, `break`, `recorded`, `visit`, `shottype`
        FROM    qb.score
        WHERE   player_id   = prmPlayer_ID 
          AND   frame_ID    = prmFrame_ID 
          AND   break      >= prmMinBreak
          AND (`shottype` IN (100, 101, 106)) ; --  End Break, End Unf Break, End Frame
        --    
    END IF ;
    --
    SET vBreaks = '' ;          --  Initialise all varioables
    SET done = FALSE;           --
    SET vBreak = 0 ;
    --
    --  Loop round, building string containing comma-separated breaks
    --
    OPEN curNextScore ;         --  Open the cursor and read in the first record.                 
    read_loop: LOOP
        -- 
        FETCH curNextScore INTO  vBreak, vVisit, vRecorded, vShottype ;
        IF done THEN  LEAVE read_loop;  END IF;
        --
        --  If the break is UNFINISHED (101) then mark it as negative
        IF (vShottype = 101) THEN  SET vBreak = -1 * vBreak ;  END IF ;
        --  Append this break to the end of the list.
        SET vBreaks = CONCAT(vBreaks , vBreak, ', ' ) ;                    
        --
    END LOOP ;
    CLOSE curNextScore ;
    --
    if (prmVerbose = 1) THEN
        --
        --  Tidy up the list of breaks. ie. strip the trailing comma+space and clear string 
        --  if no breaks recorded.
        SET vLen    = LENGTH(vBreaks) ;
        SET vBreaks = LEFT( vBreaks,  vLen-2 ) ;    --  Strip last comma+space
        -- 
        IF (vLen > 1) THEN
            SET vBreaks = CONCAT( '(', vBreaks, ')' ) ; --  Enclose in brackets
            SET vBreaks = REPLACE ( vBreaks, '-', 'unf' ) ;
        ELSE
            CALL qb.pLog( 'pGetBreaks> No breaks ', 0 ) ;   --  Ignore no breaks
        END IF ;
        --
        --  Return the list of breaks - eg. "56, 123, 321" 
        --  Return results in the OUTPUT parameter 
        SET prmResults = vBreaks ;          
        --
    ELSEIF (prmVerbose = 2) THEN
        --
        --  Tidy up the list of breaks. ie. strip the trailing comma+space and clear string 
        --  if no breaks recorded.
        SET vLen    = LENGTH(vBreaks) ;
        SET vBreaks = LEFT( vBreaks,  vLen-2 ) ;    --  Strip last comma+space
        -- 
        IF (vLen <=1)  THEN CALL qb.pLog( 'pGetBreaks> No breaks ', 0 ) ; END IF ;  --  Ignore no breaks
        --
        --  Return the list of breaks - eg. "56, 123, 321" 
        --  Return results in the OUTPUT parameter 
        SET prmResults = vBreaks ;          
        --
    ELSEIF (prmVerbose = 3) THEN
        --
        --  Return the list of breaks as a list - eg.  56   29-jan-2015 13:33.7   4
        --                                             123  29-jan-2015 13:54.1   6
        --                                             ...etc...
        --  Return the list of breaks - eg. "56, 123, 321" 
        --  Return results in the OUTPUT parameter 
        SET prmResults = 'Cannot return resultset' ;          
        --
    ELSE
        SET prmResults = 'Invalid option' ;          
    END IF ;
    --
    CALL qb.pLog( 'pGetBreaks> Finished.  ', 0 ) ;   --  DEBUG/log
    --
END


