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
import helperObjects.ExpenseHelper;
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
	// public static List<String> vendorsList = new ArrayList<String>();
	public static List<String> vendorsListActual = new ArrayList<String>();	
	public static List<String> totalExpenseLegendsList = new ArrayList<String>();	
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
