package testSuiteLocalization;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.LocalizationHelper;
import localization.FleetUsageLanguage;

public class LocalizationFleetUsageTest extends BaseClass {

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	@Test
	public static void LocalizationFleetUsage() throws Exception {

		String languageTag = "[MDE]";  // <-- For German it used to be [de]
//		String languageTag = "[ja]";
//		String languageTag = "[es]";
		
		String month = "September";
		String year = "2016";
		
		String monthYear = LocalizationHelper.getLocalizedMonthYear(month, year, languageTag);
			
		CommonTestStepActions.selectMonthYearPulldown(monthYear);
		
		CommonTestStepActions.WaitForViewUsageSelector();
		
//		FleetUsageLanguage.OpenSettingsPanel();
//		
//		String language = "es-ES";
//		FleetUsageLanguage.selectLanguage(language);
//		
//		FleetUsageLanguage.CloseSettingsPanel();
//		Thread.sleep(3000);
		
		FleetUsageLanguage.verifyMainTitle(languageTag, monthYear);
		
		FleetUsageLanguage.verifyLocalizationOnUsageKpisWithTags(languageTag);
		
		FleetUsageLanguage.verifyLocalizationOnTotalUsageWithTags(languageTag, monthYear);
		
		FleetUsageLanguage.verifyLocalizationOnUsageTrendingWithTags(languageTag);
		
//		FleetUsageLanguage.verifyLocalizationOnUsageKpisSpanish(languageTag);
//		FleetUsageLanguage.verifyLocalizationOnTotalUsageSpanish(languageTag, monthYear);
		
		
		
		
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
