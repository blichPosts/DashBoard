package helperObjects;

import java.text.ParseException;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;

public class ExpenseHelper extends BaseClass
{
	public static String tmpStr = "";
	public static String errMessage = "";	
	public static String desiredMonth = "June 2016";	
	public static String chartId = "";
	public static String otherText = "Other";
	public static String tempLocator = "";
	
	public static List<WebElement> webElementListLegands;	
	public static List<String> legendsListTotalExpense = new ArrayList<String>();	

	// these are for VerifyMonths method 
	public static List<String> expectedYearMonthList = new ArrayList<String>();
	public static List<WebElement> webEleListMonthYearActual;	
	public static List<String> actualYearMonthList = new ArrayList<String>();
	public static List<WebElement> ctryList; 
	public static List<WebElement> vndrList;	
	public static String errNeedToCallInitializeMethod = "Failed in method call. You first need to call SetupCountryAndVendorData() to use this method.";
	
	public static int maxNumberOfLegends = 5; // max number of legends that are not labeled 'other'.
	
	// this is for  distinguishing a control type in the expenses page. 
	public static enum controlType
	{
		totalExpense,
		totalExpenseSpendCatergory,
		expenseTrending,
		costPerServiceNumber,
		countOfServiceNumbers,
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// The three xpaths below are for locating things in the controls that have a series of bar graphs with months and legends below.    
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// this is the same for all three controls that have a bar chart for each month. this gets the list of months above the legends 
	public static String partialXpathToMonthListInControls = "/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels ']/*/*";
	
	// this is the same for all three controls that have a bar chart for each month. this gets the list of legends. also can be used for 'expense control'
	public static String partialXpathToLegendsListInControls = "/*/*[@class='highcharts-legend']/*/*/*";
	
	// this is the same for all three controls that have a bar chart for each month. this gets the bar graphs.
	public static String partialXpathToBarGrapghControls = "/*/*[@class='highcharts-series-group']/*[contains(@class,'highcharts-series highcharts')]";
	
	// this is for getting the legends in 'Total Expense by Vendor/Country and Spend Category'.
	public static String partialXpathForLegendsInTotalSpendCategory = "/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels ']/*/*";

	
	public static void VerifyThreeComponents()
	{
		errMessage = "Failure in ExpenseHelper.VerifyThreeComponents";
		
		Assert.assertEquals(driver.findElements(By.cssSelector(".tdb-kpi")).size(), 3, errMessage);
		
		List<WebElement> webList = driver.findElements(By.cssSelector(".tdb-kpi > h3")); // get the names of the three KPI components. 

		Assert.assertTrue(webList.size() == 3, errMessage);
		
		// verify the three components.
		for(int x = 0; x < ExpenseKpiNames.length; x++)
		{
			Assert.assertEquals(ExpenseKpiNames[x], webList.get(x).getText(), errMessage);
		}
	}
	
	public static void VerifyTotalExpenseCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyTotalExpenseCost";
		
		//The top says 'Total Expense'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > h3")).getText(), ExpenseKpiNames[0], errMessage);
		// css=.tdb-kpi:nth-of-type(1) > h3 // this type of indexing works.

		VerifyExpenseTotalCost(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)")).getText(), errMessage);
	}
	
	public static void VerifyCountOfServiceNumbersCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyCountOfServiceNumbersAndTitle";
		
		//The top says 'Count of Service Numbers'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > h3")).getText(), ExpenseKpiNames[1], errMessage);

		VerifyCountServiceNumbers(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > div> div:nth-of-type(1)")).getText(), errMessage);
	}	
	
	public static void VerifyCostPerServiceNumberCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyCostPerServiceNumberCostAndTitle";
		
		//The top says 'Count of Service Numbers'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(3) > h3")).getText(), ExpenseKpiNames[2], errMessage);

		VerifyCostPerServiceNumber(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(3) > div> div:nth-of-type(1)")).getText(), errMessage);
	}		
	
	public static void TotalExpensedVerifyTrend()
	{
		errMessage = "Failure in ExpenseHelper.TotalExpensedVerifyTrend";
		
		VerifyTrendDetails(driver.findElement(By.xpath("((//div[@class='tdb-kpi__trend'])[1]/span)[2]")).getText(), errMessage);
	}
	
	public static void CountServiceNumbersVerifyTrend()
	{
		errMessage = "Failure in ExpenseHelper.CountServiceNumbersVerifyTrend";
		
		VerifyTrendDetails(driver.findElement(By.xpath("((//div[@class='tdb-kpi__trend'])[2]/span)[2]")).getText(), errMessage);
	}	
	
	public static void CostPerServiceNumberVerifyTrend()
	{
		errMessage = "Failure in ExpenseHelper.CostPerServiceNumberVerifyTrend";
		
		VerifyTrendDetails(driver.findElement(By.xpath("((//div[@class='tdb-kpi__trend'])[3]/span)[2]")).getText(), errMessage);
	}		
	
	public static void VerifyRollingAverageTotalExpense()
	{
		errMessage = "Failure in ExpenseHelper.VerifyRollingAverage";
		
		VerifyRollingAverageTotalExpense(errMessage, "(//h4[@class='tdb-h4'])[1]/../..");
	}
	
	public static void VerifyRollingAverageCountServiceNumbers()
	{
		errMessage = "Failure in ExpenseHelper.VerifyRollingAverageCountServiceNumbers";
		
		VerifyRollingAverageTotalExpense(errMessage, "(//h4[@class='tdb-h4'])[2]/../.."); 
	}
	
	public static void VerifyRollingAverageCostPerServiceNumber()
	{
		errMessage = "Failure in ExpenseHelper.VerifyRollingAverageCountServiceNumbers";
		
		VerifyRollingAverageTotalExpense(errMessage, "(//h4[@class='tdb-h4'])[3]/../.."); 
	}
	
	// this gets the legends in the expense control. this is here because all the legends in the other controls are supposed to
	// match the total expense control. this is called by each control when the control's legends are verified to match the 
	// total expense control's legends. 
	public static List<String> GetTotalExpenseLegends()
	{
		chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.

		// store all legend names into web element list.
		webElementListLegands = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls));		
		
		// for(WebElement ele : webElementListLegands ){System.out.println(ele.getText());} // DEBUG
		
		if(legendsListTotalExpense != null)
		{
			legendsListTotalExpense.removeAll(legendsListTotalExpense);
		}
		
		// store legend names into legend list.
		for(WebElement ele : webElementListLegands )
		{
			legendsListTotalExpense.add(ele.getText());

		} 
		
		return legendsListTotalExpense;
	}
	
	// this is for the controls that have a bar graph shown for each month. this verifies the months match the months in the pull down.  
	public static void VerifyMonths(String xpathText) throws ParseException 
	{
		ClearAllContainersForVerifyMonths();
		
		// get expected month(int)/years pulldown - format is MM-YYYY. 
		expectedYearMonthList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		// get the months shown in the control in the UI.
		webEleListMonthYearActual = driver.findElements(By.xpath(xpathText));
		
		// put actual web list values into a list of strings.
		for(WebElement ele : webEleListMonthYearActual)
		{
			actualYearMonthList.add(ele.getText());
			//System.out.println(ele.getText()); // DEBUG.			
		}

		// the actual list is in descending order. the expected list is in ascending order. using sort on actual list didn't correct order, so use x increment and y decrement.  
		for(int x = 0, y = expectedYearMonthList.size() - 1; x < expectedYearMonthList.size(); x++, y--)
		{
			Assert.assertEquals(actualYearMonthList.get(y), expectedYearMonthList.get(x), "Failed check of months in TotalExpensesTrend.VerifyMonths");
		}
	}	

	// this waits for the legends block to be visible in the control type passed in. 
	public static void WaitForControlLegend(controlType control) throws Exception
	{
		switch(control)
		{
			case totalExpense:
			{
				chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.
				WaitForElementVisible(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls), MediumTimeout);
			}
			
			case totalExpenseSpendCatergory:
			{
				chartId = UsageHelper.getChartId(1); // get current chart Id for expense control.
				WaitForElementVisible(By.xpath("//div[@id='" +  chartId + "']" + partialXpathForLegendsInTotalSpendCategory), MediumTimeout);
			}
			
			case expenseTrending:
			{
				chartId = UsageHelper.getChartId(2); // get current chart Id for expense control.
				WaitForElementVisible(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls), MediumTimeout);
			}
			
			case costPerServiceNumber:
			{
				chartId = UsageHelper.getChartId(3); // get current chart Id for expense control.
				WaitForElementVisible(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls), MediumTimeout);
			}
			
			case countOfServiceNumbers:
			{
				chartId = UsageHelper.getChartId(4); // get current chart Id for expense control.
				WaitForElementVisible(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls), MediumTimeout);
			}			
		}
	}
	
	// this is passed a control type and and a list of legend names (strings) that are expected to be found in the control type passed in.
	// for each control type passed in a locator is created that will select the control type passed in. 
	// method 'VerifyActualAndExpectedLists' is called foe the control type passed in.
	public static void VerifyControlLegends(controlType control, List<String> addedList) throws Exception
	{
		switch(control)
		{
			case totalExpense:
			{
				chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.
				
				// build locator and wait section.
				tempLocator = "(//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls + ")[" + addedList.size() +"]" ;
				WaitForElementVisible(By.xpath(tempLocator), MediumTimeout);

				// do verification. 
				// this sends a list of web elements that result from doing driver.findelements with the 'tempLocator' created above. the addedList string is also passed.
				VerifyActualAndExpectedLists(driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls)), addedList); 
				break;
			}
			
			case totalExpenseSpendCatergory:
			{
				
				chartId = UsageHelper.getChartId(1); // get current chart Id for expense spend category control.
				
				// build locator and wait section.
				tempLocator = "(//div[@id='" +  chartId + "']" + partialXpathForLegendsInTotalSpendCategory + ")[" + addedList.size() +"]" ;
				WaitForElementVisible(By.xpath(tempLocator), MediumTimeout);

				// do verification. 
				// this sends a list of web elements that result from doing driver.findelements with the 'tempLocator' created above. the addedList string is also passed.
				VerifyActualAndExpectedLists(driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathForLegendsInTotalSpendCategory)), addedList);				
				break;
			}
			
			case expenseTrending:
			{
				chartId = UsageHelper.getChartId(2); // get current chart Id for expense control.
				
				// build locator and wait section.
				tempLocator = "(//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls + ")[" + addedList.size() +"]" ;
				WaitForElementVisible(By.xpath(tempLocator), MediumTimeout);

				// do verification. 
				// this sends a list of web elements that result from doing driver.findelements with the 'tempLocator' created above. the addedList string is also passed.
				VerifyActualAndExpectedLists(driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls)), addedList);				
				break;
			}
			
			case costPerServiceNumber:
			{
				chartId = UsageHelper.getChartId(3); // get current chart Id for expense control.
				
				// build locator and wait section.
				tempLocator = "(//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls + ")[" + addedList.size() +"]" ;
				WaitForElementVisible(By.xpath(tempLocator), MediumTimeout);

				// do verification. 
				// this sends a list of web elements that result from doing driver.findelements with the 'tempLocator' created above. the addedList string is also passed.
				VerifyActualAndExpectedLists(driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls)), addedList);				
				break;			
			}
			
			case countOfServiceNumbers:
			{
				chartId = UsageHelper.getChartId(4); // get current chart Id for expense control.
				
				// build locator and wait section.
				tempLocator = "(//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls + ")[" + addedList.size() +"]" ;
				WaitForElementVisible(By.xpath(tempLocator), MediumTimeout);

				// do verification. 
				// this sends a list of web elements that result from doing driver.findelements with the 'tempLocator' created above. the addedList string is also passed.
				VerifyActualAndExpectedLists(driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls)), addedList);				
				break;			
			}			
		}
	}

	// this receives a list of web element(s) that are the legend(s) in a control and a list of items that are supposed to be in the list of web elements.   
	public static void VerifyActualAndExpectedLists(List<WebElement> actualList, List<String> addedList) throws Exception
	{
		Assert.assertTrue(actualList.size() == addedList.size(), "Fail in ExpenseHelper.VerifyActualAndExpectedLists. The two list passed in should have the same size."); 
		
		for(WebElement ele : actualList) 
		{
			Assert.assertTrue(addedList.contains(ele.getText()));
		}
	}

	// NOTE: this method is also in CommonTestStepActions - it doesn't work there. 
	// loads each country into a country list. it also adds the vendors to a list in each country.
	public static void SetupCountryAndVendorData()
	{
		countryList.clear(); // make sure it's cleared in case has already been used.  
		
		// get all countries
		ctryList = driver.findElements(By.cssSelector(".tdb-povGroup__label--subhead"));
		
		// get all vendors
		vndrList = driver.findElements(By.cssSelector(".md-checkbox-label"));	
		
		// for(WebElement ele : vndrList){ShowText(ele.getText());} // DEBUG - show country
		
		int y = 0;
		int numberOfVendorsInCountry = 0;

		// got through the countries
		for(int x = 1; x <= ctryList.size(); x++)
		{
			// create country object and add to list.
			Country tmpCountry = new Country(ctryList.get(x-1).getText()); 
			countryList.add(tmpCountry);
			
			// add the vendors to the country just added to the list by getting  the number of vendors for the country and add that number of vendors from the vendor list.
			numberOfVendorsInCountry = driver.findElements(By.xpath("(//div/div[@class='tdb-povGroup__label--subhead'])[" + x + "]/following-sibling ::div/div")).size();
			
			Assert.assertTrue(numberOfVendorsInCountry > 0, "Found a country with no vendors."); // verify at least one vendor is found.
			
			for(int z = 0; z < numberOfVendorsInCountry; z++) // add vendors to current country.
			{
				tmpCountry.AddToVendorList(vndrList.get(y).getText());
				y++;
			}
		}		
	}
	
	// This shows all countries with vendors.
	// NOTE: you MUST run  SetupCountryAndVendorData() before using this.
	public static void ShowCountriesVendors()
	{
		Assert.assertTrue(countryList.size() > 0, errNeedToCallInitializeMethod); // verify main container have been initialized.
		for(Country ctry : countryList)
		{
			ShowText("Country:" + ctry.name);
			for(String str :  ctry.vendorList)
			{
				ShowText("  " + str);
			}
			ShowText("");
		}
	}
	
	// This gets the country associated with a vendor.
	// NOTE: you MUST run  SetupCountryAndVendorData() before using this.
	public static String GetCountryForVendor(String vendor)
	{
		Assert.assertTrue(countryList.size() > 0, errNeedToCallInitializeMethod); // verify main container have been initialized.

		for(Country ctry : countryList)
		{
			if(ctry.vendorList.contains(vendor))
			{
				return ctry.name;
			}
		}

		// if logic path gets here, the country was not found. throw an error.
		Assert.fail("Received bad vendor in method ExpenseHelper.GetCountryForVendor. Vendor name is " + vendor  + ". Stopping program.");
		
		
		return"";
		
	}
	
	// //////////////////////////////////////////////////////////////////////	
	// 								helpers
	// //////////////////////////////////////////////////////////////////////
	
	public static void ClearAllContainersForVerifyMonths()
	{
		if(expectedYearMonthList != null)
		{
			expectedYearMonthList.removeAll(expectedYearMonthList);
		}
		
		if(webEleListMonthYearActual != null)
		{
			webEleListMonthYearActual.removeAll(expectedYearMonthList);
		}		
		
		if(actualYearMonthList != null)
		{
			actualYearMonthList.removeAll(expectedYearMonthList);
		}
	}
	
	public static void VerifyRollingAverageTotalExpense(String errMess, String locator)   
	{
		// String strArray [] = driver.findElement(By.xpath("(//h4[@class='tdb-h4'])[1]/../..")).getText().split("\n");
		
		String strArray [] = driver.findElement(By.xpath(locator)).getText().split("\n");
		
		Assert.assertEquals(strArray[0], rollingAverages, errMess); // rolling averages text
		Assert.assertEquals(strArray[1].split("s")[0] + "s", rollingMonthsThree, ""); // 3 months text 
		Assert.assertEquals(strArray[2].split("s")[0] + "s", rollingMonthsSix, "");	//  6 months text
	
		VerifyInteger(strArray[1].split("months")[1].replace("K","").replace(".","").replace("$", "").trim(), errMess);	// numeric value	
		VerifyInteger(strArray[2].split("months")[1].replace("K","").replace(".","").replace("$", "").trim(), errMess);	// numeric value
	}
	
	public static void VerifyInteger(String tmpStr, String errMessage)
	{
		try
		{
			int num = Integer.parseInt(tmpStr);
		} 
		catch (NumberFormatException e) 
		{
			Assert.fail(errMessage + e.getMessage());
		}
	}
	
	public static void VerifyExpenseTotalCost(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.startsWith("$"), errMessage); // verify contains '$'.
		Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace("$", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}
	
	public static void VerifyCountServiceNumbers(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace("$", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
	
	public static void VerifyCostPerServiceNumber(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.startsWith("$"), errMessage); // verify contains '$'.
		tmpStrIn = tmpStrIn.replace("$","").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
	
	public static void VerifyCountServiceNumbersCost(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace(".", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
	
	public static void VerifyTrendDetails(String tmpStrIn, String errMess)
	{
		// this is up arrow. - can't check
		// tmpStr = driver.findElement(By.cssSelector(".tdb-kpi__trend > span:nth-of-type(1)")).getText();
		// Assert.assertTrue(tmpStr.contains("?"), ""); 
		
		Assert.assertTrue(tmpStrIn.endsWith("%"), ""); // verify contains "%".		
		tmpStrIn = tmpStrIn.replace("%", "").replace(".", ""); // get number with percent.
		VerifyInteger(tmpStrIn, errMessage); // verify all integers after removing $, % , and '.'.
	}
};


