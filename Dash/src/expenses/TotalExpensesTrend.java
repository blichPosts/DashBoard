package expenses;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Exchanger;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.w3c.dom.Element;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.UsageHelper;


public class TotalExpensesTrend extends BaseClass 
{

	public static List<WebElement> webEleListTrends;
	public static List<WebElement> webEleListLegends;
	public static List<WebElement> webEleListMonthYearActual;	
	public static List<WebElement> webEleListVendorCountriesLegends;
	public static List<WebElement> webEleListBarGraphHoverValues;

	public static List<String> expectedTrends = new ArrayList<String>();
	public static List<String> actualTrends = new ArrayList<String>();
	public static List<String> expectedYearMonthList = new ArrayList<String>();
	public static List<String> actualYearMonthList = new ArrayList<String>();
	public static List<String> vendorsList = new ArrayList<String>();
	public static List<String> vendorsListActual = new ArrayList<String>();	
	public static List<String> totalExpenseLegendsList = new ArrayList<String>();
	public static List<String> expectedMonthYear = new ArrayList<String>();
	public static String [] strArray;
	
	public static String mainTitle = "Expense Trending"; 
	public static String vendorTitle = "Expense by Vendor - All Categories";
	public static String countryTitle = "Expense by Country - All Categories";	
	
	// Expense by Country - All Categories
	public static String otherString = "Other";
	public static String errMessage = "";
	public static int numberOfLegends = 0; // this is used to save the number of legends found in VerifyVendorsCountries().

	public static String chartId = "";
	
	public static boolean firstPass = false;
	
	public static void Setup()
	{
		if(totalExpenseLegendsList != null)
		{
			totalExpenseLegendsList.clear();
		}
		
		// this gets a string list of the legends 
		totalExpenseLegendsList = ExpenseHelper.GetTotalExpenseLegends(); // this is needed in 'VerifyToolTipInfo' method.
		
		webEleListLegends = driver.findElements(By.xpath("//div[@id='" +  chartId + "']"  + ExpenseHelper.partialXpathForLegendsInTotalSpendCategoryCategories));
	}
	
	public static void VerifyRemovingLegends() throws Exception // bladdxx
	{
		Thread.sleep(1000);

		// get the 'expense trending' control visible. 
		WebElement usageTrendingSection = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)>div:nth-of-type(1)"));
		new Actions(driver).moveToElement(usageTrendingSection).perform();
		
		Thread.sleep(1000);
		
		//ShowText("external expected --------------------------------");
		//ShowListOfStrings(totalExpenseLegendsList);

		
		for(int x = 0; x < webEleListLegends.size(); x++)
		{
	
			for(int y = 1; y <= ExpenseHelper.maxNumberOfMonths; y++)
			{
				clickBarIndex(y);
				Thread.sleep(500);
				VerifyToolTipTwo(totalExpenseLegendsList); 
			}
			totalExpenseLegendsList.remove(0);
			Thread.sleep(500);
			//ShowText("external expected --------------------------------");
			//ShowListOfStrings(totalExpenseLegendsList);
			webEleListLegends.get(x).click();
			Thread.sleep(2000);
		}
		DebugTimeout(9999, "9999 done");

		
		
		/*
		I have these two lines,  to move to the element I want:
			WebElement usageTrendingSection = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)"));
			   new Actions(driver).moveToElement(usageTrendingSection).perform();
			that's for the Usage Trending charts
		*/
		
		click();
		Thread.sleep(1000);
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		ShowInt(webEleListBarGraphHoverValues.size());
		
		webEleListLegends.get(0).click();
		
		DebugTimeout(1, "Change - 7");
		
		click();
		Thread.sleep(1000);
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		ShowInt(webEleListBarGraphHoverValues.size());
		webEleListLegends.get(1).click();
		
		DebugTimeout(1, "Change - 7");
		
		click();
		Thread.sleep(1000);
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		ShowInt(webEleListBarGraphHoverValues.size());
		webEleListLegends.get(2).click();
		
		DebugTimeout(1, "Change - 7");
		
		click();
		Thread.sleep(1000);
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		ShowInt(webEleListBarGraphHoverValues.size());
		webEleListLegends.get(3).click();
	}
	
	
	public static void click() throws Exception
	{
		String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(1)";
		String cssLine = "#" + chartId + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path:nth-of-type(2)";
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point barCoordinates = bar.getLocation();
		Point lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 250;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(1000);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // may need
	}
	
	public static void clickBarIndex(int barIndex) throws Exception
	{
		String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + barIndex + ")";
		String cssLine = "#" + chartId + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path:nth-of-type(2)";
		
		// 'bar' and 'line' WebElements will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));
		WebElement line = driver.findElement(By.cssSelector(cssLine));
		
		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// Get the location of the second line of the chart -> to get the "y" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point barCoordinates = bar.getLocation();
		Point lineCoordinates = line.getLocation();
		
		Robot robot = new Robot(); 
		int x = barCoordinates.getX() + 30;
		int y = lineCoordinates.getY() + 250;
		
		robot.mouseMove(x, y);
		//System.out.println("coordinates - x: " + x + "  y: " + y);
		
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		if(barIndex == 1) // it takes a while for the hover to become visible on first bar graph.
		{
			//Thread.sleep(2000);
		}
	}
	
	// FAILURE - keeping here for reference
	public static void TestOneVendor(String addedVendor) throws Exception // bladdxx
	{
		vendorsList.add(addedVendor);
		
		// setup expected months.
		expectedMonthYear = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		Collections.reverse(expectedMonthYear);
		
		WaitForElementPresent(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForSliceSelections), MediumTimeout);

		// make a web element list containing all of the bar graphs. 
		List<WebElement> eleList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForSliceSelections));
		
		for(int x = 0; x < eleList.size(); x++)
		{
			Thread.sleep(500);
			try
			{
				ClickBarGraph(eleList.get(x));
			}
			catch(Exception e)
			{
				ShowText("This is an expected error in " + "'" + e.getMessage() + "'"); ///   FINISH text message
				break;
			}
			
			// JOptionPane.showMessageDialog(frame, "Select OK.");
			
			VerifyToolTip(expectedMonthYear.get(x), vendorsList);

		}
	}
	
	public static void ClickBarGraph(WebElement ele)
	{
		ele.click();
		ele.click();
		ele.click();
	}
	
	
	/*
	public static void VerifyToolTipTwo(List<String> expectList) throws Exception
	{
		
		List<String> actualList = new ArrayList<String>();
		List<String> expectedList = new ArrayList<String>();
		
		
		
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));

		//ShowText(webEleListBarGraphHoverValues.get(0).getText()); //  month/year
		
		for(WebElement ele : webEleListBarGraphHoverValues)
		{
			if(ele.getText().contains(":"))
			{
				actualList.add(ele.getText().replace(":",  ""));
			}
		}
		

		ShowText("Show actual");
		ShowListOfStrings(actualList);		
		ShowText("Show expected in method");
		ShowListOfStrings(expectedList);

		
		
		Collections.sort(actualList);
		Collections.sort(expectedList);

		
		Assert.assertEquals(actualList, expectedList);
		
		ShowText("DONE");
		
		
	}
	*/
	
	
	
	
	// keep for reference.
	public static void VerifyToolTip(String expectedMonthYear, List<String> vendorList)
	{
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));
		Assert.assertEquals(webEleListBarGraphHoverValues.get(0).getText(), expectedMonthYear, ""); // verify date at the top of the hover info 
		
		for(int y = 2, x = 0; x < vendorList.size(); y += 4, x++)
		{
			ShowText("Hover " + webEleListBarGraphHoverValues.get(y).getText().replace(":",""));
			ShowText("Vendor " + vendorList.get(x));
			Assert.assertEquals(webEleListBarGraphHoverValues.get(y).getText().replace(":",""), vendorList.get(x));
		}
	}
	
	
	// partialXpathForHoverInfo
	
	public static void Setupdata()
	{
		// get the actual trend selections.
		webEleListTrends = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right > div"));
 
		// load the actual trend selections
		for(WebElement ele : webEleListTrends)
		{
			actualTrends.add(ele.getText());
		}

		// load the expected trend selections 
		expectedTrends.add("All");
		expectedTrends.add("Voice");
		expectedTrends.add("Data");
		expectedTrends.add("Messages");
		expectedTrends.add("Roaming");
		expectedTrends.add("Equipment");
		expectedTrends.add("Taxes");
		expectedTrends.add("Other");
		expectedTrends.add("Account");
	}

	
	public static void VerifyTrendValues()
	{
		ClearAllContainers();
		
		Setupdata();
		
		Assert.assertEquals(actualTrends, expectedTrends, "Failure in verifying selectable trend items.");
	}

	public static void VerifyTitlesVendorView()
	{
		errMessage = "Failed check in TotalExpensesTrend.VerifyTitlesVendorView";
		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText(), vendorTitle, errMessage);
		
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='tdb-card'])[2]/h2")).getText(), mainTitle, errMessage);
	}
	
	public static void VerifyTitlesCountryView() throws Exception
	{
		errMessage = "Failed check in TotalExpensesTrend.VerifyTitlesVendorView";
		
		WaitForElementVisible(By.xpath("(//span[text()='Country'])[2]"), MediumTimeout);
		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText(), countryTitle, errMessage);
		
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='tdb-card'])[2]/h2")).getText(), mainTitle, errMessage);
	}
	
	// this verifies the months in the control are the same months that are in the months pulldown - need to send xpath to the method. 	
	public static void VerifyMonths() throws ParseException 
	{
		ExpenseHelper.VerifyMonths(".//*[@id='" + chartId + "']" + ExpenseHelper.partialXpathToMonthListInControls);
	}
	
	// this verifies the legends under the control match the legends in the 'total expense' control. 
	public static void VerifyVendorsCountriesLegends() throws ParseException
	{
		errMessage = "Failed verification in TotalExpenseTrend.VerifyVendorsCountriesLegends";
		
		// clear containers if needed.
		ClearAllContainers();

		webEleListVendorCountriesLegends = driver.findElements(By.xpath("//div[@id = '" + chartId  + "']" + ExpenseHelper.partialXpathToLegendsListInControls));
		
		for(WebElement ele : webEleListVendorCountriesLegends) // put vendor names into a list so can use 'contains'. 
		{
			vendorsListActual.add(ele.getText());
		}
		
		// verify vendors list is the same as the 'total expense' control. 
		// get the legends in the 'total expense' control.  
		totalExpenseLegendsList =  ExpenseHelper.GetTotalExpenseLegends();
		
		Assert.assertEquals(vendorsListActual, totalExpenseLegendsList, errMessage); 
		
		// verify 'other' selection present or not.
		if(webEleListVendorCountriesLegends.size() < 5)
		{
			Assert.assertFalse(vendorsListActual.contains(otherString));
		}
		else
		{
			Assert.assertTrue(vendorsListActual.contains(otherString));
		}

		numberOfLegends = vendorsListActual.size(); // to be used in VerifyNumLegendsMatchNumBarSections().
	}
	
	// verify number of legends equals number of sections in the bar graphs.
	public static void VerifyNumLegendsMatchNumBarSections()
	{
		errMessage = "Fail verifying number of legands and number of bar chart parts in TotalExpensesTrend.VerifyNumLegendsMatchNumBarSections";
		
		String cntrlPath = "//div[@id = '" + chartId  + "']" + ExpenseHelper.partialXpathToBarGrapghControls;

		Assert.assertTrue(driver.findElements(By.xpath(cntrlPath)).size() == numberOfLegends, errMessage);
	}
	
	public static void SetupChartId()
	{
		chartId = UsageHelper.getChartId(2);
	}
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helpers.
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	public static void VerifyToolTipTwo(List<String> expectList) throws Exception
	{
		
		List<String> actualList = new ArrayList<String>();
		List<String> copy = new ArrayList<String>();
		
		copy.addAll(expectList);
		
		webEleListBarGraphHoverValues = driver.findElements(By.xpath("//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForHoverInfo));

		//ShowText(webEleListBarGraphHoverValues.get(0).getText()); //  month/year
		
		for(WebElement ele : webEleListBarGraphHoverValues)
		{
			if(ele.getText().contains(":"))
			{
				actualList.add(ele.getText().replace(":",  ""));
			}
		}
		
		Collections.sort(actualList);
		Collections.sort(copy);

		
		Assert.assertEquals(actualList, copy);
	}

	public static List<String> MakeCopyOfList(List<String> origList)
	{
		List<String> copyList = new ArrayList<String>();
		
		copyList.addAll(origList);
		
		return copyList;
	}
	
	
	public static void ClearAllContainers()
	{
		if(webEleListTrends != null)
		{
			webEleListTrends.clear();
		}

		if(expectedTrends != null)
		{
			expectedTrends.clear();
		}
		
		if(webEleListTrends != null)
		{
			webEleListTrends.clear();
		}		
		
		if(webEleListMonthYearActual != null)
		{
			webEleListMonthYearActual.clear();
		}		
		
		if(webEleListVendorCountriesLegends != null)
		{
			webEleListVendorCountriesLegends.clear();
		}		

		if(expectedTrends  != null)
		{
			expectedTrends.clear();
		}		
	
		if(actualTrends  != null)
		{
			actualTrends.clear();
		}		
		
		if(expectedYearMonthList  != null)
		{
			expectedYearMonthList.clear();
		}		
		
		if(actualYearMonthList  != null)
		{
			actualYearMonthList.clear();
		}		
		
		if(vendorsListActual  != null)
		{
			vendorsListActual.removeAll(vendorsListActual);
		}		
		
		if(totalExpenseLegendsList  != null)
		{
			totalExpenseLegendsList.clear();
		}		
		
		if(totalExpenseLegendsList  != null)
		{
			totalExpenseLegendsList.clear();
		}		
		
		if(webEleListVendorCountriesLegends  != null)
		{
			webEleListVendorCountriesLegends.clear();
		}				
	}
}
