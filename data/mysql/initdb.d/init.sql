CREATE TABLE music_files (
    id BIGINT NOT NULL AUTO_INCREMENT,
    absolute_path VARCHAR(500) NOT NULL,
    size INT UNSIGNED COMMENT 'Bytes', 
    bitrate SMALLINT UNSIGNED DEFAULT NULL,
    bitrate_type ENUM('CBR', 'VBR') DEFAULT NULL,
    duration SMALLINT UNSIGNED DEFAULT NULL COMMENT 'In seconds',
    artist VARCHAR(100) DEFAULT NULL COMMENT 'Metadata first, if empty fallback to filename',
    album VARCHAR(100) DEFAULT NULL,
    year YEAR DEFAULT NULL COMMENT 'Album year release',
    genre VARCHAR(100) DEFAULT NULL COMMENT 'Genre metadata',
    title VARCHAR(100) DEFAULT NULL COMMENT 'Title metadata',
    album_image MEDIUMBLOB,
    album_image_mime_type VARCHAR(20),
    
    PRIMARY KEY(id),
    UNIQUE(absolute_path)
) ENGINE=InnoDB;

CREATE TABLE musicbrainz_files (
    music_file_id BIGINT NOT NULL,
    album_type VARCHAR(50),
    album_status VARCHAR(50),
    album_release_country CHAR(2),
    work_id BINARY(16),
    album_id BINARY(16),
    artist_id BINARY(16),
    album_artist_id BINARY(16),
    release_group_id BINARY(16),
    release_track_id BINARY(16),
    recording_id BINARY(16),
    acoustid_id BINARY(16),

    UNIQUE (music_file_id),
    FOREIGN KEY (music_file_id)
        REFERENCES music_files(id)
        ON UPDATE CASCADE ON DELETE CASCADE
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
    total_bytes BIGINT COMMENT 'Total size of all the files found',
    finished TIMESTAMP,
    has_errors BOOLEAN,

    PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE scan_ops_errors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    scan_op_id BIGINT NOT NULL,
    absolute_path VARCHAR(500) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),

    PRIMARY KEY (id),
    FOREIGN KEY (scan_op_id)
        REFERENCES scan_ops(id)
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

    PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE bands_activity (
    band_id BIGINT NOT NULL,
    active_from YEAR,
    active_to YEAR,

    PRIMARY KEY (band_id, active_from, active_to),
    FOREIGN KEY (band_id)
        REFERENCES bands(id)
        ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE musicians (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    instruments VARCHAR(150),
    age TINYINT UNSIGNED,
    died BOOLEAN DEFAULT FALSE,

    PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE band_members (
    band_id BIGINT NOT NULL,
    musician_id BIGINT NOT NULL,
    active_from YEAR,
    active_to YEAR,

    PRIMARY KEY (band_id, musician_id, active_from, active_to),
    FOREIGN KEY (band_id)
        REFERENCES bands(id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (musician_id)
        REFERENCES musicians(id)
        ON UPDATE CASCADE ON DELETE CASCADE            
) ENGINE=InnoDB;

CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100),
    email VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),

    PRIMARY KEY (id),
    UNIQUE (username)
) ENGINE=InnoDB;
