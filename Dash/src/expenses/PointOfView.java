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
	public static List<String> expectedCountriesList = new ArrayList<String>(); // this holds country legends in expense control. not currently used.
	public static List<String> listOfItemsAdded = new ArrayList<String>(); // this is used by 'VerifyAddingVendors' and 	
	public static List<WebElement> tempWebElementList;
	public static String chartId = "";
	public static String tempUrl = "";
	public static String tempString = "";
	
	
	public static void StoreAllLegendsInTotalExpense() 
	{
		expectedLegendsList =  ExpenseHelper.GetTotalExpenseLegends();
	}

	// this verifies that each control is not visible by looking for the first legend in each control being not visible.
	public static void VerifyControlsNotPresent() throws Exception
	{
		chartId =  UsageHelper.getChartId(0); // total expenses. 
		tempUrl = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[1]/*";		
		Assert.assertTrue(WaitForElementNotVisibleNoThrow(By.xpath(tempUrl), MediumTimeout));
		
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
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfItemsAdded'. it can't be selected in the vendors list.
			{
				CommonTestStepActions.SelectSingleVendor(expectedLegendsList.get(x)); // select vendor check box	
				listOfItemsAdded.add(expectedLegendsList.get(x)); // add selected vendor to the list of vendors selected/added.
				
				// this section verifies the newly added legend and any legends that have already been added for each control type.
				ExpenseHelper.VerifyControlLegends(controlType.totalExpense, listOfItemsAdded); 
				ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfItemsAdded); 
				ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);
				ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfItemsAdded);				
				ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfItemsAdded);
			}
		}
		
		// if more than the max number of legends were found in the 'expectedLegendsList' list, verify each control contains an 'other' legend.
		// this is done by adding the 'other' legend to the 'listOfVendorsAdded' list and making the verify call on each control.
		if(expectedLegendsList.size() > ExpenseHelper.maxNumberOfLegends)
		{
			CommonTestStepActions.SelectAllVendors(); // select all vendors to get to the state where the 'expectedLegendsList' list was created.
			
			// add the 'other' legend to 'listOfVendorsAdded' list
			listOfItemsAdded.add(ExpenseHelper.otherText);
			
			ExpenseHelper.VerifyControlLegends(controlType.totalExpense, listOfItemsAdded);
			ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfItemsAdded); 
			ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);
			ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfItemsAdded);				
			ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfItemsAdded);
		}
		
		if(listOfItemsAdded != null) // clear listOfVendorsAdded items.
		{
			listOfItemsAdded.clear();
		}
	}
	
	// * this will individually select, using the point of view check boxes, each vendor that is in the 'expectedLegendsList' list. the 'expectedLegendsList'
	//   is created when the 'StoreAllLegendsInTotalExpense' method is called from the test suite with all vendors selected. the other legend can't be added
	//   using the point of view.  
    //
	// * each time a vendor is added this will verify the vendor/country is shown in each control. each vendor/country that has already been added is 
	//   verified to still be there when a new vendor is selected and verified to exist in each control.
    //	
	// * if there was an other legend shown when the 'expectedLegendsList' list was created. verify each control has an 'other' legend.
    //
	// * NOTE: this is meant to work for the vendor and country view. The enum passed in tells which view is being tested.
	// 
	public static void VerifyAddingVendorsTwo(ExpensesViewMode viewMode) throws Exception 
	{
		// add the vendors/countries in the expectedLegendsList, one at a time, by selecting the vendors check box.
		for(int x = 0; x < expectedLegendsList.size(); x++)
		{
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfItemsAdded'. it can't be selected in the vendors list.
			{
				CommonTestStepActions.SelectSingleVendor(expectedLegendsList.get(x)); // select vendor check box	
				
				if(viewMode == ExpensesViewMode.country) // this section makes sure a country is only added to the 'listOfItemsAdded' list once.  
				{
					tempString = ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x)); // get country for the vendor selected

					if(!listOfItemsAdded.contains(tempString)) // add country to the list if country is not already on the list. 
					{
						listOfItemsAdded.add(tempString);
					}
				}
				else
				{
					listOfItemsAdded.add(expectedLegendsList.get(x)); // add selected vendor to the list of vendors selected/added.					
				}
				
				// this section verifies the newly added legend and any legends that have already been added for each control type.
				ExpenseHelper.VerifyControlLegends(controlType.totalExpense, listOfItemsAdded); 
				ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfItemsAdded); 
				ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);
				ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfItemsAdded);				
				ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfItemsAdded);
			}
		}
		
		// if more than the max number of legends were found in the 'expectedLegendsList' list, verify each control contains an 'other' legend.
		// this is done by adding the 'other' legend to the 'listOfVendorsAdded' list and making the verify call on each control.
		if(expectedLegendsList.size() > ExpenseHelper.maxNumberOfLegends && viewMode == ExpensesViewMode.vendor)
		{
			CommonTestStepActions.SelectAllVendors(); // select all vendors to get to the state where the 'expectedLegendsList' list was created.
			
			// add the 'other' legend to 'listOfVendorsAdded' list
			listOfItemsAdded.add(ExpenseHelper.otherText);
			
			ExpenseHelper.VerifyControlLegends(controlType.totalExpense, listOfItemsAdded);
			ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfItemsAdded); 
			ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);
			ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfItemsAdded);				
			ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfItemsAdded);
		}
		
		
		if(listOfItemsAdded != null) // clear listOfItemsAdded items.
		{
			listOfItemsAdded.clear();
		}
		
	}	
	
	// this removes all the selected vendors and then adds back the  
	public static void SetupForRemoveVendorsTest() throws Exception
	{
		CommonTestStepActions.SelectAllVendors();
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
				listOfItemsAdded.add(expectedLegendsList.get(x)); // add selected vendor to the list of vendors selected/added.
			}
		}
	}

	// this un-checks vendors that have been added in the point of view, one at a time. when each vendor is un-checked it is verified as not being in the control legends.
	// the legends that are expected to be remaining are also checked.
	public static void RemoveVendorsAndVerify() throws Exception
	{
		
		// remove the vendors in the expectedLegendsList, one at a time.
		for(int x = 0; x < expectedLegendsList.size(); x++)
		{
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfVendorsAdded' it can't be selected in the vendors list.
			{
				ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);
				ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfItemsAdded);
				ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);				
				ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfItemsAdded);
				ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfItemsAdded);				
				
				CommonTestStepActions.SelectSingleVendor(expectedLegendsList.get(x)); // select vendor check box to remove the vendor that's been added.
				listOfItemsAdded.remove(listOfItemsAdded.remove(expectedLegendsList.get(x))); // remove un-checked vendor from listOfVendorsAdded.               

				chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.
				
				ExpenseHelper.tempLocator = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[" + (listOfItemsAdded.size() + 1) +"]";
				WaitForElementNotVisibleNoThrow(By.xpath(ExpenseHelper.tempLocator), ShortTimeout);
			}
		}		

	}
	
	// this un-checks vendors that have been added in the point of view, one at a time. when each vendor is un-checked it is verified as not being in the control legends.
	// the legends that are expected to be remaining are also checked.
	public static void RemoveVendorsAndVerifyTwo(ExpensesViewMode viewMode) throws Exception
	{
		ShowInt(listOfItemsAdded.size());
	
		switch(viewMode)
		{
			case vendor:
			{
				// remove the vendors in the expectedLegendsList, one at a time.
				for(int x = 0; x < expectedLegendsList.size(); x++)
				{
					if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfVendorsAdded' it can't be selected in the vendors list.
					{
						ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);
						ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfItemsAdded);
						ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);				
						ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfItemsAdded);
						ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfItemsAdded);				
						
						CommonTestStepActions.SelectSingleVendor(expectedLegendsList.get(x)); // select vendor check box to remove the vendor that's been added.
						listOfItemsAdded.remove(listOfItemsAdded.remove(expectedLegendsList.get(x))); // remove un-checked vendor from listOfVendorsAdded.               

						chartId = UsageHelper.getChartId(0); // get current chart Id for expense control.
						
						ExpenseHelper.tempLocator = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[" + (listOfItemsAdded.size() + 1) +"]";
						WaitForElementNotVisibleNoThrow(By.xpath(ExpenseHelper.tempLocator), ShortTimeout);
					}
				}		
				break;
			}
		
			case country:
			{
				for(int x = 0; x < expectedLegendsList.size(); x++)
				{
					if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText))
					{
						ShowText(ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x)));
					}
				}
				break;
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

	// NOTE: Not currently used. May need in future.
	// this takes the vendors stored in the 'expectedLegendsList' list and creates a list of corresponding countries called 'expectedCountriesList'.
	// the 'expectedCountriesList' will hold the corresponding country for each vendor in the 'expectedLegendsList'.  
	public static void StoreExpectedCountryLegendsFromExpectedVendorLegends()
	{
		ExpenseHelper.SetupCountryAndVendorData(); // populate container that holds country objects.
		
		for(String str : expectedLegendsList)
		{
			if(!str.equals(ExpenseHelper.otherText))
			{
				if(!expectedCountriesList.contains(ExpenseHelper.GetCountryForVendor(str)))
				{
					expectedCountriesList.add(ExpenseHelper.GetCountryForVendor(str));
				}
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
