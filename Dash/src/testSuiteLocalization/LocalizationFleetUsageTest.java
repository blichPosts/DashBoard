package testSuiteLocalization;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.LocalizationHelper;
import helperObjects.UsageHelper;
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

		String languageTag = LocalizationHelper.languageTag;
		
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
		
		try {
			
			// Domestic chart
			UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
			
			FleetUsageLanguage.verifyLocalizationOnUsageTrendingTooltipsWithTags(UsageHelper.usageTrendingDomesticChart, languageTag);
			Thread.sleep(2000);
			
			UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
			
			FleetUsageLanguage.verifyLocalizationOnUsageTrendingTooltipsWithTags(UsageHelper.usageTrendingDomesticChart, languageTag);
			Thread.sleep(2000);
			
			UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
			
			FleetUsageLanguage.verifyLocalizationOnUsageTrendingTooltipsWithTags(UsageHelper.usageTrendingDomesticChart, languageTag);
			Thread.sleep(2000);
			
			// Roaming chart
			UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryVoice);
			
			FleetUsageLanguage.verifyLocalizationOnUsageTrendingTooltipsWithTags(UsageHelper.usageTrendingRoamingChart, languageTag);
			Thread.sleep(2000);	
			
			UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryData);
			
			FleetUsageLanguage.verifyLocalizationOnUsageTrendingTooltipsWithTags(UsageHelper.usageTrendingRoamingChart, languageTag);
			Thread.sleep(2000);	
			
			UsageHelper.selectCategory(UsageHelper.usageTrendingSection, UsageHelper.categoryMessages);
			
			FleetUsageLanguage.verifyLocalizationOnUsageTrendingTooltipsWithTags(UsageHelper.usageTrendingRoamingChart, languageTag);
			Thread.sleep(2000);
			
			
		} catch (NullPointerException e) {
			
			System.out.println("chart not found or value is null...");
			
		}
		
		
		
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
