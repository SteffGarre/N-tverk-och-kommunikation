# Nätverk-och-kommunikation

Projekt består av 4 deluppggifter ur kursen IK1203 Networks and Communication


**Task 1:**
The first programming assignment is to implement a TCP client, called TCPAsk. TCPAsk operates in a straight-forward manner:

Open a TCP connection to a server at a given host address and port number.
Take any data that the server sends, and and print the data.
TCPAsk takes an optional string as parameter. This string is sent as data to the server when the TCP connection is opened, followed by a newline character (linefeed '\n').
****

**Task 2**
In this part, you will implement a web server. The server accepts an incoming TCP connection, reads data from it until the client closes the connection, and returns ("echoes") an HTTP response back with the data in it. 
Your job is to implement a class called HTTPEcho. It's "main" method implements the TCP server. It takes one argument: the port number.
****

**Task 3**
In this part, you will implement another web server – HTTPAsk. This is a web server that uses the client you did in Task 1. When you send an HTTP request to HTTPAsk, you provide a hostname, a port number, and optionally a string as parameters for the request.
****

**Task 4**
In this task, you will take the HTTPAsk server you did in Task 3, and turn it into a concurrent server. The server you did for Task 3 deals with one client at a time (most likely). A concurrent server can handle multiple clients in parallel.
****

**Notera**: Beskrivningar av Task 1-4 är enligt kursen IK1203 Networks and Communication

