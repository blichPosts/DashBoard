package expenses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;


public class TotalExpensesTrend extends BaseClass 
{

	public static List<WebElement> webEleList;
	public static List<String> expectedTrends = new ArrayList<String>();
	public static List<String> actualTrends = new ArrayList<String>();

	public static String mainTitle = "Expense Trending"; 
	public static String vendorTitle = "Expense by Vendor - All Categories";
	
	
	public static void Setupdata()
	{
		// get the actual trend selections.
		webEleList = driver.findElements(By.cssSelector(".tdb-boxSelector.tdb-align--right > div"));
 
		// load the actual trend selections
		for(WebElement ele : webEleList)
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
	
	public static void VerifyTrends()
	{
		ClearAllContainers();
		Setupdata();
		
		Assert.assertEquals(actualTrends, expectedTrends, "");
	}

	public static void VerifyTitlesVendorView()
	{
	
		System.out.println(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(1)")).getText());		
		
		
		
		// .tdb-card > h3:nth-of-type(1) > span
	
	
	
	}

	
	// .tdb-card > h3:nth-of-type(1) > span
	
	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helpers.
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	public static void ClearAllContainers()
	{
		if(webEleList != null)
		{
			webEleList.clear();
		}

		if(expectedTrends != null)
		{
			expectedTrends.clear();
		}
	}
}