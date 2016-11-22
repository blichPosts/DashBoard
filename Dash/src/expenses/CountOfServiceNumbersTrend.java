package expenses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.UsageHelper;

public class CountOfServiceNumbersTrend extends BaseClass 
{
	public static List<String> totalExpenseLegends  = new ArrayList<String>(); // this will hold the legend names in the total expanse control.
	public static List<String> countOfServiceNumsLegends  = new ArrayList<String>(); // this will hold the legend names in the count of service numbers control.	
	public static List<WebElement> tempList; // this is for temporary values.
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

	// verify the legends in the control match legends in the 'total expense' control. 
	public static void VerifyLegends()
	{
		errMessage = "Failed test of legends in CountOfServiceNumbersTrend.VerifyLegends"; 
		
		chartId = UsageHelper.getChartId(4); // get dynamic chart id for expenses control.

		 // get the legends from 'Total Expense' pie control and store into totalExpenseLegends list
		 tempList = driver.findElements(By.xpath(".//*[@id='" + chartId +  "']/*/*[@class='highcharts-legend']"));
		 
		 for(WebElement ele : tempList) 
		 {
			 totalExpenseLegends.add(ele.getText());			 
		 }

		 tempList.removeAll(tempList);
		 
		 chartId = UsageHelper.getChartId(0); // get dynamic chart id for count of service numbers control
		 
		 // now get the legends in the count of service numbers control annd put into list
		 tempList = driver.findElements(By.xpath(".//*[@id='" + chartId +  "']/*/*[@class='highcharts-legend']"));
		 
		 for(WebElement ele : tempList)
		 {
			 countOfServiceNumsLegends.add(ele.getText());			 
		 }

		 Assert.assertEquals(countOfServiceNumsLegends, totalExpenseLegends, errMessage);
	}
}
