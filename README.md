# battleshipMultiplayer
a multiplayer game of battleship

players will be able to connect to a server and play against each other

the board has been merged into the client file. originally, the plan was to take input through the GUI, but this proved to be an issue because of multithreading. the GUI couldn't be both a thread and an ActionListener (which it needed to be in order to use buttons) so inputs will be taken from the command line and shown on the user interface. the only things that need to be implemented is setting up the ships with error checking and exit conditions.

- to run, compile all the files in java using _javac *.java_
- the server should be started first with _java battleServer_
- the first client can be run in a different terminal with _java battleClient_ and the second client should also be run with the same command, in a different terminal. only two users are allowed to connect to a session.

the function needed to get user coordinates and assign ships is still a work in progress, currently random values have been assigned for ships, but once the client is running, entering 'send ships' will transmit your ships to the enemy. for now, these values are the same for each player and are set to visible to error check any problems in sending coordinates.
