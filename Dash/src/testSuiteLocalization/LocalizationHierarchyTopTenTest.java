package testSuiteLocalization;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.HierarchyHelper;
import helperObjects.LocalizationHelper;
import localization.TopTen;

public class LocalizationHierarchyTopTenTest extends BaseClass {

	
	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	@Test
	public static void LocalizationHierarchyTopTen() throws Exception {

		
		// #1 Select the "VIEW BY HIERARCHY" button
		HierarchyHelper.selectHierarchyView();
		
		// #2 Select the "TOP TEN VIEW" 
		HierarchyHelper.selectTopTenView(2);
		Thread.sleep(2000);
		
		String languageTag = LocalizationHelper.languageTag;
		
		String month = "January";
		String year = "2017";
		
		String monthYear = LocalizationHelper.getLocalizedMonthYear(month, year, languageTag);
			
		CommonTestStepActions.selectMonthYearPulldown(monthYear);
		
		Thread.sleep(5000);
		
//		FleetUsageLanguage.OpenSettingsPanel();
//		
//		String language = "es-ES";
//		FleetUsageLanguage.selectLanguage(language);
//		
//		FleetUsageLanguage.CloseSettingsPanel();
//		Thread.sleep(3000);
		
		TopTen.verifyLocalizationHierarchyTopTenWithTags(languageTag);
		
		
		
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
