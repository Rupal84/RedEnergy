package simplenem12;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

public class SimpleNem12ParserTests {
	
	static SimpleNem12ParserImpl parser = null;
	static BufferedReader bufferedReader = null;
	
	@BeforeClass
	public static void beforeAllTestMethods() {
		parser = new SimpleNem12ParserImpl();
		bufferedReader = Mockito.mock(BufferedReader.class);
	}

	@Before
	public void beforeEachTestMethod() {
		System.out.println("Invoked before each test method");
	}

	@After
	public void afterEachTestMethod() {
		System.out.println("Invoked after each test method");
	}

	@AfterClass
	public static void afterAllTestMethods() {
		System.out.println("Invoked once after all test methods");
	}

	@Test
	public void shouldCreateTwoMeterReadObjs() {
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

	@Test(expected = FileReadException.class)
	public void shouldThrowErrorForMissingHeader() {
		List<String> lines = new ArrayList<>();
		lines.add("200,1234567890,KWH");
		lines.add("300,20170311,3,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		parser.processRecords(bufferedReader);
	}	

	@Test(expected = FileReadException.class)
	public void shouldThrowErrorForInCorrectUnit() {
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWA");
		lines.add("300,20170311,3,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		parser.processRecords(bufferedReader);
	}
	
	@Test(expected = FileReadException.class )
	public void shouldThrowErrorForInCorrectReadingType() {
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWH");
		lines.add("300,20170311,3,Invalid");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		parser.processRecords(bufferedReader);
	}
	
	@Test(expected = FileReadException.class)
	public void shouldThrowErrorForInvalidVolume() {
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWH");
		lines.add("300,20170311,Invalid,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		parser.processRecords(bufferedReader);
	}
	
	@Test(expected = FileReadException.class)
	public void shouldThrowErrorForInvalidDate() {
		List<String> lines = new ArrayList<>();
		lines.add("100");
		lines.add("200,1234567890,KWH");
		lines.add("300,invalid,3,A");
		lines.add("900");
		List<String> lstAllButFirst = lines.stream().skip(1).collect(Collectors.toList());
		Mockito.when(bufferedReader.lines()).thenReturn(lines.stream()).thenReturn(lstAllButFirst.stream());
		parser.processRecords(bufferedReader);
	}
	
}