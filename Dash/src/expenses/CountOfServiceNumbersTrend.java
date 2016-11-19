package expenses;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;

public class CountOfServiceNumbersTrend extends BaseClass 
{
	public static String vendorTitle =  "Count of Service Numbers by Vendor";
	public static String countryTitle =  "Count of Service Numbers by Country";
	public static String titleXpath = "//h3[starts-with(text(), 'Count of Service Numbers')]";
	public static String errMessage = "";
	public static String chartId = "";	
	
	public static void VerifyTitle(CommonTestStepActions.ExpensesViewMode mode)
	{
		errMessage = "Fail in test for title in CountOfServiceNumbersTrend.VerifyTitle";
		switch(mode)
		{
			case vendor:
			{
				Assert.assertEquals(driver.findElement(By.xpath("(//h3[starts-with(text(), 'Count of Service Numbers')])[2]")).getText(), vendorTitle, errMessage);
				break;
			}
			case country:
			{
				Assert.assertEquals(driver.findElement(By.xpath("//h3[starts-with(text(), 'Count of Service Numbers')]")).getText(), countryTitle, errMessage);
				break;				
			}
		}
	}

	public static void VerifyLegends()
	{
		// get the legends from 'Total Expense' pie control.
		// .//*[@id='highcharts-0']/*/*[@class='highcharts-legend']/*/*/*
		
		 chartId = UsageHelper.getChartId(0);
		 
		 List<WebElement> eleList = driver.findElements(By.xpath(".//*[@id='" + chartId +  "']/*/*[@class='highcharts-legend']/*/*/*/*"));
		 
		 for(WebElement ele : eleList)
		 {
			 System.out.println(ele.getText().replaceAll("\\r\\n", "").trim());			 
		 }
	}

}
