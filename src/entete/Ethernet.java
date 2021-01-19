package entete;

import outil.Conversion;
/**
 * Classe definissant la couche ethernet
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class Ethernet implements Couche{
	private StringBuilder trame, chaineAffichage;
	private int ip, arp;
	private boolean affichageOK = false, hasIP = false;
	
	
	/**
	 * 
	 * @param trame la trame a traiter
	 */
	public Ethernet(StringBuilder trame) {
		chaineAffichage = new StringBuilder("Champ Ethernet:");
		this.trame = new StringBuilder();
		
		for (int i = 0; i < 28; i++) {
			this.trame.append(trame.charAt(i));
		}
		
		ip = Conversion.HexaToDec("0800");
		arp = Conversion.HexaToDec("0806");
		
	}
	
	@Override
	public StringBuilder actualiserTrame(StringBuilder trame) {
		
		StringBuilder new_trame = new StringBuilder("");
		
		for (int i = 28; i < trame.length(); i++) {
			new_trame.append(trame.charAt(i));
		}
		
		return new_trame;
	}
	
	/**
	 * Gere l'affichage de l'adresse mac destination
	 */
	public void affichageDest() {
		StringBuilder mac_destination = new StringBuilder("\n\tDestination: ");
		for (int i = 0; i < 12; i++) {
			if (i != 0 && (i % 2) == 0) mac_destination.append(':');
			mac_destination.append(trame.charAt(i));
			
		}
		chaineAffichage.append(mac_destination);
	}
	
	/**
	 * Gere l'affichage de l'adresse mac destination
	 */
	public void affichageSrc() {
		StringBuilder mac_source = new StringBuilder("\n\tSource: ");
		for (int i = 12; i < 24; i++) {
			if (i != 12 && (i % 2) == 0) mac_source.append(':');
				mac_source.append(trame.charAt(i));
			}	
		
		chaineAffichage.append(mac_source);
		}
	
	/**
	 * Gere l'affichage du type
	 */
	public void affichageType() {
		StringBuilder type = new StringBuilder("\n\tType: ");
		
		StringBuilder type_hexa = new StringBuilder("");
		
		for (int i = 24; i < 28; i++) {
			type_hexa.append(trame.charAt(i));
		}
		
		if (Conversion.HexaToDec(type_hexa.toString()) == ip) {
			type.append("IPv4");
			hasIP = true;
		}
		if (Conversion.HexaToDec(type_hexa.toString()) == arp) type.append("ARP");
		
		type.append(" (0x");
		type.append(type_hexa);
		type.append(")");
		
		chaineAffichage.append(type);
			
	}
	
	/**
	 * Permet de savoir si la trame contient le protocole IPv4
	 * @return true si le protocole est IP, false sinon
	 */
	public boolean hasIP() {
		if (!affichageOK) toString();
		return hasIP;
	}
		
	@Override
	/**
	 * Recupere tous les affichages pour les reunir dans une String
	 */
	public String toString() {
		if (! affichageOK) {
			affichageDest();
			affichageSrc();
			affichageType();
			
			affichageOK = true;
		}
		chaineAffichage.append("\n");
		return chaineAffichage.toString();
	}
	
	
}