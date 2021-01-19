package entete;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import outil.Conversion;

/**
 * Classe definissant la couche TCP
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class Tcp implements Couche {
	private StringBuilder trame, chaineAffichage;
	private boolean affichageOK = false, hasOptions = false, hasHTTP = false, isServerHTTP = false;
	private int tailleEntete;
	Map<String, Integer> listeOptions;
	
	/**
	 * 
	 * @param trame la trame a traiter
	 */
	public Tcp (StringBuilder trame) {
		this.trame = trame;
		chaineAffichage = new StringBuilder("\n\nTransmission Control Protocol (TCP):");
		
		tailleEntete = 4 * Conversion.HexaToDec(trame.charAt(24));
		if (tailleEntete > 20) hasOptions = true;
		
		//les différentes options...
		listeOptions = new HashMap<>();
		listeOptions.put("End of Options List (EOOL) ", 0);
		listeOptions.put("No Operation (NOP) ", 1);
		listeOptions.put("Maximum Segment Size (MSS) ", 2);
		listeOptions.put("WSOPT - Window Scale ", 3);
		listeOptions.put("SACK Permitted ", 4);
		listeOptions.put("SACK (Selective ACK) ", 5);
		listeOptions.put("Echo (obsoleted by option 8) ", 6);
		listeOptions.put("Echo Reply (obsoleted by option 8) ", 7);
		listeOptions.put("TSOPT - Time Stamp Option ", 8);
		listeOptions.put("Partial Order Connection Permitted ", 9);
		listeOptions.put("Partial Order Service Profile ", 10);
		listeOptions.put("CC  ", 11);
		listeOptions.put("CC.NEW ", 12);
		listeOptions.put("CC.ECHO ", 13);
		listeOptions.put("TCP Alternate Checksum Request ", 14);
		listeOptions.put("TCP Alternate Checksum Data ", 15);
		
	}
	
	@Override
	public StringBuilder actualiserTrame(StringBuilder trame) {
		StringBuilder new_trame = new StringBuilder("");
		for (int i = 2*tailleEntete; i < trame.length(); i++) {
			new_trame.append(trame.charAt(i));
		}
		
		return new_trame;
	}

	/**
	 * Gere l'affichage du port source
	 */
	public void affichageSrc() {
		StringBuilder src = new StringBuilder("\n\tSource Port : ");
		StringBuilder hexa = new StringBuilder("");
		for (int i = 0; i < 4; i++) {
			hexa.append(trame.charAt(i));
		}
		int decimal = Conversion.HexaToDec(hexa.toString());
		if (decimal == 80) {
			hasHTTP = true;
			isServerHTTP = true;
		}
		src.append(decimal);
		
		chaineAffichage.append(src);
	}
	
	/**
	 * Gere l'affichage du port destination
	 */
	public void affichageDest() {
		StringBuilder dest = new StringBuilder("\n\tDestination Port : ");
		StringBuilder hexa = new StringBuilder("");
		for (int i = 4; i < 8; i++) {
			hexa.append(trame.charAt(i));
		}
		int decimal = Conversion.HexaToDec(hexa.toString());
		dest.append(decimal);
		if (decimal == 80) hasHTTP = true;
		chaineAffichage.append(dest);
	}
	
	/**
	 * Gere l'affichage du sequence number
	 */
	public void affichageSeqNum() {
		StringBuilder seqNum = new StringBuilder("\n\tSequence Number: ");
		StringBuilder hexa = new StringBuilder("");
		for (int i = 8; i < 16; i++) {
			hexa.append(trame.charAt(i));
		}
		seqNum.append(Conversion.HexaToDecBigNumbers(hexa.toString()));
		chaineAffichage.append(seqNum);
		
	}
	
	/**
	 * Gere l'affichage de l'ack number
	 */
	public void affichageAckNum() {
		StringBuilder ackNum = new StringBuilder("\n\tAcknowledgment Number: ");
		StringBuilder hexa = new StringBuilder("");
		for (int i = 16; i < 24; i++) {
			hexa.append(trame.charAt(i));
		}
		ackNum.append(Conversion.HexaToDecBigNumbers(hexa.toString()));
		chaineAffichage.append(ackNum);
	}
	
	/**
	 * Gere l'affichage du data offset
	 */
	public void affichageDataOffset() {
		StringBuilder dataOffset = new StringBuilder("\n\t");
		dataOffset.append(Conversion.HexaToBin(trame.charAt(24)));
		dataOffset.append(" .... = Header Length: ");
		dataOffset.append(tailleEntete + " bytes (" + trame.charAt(24) + ")");
		
		chaineAffichage.append(dataOffset);
	}
	
	/**
	 * Gere l'affichage de reserved et des flags
	 */
	public void affichageReservedEtFlags() {
		StringBuilder ReservedEtFlags_hexa = new StringBuilder("");
		for (int i = 25; i < 28; i++) {
			ReservedEtFlags_hexa.append(trame.charAt(i));	
		}		
		String ReservedEtFlags_bin = Conversion.HexaToBin(ReservedEtFlags_hexa.toString());
		
		
		
		StringBuilder res = new StringBuilder("\n\tFlags: 0x" + ReservedEtFlags_hexa);
		
		StringBuilder reserved = new StringBuilder("\n\t\t");
		for (int i = 0; i < 3; i++) {
			reserved.append(ReservedEtFlags_bin.charAt(i));
			
		}
		reserved.append(". .... .... = Reserved: Not set");
		res.append(reserved);
		
		StringBuilder[] tabFlags = new StringBuilder[9];
		
		
		tabFlags[0] = new StringBuilder("\n\t\t");
		tabFlags[0].append("..." + ReservedEtFlags_bin.charAt(3));
		tabFlags[0].append(" .... .... = Nonce: ");
		
		tabFlags[1] = new StringBuilder("\n\t\t");
		tabFlags[1].append(".... " + ReservedEtFlags_bin.charAt(4));
		tabFlags[1].append("... .... = Congestion Window Reduced (CWR): ");
		
		tabFlags[2] = new StringBuilder("\n\t\t");
		tabFlags[2].append(".... ." + ReservedEtFlags_bin.charAt(5));
		tabFlags[2].append(".. .... = ECN-Echo: ");
		
		tabFlags[3] = new StringBuilder("\n\t\t");
		tabFlags[3].append(".... .." + ReservedEtFlags_bin.charAt(6));
		tabFlags[3].append(". .... = Urgent: ");
		
		tabFlags[4] = new StringBuilder("\n\t\t");
		tabFlags[4].append(".... ..." + ReservedEtFlags_bin.charAt(7));
		tabFlags[4].append(" .... = Acknowledgment: ");
		
		tabFlags[5] = new StringBuilder("\n\t\t");
		tabFlags[5].append(".... .... " + ReservedEtFlags_bin.charAt(8));
		tabFlags[5].append("... = Push: ");
		
		tabFlags[6] = new StringBuilder("\n\t\t");
		tabFlags[6].append(".... .... ." + ReservedEtFlags_bin.charAt(9));
		tabFlags[6].append(".. = Reset: ");
		
		tabFlags[7] = new StringBuilder("\n\t\t");
		tabFlags[7].append(".... .... .." + ReservedEtFlags_bin.charAt(10));
		tabFlags[7].append(". = Syn: ");
		
		tabFlags[8] = new StringBuilder("\n\t\t");
		tabFlags[8].append(".... .... ..." + ReservedEtFlags_bin.charAt(11));
		tabFlags[8].append(" = Fin: ");
		
		
		
		for (int i = 0; i < tabFlags.length; i++) {
			if (ReservedEtFlags_bin.charAt(i+3) == '1') tabFlags[i].append("Set");
			else {
				tabFlags[i].append("Not set");
			}
			
			res.append(tabFlags[i]);
		}
		
		chaineAffichage.append(res);
	}
	
	/**
	 * Gere l'affichage du window
	 */
	public void affichageWindow() {
		StringBuilder window = new StringBuilder("\n\tWindow: ");
		
		StringBuilder window_hexa = new StringBuilder("");
		for (int i = 28; i < 32; i++) {
			window_hexa.append(trame.charAt(i));
		}
		
		window.append(Conversion.HexaToDec(window_hexa.toString()));
		chaineAffichage.append(window);
	
	}
	
	/**
	 * Gere l'affichage du checksum
	 */
	public void affichageChecksum() {
		StringBuilder checksum = new StringBuilder("\n\tChecksum: 0x");
		
		
		for (int i = 32; i < 36; i++) {
			checksum.append(trame.charAt(i));
		}
		
		chaineAffichage.append(checksum);
	}
	
	/**
	 * Gere l'affichage de l'urgent pointer
	 */
	public void affichageUrgentPointer() {
		StringBuilder urgent = new StringBuilder("\n\tUrgent pointer: ");
		
		StringBuilder urgent_hexa = new StringBuilder("");
		for (int i = 36; i < 40; i++) {
			urgent_hexa.append(trame.charAt(i));
		}
		
		urgent.append(Conversion.HexaToDec(urgent_hexa.toString()));
		chaineAffichage.append(urgent);
	}
	
	/**
	 * Gere l'affichage des potentielles options
	 */
	public void affichageOptions() {
		if (! hasOptions) return;
		
		
		//nb d'octets dans options
		int finOptions = 2* tailleEntete;
		StringBuilder options = new StringBuilder("\n\tOptions: (" + (tailleEntete - 20) + " bytes)\n");
		
		
		StringBuilder len_Option = new StringBuilder("");
		
		int OptionCourante = 40, type, sommeTailleOptions = 0, len_Option_dec, value = -1;
		
		
		while (OptionCourante < finOptions) {
			options.append("\t\tTCP Option - ");
			
			//valeur décimale du type
			type = Conversion.HexaToDec(""+trame.charAt(OptionCourante) + trame.charAt(OptionCourante+1));
			
			//On cherche si l'option est reconnue dans notre liste
			for (Entry<String, Integer> entry : listeOptions.entrySet()) {
				
				if (entry.getValue() == type)  {
					//Si on la trouve, on affiche son nom
					options.append(entry.getKey());
					value = entry.getValue();
					break;
				}
				
			}
			
			if (value == 0 || value == 1) {
				len_Option_dec = 1;
			}
			else {
				len_Option.append(trame.charAt(OptionCourante+2));
				len_Option.append(trame.charAt(OptionCourante+3));
				len_Option_dec = Conversion.HexaToDec(len_Option.toString());
				
				if (value == -1) options.append("Unknown option");
			}
			
			options.append(" (" + len_Option_dec + " bytes)\n");
			sommeTailleOptions += len_Option_dec;
			OptionCourante += 2 * len_Option_dec;
		}
		
		int padding = (tailleEntete - 20) - (sommeTailleOptions);
		if (padding > 0) {
			options.append("\tPadding : " + padding + " byte(s)");
			
		}
		
		chaineAffichage.append(options);
	}
	
	/**
	 * Permet de savoir si la trame contient le protocole HTTP
	 * @return true si elle contient HTTP, false sinon
	 */
	public boolean hasHTTP() {
		if (!affichageOK) toString();
		return hasHTTP;
	}
	
	/**
	 * Determine si la source est le serveur (appelee seulement si la trame contient HTTP)
	 * @return true si la source est le serveur, false sinon
	 */
	public boolean srcIsServer() {
		if (!affichageOK) toString();
		return isServerHTTP;
		
	}
	
	@Override
	/**
	 * Recupere tous les affichages pour les reunir dans une String
	 */
	public String toString() {
		if (! affichageOK) {
			affichageSrc();
			affichageDest();
			affichageSeqNum();
			affichageAckNum();
			affichageDataOffset();
			affichageReservedEtFlags();
			affichageWindow();
			affichageChecksum();
			affichageUrgentPointer();
			affichageOptions();
			
			affichageOK = true;
		}
		return chaineAffichage.toString();
	}
}