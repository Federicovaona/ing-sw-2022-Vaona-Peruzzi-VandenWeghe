package it.polimi.ingsw.view.GUI.scene;

import it.polimi.ingsw.model.game.Difficulty;
import it.polimi.ingsw.observer.ObservableView;
import it.polimi.ingsw.observer.ObserverView;
import it.polimi.ingsw.view.GUI.GuiManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;


import java.util.Locale;

public class NewGameScene extends ObservableView implements GenericScene {
    @FXML
    public ComboBox difficulty;
    @FXML
    public ComboBox numberOfPlayers;
    @FXML
    private Button buttonNext;
    @FXML
    private Button exitButton;

    @FXML
    public void initialize(){
        buttonNext.addEventHandler(MouseEvent.MOUSE_CLICKED, this::clickButton);
        //exit.addEventHandler(MouseEvent.MOUSE_CLICKED, this::closeGui);

        difficulty.getItems().add("standard");
        difficulty.getItems().add("expert");

        difficulty.setValue("standard");

        numberOfPlayers.getItems().add("2");
        numberOfPlayers.getItems().add("3");
        numberOfPlayers.getItems().add("4");

        numberOfPlayers.setValue("2");
    }

    /** gestisce il click sul pulsante */
    @FXML
    private void clickButton(Event event){
        buttonNext.setDisable(true);
        String g = (String) difficulty.getValue();
        Difficulty difficulty1 = Difficulty.valueOf(g.toUpperCase(Locale.ROOT)+"MODE");
        notifyObserver(obs -> obs.choosePlayersNumberAndDifficulty((Integer) numberOfPlayers.getValue(),difficulty1) );
    }

    @FXML
    private void closeGui(){
        notifyObserver(ObserverView:: updateDisconnect);
        System.exit(0);
    }


}
