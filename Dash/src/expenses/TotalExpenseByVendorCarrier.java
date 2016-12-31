package expenses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bouncycastle.eac.EACCertificateBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.sun.jna.StringArray;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.UsageHelper;

public class TotalExpenseByVendorCarrier extends BaseClass
{
	public static String [] legendsArray;
	public static String [] strArray;;
	public static List<String> legendsList = new ArrayList<String>();
	public static List<WebElement> webElementListLegands;
	public static int numPieParts = 0;
	public static int maxNumLegends = 6;	
	public static String otherString = "Other";
	public static String titleVendor = "Total Expense by Vendor";
	public static String titlecountry = "Total Expense by Country";	
	public static String tmpString = "";
	public static String expensePieDateLocator = "(//h2[@class='tdb-h2'])[1]";
	public static String chartId = "";	
	
	// variables for actions
	
	// this holds vendor legend names in expense control (string).
	public static List<String> totalExpenseLegendsList = new ArrayList<String>(); 
	
	// this holds what vendors are currently selected in the expense control legends. 
	public static List<String> totalExpenseExpectedLegendsList = new ArrayList<String>(); 
	
	// this holds the list of the web elements for each expense legend.	
	public static List<WebElement> totalExpenseExpectedLegendsWebList = new ArrayList<WebElement>();   	
	
	// this creates a list that holds the string names of the total expense legends and also creates a list (strings) with the same names that is used in testing.
	public static void StoreAllLegendsInTotalExpense() 
	{
		totalExpenseLegendsList =  ExpenseHelper.GetTotalExpenseLegends(); // get string list of legends in expense control.
		
		ShowListOfStrings(totalExpenseLegendsList);
		
		for(String str : totalExpenseLegendsList) // setup 'totalExpenseExpectedLegendsList' to have each legend name that is in 'totalExpenseLegendsList'.
		{
			totalExpenseExpectedLegendsList.add(str);
		}
		
		// get a web element for each legend in expense control.  
		totalExpenseExpectedLegendsWebList = driver.findElements(By.xpath("//div[@id='" +  ExpenseHelper.chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls));
	}
	
	// * this goes through each legend name in the 'totalExpenseLegendsList' and sends the name and the 'totalExpenseExpectedLegendsWebList' web element list 
	//   to method 'ExpenseHelper.SelectLegendByText'.
	// 
	// 
	public static void VerifySelectingLegendsAddSlices() throws Exception
	{

		ShowListOfStrings(totalExpenseExpectedLegendsList);
		
		// first verify all the pie slices are present.
		ExpenseHelper.SelectLegendByTextAndDoVerification(totalExpenseExpectedLegendsWebList, "", totalExpenseExpectedLegendsList);
		
		// now go through each legend and select it one at a time.
		for(String str : totalExpenseLegendsList)
		{
			totalExpenseExpectedLegendsList.remove(str);
			ExpenseHelper.SelectLegendByTextAndDoVerification(totalExpenseExpectedLegendsWebList, str, totalExpenseExpectedLegendsList); 
		}
	}
	
	
	
	public static void setAllLegendsAndPieCount() throws InterruptedException
	{
		
		if(webElementListLegands != null) // clear if already has contents.
		{
			webElementListLegands.clear();			
		}

		if(legendsList != null) // clear if already has contents.
		{
			legendsList.clear();			
		}
		
		// get current chart Id for expense control. NOTE - this is global to this class and is set here
		chartId = UsageHelper.getChartId(0); 

		// get the legends in the expense control.
		legendsList = ExpenseHelper.GetTotalExpenseLegends();
		
		// show Ana
		// String temp = ".//*[@id='" +  chartId + "']/*/*[@class='highcharts-series-group ']"; // nope -- this works in fire bug.
		// String temp = ".//*[@id='" +  chartId + "']/*/*[@class='highcharts-series-group']"; // yes -- works in selenium code.
		
		numPieParts = driver.findElements(By.xpath("//div[@id='" +  chartId + "']/*/*[@class='highcharts-series-group']/*/*")).size();		
	}
	
	public static void VerifyLegendsTitleAndPieCount() throws InterruptedException
	{
		String errMessage = "Failed checks in TotalExpenseByVendorCarrier.VerifyLegendsAndPieCount";
		
		setAllLegendsAndPieCount();

		Assert.assertTrue(legendsList.size() == numPieParts); // the number of legends should equal the number of parts in the expense pie. 
		
		if(numPieParts <= 5)
		{
			Assert.assertFalse(legendsList.contains(otherString), errMessage); // verify other legend is not there.			
		}
		else
		{
			Assert.assertTrue(legendsList.contains(otherString), errMessage); // verify other legend is there.
			Assert.assertTrue(numPieParts == maxNumLegends, errMessage); // verify no more than max number of legends are shown.
		}
	}
	
	// verify title and month/year are correct - vendor.
	public static void VerifyVendorView()
	{
		String errMessage = "Failed check for vendor text in TotalExpenseByVendorCarrier.VerifyVendorView.";
		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[@class='tdb-h3']")).getText(), titleVendor, errMessage);
		Assert.assertEquals(CommonTestStepActions.GetPulldownTextSelected(), driver.findElement(By.xpath(expensePieDateLocator)).getText(), errMessage);		
	}
	
	// verify title and month/year are correct - country.
	public static void VerifyCountryView() throws Exception
	{
		String errMessage = "Failed check for country text in TotalExpenseByVendorCarrier.VerifyCountryView.";
		
		WaitForElementVisible(By.xpath("//span[text()='Country']"), MainTimeout);
		
		Assert.assertEquals(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[1]")).getText(), titlecountry, errMessage);
		Assert.assertEquals(CommonTestStepActions.GetPulldownTextSelected(), driver.findElement(By.xpath(expensePieDateLocator)).getText(), errMessage);
	}	

	public static String ConvertMonthToInt(String monthToConvert) throws ParseException
	{
		Date date = new SimpleDateFormat("MMM").parse(monthToConvert);
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    return String.valueOf(cal.get(Calendar.MONTH) + 1);
	}
}
