package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.assistant.AssistantCard;
import it.polimi.ingsw.model.assistant.DeckAssistant;
import it.polimi.ingsw.model.game.GameMode;
import it.polimi.ingsw.model.school.*;
import it.polimi.ingsw.model.school.Prof;
import it.polimi.ingsw.model.student.SColor;
import it.polimi.ingsw.model.table.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Represents the player and contains all his information
 */
public class Player implements Serializable {
    private String nickname;
    private GregorianCalendar playerDate;
    private final PlayerNumber playerNumber;
    private School personalSchool;
    private DeckAssistant deckOfPlayer;
    private AssistantCard trash;
    private int coinScore;
    private TColor tColor;
    private int influenceOnIsland;
    private String teamMate;
    private boolean teamLeader;

    /**
     * Default constructor
     * @param tColor tower's color from enumeration
     * @param playerNumber player's number from enumeration
     */
    public Player(TColor tColor, PlayerNumber playerNumber) {
        nickname = "";
        playerDate = null;
        this.playerNumber = playerNumber;
        influenceOnIsland = 0;
        personalSchool = new School();
        this.tColor = tColor;
        trash = null;
        coinScore = 0;
        teamMate = null;
        teamLeader = false;
    }

    /**
     * Creates the player's school
     * @param table of the match
     * @param gameMode of the match
     */
    public void generateSchool(Table table, GameMode gameMode) {
        int i = 0, t = 0;
        switch (gameMode) {
            case TWOPLAYERS:
            case COOP:
                i = 7;
                t = 8;
                break;
            case THREEPLAYERS:
                i = 9;
                t = 6;
                break;
        }
        for (int s = 0; s < i; s++) {
            personalSchool.getEntry().add(table.getBag().get(s));
            table.getBag().remove(table.getBag().get(s));
        }
        for (int f = 1; f < t+1; f++) {
            personalSchool.addTower(tColor);
        }

        personalSchool.getProfOfPlayer().add(new Prof(SColor.GREEN));
        personalSchool.getProfOfPlayer().add(new Prof(SColor.RED));
        personalSchool.getProfOfPlayer().add(new Prof(SColor.YELLOW));
        personalSchool.getProfOfPlayer().add(new Prof(SColor.PINK));
        personalSchool.getProfOfPlayer().add(new Prof(SColor.BLUE));

    }

    /**
     * Returns the Assistant Card from the list of assistant card with the name selected
     * @param assistantCard name of the assistant card
     * @return Assistant Card selected
     */
    public AssistantCard getAssistantCard(String assistantCard){
        int indexPlayer = getAssistantCardByNickname().indexOf(assistantCard);
        return deckOfPlayer.getCardsInHand().get(indexPlayer);
    }

    /**
     * Returns an array with the names of the assistant cards by the nickname associated to each card
     * @return an array with the names of the assistant cards in hand
     */
    public ArrayList<String> getAssistantCardByNickname() {
        ArrayList<String> assistantCardList = new ArrayList<>();
        for (int i = 0; i < deckOfPlayer.getCardsInHand().size(); i++) {
            assistantCardList.add(deckOfPlayer.getCardsInHand().get(i).getAssistantName());
        }
        return assistantCardList;
    }

    public School getPersonalSchool() {
        return personalSchool;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public GregorianCalendar getPlayerDate() {
        return playerDate;
    }

    public void setPlayerDate(GregorianCalendar playerDate) {
        this.playerDate = playerDate;
    }

    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

    public AssistantCard getTrash() {
        return trash;
    }

    public void setTrash(AssistantCard trash) {
        this.trash = trash;
    }

    public TColor getTColor() {
        return tColor;
    }

    public void setTColor(TColor tColor) {
        this.tColor = tColor;
    }

    public int getCoinScore() {
        return coinScore;
    }

    public void setCoinScore(int coinScore) {
        this.coinScore = coinScore;
    }

    public DeckAssistant getDeckOfPlayer(){
        return deckOfPlayer;
    }

    public void setDeckOfPlayer(DeckAssistant deckOfPlayer) {
        deckOfPlayer.generateAssistantDeck();
        this.deckOfPlayer = deckOfPlayer;
    }

    public int getInfluenceOnIsland() {
        return influenceOnIsland;
    }

    public void setInfluenceOnIsland(int influenceOnIsland) {
        this.influenceOnIsland = influenceOnIsland;
    }

    public String getTeamMate() {
        return teamMate;
    }

    public void setTeamMate(String teamMate) {
        this.teamMate = teamMate;
    }

    public boolean isTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(boolean teamLeader) {
        this.teamLeader = teamLeader;
    }
}
