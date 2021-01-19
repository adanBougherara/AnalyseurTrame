package main;

import java.io.IOException;

import exception.CaractereInterditException;
import exception.LigneIncompleteException;
/**
 * Classe main utilisee pour les tests
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class TestAffichageConsoleMain {

	/**
	 * main qui execute la methode d'analyseur et qui affiche le resultat dans la console
	 * @param args le path du fichier a traiter
	 */
	public static void main(String[] args) {
		
		try {
			String affichage = outil.Analyseur.affichage("src/data/trame3.txt");
			
			System.out.println(affichage);
			

			
		} 
		catch (IOException | CaractereInterditException | LigneIncompleteException e) {
			System.out.println(e.getMessage());
		}
		
		
		

	}

}
