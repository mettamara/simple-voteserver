simple-voteserver
=================

Simple Stateful Vote-Counting Server

The seperation of components, is a little bit extreme for the functionality required for the project, but trying to demonstrate good engineering principles such as composition, dependency injection for unit testing, project structure, etc.

The choice of modules was simply due to familiarity and development speed. If this was a public service, I would likely use Coda Hale's dropwizard to generate the component wireframe. Reason for not using here was unfamiliarity.

AddMember and Vote both require that the input values are alphanumeric only.  Could easily extend to support escaped characters, such as spaces and stored the escaped values in the data layer. Simplicity for this sample. Inputs are case sensitive.

curl -v  -d "agent=isaac" http://localhost:8080/member
curl -v  -d "agent=frank&vote=frank" http://localhost:8080/vote
curl -v http://localhost:8080/victory
curl -v  -d "" http://localhost:8080/rst

