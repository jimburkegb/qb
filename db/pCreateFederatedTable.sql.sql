-- 
USE qb ;
-- 
DROP PROCEDURE IF EXISTS qb.`pCreateFederatedTable` ;
-- 
DELIMITER $$

CREATE  PROCEDURE qb.`pCreateFederatedTable`(   use_server  INT, 
                                                host        VARCHAR(100), 
                                                port        VARCHAR(10), 
                                                usr         VARCHAR(50), 
                                                pwd         VARCHAR(50)  )
BEGIN
--
DECLARE connstr  VARCHAR(200) ;
DECLARE st       VARCHAR(2000) ;
DECLARE vPort    VARCHAR(10);
DECLARE vUsr     VARCHAR(50);
DECLARE vPwd     VARCHAR(50);
DECLARE vHost    VARCHAR(50);
--
DECLARE EXIT HANDLER FOR SQLEXCEPTION
BEGIN
    ROLLBACK;
    CALL qb.pLog( 'pCreateFederatedTable> ERROR...', -1 ) ;   --  DEBUG/log
END;
--
--  SYNTAX:-  scheme://user_name[:password]@host_name[:port_num]/db_name/tbl_name
--            (password and port are optional)
IF port = '' THEN SET vPort = '' ; ELSE SET vPort = CONCAT( ':', port) ; END IF ;
IF pwd  = '' THEN SET vPwd  = '' ; ELSE SET vPwd  = CONCAT( ':', pwd)  ; END IF ;
SET vUsr  = usr; 
SET vHost = CONCAT( '@', host) ; 
--
--      Proc to set up Federated table for recording scores up on the server
--      --------------------------------------------------------------------
--      Params:
--          use_server     If TRUE then set up the federated table. If false then DELETE it.
--          host         Servername or IP address
--          usr         Username for server credetials
--          pwd         Password for server credetials
--
--  CALL qb.`pCreateFederatedTable`( 0, '10.0.0.137', '', 'jim', ''  ) ;
--  CALL qb.`pCreateFederatedTable`( 1, '10.0.0.137', '', 'jim', ''  ) ;
--  CALL qb.`pCreateFederatedTable`( 1, '10.0.0.137', '9010', 'jim', 'tennent6'  ) ;
--  CALL qb.`pCreateFederatedTable`( 1, '10.0.0.137', '9010', 'jim', ''  ) ;
--  CALL qb.`pCreateFederatedTable`( 1, '10.0.0.137', '', 'jim', 'tennent6'  ) ;
    --
    CALL qb.pLog( 'pCreateFederatedTable> Creating federated table - qb_cores', -1 ) ;  
    --
    START TRANSACTION  ;     --  <<<<---- TRANSACTION Active <<<<----
        --
        DROP TABLE IF EXISTS qb.`qb_scores` ;
        --
        --  If no server recording then that's us done.  It may fail anyway and thrown an exception.
        --
        IF use_server >= 1 THEN
            -- 
            SET connstr = '' ;
            CALL qb.pLog( 'pCreateFederatedTable> setting up connection string...', -1 ) ;   --  DEBUG/log
            --  Build the connection string according to ...
            --  SYNTAX:-  scheme://user_name[:password]@host_name[:port_num]/db_name/tbl_name
            --
            SET connstr = CONCAT( 'CONNECTION = \'mysql://', vUsr, vPwd, vHost, vPort, '/qb/qb_scores\'') ;
--            SET connstr = CONCAT( '\'mysql://', usr, ':', pwd, '@', svr, '/qb/qb_scores\'') ;
            CALL qb.pLog( 'pCreateFederatedTable> Connection string is...', -1 ) ;   --  DEBUG/log
            CALL qb.pLog( connstr, -1 ) ;  
            --
            --  A rule of Federated tables is that this table MUST BE DUPLICATED and already 
            --  exist on the server.
            --  The reason I create the table using the PREPARE/EXECUTE statements is that there are
            --  variables which have to be factored in, and the CREATE TABLE statement seems to allow 
            --  only constants. Therefore the CREATE statement has to be pre-assembled.
            --
            SET @tmp = "
                    CREATE TABLE qb.`qb_scores` (
                          `ID`              INT(11) NOT NULL,
                          `MACADDR`         char(16) NOT NULL,
                          `player_ID`       INT(11) NOT NULL,
                          `score`           INT(11) NOT NULL,
                          `break`           INT(11) NOT NULL,
                          `match_ID`        INT(11) NOT NULL,
                          `frame_ID`        INT(11) NOT NULL,
                          `recorded`        DATETIME NOT NULL,
                          `comments`        VARCHAR(45) DEFAULT NULL,
                          `shottype`        INT(11) DEFAULT NULL,
                          `shotnumber`      INT(11) DEFAULT NULL,
                          `dispatched`      DATETIME DEFAULT NULL,
                          `total_player1`   INT(11) DEFAULT NULL,
                          `total_player2`   INT(11) DEFAULT NULL,
                          `player1_ID`      INT(11) DEFAULT NULL,
                          `player2_ID`      INT(11) DEFAULT NULL,
                          `visit`           INT(11) DEFAULT NULL,
                          KEY `UID` (`MACADDR`,`recorded`)
                    )   ENGINE=FEDERATED
                        DEFAULT CHARSET=utf8 " ;
            SET @tmp = CONCAT( @tmp, connstr, ';') ;    --  Append the connection string
            PREPARE st FROM @tmp ;                      --  Prepare and execute it
            EXECUTE st ;
            DEALLOCATE PREPARE st ;
            --   CONNECTION='mysql://jim:tennent6@10.0.0.137/qb/qb_scores';
            --   CONNECTION='mysql://jim:tennen6@10.0.0.137:9000/qb/qb_scores';
            -- 
            CALL qb.pLog( 'pCreateFederatedTable> Federated table created successfully ', -1 ) ;  
        END IF ;
        --
        CALL qb.pLog( 'pCreateFederatedTable> Exit ', -1 ) ;   --  DEBUG/log
        --  
    COMMIT ;                --  <<<<---- TRANSACTION inactive <<<<----
    --
END
