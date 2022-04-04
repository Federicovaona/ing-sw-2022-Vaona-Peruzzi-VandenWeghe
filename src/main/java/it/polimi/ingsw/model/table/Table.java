package it.polimi.ingsw.model.table;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import it.polimi.ingsw.model.bag.Bag;
import it.polimi.ingsw.model.cloud.CloudCard;
import it.polimi.ingsw.model.character.CharacterCard;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameMode;
import it.polimi.ingsw.model.island.IslandCard;
import it.polimi.ingsw.model.island.MotherEarth;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.school.TColour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Table {

    private ArrayList<CloudCard> cloudNumber;
    private ArrayList<IslandCard> listOfIsland;
    private ArrayList<CharacterCard> characterCardsOnTable;
    private int coinsOnTable;


    public void generateCloudNumber(GameMode gm){
        int x;
        int maxNumberOfStudents;

        if(gm.equals(GameMode.TWOPLAYERS)){
            x=2;
            maxNumberOfStudents = 3;
        }else if(gm.equals(GameMode.THREEPLAYERS)){
            x=3;
            maxNumberOfStudents = 4;
        }else{
            x=4;
            maxNumberOfStudents = 3;
        }
        for(int i=0;i<x;i++){
            cloudNumber.add(new CloudCard(i, maxNumberOfStudents));
        }
    }

    public void generateIslandCards(){
        for(int s=1;s<13;s++) {
            listOfIsland.add(new IslandCard(s));
        }
    }

    public void generateMotherEarth(MotherEarth me ){
        Random rn = new Random();
        int n = rn.nextInt(12)+1;
        System.out.println(n);
        listOfIsland.get(n-1).setMotherEarthOnIsland(true);
        me.setPosition(n);
    }

    public ArrayList<CloudCard> getCloudNumber() {
        return cloudNumber;
    }

    public ArrayList<IslandCard> getListOfIsland() {
        return listOfIsland;
    }

    public int getCoinsOnTable() {
        return coinsOnTable;
    }

    public ArrayList<CharacterCard> getCharacterCardsOnTable() {
        return characterCardsOnTable;
    }

    public ArrayList<IslandCard> joinIsland(){
        //da fare

    }

    public Player playerIsWinning(Table table, ArrayList<Player> listOfPlayers){  //calcola influenza torri sul tavolo e restituisce quello con più influenza
        int countGrey = 0;
        int countWhite = 0;
        int countBlack = 0;
        Player winner = null;
        Player alsoWinner = null ;

        /** conto il numero di torri presenti sul tavolo per ogni colore */
        for (int s=1;s<13;s++){
            if((listOfIsland.get(s).getTowerOnIsland().getTColour()).equals(TColour.GREY)){
                countGrey++;
            }
            else if ((listOfIsland.get(s).getTowerOnIsland().getTColour()).equals(TColour.WHITE)){
                countWhite++;
            }
            else if ((listOfIsland.get(s).getTowerOnIsland().getTColour()).equals(TColour.BLACK)){
                countBlack++;
            }
        }

        /** confronto e cerco chi ha maggior influenza */
        if (countBlack > countGrey && countBlack > countWhite){
            for(Player player : listOfPlayers){
                if(player.getTColour().equals(TColour.BLACK)){
                    return winner = player;
                }
            }
        }

        else if(countGrey > countBlack && countGrey > countWhite){
                for(Player player : listOfPlayers) {
                    if (player.getTColour().equals(TColour.BLACK)) {
                        return winner = player;
                    }
                }
        }

        else if(countWhite > countBlack && countWhite > countGrey){
            for(Player player : listOfPlayers) {
                if (player.getTColour().equals(TColour.BLACK)) {
                    return winner = player;
                }
            }
        }

        /** in caso di parità, confronto i player e vince quello con più prof */
        else if(countBlack == countGrey && countBlack > countWhite){
            for(Player player : listOfPlayers) {
                if (player.getTColour().equals(TColour.BLACK)) {
                    winner = player;
                }
                else if(player.getTColour().equals(TColour.GREY)) {
                    alsoWinner = player;
                }
            }
            int profWinner = winner.getPersonalSchool().numberOfProf();
            int profAlsoWinner = alsoWinner.getPersonalSchool().numberOfProf();

            if(profWinner > profAlsoWinner){
                return winner;
            }
            else return winner = alsoWinner;
        }

        else if(countGrey == countWhite && countGrey > countBlack){
            for(Player player : listOfPlayers) {
                if (player.getTColour().equals(TColour.GREY)) {
                    winner = player;
                }
                else if(player.getTColour().equals(TColour.WHITE)) {
                    alsoWinner = player;
                }
            }
            int profWinner = winner.getPersonalSchool().numberOfProf();
            int profAlsoWinner = alsoWinner.getPersonalSchool().numberOfProf();

            if(profWinner > profAlsoWinner){
                return winner;
            }
            else return winner = alsoWinner;
        }

        else if(countWhite == countBlack && countWhite > countGrey){
            for(Player player : listOfPlayers) {
                if (player.getTColour().equals(TColour.WHITE)) {
                    winner = player;
                }
                else if(player.getTColour().equals(TColour.BLACK)) {
                    alsoWinner = player;
                }
            }
            int profWinner = winner.getPersonalSchool().numberOfProf();
            int profAlsoWinner = alsoWinner.getPersonalSchool().numberOfProf();

            if(profWinner > profAlsoWinner){
                return winner;
            }
            else if(profAlsoWinner > profWinner){
                winner = alsoWinner;
            }
            else{
                System.out.println(String.format("%s e %s", winner.getNickname(), alsoWinner.getNickname()) + "HANNO PAREGGIATO!");
            }
        }

        if(Game.gameIsFinished(Bag.getBag(), table))   {   //Bag come attributo di table invece di fare la classe bag

            System.out.println("HA VINTO IL GIOCATORE " + winner.getNickname());

        return winner;


}
