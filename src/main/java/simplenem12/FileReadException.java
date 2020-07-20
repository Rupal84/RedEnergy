package simplenem12;

/**
 * Exception class to be used for handling various file read scenarios.
 * @author kulwinder
 *
 */
public class FileReadException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	public FileReadException(String errorMessage) {
        System.err.println(errorMessage);
    }
    public FileReadException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        System.err.println(errorMessage);
    }
}
