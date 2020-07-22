package simplenem12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation for SimpleNem12Parser
 * @author kulwinder
 *
 */
public class SimpleNem12ParserImpl implements SimpleNem12Parser {
	private static final Logger logger = LogManager.getLogger(SimpleNem12ParserImpl.class);
	@Override
	public Collection<MeterRead> parseSimpleNem12(File simpleNem12File) throws FileReadException {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(simpleNem12File))) {
			return processRecords(bufferedReader);
		} catch (FileNotFoundException e) {
			throw new FileReadException("File not found", e);
		} catch (IOException e) {
			throw new FileReadException("I/O error occurred", e);
		} 
	}
	
	/**
	 * This method updates meter volumes in corresponding MeterRead object
	 * @param record
	 * @param meters
	 * @throws FileReadException
	 */
	private void updateMeterVolume(String record, List<MeterRead> meters) throws FileReadException{
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate localDate = LocalDate.parse(record.split(",")[1], formatter);
			MeterRead currentMeter = meters.get(meters.size() - 1);
			currentMeter.appendVolume(localDate, createMeterVol(record));
		} catch (DateTimeParseException e) {
			throw new FileReadException("Error occurred while parsing date in record: "+record, e);
		}
		
	}
	
	/**
	 * This method creates MeterRead objects for all 200 type records.
	 * @param Single record from file
	 * @return MeterRead
	 * @throws FileReadException
	 */
	private MeterRead createNewMeterRead(String line) throws FileReadException{
		try {
			logger.debug("createNewMeterRead method: Processing record:"+line);
			String[] cols = line.split(",");
			String strNmi = cols[1];
			String strUnit = cols[2];
			EnergyUnit unit = EnergyUnit.valueOf(strUnit);
			return new MeterRead(strNmi, unit);
		} catch(IllegalArgumentException e) {
			throw new FileReadException("Error occurred while parsing unit in record: "+line, e);
		}
	}

	/**
	 * This method creates MeterVolume object for 300 type records.
	 * @param single record from file
	 * @return MeterVolume
	 */
	private MeterVolume createMeterVol(String line) {
		try {
			String[] cols = line.split(",");
			BigDecimal volume = new BigDecimal(cols[2]);
			Quality quality = Quality.valueOf(cols[3]);
			return new MeterVolume(volume, quality);
		}
		catch(NumberFormatException e) {
			throw new FileReadException("Error occurred while parsing volume in record: "+line, e);
		}
		catch(IllegalArgumentException e) {
			throw new FileReadException("Error occurred while parsing reading type in record: "+line, e);
		}
	}
	
	/**
	 * This methods reads all records from file using streams to prevent out of memory errors.
	 * @param bufferedReader
	 * @return list of MeterRead objects
	 * @throws FileReadException
	 */
	public List<MeterRead> processRecords(BufferedReader bufferedReader) throws FileReadException{
		logger.debug("Processing started.....");
		List<MeterRead> meters = new ArrayList<>();
		
		String header = bufferedReader.lines().findFirst().map(Object::toString).get();
		if(!"100".equals(header)) {
			throw new FileReadException("Missing header record");
		}
		boolean endOfFile = false;
		Iterator<String> itr = bufferedReader.lines().iterator();
		while(itr.hasNext() && !endOfFile) {
			String currRecord = itr.next();
			String recordType = currRecord.split(",")[0];
			switch (recordType) {
			case "200" :
				meters.add(createNewMeterRead(currRecord));
				break;
			case "300":
				updateMeterVolume(currRecord, meters);
				break;
			case "900": 
				endOfFile = true;
				break;
			default:
				// Nothing much to do					
			}
		}
		logger.debug("Processing completed.....");
		return meters;
	}
}
