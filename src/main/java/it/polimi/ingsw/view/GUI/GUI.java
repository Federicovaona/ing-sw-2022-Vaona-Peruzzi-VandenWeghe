package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.assistant.AssistantCard;
import it.polimi.ingsw.model.assistant.AssistantDeckName;
import it.polimi.ingsw.model.assistant.DeckAssistant;
import it.polimi.ingsw.model.character.CardEffect;
import it.polimi.ingsw.model.character.CharacterCard;
import it.polimi.ingsw.model.game.Difficulty;
import it.polimi.ingsw.model.game.GameMode;
import it.polimi.ingsw.model.school.School;
import it.polimi.ingsw.model.school.TColor;
import it.polimi.ingsw.model.student.SColor;
import it.polimi.ingsw.model.student.Student;
import it.polimi.ingsw.model.table.Table;
import it.polimi.ingsw.network.LobbyForPrint;
import it.polimi.ingsw.observer.ObservableView;
import it.polimi.ingsw.observer.ObserverView;
import it.polimi.ingsw.view.GUI.scene.*;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Implements the GUI (graphical user interface)
 */
public class GUI extends ObservableView implements View {
    private static ArrayList<String> playerList = new ArrayList<>();
    private ArrayList<SchoolController> schools = new ArrayList<>();
    private Table table;
    private boolean gameStarted = false;

    int studentAcrobat1 = 131, studentAcrobat2 = 131;

    /**
     * Asks the ip address and the server port for the connection to the server
     */
    @Override
    public void askConnect() {
        ConnectToServerScene connectToServer = new ConnectToServerScene();
        connectToServer.addAllObservers(observers);
        Platform.runLater(() -> {
            GuiManager.changeRootPane(observers, "/fxml/connect_to_server_scene");
        });
    }

    /**
     * Asks the player's info and the game id that the player wants to join (or create)
     * @param lobbyMap map for print the information of the lobby
     */
    @Override
    public void askLobby(Map<String, LobbyForPrint> lobbyMap) {
        SetupGameScene setupGame = new SetupGameScene();
        setupGame.addAllObservers(observers);
        setupGame.setLobbyMap(lobbyMap);

        Platform.runLater(() -> GuiManager.changeRootPane(setupGame, "/fxml/setup_game_scene") );
    }

    /**
     * Asks at the player who creates the lobby the difficulty and the gameMode
     */
    @Override
    public void askPlayersNumberAndDifficulty() {
        NewGameScene pNandDifficulty = new NewGameScene();
        pNandDifficulty.addAllObservers(observers);
        Platform.runLater(()-> GuiManager.changeRootPane(observers,"/fxml/new_game_scene") );
    }

    /**
     * Asks at the player to select deck and towers
     * @param towerColors array with the colors of the towers
     * @param assistantDeckNames array with the string's name of decks
     */
    @Override
    public void askTowerColorAndDeck(ArrayList<TColor> towerColors, ArrayList<AssistantDeckName> assistantDeckNames) {
        TowerAndDeckScene towerAndDeckScene = new TowerAndDeckScene();
        towerAndDeckScene.addAllObservers(observers);
        towerAndDeckScene.setAssistantDeckNames(assistantDeckNames);
        towerAndDeckScene.setTowerColors(towerColors);
        Platform.runLater(() -> GuiManager.changeRootPane(towerAndDeckScene, "/fxml/tower_and_deck_scene") );
    }

    /**
     * Shows if the login was successful
     * @param nickname of the player
     * @param gameId chosen by the player
     * @param playerDate of the player
     * @param wasJoined false if the player hasn't joined a match yet
     */
    @Override
    public void showLogin(String nickname, String gameId, GregorianCalendar playerDate, boolean wasJoined) {
        if (wasJoined){
            notifyObserver(obs -> obs.createNickname(nickname));
            notifyObserver(obs -> obs.createPlayerDate(playerDate));
            GUI.playerList.add(nickname);
            showMessage("You joined the game as "+nickname);
        }
        else {
            showMessage("Game is already full");
            notifyObserver(ObserverView::askLobbyServerInfo);
        }
    }

    /**
     * Shows a generic message
     * @param message to be shown
     */
    @Override
    public void showMessage(String message) {
        if (message.equals("Something went wrong.") || message.equals("**** TIE ****")){
            TieScene tie = new TieScene();
            tie.addAllObservers(observers);
            Platform.runLater(() -> GuiManager.changeRootPane(tie,"/fxml/tie_scene"));
        }
        else if(message.substring(0,6).equals("\nRound"))
            Platform.runLater(() -> GuiManager.getMainScene().updateRoundText(message));
        else
            Platform.runLater(() -> MessageScene.display(null,null, message));

    }

    /**
     * Shows a waiting message
     * @param message to be shown
     */
    @Override
    public void showWaitingMessage(String message) {
        Platform.runLater(() -> GuiManager.changeRootPane(observers, "/fxml/waiting_scene") );
    }

    /**
     * Shows the updated School of the player
     * @param school of the player
     * @param nickname of the player
     * @param trash card of the player
     * @param difficulty of the match
     * @param coins coinScore of the player
     * @param gameMode of the match
     * @param teamMate of the player (if coop mode)
     */
    @Override
    public void showPersonalSchool(School school, String nickname, AssistantCard trash, Difficulty difficulty, int coins, GameMode gameMode, String teamMate) {
        SchoolController schoolController = new SchoolController(school,trash, difficulty, coins);
        if(nickname.equals("Your "))
            Platform.runLater(() -> {
                try {
                    GuiManager.getMainScene().updatePersonalSchool(schoolController, gameMode, teamMate);
                    Platform.runLater(() -> GuiManager.changeRootMainScene(observers));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        else
            Platform.runLater(() ->GuiManager.getMainScene().updateOtherSchool(schoolController, gameMode, nickname));
    }

    /**
     * Shows the updated list of islands
     * @param table of the match
     * @param difficulty of the match
     */
    @Override
    public void showListOfIsland(Table table, Difficulty difficulty) {
        this.table = table;
       Platform.runLater(() -> GuiManager.getMainScene().updateIslands(table.getListOfIsland()));
    }

    /**
     * Shows the updated table of the match
     * @param table of the match
     * @param difficulty mode of the match
     */
    @Override
    public void showTable(Table table, Difficulty difficulty) {
        this.table = table;
        if (!gameStarted) {
            Platform.runLater(() -> GuiManager.changeRootMainScene(observers));
            Platform.runLater(() -> {
                try {
                    GuiManager.getMainScene().initializeTable(difficulty, table);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            gameStarted = true;
        }else{
            Platform.runLater(() -> GuiManager.getMainScene().updateTable(table));
            Platform.runLater(() -> GuiManager.changeRootMainScene(observers));
        }
    }

    /**
     * Shows the updated cards in hand of the player
     * @param deckAssistant of the player
     */
    @Override
    public void showDeckAssistant(DeckAssistant deckAssistant) {
        ViewDeckScene viewDeckScene = new ViewDeckScene(deckAssistant);
        Platform.runLater(()->GuiManager.getMainScene().updateAssistantCardDeck(viewDeckScene));
    }

    /**
     * Shows Winning message and disconnects the player
     */
    @Override
    public void showWinMessage() {
        WinnerScene win = new WinnerScene();
        notifyObserver(ObserverView::updateDisconnect);
        Platform.runLater(() -> GuiManager.changeRootPane(win,"/fxml/winner_scene"));

    }

    /**
     * Shows Losing message and disconnects the player
     * @param nickname of the player that won the match
     */
    @Override
    public void showLoseMessage(String nickname) {
        LoserScene lost = new LoserScene();
        notifyObserver(ObserverView::updateDisconnect);
        Platform.runLater(() -> GuiManager.changeRootPane(lost, "/fxml/loser_scene"));
    }

    /**
     * Shows the player an error message
     * @param message to be shown
     */
    @Override
    public void showErrorMessage(String message) {
        Platform.runLater(() -> MessageScene.display("Error","Error", message));
    }

    /**
     * Shows coins and character cards on table
     * @param coins on table
     * @param characterCardsOnTable on table
     */
    @Override
    public void showCoinAndCharacterCards(int coins, ArrayList<CharacterCard> characterCardsOnTable) {
        Platform.runLater(() -> GuiManager.getMainScene().updateCoinOnTableAndCharacterCards(coins,characterCardsOnTable));
    }

    /**
     * Asks the player which Assistant Card wants to play
     */
    @Override
    public void askAssistantCardToPlay() {
        Platform.runLater(() -> {
            GuiManager.getMainScene().setPlanning(true);
            GuiManager.getMainScene().disabilitateCharacterCards(true);
            GuiManager.getMainScene().getAssistantDeck().addAllObservers(observers);});
        showMessage("PLANNING PHASE\nChoose an Assistant Card");
    }

    /**
     * Asks at the player which Character Card he wants to play
     * @param choice false if the player hasn't already decided if he wants to activate a character card,
     *               true if the player wants to play a certain character card
     * @param coins of player
     * @param list of Character Cards on table
     */
    @Override
    public void askCharacterCardToPlay(boolean choice, int coins, ArrayList<CharacterCard> list) {
        if(!choice) {
            notifyObserver(obs -> obs.chooseCharacterCard("YES",choice));
        }else{
            switch(GuiManager.getMainScene().getCardSelected().getCardEffect()){
                case ABBOT:
                case COURTESAN:
                    GuiManager.getMainScene().getCharacterCardControllerMap().get(GuiManager.getMainScene().getCardSelected().getCardEffect()).disableStudents(false);
                    GuiManager.getMainScene().disableDoneButton(true);
                    GuiManager.getMainScene().reactivateActionPhase();
                    break;
                case ACROBAT:
                    GuiManager.getMainScene().getCharacterCardControllerMap().get(GuiManager.getMainScene().getCardSelected().getCardEffect()).disableStudents(false);
                    GuiManager.getMainScene().reactivateActionPhase();
                    GuiManager.getMainScene().getPersonalSchoolController().addMouseEventToStudentsOfEntry();
                    GuiManager.getMainScene().getPersonalSchoolController().disableEntry(true);
                    GuiManager.getMainScene().disableDoneButton(false);
                    break;
                case BARD:
                    GuiManager.getMainScene().getPersonalSchoolController().addMouseEventToStudentsOfEntry();
                    GuiManager.getMainScene().getPersonalSchoolController().addMouseEventToLastStudentsOfHall();
                    GuiManager.getMainScene().getPersonalSchoolController().disableEntry(false);
                    GuiManager.getMainScene().disableDoneButton(false);
                    break;
                case HERALD:
                case CURATOR:
                    GuiManager.getMainScene().getCharacterCardControllerMap().get(GuiManager.getMainScene().getCardSelected().getCardEffect()).disableStudents(false);
                    GuiManager.getMainScene().disableDoneButton(true);
                    GuiManager.getMainScene().addMouseEventToIslands();
                    GuiManager.getMainScene().reactivateActionPhase();
                    break;
                default:
                    GuiManager.getMainScene().getCharacterCardControllerMap().get(GuiManager.getMainScene().getCardSelected().getCardEffect()).disableStudents(false);
                    GuiManager.getMainScene().disableDoneButton(true);
                    notifyObserver(obs -> obs.chooseCharacterCard(GuiManager.getMainScene().getCardSelected().getCardEffect().toString(),choice));
                    break;
            }

        }
    }

    /**
     * Handles the effect of the Herbalist Card and Junkdealer Card
     * Asks the player which color he wants to select
     * @param color of the students to block in the influence calculation or to remove from the entry
     */
    @Override
    public void askColorToBlock(CardEffect color) {
        ColorChoice colorChoice = new ColorChoice();
        colorChoice.addAllObservers(observers);
        Platform.runLater(() -> {GuiManager.newStagePane(colorChoice, "/fxml/color_choice_scene");
            GuiManager.getMainScene().reactivateActionPhase();});

    }

    /**
     * Asks the player which students  he wants to move and where
     * @param entry of the player's school
     */
    @Override
    public void askPlaceAndStudentForMove(ArrayList<Student> entry) {
        Platform.runLater(() ->
            {GuiManager.getMainScene().getPersonalSchoolController().disableEntry(false);
            GuiManager.getMainScene().setActionStudent(true);
                GuiManager.getMainScene().disabilitateCharacterCards(false);});
        showMessage("ACTION PHASE\nMove a student from your entry ");

    }

    /**
     * Asks the player how many steps he wants to move Mother Earth
     * @param maxSteps max steps the player can choose
     * @param table of the match
     * @param difficulty of the match
     */
    @Override
    public void askMotherEarthSteps(int maxSteps, Table table, Difficulty difficulty) {
        Platform.runLater(() ->
            {GuiManager.getMainScene().getPersonalSchoolController().disableEntry(true);
            GuiManager.getMainScene().setActionStudent(false);
            GuiManager.getMainScene().disabilitateMother(table,maxSteps,false);
            GuiManager.getMainScene().setActionMother(true);});
        showMessage("Move MotherEarth");
    }

    /**
     * Asks the player which cloud he wants to choose
     * @param table of the match
     */
    @Override
    public void askCloud(Table table) {
        Platform.runLater(() ->
            {GuiManager.getMainScene().disabilitateMother(table,GuiManager.getMainScene().getMaxSteps(),true);
            GuiManager.getMainScene().setActionMother(false);
            GuiManager.getMainScene().getCloudController().disabilitateCloud(false);
            GuiManager.getMainScene().setActionCloud(true);});
        showMessage("Choose a Cloud");
    }

    /**
     * Asks the player which island he wants to select (or student on card for cards)
     * @param choice true for the selection of an island (that includes the effect of herald and curator) otherwise
     * @param characterCard played in the round
     * @param indexAcrobat index for the acrobat effect
     * @param school of current player
     */
    @Override
    public void askId(boolean choice, CharacterCard characterCard, int indexAcrobat, School school) {
        if (choice) {
            if (characterCard != null) {
                if (characterCard.getCardEffect().equals(CardEffect.HERALD))
                    notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getStudentDestinationIslandId(),choice, indexAcrobat, false));
                if (characterCard.getCardEffect().equals(CardEffect.CURATOR))
                    notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getStudentDestinationIslandId(),choice, indexAcrobat, false));
            } else
                notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getStudentDestinationIslandId(),choice, indexAcrobat, false));
        }else{
            if (characterCard.getCardEffect().equals(CardEffect.ACROBAT)) {
                if(GuiManager.getMainScene().getCharacterCardControllerMap().get(GuiManager.getMainScene().getCardSelected().getCardEffect()).getStudentSelectedForSwitch()[indexAcrobat/2]!=-1) {
                    if (indexAcrobat % 2 == 1) {
                        notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getCharacterCardControllerMap().get(GuiManager.getMainScene().getCardSelected().getCardEffect()).getStudentSelectedForSwitch()[indexAcrobat / 2], choice, indexAcrobat, false));
                    } else {
                        notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getPersonalSchoolController().getStudentSelectedForSwitch()[indexAcrobat / 2], choice, indexAcrobat, false));
                    }
                }else{
                    notifyObserver(obs -> obs.chooseId(-2, choice, indexAcrobat, true));
                }
            }
            else if (characterCard.getCardEffect().equals(CardEffect.BARD)) {
                if(GuiManager.getMainScene().getPersonalSchoolController().getStudentSelectedForSwitch()[indexAcrobat/2]!=-1) {
                    if (indexAcrobat % 2 == 1) {
                        notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getPersonalSchoolController().getStudentSelectedForSwitchHall()[indexAcrobat / 2], choice, indexAcrobat, false));
                    } else {
                        notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getPersonalSchoolController().getStudentSelectedForSwitch()[indexAcrobat / 2], choice, indexAcrobat, false));
                    }
                }else{
                    notifyObserver(obs -> obs.chooseId(-2, choice, indexAcrobat, true));
                }
            }
            else if (characterCard.getCardEffect().equals(CardEffect.COURTESAN)){
                notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getCharacterCardControllerMap().get(CardEffect.COURTESAN).getStudentSelected(), choice, indexAcrobat, true));
            }
            else {
                notifyObserver(obs -> obs.chooseId(GuiManager.getMainScene().getCharacterCardControllerMap().get(CardEffect.ABBOT).getStudentSelected(), choice, indexAcrobat, true));
            }
        }
    }
}
