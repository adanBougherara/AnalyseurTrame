package entete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import outil.Conversion;

/**
 * Classe definissant la couche IP
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class Ip implements Couche{
	private Map<String, Integer> protocoles, listeOptions;
	private int tailleEntete;
	private StringBuilder chaineAffichage, trame;
	private boolean affichageOK = false, hasOptions = false, hasTCP;
	
	
	/**
	 * 
	 * @param trame la trame a traiter
	 */
	public Ip(StringBuilder trame) {
		chaineAffichage = new StringBuilder("\nInternet Protocol (IP):");
		
		//taille en octet de l'entete IP
		tailleEntete = 4 * Conversion.HexaToDec(""+trame.charAt(1));
		if (tailleEntete > 20) hasOptions = true;
		
		
		//On stocke les octets qui nous interessent dans this.trame
		this.trame = new StringBuilder("");
		for (int i = 0; i < tailleEntete * 2; i++) {
			this.trame.append(trame.charAt(i));
		}
		
		//les differents protocoles...
		protocoles = new HashMap<>();
		protocoles.put("ICMP ", 1);
		protocoles.put("IGMP ", 2);
		protocoles.put("TCP ", 6);
		protocoles.put("EGP ", 8);
		protocoles.put("IGP ", 9);
		protocoles.put("UDP ", 17);
		protocoles.put("XTP ", 36);
		protocoles.put("RSVP ", 46);
		
		//les différentes options...
		listeOptions = new HashMap<>();
		listeOptions.put("End of Options List (EOOL) ", 0);
		listeOptions.put("No Operation (NOP)", 1);
		listeOptions.put("Record Route (RR) ", 7);
		listeOptions.put("Time Stamp (TS) ", 68);
		listeOptions.put("Loose Source Route (LSR) ", 131);
		listeOptions.put("Strict Source Route (SSR) ", 137);
		
		
		
		
		
		
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
	 * Gere l'affichage de la version
	 */
	public void affichageVersion() {
		StringBuilder version = new StringBuilder("\n\t");
		
		
		version.append(Conversion.HexaToBin(trame.charAt(0)));
		version.append(" .... = Version: ");
		version.append(Conversion.HexaToDec(trame.charAt(0)));
		
		chaineAffichage.append(version);
		
	}
	
	/**
	 * Gere l'affichage de l'IHL
	 */
	public void affichageIHL() {
		StringBuilder ihl = new StringBuilder("\n\t");
		ihl.append(".... ");
		ihl.append(Conversion.HexaToBin("" + trame.charAt(1)));
		ihl.append(" = Header Length: " + tailleEntete + " bytes (" + Conversion.HexaToDec("" + trame.charAt(1)) + ")");
		
		chaineAffichage.append(ihl);
	}
	
	/**
	 * Gere l'affichage du TOS
	 */
	public void affichageTos() {
		StringBuilder tos = new StringBuilder("\n\tTos: ");
		String tos_hexa = "" + trame.charAt(2) + trame.charAt(3);
		
		tos.append(Conversion.HexaToDec(tos_hexa));
		
		chaineAffichage.append(tos);
	}
	
	/**
	 * Gere l'affichage du total length
	 */
	public void affichageTotalLength() {
		StringBuilder totalLength = new StringBuilder("\n\tTotal Length: ");
		StringBuilder totalLength_hexa = new StringBuilder("");
		
		for (int i = 4; i < 8; i++) {
			totalLength_hexa.append(trame.charAt(i));
		}
		totalLength.append(Conversion.HexaToDec(totalLength_hexa.toString()));
		
		chaineAffichage.append(totalLength);
	}
	
	/**
	 * Gere l'affichage de l'identification
	 */
	public void afficheIdentification() {
		StringBuilder identification = new StringBuilder("\n\tIdentification: 0x");
		StringBuilder identification_hexa = new StringBuilder("");
		
		for (int i = 8; i < 12; i++) {
			identification_hexa.append(trame.charAt(i));
		}
		identification.append(identification_hexa);
		identification.append(" (");
		identification.append(Conversion.HexaToDec(identification_hexa.toString()));
		identification.append(")");
		
		chaineAffichage.append(identification);
	}
	
	/**
	 * Gere l'affichage des flags et du fragment offset
	 */
	public void afficheFlagsEtFragmentOffset() {
		
		//utile pour flags ET fragment offset (pour éviter de refaire les calculs 2x
		//dans une méthode plutot que dans constructeur mieux jpense
		StringBuilder flagsEtFragmentOffset_hexa = new StringBuilder("");
		for (int i = 12; i < 16; i++) {
			flagsEtFragmentOffset_hexa.append(trame.charAt(i));
		}
		StringBuilder flagsEtFragmentOffset_bin = new StringBuilder( Conversion.HexaToBin(flagsEtFragmentOffset_hexa.toString()));
				
		
		/*FLAGS*/
		StringBuilder flags = new StringBuilder("\n\t");
		
		
		List<StringBuilder> listeflags = new ArrayList<StringBuilder>(); 
		listeflags.add(new StringBuilder("\n\t\t" + flagsEtFragmentOffset_bin.charAt(0) + "... .... = Reserved bit: "));
		listeflags.add(new StringBuilder("\n\t\t." + flagsEtFragmentOffset_bin.charAt(1) + ".. .... = Don't fragment: "));
		listeflags.add(new StringBuilder("\n\t\t.." + flagsEtFragmentOffset_bin.charAt(2) + ". .... = More fragments: "));
		
		flags.append("Flags: 0x" + flagsEtFragmentOffset_hexa.charAt(0) + flagsEtFragmentOffset_hexa.charAt(1));
		
		for (int i = 0; i < listeflags.size(); i++) {
			if (flagsEtFragmentOffset_bin.charAt(i) == '0') listeflags.get(i).append("Not set");
			else {
				listeflags.get(i).append("Set");
			}
			flags.append(listeflags.get(i));
		}
		
		chaineAffichage.append(flags);
		
		/*FRAGMENT OFFSET*/
		
		StringBuilder fragmentOffset = new StringBuilder("\n\tFragment Offset: ");
		
		// on récupère les 13 bits définissant le fragment offset
		StringBuilder valeur = new StringBuilder("");
		for (int i = 3; i < 16; i++) {
			valeur.append(flagsEtFragmentOffset_bin.charAt(i));
		}
		
		//On convertit en décimale
		int valeur_decimale = Integer.parseInt(valeur.toString(), 2);
		
		// et on l'ajoute a notre chaine d'affichage
		fragmentOffset.append(valeur_decimale);
		
		chaineAffichage.append(fragmentOffset);
	}
	
	/**
	 * Gere l'affichage du TTL
	 */
	public void afficheTTL() {
		StringBuilder ttl = new StringBuilder("\n\tTime To Live: ");
		ttl.append(Conversion.HexaToDec(""+ trame.charAt(16) + trame.charAt(17)));
		
		chaineAffichage.append(ttl);
	}
	
	/**
	 * Gere l'affichage du protocole
	 */
	public void afficheProtocol() {
		StringBuilder protocol = new StringBuilder("\n\tProtocol: ");
		int valeur_protocol = Conversion.HexaToDec(""+ trame.charAt(18) + trame.charAt(19));
		for (Entry<String, Integer> entry : protocoles.entrySet() ) {
			if (entry.getValue() == valeur_protocol) {
				protocol.append(entry.getKey());
				if (entry.getValue() == 6) hasTCP = true;
				break;
			}
		}
		
		protocol.append("(" + valeur_protocol+ ")");
		
		chaineAffichage.append(protocol);
	}
	
	/**
	 * Permet de savoir si la trame contient le protocole TCP
	 * @return true si elle contient TCP, false sinon
	 */
	public boolean hasTCP() {
		if (!affichageOK) toString();
		return hasTCP;
	}
	
	/**
	 * Gere l'affichage du checksum
	 */
	public void afficheHeaderChecksum() {
		StringBuilder headerChecksum = new StringBuilder("\n\tHeader Checksum: 0x");
		
		StringBuilder checksum_hexa = new StringBuilder("");
		for (int i = 20; i < 24; i++) {
			checksum_hexa.append(trame.charAt(i));
		}
		headerChecksum.append(checksum_hexa);
		
		chaineAffichage.append(headerChecksum);
		
	}
	
	/**
	 * Gere l'affichage de l'adresse IP source
	 */
	public void afficheSrc() {
		
		StringBuilder src = new StringBuilder("\n\tSource address: ");
		StringBuilder tmp = new StringBuilder("");
		
		for (int i = 24; i < 32; i++) {
			if ((i % 2) == 0 && i != 24) {
				
				src.append(Conversion.HexaToDec(tmp.toString()));
				src.append(".");
				tmp = new StringBuilder("");
			}
			
			
			tmp.append(trame.charAt(i));
		}
		src.append(Conversion.HexaToDec(tmp.toString()));
		
		chaineAffichage.append(src);
	}
	
	/**
	 * Gere l'affichage de l'adresse IP destination
	 */
	public void afficheDest() {
		StringBuilder dest = new StringBuilder("\n\tDestination address: ");
		StringBuilder tmp = new StringBuilder("");
		
		for (int i = 32; i < 40; i++) {
			if ((i % 2) == 0 && i != 32) {
				dest.append(Conversion.HexaToDec(tmp.toString()));
				if (i!= 39) dest.append(".");
				tmp = new StringBuilder("");
			}
			
			tmp.append(trame.charAt(i));
		}
		dest.append(Conversion.HexaToDec(tmp.toString()));
		
		chaineAffichage.append(dest);
	}
	
	/**
	 * Gere l'affichage des potentielles options
	 */
	public void afficheOptions() {
		if (! hasOptions) return;
		
		
		//nb d'octets dans options
		int finOptions = 2* tailleEntete;
		StringBuilder options = new StringBuilder("\n\tOptions: (" + (tailleEntete - 20) + " bytes)\n");
		
		
		StringBuilder len_Option = new StringBuilder("");
		
		int OptionCourante = 40, type, sommeTailleOptions = 0, len_Option_dec, value = -1;
		
		
		while (OptionCourante < finOptions) {
			options.append("\t\tIP Option - ");
			
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
	
	
	@Override
	/**
	 * Recupere tous les affichages pour les reunir dans une String
	 */
	public String toString() {
		
		if (! affichageOK) {
			affichageVersion();
			affichageIHL();
			affichageTos();
			affichageTotalLength();
			afficheIdentification();
			afficheFlagsEtFragmentOffset();
			afficheTTL();
			afficheProtocol();
			afficheHeaderChecksum();
			afficheSrc();
			afficheDest();
			afficheOptions();
			
			affichageOK = true;
		}
		
		return chaineAffichage.toString();
	}

}
