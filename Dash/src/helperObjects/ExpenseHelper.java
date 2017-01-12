package helperObjects;

import java.text.ParseException;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;

public class ExpenseHelper extends BaseClass
{
	public static String tmpStr = "";
	public static String errMessage = "";	
	public static String desiredMonth = "June 2016";
	public static String impericalDesiredMonth = ""; // this is found by going through the months and finding the month(s) with the most amount of vendors showing in the expense control. 
	public static String chartId = "";
	public static String otherText = "Other";
	public static String tempLocator = "";
	public static String tempUrl = "";
	
	public static List<WebElement> webElementListLegends;	
	public static List<WebElement> webEleListBarGraphHoverValues;
	public static List<String> legendsListTotalExpense = new ArrayList<String>();	

	// these are for VerifyMonths method 
	public static List<String> expectedYearMonthList = new ArrayList<String>();
	public static List<String> tempStringList = new ArrayList<String>();
	public static List<WebElement> webEleListMonthYearActual;	
	public static List<String> actualYearMonthList = new ArrayList<String>();
	public static List<WebElement> ctryList; 
	public static List<WebElement> vndrList;	
	public static String errNeedToCallInitializeMethod = "Failed in method call. You first need to call SetupCountryAndVendorData() to use this method.";
	public static int maxNumberOfLegends = 5; // max number of legends that are not labeled 'other'.
	public static int numOfLegendsInExpenseSpendCategory = 8; // number of legends in the spend category
	public static int maxNumberOfMonths = 13; // max number of months in graphs listed by months.
	
	public static List<WebElement> expenseControlSlicesElemntsList; // this holds web elements containing the slices in the 'total expense' control. 	
	public static HashMap<String, String> expenseControlHMap; // this holds a hash map list that holds the vendor/value for each visible slice in the 'total expense' control. 

	
	// this is for  distinguishing a control type in the expenses page. 
	public static enum controlType
	{
		totalExpense,
		totalExpenseSpendCatergory,
		expenseTrending,
		costPerServiceNumber,
		countOfServiceNumbers,
	}

	// this is for testing legend clicks and verifying if legends are enabled or disabled. 
	public static enum enableDisableActionsType
	{
		enabling,
		disabling,
	}
	
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// The xpaths below help in locating legend info in the controls.     
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// this is the same for all three controls that have a bar chart for each month. this gets the list of months above the legends 
	public static String partialXpathToMonthListInControls = "/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels ']/*/*";
	
	// this is the same for all three controls that have a bar chart for each month. this gets the list of legends. 
	// also can be used for 'expense control' and 'expense by category control'.
	public static String partialXpathToLegendsListInControls = "/*/*[@class='highcharts-legend']/*/*/*";
	
	// this is the same for all three controls that have a bar chart for each month. this gets the bar graphs.
	public static String partialXpathToBarGrapghControls = "/*/*[@class='highcharts-series-group']/*[contains(@class,'highcharts-series highcharts')]";
	
	// this is for getting the vendors in 'Total Expense by Vendor/Country and Spend Category'.
	public static String partialXpathForLegendsInTotalSpendCategory = "/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels ']/*/*";
	
	// this is for getting the vendors in 'Total Expense by Vendor/Country and Spend Category'.
	public static String partialXpathForLegendsInTotalSpendCategoryCategories = "/*/*[@class='highcharts-legend']/*/*/*";

	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// The xpaths below help in getting hover values in the controls and selecting elements in controls (some controls).    
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// this is for getting the hover info in expense control 
	public static String partialXpathForHoverInfo = "/*/*[contains(@class,'highcharts-tooltip')]/*/*";	
	
	// this is for selecting slices in the expense control and expense control spend category.
	public static String partialXpathForSliceSelections = "/*/*[@class='highcharts-series-group']/*/*";	
	
	// setup - read comments below.
	public static void SetupExpenseControSliceTesting()
	{
		if(expenseControlSlicesElemntsList != null)
		{
			expenseControlSlicesElemntsList.clear();
		}
		expenseControlSlicesElemntsList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathForSliceSelections)); // web list holding slices in 'total expense' control. 

		if(expenseControlHMap == null)
		{
			expenseControlHMap = new HashMap<String, String>(); // hash map list that holds the vendor/value for each visible slice in the 'total expense' control.			
		}
	}
	
	public static void SetupForCountryViewPageLoad()
	{
		List<String> totalExpenseLegendsList = GetTotalExpenseLegends();
		
		if(tempStringList != null)
		{
			tempStringList.clear();
		}
		
		if(totalExpenseLegendsList != null)
		{
			totalExpenseLegendsList.clear();
		}
		
		// get the vendor values while in vendor view.
		totalExpenseLegendsList = ExpenseHelper.GetTotalExpenseLegends();

		// put the corresponding country values in tempList.
		for(String str : totalExpenseLegendsList)
		{
			if(!str.equals("Other"))
			{
				tempStringList.add(ExpenseHelper.GetCountryForVendor(str));				
			}
		}
	}
	
	public static void WaitForCountryPageLoad() throws Exception 
	{

		chartId = UsageHelper.getChartId(4);
		
		if(webElementListLegends != null)
		{
			webElementListLegends.clear();
		}
		
		long currentTime= System.currentTimeMillis();
		long endTime = currentTime+10000;
		int x = 0;
		while(System.currentTimeMillis() < endTime) 
		{
			Thread.sleep(1000);
			// get list that
			webElementListLegends = driver.findElements(By.xpath("//div[@id='" + chartId + "']" + partialXpathToLegendsListInControls));  
			if(webElementListLegends != null)
			{
				if(tempStringList.contains(webElementListLegends.get(0).getText()))
				{
					break;
				}
			}
		}
	}
	
	// this uses the 'expenseControlSlicesElemntsList' list  (global in this object) that contains the slices in the 'total expense' control.
	// for each slice that does not have the DOM value 'visibility=hidden', store the vendor and numeric value onto a hash map list.
	public static void GetvaluesInExpenseControlAndStore() throws Exception
	{
		for(WebElement ele : expenseControlSlicesElemntsList) // go through the list of control slices.
		{
			if(ele.getAttribute("visibility") != null) //  if the current slice has a 'visibility' attribute, this means the control is not shown in the control. ignore it.   
			{
				Assert.assertTrue(ele.getAttribute("visibility").equals("hidden"), ""); // make sure the attribute value 'visibility' has value 'hidden'.
			}
			else // the control slice is clickable. select it and put the vendor/value onto the hash map. 
			{
				ele.click();
				Thread.sleep(1000);
				expenseControlHMap.put(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-size')]")).getText(),
									   driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-weight')]")).getText());
				//ShowText(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-size')]")).getText());
				//ShowText(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'ont-weight')]")).getText());				
			}
		}
	}
	
	// this is for testing one vendor at a time. 
	public static String GetvaluesInExpenseControlAndStoreOneSlice() throws Exception
	{
		int x = 1;
		
		for(WebElement ele : expenseControlSlicesElemntsList) // go through the list of control slices.
		{
			ExpenseHelper.SetChartId(0);
			
			if(ele.getAttribute("visibility") != null) //  if the current slice has a 'visibility' attribute, this means the control is not shown in the control. ignore it.   
			{
				Assert.assertTrue(ele.getAttribute("visibility").equals("hidden"), ""); // make sure the attribute value 'visibility' has value 'hidden'.
			}
			else // the control slice is clickable. select it and put the vendor/value onto the hash map. 
			{
				if(x > 1)
				{
					break;
				}
				//ShowText("pre click");
				ele.click();
				Thread.sleep(1000);
				x++;
				//ShowText(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-size')]")).getText());
				//ShowText(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'ont-weight')]")).getText());
				
				return driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-size')]")).getText();
			}
		}
		return "";
	}
	
	// parameters: 
	// expenseLegendsList - this is need to be able to select a legend by its text name.
	// desiredLegendname - this is the text name of the legend to select.
	// expectedVendorNamesList - this has the vendor names that are expected to be shown in the 'total expense' control slices after a legend is clicked.
	// * if an empty string is passed in this is telling this method to not click a legend. 
	// * whether an empty string is passed in or not, calling method 'GetvaluesInExpenseControlAndStore' will load a hash map with the actual values in the expense control.
	// * method 'VerifyCorrectControlSlices' will verify the vendor names in the hash map and the vendor names in input parameter 'expectedVendorNamesList' are equal.
	public static void SelectLegendByTextAndDoVerification(List<WebElement> expenseLegendsList, String desiredLegendname, List<String> expectedVendorNamesList) throws Exception
	{
		expenseControlHMap.clear(); // this map should be empty at start.
		
		if(desiredLegendname.equals("")) // if 'desiredLegendname' is "", don't select any legends. this is how all legends selected is verified.
		{
			GetvaluesInExpenseControlAndStore(); // store vendor and value for each enabled slice. 
			VerifyCorrectControlSlices(expectedVendorNamesList); // verify actual/expected are equal. 
		}
		else
		{
			for(WebElement ele : expenseLegendsList)
			{
				if(ele.getText().equals(desiredLegendname))
				{
						ele.click(); // select a slice legend.
						Thread.sleep(1000); // wait after the click 
						GetvaluesInExpenseControlAndStore(); // store vendor and value for each enabled slice into hash map.
						VerifyCorrectControlSlices(expectedVendorNamesList); // verify actual/expected are equal.
				}
			}			
		}
	}
	
	public static void VerifyCorrectControlSlices(List<String> expectedVendorNamesList)
	{
		tempStringList.clear();
		
		for (String key : expenseControlHMap.keySet()) 
		{	
			tempStringList.add(key);
		}

		java.util.Collections.sort(expectedVendorNamesList);
		java.util.Collections.sort(tempStringList);
		
		//ShowText("EXPECT");	ShowListOfStrings(expectedVendorNamesList); // DEBUG
		//ShowText("ACTUAL");	ShowListOfStrings(tempStringList); // DEBUG
		
		Assert.assertEquals(tempStringList, expectedVendorNamesList, "Failed verifying actual versus expected in ExpenseHelpser.VerifyCorrectControlSlices.");
	}
	
	
// 	.//*[@id='highcharts-ejp1hdz-408']/*/*[@class='highcharts-legend']
	
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
		webElementListLegends = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls));		
		
		// for(WebElement ele : webElementListLegands ){System.out.println(ele.getText());} // DEBUG
		
		if(legendsListTotalExpense != null)
		{
			legendsListTotalExpense.removeAll(legendsListTotalExpense);
		}
		
		// store legend names into legend list.
		for(WebElement ele : webElementListLegends )
		{
			legendsListTotalExpense.add(ele.getText());

		} 
		
		webElementListLegends.clear();
		
		return legendsListTotalExpense;
	}
	
	// this gets the legends in the expense control . it is a list of web elements.
	public static List<WebElement> GetTotalExpenseCatergoryLegends()
	{
		chartId = UsageHelper.getChartId(1); // get current chart Id for expense control.

		if(webElementListLegends != null)
		{
			webElementListLegends.clear();
		}
		
		// store all legend names into web element list.
		webElementListLegends = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls));		
		
		return webElementListLegends;
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

	
	
	// (//h3[@class='tdb-h3']/span[text()='Country'])[4]
	
	public static void SetWaitDefault()
	{
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS); 		
	}
	
	// this is used when waiting for element not present.
	public static void SetWaitShort()
	{
		driver.manage().timeouts().implicitlyWait(300, TimeUnit.MILLISECONDS); 		
	}
	
	// this is used when waiting for element not present.
	public static void SetWaitTiny()
	{
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.MILLISECONDS); 		
	}
	
	
	// this sets the global chartId (global to this class).
	public static void SetChartId(int id)
	{
		chartId = UsageHelper.getChartId(id);
	}
	
	// this sets the global chartId (global to this class).
	public static void SetTempLocator(String tmpLocator)
	{
		tempLocator = tmpLocator;
	}
	

	// this verifies that each control is not visible by looking for the first legend in each control being not visible.
	public static void VerifyControlsNotPresent() throws Exception
	{
		ExpenseHelper.SetWaitShort(); // override the default because of wait for no element.
		
		chartId =  UsageHelper.getChartId(1); // total expense vendor spend.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForLegendsInTotalSpendCategory + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));

		chartId =  UsageHelper.getChartId(2); // expense trending. 
		ShowText(chartId);
		tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));

		chartId =  UsageHelper.getChartId(3); // cost per service number. 
		// ShowText(chartId);
		tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
		
		chartId =  UsageHelper.getChartId(4); // cost per service number.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		
		ExpenseHelper.SetWaitDefault(); // back to default.
	}	
	
	// this verifies one control is not visible by looking for the first legend in each control being not visible.
	// NOTE --- needs finished.
	public static void VerifyOneControlNotPresent(controlType cntrlType) throws Exception
	{
		ExpenseHelper.SetWaitShort(); // override the default because of wait for no element.

		switch(cntrlType)
		{
			case totalExpenseSpendCatergory:
			{
				chartId =  UsageHelper.getChartId(1);  
				ShowText(chartId);
				// tempUrl = "#" + chartId + ">svg>.highcharts-yaxis-labels>text:nth-of-type(1)"; // css no work		
				tempUrl = "(//div[@id='" +  chartId + "']" +  "/*/*[@class='highcharts-axis-labels highcharts-yaxis-labels '])[1]";
				Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
				// WaitForElementVisible(By.xpath(tempUrl), MediumTimeout); // DEBUG 
				break;
			}
			case expenseTrending:
			{
				chartId =  UsageHelper.getChartId(2);  
				ShowText(chartId);
				tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
				Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
				break;
			}
			case costPerServiceNumber:
			{
				chartId =  UsageHelper.getChartId(3);  
				// ShowText(chartId);
				tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
				Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
				break;
			}
		
		
		}
		
		/*
		chartId =  UsageHelper.getChartId(0); // total expenses. 
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
		
		chartId =  UsageHelper.getChartId(1); // total expense vendor spend.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForLegendsInTotalSpendCategory + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		
		chartId =  UsageHelper.getChartId(2); // expense trending.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		
		chartId =  UsageHelper.getChartId(3); // cost per service number.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		
		chartId =  UsageHelper.getChartId(4); // cost per service number.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		*/
		ExpenseHelper.SetWaitDefault(); // back to default.
	}	
	
	
	// this is meant to be run once to get the first month (using the expense control) that has the max number of vendors and the other legend. 
	// if that combination is not found it will find the moth with the most amount of legends in the expense control. 
	public static void FindMonthWithMostVendors() throws Exception
	{
		chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.
		int largestNumOfVendors = 0;
		int currentNumOfVendors = 0; 
		
		String locator = "//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls;
		
		CommonTestStepActions.initializeMonthSelector();

		// loop through selecting each month to find the month to find the combination described above.
		for(WebElement ele : CommonTestStepActions.webListPulldown) 
		{
			CommonTestStepActions.selectMonthYearPulldown(ele.getText()); 
			
			// wait for the selected month to show up in the top left of page and the expense control. 
			WaitForElementVisible(By.xpath("//h1[text()='" +   ele.getText()  + "']"), MediumTimeout);
			WaitForElementVisible(By.xpath("//h2[text()='" +   ele.getText()  + "']"), MediumTimeout);

			// get the number of legends shown in the total expense control with current month.
			currentNumOfVendors = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls)).size();
			
			if(currentNumOfVendors > largestNumOfVendors) // if number legends shown for this month is greater than others, store the number of legends
			{
				largestNumOfVendors = currentNumOfVendors;
				
				if(largestNumOfVendors == ExpenseHelper.maxNumberOfLegends + 1) // this is the max number of legends that can be shown. show and leave.
				{
					impericalDesiredMonth = ele.getText();
					ShowText("Desired month is " + impericalDesiredMonth);
					return;
				}
				else
				{
					impericalDesiredMonth = ele.getText();					
				}
			}
		}
		
		ShowText("Desired month is " + impericalDesiredMonth);
	}
	
	public static void VerifySelectUnselect(enableDisableActionsType actionType) throws Exception 
	{
		int cntr = 1; // used to keep track of how many legends should be disabled.
		
		// the wait to verify a legend, in 'VerifyLegendListStates' method, that doesn't contain "[contains(@class,'item-hidden')]" needs to be short or things get slow.  
		// i did force an error in the case where "[contains(@class,'item-hidden')]" is present and is not supposed to be present, and a fail was found.
		// this sets up the default timeout for method 'Assert.assertFalse' in method 'VerifyLegendListStates'. 
		SetWaitTiny();  
		
		// chartId = UsageHelper.getChartId(1);// get chart id for total expense
		
		webElementListLegends = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + tempLocator)); // store all the web elements for the legends.   
		
		// go through the legends and select one at time. verify all states (enabled/disabled) of the legends are correct after each legend is selected.
		for(WebElement ele : webElementListLegends) 
		{
			driver.findElement(By.xpath("(//div[@id='" +  chartId + "']" + tempLocator + ")[" + cntr +  "]")).click(); // select current web element's legend. 
			VerifyLegendListStates(webElementListLegends, chartId, cntr, tempLocator, actionType); // this does the verification of all legend states.
			cntr++;
		}
		
		webElementListLegends.clear();
		
		SetWaitDefault();

		// DEBUG - this shows not able to use isEnabled() to tell if a legend is disabled. isEnabled() always shows true here.
		//System.out.println(driver.findElement(By.xpath("(//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls + ")[" + 1 +  "]")).isEnabled());
		//System.out.println(driver.findElement(By.xpath("(//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls + ")[" + 1 +  "]/*")).isEnabled());			
		//System.out.println(driver.findElement(By.xpath("(//div[@id='" +  chartId + "']" + partialXpathToLegendsListInControls + ")[" + 1 +  "]/*/*")).isEnabled());
	}

	
	// this verifies the elements that are to be enabled and the elements that are to be disabled. 
	// 
	// Inputs:
	// webElementList - this is a list of the legends to test for enabled/disabled
	// chartId - chart id of control being tested.
	// numSelected - number of legends that should be disabled
	// legendXpath - xpath the control's legend.
	// enableDisable - this (enum) tells the method whether the legends are being disabled or enabled.
	public static void VerifyLegendListStates(List<WebElement> webElementList, String chartId, int numSelected, String legendXpath, enableDisableActionsType enableDisable) throws Exception 
	{
		for(int x = 1; x <= webElementList.size(); x++) // go through all the legends and verify if they are in correct state (enabled/disabled).
		{
			if(x <= numSelected) 
			{
				errMessage = "Failed to verify this legend is enabled.";
				if(enableDisable ==  enableDisableActionsType.disabling) // this element is expected to be disabled. it should have the "[contains(@class,'item-hidden')]" in its xpath.  
				{
					Assert.assertTrue(WaitForElementPresentNoThrow(By.xpath("(//div[@id='" +  chartId + "']" + legendXpath + ")[" + x + "][contains(@class,'item-hidden')]"), ShortTimeout), errMessage);
				}
				else // this element is not expected to be enabled. there should be no "[contains(@class,'item-hidden')]" in its xpath. 
				{
					Assert.assertFalse(WaitForElementPresentNoThrow(By.xpath("(//div[@id='" +  chartId + "']" + legendXpath + ")[" + x + "][contains(@class,'item-hidden')]"), 0), errMessage);					
				}

			}
			else 
			{
				errMessage = "Failed to verify this legend is disabled.";
				if(enableDisable ==  enableDisableActionsType.disabling) // this element is expected to be disabled. it should have the "[contains(@class,'item-hidden')]" in its xpath.
				{
					Assert.assertFalse(WaitForElementPresentNoThrow(By.xpath("(//div[@id='" +  chartId + "']" + legendXpath + ")[" + x + "][contains(@class,'item-hidden')]"), 0), errMessage);
				}
				else // this element is not expected to be enabled. there should be no "[contains(@class,'item-hidden')]" in its xpath.
				{
					Assert.assertTrue(WaitForElementPresentNoThrow(By.xpath("(//div[@id='" +  chartId + "']" + legendXpath + ")[" + x + "][contains(@class,'item-hidden')]"), ShortTimeout), errMessage);					
				}
			}
		}
	}

	// this is unique to expense page. desired month is the month setting that has the most amount of legends for actions testing. 
	public static void VerifyMonthPullDownFormatExpense()
	{
		Assert.assertEquals(new Select(driver.findElement(By.cssSelector(CommonTestStepActions.pullDownCss))).getFirstSelectedOption().getText(), ExpenseHelper.desiredMonth, 
				                       "Failed test for verifying correct month in month pulldown in CommonTestStepActions.VerifyMonthPullDownFormatExpense.");
	}

	// this receives expected and actual lists and verifies. 
	public static void VerifyToolTipTwo(List<String> expectList, String expectedMonth) throws Exception // bladdxx
	{
		List<String> actualList = new ArrayList<String>();
		List<String> copy = new ArrayList<String>();
		
		errMessage = "Failure in TotalExpensesTrendVendorActions.VerifyToolTipTwo. Failed to verify correct ";
		
		
		// make a copy of the expected list. sorting done further below made failures when expectList was sorted.
		copy.addAll(expectList);  

		// get web list that holds the DOM section that holds the hover values.
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));

		Assert.assertEquals(webEleListBarGraphHoverValues.get(0).getText(), expectedMonth, errMessage + "month value in hover text."); // verify month at top of hover.
		
		for(WebElement ele : webEleListBarGraphHoverValues)
		{
			if(ele.getText().contains(":"))
			{
				actualList.add(ele.getText().replace(":", ""));
			}
		}
		
		// sort in case orders are different.
		Collections.sort(actualList);
		Collections.sort(copy);
		
		Assert.assertEquals(actualList, copy, errMessage + "vendor.");
	}
	
	
	
	// //////////////////////////////////////////////////////////////////////	
	// 								helpers
	// //////////////////////////////////////////////////////////////////////
	
	public static void ClearAllContainersForVerifyMonths()
	{

		ExpenseHelper.SetWaitShort();
		
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


