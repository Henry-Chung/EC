 -- Index Partition
-- 	ALTER table songs
-- 	PARTITION BY Hash(id)
-- 	PARTITIONS 4;
-- Score Partition
	ALTER table songs
	PARTITION BY range(artist_familiarity_int)
	(
		PARTITION p0 VALUES LESS THAN (25),
        PARTITION p1 VALUES LESS THAN (50),
        PARTITION p2 VALUES LESS THAN (75),
		PARTITION p3 VALUES LESS THAN maxvalue
	);