---------------------------------------------------------------------------------------
Name 	 = Ram Krishna
Entry No = 2016csb1053
Course   = BTP
---------------------------------------------------------------------------------------
For Java File UDPServer.java

For Compiling:
	javac -cp jar/*:. UDPServer.java
For Running:
	java -cp jar/*:. UDPServer

--------------------------------------
For Java File UDPClient.java

For Compiling:
	javac UDPClient.java

For Running:
	java UDPClient

--------------------------------------
For Java File Subscriber.java

For Compiling:
	javac Subscriber.java

For Running:
	java Subscriber


--------------------------------------
For opening mysql from terminal

Command: mysql -u root -p
password: abhi12345
Show databases: show databases;
USE databases: use databasename;
For Schema of the table: SHOW COLUMNS FROM table_name;
For inserting into table: 
insert into sub_info (pub_ip, pub_port, sub_ip, sub_port) values('172.21.13.76',1985, '172.21.4.237', 6700);
insert into sub_info (pub_ip, pub_port, sub_ip, sub_port) values('172.26.15.67',6566, '172.26.15.67', 6700);
insert into sub_info (pub_ip, pub_port, sub_ip, sub_port) values('172.26.15.67',6566, '172.26.15.67', 6701);
insert into sub_info (pub_ip, pub_port, sub_ip, sub_port) values('172.26.15.67',6566, '172.26.15.67', 6702);
select * from sub_info;
--------------------------------------

Server Port is 9876
Publisher Port is 6566

--------------------------------------
Commands

For finding the ip address : hostname -I, ip addr
For clearing the mysql terminal : system clear

