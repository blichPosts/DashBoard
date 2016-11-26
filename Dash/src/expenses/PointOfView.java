package expenses;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;
import helperObjects.ExpenseHelper;

public class PointOfView extends BaseClass
{
	
	public static List<String> expectedLegendsList = new ArrayList<String>();
	public static List<String> listOfVendorsForSelection = new ArrayList<String>();
	public static List<WebElement> tempWebElementList;
	
	public static void StoreAllLegendsInTotalExpense()
	{
		expectedLegendsList =  ExpenseHelper.GetTotalExpenseLegends();
	}
	
	public static void BuildListOfVendorsForVendorSelection() throws InterruptedException
	{
		tempWebElementList = driver.findElements(By.cssSelector(".md-checkbox-label"));
		for(WebElement ele : tempWebElementList)
		{
			listOfVendorsForSelection.add(ele.getText());
		}
	}
	
	
	public static void ClickVendor() throws InterruptedException
	{

		DebugTimeout(3, "three");
		
		
		driver.findElement(By.xpath("(//div[@class='md-checkbox-inner-container'])[3]")).click();		
		
		DebugTimeout(9999, "Freeze");
		
		
		
		// (//div[@class='md-checkbox-inner-container'])[3]
		
	}

	// ///////////////////////////////////////////// HELPER ////////////////////////////////////////////
	
	public static void ClearContainers()
	{
		if(expectedLegendsList != null)
		{
			expectedLegendsList.removeAll(expectedLegendsList);
		}
		
		if(listOfVendorsForSelection  != null)
		{
			listOfVendorsForSelection .removeAll(expectedLegendsList);
		}
	
		if(tempWebElementList != null)
		{
			tempWebElementList.removeAll(expectedLegendsList);
		}
	}
}
