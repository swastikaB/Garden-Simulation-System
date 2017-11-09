package bhat.gupta.hummingbee.unitTestCases;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import bhat.gupta.hummingbee.controller.HummingBeeApp;

@RunWith(Parameterized.class)
public class JUnitAppTest {
	
	private String strMinTemp = "";
	private String strMaxTemp = "";
	private String strStartTime = "";
	private String strStopTime = "";
	
	private HummingBeeApp hummBeeTestApp;
	//private String[] input = new String[4];
	private boolean expectedResult;
	
	@Before
	public void initialize(){
		//System.out.println("Initialization");
		hummBeeTestApp = new HummingBeeApp();
	}
	

	public JUnitAppTest(String input0, String input1, String input2, String input3, boolean expectedResult){
		this.strMinTemp = input0;
		this.strMaxTemp = input1;
		this.strStartTime = input2;
		this.strStopTime = input3;
		this.expectedResult = expectedResult;
	}
	
	@Parameterized.Parameters
	public static Collection inputData(){
		return Arrays.asList(new Object[][] {
			{"41","90", "10:31", "10:32", true},
			{"91","90", "10:31", "10:32", false},
			{"45","65", "", "", false},
			{"","", "10:31", "10:32", true},
			{"41","90", "10:32", "10:31", false},
		});
	}
	
	@Test
	public void TestValidateData(){
		//System.out.println("Input is : " + input0 + input1, input2, input3);
		//System.out.println("output is : " + hummBeeTestApp.validateData(strMinTemp, strMaxTemp, strStartTime, strStopTime));
		assertEquals(expectedResult, hummBeeTestApp.validateData(strMinTemp, strMaxTemp, strStartTime, strStopTime));
	}
}
