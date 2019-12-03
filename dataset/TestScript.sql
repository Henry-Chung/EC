SELECT count(*)  FROM songs PARTITION (p0) order by id ;
SELECT count(*) FROM songs PARTITION (p1) order by id ;
SELECT count(*)  FROM songs PARTITION (p2) order by id;
SELECT artist_hotttnesss_int FROM songs PARTITION (p3) where title like '%Gun%' order by artist_hotttnesss_int, artist_familiarity_int desc;

select * from test.songs where title like '%Gun%'order by artist_hotttnesss_int desc limit 5;

SELECT * FROM mysql.slow_log where rows_sent <> "0" order by start_time desc limit 10;
