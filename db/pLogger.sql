USE qb ;
-- 
DROP PROCEDURE  IF EXISTS qb.pLog ;
-- 
DELIMITER $$

CREATE  PROCEDURE `pLog`(IN sMsg VARCHAR(255), IN iX INT)
BEGIN
    -- 
    --  SP to log info to the log table
    --  -------------------------------
    --
    --  TEST ->    call qb.pLog( 'test with int param' , 123) ;
    --             call qb.pLog( 'test with no param', -1 ) ;
    --             select * from qb.log ;
    --             delete from qb.log ;
    --
    CASE iX   --  Integer param supplied ?
       WHEN -1 THEN  INSERT INTO qb.log ( Timestamp, Msg, User ) VALUES  (Now(), sMsg, USER() )  ;
       ELSE          INSERT INTO qb.log ( Timestamp, Msg, User ) VALUES  (Now(), CONCAT(sMsg, CAST(iX AS CHAR)), USER() ) ; 
    END CASE ;
    --
    --   
END
--

