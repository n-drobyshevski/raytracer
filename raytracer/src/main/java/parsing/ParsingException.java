package parsing;

/**
 * Exception personnalisée pour les erreurs lors de l'analyse du fichier scène.
 */
public class ParsingException extends RuntimeException  {
  /**
   * Crée une exception avec un message explicite.
   * @param message Message d'erreur détaillé
   */
  public ParsingException(String message) {
    super(message);
  }
}
