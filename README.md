# Implement Top - K Queries with Set-Defined Selections 

Author: Meng-Han Chung, Mayur Jain

## Research Paper Implement

Stupar, Aleksandar, and Sebastian Michel. "Being picky: processing top-k queries with set-defined selections." Proceedings of the 21st ACM international conference on Information and knowledge management. ACM, 2012.

### Dataset

Million Song Dataset
Reference: <http://millionsongdataset.com/>

### Requirement and Environment

1. MySQL
2. mysql-connector-java-8.0.18.jar
3. Eclipse
4. Java SE Development

### Method

/dataset/songs.csv
1. Dowload the dataset from the website of Million Song. Then, add 3 columns in the songs table (id, artist_hotttnesss_int, artist_familiarity_int).

/dataset/setQueryLog.sql
2. Activate the query log by using the script in file dataset.

### How to run the code

Excute the code by using Eclipse with mysql-connector-java-8.0.18.jar library
