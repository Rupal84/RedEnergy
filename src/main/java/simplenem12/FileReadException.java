package simplenem12;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Exception class to be used for handling various file read scenarios.
 * @author kulwinder
 *
 */
public class FileReadException extends RuntimeException{
	private static final Logger logger = LogManager.getLogger(SimpleNem12ParserImpl.class);
	private static final long serialVersionUID = 1L;
	public FileReadException(String errorMessage) {
		logger.error(errorMessage);
    }
    public FileReadException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        logger.error(errorMessage, err);
    }
}
