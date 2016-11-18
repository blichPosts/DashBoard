package expenses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.thoughtworks.selenium.webdriven.commands.GetText;

import Dash.BaseClass;

public class TotalExpenseByVendorSpendCategory extends BaseClass 
{
 
	public static List<WebElement> eleList; 
	public static List<String> expectedSpendCategoryLegends = new ArrayList<String>();
	public static List<String> actualSpendCategoryLegends = new ArrayList<String>();
	public static List<String> vendorsList = new ArrayList<String>();
	public static String []  legendsArray;
	public static int numberOfLegendsInBarChart = 0;
	public static int numberOfLegendsInLegendList = 0;	
	public static String otherString = "Other";
	
	public static String title = "Total Expense by Vendor and Spend Category";
	static String errMessage = "";
	
	
	public static void Setupdata()
	{
		// this gets legends of spend categories text
		legendsArray= driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[7]")).getText().split("\n");
		//ShowArray(legendsArray);

		// build the list of expected spend category legends.
		expectedSpendCategoryLegends.add("Voice");
		expectedSpendCategoryLegends.add("Data");
		expectedSpendCategoryLegends.add("Messages");
		expectedSpendCategoryLegends.add("Roaming");
		expectedSpendCategoryLegends.add("Equipment");
		expectedSpendCategoryLegends.add("Taxes");
		expectedSpendCategoryLegends.add("Other");
		expectedSpendCategoryLegends.add("Account");
		
		// this gets the number of items (legends) shown in the bar charts  
		List<WebElement> eleList =  driver.findElements(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[7]/*/*/*"));
		numberOfLegendsInBarChart = eleList.size();
		
		// this gets the expected number of legends.
		numberOfLegendsInLegendList = expectedSpendCategoryLegends.size();
	}
	
	public static void VerifyLegendsTitleAndbarGraphCount() throws InterruptedException
	{
		errMessage = "Failed checks in TotalExpenseByVendorSpendCategory.VerifyLegendsTitleAndPieCount.";
		
		ClearAllContainers();
		
		Setupdata();
		
		// verify title
		Assert.assertEquals(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]")).getText(), title, errMessage);
		
		// verify correct names for legends.
		// used array for actual, had problems with web element list. 
		for(int x = 0; x < expectedSpendCategoryLegends.size(); x++)
		{
			Assert.assertEquals(legendsArray[x], expectedSpendCategoryLegends.get(x));
		}
		
		// verify number of sections in the bar graphs equals number of legends.
		Assert.assertTrue(numberOfLegendsInBarChart == numberOfLegendsInLegendList, errMessage);
		
		// NOTE - to get at vendor names use this - (//div[@id='highcharts-2']/*/*/*/..)[8]
				
		// this selects (clicks) 'voice' and 'other' legends.
		//WaitForElementClickable(By.xpath("(//div[@id='highcharts-2']/*/*)[9]/*/*/*/*[text()='Voice']"), MediumTimeout, "");
		//driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*)[9]/*/*/*/*[text()='Voice']")).click();
		//driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*)[9]/*/*/*/*[text()='Other']")).click();
		
		
		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// this gets the the sections in the horizontal bar graph. 
		// List<WebElement> eleList =  driver.findElements(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[5]/*/*/.."));
		
		// UN-SELECTING LEGENDS AND VERIFYING THEY ARE SELECTED.
		// In web element loop below,the attribute "visibility" will be null if the legend is selected.
		// It will be equal to "hidden" if the legend is not selected it is not selected. 

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// loop through the sections. 
		/*
		for(WebElement ele : eleList)
		{
			//System.out.println(ele.getAttribute("width"));
			//System.out.println(ele.getAttribute("height"));
			System.out.println(ele.getAttribute("visibility"));			
		}
		*/
	}	
	
	public static void VerifyVendorsCountries()
	{
		// clear containers if needed.
		ClearAllContainers();
		
		Setupdata();

		// get the list vendors or countries listed by the bar graph.
		eleList = driver.findElements(By.xpath("(.//*[@id='highcharts-2']/*/*)[11]/*/*"));
		
		for(WebElement ele : eleList) // put vendor names into a list so can use 'contains'. 
		{
			vendorsList.add(ele.getText());
		}
		
		// verify 'other' selection present or not.
		if(eleList.size() < 5)
		{
			Assert.assertFalse(vendorsList.contains(otherString));
		}
		else
		{
			Assert.assertTrue(vendorsList.contains(otherString));
		}
	}
	
	
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	// 										Helpers.
	// //////////////////////////////////////////////////////////////////////////////////////////////////
	public static void ClearAllContainers()
	{
		if(eleList != null)
		{
			eleList.clear();
		}
		
		if(expectedSpendCategoryLegends  != null)
		{
			expectedSpendCategoryLegends.clear();			
		}
		
		if(actualSpendCategoryLegends  != null)
		{
			actualSpendCategoryLegends.clear();			
		}
		
		if(vendorsList  != null)
		{
			vendorsList.clear();			
		}
		
		legendsArray = null;
	}
}
