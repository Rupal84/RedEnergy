// Copyright Red Energy Limited 2017

package simplenem12;

import java.io.File;
import java.util.Collection;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple test harness for trying out SimpleNem12Parser implementation
 */
public class TestHarness {
	private static final Logger logger = LogManager.getLogger(SimpleNem12ParserImpl.class);
  public static void main(String[] args) {
	  String filePath = null;
	  if(args== null || args.length == 0) {
		    Scanner scanner = new Scanner(System.in);
		    System.out.print("Enter absolute file path: ");
		    filePath = scanner.next();
	  } else {
		  filePath = args[0];
	  }
    File simpleNem12File = new File(filePath);

   //  Uncomment below to try out test harness.
    Collection<MeterRead> meterReads = new SimpleNem12ParserImpl().parseSimpleNem12(simpleNem12File);

    MeterRead read6123456789 = meterReads.stream().filter(mr -> mr.getNmi().equals("6123456789")).findFirst().get();
    logger.debug(String.format("Total volume for NMI 6123456789 is %f", read6123456789.getTotalVolume()));  // Should be -36.84

    MeterRead read6987654321 = meterReads.stream().filter(mr -> mr.getNmi().equals("6987654321")).findFirst().get();
    logger.debug(String.format("Total volume for NMI 6987654321 is %f", read6987654321.getTotalVolume()));  // Should be 14.33
  }
}
