package simplenem12;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mockito.Mockito;

public class SimpleNem12ParserTests {
	@Rule public TestName name = new TestName();
	private static final Logger logger = LogManager.getLogger(SimpleNem12ParserTests.class);
	
	static SimpleNem12ParserImpl parser = null;
	static BufferedReader bufferedReader = null;
	
	@BeforeClass
	public static void beforeAllTestMethods() {
		
		parser = new SimpleNem12ParserImpl();
		bufferedReader = Mockito.mock(BufferedReader.class);
	}

	@Test
	public void shouldCreateTwoMeterReadObjs() {
		logger.debug(name.getMethodName());
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWH");
		lines.add("300,20170311,3,A");
		lines.add("300,20170312,3,E");
		lines.add("200,9876543210,KWH");
		lines.add("300,20170311,3,A");
		lines.add("300,20170312,3,E");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		List<MeterRead> meters = parser.processRecords(bufferedReader);
		assertEquals(2, meters.size());
	}	

	@Test
	public void shouldThrowExceptionForMissingHeader() {
		logger.debug(name.getMethodName());
		List<String> lines = new ArrayList<>();
		lines.add("200,1234567890,KWH");
		lines.add("300,20170311,3,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		FileReadException ex = assertThrows(FileReadException.class, ()->parser.processRecords(bufferedReader));
		assertTrue(ex.getMessage().contains("Missing header record"));
	}	

	@Test
	public void shouldThrowExceptionForInCorrectUnit() {
		logger.debug(name.getMethodName());
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWA");
		lines.add("300,20170311,3,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		FileReadException ex = assertThrows(FileReadException.class, ()->parser.processRecords(bufferedReader));
		assertTrue(ex.getMessage().contains("parsing unit"));
	}
	
	@Test
	public void shouldThrowExceptionForInCorrectReadingType() {
		logger.debug(name.getMethodName());
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWH");
		lines.add("300,20170311,3,Invalid");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		FileReadException ex = assertThrows(FileReadException.class, ()->parser.processRecords(bufferedReader));
		assertTrue(ex.getMessage().contains("parsing reading type"));
	}
	
	@Test
	public void shouldThrowExceptionForInvalidVolume() {
		logger.debug(name.getMethodName());
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWH");
		lines.add("300,20170311,Invalid,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		FileReadException ex = assertThrows(FileReadException.class, ()->parser.processRecords(bufferedReader));
		assertTrue(ex.getMessage().contains("parsing volume"));
	}
	
	@Test
	public void shouldThrowExceptionForInvalidDate() {
		logger.debug(name.getMethodName());
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWH");
		lines.add("300,invalid,3,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		FileReadException ex = assertThrows(FileReadException.class, ()->parser.processRecords(bufferedReader));
		assertTrue(ex.getMessage().contains("parsing date"));
	}
	
}