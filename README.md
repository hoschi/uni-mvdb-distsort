Description
========================================
Distsort is a project to sort an array of strings in a distributed
way. There is a server and a client implementation. One to n
clients can connect to a server. A client can sort a transmitted block
of data and return it sorted to the server.

Joins is a project to test several distributed join algorithms

 * ship as a whole
 * fetch as needed
 * semi join

Both projects are tested with jUnit and EasyMock. The german report is
generated with dojo toolkit and "generate toc" js script.

Make
========================================

This project uses maven as the build system, use

	mvn clean package

to generate "one jar" files in the target directories of each project.

Command line execution - distsort
========================================
start the server with
	
	java -jar distsort/sortserver/target/sortserver-1.0-SNAPSHOT.one-jar.jar TIMEOUT_FOR_NODE_CONNECTION 

and then connect the nodes with

	java -jar distsort/sortclient/target/sortclient-1.0-SNAPSHOT.one-jar.jar SERVER_IP

and watch the log output on stdout.

Command line execution - joins
========================================

start the server with
	
	java -jar joins/joinserver/target/joinserver-1.0-SNAPSHOT.one-jar.jar TIMEOUT_FOR_NODE_CONNECTION ROW_COUNT

and then connect the nodes with

	java -jar joins/joinnode/target/joinnode-1.0-SNAPSHOT.one-jar.jar SERVER_IP

and watch the log output on stdout.
