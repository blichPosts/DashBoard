package expenses;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.ClassLiteralAccess;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
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
	public static int numberOfLegends = 0;
	
	
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

	public static void SetupChartId()
	{
		chartId = UsageHelper.getChartId(4);
	}
	
	// verify the legends in the control match legends in the 'total expense' control. 
	public static void VerifyLegends()
	{
		errMessage = "Failed test of legends in CountOfServiceNumbersTrend.VerifyLegends"; 
		
		totalExpenseLegends = ExpenseHelper.GetTotalExpenseLegends();
		
		// now get the legends in the count of service numbers control and put into list
		tempList = driver.findElements(By.xpath(".//*[@id='" + chartId +  "']" + ExpenseHelper.partialXpathToLegendsListInControls));
		 
		for(WebElement ele : tempList)
		{
			countOfServiceNumsLegends.add(ele.getText());			 
		}

		Assert.assertEquals(countOfServiceNumsLegends, totalExpenseLegends, errMessage);
		
		numberOfLegends = totalExpenseLegends.size(); // save the number of legends.
	}
	
	// this verifies the months in the control are the same months that are in the months pulldown - need to send xpath to the method.
	public static void VerifyMonths() throws ParseException 
	{
		ExpenseHelper.VerifyMonths(".//*[@id='" + chartId + "']" + ExpenseHelper.partialXpathToMonthListInControls);
	}
	
	// verify number of legends equals number of sections in the bar graphs.
	public static void VerifyNumLegendsMatchNumBarSections()
	{
		errMessage = "Fail verifying number of legands and number of bar chart parts in CountOfServiceNumbersTrend.VerifyNumLegendsMatchNumBarSections";
		
		String cntrlPath = "//div[@id = '" + chartId  + "']" + ExpenseHelper.partialXpathToBarGrapghControls;

		Assert.assertTrue(driver.findElements(By.xpath(cntrlPath)).size() == numberOfLegends, errMessage);
	}
}
