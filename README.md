Players can to connect to a server and play against each other.

The original plan was to take input through the GUI, but this proved to be an issue because of multithreading. Input is instead taken through command line, and thoroughly error-checked. To run, clone the repo and compile all the files with the command _javac *.java_

The server has to be launched first, this is done with the command _java battleServer_

After the server has been launched, the clients can then be launched with the command _java battleClient_

Socket connections are launched on local host, on port 6969. 

The first thing that happens when the client is launched is the user being asked to place their ships. The GUI appears, so they can look at it while making their selection. The confirmed coordinates are echoed back to the user, or a reason is given to refuse them. If player one finishes their selection first, they are blocked from sending coordinates until player two has set up their ships. Inversely, if player two finishes placing their ships first, they are blocked because player one goes first.

Once the ships have been placed, the player has to enter "send" in order to start the game. Players will then take turns guessing. A player is blocked from trying to attack a coordinate out of turn. When one player has won the game, a Game Over screen appears for both players. The server and client will have to be started again in order for them to play another game.
