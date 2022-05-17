package it.polimi.ingsw.message;

public enum MessageType {
    //messaggi del gioco
    LOGIN,
    LOGIN_RESULT,
    PLAYERS_NUMBER_AND_DIFFICULTY_CHOSEN,
    ASSISTANTCARD_PLAYED,
    CHARACTER_CARD_PLAYED,
    CLOUD_CHOSEN,
    PLACE_AND_STUDENT_FOR_MOVE_CHOSEN,
    STEP_MOTHER_EARTH_CHOSEN,
    ID_ISLAND_CHOSEN,

    //messaggi di connessione al server
    SUCCESSFUL_HOST,
    STRING_MESSAGE,

    //messaggi del turno
    END,

    //setup

    //richiesta di azioni es. tutte le show
    ASK_PLAYER,
    SHOW_PLAYER,
    SHOW_PLAYER_INFLUENCE,
    SHOW_TABLE,
    SHOW_PERSONAL_SCHOOL,
    SHOW_ASSISTANT_DECK,

    //azioni
    PLAY_CHARACTER_CARD,
    PLAY_ASSISTANT_CARD,
    CHOOSE_CLOUD_CARD,
    CHOOSE_PLACE_AND_STUDENT_FOR_MOVE,
    CHOOSE_MOTHER_EARTH_STEPS,
    CHOOSE_ID_ISLAND,


    //messaggi intermediari
    WIN_PROF,
    WIN_TOWER,

    //altri
    OTHER_PLAYER_TURN,
    ERROR,
    WIN,
    LOSE;

}
