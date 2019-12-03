SET Global slow_query_log = 'ON';
SET GLOBAL log_output='TABLE';
SET Global long_query_time = 0;
SET Global log_queries_not_using_indexes =1;

SELECT * FROM mysql.slow_log;