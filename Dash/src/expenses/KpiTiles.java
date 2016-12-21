package expenses;

import org.openqa.selenium.By;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.Country;

public class KpiTiles extends BaseClass 
{

	public static String xpathTotalExpenseKpi = "(//div[@class='tdb-kpi__statistic'])[1]";
	public static String xpathCountOfServiceNumbersKpi = "(//div[@class='tdb-kpi__statistic'])[2]";
	public static String xpathCostPerServiceNumberKpi = "(//div[@class='tdb-kpi__statistic'])[3]";

	// these are used in VerifyAddingEachVendor().
	public static double previous = 0; 
	public static double latest = 0;

	
	public static enum AscendDescend
	{
		ascending,
		descending,		
	}
	
	// this verifies each KPI has a value of zero, verifies the 'Total Expense and 'Cost per Service Number' KPIs have leading dollar signs, 
	// and the 'Count of Service Numbers' KPI doesn't have a leading dollar sign.
	public static void VerifyInitialValues()
	{
		Assert.assertTrue(driver.findElement(By.xpath(xpathTotalExpenseKpi)).getText().contains("$"));
		Assert.assertTrue(driver.findElement(By.xpath(xpathTotalExpenseKpi)).getText().contains("0"));

		Assert.assertFalse(driver.findElement(By.xpath(xpathCountOfServiceNumbersKpi)).getText().contains("$"));
		Assert.assertTrue(driver.findElement(By.xpath(xpathCountOfServiceNumbersKpi)).getText().contains("0"));

		Assert.assertTrue(driver.findElement(By.xpath(xpathCostPerServiceNumberKpi)).getText().contains("$"));
		Assert.assertTrue(driver.findElement(By.xpath(xpathCostPerServiceNumberKpi)).getText().contains("0"));
		
		// ShowText(driver.findElement(By.xpath(xpathTotalExpenseKpi)).getText()); // DEBUG to see a KPI value.
	}
	
	// this takes the xpath to the KPI to verify.
	// * select each month, one at a time.
	// * after each month is selected, verify the numeric value shown in the KPI of interest is >=  to the value before the month is selected. 
	public static void VerifyAddingEachVendor(String xpath, AscendDescend ascendDescend) throws InterruptedException
	{
		for(Country ctr : countryList)
		{
			for(String str : ctr.vendorList)
			{
				CommonTestStepActions.selectOneVendor(str); // select vendor
				Thread.sleep(1500); // have to do a hard coded wait. there is no element on the dash board page that consistently changes each time a new vendor is added.  
				latest = GetNumericValue(driver.findElement(By.xpath(xpath)).getText()); // get the latest
				
				//System.out.println(latest);
				//System.out.println(previous);
				//ShowText("===========");
				
				ShowLatestPrevious(latest, previous); // DEBUG

				
				switch (ascendDescend)
				{
					case ascending:
					{
						Assert.assertTrue(latest >= previous); // verify latest is > previous.
						break;
					}

					case descending:
					{
						Assert.assertTrue(latest <= previous); // verify latest is < previous.
						break;
					}
				}
				
				previous = latest;
			}
		}
	}
	
	
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 												Helpers
	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public static double GetNumericValue(String stringVal)
	{
		// remove dollar sign (may not always be there)
		stringVal = stringVal.replace("$", "");
		
		return   GetMultiplier(stringVal)   *  Double.parseDouble(stringVal.replace("K","").replace("M","").replace("G",""));
	}
	
	public static double GetMultiplier(String kpiVal)
	{
		double multiplier = 0;
		
		if(kpiVal.contains("K"))
		{
			multiplier = java.lang.Math.pow(10,3);
			return multiplier;
		}
		else if(kpiVal.contains("M"))
		{
			multiplier = java.lang.Math.pow(10,6);
			return multiplier;
		}
		else if(kpiVal.contains("G"))
		{
			multiplier = java.lang.Math.pow(10,9);
			return multiplier;
		}
		
		return 1;
	}
	
	public static void ShowLatestPrevious(double latest, double previous)
	{
		ShowText("==================");
		System.out.println("Latest: " + latest);
		System.out.println("Previous: " + previous);
	}
	
}
