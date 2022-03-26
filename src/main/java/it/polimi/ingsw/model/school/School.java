package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.cloud.CloudCard;
import it.polimi.ingsw.model.student.SColour;
import it.polimi.ingsw.model.student.Student;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameMode;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;

public class School {

    private ArrayList<Student> entry;
    private ArrayList<Student> GTable;
    private ArrayList<Student> RTable;
    private ArrayList<Student> YTable;
    private ArrayList<Student> PTable;
    private ArrayList<Student> BTable;
    private boolean profGInHall;
    private boolean profRInHall;
    private boolean profYInHall;
    private boolean profPInHall;
    private boolean profBInHall;
    private ArrayList<Tower> tower;                //A che serve?
    private ArrayList<Prof> profOfPlayer = null;

    public School(ArrayList<Prof> profOfPlayer) {
        Game gameMode = Game.getGameMode();
        assert gameMode != null;
        if (gameMode.equals(GameMode.THREEPLAYERS))
            entry = new ArrayList<>(9);
        else entry = new ArrayList<>(7);

        GTable = new ArrayList<>();
        RTable = new ArrayList<>();
        YTable = new ArrayList<>();
        PTable = new ArrayList<>();
        BTable = new ArrayList<>();
        tower = new ArrayList<>();
    }

    public ArrayList<Student> getEntry() {
        return this.entry;
    }
    public void addStudentInEntry(ArrayList<Student> students, GameMode gameMode,CloudCard cloudCard) {

        if (gameMode.equals(GameMode.THREEPLAYERS)) {
            for(int i=4; i>0; i--) {
                entry.add(new Student(students.get(i).getIdStudent(), students.get(i).getsColour()));
            }
            cloudCard.clearCloud();
        }
        else {
            for(int i=3; i>0; i--) {
                entry.add(new Student(students.get(i).getIdStudent(), students.get(i).getsColour()));
            }
            cloudCard.clearCloud();
        }
    }

    public ArrayList<Student> moveStudentInHall(int id, SColour sColour){    //DA RIGUARDARE!!
        if(sColour == SColour.GREEN) GTable.add(new Student(id, sColour));
        else if(sColour == SColour.RED) RTable.add(new Student(id, sColour));
        else if(sColour == SColour.YELLOW) YTable.add(new Student(id,sColour));
        else if(sColour == SColour.PINK) PTable.add(new Student(id, sColour));
        else if(sColour == SColour.BLUE) BTable.add(new Student(id, sColour));
        entry.remove(new Student(id,sColour));   // da verificare il "new"!!!!!
    }

    public ArrayList<Student> getGTable(){
        return this.GTable;
    }

    public ArrayList<Student> getRTable(){
        return this.RTable;
    }

    public ArrayList<Student> getYTable(){
        return this.YTable;
    }

    public ArrayList<Student> getPTable(){
        return this.PTable;
    }

    public ArrayList<Student> getBTable(){
        return this.BTable;
    }

    public int numberOfG(ArrayList<Student> gTable){
        return gTable.size();
    }

    public int numberOfR(ArrayList<Student> rTable){
        return rTable.size();
    }

    public int numberOfY(ArrayList<Student> yTable){
        return yTable.size();
    }

    public int numberOfP(ArrayList<Student> pTable){
        return pTable.size();
    }

    public int numberOfB(ArrayList<Student> bTable){
        return bTable.size();
    }

    public int calculateGInfluence(ArrayList<Player> players) {   //calcolo se player che sta giocando conquista il prof giallo
        // numero di studenti gialli del player "selezionato"
        int max=0;
        for (Player player : players){
            if(player.getPersonalSchool().numberOfG(player.getPersonalSchool().getGTable()) > max){
                max= player.getPersonalSchool().numberOfG(player.getPersonalSchool().getGTable());
            }
        }
        return max;
        /*Da Fare: passo alla funzione tutti i player(Arraylist<Player>) e usando metodo numberOfG tengo traccia di chi ha più gialli,
        cosa ritorna la funzione? Il numero di gialli maggiore? Il giocatore che ha più gialli? Un booleano? */
    }

    public boolean calculateRInfluence() { //Vedi sopra
        //da fare
        return true;
    }

    public boolean calculateYInfluence() {
        //da fare
        return true;
    }

    public boolean calculatePInfluence() {
        //da fare
        return true;
    }

    public boolean calculateBInfluence() {
        //da fare
        return true;
    }

    public boolean getProfGInHall(){
        return this.profGInHall;
    }

    public boolean getProfRInHall(){
        return this.profRInHall;
    }

    public boolean getProfYInHall(){
        return this.profYInHall;
    }

    public boolean getProfPInHall(){
        return this.profPInHall;
    }

    public boolean getProfBInHall(){
        return this.profBInHall;
    }

    public boolean setProfGInHall(boolean profGInHall){
        return this.profGInHall = profGInHall;
    }

    public boolean setProfRInHall(boolean profRInHall){
        return this.profRInHall = profRInHall;
    }

    public boolean setProfYInHall(boolean profYInHall){
       return this.profYInHall = profYInHall;
    }

    public boolean setProfPInHall(boolean profPInHall){
        return this.profPInHall = profPInHall;
    }

    public boolean setProfBInHall(boolean profBInHall){
        return this.profBInHall = profBInHall;
    }

    public ArrayList<Tower> getTower() {
        return this.tower;
    }

    public void addTower(int id, TColour tColour) {
            tower.add(new Tower(id,tColour));
        // ci sarà una notify observer
    }

    public void removeTower(int id, TColour tColour) {
        tower.remove(new Tower(id,tColour)); // da verificare il "new"
    }

    public boolean checkTowerIsEmpty() {     //(Ricorda: se non ci sono Tower in TowerZone finisce la partita)
        return tower.toArray().length != 0;
    }

}