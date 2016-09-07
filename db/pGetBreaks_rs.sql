-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pGetBreaks_rs` ;
-- 
DELIMITER $$
-- 
CREATE  PROCEDURE qb.`pGetBreaks_rs`(  IN prmVerbose   INT,
                                       IN prmFrame_ID  INT , 
                                       IN prmPlayer_ID INT , 
                                       IN prmMinBreak  INT, 
                                       IN prmUseServer INT )
BEGIN
DECLARE vBreaks     VARCHAR(500) ;
    -- 
    --  --------------------------------------------------------------
    --  Get the breaks for the specified player in the specified frame.
    --  This differs from pGetBreaks only in that the results are passed back
    --  as a resultset, not as an output parameter.
    --  The reason this palaver is necessary (ie. instead of just using pGetBreaks), is
    --  that I could not get the JDBC registerOutParameter function to work properly.
    --  It was truncating the returned string to max 50 chars and I could not find a
    --  solution.
    --  JimB, 21-Mar-16
    --  --------------------------------------------------------------
    --
    --
    CALL qb.pLog( 'pGetBreaks_rs> Frame ID = ', prmFrame_ID ) ;   --  DEBUG/log
    --
    --
    CALL  qb.`pGetBreaks`(  prmVerbose, prmFrame_ID, prmPlayer_ID, prmMinBreak, prmUseServer, vBreaks ) ;
    --
    SELECT vBreaks ;        --  Return the list of breaks
    --
    CALL qb.pLog( 'pGetBreaks_rs> Finished.  ', 0 ) ;   --  DEBUG/log
    --
END


