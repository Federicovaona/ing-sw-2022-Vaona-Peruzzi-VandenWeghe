package it.polimi.ingsw.view.GUI.scene;

import it.polimi.ingsw.model.character.CharacterCard;
import it.polimi.ingsw.model.game.Difficulty;
import it.polimi.ingsw.model.game.GameMode;
import it.polimi.ingsw.observer.ObservableView;
import it.polimi.ingsw.view.GUI.GuiManager;
import it.polimi.ingsw.view.GUI.StartGUI;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardScene extends ObservableView implements GenericScene {

    private Map<String,SchoolController> schoolControllers = new HashMap<>();

    private static Map<String, String> assistantCardMap;
    static {
        assistantCardMap = new HashMap<>();
        assistantCardMap.put("lion", "/images/assistantCards/assistant1.png");
        assistantCardMap.put("ostrich", "/images/assistantCards/assistant2.png");
        assistantCardMap.put("cat", "/images/assistantCards/assistant3.png");
        assistantCardMap.put("falcon", "/images/assistantCards/assistant4.png");
        assistantCardMap.put("fox", "/images/assistantCards/assistant5.png");
        assistantCardMap.put("lizard", "/images/assistantCards/assistant6.png");
        assistantCardMap.put("octopus", "/images/assistantCards/assistant7.png");
        assistantCardMap.put("dog", "/images/assistantCards/assistant8.png");
        assistantCardMap.put("elephant", "/images/assistantCards/assistant9.png");
        assistantCardMap.put("turtle", "/images/assistantCards/assistant10.png");
    }

    private ViewDeckScene assistantDeck;

    private GameMode gameMode;
    private Difficulty difficulty;

    private boolean planning = false;



    @FXML
    private ImageView ImageTrashPersonal;

    @FXML
    private Pane PaneCoinScore;

    @FXML
    private Pane PaneTeamMate;

    @FXML
    private Text TextCoinscore;

    @FXML
    private Text TextTeamMate;

    @FXML
    private VBox characterCardLayout1;

    @FXML
    private ImageView cloud1;

    @FXML
    private ImageView cloud2;

    @FXML
    private ImageView cloud3;

    @FXML
    private ImageView cloud4;

    @FXML
    private Pane cloudPane;

    @FXML
    private ImageView coinImagePersonal;

    @FXML
    private ImageView coinImageTable;

    @FXML
    private Pane coinPanePersonal;

    @FXML
    private Pane coinPaneTable;

    @FXML
    private Text coinTextPersonal;

    @FXML
    private Text coinTextTable;

    @FXML
    private Button deckButton;

    @FXML
    private ImageView deckLogo;

    @FXML
    private ImageView island0;

    @FXML
    private ImageView island1;

    @FXML
    private ImageView island10;

    @FXML
    private ImageView island11;

    @FXML
    private ImageView island2;

    @FXML
    private ImageView island3;

    @FXML
    private ImageView island4;

    @FXML
    private ImageView island5;

    @FXML
    private ImageView island6;

    @FXML
    private ImageView island7;

    @FXML
    private ImageView island8;

    @FXML
    private ImageView island9;

    @FXML
    private Pane islandPane;

    @FXML
    private ImageView motherEarth;

    @FXML
    private Button otherSchoolButton;

    @FXML
    private Pane schoolPane;

    @FXML
    private Pane schoolPane1;

    @FXML
    private Pane schoolPane11;

    @FXML
    private Pane sxPane;

    @FXML
    private Text textCoinsOnTable;

    @FXML
    private Pane trashPanePersonalSchool;


    public void updatePersonalSchool(SchoolController controller, GameMode gameMode, String teamMate) throws IOException {
        FXMLLoader loader = new FXMLLoader(StartGUI.class.getResource("/fxml/school.fxml"));
        loader.setController(controller);
        AnchorPane personalSchool = loader.load();
        schoolPane.getChildren().setAll(personalSchool);
        if(controller.getTrash()!=null)
            ImageTrashPersonal.setImage(new Image(assistantCardMap.get(controller.getTrash().getAssistantName())));
        if(difficulty.equals(Difficulty.EXPERTMODE))
            coinTextPersonal.setText(String.valueOf(controller.getCoins()));
        if(!gameMode.equals(GameMode.COOP)){
            PaneTeamMate.setVisible(false);
            PaneTeamMate.setDisable(true);
            TextTeamMate.setDisable(true);
            TextTeamMate.setVisible(false);
        }
        else
            TextTeamMate.setText("TeamMate: " + teamMate);

    }

    public void initializeDifficulty(Difficulty difficulty, int coinsOnTable){
        this.difficulty = difficulty;
        if(difficulty.equals(Difficulty.STANDARDMODE)){
            coinPaneTable.setVisible(false);
            coinPaneTable.setDisable(true);
            coinImageTable.setVisible(false);
            coinImageTable.setDisable(true);
            coinPanePersonal.setVisible(false);
            coinPanePersonal.setDisable(true);
            coinImagePersonal.setVisible(false);
            coinImagePersonal.setDisable(true);
            textCoinsOnTable.setDisable(true);
            textCoinsOnTable.setVisible(false);
            TextCoinscore.setVisible(false);
            TextCoinscore.setDisable(true);
            PaneCoinScore.setDisable(true);
            PaneCoinScore.setVisible(false);
            coinTextPersonal.setVisible(false);
            coinTextPersonal.setDisable(true);
            ImageTrashPersonal.setVisible(false);
        }else{
            coinTextTable.setText(String.valueOf(coinsOnTable));
        }
    }

    @FXML
    public void initialize(){
        otherSchoolButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            try {
                otherSchoolClicked(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        deckButton.addEventHandler(MouseEvent.MOUSE_CLICKED,this::deckButtonClicked);

        /**
        ArrayList<CharacterCard> characterCardsPlaying = new ArrayList<>((characterCardsPlaying()));
        try{
            for(int i = 0; i < characterCardsPlaying().size(); i++) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/characterCard.fxml"));
                ImageView characterCardImage = loader.load();

                CharacterCardController characterCardController = loader.getController();
                characterCardController.setData(characterCardsPlaying.get(i));
                characterCardLayout.getChildren().add(characterCardImage);
                }
        } catch (IOException e) {
                e.printStackTrace();

        }*/
    }

    private void otherSchoolClicked(Event event) throws IOException {
        switch(gameMode){
            case TWOPLAYERS:
                ViewOtherSchoolScene1 scene1 = new ViewOtherSchoolScene1();
                GuiManager.newStagePane(scene1,"/fxml/view_other_school_1_scene");
                scene1.updatePersonalSchool(schoolControllers);
                break;
            case THREEPLAYERS:
                ViewOtherSchoolScene2 scene2 = new ViewOtherSchoolScene2();
                GuiManager.newStagePane(scene2,"/fxml/view_other_school_2_scene");
                scene2.updatePersonalSchool(schoolControllers);
                break;
            case COOP:
                ViewOtherSchoolScene3 scene3 = new ViewOtherSchoolScene3();
                GuiManager.newStagePane(scene3, "/fxml/view_other_school_3_scene");
                scene3.updatePersonalSchool(schoolControllers);
                break;
        }
    }

    public void updateOtherSchool(SchoolController controller, GameMode gameMode, String nickname){
        this.gameMode = gameMode;
        if(schoolControllers.containsKey(nickname)){
            schoolControllers.remove(nickname);
            schoolControllers.put(nickname,controller);
        }else{
            schoolControllers.put(nickname,controller);
        }

    }

    private void deckButtonClicked(Event event){
        GuiManager.newStagePane(assistantDeck, "/fxml/view_deck_scene");
        if(planning) {
            assistantDeck.activatePlayButton();
        }
    }

    public void updateAssistantCardDeck(ViewDeckScene viewDeckScene){
        this.assistantDeck = viewDeckScene;
        switch(viewDeckScene.getDeckAssistant().getDeckName()){
            case WIZARD1:
                deckLogo.setImage((new Image("/images/assistantDeck/assistantCard_back_1.png")));
                break;
            case WIZARD2:
                deckLogo.setImage((new Image("/images/assistantDeck/assistantCard_back_2.png")));
                break;
            case WIZARD3:
                deckLogo.setImage((new Image("/images/assistantDeck/assistantCard_back_3.png")));
                break;
            case WIZARD4:
                deckLogo.setImage((new Image("/images/assistantDeck/assistantCard_back_4.png")));
                break;
        }
    }


    /** esempio inizializzazione carta personaggio -> da sistemare con */
    private List<CharacterCard> characterCardsPlaying(){
        List<CharacterCard> cards = new ArrayList<>();
        CharacterCard characterCard = null /**= new CharacterCard() */ ;
        characterCard.setImageSrc("/images/characterCards/characterCard_front.jpg");

        /** per le tre carte */
        return cards;
    }

    public ViewDeckScene getAssistantDeck(){
        return assistantDeck;
    }

    public void setPlanning(boolean planning){
        this.planning = planning;
    }
}
