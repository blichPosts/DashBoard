package expenses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;

public class TotalExpenseByVendorSpendCategory extends BaseClass 
{
 
	public static List<WebElement> eleList = new ArrayList<WebElement>();
	public static List<String> expectedSpendCategoryLegends;
	public static List<String> actualSpendCategoryLegends;
	
	public static String title = "Total Expense by Vendor and Spend Category";
	static String errMessage = "";
	
	
	public static void Setupdata()
	{

		// this gets legends of spend categories text
		//String []  legendsArray= driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[6]")).getText().split("\n");
		//ShowArray(legendsArray);

		// build the list of expected legends.
		actualSpendCategoryLegends.add("Voice");
		actualSpendCategoryLegends.add("Data");
		actualSpendCategoryLegends.add("Messages");
		actualSpendCategoryLegends.add("Roaming");
		actualSpendCategoryLegends.add("Equipment");
		actualSpendCategoryLegends.add("Taxes");
		actualSpendCategoryLegends.add("Other");
		actualSpendCategoryLegends.add("Account");
		

		// build the list of actual legends. 
		eleList = driver.findElements(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[6]"));
		
		for(WebElement ele : eleList)
		{
			actualSpendCategoryLegends.add(ele.getText());
		}
	}
	
	
	
	
	public static void VerifyLegendsTitleAndPieCount() throws InterruptedException
	{
		errMessage = "Failed checks in TotalExpenseByVendorSpendCategory.VerifyLegendsTitleAndPieCount.";
		
		// verify title
		Assert.assertEquals(driver.findElement(By.xpath("(//h3[@class='tdb-h3'])[2]")).getText(), title, errMessage);

		Assert.assertEquals(actualSpendCategoryLegends, expectedSpendCategoryLegends, errMessage);

		
		
		
		
		
		// NOTE - to get at vendor names use this - (//div[@id='highcharts-2']/*/*/*/..)[8]
				
		// this ALSO gets legends of spend categories text
		//legendsArray= driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*)[9]")).getText().split("\n");
		//ShowArray(legendsArray);
		
		// WaitForElementClickable(By.xpath("((//div[@id='highcharts-2']/*/*)[9]/*/*/*)[1]/*[2]"), MediumTimeout, "");
		// driver.findElement(By.xpath("((//div[@id='highcharts-2']/*/*)[9]/*/*/*)[1]/*[2]")).click();
		
		WaitForElementClickable(By.xpath("(//div[@id='highcharts-2']/*/*)[9]/*/*/*/*[text()='Voice']"), MediumTimeout, "");

		driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*)[9]/*/*/*/*[text()='Voice']")).click();
		driver.findElement(By.xpath("(//div[@id='highcharts-2']/*/*)[9]/*/*/*/*[text()='Other']")).click();
		

		// this gets the the sections in the horizontal bar graph. 
		// List<WebElement> eleList =  driver.findElements(By.xpath("(//div[@id='highcharts-2']/*/*/*/..)[5]/*/*/.."));
		
		/*
		for(WebElement ele : eleList)
		{
			//System.out.println(ele.getAttribute("width"));
			//System.out.println(ele.getAttribute("height"));
			System.out.println(ele.getAttribute("visibility"));			
		}
		*/
		
		
		
		
		
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
