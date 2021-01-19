package main;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 * Classe main qui lance l'interface graphique du programme
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class EditeurMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new EditeurFenetre(stage);
       	
    }
	
    public static void main(String[] args) { 
    	launch(args); 
    } 
}