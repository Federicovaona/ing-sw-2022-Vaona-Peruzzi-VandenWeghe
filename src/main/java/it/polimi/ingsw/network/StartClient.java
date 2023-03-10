package it.polimi.ingsw.network;

import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.GUI.StartGUI;
import javafx.application.Application;

import java.util.Locale;
import java.util.Scanner;

/**
 * Starts the client
 */
public class StartClient {

    /**
     * Starts the client and asks the player if he wants CLI or GUI mode
     */
    public static void main(String[] args){
        System.out.println("Hi! Digit \"CLI\" to play from the command line interface, or \"GUI\" to play " +
                "with the graphic interface mode.");
        Scanner input = new Scanner(System.in);
        String mode;
        do{
            mode = input.nextLine().toUpperCase(Locale.ROOT);
            if(!(mode.equals("CLI"))&&!(mode.equals("GUI"))) System.out.println("Mode not recognised. Please insert \"CLI\" o \"GUI\".");
            else{
                if(mode.equals("CLI")){
                    CLI cli = new CLI();
                    ClientMessanger clientMessenger = new ClientMessanger(cli);
                    cli.addObserver(clientMessenger);
                    cli.start();
                }
                else Application.launch(StartGUI.class);
            }
        }
        while(!(mode.equals("CLI")) && !(mode.equals("GUI")));
    }
}
