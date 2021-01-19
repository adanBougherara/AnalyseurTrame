package outil;

import java.io.IOException;
import java.util.List;
import entete.*;
import exception.*;

/**
 * Classe contenant la methode coeur du projet
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class Analyseur {
	
	/**
	 * Methode qui recupere le contenu d'un fichier trame et qui l'analyse en gerant l'affichage correspondant aux differentes couches
	 * @param path le path du file
	 * @return le contenu a afficher dans l'interface graphique
	 * @throws IOException en cas de probleme d'ouverture de fichier
	 * @throws CaractereInterditException en cas de detection d'un caractere interdit dans la trame
	 * @throws LigneIncompleteException en cas de detection d'une ligne incomplete
	 */
	public static String affichage(String path) throws IOException, CaractereInterditException, LigneIncompleteException {
		StringBuilder affichage = new StringBuilder("");
		
		List<String> trames = OpenFile.recupererContenu(path);
		StringBuilder trame;
			
		for (int i = 0; i < trames.size(); i++) {	
			affichage.append("Trame " + (i+1) +":\n\n");
			trame = new StringBuilder(trames.get(i));
				
				
			Ethernet coucheEthernet = new Ethernet(trame);
			trame = coucheEthernet.actualiserTrame(trame);
			affichage.append(coucheEthernet);
	
			if (coucheEthernet.hasIP()) {
				Ip coucheIp = new Ip(trame);
				trame = coucheIp.actualiserTrame(trame);
				affichage.append(coucheIp);
				
					
					if (coucheIp.hasTCP()) {
						Tcp coucheTcp = new Tcp(trame);
						trame = coucheTcp.actualiserTrame(trame);
						affichage.append(coucheTcp);	
						
						if (coucheTcp.hasHTTP()) {
							Http coucheHttp = new Http(trame, coucheTcp.srcIsServer());
							affichage.append(coucheHttp);
						}
					}
					
			}
			affichage.append("\n\n");
		}
	 
		
		return affichage.toString();
	}
}
