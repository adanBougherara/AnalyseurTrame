package outil;

import java.math.BigInteger;

/**
 * Classe contenant des methodes de conversions
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class Conversion {
	
	/**
	 * Convertit une chaine hexadecimale en chaine binaire
	 * @param s la chaine hexadecimale a convertir
	 * @return la chaine binaire correspondante (4 chiffres binaires pour un chiffre hexa)
	 */
	public static String HexaToBin(String s) {
		if (s.equals("")) return "";
		
		int i = 0;
		String padded = "";
		
		
		i = Integer.parseInt(s, 16);
		
		if (s.length() <= 1) padded += String.format("%4s", Integer.toBinaryString(i)).replace(' ', '0');
	    
		else {
			if (s.length() <= 2) padded += String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
			else {
				if (s.length() <= 3) padded += String.format("%12s", Integer.toBinaryString(i)).replace(' ', '0');
				else {
					padded += String.format("%16s", Integer.toBinaryString(i)).replace(' ', '0');
				}
			}
		}
	   
	    return padded;
	}
	
	/**
	 * Convertit un caractere hexadecimal en binaire
	 * @param c un caractere hexadecimal
	 * @return le caractere binaire correspondant
	 */
	public static String HexaToBin(char c) {
		String s = "" + c;
	
		return HexaToBin(s);
	}
	
	/**
	 * Convertit une chaine hexadecimale en decimal (pour les entiers trop grands pour etre des int)
	 * @param s la chaine a convertir
	 * @return le nombre decimale correspondant
	 */
	public static BigInteger HexaToDecBigNumbers(String s) {
		return new BigInteger(s, 16);
	}
	
	/**
	 * Convertit une chaine hexadecimale en decimal
	 * @param s la chaine a convertir
	 * @return le nombre decimal correspondant
	 */
	public static int HexaToDec(String s) {
		return Integer.parseInt(s, 16);
	}
	
	/**
	 * Convertit un caractere hexadecimal en decimal
	 * @param c le caractere a convertir
	 * @return le nombre decimal correspondant
	 */
	public static int HexaToDec(char c) {
		String s = "" + c;
		return HexaToDec(s);
	}
	
	/**
	 * Convertit une chaine hexadecimale en code ASCII
	 * @param hexStr la chaine a convertir
	 * @return le code correspondant
	 */
	public static String HexaToAscii(String hexStr) {
		StringBuilder output = new StringBuilder("");
	    
	    for (int i = 0; i < hexStr.length(); i += 2) {
	        String str = hexStr.substring(i, i + 2);
	        output.append((char) Integer.parseInt(str, 16));
	    }
	    
	    return output.toString();
	}
	
	
	
}
