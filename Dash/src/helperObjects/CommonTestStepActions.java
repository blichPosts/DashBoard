package helperObjects;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
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
	
	// put all of the pulldown items in a web list. 
	public static List<WebElement> webListPulldown = new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getOptions();

	public static String errMessage = "";
	
	// loads each country into a country list. it also adds the vendors for each country.
	public static String GetPulldownTextSelected()
	{
		return new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getFirstSelectedOption().getText();
	}
	
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
			
			Assert.assertTrue(numberOfVendorsInCountry > 0, "Found a country with no vendors."); // verify at leasts one vendor is found.
			
			for(int z = 0; z < numberOfVendorsInCountry; z++)
			{
				tmpCountry.AddToVendorList(vndrList.get(y).getText());
				y++;
			}
		}		
	}

	public static void VerifyMonthPullDownFormat()
	{
		Assert.assertEquals(new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getFirstSelectedOption().getText(), MonthYearMinusOne(), "");
	}
	
	public static void SelectAllVendors()
	{
		WaitForElementClickable(By.cssSelector(".tdb-povGroup__toggle > a"), MediumTimeout, "Select all is not present in CommonTestStepActions.SelectAllVendors");
		if(driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).getText().equals("All"))
		{
			driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).click();
		}
	}
	
	public static void UnSelectAllVendors()
	{
		WaitForElementClickable(By.cssSelector(".tdb-povGroup__toggle > a"), MediumTimeout, "Unselect all is not present in CommonTestStepActions.UnSelectAllVendors");
		if(driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).getText().equals("None"))
		{
			driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).click();
		}
	}	

	public static void SelectVendorView()
	{
		WaitForElementClickable(By.cssSelector("#md-tab-label-0-0"), MediumTimeout, "Vendor view is not present in CommonTestStepActions.UnSelectAllVendors");
		driver.findElement(By.cssSelector("#md-tab-label-0-0")).click();
	}	
	
	public static void SelectCountryView()
	{
		WaitForElementClickable(By.cssSelector("#md-tab-label-0-1"), MediumTimeout, "Vendor view is not present in CommonTestStepActions.UnSelectAllVendors");
		driver.findElement(By.cssSelector("#md-tab-label-0-1")).click();
	}		
	
	
	public static void VerifyMonthPullDownSelectionsAndContent() 
	{
		String firstMonth;
		String lastMonth;
		
		Assert.assertTrue(webListPulldown.size() == 13); // verify 13 months shown
		
		// create a list of months expected from the hard-coded monthArray.
		for(int x = 0; x < monthArray.length; x++)
		{
			monthListExpected.add(monthArray[x]);
		}
		
		// create a list of months actual from the web list.
		for(int x = 0; x < monthArray.length; x++)
		{
			monthListActual.add(webListPulldown.get(x).getText().split(" ")[0]);
		}
		
		// store the first month in the web list. 
		firstMonth = webListPulldown.get(0).getText().split(" ")[0];
		
		// store the last month in the web list.
		lastMonth = webListPulldown.get(webListPulldown.size() - 1).getText().split(" ")[0];

		Assert.assertEquals(lastMonth, firstMonth); // verify last and first are equal as expected.
		
		// sort the arrays for compare. 
		Collections.sort(monthListExpected);
		Collections.sort(monthListActual);
		
		//for(int x = 0; x < monthArray.length; x++){DebugTimeout(0, monthListExpected.get(x));} // DEBUG
		//for(int x = 0; x < monthArray.length; x++) {DebugTimeout(0, monthListActual.get(x));} // DEBUG		
		
		// do the compare to verify all 12 months are shown
		Assert.assertEquals(monthListActual, monthListExpected, "Failed month list compare in CommonTestStepActions.VerifyMonthPullDownSelectionsAndContent");
	}	
	
	// select and verify the first, middle, and last selections in the pulldown.
	public static void UsePulldownList()
	{
		String actualString = "";
		errMessage = "Failure in verifying point of view pulldown selections.";
		
		// select using text from web list, get selection, and compare with web list selection.
		new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).selectByVisibleText(webListPulldown.get(0).getText());
		actualString = new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getFirstSelectedOption().getText();
		Assert.assertEquals(actualString, webListPulldown.get(0).getText(), errMessage);
		
		// select using text from web list, get selection, and compare with web list selection.
		new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).selectByVisibleText(webListPulldown.get(webListPulldown.size()/2).getText());
		actualString = new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getFirstSelectedOption().getText();		
		Assert.assertEquals(actualString, webListPulldown.get(webListPulldown.size()/2).getText(), errMessage);
		
		
		// select using text from web list, get selection, and compare with web list selection.
		new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).selectByVisibleText(webListPulldown.get(webListPulldown.size() - 1).getText());
		actualString = new Select(driver.findElement(By.cssSelector(".tbd-pov__monthPicker>select"))).getFirstSelectedOption().getText();		
		Assert.assertEquals(actualString, webListPulldown.get(webListPulldown.size()- 1).getText(), errMessage); 
	}		
	
	// verify the list of months/years in the months pulldown is descending for 13 months, starting at the current month/year.  
	public static void VerifyMonthPulldownDetail()
	{
		
		// list with two years worth of months.
		List<String> twoYearMonthsList = new ArrayList<String>();
		
		twoYearMonthsList.add("January");
		twoYearMonthsList.add("February"); 
		twoYearMonthsList.add("March"); 
		twoYearMonthsList.add("April");
		twoYearMonthsList.add("May");  
		twoYearMonthsList.add("June"); 
		twoYearMonthsList.add("July"); 
		twoYearMonthsList.add("August");
		twoYearMonthsList.add("September"); 
		twoYearMonthsList.add("October");
		twoYearMonthsList.add("November");
		twoYearMonthsList.add("December");
		twoYearMonthsList.add("January");
		twoYearMonthsList.add("February");
		twoYearMonthsList.add("March");
		twoYearMonthsList.add("April");
		twoYearMonthsList.add("May");
		twoYearMonthsList.add("June");
		twoYearMonthsList.add("July");
		twoYearMonthsList.add("August"); 
		twoYearMonthsList.add("September");
		twoYearMonthsList.add("October");
		twoYearMonthsList.add("November");
		twoYearMonthsList.add("December");

		String currentMonthYear = MonthYearCurrentYear(); // get current month/year.
		
		int currentYear =  Integer.valueOf(MonthYearCurrentYear().split(" ")[1]); // store current year as int.
		
		// find the index of the current month in the 'twoYearMonthsList' list and get its index + 12.
		// 12 is added because the list will be traversed backwards from  index + 12.
		int startMonth = twoYearMonthsList.indexOf(currentMonthYear.split(" ")[0]) + 12; 

		// go through the lists comparing actual month/year to expected month/year.
		for(int x = startMonth, y = 0; y < 13; x--, y++)
		{
			//System.out.println(webListPulldown.get(y).getText()); // DEBUG
			//System.out.println(twoYearMonthsList.get(x) + " " + currentYear); // DEBUG
			
			Assert.assertEquals(webListPulldown.get(y).getText(), twoYearMonthsList.get(x) + " " + currentYear, "");
			
			// if 'January' has been reached, need to decrement year for rest of descending months. 
			if(webListPulldown.get(y).getText().contains("January"))
			{
				currentYear--;
			}
		}
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
	
	public static void GoToExpensePageDetailedWait() throws Exception
	{
		WaitForExpensePageDetailedLoad();
		SelectExpenseTab();
	}
	
	
	// added by Ana
	public static void GoToUsagePageDetailedWait() throws Exception
	{
		//WaitForExpensePageDetailedLoad();  // wait for Expense page to load since it's the page loaded by default when loading Dashboard UI
		WaitForViewUsageSelector();
		//SelectUsageTab();
	}

	
	// returns one month behind current month and year. 
	static public String MonthYearCurrentYear()
	{
		Calendar c = Calendar.getInstance();		
		int month = c.get(Calendar.MONTH);		
		int year = c.get(Calendar.YEAR);		
		String monthString = new DateFormatSymbols().getMonths()[month];
		
		return monthString + " " + year;
	}	

	// helpers
	public static void BobTest()
	{
		
	}

	public static void anaTest(){
		// testing branches
	}

	
	
	// It returns a list with the vendors that have been selected on the Point of View section
	// - by Ana
	public static List<String> getVendorsSelectedInPointOfView() {

		// this list will have the names of the vendors that have been SELECTED on the Point of View section
		List<String> listVendorsChecked = new ArrayList<String>();
		
		// this list will have ALL the names of the vendors LISTED on the Point of View section
		List<WebElement> listVendorsLabels = driver.findElements(By.cssSelector(".md-checkbox-label"));
		
		
		// Add the names of the vendors that are selected on the Point of View to the listVendorsChecked list
		for(int i = 0; i < listVendorsLabels.size(); i++){
			
			int num = i + 1;
			String checkBoxXpath = ".//*[@id='input-md-checkbox-" + num + "']";
			boolean isChecked = driver.findElement(By.xpath(checkBoxXpath)).isSelected();
			
			// if the vendor's checkbox is checked then add the vendor's name to the list
			if(isChecked){
				
				String vendorName = listVendorsLabels.get(i).getText();
				listVendorsChecked.add(vendorName);
				//System.out.println("Is selected? : " + isChecked + "- Vendor Name: " + vendorName);
				
			}
			
		}
		
		
		return listVendorsChecked;
		
	}
	
	
	
	
	
}
