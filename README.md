# battleshipMultiplayer
a multiplayer game of battleship

players will be able to connect to a server and play against each other

the board has been merged into the client file. originally, the plan was to take input through the GUI, but this proved to be an issue because of multithreading. the GUI couldn't be both a thread and an ActionListener (which it neededto be in order to use buttons) so inputs will be taken from the command line and shown on the user interface. the only things that need to be implemented is setting up the ships, transmitting those coordinates, and implementing some error checking and exit conditions.

- to run, compile all the files in java using _javac *.java_
- the server should be started first with java _battleServer_
- the first client can be run in a different terminal with _java battleClient_ and the second client should also be run with the same command, in a different terminal. only two users are allowed to connect to a session

currently, the socket connection is opened, the client launches the GUI, and the messages sent are displayed to each user.
