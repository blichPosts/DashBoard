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


public class TotalExpensesTrend extends BaseClass 
{

	public static List<WebElement> webEleListTrends;
	public static List<WebElement> webEleListMonthYearActual;	
	public static List<String> expectedTrends = new ArrayList<String>();
	public static List<String> actualTrends = new ArrayList<String>();
	public static List<String> expectedYearMonthList = new ArrayList<String>();
	public static List<String> actualYearMonthList = new ArrayList<String>();
	public static String [] strArray;
	
	public static String mainTitle = "Expense Trending"; 
	public static String vendorTitle = "Expense by Vendor - All Categories";
	public static String errMessage = "";
	
	
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
		
		System.out.println(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText());		
		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText(), vendorTitle, errMessage);
		
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='tdb-card'])[2]/h2")).getText(), mainTitle, errMessage);
	}
	
	public static void VerifyMonths() throws ParseException
	{
	
		// get expected month(int)/years pulldown
		expectedYearMonthList = YearMonthIntergerFromPulldown(); 
		
		// get actual month(int)/years pulldown
		webEleListMonthYearActual = driver.findElements(By.xpath(".//*[@id='highcharts-4']/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels']/*/*"));
		
		for(WebElement ele : webEleListMonthYearActual)
		{
			// actualYearMonthList.add(ele.getText());
			System.out.println(ele.getText());			
		}

		 // java.util.Collections.sort(actualYearMonthList);
		
		for(int x = 0; x < expectedYearMonthList.size(); x++)
		{
			System.out.println(expectedYearMonthList.get(x));
			// System.out.println(actualYearMonthList.get(x));

		}
		
		
		// (.//*[@id='highcharts-4']/*/*)[@class='highcharts-axis-labels highcharts-xaxis-labels']/*
		
		// (.//*[@id='highcharts-4']/*/*)[@class='highcharts-axis-labels highcharts-xaxis-labels']/*/*

		
		
		// (.//*[@id='highcharts-4']/*/*)[@class='highcharts-axis-labels highcharts-xaxis-labels'] // <<<< this is row of dates.
		// System.out.println(ConvertMonthToInt("January"));
		
	}
	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helpers.
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	public static List<String> YearMonthIntergerFromPulldown() throws ParseException
	{
		List<String> tmpList = new ArrayList<String>();
		
		// get the year/month items from the pulldown and create a list of of month (integer) with year.  
		for( WebElement ele : CommonTestStepActions.webListPulldown)
		{
			if(ele.getText().split(" ")[0].length() == 1)
			{
				System.out.println("Here " + ele.getText().split(" ")[0]);
			}
			tmpList.add(ConvertMonthToInt(ele.getText().split(" ")[0]) + "-" + ele.getText().split(" ")[1]);
		}
		
		return tmpList;
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
	}
	
	public static int ConvertMonthToInt(String monthToConvert) throws ParseException
	{
		Date date = new SimpleDateFormat("MMM").parse(monthToConvert);//put your month name here
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int monthNumber=cal.get(Calendar.MONTH);
	    return cal.get(Calendar.MONTH) + 1;
	}
	
	
	
}
