package expenses;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	public static List<String> expectedCountriesList = new ArrayList<String>(); // this holds the country associated with each 
	public static List<String> expectedCountriesListUnigue = new ArrayList<String>(); //
	public static List<String> listOfItemsAdded = new ArrayList<String>(); // this is used by 'VerifyAddingVendors' and 	
	public static List<WebElement> tempWebElementList;
	public static String chartId = "";
	// public static String tempUrl = "";
	public static String tempString = "";
	
	
	public static void StoreAllLegendsInTotalExpense() 
	{
		expectedLegendsList =  ExpenseHelper.GetTotalExpenseLegends();
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
				CommonTestStepActions.selectOneVendor(expectedLegendsList.get(x)); // select vendor check box	
				
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
		ExpenseHelper.VerifyControlsNotPresent();
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
				CommonTestStepActions.selectOneVendor(expectedLegendsList.get(x)); // select vendor check box	
				listOfItemsAdded.add(expectedLegendsList.get(x)); // add selected vendor to the list of vendors selected/added.
			}
		}
	}
	
	// this un-checks vendors that have been added in the point of view, one at a time. when each vendor is un-checked it is verified as not being in the control legends.
	// the legends that are expected to be remaining are also checked.
	public static void RemoveVendorsAndVerifyTwo(ExpensesViewMode viewMode) throws Exception
	{
		ExpenseHelper.SetWaitShort(); // override the default because of wait for no element.
		
		switch(viewMode)
		{
			case vendor:
			{
				// remove the vendors in the expectedLegendsList, one at a time.
				for(int x = 0; x < expectedLegendsList.size(); x++)
				{
					if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'listOfVendorsAdded' it can't be selected in the vendors list.
					{
						ExpenseHelper.VerifyControlLegends(controlType.totalExpense, listOfItemsAdded);
						ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, listOfItemsAdded);
						ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, listOfItemsAdded);				
						ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, listOfItemsAdded);
						ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, listOfItemsAdded);				
						
						CommonTestStepActions.selectOneVendor(expectedLegendsList.get(x)); // select vendor check box to remove the vendor that's been added.
						listOfItemsAdded.remove(listOfItemsAdded.remove(expectedLegendsList.get(x))); // remove un-checked vendor from listOfVendorsAdded.               

						// this section waits for the removed item to show up removed in the first control.  
						chartId = UsageHelper.getChartId(0); 
						ExpenseHelper.tempLocator = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[" + (listOfItemsAdded.size() + 1) +"]";
						WaitForElementNotVisibleNoThrow(By.xpath(ExpenseHelper.tempLocator), ShortTimeout);
					}
				}		
				break;
			}
		
			case country:
			{
				//   Method BuildCountryListsFromVendors():
				// * store the country name for each vendor. the list that holds the country names, 'expectedCountriesList', can have the same 
				//   country name listed more than once and can also contain the 'other' legend.   
				// * store the unique country names for the vendors. the list that holds the country names, 'expectedCountriesListUnique' has the 
				//   unique countries associated with the the vendors in 'expectedLegendsList', 
				BuildCountryListsFromVendors();  
				
				// go through the 'expectedLegendsList'that has all of the vendor/legend names and un-select each one (except 'other' if it exists). 
				for(int x = 0; x < expectedLegendsList.size(); x++)
				{
					if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText)) // the 'other' legend is not added to 'expectedLegendsList' because it can't be selected in the POV vendors list.					
					{

						// verify the country legends in all controls.
						ExpenseHelper.VerifyControlLegends(controlType.totalExpense, expectedCountriesListUnigue); 
						ExpenseHelper.VerifyControlLegends(controlType.totalExpenseSpendCatergory, expectedCountriesListUnigue);						
						ExpenseHelper.VerifyControlLegends(controlType.expenseTrending, expectedCountriesListUnigue); 						
						ExpenseHelper.VerifyControlLegends(controlType.costPerServiceNumber, expectedCountriesListUnigue); 
						ExpenseHelper.VerifyControlLegends(controlType.countOfServiceNumbers, expectedCountriesListUnigue);						
						
						CommonTestStepActions.selectOneVendor(expectedLegendsList.get(x)); // un-select the vendor.
						
						// remove the country associated with the vendor from the  'expectedCountriesList'.
						expectedCountriesList.remove(ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x))); 
						
						// if the country removed above is no longer present in the expectedCountriesList, this means it can be removed
						// from the 'expectedCountriesListUnigue' list. the 'expectedCountriesListUnigue' is used to verify expected country legends.  
						if(!expectedCountriesList.contains(ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x))))
						{
							// ShowText("Remove "  +  ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x))); // DEBUG
							expectedCountriesListUnigue.remove(ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x)));
						}

						// this section waits for the removed item to show up removed in the expense control.  
						chartId = UsageHelper.getChartId(0); 
						ExpenseHelper.tempLocator = "(//div[@id='" +  chartId + "']" + ExpenseHelper.partialXpathToLegendsListInControls + ")[" + (expectedCountriesListUnigue.size() + 1) +"]";
						WaitForElementNotVisibleNoThrow(By.xpath(ExpenseHelper.tempLocator), ShortTimeout);
					}
				}

				break;
			}
		}
		ExpenseHelper.SetWaitDefault(); // set back to default
	}
	
	// this goes through each month in months pulldown and verifies the month text is in the top left corner and the expense control.
	public static void VerifyMonthPulldownSelectionsInControls() throws Exception
	{
		List<WebElement> monthYearList;
		
		CommonTestStepActions.initializeMonthSelector();
		
		CommonTestStepActions.SelectAllVendors();
		
		for(WebElement ele : CommonTestStepActions.webListPulldown) // go through each month.
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
	
	public static void BuildCountryListsFromVendors()
	{
		for(int x = 0; x < expectedLegendsList.size(); x++)
		{
			// create a countries list that holds a country for each vendor in expectedLegendsList. this means the list can contain a country more than once. 
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText))
			{
				expectedCountriesList.add(ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x)));
			}

			// create a countries list that holds a unique country for each vendor in expectedLegendsList. this means if the vendors list has 
			// two vendors that are in the same country. the country will only be listed once in this list. 
			if(!expectedLegendsList.get(x).contains(ExpenseHelper.otherText))
			{
				if(!expectedCountriesListUnigue.contains(ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x))))
				{
					expectedCountriesListUnigue.add(ExpenseHelper.GetCountryForVendor(expectedLegendsList.get(x)));					
				}
			}
		}
		
		//ShowListOfStrings(expectedCountriesList); // DEBUG
		//ShowText("------------"); // DEBUG
		//ShowListOfStrings(expectedCountriesListUnigue); // DEBUG
	}
	
}
