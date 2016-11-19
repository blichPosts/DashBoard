package expenses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.sun.jna.StringArray;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
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
	
	public static void setAllLegendsAndPieCount()
	{
		
		if(webElementListLegands != null) // clear if already has contents.
		{
			webElementListLegands.clear();			
		}

		if(legendsList != null) // clear if already has contents.
		{
			legendsList.clear();			
		}
		
		chartId = UsageHelper.getChartId(0); // get current chart Id.
				
		// store all legend names into web element list.
		// webElementListLegands = driver.findElements(By.xpath("//div[@id='highcharts-0']/*/*[@class='highcharts-legend']/*/*/*[@class='highcharts-legend-item']"));
		webElementListLegands = driver.findElements(By.xpath("//div[@id='" +  chartId + "']/*/*[@class='highcharts-legend']/*/*/*"));		
		
		
		int zz = driver.findElements(By.xpath("//div[@id='" +  chartId + "']/*/*[@class='highcharts-legend']/*/*/*")).size();
		System.out.println("Show number of legends = " + zz);
		
		
		System.out.println("Show legends list");
		for(WebElement ele : webElementListLegands ){System.out.println(ele.getText());} // DEBUG
		
		// store number of sections in the pie.
		// numPieParts = driver.findElements(By.xpath("//div[@id='" +  chartId + "'][@class='highcharts-series highcharts-series-0 highcharts-tracker']/*")).size();
		List<WebElement> myList = driver.findElements(By.xpath("//div[@id='" +  chartId + "']/*/*[@class='highcharts-series-group ']/*/*[contains(@class,'highcharts-point highcharts-color')]"));
		
		System.out.println("Parts " + myList.size());
		for(WebElement ele : myList ){System.out.println(ele.getAttribute("class"));}		
		
		
		
		// store legend names into legend list.
		for(WebElement ele : webElementListLegands )
		{
			legendsList.add(ele.getText());
		} 
	}
	
	public static void VerifyLegendsTitleAndPieCount()
	{
		String errMessage = "Failed checks in TotalExpenseByVendorCarrier.VerifyLegendsAndPieCount";
		
		setAllLegendsAndPieCount();
		
		if(numPieParts <= 5)
		{
			Assert.assertFalse(legendsList.contains(otherString), errMessage); // verify other legend is not there.			
		}
		else
		{
			Assert.assertTrue(legendsList.contains(otherString), errMessage); // verify other legend is there.
			Assert.assertTrue(numPieParts == maxNumLegends, errMessage); // assert max legends shown.
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
