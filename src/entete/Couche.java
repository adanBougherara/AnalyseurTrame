package entete;

/**
 * Interface d�finissant une couche 
 * @author Adan Bougherara et Aurelien Soutil
 *
 */
public interface Couche {
	/**
	 * Permet d'actualiser la trame apr�s le traitement de la couche
	 * @param trame la trame a traiter
	 * @return la nouvelle trame destinee aux couches suivantes
	 */
	public StringBuilder actualiserTrame(StringBuilder trame);
}
