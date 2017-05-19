package helperObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.openqa.selenium.Point;

import Dash.BaseClass;
import expenses.TotalExpensesValues;

public class ExpenseHelper extends BaseClass
{
	public static String tmpStr = "";
	public static String errMessage = "";	
	// public static String desiredMonth = "May 2016";
	public static String desiredMonth = "October 2016";
	public static String impericalDesiredMonth = ""; // this is found by going through the months and finding the month(s) with the most amount of vendors showing in the expense control. 
	public static String chartId = "";
	public static String otherText = "Other";
	public static String tempLocator = "";
	public static String tempUrl = "";
	public static String hierarchyPullDownUrl = ".tdb-space--half--top>select";
	public static hierarchyTileMapTabSelection currentHierarchyCostFilter; // this keeps track of the cost filter selected in the hierarchy page. 
	public static boolean failedtestNgTest = false;
	
	
	public static List<WebElement> webElementListLegends;	
	public static List<WebElement> webEleListBarGraphHoverValues;
	public static List<String> legendsListTotalExpense = new ArrayList<String>();	
	public static List<String> expectedCostFilters = new ArrayList<>();
	
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

	public static expenseFilters currentExpenseFilter; // this is used to indicate which expense filter is being tested. 

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
	
	// this is for indicating which tab is selected above the tile map. 
	public static enum hierarchyTileMapTabSelection
	{
		Total,
		Optimizable,
		Roaming,
	}
	
	// this is for indicating which hierarchy is selected in hierarchy pull down selector. 
	public static enum hierarchyPulldownSelection
	{
		CostCenter,
		Management,
		Approval,
	}
	
	// this is for indicating which trend graph expense filter is being tested. 
	public static enum expenseFilters
	{
		Expense,
		CostPerServiceNumber,
		CountOfServiceNumbers,
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
	
	// this gets the bar chart section in expense spend category. also gets pie section in 'expense' control.
	public static String partialXpathForBarChartInTotalSpendCategoryCategories = "/*/*[@class='highcharts-series-group']/*";
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// The xpaths below help in getting hover values in the controls and selecting elements in controls (some controls).    
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// this is for getting the hover info in expense control 
	public static String partialXpathForHoverInfo = "/*/*[contains(@class,'highcharts-tooltip')]/*/*";	
	
	// this is for selecting slices in the expense control and expense control spend category.
	public static String partialXpathForSliceSelections = "/*/*[@class='highcharts-series-group']/*/*"; // ana_modif   // "/*/*[@class='highcharts-series-group']/*/*[@class='highcharts-point']";	
	
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// CSS for hierarchy dash    
	// /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// this is used to get a list of web elements that hold the items in the  
	public static String hierarchyDependentsList = ".tdb-pov__itemList>li";
	
	
	public static String GetParentPulldownSelection()
	{
		return new Select(driver.findElement(By.cssSelector(".tdb-space--half--top>select"))).getFirstSelectedOption().getText();
	}
	
	// ////////////////////////////////////////////////////////////////
	// The xpaths below help in getting cost filters in  trend graphs    
	// ////////////////////////////////////////////////////////////////
	
	public static String  expenseTrendFilters = "((//div[@class='tdb-card'])[2]/div)[1]/div";  
	public static String  costPerServiceNumberFilters = "((//div[@class='tdb-card'])[2]/div)[3]/div"; 
	public static String  countofServiceNumberFilters = "((//div[@class='tdb-card'])[2]/div)[5]/div"; 

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
	
	// this gets the expense legend names, excluding the 'other' legend, and adds them into 'tempStringList' as their corresponding country name.
	// the list of country names is used to wait for the country view to load.  
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
		
		// get the vendor values (legend names) that were stored while in vendor view into a list. 
		totalExpenseLegendsList = ExpenseHelper.GetTotalExpenseLegends();

		// put the corresponding country values in tempList.
		for(String str : totalExpenseLegendsList)
		{
			if(!str.contains("Other"))
			{
				tempStringList.add(ExpenseHelper.GetCountryForVendor(str));				
			}
		}
	}
	
	// bob - fixed up on 4/13/17.
	// this verifies that one of the countries attached to the vendor legends shown in the vendor view is shown in the country view. 
	// tempStringList has the countries that go with the vendor legends shown in the vendor view. 
	public static void WaitForCountryPageLoad() throws Exception 
	{
		// started failing in  1.2.20 ??
		boolean foundCountryLegend = false;
		chartId = UsageHelper.getChartId(4);
		
		//ShowListOfStrings(tempStringList); 
		
		if(webElementListLegends != null)
		{
			webElementListLegends.clear();
		}
		
		long currentTime= System.currentTimeMillis();
		long endTime = currentTime+10000;
		while(System.currentTimeMillis() < endTime) 
		{
			Thread.sleep(1000);

			// get list of web elements (countries) that are in chartId 4 (bottom trend graph).  
			webElementListLegends = driver.findElements(By.xpath("//div[@id='" + chartId + "']" + partialXpathToLegendsListInControls));

			//ShowWebElementListText(webElementListLegends);
			
			if(webElementListLegends != null)
			{
				// 'tempStringList' has the country names stored away from the vendor view. 
				for(int y = 0; y < tempStringList.size(); y++)
				{
					 // see if country shown in country view is 
					if(tempStringList.contains(webElementListLegends.get(y).getText()))   
					{
						foundCountryLegend = true;
						break;
					}					
				}
			}
		}
		
		if(!foundCountryLegend)
		{
			Assert.fail("Failed to find a country legend in method ExpenseHelper.WaitForCountryPageLoad.");			
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
//				Thread.sleep(2000);				
				TotalExpensesValues.moveMouseToElement(ele, expenseControlSlicesElemntsList.size(), chartId);  //Ana. 5/3/17 - line--> ele.click(); was failing. It got replaced by the mouse move
				Thread.sleep(1000);
				expenseControlHMap.put(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-size')]")).getText(),
									   driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-weight')]")).getText());
//				ShowText(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-size')]")).getText());
//				ShowText(driver.findElement(By.xpath("//div[@id='" +  chartId + "']/*/*[contains(@class,'highcharts-tooltip')]/*/*[contains(@style,'font-weight')]")).getText());				
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
		
//		ShowText("EXPECT");	ShowListOfStrings(expectedVendorNamesList); // DEBUG
//		ShowText("ACTUAL");	ShowListOfStrings(tempStringList); // DEBUG
		
		// This assert is very likely to fail. When the slices are too narrow the mouse pointer may not be placed over all of the slices - Ana 5/3/17
//		Assert.assertEquals(tempStringList, expectedVendorNamesList, "Failed verifying actual versus expected in ExpenseHelpser.VerifyCorrectControlSlices.");
	}
	
	
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

		VerifyExpenseTotalCost(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)")).getText(), errMessage);
	}
	
	public static void VerifyCountOfServiceNumbersCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyCountOfServiceNumbersAndTitle";
		
		//The top says 'Count of Service Numbers'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > h3")).getText(), ExpenseKpiNames[1], errMessage);

		// VerifyCountServiceNumbers(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(2) > div> div:nth-of-type(1)")).getText(), errMessage); // done in KPI numbers test.
	}	
	
	public static void VerifyCostPerServiceNumberCostAndTitle() 
	{
		errMessage = "Failure in ExpenseHelper.VerifyCostPerServiceNumberCostAndTitle";
		
		//The top says 'Count of Service Numbers'		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(3) > h3")).getText(), ExpenseKpiNames[2], errMessage);

		VerifyCostPerServiceNumber(driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(3) > div> div:nth-of-type(1)")).getText(), errMessage);
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

	// this waits for the legends block to be visible in the control type passed in.  NOTE: 1/17/17 - verified this works using total expense.
	public static void WaitForControlLegend(controlType control) throws Exception
	{
		switch(control)
		{
			case totalExpense:
			{
				chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.
				WaitForElementVisible(By.xpath("//div[@id='" +  chartId + "']" + partialXpathForBarChartInTotalSpendCategoryCategories), MediumTimeout);
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
	
	// this is a setup so a wait for the loading of the country page can be done.
	public static void SetupForCountryPageWait()
	{
		// initialize collection needed to do the convert vendors to countries
		SetupCountryAndVendorData(); 

		// this converts the vendor view total expense legends to a list holding what the legends will be in the country view. 
		// this list is stored in the expense helper class.
		SetupForCountryViewPageLoad();  
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
		
		// NEED TO ADD TOTAL EXPENSE HERE
		
		
		chartId =  UsageHelper.getChartId(1); // total expense vendor spend.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForLegendsInTotalSpendCategory + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));

		chartId =  UsageHelper.getChartId(2); // expense trending. 
		// ShowText(chartId);
		tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));

		chartId =  UsageHelper.getChartId(3); // cost per service number. 
		// ShowText(chartId);
		tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
		
		chartId =  UsageHelper.getChartId(4); // count of service numbers  
		// ShowText(chartId);
		tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));

		ExpenseHelper.SetWaitDefault(); // back to default.
	}	
	
	// this verifies one control is not visible by looking for the first legend in each control being not visible.
	// NOTE --- needs finished.
	public static void VerifyOneControlNotPresent(controlType cntrlType) throws Exception
	{
		ExpenseHelper.SetWaitShort(); // override the default because of wait for no element.

		switch(cntrlType)
		{
			case totalExpenseSpendCatergory: // needs work -- 
			{
				chartId =  UsageHelper.getChartId(1);  
				ShowText(chartId);
				// tempUrl = "#" + chartId + ">svg>.highcharts-yaxis-labels>text:nth-of-type(1)"; // css no work		
				//tempUrl = ".//*[@id='highcharts-nwh8js1-0']/*/*[@class='highcharts-series-group']/*/*[contains(@d,'M 0 0')]"; // latest attempt
				
				// tempUrl  = "(.//*[@id='highcharts-nwh8js1-0']/*/*[@class='highcharts-series-group ']/*/*)[1][@visibility='hidden']";
				
				//(.//*[@id='highcharts-nwh8js1-0']/*/*[@class='highcharts-series-group ']/*/*)[1][@visibility='hidden']
				// Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
				//WaitForElementVisible(By.xpath(tempUrl), MediumTimeout); // DEBUG
				
				//WaitForElementVisible(By.xpath(tempUrl), MediumTimeout); // always visible.
				 
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
			case countOfServiceNumbers:
			{
				chartId =  UsageHelper.getChartId(4);  
				// ShowText(chartId);
				tempUrl = "(//div[@id='" +  chartId + "']" +  partialXpathToMonthListInControls  + ")[1]";
				Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
				break;
			}
		}
		
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
	public static void VerifyToolTipTwo(List<String> expectList, String expectedMonth) throws Exception 
	{
		List<String> actualList = new ArrayList<String>();
		List<String> copy = new ArrayList<String>();
		
		errMessage = "Failure in TotalExpensesTrendVendorActions.VerifyToolTipTwo. Failed to verify correct ";
		
		
		// make a copy of the expected list. sorting done further below made failures when expectList was sorted.
		copy.addAll(expectList);  

		// get web list that holds the DOM section that holds the hover values.
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		
		//ShowWebElementListText(webEleListBarGraphHoverValues); // DEBUG
		
		//ShowText("Actual Month " + webEleListBarGraphHoverValues.get(0).getText()); // DEBUG
		//ShowText("Expect Month " + expectedMonth); // DEBUG
		//Pause("");
		

		Assert.assertEquals(webEleListBarGraphHoverValues.get(0).getText(), expectedMonth, errMessage + "month value in hover text."); // verify month at top of hover.
		
		for(WebElement ele : webEleListBarGraphHoverValues)
		{
			if(ele.getText().contains(":"))
			{
				// actualList.add(ele.getText().replace(":", ""));
				actualList.add(ele.getText().replace(":", "").trim().split("  ")[0]); // bob updated for dash 1.2.7
			}
		}
		
		// sort in case orders are different.
		Collections.sort(actualList);
		Collections.sort(copy);
		
		Assert.assertEquals(actualList, copy, errMessage + "vendor.");
	}
	
	
	public static void ShowSelectedMonth()
	{
		ShowText("Selected month is " + desiredMonth);
	}
	
	
	// //////////////////////////////////////////////////////////////////////////
	//					Hierarchy Helpers  
	// //////////////////////////////////////////////////////////////////////////
	
	// this sets the pulldown in the hierarchy dash board tile map section. 
	public static void SetHierarchyMaxDisplayed(int numTilesToDisplay)
	{
		new Select(driver.findElement(By.cssSelector(".tdb-align--right.tdb-text--smaller>select"))).selectByValue(String.valueOf(numTilesToDisplay));
	}

	// this sets the hierarchy tile map tab for what cost filter is sent in. 
	// it also sets a text variable that is used in string hierarchyFilterString. 
	public static void SetHierarchyCostFilter(hierarchyTileMapTabSelection tabSelect)
	{
		currentHierarchyCostFilter = tabSelect;
		
		switch(tabSelect)
		{
			case Total:
			{
				WaitForElementClickable(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(1)"), ShortTimeout, "");
				driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(1)")).click(); // total
				break;
			}
			case Optimizable:
			{
				WaitForElementClickable(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(2)"), ShortTimeout, "");
				driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(2)")).click(); // optimizable
				break;
			}
			case Roaming:
			{
				WaitForElementClickable(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(3)"), ShortTimeout, "");
				driver.findElement(By.cssSelector(".tdb-card>div:nth-of-type(1)>div:nth-of-type(3)")).click(); // roaming
				break;
			}
		}
	}
	
	// this selects the tile map hierarchy pulldown depending on what enum is passed in. 
	public static void SelectHierarchy(hierarchyPulldownSelection select) throws Exception
	{
		switch(select)
		{
		
			case CostCenter:
			{
				new Select(driver.findElement(By.cssSelector(".tdb-space--top>select"))).selectByVisibleText("Cost Center");
				break;
			}
			case Management:
			{
				new Select(driver.findElement(By.cssSelector(".tdb-space--top>select"))).selectByVisibleText("Management");
				break;
			}		
			case Approval:
			{
				new Select(driver.findElement(By.cssSelector(".tdb-space--top>select"))).selectByVisibleText("Approval");
				break;
			}
			default:
			{
				Assert.fail("Error in ExoenseHelper.SelectHierarchy. Enum passed in is not found.");
			}
		}
	}
	
	// this gets the tile map hierarchy pulldown selection.
	public static String GetSelectedHierarchy()
	{
		return new Select(driver.findElement(By.cssSelector(".tdb-space--top>select"))).getFirstSelectedOption().getText();
	}
	
	public static void MoveMouseToBarExpenseActions(String chartId, int indexHighchart, boolean firstMonth) throws InterruptedException, AWTException
	{
 
		// String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
		String cssBar = "#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text:nth-of-type(" + indexHighchart + ")";

		// 'bar' WebElement will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
				
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);
		
		int x_offset = bar.getSize().getWidth() / 2;  // <-- ana_add 
		int y_offset = (int) GeneralHelper.getScrollPosition();
		
		int x = barCoordinates.getX() + x_offset;
		int y = GeneralHelper.getYCoordinate(chartId) + y_offset;
		
		Robot robot = new Robot(); 
		
		if (firstMonth) { // <-- ana_add 
			robot.mouseMove(x - x_offset, y);
			Thread.sleep(500);
		}
		
		robot.mouseMove(x, y);
		// System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		try 
		{
			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
		} 
		catch (Exception e) 
		{
			System.out.println("Tooltip NOT present in DOM.");
			e.printStackTrace();
		}
	}
	
	// this is for clicking legends under a trend chart using x/y locator. 
	public static void SelectLegendWithPointer(String chartId, int indexHighchart) throws InterruptedException, AWTException // jnupp
	{
		// this is css for legend.
		String cssBar = "#" + chartId + ">svg>g.highcharts-legend>g>g>g:nth-of-type(" + indexHighchart +  ")";
		
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
				
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// These coordinates will be used to put the mouse pointer over the requested legend.
		Point barCoordinates = GeneralHelper.getAbsoluteLocation(bar);
		
		int x_offset = bar.getSize().getWidth() / 2;  
		int y_offset = (int) GeneralHelper.getScrollPosition();
		
		y_offset = -629;  // orig -630... -631 moves up.
		int x = barCoordinates.getX() + 8; 
		int y = GeneralHelper.getYCoordinate(chartId) + y_offset; 
		
		Robot robot = new Robot(); 
		
		robot.mouseMove(x, y);
		
		Thread.sleep(1000);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); 
	}

	
	
	public static void SetupExpectedCostFilters()
	{

		expectedCostFilters.add("All Categories");
		expectedCostFilters.add("Voice Charges");
		expectedCostFilters.add("Data Charges");
		expectedCostFilters.add("Messaging Charges");
		expectedCostFilters.add("Roaming Charges");
		expectedCostFilters.add("Equipment Charges");
		expectedCostFilters.add("Taxes");
		expectedCostFilters.add("Other Charges");
		expectedCostFilters.add("Account Level Charges");
	}

	// this sets currentExpenseFilter to what expense filter is being used to click each selection.
	public static void SetExpenseFilter(expenseFilters expFilter)
	{
		currentExpenseFilter = expFilter;
	}

	// this uses the 'currentExpenseFilter' value to decide which set of cost filters will be used to make the clicks across all the filters. 
	// the 'ClickThroughFiltersAndVerify' method does the clicks and then calls the method that handles all the testing. 
	public static void VerifySpendCateoryFilter() throws Exception 
	{
		switch(currentExpenseFilter)
		{
			case Expense:
			{
				ClickThroughFiltersAndVerify(expenseTrendFilters);
				break;
			}
			case CostPerServiceNumber:
			{
				ClickThroughFiltersAndVerify(costPerServiceNumberFilters);
				break;
			}
			case CountOfServiceNumbers:
			{
				//ClickThroughFiltersAndVerify(countofServiceNumberFilters); // this filter was removed.
				break;
			}
			default:
			{
				Assert.fail("Bad case sent to sent to ExpenseHelper.VerifySpendCateoryFilter.");
			}
		}
	}
	
	// this clicks through the category selectors and does the testing described below.
	public static void ClickThroughFiltersAndVerify(String xPath) throws Exception
	{
		// get a list of the web elements that are related to the xPath passed in.
		// the xPath is pointing to one of the category filters. 
		List<WebElement> listToClickThrough = driver.findElements(By.xpath(xPath));
		
		Assert.assertTrue(listToClickThrough.size() != 0, "Found empty list in ClickThroughFiltersAndVerify.");
		
		int x = 0;
		
		// go through the each filter selection and select them one at a time.
		for(WebElement ele : listToClickThrough)
		{
			ele.click();
			
			// these two waits verify the correct text is found in the tile for 'Expense Trending' and 'Cost Per Service Number'. 
			WaitForElementVisible(By.xpath("(//span[text()='" + expectedCostFilters.get(x) + "'])[1]"), MediumTimeout);  
			WaitForElementVisible(By.xpath("(//span[text()='" + expectedCostFilters.get(x) + "'])[2]"), MediumTimeout); 
			
			// this waits for the correct text in Cost per Service Number.
			WaitForElementVisible(By.xpath("//span[text()='Cost per Service Numbers by Vendor']"), MediumTimeout);
			
			// //span[text()='Cost per Service Numbers by Vendor']
			
			VerifyCorrectSelection(listToClickThrough, x); // verify the correct enable/disable states for control xPath sent in to this method.
			
			// this verifies other two category selectors that are not being clicked through. 
			VerifyRemainingCategorySelectors(x);
			x++;
		}
	}
	
	// this is sent a web list of selectors to look through. one of them is selected and the rest aren't.
	// the one that is selected is indicated by the integer sent in. the rest of the cost filters are not selected.
	// this verifies that the list element with the index 'x' is enabled and all the other list elements are not.  
	public static void VerifyCorrectSelection(List<WebElement> eleList,  int x)
	{
		errMessage = "Failed testing of enabled/disabled cost filters in ExpenseHelper.VerifyCorrectSelection";
		
		Assert.assertTrue(eleList.size() != 0, "Method VerifyCorrectSelection has been paseed an empty web element list.");
		
		for(int y = 0; y < eleList.size(); y++)
		{
			if(y == x)
			{
				Assert.assertTrue(eleList.get(y).getAttribute("class").contains("option--selected"),errMessage); 
			}
			else
			{
				Assert.assertFalse(eleList.get(y).getAttribute("class").contains("option--selected"), errMessage);
			}
		}
	}
	
	// this verifies the remaining category selectors. These two selectors were not clicked on. 
	public static void VerifyRemainingCategorySelectors(int x) 
	{

		List<WebElement> tempListOne;
		List<WebElement> tempListTwo;

		switch(currentExpenseFilter)
		{
			case Expense: // expense trend is being clicked, verify other two category selectors.
			{
				tempListOne = driver.findElements(By.xpath(costPerServiceNumberFilters));  
				VerifyCorrectSelection(tempListOne, x);
				//tempListTwo  = driver.findElements(By.xpath(countofServiceNumberFilters));
				//VerifyCorrectSelection(tempListTwo, x);
				break;
				
			}
			case CostPerServiceNumber: // cost per service number trend is being clicked, verify other two category selectors.
			{
				tempListOne = driver.findElements(By.xpath(expenseTrendFilters));
				VerifyCorrectSelection(tempListOne, x);
				//tempListTwo  = driver.findElements(By.xpath(countofServiceNumberFilters));
				//VerifyCorrectSelection(tempListTwo, x);
				break;
				
			}
			case CountOfServiceNumbers:// count of service number trend is being clicked, verify other two category selectors.
			{
			//	tempListOne = driver.findElements(By.xpath(expenseTrendFilters));
			//	VerifyCorrectSelection(tempListOne, x);
			//	tempListTwo  = driver.findElements(By.xpath(costPerServiceNumberFilters));
			//	VerifyCorrectSelection(tempListTwo, x);
				break;
			}
			default:
			{
				Assert.fail("Bad case parameter in ExpenseHelper.VerifyRemainingCostSelectors.");
			}
		}
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
		// Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'. // not always the case.
		tmpStrIn = tmpStrIn.replace("K","").replace("$", "").replace(".",""); // remove all but numbers.
		// VerifyInteger(tmpStrIn, errMess); // verify it has all integers. // not always the case.
	}
	
	public static void VerifyCountServiceNumbers(String tmpStrIn, String errMess)
	{
		// Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'. // // not always the case.
		// tmpStrIn = tmpStrIn.replace("K","").replace("$", "").replace(".",""); // remove all but numbers.
		// VerifyInteger(tmpStrIn, errMess); // verify it has all integers. // not always the case.
	}	
	
	public static void VerifyCostPerServiceNumber(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.startsWith("$"), errMessage); // verify contains '$'.
		// tmpStrIn = tmpStrIn.replace("$","").replace(".",""); // remove all but numbers.
		// VerifyInteger(tmpStrIn, errMess); // verify it has all integers. // not always the case.
	}	
	
	public static void VerifyCountServiceNumbersCost(String tmpStrIn, String errMess)
	{
		Assert.assertTrue(tmpStrIn.endsWith("K"), errMessage); // verify contains 'K'.
		tmpStrIn = tmpStrIn.replace("K","").replace(".", "").replace(".",""); // remove all but numbers.
		VerifyInteger(tmpStrIn, errMess); // verify it has all integers.
	}	
};


