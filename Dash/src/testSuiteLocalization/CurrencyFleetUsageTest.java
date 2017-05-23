package testSuiteLocalization;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.Currencies;
import localization.FleetUsageCurrency;
import localization.FleetUsageLanguage;

public class CurrencyFleetUsageTest extends BaseClass {

	@BeforeClass
	public static void setUp() throws Exception
	{
		setUpDriver();
		MainLogin();
 		
	}
	
	
	@Test
	public static void CurrencyFleetUsage() throws Exception {
		

		CommonTestStepActions.selectMonthYearPulldown("October 2016");
		Currencies.setUpCurrencies();
		
		Thread.sleep(2000);
		
		FleetUsageLanguage.OpenSettingsPanel();
		
		String currency = "EUR";
		FleetUsageCurrency.selectCurrency(currency);
		
		FleetUsageLanguage.CloseSettingsPanel();
		Thread.sleep(2000);	
				
		FleetUsageCurrency.verifyCurrencyKpis(currency);
		
	}
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Test for Currencies finished. Select OK to close browser.");
		driver.close();
		driver.quit();
	}


}
