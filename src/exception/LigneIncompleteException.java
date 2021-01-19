package exception;

/**
 * Leve une exception pour une ligne incomplete dans la trame
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public class LigneIncompleteException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param msg le message d'erreur a afficher
	 */
	public LigneIncompleteException(String msg) {
		super(msg);
	}
}
