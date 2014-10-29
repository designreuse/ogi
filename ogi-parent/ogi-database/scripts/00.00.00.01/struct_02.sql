CREATE TABLE TT_VERSION  (
	TTV_ID INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	TTV_VERSION VARCHAR(32) NOT NULL,
	TTV_DATE TIMESTAMP NOT NULL
) ENGINE=InnoDB;


--  ###### Table and functions to simulate Sequence ######
-- http://www.microshell.com/database/mysql/emulating-nextval-function-to-get-sequence-in-mysql/
CREATE TABLE `TT_SEQUENCE` (
    `SEQ_NAME` varchar(100) NOT NULL,
    `SEQ_INCREMENT` int(11) unsigned NOT NULL DEFAULT 1,
    `SEQ_MIN_VALUE` int(11) unsigned NOT NULL DEFAULT 1,
    `SEQ_MAX_VALUE` bigint(20) unsigned NOT NULL DEFAULT 18446744073709551615,
    `SEQ_CUR_VALUE` bigint(20) unsigned DEFAULT 1,
    `SEQ_CYCLE` boolean NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`SEQ_NAME`)
) ENGINE=InnoDB;

DELIMITER $$
DROP FUNCTION nextval$$
CREATE FUNCTION nextval (param_seq_name varchar(100))
RETURNS bigint(20) NOT DETERMINISTIC
BEGIN
    DECLARE cur_val bigint(20);
 
    SELECT SEQ_CUR_VALUE INTO cur_val FROM TT_SEQUENCE
    WHERE SEQ_NAME = param_seq_name 
    FOR UPDATE; -- In order to lock row
 
    IF cur_val IS NOT NULL THEN
        UPDATE TT_SEQUENCE
        SET SEQ_CUR_VALUE = IF (
                (SEQ_CUR_VALUE + SEQ_INCREMENT) > SEQ_MAX_VALUE,
                IF (
                    SEQ_CYCLE = TRUE,
                    SEQ_MIN_VALUE,
                    NULL
                ),
                SEQ_CUR_VALUE + SEQ_INCREMENT
            )
        WHERE SEQ_NAME = param_seq_name;
    END IF;
 
    RETURN cur_val;
END;
$$

DROP FUNCTION curval$$
CREATE FUNCTION curval (param_seq_name varchar(100))
RETURNS bigint(20) DETERMINISTIC
BEGIN
    DECLARE cur_val bigint(20);
 
    SELECT SEQ_CUR_VALUE INTO cur_val FROM TT_SEQUENCE
    WHERE SEQ_NAME = param_seq_name;
 
    RETURN cur_val;
END;
$$

DELIMITER ;


INSERT INTO TT_SEQUENCE(SEQ_NAME, SEQ_CUR_VALUE) VALUE ('SEQ_PROPERTY', 605);
COMMIT;