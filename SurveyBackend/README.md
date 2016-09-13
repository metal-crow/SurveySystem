//TODO autogenerate table from
//create table USER (
id INT NOT NULL auto_increment, 
email VARCHAR(70) NOT NULL UNIQUE, 
password_hash VARCHAR(120) NOT NULL, 
first_name VARCHAR(50), 
last_name VARCHAR(50), PRIMARY KEY(id));


Using Hibernate (http://hibernate.org/orm/), which requires MySQL Connector(https://dev.mysql.com/downloads/connector/j/)
Include all Hibernate/Required jars in your build path, as well as the mysql-connector jar