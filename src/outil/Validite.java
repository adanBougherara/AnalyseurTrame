package outil;

/**
 * Classe contenant des methodes de test de validite sur des chaines ou caracteres
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class Validite {
	
	/**
	 * Determine si un caracetere est un chiffre
	 * @param c le caractere a tester
	 * @return true si le caractere est un chiffre, false sinon
	 */
	public static boolean isChiffre(char c) {
		return c >= '0' && c <= '9';
	}
	
	/**
	 * Determine si un caractere est une lettre entre A et F
	 * @param c le caractere a tester
	 * @return true si le caractere est une lettre existante en langage hexadecimale, false sinon
	 */
	public static boolean isLettre(char c) {	
		return (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'); 
	}
	
	/**
	 * Determine si le format de l'offset est valide
	 * @param s la chaine a tester
	 * @return true si l'offset est en format hexadecimal, false sinon
	 */
	public static boolean offsetValide(String s) {
		if (s.equals("")) return false;
		for (int i = 0; i < s.length(); i++)  {
			if (!isChiffre(s.charAt(i)) && !isLettre(s.charAt(i))) return false;
	
		}
		return true;
	}
	
	/**
	 * Determine si une chaine de caractere correspond a 2 hexa, c'est a dire un octet
	 * @param s la chaine a tester
	 * @return true si la chaine est en format hexadecimal de longueur 2, false sinon
	 */
	public static boolean codeValide(String s) {
		if (s.length() != 2) return false;
		for (int i = 0; i < s.length(); i++)  {
			if (!isChiffre(s.charAt(i)) && !isLettre(s.charAt(i))) return false;	
		}
		
		
		return true;
	}
}
