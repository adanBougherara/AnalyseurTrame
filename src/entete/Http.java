package entete;

import outil.Conversion;

/**
 * Classe definissant la couche HTTP
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class Http implements Couche {
	private StringBuilder trame, chaineAffichage;
	private boolean isServer, affichageOK = false;
	private int curseur;
	private String octetCourant = "";
	
	/**
	 * 
	 * @param trame la trame a traiter
	 * @param isServer indique si le serveur est la source ou le destinataire
	 */
	public Http(StringBuilder trame, boolean isServer) {
		this.trame = trame;
		this.isServer = isServer;
		curseur = 0;
		
		chaineAffichage = new StringBuilder("\n\nHypertext Transfer Protocol (HTTP): ");
		
		if (isServer) chaineAffichage.append("Response\n");
		else {
			chaineAffichage.append("Request\n");
		}
	}
	
	
	@Override
	public StringBuilder actualiserTrame(StringBuilder trame) {
		
		return null;
	}

	/**
	 * Gere l'affichage de la methode ( cas HTTP Request)
	 */
	public void affichageMethode() {
		StringBuilder methode = new StringBuilder("\tMethode: ");
		StringBuilder hexa = new StringBuilder("");
		octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
		
		while ( ! octetCourant.equals("20")) {
			hexa.append(octetCourant.charAt(0));
			curseur++;
			octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
	
		}
		methode.append(Conversion.HexaToAscii(hexa.toString()) + "\n");
		
		curseur += 2;
		chaineAffichage.append(methode.toString());
	}
	
	/**
	 * Gere l'affichage de l'URI ( cas HTTP Request)
	 */
	public void affichageURI() {
		StringBuilder url = new StringBuilder("\tURI: ");
		StringBuilder hexa = new StringBuilder("");
		octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
		
		while ( ! octetCourant.equals("20")) {
			hexa.append(octetCourant.charAt(0));
			curseur++;
			octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
	
		}
		url.append(Conversion.HexaToAscii(hexa.toString()) + "\n");
		
		curseur += 2;
		chaineAffichage.append(url.toString());
	}
	
	/**
	 * Gere l'affichage de la version ( cas HTTP Request)
	 */
	public void affichageVersionRequete() {
		StringBuilder version = new StringBuilder("\tVersion: ");
		StringBuilder hexa = new StringBuilder("");
		octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur + 3);
		
		while ( ! octetCourant.equals("0d0a")) {
			hexa.append(octetCourant.charAt(0));
			curseur++;
			octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur + 3);
	
		}
		version.append(Conversion.HexaToAscii(hexa.toString()) + "\n");
		
		//curseur += 4;
		chaineAffichage.append(version.toString());
	}
	
	/**
	 * Gere l'affichage de la version ( cas HTTP Response)
	 */
	public void affichageVersionReponse() {
		StringBuilder version = new StringBuilder("\tVersion: ");
		StringBuilder hexa = new StringBuilder("");
		octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
		
		while ( ! octetCourant.equals("20")) {
			hexa.append(octetCourant.charAt(0));
			curseur++;
			octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
	
		}
		version.append(Conversion.HexaToAscii(hexa.toString()) + "\n");
		
		curseur += 2;
		chaineAffichage.append(version.toString());
	}
	
	/**
	 * Gere l'affichage du code status ( cas HTTP Response)
	 */
	public void affichageCodeStatus() {
		StringBuilder code = new StringBuilder("\tStatus Code: ");
		StringBuilder hexa = new StringBuilder("");
		octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
		
		while ( ! octetCourant.equals("20")) {
			hexa.append(octetCourant.charAt(0));
			curseur++;
			octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1);
	
		}
		code.append(Conversion.HexaToAscii(hexa.toString()) + "\n");
		
		curseur += 2;
		chaineAffichage.append(code.toString());
	}
	
	/**
	 * Gere l'affichage du message ( cas HTTP Response)
	 */
	public void affichageMessage() {
		StringBuilder msg = new StringBuilder("\tMessage: ");
		StringBuilder hexa = new StringBuilder("");
		octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur + 3);
		
		while ( ! octetCourant.equals("0d0a")) {
			hexa.append(octetCourant.charAt(0));
			curseur++;
			octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur + 3);
	
		}
		msg.append(Conversion.HexaToAscii(hexa.toString()) + "\n");
		
		//curseur += 4;
		chaineAffichage.append(msg.toString());
	}
	
	/**
	 * Gere l'affichage des potentielles entetes ( cas HTTP Request et Response)
	 */
	public void affichageEntetes() {
		
		StringBuilder entetes = new StringBuilder("\tEntetes:\n");
		StringBuilder hexa = new StringBuilder("");
		octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur + 3) +
				trame.charAt(curseur+4) + trame.charAt(curseur+5) + trame.charAt(curseur+6) + trame.charAt(curseur +7);
		
		String buffer = ""; 
		
		while ( ! octetCourant.equals("0d0a0d0a")) {
			curseur += 4;
			buffer = "" + trame.charAt(curseur) + trame.charAt(curseur +1);
			while (! buffer.equals("20")) {
				hexa.append(buffer.charAt(0));
				curseur++;
				buffer = "" + trame.charAt(curseur) + trame.charAt(curseur +1);
			}
			entetes.append("\t\t" + Conversion.HexaToAscii(hexa.toString()) + " ");
			curseur += 2;
			hexa = new StringBuilder("");
			
			buffer = "" + trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur+3);
			while (! buffer.equals("0d0a")) {
				hexa.append(buffer.charAt(0));
				curseur++;
				buffer = "" + trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur+3); 
			}
			entetes.append(Conversion.HexaToAscii(hexa.toString()) + "\n");
			hexa = new StringBuilder("");
			
			octetCourant = "" +  trame.charAt(curseur) + trame.charAt(curseur+1) + trame.charAt(curseur+2) + trame.charAt(curseur + 3) +
					trame.charAt(curseur+4) + trame.charAt(curseur+5) + trame.charAt(curseur+6) + trame.charAt(curseur +7);
		}
		
		curseur += 8;
		chaineAffichage.append(entetes.toString());
	}
	
	@Override
	/**
	 * Recupere tous les affichages pour les reunir dans une String
	 */
	public String toString() {
		if (! affichageOK) {
			if (isServer) {
				affichageVersionReponse();
				affichageCodeStatus();
				affichageMessage();	
			}
			else {
				affichageMethode();
				affichageURI();
				affichageVersionRequete();
			}
			affichageEntetes();
		}
		
		return chaineAffichage.toString();
	}
}
	
	