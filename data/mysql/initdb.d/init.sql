CREATE TABLE music_files (
    id BIGINT NOT NULL AUTO_INCREMENT,
    absolute_path VARCHAR(500) NOT NULL,
    size INT UNSIGNED COMMENT 'Bytes',
    duration SMALLINT UNSIGNED DEFAULT NULL COMMENT 'In seconds',
    artist VARCHAR(100) DEFAULT NULL COMMENT 'Metadata first, if empty fallback to filename',
    album VARCHAR(100) DEFAULT NULL,
    year YEAR DEFAULT NULL COMMENT 'Album year release',
    genre VARCHAR(100) DEFAULT NULL COMMENT 'Genre metadata',
    title VARCHAR(100) DEFAULT NULL COMMENT 'Title metadata',

    PRIMARY KEY(id),
    UNIQUE(absolute_path)
) ENGINE=InnoDB;

CREATE TABLE stats (
	id BIGINT NOT NULL AUTO_INCREMENT,
	total_files MEDIUMINT UNSIGNED COMMENT 'Total files in the collection',
	total_bytes BIGINT COMMENT 'Total size of all the files',
	last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE scan_ops (
    id BIGINT NOT NULL AUTO_INCREMENT,
    started TIMESTAMP DEFAULT NOW(),
    total_files_scanned MEDIUMINT UNSIGNED COMMENT 'Total files found during this scanning',
    total_files_inserted MEDIUMINT UNSIGNED COMMENT 'Total files inserted in the DB',
    total_elapsed_time SMALLINT UNSIGNED COMMENT 'Total duration in seconds',
    total_bytes BIGINT UNSIGNED COMMENT 'Total size of all the files found',
    finished TIMESTAMP,
    has_errors BOOLEAN,

    PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE playlist (
	id BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(150) NOT NULL,
	image MEDIUMBLOB,
	rating TINYINT UNSIGNED,
	tags JSON DEFAULT ('[]'),
	created_at TIMESTAMP DEFAULT NOW(),
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

	PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE playlist_tracks (
	playlist_id BIGINT NOT NULL,
	track_id BIGINT NOT NULL,
	created_at TIMESTAMP DEFAULT NOW(),

	UNIQUE (playlist_id, track_id),
	FOREIGN KEY (playlist_id)
        REFERENCES playlist(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
	FOREIGN KEY (track_id)
        REFERENCES music_files(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE bands (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    country CHAR(2),
    country_name VARCHAR(100),
    active_from YEAR DEFAULT NULL,
    active_to YEAR DEFAULT NULL,
    total_albums_released TINYINT UNSIGNED DEFAULT NULL,
    website VARCHAR(150),
    twitter VARCHAR(150),
    youtube VARCHAR(150),
    musicbrainz_url VARCHAR(150),

    PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE musicians (
    id BIGINT NOT NULL AUTO_INCREMENT,
    real_name VARCHAR(50),
    artistic_name VARCHAR(50),
    instruments VARCHAR(150),
    birthday DATE,
    died DATE,
    musicbrainz_url VARCHAR(150),

    PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE band_musicians (
    band_id BIGINT NOT NULL,
    musician_id BIGINT NOT NULL,

    PRIMARY KEY (band_id, musician_id),
    FOREIGN KEY (band_id)
        REFERENCES bands(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (musician_id)
        REFERENCES musicians(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE albums (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    band_id BIGINT NOT NULL,
    released_on DATE,

    PRIMARY KEY (id),
    FOREIGN KEY (band_id)
        REFERENCES bands(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE albums_musicians (
    album_id BIGINT NOT NULL,
    musician_id BIGINT NOT NULL,

    PRIMARY KEY (album_id, musician_id),
    FOREIGN KEY (musician_id)
        REFERENCES musicians(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (album_id)
        REFERENCES albums(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE songs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    album_id BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (album_id)
        REFERENCES albums(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;
