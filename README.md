simple-voteserver
=================

Simple Stateful Vote-Counting Server for a sample programming test.

The server is a simple rest server to support adding member agents, supporting agent votes for a single ballot, checking to see if a majority of current agents have agreed on a given value, and the ability to reset the server's persisted state.

Valid http requests, respond with a 200 http response code and simple string of either '-' for modifications (posts) or the value of a read request (get). No newline characters are appended to the request value. Malformed requests return a 400 response code, and invalid URI's result in a 404. 

Client Assumptions/Behavior

 * Members/Agents are unique and case sensitive. 
 * Each member may only vote once. 
 * The vote value can be any arbitrary alphanumeric string.  No special characters, spaces, etc. 
 * Vote value and member cannot be empty string
 * Any extra parameters sent are ignored. 

Run the embedded server with: [java -jar simple-voteserver-0.1-jar-with-dependencies.jar]

Design Decisions
================
The seperation of components, is a little bit extreme for the functionality required for the project, but trying to demonstrate good engineering principles such as composition, dependency injection for unit testing, project structure, etc.

The choice of modules was simply due to familiarity and development speed. If this was a production service, Coda Hale's dropwizard would be a better candidate to generate the component wireframe. 

For simplicity, add member and Vote both require that the input values are alphanumeric only. This could easily be extended to support escaped characters, such as spaces and stored the escaped values in the data layer.



Packaging and Testing
==============
simple-voteserver uses maven for building and executing the tests.
To build: mvn package
Execute unit tests: mvn test

To run server: java -jar simple-voteserver-0.1-jar-with-dependencies.jar 

Sample curl requests
 * curl -v  -d "agent=isaac" http://localhost:8080/member
 * curl -v  -d "agent=isaac&vote=frank" http://localhost:8080/vote
 * curl -v http://localhost:8080/victory
 * curl -v  -d "" http://localhost:8080/rst

A small multiprocess python load script tester.py is available. This script requires that the python library requests is install (python-requests on debian).

A shell file runLoadAndGraph.sh will execute the load script and then aggregate perf4j stats that measure server request time by service target.  This script assumes a maven repository is accessible and that the perfStats.log is in the root directory. The runs may not be long enough produce viable graphs, but sliding 30 second window aggregations are shown. The graph generation script may need to be run seperately: java -jar [path to perf4j-0.9.16.jar] --graph perfGraphs.html [path to perfStats.log]


Database
============
simple-voteserver uses an embedded h2 database which is persisted in a file in the working directory. For ease of testing the datbase is re-constructed everytime the db connection pool is established. MySQL or postgres would likely improve performance greatly.

Can start DB in server mode via (replace jar classpath) : java -cp ~/.m2/repository/com/h2database/h2/1.3.170/h2-1.3.170.jar org.h2.tools.Server

If executed from same directory as db source file, can connect in browser via
jdbc:h2:tcp://localhost/voteServer
with user name and password ="sa"

See: http://www.h2database.com/html/cheatSheet.html

Note that a database file can only be opened by one process. So a server will block a stand alone server process, and vice versa.

TODOs
===========
 * Configure Spring IoC
 * Inject application properties through spring / prop file. Not many needed.
 



