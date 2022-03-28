package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.ModelView;
import it.polimi.ingsw.view.View;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Model model = new Model();
        ModelView modelView = new ModelView();
        View view = new View();
        Controller controller = new Controller(model, view);

        view.addObserver(controller);  //(= controller osserva view)
        model.addObserver(modelView);
        modelView.addObserver(view);

        view.run();

    }
}
