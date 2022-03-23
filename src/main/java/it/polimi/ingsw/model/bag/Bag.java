package it.polimi.ingsw.model.bag;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameMode;
import it.polimi.ingsw.model.school.School;
import it.polimi.ingsw.model.student.Student;

import java.util.ArrayList;

public class Bag {

    private ArrayList<Student> bag = new ArrayList<>(130);

    public ArrayList<Student> shuffleBag(){
        //da fare
        return this.bag;
    }

    public ArrayList<Student> extractStudent(ArrayList<Student> stud, School school, Game gameMode){   //estrae dal sacchetto 3/4 studenti

        //GameMode gameMode = Game.getGameMode();                          // DA RIVEDERE
        //School entry = School.getEntry();    (FEDERICO: L'ho tolto e ho inserito school come parametro della funzione)

        if(gameMode.equals(GameMode.THREEPLAYERS)){
            for (int i = bag.size(); i <= bag.size()-4; i--) {
                school.addStudent(stud.get(i).getIdStudent(), stud.get(i).getsColour());
                bag.remove(stud);
            }
        }
        else {
            for (int i = bag.size(); i <= bag.size()-3; i--) {
                school.addStudent(stud.get(i).getIdStudent(), stud.get(i).getsColour());
                bag.remove(stud);
        }
        }
        return bag;
    }

    public boolean checkIsEmpty(){
        return bag.toArray().length != 0;
    }

}
