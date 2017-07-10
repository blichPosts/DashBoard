package expenses;

// 
// Revision history.
//
// 1/29/16 - updated VerifyRemovingLegends() to call click method in ExpenseValueHelper because it is more reliable.


import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import Dash.BaseClass.BrowserType;
import Dash.BaseClass.LoginType;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseValuesHelper;
import helperObjects.UsageHelper;
import helperObjects.ExpenseHelper.controlType;

public class CostPerServiceNumberTrend extends  BaseClass
{
	
	public static String chartId = "";
	
	public static String vendorTitle = "Cost per Service Numbers by Vendor ($) - All Categories";
	public static String vendorTitleShort = "Cost per Service Numbers by Vendor ($)";
	
	public static String countryTitle = "Cost per Service Numbers by Country ($) - All Categories";	
	public static String countryTitleShort = "Cost per Service Numbers by Country ($)";
	
	public static List<WebElement> webEleListLegends;
	public static List<WebElement> webEleListBarGraphHoverValues;
	
	public static List<String> totalExpenseLegends  = new ArrayList<String>(); // this will hold the legend names in the total expanse control.
	public static List<String> countOfServiceNumsLegends  = new ArrayList<String>(); // this will hold the legend names in the count of service numbers control.
	public static List<String> expectedMonthYear = new ArrayList<String>(); 
	public static List<WebElement> tempList; // this is for temporary values.
	public static List<String> totalExpenseLegendsList = new ArrayList<String>();
	public static String titleXpath = "//h3[starts-with(text(), 'Count of Service Numbers')]";
	public static String errMessage = "";
	public static int numberOfLegends = 0;
	
	
	// mouse clicks.
	public static String cssBar = "";
	public static String cssLine = "";
	public static Point barCoordinates;
	public static Point lineCoordinates;

	
	public static void Setup()
	{
		if(totalExpenseLegendsList != null)
		{
			totalExpenseLegendsList.clear();
		}
		
		if(webEleListLegends != null)
		{
			webEleListLegends.clear();
		}
		
		// this gets a string list of the legends. these are used for expected values. 
		totalExpenseLegendsList = ExpenseHelper.GetTotalExpenseLegends(); // this is needed in 'VerifyToolTipInfo' method.
		
		// this get web list of legends that are used for clicking legends. 
		webEleListLegends = driver.findElements(By.xpath("//div[@id='" +  chartId + "']"  + ExpenseHelper.partialXpathForLegendsInTotalSpendCategoryCategories));
	}	
	
	// this verifies removing vendors one at a time in the expense trending control. 
	public static void VerifyRemovingLegends() throws Exception
	{
		Thread.sleep(1000);
		
		// this does nothing in fire fox.
		// change to below because of legend click problems. 5/6/17
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(2)"));  
		new Actions(driver).moveToElement(expenseTrending).perform();

		if(browserType.equals(BrowserType.FireFox))
		{
			ExpenseHelper.scrollPage();
		}
		
		Thread.sleep(1000);
		
		// this gets the web element list for the legends so they can be clicked using web elements.
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text")); 

		Assert.assertTrue(eleList.size() > 0, "Error in CostPerServiceNumber.VerifyRemovingLegends. Legends list should not be empty.");
		
		// setup expected months. these are the months that will be shown in the hover for each bar clicked.
		expectedMonthYear = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		Collections.reverse(expectedMonthYear);
		
		ExpenseValuesHelper.SetupChartIdForCostPerServiceNumber(); // 1/29/17 - need this so  'ExpenseValuesHelper' can call reliable x/y click.
		
		// this loop will un-select each vendor, one at a time, and verify the hover values for each month.
		for(int x = 0; x < webEleListLegends.size(); x++)
		{
			boolean firstMonth = true; // <-- ana_add

			for(int y = 1; y <= ExpenseHelper.maxNumberOfMonths; y++)
			{
				if(loginType.equals(LoginType.MatrixPortal)) // matrix ---------------- !!! 
				{
					TotalExpensesTrend.clickBarIndexMatrixPortal(y, firstMonth, chartId);
					firstMonth = false;
				}
				else if(loginType.equals(LoginType.ReferenceApp)) // for reference app.
				{
					ExpenseValuesHelper.CostPerServiceNumberclickBarIndex(y); // 1/29/17 - changed to call ExpenseValuesHelper. this has a more reliable click method.
				}
				else
				{
					ExpenseHelper.MoveMouseToBarExpenseActions(chartId, y, firstMonth);
					firstMonth = false; // <-- ana_add
				}				
				// Thread.sleep(500);
				
				// need to see which view type is being tested. each view type will have a different expected hover title.   
				if(ExpenseHelper.expenseViewMode == ExpensesViewMode.vendor)
				{
					ExpenseHelper.VerifyToolTipTwo(totalExpenseLegendsList, expectedMonthYear.get(y - 1), ExpenseHelper.expectedHoverTitleCostPerServiceNumVendor);  // verify current hover value with vendor title					
				}
				else
				{
					ExpenseHelper.VerifyToolTipTwo(totalExpenseLegendsList, expectedMonthYear.get(y - 1), ExpenseHelper.expectedHoverTitleCostPerServiceNumCountry);  // verify current hover value with country title. 
				}
			}

			Thread.sleep(2000);
			// webEleListLegends.get(x).click();
			totalExpenseLegendsList.remove(webEleListLegends.get(x).getText());
			
			if(loginType.equals(LoginType.MatrixPortal)) // matrix ---------------- !!! 
			{
				webEleListLegends.get(x).click(); // this is web element click.
			}
			else
			{
				// 6/26/17 - this x/y click method is no longer needed to click legends. see directly below. keeping this call here in case it's needed in the future.
				// ExpenseHelper.SelectLegendWithPointer(chartId, x + 1);
				
				// 6/26/17 - this is the new click. by changing the way the list of legend web elements is created further above in this method, the click below works.   
				eleList.get(x).click();
			}
			Thread.sleep(1500);
		}
		ExpenseHelper.VerifyOneControlNotPresent(ExpenseHelper.controlType.costPerServiceNumber); // verify there are no bar graphs in cost per service category.
	}

	// this verifies adding vendors one at a time in the cost per service trending control.
	// NOTE: this is intended to be run after all legends in the trend control are disabled.  
	public static void VerifyAddingLegends() throws Exception 
	{
		Thread.sleep(1000);
		
		// this string list will have legend names added as legends are enabled/clicked. it is expected to be non-null
		// because the VerifyRemovingLegends() test created it. 
		totalExpenseLegendsList.clear(); 
		
		// this does nothing in fire fox.
		// get the 'cost per service number' control visible by moving to it. 
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(2)"));
		new Actions(driver).moveToElement(expenseTrending).perform();

		if(browserType.equals(BrowserType.FireFox))
		{
			ExpenseHelper.scrollPage();
		}
		
		Thread.sleep(1000);

		// this gets the web element list for the legends so they can be clicked using web elements.
		List<WebElement> eleList = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-legend>g>g>g>text")); 
		
		Assert.assertTrue(eleList.size() > 0, "Error in TotalExpensesTrend.VerifyRemovingLegends. Legends list should not be empty.");
		
		// select the first legend so there are bar graphs and add the legend name to totalExpenseLegendsList.
		webEleListLegends.get(0).click();
		totalExpenseLegendsList.add(webEleListLegends.get(0).getText());
		
		// setup expected months. these are the months that will be shown in the hover for each bar clicked.
		expectedMonthYear = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		Collections.reverse(expectedMonthYear);
		
		// ShowText(expectedMonthYear.get(0)); 
		
		// this loop will un-select each vendor, one at a time, and verify the hover values for each month.
		for(int x = 1; x <= webEleListLegends.size(); x++) 
		{
			boolean firstMonth = true; // <-- ana_add

			// this loops across all months and clicks/verifies each month in the trend. 
			for(int y = 1; y <= ExpenseHelper.maxNumberOfMonths; y++)
			{
				if(loginType.equals(LoginType.MatrixPortal)) // matrix ---------------- !!! 
				{
					TotalExpensesTrend.clickBarIndexMatrixPortal(y, firstMonth, chartId);
					firstMonth = false;
				}
				else if(loginType.equals(LoginType.ReferenceApp))
				{
					clickBarIndex(y);
				}
				else
				{  
					ExpenseHelper.MoveMouseToBarExpenseActions(chartId, y, firstMonth);
					firstMonth = false;
				}
				
				// Thread.sleep(500);
				
				// need to see which view type is being tested. each view type will have a different expected hover title.   
				if(ExpenseHelper.expenseViewMode == ExpensesViewMode.vendor)
				{
					ExpenseHelper.VerifyToolTipTwo(totalExpenseLegendsList, expectedMonthYear.get(y - 1), ExpenseHelper.expectedHoverTitleCostPerServiceNumVendor);  // verify current hover value with vendor title					
				}
				else
				{
					ExpenseHelper.VerifyToolTipTwo(totalExpenseLegendsList, expectedMonthYear.get(y - 1), ExpenseHelper.expectedHoverTitleCostPerServiceNumCountry);  // verify current hover value with country title. 
				}
			}
			
			// the hovers have been tested. if the last legend to be added has been added, exit the for loop that clicks the legends to add a legend.
			if(x == webEleListLegends.size()) 
			{
				break;
			}						
			
			// totalExpenseLegendsList.remove(webEleListLegends.get(x).getText()); 
			Thread.sleep(1000);
			
			if(loginType.equals(LoginType.MatrixPortal)) // matrix ---------------- !!! 
			{
				// webEleListLegends.get(x).click(); // this is web element click.
				eleList.get(x).click(); // 6/26/17 
			}
			else
			{
				// 6/26/17 - this x/y click method is no longer needed to click legends. see directly below. keeping this call here in case it's needed in the future.
				// ExpenseHelper.SelectLegendWithPointer(chartId, x + 1);	 // this click needs x/y click.  	
				
				// 6/26/17 - this is the new click. by changing the way the list of legend web elements is created further above in this method, the click below works.
				totalExpenseLegendsList.add(webEleListLegends.get(x).getText());
				eleList.get(x).click();
			}
			Thread.sleep(1500);
		}
	}
	
	
	public static void clickBarIndex(int barIndex) throws Exception
	{
		cssBar = "#" + chartId + ">svg>g:nth-of-type(8)>text:nth-of-type(" + barIndex + ")";
		cssLine = "#" + chartId + ">svg>g:nth-of-type(7)>text:nth-of-type(1)";
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		barCoordinates = bar.getLocation();
		lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		//int x = barCoordinates.getX() + 30; 
		//int y = lineCoordinates.getY() + 250;
		
		// 1/11/16 - moves cursor arrow up.
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 150;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		if(barIndex == 1) // it takes a while for the hover to become visible on first bar graph.
		{
			//Thread.sleep(2000); // quit happening
		}
	}
	
	// this has a wait for element visible in each case statement. this is needed because this can be the first method called after switching to the country view.
	// there is a wait in the vendor view  also as a redundant check.
	public static void VerifyTitle(CommonTestStepActions.ExpensesViewMode mode) throws Exception
	{
		errMessage = "Fail in test for title in CountOfServiceNumbersTrend.VerifyTitle";
		switch(mode)
		{
			case vendor:
			{
				WaitForElementVisible(By.xpath("(//h3/span)[1][contains(text(),'Vendor')]"), MediumTimeout);
				Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(2)")).getText(), vendorTitle, errMessage);
				break;
			}
			case country:
			{
				WaitForElementVisible(By.xpath("(//h3/span)[1][contains(text(),'Country')]"), MediumTimeout);
				Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(2)")).getText(), countryTitle, errMessage);
				break;				
			}
		}
	}
	
	// verify the legends in the control match legends in the 'total expense' control. 
	public static void VerifyLegends() throws Exception
	{
		errMessage = "Failed test of legends in CostPerServiceNumberTrend.VerifyLegends"; 
		
		// bob had to add  - 5/23/17
		ExpenseHelper.WaitForControlLegend(controlType.totalExpense);
		ExpenseHelper.WaitForControlLegend(controlType.countOfServiceNumbers);
		
		totalExpenseLegends = ExpenseHelper.GetTotalExpenseLegends();
		
		ClearContainers(); 
		
		// now get the legends in the count of service numbers control and put into list
		tempList = driver.findElements(By.xpath(".//*[@id='" + chartId +  "']" + ExpenseHelper.partialXpathToLegendsListInControls));
		
		// System.out.println(tempList.size());
		// for(WebElement ele: tempList) {System.out.println(ele.getText());} // DEBUG

		for(WebElement ele : tempList)
		{
			countOfServiceNumsLegends.add(ele.getText());			 
		}

		Assert.assertEquals(countOfServiceNumsLegends, totalExpenseLegends, errMessage);
		
		numberOfLegends = totalExpenseLegends.size(); // save the number of legends for future test.
	}
	
	// this verifies the months in the control are the same months that are in the months pulldown - need to send xpath to the method.
	public static void VerifyMonths() throws ParseException 
	{
		ExpenseHelper.VerifyMonths(".//*[@id='" + chartId + "']" + ExpenseHelper.partialXpathToMonthListInControls);
	}
	
	// verify number of legends equals number of sections in the bar graphs.
	public static void VerifyNumLegendsMatchNumBarSections() throws Exception 
	{
		errMessage = "Fail verifying number of legands and number of bar chart parts in CostPerServiceNumberTrend.VerifyNumLegendsMatchNumBarSections";
		
		// verify each bar is there
		if (webEleListLegends != null)
		{
			webEleListLegends.clear();
		}
		
		// this gets the horizontal bars, in the DOM, that extend across all the bar graphs. 
		webEleListLegends =  driver.findElements(By.xpath("//div[@id = '" + chartId  + "']" + ExpenseHelper.partialXpathToBarGrapghControls ));
		
		// verify there are no bar sections with attribute 'visibility'. 
		for(WebElement ele : webEleListLegends)
		{
			if(ele.getAttribute("visibility") != null)
			{
				Assert.fail(errMessage);
			}
		}
		
		// verify 
		Assert.assertTrue(webEleListLegends.size() == numberOfLegends, errMessage);

	}	
	
	
	public static void SetupChartId()
	{
		chartId = UsageHelper.getChartId(3);
	}

	// //////////////////////////////// HELPER ///////////////////////////////////////////////////////////

	public static void ClearContainers()
	{
		if(countOfServiceNumsLegends !=  null)
		{
			countOfServiceNumsLegends.removeAll(countOfServiceNumsLegends);
		}		
	}	
}
