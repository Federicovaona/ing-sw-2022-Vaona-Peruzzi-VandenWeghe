package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.character.CardEffect;
import it.polimi.ingsw.model.character.CharacterCard;
import it.polimi.ingsw.model.island.IslandCard;
import it.polimi.ingsw.model.student.SColor;
import it.polimi.ingsw.model.student.Student;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class School {

    private ArrayList<Student> entry = new ArrayList<>();
    private ArrayList<Student> GTable = new ArrayList<>();
    private ArrayList<Student> RTable = new ArrayList<>();
    private ArrayList<Student> YTable = new ArrayList<>();
    private ArrayList<Student> PTable = new ArrayList<>();
    private ArrayList<Student> BTable = new ArrayList<>();
    private boolean profGInHall;
    private boolean profRInHall;
    private boolean profYInHall;
    private boolean profPInHall;
    private boolean profBInHall;
    private ArrayList<Tower> towerZone = new ArrayList<>();
    private ArrayList<Prof> profOfPlayer = new ArrayList<>();

    public School() {

        GTable = new ArrayList<>();
        RTable = new ArrayList<>();
        YTable = new ArrayList<>();
        PTable = new ArrayList<>();
        BTable = new ArrayList<>();
        towerZone = new ArrayList<>();
        profOfPlayer = new ArrayList<>();
    }

    public ArrayList<Student> getEntry() {
        return (ArrayList<Student>) entry;
    }

    public void moveStudentToIsland(IslandCard islandCard, int id){ //Specifico Studente va spostato (sceglie player)
        Student student = new Student(131,null);
        for(int i = 0; i < entry.size(); i++) {
            if(id==entry.get(i).getIdStudent())
                student = entry.get(i);
        }
        islandCard.getStudentOnIsland().add(entry.get(entry.indexOf(student)));
        entry.remove(entry.get(entry.indexOf(student)));
    }

    public void moveStudentInHall(int id) {
        Student student = new Student(131, null);
        for (int i = 0; i < entry.size(); i++) {
            if (id == entry.get(i).getIdStudent())
                student=entry.get(i);
        }
        switch(student.getsColour()) {
            case GREEN:
                GTable.add(student);
                /** if(GameMode.equals(EXPERTMODE) && (GTable.size()==3 || GTable.size()==6 || GTable.size()=9)){
                    player.setCoinScore(player.getCoinScore() + 1);
                    table.decreaseCoinScore(); }  */
                entry.remove(entry.get(entry.indexOf(student)));
                break;
            case RED:
                RTable.add(student);
                entry.remove(entry.get(entry.indexOf(student)));
                break;
            case YELLOW:
                YTable.add(student);
                entry.remove(entry.get(entry.indexOf(student)));
                break;
            case PINK:
                PTable.add(student);
                entry.remove(entry.get(entry.indexOf(student)));
                break;
            case BLUE:
                BTable.add(student);
                entry.remove(entry.get(entry.indexOf(student)));
                break;
        }
    }

    public ArrayList<Prof> getProfOfPlayer(){
        return (ArrayList<Prof>) profOfPlayer;
    }

    public ArrayList<Student> getGTable(){
        return (ArrayList<Student>) GTable.clone();
    }

    public ArrayList<Student> getRTable(){
        return (ArrayList<Student>) RTable.clone();
    }

    public ArrayList<Student> getYTable(){
        return  (ArrayList<Student>) YTable.clone();
    }

    public ArrayList<Student> getPTable(){
        return  (ArrayList<Student>) PTable.clone();
    }

    public ArrayList<Student> getBTable(){
        return  (ArrayList<Student>) BTable.clone();
    }

    /** da generalizzare tutto con questo */
    public int numberOfStudents(Player player, SColor color){
        switch(color){
            case GREEN:
                return player.getPersonalSchool().getGTable().size();
            case RED:
                return player.getPersonalSchool().getRTable().size();
            case YELLOW:
                return player.getPersonalSchool().getYTable().size();
            case PINK:
                return player.getPersonalSchool().getPTable().size();
            case BLUE:
                return player.getPersonalSchool().getBTable().size();
        }
        return 0;
    }

    void winProf(ArrayList<Player> players, Player playerTurn, CardEffect cardEffectPlayed, SColor color) {
        int max = 0;
        int playerWithMax = 0;
        Player maxPlayer = null;

        switch (color){
            case GREEN:
                for (Player p : players) {                         /** In questo for trovo max numero di verdi generale*/
                    if (numberOfStudents(p, SColor.GREEN) > max) {
                        max = numberOfStudents(p, SColor.GREEN);
                    } else p.getPersonalSchool().getProfOfPlayer().get(0).setInHall(false); //Perchè in else?
                }
                for (Player p : players) {                          /**In questo for conto i giocatori che hanno il max numero di verdi*/
                    if (numberOfStudents(p, SColor.GREEN) == max) {
                        playerWithMax++;
                        maxPlayer = p;      /**Se è solo uno lo salvo in maxPlayer*/
                    }
                }
                if (playerWithMax == 1) {   /**Setto a true il prof verde del maxplayer */
                    maxPlayer.getPersonalSchool().getProfOfPlayer().get(0).setInHall(true);
                }
                else if(playerWithMax > 1 && cardEffectPlayed.isCiccioPanzaPlayed()){ /**Se ho più giocatori in pareggio, setto a true solo il prof di chi ha l'effetto di CiccioPanza*/
                        playerTurn.getPersonalSchool().getProfOfPlayer().get(0).setInHall(true);

                }

            case RED:
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.RED) > max) {
                        max = numberOfStudents(p, SColor.RED);
                    } else p.getPersonalSchool().getProfOfPlayer().get(1).setInHall(false);
                }
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.RED) == max) {
                        playerWithMax++;
                        maxPlayer = p;
                    }
                }
                if (playerWithMax == 1) {
                    maxPlayer.getPersonalSchool().getProfOfPlayer().get(1).setInHall(true);
                }
                else if(playerWithMax > 1 && cardEffectPlayed.isCiccioPanzaPlayed()){ /**Se ho più giocatori in pareggio, setto a true solo il prof di chi ha l'effetto di CiccioPanza*/
                    playerTurn.getPersonalSchool().getProfOfPlayer().get(1).setInHall(true);

                }
            case YELLOW:
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.YELLOW) > max) {
                        max = numberOfStudents(p, SColor.YELLOW);
                    } else p.getPersonalSchool().getProfOfPlayer().get(2).setInHall(false);
                }
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.YELLOW) == max) {
                        playerWithMax++;
                        maxPlayer = p;
                    }
                }
                if (playerWithMax == 1) {
                    maxPlayer.getPersonalSchool().getProfOfPlayer().get(2).setInHall(true);
                }
                else if(playerWithMax > 1 && cardEffectPlayed.isCiccioPanzaPlayed()){ /**Se ho più giocatori in pareggio, setto a true solo il prof di chi ha l'effetto di CiccioPanza*/
                    playerTurn.getPersonalSchool().getProfOfPlayer().get(2).setInHall(true);

                }
            case PINK:
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.PINK) > max) {
                        max = numberOfStudents(p, SColor.PINK);
                    } else p.getPersonalSchool().getProfOfPlayer().get(3).setInHall(false);
                }
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.PINK) == max) {
                        playerWithMax++;
                        maxPlayer = p;
                    }
                }
                if (playerWithMax == 1) {
                    maxPlayer.getPersonalSchool().getProfOfPlayer().get(3).setInHall(true);
                }
                else if(playerWithMax > 1 && cardEffectPlayed.isCiccioPanzaPlayed()){ /**Se ho più giocatori in pareggio, setto a true solo il prof di chi ha l'effetto di CiccioPanza*/
                    playerTurn.getPersonalSchool().getProfOfPlayer().get(3).setInHall(true);
                }
            case BLUE:
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.BLUE) > max) {
                        max = numberOfStudents(p, SColor.BLUE);
                    } else p.getPersonalSchool().getProfOfPlayer().get(4).setInHall(false);
                }
                for (Player p : players) {
                    if (numberOfStudents(p, SColor.BLUE) == max) {
                        playerWithMax++;
                        maxPlayer = p;
                    }
                }
                if (playerWithMax == 1) {
                    maxPlayer.getPersonalSchool().getProfOfPlayer().get(4).setInHall(true);
                }
                else if(playerWithMax > 1 && cardEffectPlayed.isCiccioPanzaPlayed()){ /**Se ho più giocatori in pareggio, setto a true solo il prof di chi ha l'effetto di CiccioPanza*/
                    playerTurn.getPersonalSchool().getProfOfPlayer().get(4).setInHall(true);
                }
        }
        cardEffectPlayed.setCiccioPanzaPlayed(false); /** Va Bene??? */
    }

    public boolean getProfInHall(SColor color){
        switch(color){
            case GREEN:
                return profGInHall;
            case RED:
                return profRInHall;
            case YELLOW:
                return profYInHall;
            case PINK:
                return profPInHall;
            case BLUE:
                return profBInHall;
        }
        return false;        //da modificare con un return valido
    }

    public ArrayList<Tower> getTower() {
        return (ArrayList<Tower>) towerZone.clone();
    }

    public void addTower(int id, TColor tColor) {
        towerZone.add(new Tower(id, tColor));
        // ci sarà una notify observer
    }

    public void removeTower() {
        towerZone.remove(towerZone.size() - 1);
    }

    public int numberOfProf(){
        int countProf = 0;

        for (Prof prof : profOfPlayer){
            if(prof.getInHall())
                countProf++;
        }

        return countProf;
    }

}

