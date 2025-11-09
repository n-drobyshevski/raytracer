package parsing;

/**
 * Exception personnalisée pour les erreurs lors de l'analyse du fichier scène.
 */
public class ParsingException extends RuntimeException  {
  public ParsingException(String message) {
    super(message);
  }
}