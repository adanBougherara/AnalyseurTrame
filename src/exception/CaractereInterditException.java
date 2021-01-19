package exception;

/**
 * Leve une exception pour un caractere non hexadecimal dans la trame
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class CaractereInterditException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param msg le message d'erreur a afficher
	 */
	public CaractereInterditException(String msg) {
		super(msg);
	}
}
