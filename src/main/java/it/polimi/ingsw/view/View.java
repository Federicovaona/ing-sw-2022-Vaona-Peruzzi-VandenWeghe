package it.polimi.ingsw.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/** la classe rappresenta la View generica che poi sarà implementata da CLI e GUI*/
public interface View {

    void showLogin(String username, String gameId, boolean wasJoined);

    /** mostra un messaggio generico */
    void showMessage(String message);

    void showPlayerInfluence(int influence);

    void showPlayerTurn(String activePlayer);
    
    void showPlayerList(ArrayList<String> playerOrder);


}
