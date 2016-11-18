package expenses;

import org.openqa.selenium.By;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;

public class CountOfServiceNumbersTrend extends BaseClass 
{
	public static String vendorTitle =  "Count of Service Numbers by Vendor";
	public static String countryTitle =  "Count of Service Numbers by Country";
	public static String titleXpath = "//h3[starts-with(text(), 'Count of Service Numbers')]";
	public static String errMessage = "";
	
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
}
