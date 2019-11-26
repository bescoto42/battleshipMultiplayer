# battleshipMultiplayer
a multiplayer game of battleship

players will be able to connect to a server and play against each other

right now, the files run independently and still need to be merged into two master documents, one for the client and the other for the server. each files shows a different feature that will be added to the overall program:

battleClient is the client side of the socket connection, which sends the user's input to the server, and then to the other user

battleServer is the server side of the socket connection, which handles the connection and manages the messages from one client to another

Board is a graphic user interface, which currently doesn't do much but take the input from one player and reveal the image of the corresponding coordinate on the enemy's side. also included are files that the Board uses, including the gameboard, and temporary icons to show in the grid squares

- to run, compile all the files in java using _javac *.java_
- the server should be started first with java _battleServer_
- the first client can be run in a different terminal with _java battleClient_ and the second client should also be run with the same command, in a different terminal. only two users are allowed to connect to a sesson
- the gameboard can be run with _java Board_

ideally, the code to manage the client's socket connection will be added to Board.java so that when the program is launched, the user will be asked to first place their ships, then the socket connection will be opened, a preliminary conversation between the client sockets will transmit the user's ship's positions, and then gameplay will start. the users will take turns guessing where their enemy's ships are, with a blocking mechanism in place to prevent the user from guessing more than one time.
