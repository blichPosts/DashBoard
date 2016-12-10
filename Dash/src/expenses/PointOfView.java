package expenses;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.cms.OtherRecipientInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.thoughtworks.selenium.webdriven.WebDriverBackedSelenium;

import Dash.BaseClass;
import helperObjects.CommonTestStepActions;
import helperObjects.CommonTestStepActions.ExpensesViewMode;
import helperObjects.ExpenseHelper;
import helperObjects.ExpenseHelper.controlType;
import helperObjects.UsageHelper;

public class PointOfView extends BaseClass
{
	
	public static List<String> expectedLegendsList = new ArrayList<String>(); // this holds vendor legends in expense control.	
	public static List<String> expectedCountriesList = new ArrayList<String>(); // this holds country legends in expense control.	
	public static List<String> listOfVendorsAdded = new ArrayList<String>(); // this is used by 'VerifyAddingVendors' and 	
	public static List<WebElement> tempWebElementList;
	public static String chartId = "";
	public static String tempUrl = "";
	
	
	public static void StoreAllLegendsInTotalExpense() 
	{
		expectedLegendsList =  ExpenseHelper.GetTotalExpenseLegends();
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
	//   is created when the 'StoreAllLegendsInTotalExpense' method is called from the test suite with all vendors selected. the other legend can't be added
	//   using the point of view.  
	// * each time a vendor is added this will verify the vendor is shown in each control. each vendor that has already been added is 
	//   verified to still be there when a new vendor is selected and verified to exist in each control.
	// * if there was an other legend shown when the  'expectedLegendsList' list was created. verify each control has an 'other' legend. 
	public static void VerifyAddingVendors() throws Exception 
	{
		// add the vendors in the expectedLegendsList, one at a time.
		for(int x = 0; x < expectedLegendsList.size(); x++)
		{
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfVendorsAdded'. it can't be selected in the vendors list.
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
		// this is done by adding the 'other' legend to the 'listOfVendorsAdded' list and making the verify call on each control.
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
		
		if(listOfVendorsAdded != null) // clear listOfVendorsAdded items.
		{
			listOfVendorsAdded.clear();
		}

	}
	
	// this removes all the selected vendors and then adds back the  
	public static void SetupForRemoveVendorsTest() throws Exception
	{
		CommonTestStepActions.UnSelectAllVendors();
		VerifyControlsNotPresent();
		SelectVendors();
	}
	
	// * this will individually select, using the point of view check boxes, each vendor that is in the 'expectedLegendsList' list. the 'expectedLegendsList'
	//   was created when the 'StoreAllLegendsInTotalExpense' method was called from the test suite with all vendors selected. the other legend can't be added
	//   using the point of view.
	// * the vendors selected are added to 'listOfVendorsAdded' list. this list will be used to track removing of vendors.
	public static void SelectVendors() throws Exception
	{
		// add the vendors in the expectedLegendsList, one at a time.
		for(int x = 0; x < expectedLegendsList.size(); x++)
		{
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfVendorsAdded' it can't be selected in the vendors list.
			{
				CommonTestStepActions.SelectSingleVendor(expectedLegendsList.get(x)); // select vendor check box	
				listOfVendorsAdded.add(expectedLegendsList.get(x)); // add selected vendor to the list of vendors selected/added.
			}
		}
	}

	// this un-checks vendors that have been added in the point of view, one at a time. when each one is un-checked it is verified as not being in the control legends.  
	public static void RemoveVendorsAndVerify() throws Exception
	{
		// remove the vendors in the expectedLegendsList, one at a time.
		for(int x = 0; x < expectedLegendsList.size(); x++)
		{
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfVendorsAdded' it can't be selected in the vendors list.
			{
				ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfVendorsAdded);
				CommonTestStepActions.SelectSingleVendor(expectedLegendsList.get(x)); // select vendor check box to remove the vendor that's been added.
				listOfVendorsAdded.remove(listOfVendorsAdded.remove(expectedLegendsList.get(x))); // remove un-checked vendor from listOfVendorsAdded.               

				chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.
				
				ExpenseHelper.tempLocator = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[" + (listOfVendorsAdded.size() + 1) +"]";
				WaitForElementNotVisibleNoThrow(By.xpath(ExpenseHelper.tempLocator), ShortTimeout);
			}
		}
	}
	
	// this un-checks, vendors that have been added, in the point of view. 
	public static void VerifyMonthPulldownSelectionsInControls() throws Exception
	{
		List<WebElement> monthYearList; //  = new ArrayList<WebElement>();
		
		CommonTestStepActions.initializeMonthSelector();
		
		CommonTestStepActions.SelectAllVendors();
		
		for(WebElement ele : CommonTestStepActions.webListPulldown)
		{
			//  ShowText(ele.getText()); // DEBUG.
			CommonTestStepActions.selectMonthYearPulldown(ele.getText());
			WaitForElementVisible(By.xpath("//h1[text()='" +   ele.getText()  + "']"), MediumTimeout);
			Assert.assertEquals(driver.findElement(By.xpath("//h1[text()='" +   ele.getText()  + "']")).getText(), ele.getText(), "");
			WaitForElementVisible(By.xpath("//h2[text()='" +   ele.getText()  + "']"), MediumTimeout);
			Assert.assertEquals(driver.findElement(By.xpath("//h2[text()='" +   ele.getText()  + "']")).getText(), ele.getText(), "");
		}
	}

	// this takes the vendors stored in the expectedLegendsList list and creates a list of corresponding  countries.  
	public static void StoreExpectedCountries()
	{
		ExpenseHelper.SetupCountryAndVendorData(); // populate containers.
		
		for(String str : expectedLegendsList)
		{
			if(!str.equals(ExpenseHelper.otherText))
			{
				expectedCountriesList.add(str);
			}
		}
	}
	
	// ///////////////////////////////////////////// HELPER ////////////////////////////////////////////
	
	public static void ClearContainers()
	{
		if(expectedLegendsList != null)
		{
			expectedLegendsList.removeAll(expectedLegendsList);
		}
	
		if(tempWebElementList != null)
		{
			tempWebElementList.removeAll(expectedLegendsList);
		}
	}
}
