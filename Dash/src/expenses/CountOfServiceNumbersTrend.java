package expenses;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.UsageHelper;

public class CountOfServiceNumbersTrend extends BaseClass 
{
	public static List<String> totalExpenseLegends  = new ArrayList<String>(); // this will hold the legend names in the total expanse control.
	public static List<String> countOfServiceNumsLegends  = new ArrayList<String>(); // this will hold the legend names in the count of service numbers control.	
	public static List<String> expectedMonthYear = new ArrayList<String>();
	public static List<WebElement> tempList; // this is for temporary values.
	public static List<WebElement> webEleListLegends;
	public static List<String> totalExpenseLegendsList = new ArrayList<String>();
	public static String vendorTitle =  "Count of Service Numbers by Vendor";
	public static String countryTitle =  "Count of Service Numbers by Country";
	public static String titleXpath = "//h3[starts-with(text(), 'Count of Service Numbers')]";
	public static String errMessage = "";
	public static String chartId = "";	
	public static int numberOfLegends = 0;
	
	// mouse clicks.
	public static String cssBar = "";
	public static String cssLine = "";
	public static Point barCoordinates;
	public static Point lineCoordinates;
	
	
	public static void SetupChartId()
	{
		chartId = UsageHelper.getChartId(4);
	}
	
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
		
		for(WebElement ele :webEleListLegends){ShowText(ele.getText());} // DEBUG
		
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
				Assert.assertEquals(driver.findElement(By.xpath("(//h3/span[contains(text(),'Count of Service Numbers')])[1]")).getText(), vendorTitle, errMessage);
				break;
			}
			case country:
			{
				WaitForElementVisible(By.xpath("(//h3/span)[1][contains(text(),'Country')]"), MediumTimeout);
				Assert.assertEquals(driver.findElement(By.xpath("(//h3/span[contains(text(),'Count of Service Numbers')])[1]")).getText(), countryTitle, errMessage);
				break;				
			}
		}
	}

	// this is for setting the chartId to point to a different
	//public static void SetupChartId()
	//{
	//	chartId = UsageHelper.getChartId(4);
	//}
	
	// this is for changing the chartId to point to the control being used.  
	public static void SetChartId(int id)
	{
		chartId = UsageHelper.getChartId(id);
	}
	
	// this verifies removing vendors one at a time in the expense trending control. 
	public static void VerifyRemovingLegends() throws Exception // bladdxx 
	{
		Thread.sleep(1000);
		
		// get the 'Cost per Service Number trending' control visible by moving to it. 
		// WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)>div:nth-of-type(3)"));
		WebElement expenseTrending = driver.findElement(By.cssSelector(".tdb-EXPENSE__NORMAL-VIEW>div:nth-of-type(2)>div:nth-of-type(5)"));
		
		new Actions(driver).moveToElement(expenseTrending).perform();

		Thread.sleep(1000);
		
		// setup expected months. these are the months that will be shown in the hover for each bar clicked.
		expectedMonthYear = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		Collections.reverse(expectedMonthYear);

		// this loop will un-select each vendor, one at a time, and verify the hover values for each month.
		for(int x = 0; x < webEleListLegends.size(); x++)
		{
			boolean firstMonth = true; // <-- ana_add
			
			for(int y = 1; y <= ExpenseHelper.maxNumberOfMonths; y++)
			{
				// clickBarIndex(y); // orig
				if(loginType.equals(LoginType.ReferenceApp)) // for reference app.
				{
					clickBarIndex(y);
				}
				else
				{
					ExpenseHelper.MoveMouseToBarExpenseActions(chartId, y, firstMonth);
					firstMonth = false; // <-- ana_add
				}				
				ExpenseHelper.VerifyToolTipTwo(totalExpenseLegendsList, expectedMonthYear.get(y - 1));  // verify current hover value
			}
			totalExpenseLegendsList.remove(webEleListLegends.get(x).getText());
			Thread.sleep(2000);
			webEleListLegends.get(x).click();
			Thread.sleep(1500);
		}
	}
	
	public static void clickBarIndex(int barIndex) throws Exception
	{
		cssBar = "#" + chartId + ">svg>g:nth-of-type(8)>text:nth-of-type(" + barIndex + ")"; // this is x axis
		cssLine = "#" + chartId + ">svg>g:nth-of-type(7)>text:nth-of-type(2)";
		
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
		int y = lineCoordinates.getY() + 250;
		
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
	
	// verify the legends in the control match legends in the 'total expense' control. 
	public static void VerifyLegends() throws Exception
	{
		errMessage = "Failed test of legends in CountOfServiceNumbersTrend.VerifyLegends"; 
		
		ClearContainers(); 
		
		totalExpenseLegends = ExpenseHelper.GetTotalExpenseLegends();
		
		// now get the legends in the count of service numbers control and put into list
		tempList = driver.findElements(By.xpath(".//*[@id='" + chartId +  "']" + ExpenseHelper.partialXpathToLegendsListInControls));
		
		// System.out.println(tempList.size());
		// for(WebElement ele: tempList) {System.out.println(ele.getText());} // DEBUG

		for(WebElement ele : tempList)
		{
			countOfServiceNumsLegends.add(ele.getText());			 
		}

		Assert.assertEquals(countOfServiceNumsLegends, totalExpenseLegends, errMessage);
		
		numberOfLegends = totalExpenseLegends.size(); // save the number of legends.
	}
	
	// this verifies the months in the control are the same months that are in the months pulldown - need to send xpath to the method.
	public static void VerifyMonths() throws ParseException 
	{
		ExpenseHelper.VerifyMonths(".//*[@id='" + chartId + "']" + ExpenseHelper.partialXpathToMonthListInControls);
	}
	
	// verify number of legends equals number of sections in the bar graphs.
	public static void VerifyNumLegendsMatchNumBarSections() throws Exception
	{
		errMessage = "Fail verifying number of legands and number of bar chart parts in CountOfServiceNumbersTrend.VerifyNumLegendsMatchNumBarSections";
		
		if (webEleListLegends != null)
		{
			webEleListLegends.clear();
		}
		
		webEleListLegends =  driver.findElements(By.xpath("//div[@id = '" + chartId  + "']" + ExpenseHelper.partialXpathToBarGrapghControls ));
		
		// verify there are no bar sections with attribute 'visibility'. 
		for(WebElement ele : webEleListLegends)
		{
			if(ele.getAttribute("visibility") != null)
			{
				Assert.fail(errMessage);
			}
		}
		
		// verify number of legends equal number of bar chart sections.
		String cntrlPath = "//div[@id = '" + chartId  + "']" + ExpenseHelper.partialXpathToBarGrapghControls;
		
		// verify number of legends equal number of bar chart sections.
		Assert.assertTrue(driver.findElements(By.xpath(cntrlPath)).size() == numberOfLegends, errMessage);
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
