package expenses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;


public class TotalExpensesTrend extends BaseClass 
{

	public static List<WebElement> webEleListTrends;
	public static List<WebElement> webEleListMonthYearActual;	
	public static List<WebElement> webEleListVendorCountriesLegends;
	public static List<String> expectedTrends = new ArrayList<String>();
	public static List<String> actualTrends = new ArrayList<String>();
	public static List<String> expectedYearMonthList = new ArrayList<String>();
	public static List<String> actualYearMonthList = new ArrayList<String>();
	public static List<String> vendorsList = new ArrayList<String>();
	public static String [] strArray;
	
	public static String mainTitle = "Expense Trending"; 
	public static String vendorTitle = "Expense by Vendor - All Categories";
	public static String countryTitle = "Expense by Country - All Categories";	
	
	// Expense by Country - All Categories
	public static String otherString = "Other";
	public static String errMessage = "";
	public static int numberOfLegends = 0; // this is used to save the number of legends found in VerifyVendorsCountries().

	public static String chartId = "";
	
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
	
	
	public static void VerifyMonths() throws ParseException, InterruptedException
	{
		
		ClearAllContainers();
		
		// get expected month(int)/years pulldown - format is MM-YYYY.
		expectedYearMonthList = CommonTestStepActions.YearMonthIntergerFromPulldownTwoDigitYear();
		
		chartId = UsageHelper.getChartId(3);
		
		// get actual month(int)/years pulldown from UI.
		webEleListMonthYearActual = driver.findElements(By.xpath(".//*[@id='" + chartId + "']/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels ']/*/*"));
		
		// put actual web list values into a list of strings.
		for(WebElement ele : webEleListMonthYearActual)
		{
			actualYearMonthList.add(ele.getText());
			//System.out.println(ele.getText());			
		}

		// the actual list is in descending order. the expected list is in ascending order. using sort on actual list didn't correct order, so use x increment and y decrement.  
		for(int x = 0, y = expectedYearMonthList.size() - 1; x < expectedYearMonthList.size(); x++, y--)
		{
			Assert.assertEquals(actualYearMonthList.get(y), expectedYearMonthList.get(x), "Failed check of months in TotalExpensesTrend.VerifyMonths");
		}
	}
	
	
	public static void VerifyVendorsCountries()
	{
		// clear containers if needed.
		ClearAllContainers();

		// get the list vendors or countries listed under the bar graph.
		webEleListVendorCountriesLegends = driver.findElements(By.xpath(".//*[@id='highcharts-4']/*/*[@class='highcharts-legend']/*/*/*"));

		for(WebElement ele : webEleListVendorCountriesLegends) // put vendor names into a list so can use 'contains'. 
		{
			vendorsList.add(ele.getText());
		}
		
		// verify 'other' selection present or not.
		if(webEleListVendorCountriesLegends.size() < 5)
		{
			Assert.assertFalse(vendorsList.contains(otherString));
		}
		else
		{
			Assert.assertTrue(vendorsList.contains(otherString));
		}

		numberOfLegends = vendorsList.size(); // to be used in VerifyNumLegendsMatchNumBarSections().
	}
	
	public static void VerifyNumLegendsMatchNumBarSections()
	{
		errMessage = "Fail verifying number of legands and number of bar chart parts in TotalExpensesTrend.VerifyNumLegendsMatchNumBarSections";
		String cntrlPath = ".//*[@id='highcharts-4']/*/*[@class='highcharts-series-group']/*";
		
		//System.out.println(driver.findElements(By.xpath(cntrlPath + "[contains(@class,'highcharts-series highcharts')]")).size()); // DEBUG
		//System.out.println(driver.findElements(By.xpath(cntrlPath + "[contains(@visibility,'hidden')]")).size());  // DEBUG
	
		// verify none of the legends are un-selected.
		Assert.assertTrue(driver.findElements(By.xpath(cntrlPath+ "[contains(@visibility,'hidden')]")).size() == 0,  errMessage);	
		
		// verify the number of legends equals the number of bar chart slices.  
		Assert.assertTrue(driver.findElements(By.xpath(cntrlPath + "[contains(@class,'highcharts-series highcharts')]")).size() == numberOfLegends, errMessage);
	}
	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helpers.
	// //////////////////////////////////////////////////////////////////////////////////////////////////

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
			expectedTrends .clear();
		}		
	
		if(actualTrends  != null)
		{
			actualTrends .clear();
		}		
		
		if(expectedYearMonthList  != null)
		{
			expectedYearMonthList .clear();
		}		
		
		if(actualYearMonthList  != null)
		{
			actualYearMonthList .clear();
		}		
		
		if(vendorsList  != null)
		{
			vendorsList .clear();
		}		
	}
	
	
	
}
