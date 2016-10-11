package helperObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.mustache.BaseChunk;

import Dash.BaseClass;

public class CommonTestStepActions extends BaseClass
{

	public static String []  monthArray = {"January" , "February", "March", "April", "May" , "June", "July", "August", "September", "October", "November", "December"};
	public static List<String> monthListExpected = new ArrayList<>(); 
	public static List<String> monthListActual = new ArrayList<>();	
	
	// loads each country into a country list. it also adds the vendors for each country.
	public static void SetupCountryAndVendorData()
	{
		countryList.clear(); // make sure it's cleared in case has already been used.  
		
		// get all countries
		List<WebElement> ctryList = driver.findElements(By.cssSelector(".tdb-povGroup__Label--subhead"));
		
		// get all vendors
		List<WebElement> vndrList = driver.findElements(By.cssSelector(".md-checkbox-label"));	
		
		int y = 0;
		int numberOfVendorsInCountry = 0;

		// got through the countries
		for(int x = 1; x <= ctryList.size(); x++)
		{
			// create country object and add to list.
			Country tmpCountry = new Country(ctryList.get(x-1).getText()); 
			countryList.add(tmpCountry);
			
			// add the vendors to the country just added to the list by getting  the number of vendors for the country and add that number of vendors from the vendor list.
			numberOfVendorsInCountry = driver.findElements(By.xpath("(//div/div[@class='tdb-povGroup__Label--subhead'])[" + x + "]/following-sibling ::div/div")).size();
			
			Assert.assertTrue(numberOfVendorsInCountry > 0, "Found a country with no vendors."); // verify at leats one vendor is found.
			
			for(int z = 0; z < numberOfVendorsInCountry; z++)
			{
				tmpCountry.AddToVendorList(vndrList.get(y).getText());
				y++;
			}
		}		
	}

	public static void VerifyMonthPullDownFormat()
	{
		Assert.assertEquals(new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getFirstSelectedOption().getText(), MonthYear(), "");
	}
	
	public static void VerifyMonthPullDownSelectionsAndContent() 
	{
		String firstMonth;
		String lastMonth;
		
		// put all of the pulldown item in a web list.
		List<WebElement> webList = new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getOptions();
		
		// create a list of months expected from the hard-coded monthArray.
		for(int x = 0; x < monthArray.length; x++)
		{
			monthListExpected.add(monthArray[x]);
		}
		
		// create a list of months actual from the web list.
		for(int x = 0; x < monthArray.length; x++)
		{
			monthListActual.add(webList.get(x).getText().split(" ")[0]);
		}
		
		// store the first month in the web list. 
		firstMonth = webList.get(0).getText().split(" ")[0];
		
		// store the last month in the web list.
		lastMonth = webList.get(webList.size() - 1).getText().split(" ")[0];

		Assert.assertEquals(lastMonth, firstMonth); // verify last and first are 
		
		// sort the arrays for compare. 
		Collections.sort(monthListExpected);
		Collections.sort(monthListActual);
		
		//for(int x = 0; x < monthArray.length; x++){DebugTimeout(0, monthListExpected.get(x));} // DEBUG
		//for(int x = 0; x < monthArray.length; x++) {DebugTimeout(0, monthListActual.get(x));} // DEBUG		
		
		// do the compare.
		// Assert.assertEquals(monthListActual, monthListExpected, "");
	}	
	
	public static void VerifyCountryAndVendorListSorted()
	{
		List<String> actualList = new ArrayList<String>();
		List<String> expectedList = new ArrayList<String>();		
		
		SetupCountryAndVendorData();
		
		// add the name of each country to an actual and expected list. also verify the vendor list is sorted for each country.
		for(Country ctr : countryList)
		{
			ctr.VerifyVendorListSorted(ctr.name); // verify countries vendor list is sorted.
			actualList.add(ctr.name);
			expectedList.add(ctr.name);			
		}
		
		// sort the expected list 
		Collections.sort(expectedList);
		
		Assert.assertEquals(actualList, expectedList, "Failed check for country list being sorted in  CommonTestStepActions.VerifyCountryListSorted.");   
	}		
	
	public static void GoToExpensePage() throws Exception
	{
		WaitForExpensePageLoad();
		SelectExpenseTab();
	}
	
	// helpers
	public static void BuildMonthList()
	{
		
	}
	

}
