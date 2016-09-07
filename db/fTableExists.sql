CREATE DEFINER=`jim`@`%` FUNCTION `table_exists`(prmTablename VARCHAR(50)) RETURNS int(11)
BEGIN

SET @tmp = '' ;
SET @iTmp = 0 ;
--
 SELECT table_name INTO @tmp
 FROM information_schema.tables
 WHERE table_schema = 'qb'
 AND table_name = prmTablename ;

  SET @iTmp = CASE LENGTH(@tmp) WHEN 0 THEN  0  ELSE 1 END  ;

RETURN @iTmp  ;
END