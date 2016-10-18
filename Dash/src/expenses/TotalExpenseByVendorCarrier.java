package expenses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.sun.jna.StringArray;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;

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
	
	public static void setAllLegendsAndPieCount()
	{
		// store all legend names into web element list.
		webElementListLegands = driver.findElements(By.xpath("//div[@id='highcharts-0']/*/*[@class='highcharts-legend']/*/*/*[@class='highcharts-legend-item']"));
		
		// for(WebElement ele : webElementListLegands ){System.out.println(ele.getText());} // DEBUG
		
		// store number of sections in the pie.
		numPieParts = driver.findElements(By.xpath(".//*[@id='highcharts-0']/*/*/*[@class='highcharts-series highcharts-series-0 highcharts-tracker']/*")).size();

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
			
		}
		else
		{
			Assert.assertTrue(legendsList.contains(otherString), errMessage); // verify other legend is there.
			Assert.assertTrue(numPieParts == maxNumLegends, errMessage); // assert max legends shown.
		}
	}
	
	// verify title and month/year are correct.
	public static void VerifyVendorView()
	{
		String errMessage = "Failed check for Total Expanse title in TotalExpenseByVendorCarrier.VerifyVendorView";
		
		Assert.assertEquals(driver.findElement(By.xpath("//h3[@class='tdb-h3']")).getText(), titleVendor, errMessage);
		Assert.assertEquals(CommonTestStepActions.GetPulldownTextSelected(), driver.findElement(By.cssSelector(".tdb-h2")).getText(), errMessage);
	}
}
