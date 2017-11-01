package game.client;

import game.client.GameStateReceiver.GameState;
import game.client.GameStateReceiver.GameStateReceiver;
import game.client.GameStateReceiver.GameStateUpdated;
import game.client.GameStateReceiver.PlayerState;
import game.client.VisibleGameObjects.*;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

enum CurrentGameState {IDLE, WAITING_FOR_USERS, PLAYING, GAME_OVER}

public class LightCyclesGame {
    private EventListenerList listeners = new EventListenerList();
    private GameStateReceiver receiver = new GameStateReceiver();
    private GameGrid gameGrid;
    private ServerRequester serverRequester;
    private String usersName;
    private Color usersColour;

    public LightCyclesGame(String userName, Color userColour,
                           String serverIP, int serverPort) {
        serverRequester = new ServerRequester(serverIP,serverPort);
        this.usersName = userName;
        this.usersColour = userColour;
        receiver.addGameStateUpdateListener(e -> {
            receivedNewGameState(e);
        });
        receiver.start();
    }

    private void receivedNewGameState(GameStateUpdated e) {
        GameState gameState = e.getGameState();

        for (PlayerState playerState : gameState.getPlayerStates()) {
            System.out.println(String.format("Player: %s x: %s y:%s jetwall enabled: %s",
                    playerState.getName(), playerState.getPosition().width,
                    playerState.getPosition().height, playerState.isJetwallEnabled()));

            //Update player on grid
            try {
                Player player = gameGrid.getPlayerOnGrid(playerState.getName());

                Dimension newPlayerPosition = playerState.getPosition();
                Dimension currentPlayerPosition = player.getPosition();

                System.out.println(
                        String.format("Has moved horizontally: %s " +
                                        "Has moved vertically: %s",
                                currentPlayerPosition.width != newPlayerPosition.width,
                                currentPlayerPosition.height != newPlayerPosition.height)
                );

                //Place jetwall if they have it enabled and they have moved since the last broadcast update
                if ((currentPlayerPosition.width != newPlayerPosition.width ||
                                currentPlayerPosition.height != newPlayerPosition.height)) {
                    if(playerState.isJetwallEnabled()){
                        JetWallDirection direction = JetWallDirection.HORIZONTAL;
                        if (player.getDirection() == PlayerDirection.UP || player.getDirection() == PlayerDirection.DOWN) {
                            direction = JetWallDirection.VERTICAL;
                        }

                        JetWall playerJetWall = new JetWall(player, player.getPosition(), direction);
                        gameGrid.addJetWallToGrid(playerJetWall);
                    }else{
                        gameGrid.raiseGridUpdatedListener();
                    }

                }

                //Move player to new position
                player.setPosition(newPlayerPosition);
                System.out.println(String.format(
                        "Players direction of travel is: %s", player.getDirection().toString()
                ));
                //Set jetwall state
                player.setJetwallEnabled(playerState.isJetwallEnabled());

            } catch (Exception ex) {
                //Player isn't on grid yet - add them
                Player player;
                if (playerState.getName().equals(usersName)) {
                    //If we happen to be adding this users player to the grid, parse in their chosen colour
                    player = new Player(playerState.getName(),
                            playerState.getPosition(), PlayerDirection.UP, usersColour);
                } else {
                    player = new Player(playerState.getName(),
                            playerState.getPosition(), PlayerDirection.UP);
                }
                gameGrid.addPlayerToGrid(player);
            }
        }

        /*If the broadcast message no-longer contains a player that's on the grid, remove them*/
        ArrayList<Player> playersToRemove = new ArrayList<>();
        ArrayList<PlayerState> playerStates = gameState.getPlayerStates();

        for (Player playerOnGrid : gameGrid.getPlayerList()) {
            boolean playerStillExists = false;
            for (PlayerState playerState1 : playerStates) {
                if (playerState1.getName().equals(playerOnGrid.getName())) {
                    playerStillExists = true;
                }
            }
            if (!playerStillExists) {
                playersToRemove.add(playerOnGrid);
            }
        }

        for (Player playerToRemove : playersToRemove) {
            gameGrid.removePlayerFromGrid(playerToRemove);
        }

        //Check for game over
        /*Sometimes we wont be able to 'bind' to the server if other clients are doing the same
        * Just loop until we bind. Ah well.*/
        boolean checkComplete = false;
        while (!checkComplete) {
            try {
                String gameState_ = serverRequester.getRequestResponse("GAME STATE");
                if (gameState_.contains("GAME OVER")) {
                    GameOver(gameState_);
                }
                System.out.printf(
                        "Game state: %s\n", gameState_
                );
                checkComplete = true;
            } catch (Exception ex) {
                System.out.printf("Couldn't connect to the server for this reason: %s\n", ex.getMessage());
            }
        }
    }

    private CurrentGameState getServerGameState() throws Exception {
        /*Asks the server for the current game state
        * Will throw an exception if the server doesn't respond*/
        String response = serverRequester.getRequestResponse("GAME STATE");

        if(response.equals("IDLE")){
            return CurrentGameState.IDLE;
        }else if(response.equals("PLAYING")){
            return CurrentGameState.PLAYING;
        }else if (response.contains("GAME OVER")){
            return CurrentGameState.GAME_OVER;
        }else if(response.equals("WAITING FOR USERS")){
            return CurrentGameState.WAITING_FOR_USERS;
        }else {
            throw new Exception("The server did not respond.");
        }
    }

    public void joinServer() throws Exception {
        /*This method will ask the server to add you to the game, with the specified name
        * If the server is not waiting for players, it throws an exception
        * If the server didn't respond with OKAY, it throws an exception*/

        if (getServerGameState() == CurrentGameState.WAITING_FOR_USERS) {
            String response = serverRequester.getRequestResponse("ADD USER " + usersName);
            if (!response.equals("OKAY")) {
                throw new Exception("The following issue occurred when trying to " +
                        "create a new user on the server: " + response);
            }

            //Request the grid size and initialise the GameGrid object
            try {
                String gridSize = serverRequester.getRequestResponse("GRID SIZE");
                String[] gridSizeArray = gridSize.split(" ");
                Dimension gridDimensions = new Dimension(Integer.parseInt(gridSizeArray[0]),
                        Integer.parseInt(gridSizeArray[1]));
                gameGrid = new GameGrid(gridDimensions);
                System.out.println(String.format("The grid was created with the following dimensions: " +
                        "width: %s height: %s", gridDimensions.width, gridDimensions.height));
            } catch (Exception e) {
                throw new Exception("The server did not respond to the grid size request. Abandon ship!!!!");
            }

        } else {
            throw new Exception("The server is not accepting new users at this time.");
        }
    }

    public GameGrid getGameGrid() {
        return gameGrid;
    }

    public String getUsersName() {
        return usersName;
    }

    public void turnLeft() {
        /*Request server to turn your player left*/
        //getServerResponse(String.format("USER %s TURN left", usersName));
        try {
            serverRequester.sendNonRespondingRequest(String.format("USER %s TURN left", usersName));
        } catch (Exception e) {

        }
    }

    public void turnRight() {
        /*Request server to turn your player right*/
        //getServerResponse(String.format("USER %s TURN right", usersName));
        try {
            serverRequester.sendNonRespondingRequest(String.format("USER %s TURN right", usersName));
        } catch (Exception e) {

        }
    }

    public void beginMovingSlowly() {
        /*Request server to set your player speed to slow*/
        //getServerResponse(String.format("USER %s GO slower", usersName));
        try {
            serverRequester.sendNonRespondingRequest(String.format("USER %s GO slower", usersName));
        } catch (Exception e) {

        }
    }

    public void beginMovingQuickly() {
        /*Request server to set your player speed to fast*/
        //getServerResponse(String.format("USER %s GO faster", usersName));
        try {
            serverRequester.sendNonRespondingRequest(String.format("USER %s GO faster", usersName));
        } catch (Exception e) {

        }
    }

    public void turnOnJetwall() {
        /*Request server to turn on your jet wall*/
        //getServerResponse(String.format("USER %s JETWALL on", usersName));
        try {
            serverRequester.sendNonRespondingRequest(String.format("USER %s JETWALL on", usersName));
        } catch (Exception e) {

        }
    }

    public void turnOffJetwall() {
        /*Request server to turn off your jet wall*/
        //getServerResponse(String.format("USER %s JETWALL off", usersName));
        try {
            serverRequester.sendNonRespondingRequest(String.format("USER %s JETWALL off", usersName));
        } catch (Exception e) {

        }
    }

    public void toggleJetwall() {
        try {
            Player thisPlayer = gameGrid.getPlayerOnGrid(usersName);
            if (thisPlayer.isJetwallEnabled()) {
                turnOffJetwall();
            } else {
                turnOnJetwall();
            }
        } catch (Exception e) {
            System.out.println("---FAILED TO TOGGLE JETWALL---");
        }
    }

    public void leaveGame() throws Exception {
        String response = "";
        try {
            response = serverRequester.getRequestResponse(String.format(
                    "REMOVE USER %s", usersName
            ));
        } catch (Exception e) {

        }

        if (!response.contains("OKAY")) {
            String error = response.replace("FAILED ", "");
            throw new Exception(error);
        }
    }

    public HashMap<String, Integer> getLeaderBoard() throws Exception {
        String response = "";
        try {
            response = serverRequester.getRequestResponse("GET LEADERBOARD");
            System.out.printf("Leaderboard response: %s", response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (response.contains("FAILED")) {
            String error = response.replace("FAILED ", "");
            throw new Exception(error);
        } else if (response.contains("OKAY:")) {
            String leaderBoardString = response.replace("OKAY:", "");
            HashMap<String, Integer> leaderBoard = new HashMap<>();

            String[] leaderBoardEntries = leaderBoardString.split(" ");

            for (String leaderBoardEntry : leaderBoardEntries) {
                String[] nameAndScore = leaderBoardEntry.split(",");
                leaderBoard.put(nameAndScore[0], Integer.parseInt(nameAndScore[1]));
            }
            return leaderBoard;
        } else {
            throw new Exception("Something went wrong when requesting the leader board from the server");
        }
    }

    public void postScoreToLeaderBoard(int score) throws Exception{
        String requestString = String.format("SAVE SCORE %s %s",
                usersName, score);
        serverRequester.sendNonRespondingRequest(requestString);
    }

    private void GameOver(String serverResponseMessage) {
        serverResponseMessage = serverResponseMessage.replace("GAME OVER ", "");
        GameOverOccurred gameOverOccurred = new GameOverOccurred(this, serverResponseMessage);
        raiseGameOverListener(gameOverOccurred);
    }

    public void addGameOverListener(GameOverEvent e) {
        listeners.add(GameOverEvent.class, e);
    }

    private void raiseGameOverListener(GameOverOccurred e) {
        for (GameOverEvent event : listeners.getListeners(GameOverEvent.class)) {
            event.gameOverOccurred(e);
        }
    }

    public void close(){
        //Close this game object. Alright, parties over fellas!
        listeners = null;
        receiver.close();

    }
}
