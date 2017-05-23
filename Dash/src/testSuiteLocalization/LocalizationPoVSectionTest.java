package testSuiteLocalization;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;

import helperObjects.LocalizationHelper;
import localization.PointOfView;


public class LocalizationPoVSectionTest extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	@Test
	public static void LocalizationPoVSection() throws Exception {

		String languageTag = LocalizationHelper.languageTag;
		
		PointOfView.verifyLocalizationOnPoVSection(languageTag);
		
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Localization finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}

}
