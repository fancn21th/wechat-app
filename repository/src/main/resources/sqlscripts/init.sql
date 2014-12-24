USE checkin;

CREATE TABLE wechat_user (
  id          BIGINT(20)  NOT NULL AUTO_INCREMENT,
  wechat_uid  VARCHAR(32) NOT NULL,
  enabled     BIT(1)      NOT NULL DEFAULT 1,
  nickname    VARCHAR(32),
  avatar      VARCHAR(128),
  sex         INT(1)      NOT NULL DEFAULT 0,
  province    VARCHAR(32),
  city        VARCHAR(32),
  country     VARCHAR(16),
  union_id    VARCHAR(32),
  description VARCHAR(512),
  PRIMARY KEY (id)
);

CREATE TABLE fitness_checkin (
  id            BIGINT(20) NOT NULL AUTO_INCREMENT,
  uid           BIGINT(20) NOT NULL,
  review_passed BIT(1)     NOT NULL DEFAULT 1,
  timestamp     TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  record        VARCHAR(768),
  PRIMARY KEY (id)
);

CREATE TABLE checkin_photo (
  id                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
  fitness_checkin_id BIGINT(20)   NOT NULL DEFAULT -1,
  label              VARCHAR(32)  NOT NULL,
  storage_name       VARCHAR(128) NOT NULL,
  type               VARCHAR(32)  NOT NULL,
  size               INT(20)      NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO wechat_user
(id, wechat_uid, enabled, nickname, avatar, sex, province, city, country, union_id, description)
VALUES (1, "dev", 1, "Dev", NULL, 1, "Hubei", "Wuhan", "China", NULL, NULL);

CREATE UNIQUE INDEX idx_wechat_user_wechat_uid ON wechat_user (wechat_uid);
CREATE INDEX idx_fitness_checkin_uid ON fitness_checkin (uid);
CREATE INDEX idx_fitness_checkin_review_passed ON fitness_checkin (review_passed);
CREATE INDEX idx_checkin_photo_checkin_id ON checkin_photo (fitness_checkin_id);
