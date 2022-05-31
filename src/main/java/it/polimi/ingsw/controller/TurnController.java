package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TurnController {
    private HashMap<String, Boolean> activePlayer;
    private String playingPlayer;

    private ArrayList<String> playerOrderByName;
    private ArrayList<Player> playerOrder;
    private ArrayList<String> newPlayerOrderByName;
    private ArrayList<Player> newPlayerOrder;

    /** costruttore */
    public TurnController(GameController gameController){
        playingPlayer = YoungestPlayer(gameController).getNickname();
        playerOrder = new ArrayList<>();
        playerOrderByName = new ArrayList<>();
        newPlayerOrderByName = new ArrayList<>();
        newPlayerOrder = new ArrayList<>();
        activePlayer= new HashMap<>();
        for(int i = 0; i < gameController.getAllVirtualView().size(); i++){
            String nickname = gameController.getGameSession().getListOfPlayers().get(i).getNickname();
            activePlayer.put(nickname, true);
            playerOrderByName.add(nickname);
            playerOrder.add(gameController.getGameSession().getListOfPlayers().get(i));
        }
    }

    private Player YoungestPlayer(GameController gameController) {

        Player youngestPlayer = gameController.getGameSession().getListOfPlayers().get(0);

        for (Player p : gameController.getGameSession().getListOfPlayers()){
            if (youngestPlayer.getPlayerDate().compareTo(p.getPlayerDate()) == -1 || youngestPlayer.getPlayerDate().compareTo(p.getPlayerDate()) == 0)
                youngestPlayer = p;
        }
        return youngestPlayer;
    }


    /** cambia l'active player */
    public void nextPlayer(ArrayList<String> playerOrderByName){
        int player;
        if((playerOrderByName.indexOf(playingPlayer)+1)>(playerOrderByName.size()-1))
            player = 0;
        else
            player = playerOrderByName.indexOf(playingPlayer)+1;
        playingPlayer = playerOrderByName.get(player);
    }

    public void changeOrder(){
        ArrayList<Player>  NewPlayerOrder = (ArrayList<Player>) playerOrder.clone();
        newPlayerOrderByName.clear();
        Collections.sort(NewPlayerOrder, (o1, o2) -> Integer.valueOf(o1.getTrash().getTurnValue()).compareTo(o2.getTrash().getTurnValue()));
        for(Player player : NewPlayerOrder)
            newPlayerOrderByName.add(player.getNickname());
        newPlayerOrder = NewPlayerOrder;
        for(Player player : newPlayerOrder)
            System.out.println(player.getNickname());
        for(String nickname : newPlayerOrderByName)
            System.out.println(nickname);
    }
    public String getActivePlayer() {
        return playingPlayer;
    }
    public ArrayList<String> getPlayerOrderByName() {return playerOrderByName;}
    public ArrayList<String> getNewPlayerOrderByName() {return newPlayerOrderByName;}
    public void setPlayingPlayer(String nickname){ this.playingPlayer=nickname; }
}
