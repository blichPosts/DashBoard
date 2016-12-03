package expenses;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.cms.OtherRecipientInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.UsageHelper;

public class PointOfView extends BaseClass
{
	
	public static List<String> expectedLegendsList = new ArrayList<String>();
	public static List<String> listOfVendorsForSelection = new ArrayList<String>();
	public static List<WebElement> tempWebElementList;
	public static String chartId = "";
	public static String tempUrl = "";

	
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

	// this verifies that each control is not visible by looking for the first legend in each control being not visible.
	public static void VerifyControlsNotPresent() throws Exception
	{
		chartId =  UsageHelper.getChartId(0); // total expenses. 
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), ShortTimeout));
		
		chartId =  UsageHelper.getChartId(1); // total expense vendor spend.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathForLegendsInTotalSpendCategory + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		
		chartId =  UsageHelper.getChartId(2); // expense trending.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		
		chartId =  UsageHelper.getChartId(3); // cost per service number.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
		
		chartId =  UsageHelper.getChartId(4); // cost per service number.
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), TinyTimeout));
	}

	// * this will individually select, using the point of view check boxes, each vendor that is in the 'expectedLegendsList' list. the 'expectedLegendsList'
	//   is created when the 'StoreAllLegendsInTotalExpense' method is called from the test suite with all vendors selected.   
	// * each time a vendor is added this will verify the vendor is shown in each control. each vendor that has already been added is 
	//   verified to still be there when a new vendor is selected and verified to exist in each control.
	// * if there was an other legend shown when the  'expectedLegendsList' list was created. verify each control has an 'other' legend. 
	public static void VerifyAddingVendors() throws Exception 
	{
		List<String> listOfVendorsAdded = new ArrayList<String>();
		
		// add the vendors in the expectedLegendsList, one at a time.
		for(int x = 0; x < expectedLegendsList.size(); x++)
		{
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to ----------------------- FINISH.
			{
				CommonTestStepActions.SelectSingleVendor(expectedLegendsList.get(x)); // select vendor check box	
				listOfVendorsAdded.add(expectedLegendsList.get(x)); // add selected vendor to the list of vendors selected/added.
				
				// this section verifies the newly added legend and any legends that have already been added for each control type.
				ExpenseHelper.VerifyControlLegends(controlType.totalExpense, listOfVendorsAdded); 
				ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfVendorsAdded); 
				ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfVendorsAdded);
				ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfVendorsAdded);				
				ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfVendorsAdded);
			}
		}
		
		// if more than the max number of legends were found in the 'expectedLegendsList' list, verify each control contains an 'other' legend.
		// this is done by adding the 'other' legend to the 'listOfVendorsAdded' list and making the 
		if(expectedLegendsList.size() > ExpenseHelper.maxNumberOfLegends)
		{
			CommonTestStepActions.SelectAllVendors(); // select all vendors to get to the state where the 'expectedLegendsList' list was created.
			
			// add the 'other' legend to 'listOfVendorsAdded' list
			listOfVendorsAdded.add(ExpenseHelper.otherText);
			ExpenseHelper.VerifyControlLegends(controlType.totalExpense, listOfVendorsAdded);
			ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfVendorsAdded); 
			ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfVendorsAdded);
			ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfVendorsAdded);				
			ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfVendorsAdded);
		}
		
		if(listOfVendorsAdded != null) // remove listOfVendorsAdded items.
		{
			listOfVendorsAdded.clear();
		}
		
		listOfVendorsAdded = null;
		
		
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
