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
		
		Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText(), vendorTitle, errMessage);
		
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='tdb-card'])[2]/h2")).getText(), mainTitle, errMessage);
	}
	
	public static void VerifyMonths() throws ParseException
	{
		// get expected month(int)/years pulldown - format is MM-YYYY.
		expectedYearMonthList = YearMonthIntergerFromPulldownTwoDigitYear();
		
		
		// get actual month(int)/years pulldown
		webEleListMonthYearActual = driver.findElements(By.xpath(".//*[@id='highcharts-4']/*/*[@class='highcharts-axis-labels highcharts-xaxis-labels']/*/*"));
		
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
	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helpers.
	// //////////////////////////////////////////////////////////////////////////////////////////////////

	public static List<String> YearMonthIntergerFromPulldown() throws ParseException
	{
		List<String> tmpList = new ArrayList<String>();
		
		// get the year/month items from the pulldown and create a list of of month (integer) with year.  
		for( WebElement ele : CommonTestStepActions.webListPulldown)
		{
			// this converts the month to an integer. and then appends a dash and the year.
			// final format is like this: '6-2016' or '11-2016'.
			tmpList.add(ConvertMonthToInt(ele.getText().split(" ")[0]) + "-" + ele.getText().split(" ")[1]); 
		}
		
		return tmpList;
	}
	
	
	// return month/year from pulldown in this format MM-YYYY.  
	public static List<String> YearMonthIntergerFromPulldownTwoDigitYear() throws ParseException
	{
		List<String> tmpList = new ArrayList<String>();
		String tmpMonthInt = "";
		
		// get the year/month items from the pulldown and create a list of of month (integer) with year.  
		for( WebElement ele : CommonTestStepActions.webListPulldown)
		{
			// this converts the month to an integer string and then appends a dash and the year string.
			// final format is like this: '06-2016' or '11-2016' - YY-MMMM.
			
			// get the month.
			tmpMonthInt = ConvertMonthToInt(ele.getText().split(" ")[0]);
			
			// if the month has only one string integer, add a leading zero.
			if(tmpMonthInt.length() == 1)
			{
				tmpMonthInt = "0" + tmpMonthInt;
			}
			
			// add integer month and as string and string year to list to be returned.
			tmpList.add(tmpMonthInt + "-" + ele.getText().split(" ")[1]); 
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
	
	/*
	public static int ConvertMonthToInt(String monthToConvert) throws ParseException
	{
		Date date = new SimpleDateFormat("MMM").parse(monthToConvert);//put your month name here
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int monthNumber=cal.get(Calendar.MONTH);
	    return cal.get(Calendar.MONTH) + 1;
	}
	*/
	
	public static String ConvertMonthToInt(String monthToConvert) throws ParseException
	{
		Date date = new SimpleDateFormat("MMM").parse(monthToConvert);//put your month name here
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int monthNumber=cal.get(Calendar.MONTH);
	    return String.valueOf(cal.get(Calendar.MONTH) + 1);
	}
	
	
}
