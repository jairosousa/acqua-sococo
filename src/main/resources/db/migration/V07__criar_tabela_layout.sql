CREATE TABLE IF NOT EXISTS layouts (
                                        id BIGINT(20) NOT NULL AUTO_INCREMENT,
                                        filename VARCHAR(255) DEFAULT NULL,
                                        url VARCHAR(255) DEFAULT NULL,
                                        PRIMARY KEY (id))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;