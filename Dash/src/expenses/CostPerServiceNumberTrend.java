package expenses;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.ExpenseHelper;
import helperObjects.UsageHelper;

public class CostPerServiceNumberTrend extends  BaseClass
{
	
	public static String chartId = "";
	public static String vendorTitle = "Cost per Service Number by Vendor - All Categories";
	public static String countryTitle = "Cost per Service Number by Country - All Categories";	
	
	public static List<String> totalExpenseLegends  = new ArrayList<String>(); // this will hold the legend names in the total expanse control.
	public static List<String> countOfServiceNumsLegends  = new ArrayList<String>(); // this will hold the legend names in the count of service numbers control.	
	public static List<WebElement> tempList; // this is for temporary values.
	public static String titleXpath = "//h3[starts-with(text(), 'Count of Service Numbers')]";
	public static String errMessage = "";
	public static int numberOfLegends = 0;
	
	// this has a wait for element visible in each case statement. this is needed because this can be the first method called after switching to the country view.
	// there is a wait in the vendor view  also as a redundant check.
	public static void VerifyTitle(CommonTestStepActions.ExpensesViewMode mode) throws Exception
	{
		errMessage = "Fail in test for title in CountOfServiceNumbersTrend.VerifyTitle";
		switch(mode)
		{
			case vendor:
			{
				WaitForElementVisible(By.xpath("//h3/span[text()='Vendor']"), MediumTimeout);
				Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(2)")).getText(), vendorTitle, errMessage);
				break;
			}
			case country:
			{
				WaitForElementVisible(By.xpath("//h3/span[text()='Country']"), MediumTimeout);
				Assert.assertEquals(driver.findElement(By.cssSelector(".tdb-card > h3:nth-of-type(2)")).getText(), countryTitle, errMessage);
				break;				
			}
		}
	}
	
	// verify the legends in the control match legends in the 'total expense' control. 
	public static void VerifyLegends() throws Exception
	{
		errMessage = "Failed test of legends in CostPerServiceNumberTrend.VerifyLegends"; 
		
		totalExpenseLegends = ExpenseHelper.GetTotalExpenseLegends();
		
		ClearContainers(); 
		
		// now get the legends in the count of service numbers control and put into list
		tempList = driver.findElements(By.xpath(".//*[@id='" + chartId +  "']" + ExpenseHelper.partialXpathToLegendsListInControls));
		
		// System.out.println(tempList.size());
		// for(WebElement ele: tempList) {System.out.println(ele.getText());} // DEBUG

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
		errMessage = "Fail verifying number of legands and number of bar chart parts in CostPerServiceNumberTrend.VerifyNumLegendsMatchNumBarSections";
		
		String cntrlPath = "//div[@id = '" + chartId  + "']" + ExpenseHelper.partialXpathToBarGrapghControls;

		Assert.assertTrue(driver.findElements(By.xpath(cntrlPath)).size() == numberOfLegends, errMessage);
	}	
	
	
	public static void SetupChartId()
	{
		chartId = UsageHelper.getChartId(3);
	}

	// //////////////////////////////// HELPER ///////////////////////////////////////////////////////////
	
	public static void ClearContainers()
	{
		if(countOfServiceNumsLegends !=  null)
		{
			countOfServiceNumsLegends.removeAll(countOfServiceNumsLegends);
		}		
	}	
}
