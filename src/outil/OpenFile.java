package outil;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import exception.*;
/**
 * Classe permettant d'ouvrir un fichier trame et de recuperer le contenu
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class OpenFile {
	
	/**
	 * Classe static qui recupere dans une liste le contenu d'un fichier trame
	 * @param path le path du fichier a traiter
	 * @return une liste avec les trames (au format String sans espace) contenues dans le fichier
	 * @throws IOException en cas de probleme d'ouverture du file
	 * @throws CaractereInterditException en cas de detection d'un caractere interdit dans la trame 
	 * @throws LigneIncompleteException en cas de detection d'une ligne avec au moins un chiffre hexadecimal manquant
	 */
	public static List<String> recupererContenu (String path) throws IOException, CaractereInterditException, LigneIncompleteException{
		List<String> trames = new ArrayList<>();
		StringBuilder contenu = new StringBuilder("");
		int nbLigne = 0, offset_expected = 0, offset_courant = -1, ecart = 0;
		String line = "", nextLine = "";
		boolean isNext, out = false;
		
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {

			try (BufferedReader br2 = new BufferedReader(new FileReader(new File(path)))) {
				// On parcourt le même fichier avec une ligne d'avance pour récupérer l'offset
				nextLine = br2.readLine();
				isNext = nextLine != null;
				
				// Lecture ligne par ligne
				while ((line = br.readLine()) != null) {
					nbLigne++;
					
					nextLine = br2.readLine();
					isNext = nextLine != null;
					
					// Séparation avec le caractere espace
					String[] split = line.split(" ");
					
					
					//Si l'offset_courant n'est pas valide, on ignore la ligne
					if (!Validite.offsetValide(split[0])) {
						continue;	
					}		
					
					//Si la prochaine ligne est null, on est sur la dernière ligne
					if (!isNext) {
						out = true;
					}
					
					// Si la ligne suivante existe
					else {
						
						String[] splitNextLine = nextLine.split(" ", 2);
						
						//On cherche la prochaine ligne avec un offset valide, on ignore les lignes non valides
						while ( !Validite.offsetValide(splitNextLine[0]) ) {
							nextLine = br2.readLine();
							isNext = nextLine != null;
							if (!isNext) {
								out = true;
								break;
							}
							splitNextLine = nextLine.split(" ", 2);
							//On garde en mémoire l'écart avec la ligne courante du premier buffer
							ecart++;
							
						}
							
						if (isNext) {
							
							// On met a jour offset_expected pour déterminer la longueur de la ligne courante
							offset_expected = Conversion.HexaToDec(splitNextLine[0]);
							
							OpenFile.ajouteLigne(contenu, split, offset_expected  - offset_courant, nbLigne);
							
							//Une fois la ligne ajoutée, on met a jour offset_courant
							offset_courant += offset_expected - 1 - offset_courant;
							
							//On ramene la ligne courante a une ligne d'ecart de la ligne suivante
							while (ecart > 0) {
								line = br.readLine();
								nbLigne++;
								ecart --;
							}
						}						
					}
					// TEST FIN DE TRAME : On passe à la trame suivante si offset_expected a 0
					if (offset_expected == 0 || out) {
						OpenFile.ajouteTrame(contenu, split, trames, nbLigne);
						contenu = new StringBuilder("");
						offset_courant = -1;
					}
								
				}
			}
					
		}
		
		return trames;
	}
	
	/**
	 * Methode privee qui ajoute une ligne d'octets dans une trame
	 * @param sb la trame en cours de creation
	 * @param tab la ligne contenant les octets a ajouter a la trame
	 * @param limite indiquant le nombre d'octets sur la ligne qu'il faut ajouter a la trame
	 * @param nbLigne le numero de la ligne courante
	 * @throws LigneIncompleteException en cas de detection de ligne incomplete
	 * @throws CaractereInterditException en cas de detection de caractere interdit dans la trame
	 */
	private static void ajouteLigne (StringBuilder sb, String[] tab, int limite, int nbLigne) throws LigneIncompleteException, CaractereInterditException {
		
		for (int i = 1; i < limite; i++) {
			if (i >= tab.length) throw new LigneIncompleteException("Ligne " + nbLigne + " incomplète");
			
			if (tab[i].equals("")) {
				limite++;
				continue;
			}
			if (tab[i].equals("\n")) throw new LigneIncompleteException("Ligne " + nbLigne + " incomplète");
			
			if (!Validite.codeValide(tab[i])) throw new CaractereInterditException("Caractere(s) interdit(s) avec l'octet " + tab[i] + " ligne " + nbLigne);
			
			
			sb.append(tab[i]);
		}
		
	}
	/**
	 * Methode privee qui remplit la derniere ligne d'une trame et qui l'ajoute a la liste des trames du fichier
	 * @param sb la trame a completer
	 * @param tab la ligne contenant les derniers octets a ajouter a la trame
	 * @param trames la liste des trames du fichier
	 * @param nbLigne le numero de la ligne
	 */
	private static void ajouteTrame(StringBuilder sb, String[] tab, List<String> trames, int nbLigne)  {
	
		
		for (int i = 1; i < tab.length; i++) {
			if (tab[i].equals("")) continue;
				
			sb.append(tab[i]);
		}
		trames.add(sb.toString());
		
	}
	
	
}
