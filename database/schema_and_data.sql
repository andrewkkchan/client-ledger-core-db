-- SQL Version: MariaDB 10.3

-- Setup default DB user and privileges
GRANT ALL ON ledger.* TO 'sample_user'@'localhost' IDENTIFIED BY '19283746';

SET FOREIGN_KEY_CHECKS = 0;
SET GLOBAL FOREIGN_KEY_CHECKS = 0;

-- Create ledger schema and all tables
DROP SCHEMA IF EXISTS ledger;
CREATE SCHEMA IF NOT EXISTS ledger DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ledger;

CREATE TABLE IF NOT EXISTS ledger.`account`
(
  `id`            VARCHAR(36)    NOT NULL,
  `account_name`  VARCHAR(360)   NOT NULL,
  `account_group` VARCHAR(360)   NOT NULL,
  `createTime`    TIMESTAMP(3)   NOT NULL,
  `currency`      VARCHAR(10)    NOT NULL,
  `balance`       DECIMAL(20, 5) NOT NULL,
  `kafkaOffset`    BIGINT        NOT NULL,
  `kafkaPartition` INTEGER       NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS ledger.journal_entry
(
  `id`         VARCHAR(36)    NOT NULL,
  `requestId`  VARCHAR(36)    NOT NULL,
  `accountId`  VARCHAR(36)    NOT NULL,
  `amount`     DECIMAL(20, 5) NOT NULL,
  `currency`   VARCHAR(10)    NOT NULL,
  `createTime` TIMESTAMP(3)   NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_journal_entry_log_account`
    FOREIGN KEY (`accountId`)
      REFERENCES ledger.`account` (`id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION
)
  ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS ledger.`transaction_event`
(
  `id`             VARCHAR(36)   NOT NULL,
  `type`           VARCHAR(36)   NOT NULL,
  `request`        VARCHAR(3600) NOT NULL,
  `createTime`     TIMESTAMP(3)  NOT NULL,
  `kafkaOffset`    BIGINT        NOT NULL,
  `kafkaPartition` INTEGER       NOT NULL,

  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ledger.`transaction_result`
(
  `id`         VARCHAR(36)   NOT NULL,
  `requestId`  VARCHAR(36)   NOT NULL UNIQUE,
  `response`   VARCHAR(3600) NOT NULL,
  `createTime` TIMESTAMP(3)  NOT NULL,
  `success`    BOOLEAN       NOT NULL,
  `kafkaOffset`    BIGINT        NOT NULL,
  `kafkaPartition` INTEGER       NOT NULL,

  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;

-- Create index to support faster read
ALTER TABLE `journal_entry`
  ADD INDEX `index_journal_entry_on_requestId` (`requestId`);
ALTER TABLE `journal_entry`
  ADD INDEX `index_journal_entry_on_accountId` (`accountId`);
ALTER TABLE `journal_entry`
  ADD INDEX `index_journal_entry_on_createTime` (`createTime`);
ALTER TABLE `transaction_result`
  ADD INDEX `index_transaction_result_on_requestId` (`requestId`);


