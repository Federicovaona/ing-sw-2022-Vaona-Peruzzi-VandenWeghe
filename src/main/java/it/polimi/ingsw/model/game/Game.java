package it.polimi.ingsw.model.game;


import it.polimi.ingsw.model.assistant.AssistantCard;
import it.polimi.ingsw.model.character.CardEffect;
import it.polimi.ingsw.model.character.CharacterCard;
import it.polimi.ingsw.model.island.IslandCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Team;
import it.polimi.ingsw.model.student.SColor;
import it.polimi.ingsw.model.student.Student;
import it.polimi.ingsw.model.table.Table;

import java.util.ArrayList;
import java.util.Objects;

public class Game {

    //private int gameId;   servirebbe per partite multiple
    private GameMode gameMode;
    private ArrayList<Player> listOfPlayers;
    private State state;
    private ArrayList<Player> order;
    private Difficulty difficulty;
    private Table table;
    private ArrayList<Team> team;

    /**
     * Default constructor.
     */
    public Game() {
        this.gameMode = null;
        this.listOfPlayers = new ArrayList<>();
        this.state = null;
        this.order = new ArrayList<>();
        this.difficulty = null;
        this.table = new Table();
        this.team = new ArrayList<>();

        /**table.generateIslandCards();
        table.generateMotherEarth();
        table.generateCloudNumber(gameMode);  */
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public ArrayList<Player> getListOfPlayers(){
        return listOfPlayers;
    }

    public Player getPlayer(String nickname){
        int indexPlayer = getPlayerListByNickname().indexOf(nickname);
        return listOfPlayers.get(indexPlayer);
    }

    public ArrayList<String> getPlayerListByNickname() {
        ArrayList<String> playerList = new ArrayList<>();
        for (int i = 0; i < listOfPlayers.size(); i++) {
            playerList.add(listOfPlayers.get(i).getNickname());
        }
        return playerList;
    }

    /** aggiunge giocatore alla lista giocatori */
    public void addPlayer(Player player) {
        if(listOfPlayers.size()<4){
            listOfPlayers.add(player);
        }
    }

    public State getState(){
        return state;
    }

    public void setState(State state){
        this.state = state;
    }

    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

    public Difficulty getDifficulty(){
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty; }

    public ArrayList<Player> getOrder(){
        return order;
    }

    public void setOrder(ArrayList<Player> order, String nickname) {
        order.add(getPlayer(nickname));
        this.order = order;
    }

    /**
     * winnerIs deve rimanere all'interno di Game   (?)
     */
    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public ArrayList<Team> getTeam() {
        return (ArrayList<Team>) team.clone();
    }

    public boolean gameIsFinished(String nickname) {
        Player player = getPlayer(nickname);

        return player.getDeckOfPlayer().getCardsInHand().isEmpty() ||
                player.getPersonalSchool().getTower().isEmpty() ||
                table.getBag().isEmpty() ||
                table.getListOfIsland().size() == 3;
    }

    public void decreaseCoinScore(String nickname, int decreaseValue) {
        Player activePlayer = getPlayer(nickname);
        activePlayer.setCoinScore(activePlayer.getCoinScore() - decreaseValue);
    }

    /** AssistantName is the card chosen by the Player, nickname is the player that chooses the assistant card to play */
    public AssistantCard playAssistantCard(String assistantName, String nickname){
        
        Player activePlayer = getPlayer(nickname);
        AssistantCard assistantCardPlayed = activePlayer.getAssistantCard(assistantName);
        
        /** DA FARE: Controllo se il giocatore può giocare quella carta (non è già stata giocata da altri) o se è l'unica che può mettere */
                
        activePlayer.setTrash(assistantCardPlayed);
        activePlayer.setHasAlreadyPlayed(true);
        activePlayer.getDeckOfPlayer().getCardsInHand().remove(assistantCardPlayed);

        return assistantCardPlayed;
    }

    public void playCharacterCard(CardEffect cardEffect, String nickname) {

        Player activePlayer = getPlayer(nickname);
        CharacterCard characterCardPlayed = table.getCharacterCard(cardEffect);

        if (characterCardPlayed.getCoinOnCard()) {
            if (activePlayer.getCoinScore() >= characterCardPlayed.getCostCharacter() + 1) {
                decreaseCoinScore(nickname, characterCardPlayed.getCostCharacter() + 1);
                getTable().increaseCoinsOnTable(characterCardPlayed.getCostCharacter() + 1);
            } else {
                System.out.println("NON HAI ABBASTANZA MONETE! ");
                /** Rifai scelta */
                return;
            }
        } else {
            if (activePlayer.getCoinScore() >= characterCardPlayed.getCostCharacter()) {
                decreaseCoinScore(nickname, characterCardPlayed.getCostCharacter());
                getTable().increaseCoinsOnTable(characterCardPlayed.getCostCharacter());
                characterCardPlayed.setCoinOnCard(true);
            } else {
                System.out.println("NON HAI ABBASTANZA MONETE! ");
                /** Rifai scelta */
            }
        }

        //selezione
        switch (characterCardPlayed.getCardEffect()) {
            /** 1 */
            case ABBOT:
                Student studentChosen = null;
                IslandCard islandCardChosen = null;
                //notify (observer)----> studentChosen
                for (Student s : characterCardPlayed.getStudentsOnCard()) {
                    if (s.equals(studentChosen)) {
                        characterCardPlayed.getStudentsOnCard().remove(s);
                        getTable().getListOfIsland().get(islandCardChosen.getIdIsland() - 1).getStudentOnIsland().add(s);
                    }
                }
                characterCardPlayed.getStudentsOnCard().add(getTable().getBag().get(0));
                getTable().getBag().remove(0);

                /** 2 */
            case HOST:
                characterCardPlayed.getCardEffect().setHostPlayed(true);

            case HERALD:   /** 3 */
                IslandCard islandChosen = null;
                //notify (observer)----> islandChosen
                ArrayList<Player> playersList = new ArrayList<>(getListOfPlayers());

                islandChosen.calculateInfluence(playersList, characterCardPlayed.getCardEffect());
                islandChosen.buildTowerOnIsland(playersList, characterCardPlayed.getCardEffect());
                getTable().joinIsland(getTable().getListOfIsland());
                break;

            case BEARER:   /** 4 */
                characterCardPlayed.getCardEffect().setBearerPlayed(true);
                break;
            case CURATOR:   /** 5 */
                IslandCard islandChosenTwo = null;
                //notify (observer)----> islandChosen

                islandChosenTwo.setXCardOnIsland(true);
                if (islandChosenTwo.getXCardCounter() < 4 && characterCardPlayed.getCardEffect().getXCardOnCard() > 0) {
                    islandChosenTwo.setXCardCounter(islandChosenTwo.getXCardCounter() + 1);
                    islandChosenTwo.setXCardOnIsland(true);
                    characterCardPlayed.getCardEffect().setXCardOnCard(characterCardPlayed.getCardEffect().getXCardOnCard() - 1);
                } else
                    System.out.println(" Non puoi!!!");
                break;

            case CENTAUR:   /** 6 */
                characterCardPlayed.getCardEffect().setCentaurPlayed(true);
                break;

            case ACROBAT:   /** 7 */
                for (int i = 0; i < 3; i++) {
                    Student choice = null;  //nuovo studente da mettere nella entry
                    Student toChange = null;    //studente da togliere dalla entry
                    //notify (observer)---->scelta studente in entry da scambiare
                    activePlayer.getPersonalSchool().getEntry().remove(toChange);
                    //notify (observer)---->scelta studente nella carta da scambiare
                    activePlayer.getPersonalSchool().getEntry().add(choice);
                    characterCardPlayed.getStudentsOnCard().remove(choice);
                    characterCardPlayed.getStudentsOnCard().add(toChange);
                }
                break;

            case KNIGHT:   /** 8 */
                characterCardPlayed.getCardEffect().setKnightPlayed(true);
                break;

            case HERBALIST:   /** 9 */
                SColor colorChosen = null;
                //notify (observer)----> colorChosen
                colorChosen.lockColor();
                break;

            case BARD:   /** 10 */
                // TO DO: controlli se sono pieni
                /** togli da entry e metti in hall */
                for (int i = 0; i < 3; i++) {
                    Student choice = null;
                    //notify (observer)---->scelta 2 studenti
                    if (choice.getsColour().equals(SColor.GREEN)) {
                        activePlayer.getPersonalSchool().getGTable().add(choice);
                        getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getGTable());
                        activePlayer.getPersonalSchool().getEntry().remove(choice);
                    } else if (choice.getsColour().equals(SColor.RED)) {
                        activePlayer.getPersonalSchool().getRTable().add(choice);
                        getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getRTable());
                        activePlayer.getPersonalSchool().getEntry().remove(choice);
                    } else if (choice.getsColour().equals(SColor.YELLOW)) {
                        activePlayer.getPersonalSchool().getYTable().add(choice);
                        getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getYTable());
                        activePlayer.getPersonalSchool().getEntry().remove(choice);
                    } else if (choice.getsColour().equals(SColor.PINK)) {
                        activePlayer.getPersonalSchool().getPTable().add(choice);
                        getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getPTable());
                        activePlayer.getPersonalSchool().getEntry().remove(choice);
                    } else if (choice.getsColour().equals(SColor.BLUE)) {
                        activePlayer.getPersonalSchool().getBTable().add(choice);
                        getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getBTable());
                        activePlayer.getPersonalSchool().getEntry().remove(choice);
                    }
                }
                /** togli da hall e metti in entry */
                for (int i = 0; i < 3; i++) {
                    Student choice = null;
                    //notify (observer)---->scelta 2 studenti
                    if (choice.getsColour().equals(SColor.GREEN)) {
                        activePlayer.getPersonalSchool().getGTable().remove(choice);
                        activePlayer.getPersonalSchool().getEntry().add(choice);
                    } else if (choice.getsColour().equals(SColor.RED)) {
                        activePlayer.getPersonalSchool().getRTable().remove(choice);
                        activePlayer.getPersonalSchool().getEntry().add(choice);
                    } else if (choice.getsColour().equals(SColor.YELLOW)) {
                        activePlayer.getPersonalSchool().getYTable().remove(choice);
                        activePlayer.getPersonalSchool().getEntry().add(choice);
                    } else if (choice.getsColour().equals(SColor.PINK)) {
                        activePlayer.getPersonalSchool().getPTable().remove(choice);
                        activePlayer.getPersonalSchool().getEntry().add(choice);
                    } else if (choice.getsColour().equals(SColor.BLUE)) {
                        activePlayer.getPersonalSchool().getBTable().remove(choice);
                        activePlayer.getPersonalSchool().getEntry().add(choice);
                    }
                }
                break;

            case COURTESAN:   /** 11 */
                Student choice = null;
                int i = 0;
                //notify (observer)---->scelgo pedina da mettere nel table
                if (choice.getsColour().equals(SColor.GREEN)) {
                    activePlayer.getPersonalSchool().getGTable().add(choice);
                    getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getGTable());
                } else if (choice.getsColour().equals(SColor.RED)) {
                    activePlayer.getPersonalSchool().getRTable().add(choice);
                    getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getRTable());
                } else if (choice.getsColour().equals(SColor.YELLOW)) {
                    activePlayer.getPersonalSchool().getYTable().add(choice);
                    getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getYTable());
                } else if (choice.getsColour().equals(SColor.PINK)) {
                    activePlayer.getPersonalSchool().getPTable().add(choice);
                    getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getPTable());
                } else if (choice.getsColour().equals(SColor.BLUE)) {
                    activePlayer.getPersonalSchool().getBTable().add(choice);
                    getCoinFromStudentMove(activePlayer, activePlayer.getPersonalSchool().getBTable());
                }
                characterCardPlayed.getStudentsOnCard().remove(choice);
                //notify (observer)---->pesco pedina da mettere sulla carta
                characterCardPlayed.getStudentsOnCard().add(getTable().getBag().get(getTable().getBag().size() - 1));
                getTable().getBag().remove(getTable().getBag().get(getTable().getBag().size() - 1));
                break;

            case JUNKDEALER:   /** 12 */
                SColor colorChoice = null;
                //notify (observer)---->scelgo un colore
                for (Player p : getListOfPlayers()) {
                    if (colorChoice.equals(SColor.GREEN)) {
                        for (int j = 0; j < 3; j++) {
                            if (activePlayer.getPersonalSchool().getGTable().size() != 0)
                                activePlayer.getPersonalSchool().getGTable().remove(activePlayer.getPersonalSchool().getGTable().size() - 1);
                        }
                    } else if (colorChoice.equals(SColor.RED)) {
                        for (int j = 0; j < 3; j++) {
                            if (activePlayer.getPersonalSchool().getRTable().size() != 0)
                                activePlayer.getPersonalSchool().getRTable().remove(activePlayer.getPersonalSchool().getRTable().size() - 1);
                        }
                    } else if (colorChoice.equals(SColor.YELLOW)) {
                        for (int j = 0; j < 3; j++) {
                            if (activePlayer.getPersonalSchool().getYTable().size() != 0)
                                activePlayer.getPersonalSchool().getYTable().remove(activePlayer.getPersonalSchool().getYTable().size() - 1);
                        }
                    } else if (colorChoice.equals(SColor.PINK)) {
                        for (int j = 0; j < 3; j++) {
                            if (activePlayer.getPersonalSchool().getPTable().size() != 0)
                                activePlayer.getPersonalSchool().getPTable().remove(activePlayer.getPersonalSchool().getPTable().size() - 1);
                        }
                    } else if (colorChoice.equals(SColor.BLUE)) {
                        for (int j = 0; j < 3; j++) {
                            if (activePlayer.getPersonalSchool().getBTable().size() != 0)
                                activePlayer.getPersonalSchool().getBTable().remove(activePlayer.getPersonalSchool().getBTable().size() - 1);
                        }
                    }
                }
                break;
        }
    }

    private void getCoinFromStudentMove(Player activePlayer, ArrayList<Student> tableColor) {
        
        if(getDifficulty().equals(Difficulty.EXPERTMODE) && (tableColor.size()==3)){
            activePlayer.setCoinScore(activePlayer.getCoinScore() + 1);
            getTable().setCoinsOnTable(getTable().getCoinsOnTable() - 1);
        }
        else if( getDifficulty().equals(Difficulty.EXPERTMODE) && (tableColor.size()==6)){
              activePlayer.setCoinScore(activePlayer.getCoinScore() + 1);
             getTable().setCoinsOnTable(getTable().getCoinsOnTable() - 1);
        }
        else if( getDifficulty().equals(Difficulty.EXPERTMODE) && (tableColor.size()==9)){
             activePlayer.setCoinScore(activePlayer.getCoinScore() + 1);
            getTable().setCoinsOnTable(getTable().getCoinsOnTable() - 1);
        }
    }
    


}
