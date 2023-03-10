package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.assistant.AssistantCard;
import it.polimi.ingsw.model.assistant.AssistantDeckName;
import it.polimi.ingsw.model.assistant.DeckAssistant;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerNumber;
import it.polimi.ingsw.model.school.TColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    private GameController gc;

    @Test
    @BeforeEach
    public void init() {
        gc = new GameController();
    }

    @Test
    public void generateTable() {
        Game gameSession = new Game();
        gameSession.getTable().generateBagInit();
        assertNotNull(gameSession.getTable().getBag());

        gameSession.getTable().generateIslandCards();
        assertNotNull(gameSession.getTable().getListOfIsland());

        gameSession.getTable().generateMotherEarth();
        assertNotNull(gameSession.getTable().getPosMotherEarth());

        gameSession.getTable().extractStudentsInit();
        for (int i = 0; i < 12; i++) {
            assertNotNull(gameSession.getTable().getListOfIsland().get(i).getStudentOnIsland());
        }

        gameSession.getTable().addFinalStudents();
        assertNotNull(gameSession.getTable().getBag());
    }

    @Test
    public void generatePlayer() {
        Game gameSession = new Game();
        gameSession.setDifficulty(Difficulty.STANDARDMODE);
        for (int i = 1; i < 5; i++) {
            gameSession.addPlayer(new Player(null, PlayerNumber.values()[gameSession.getListOfPlayers().size()]));
            gameSession.getListOfPlayers().get(gameSession.getListOfPlayers().size() - 1).setNickname("");
            gameSession.getListOfPlayers().get(gameSession.getListOfPlayers().size() - 1).setPlayerDate(new GregorianCalendar(1999, 02, 01));

            assertEquals(Difficulty.STANDARDMODE, gameSession.getDifficulty());
            assertNotNull(gameSession.getListOfPlayers());
            assertNotNull(gameSession.getListOfPlayers().get(i - 1));
            assertNotNull(gameSession.getListOfPlayers().get(i - 1).getNickname());
            assertNotNull(gameSession.getListOfPlayers().get(i - 1).getPlayerDate());
            assertNull(gameSession.getListOfPlayers().get(i - 1).getTColor());
            assertEquals(i, gameSession.getListOfPlayers().size());
        }

        gameSession.getListOfPlayers().clear();
        gameSession.setDifficulty(Difficulty.EXPERTMODE);
        gameSession.getTable().setCoinsOnTable(20);

        for (int i = 1; i < 5; i++) {
            gameSession.addPlayer(new Player(null, PlayerNumber.values()[gameSession.getListOfPlayers().size()]));
            gameSession.getListOfPlayers().get(gameSession.getListOfPlayers().size() - 1).setNickname("");
            gameSession.getListOfPlayers().get(gameSession.getListOfPlayers().size() - 1).setPlayerDate(new GregorianCalendar(1999, 02, 01));

            if (gameSession.getDifficulty().equals(Difficulty.EXPERTMODE)) {
                gameSession.getListOfPlayers().get(gameSession.getListOfPlayers().size() - 1).setCoinScore(1);
                gameSession.getTable().decreaseCoinsOnTable(1);
            }

            assertEquals(Difficulty.EXPERTMODE, gameSession.getDifficulty());
            assertNotNull(gameSession.getListOfPlayers());
            assertNotNull(gameSession.getListOfPlayers().get(i - 1));
            assertNotNull(gameSession.getListOfPlayers().get(i - 1).getNickname());
            assertNotNull(gameSession.getListOfPlayers().get(i - 1).getPlayerDate());
            assertNull(gameSession.getListOfPlayers().get(i - 1).getTColor());
            assertEquals(i, gameSession.getListOfPlayers().size());
            assertEquals(1, gameSession.getListOfPlayers().get(i - 1).getCoinScore());
            assertEquals(20 - i, gameSession.getTable().getCoinsOnTable());
        }
    }


    @Test
    void setActionState() {
        Game gameSession = new Game();
        GameState gameState = GameState.INIT;
        gc.getGameSession();
        assertNotNull(gc.getGameSession());

        gc.setActionState(ActionState.MOTHEREARTH);
        assertNotNull(gc.getActionState());
        assertEquals(ActionState.MOTHEREARTH, gc.getActionState());

        gc.setActionState(ActionState.STUDENT);
        assertNotNull(gc.getActionState());
        assertEquals(ActionState.STUDENT, gc.getActionState());

        gc.setActionState(ActionState.CLOUDCARD);
        assertNotNull(gc.getActionState());
        assertEquals(ActionState.CLOUDCARD, gc.getActionState());
    }


    @Test
    void isGameStarted() {
        Game gameSession = new Game();

        gc.setGameState(GameState.INIT);
        assertNotNull(gc.isGameStarted());
        assertEquals(false, gc.isGameStarted());

        gc.setGameState(GameState.PLANNING);
        assertNotNull(gc.isGameStarted());
        assertEquals(true, gc.isGameStarted());
        assertNotEquals(false, gc.isGameStarted());

        gc.setGameState(GameState.ACTION);
        assertNotNull(gc.isGameStarted());
        assertEquals(true, gc.isGameStarted());
        assertNotEquals(false, gc.isGameStarted());

        gc.setGameState(GameState.END_GAME);
        assertNotNull(gc.isGameStarted());
        assertEquals(false, gc.isGameStarted());
    }


    @Test
    void planning(){
        gc.generateTable();
        gc.setGameState(GameState.INIT);

        Player player1 = new Player(TColor.WHITE, PlayerNumber.PLAYER1);
        player1.setNickname("Gino");
        gc.getGameSession().addPlayer(player1);

        Player player2 = new Player(TColor.BLACK, PlayerNumber.PLAYER2);
        player2.setNickname("Paolo");
        gc.getGameSession().addPlayer(player2);

        assertEquals(2, gc.getGameSession().getListOfPlayers().size());

        DeckAssistant deckAssistant1 = new DeckAssistant(AssistantDeckName.WIZARD1);
        ArrayList<AssistantCard> assistantCards1 = new ArrayList<>();
        DeckAssistant deckAssistant2 = new DeckAssistant(AssistantDeckName.WIZARD2);
        ArrayList<AssistantCard> assistantCards2 = new ArrayList<>();
        gc.getGameSession().getTable().generateIslandCards();

        gc.getGameSession().getPlayer("Gino").setDeckOfPlayer(deckAssistant1);
        assertNotNull(gc.getGameSession().getPlayer("Gino").getDeckOfPlayer());
        assertEquals(deckAssistant1, gc.getGameSession().getPlayer("Gino").getDeckOfPlayer());
        gc.getGameSession().getPlayer("Gino").getDeckOfPlayer().getCardsInHand().clear();
        assertEquals(0, gc.getGameSession().getPlayer("Gino").getDeckOfPlayer().getCardsInHand().size());

        gc.getGameSession().getPlayer("Paolo").setDeckOfPlayer(deckAssistant2);
        assertNotNull(gc.getGameSession().getPlayer("Paolo").getDeckOfPlayer());
        assertEquals(deckAssistant2, gc.getGameSession().getPlayer("Paolo").getDeckOfPlayer());
        gc.getGameSession().getPlayer("Paolo").getDeckOfPlayer().getCardsInHand().clear();
        assertEquals(0, gc.getGameSession().getPlayer("Paolo").getDeckOfPlayer().getCardsInHand().size());

        assertNotNull(gc.isGameStarted());
        assertEquals(false, gc.isGameStarted());

        gc.setGameState(GameState.PLANNING);
        assertNotNull(gc.isGameStarted());
        assertEquals(true, gc.isGameStarted());
    }
}
