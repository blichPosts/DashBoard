package helperObjects;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import Dash.BaseClass;

public class CommonTestStepActions extends BaseClass
{

	public static String []  monthArray = {"January" , "February", "March", "April", "May" , "June", "July", "August", "September", "October", "November", "December"};
	public static List<String> monthListExpected = new ArrayList<>(); 
	public static List<String> monthListActual = new ArrayList<>();	
	public static String pullDownCss  = ".tdb-flexContainer.tdb-flexContainer--center>select";

	public static enum ExpensesViewMode
	{
		vendor,
		country
	}

	
	// put all of the pulldown items in a web list. 
	public static List<WebElement> webListPulldown; //= new Select(driver.findElement(By.cssSelector(pullDownCss))).getOptions();


	public static String errMessage = "";


	// this disables all of the vendor check-boxes and then clicks the one with the text sent in. 
	public static void SelectSingleVendor(String vendorName)
	{
		// disable all and wait for the 'ALL' selector in point of view.
		UnSelectAllVendors();
		WaitForElementClickable(By.cssSelector(".tdb-povGroup__toggle>a"), ShortTimeout, "Failed wait in CommonTestStepActions.SelectSingleVendor.");
		
		driver.findElement(By.xpath("//span[text()='" + vendorName + "']")).click(); // click
	}
	
	// loads each country into a country list. it also adds the vendors for each country.
	public static String GetPulldownTextSelected()
	{
		return new Select(driver.findElement(By.cssSelector(pullDownCss))).getFirstSelectedOption().getText();
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
		Assert.assertEquals(new Select(driver.findElement(By.cssSelector(pullDownCss))).getFirstSelectedOption().getText(), MonthYearMinusOne(), "");
	}
	
	public static void SelectAllVendors()
	{
		WaitForElementClickable(By.cssSelector(".tdb-povGroup__toggle > a"), MediumTimeout, "Select all is not present in CommonTestStepActions.SelectAllVendors");
		if(driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).getText().equals("ALL"))
		{
			driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).click();
		}
	}
	
	public static void UnSelectAllVendors()
	{
		WaitForElementClickable(By.cssSelector(".tdb-povGroup__toggle > a"), MediumTimeout, "Unselect all is not present in CommonTestStepActions.UnSelectAllVendors");
		if(driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).getText().equals("NONE"))
		{
			driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).click();
		}
		// If at least one vendor is selected, the button's label will be = "ALL", so two clicks will be needed: 
		// one to select all vendors; so the label will change to "NONE", and the second to unselect all the vendors at once
		else if(driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).getText().equals("ALL"))
		{
			driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).click(); // First click so  the label changes to NONE
			driver.findElement(By.cssSelector(".tdb-povGroup__toggle > a")).click(); // Second click will definitely unselect all the vendors
		}
	}	

	public static void SelectVendorView()
	{
		WaitForElementClickable(By.cssSelector("#md-tab-label-0-0"), MediumTimeout, "Vendor view is not present in CommonTestStepActions.UnSelectAllVendors");
		driver.findElement(By.cssSelector("#md-tab-label-0-0")).click();
	}	
	
	public static void SelectCountryView() throws Exception
	{
		WaitForElementClickable(By.cssSelector("#md-tab-label-0-1"), MediumTimeout, "Vendor view is not present in CommonTestStepActions.SelectCountryView");
		WaitForElementClickable(By.cssSelector(pullDownCss), MediumTimeout, "");
		WaitForElementVisible(By.cssSelector(pullDownCss), MediumTimeout);
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
		new Select(driver.findElement(By.cssSelector(pullDownCss))).selectByVisibleText(webListPulldown.get(0).getText());
		actualString = new Select(driver.findElement(By.cssSelector(pullDownCss))).getFirstSelectedOption().getText();
		Assert.assertEquals(actualString, webListPulldown.get(0).getText(), errMessage);
		
		// select using text from web list, get selection, and compare with web list selection.
		new Select(driver.findElement(By.cssSelector(pullDownCss))).selectByVisibleText(webListPulldown.get(webListPulldown.size()/2).getText());
		actualString = new Select(driver.findElement(By.cssSelector(pullDownCss))).getFirstSelectedOption().getText();		
		Assert.assertEquals(actualString, webListPulldown.get(webListPulldown.size()/2).getText(), errMessage);
		
		
		// select using text from web list, get selection, and compare with web list selection.
		new Select(driver.findElement(By.cssSelector(pullDownCss))).selectByVisibleText(webListPulldown.get(webListPulldown.size() - 1).getText());
		actualString = new Select(driver.findElement(By.cssSelector(pullDownCss))).getFirstSelectedOption().getText();		
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
		WaitForViewUsageSelector();
		
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

	public static List<String> YearMonthIntergerFromPulldown() throws ParseException
	{
		List<String> tmpList = new ArrayList<String>();
		
		// get the year/month items from the pulldown and create a list of of month (integer) with year.  
		for( WebElement ele : CommonTestStepActions.webListPulldown)
		{
			// this converts the month to an integer. and then appends a dash and the year.
			// final format is like this: '6-2016' or '11-2016'.
			tmpList.add(ConvertMonthToInt(ele.getText().split(" ")[0]) + "-" + ele.getText().split(" ")[1]); 
		}
		
		return tmpList;
	}
	
	
	// return month/year from pulldown in this format MM-YYYY.  
	public static List<String> YearMonthIntergerFromPulldownTwoDigitYear() throws ParseException
	{
		
		List<String> tmpList = new ArrayList<String>();
		String tmpMonthInt = "";
		
		initializeMonthSelector();
				

		// get the year/month items from the pulldown and create a list of of month (integer) with year.  
		for( WebElement ele : CommonTestStepActions.webListPulldown)
		{
			// this converts the month to an integer string and then appends a dash and the year string.
			// final format is like this: '06-2016' or '11-2016' - YY-MMMM.
			
			// get the month.
			tmpMonthInt = ConvertMonthToInt(ele.getText().split(" ")[0]);
			
			// if the month has only one string integer, add a leading zero.
			if(tmpMonthInt.length() == 1)
			{
				tmpMonthInt = "0" + tmpMonthInt;
			}
			
			// add integer month and as string and string year to list to be returned.
			tmpList.add(tmpMonthInt + "-" + ele.getText().split(" ")[1]);  // on Dashboard 1.1.0 it changed from "-" to "/". So changed it to "/". Ana - 01/04/17
		}
		return tmpList;
	}
	
	public static String ConvertMonthToInt(String monthToConvert) throws ParseException
	{
		Date date = new SimpleDateFormat("MMM").parse(monthToConvert);//put your month name here
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    // int monthNumber=cal.get(Calendar.MONTH);
	    return String.valueOf(cal.get(Calendar.MONTH) + 1);
	}

	
	
	
	// helpers
	public static void BobTest()
	{
		
	}

	
	
	// It returns a list with the vendors that have been selected on the Point of View section
	// - by Ana
	public static List<String> getVendorsSelectedInPointOfView() {

		// this list will have the names of the vendors that have been SELECTED on the Point of View section
		List<String> listVendorsChecked = new ArrayList<String>();
		
		// this list will have ALL the names of the vendors LISTED on the Point of View section
		List<WebElement> listVendorsLabels = getAllVendorNames();   //driver.findElements(By.cssSelector(".md-checkbox-label"));
		
		
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
	
	
	
	// It returns a list with the countries that have vendors selected on the Point of View section
	// - by Ana
	public static List<String> getCountriesSelectedInPointOfView() {

		// this list will have the names of the countries that have been SELECTED on the Point of View section
		List<String> listCountriesCheckedTemp = new ArrayList<String>();			
			
		// this list will have ALL the names of the vendors LISTED on the Point of View section
		List<WebElement> listVendorsLabels = getAllVendorNames();   //driver.findElements(By.cssSelector(".md-checkbox-label"));
		
		
		// Add the names of the vendors that are selected on the Point of View to the listVendorsChecked list
		for(int i = 0; i < listVendorsLabels.size(); i++){
			
			int num = i + 1;
			String checkBoxXpath = ".//*[@id='input-md-checkbox-" + num + "']";
			boolean isChecked = driver.findElement(By.xpath(checkBoxXpath)).isSelected();
			
			// if the vendor's checkbox is checked then get the country name and add it to the list
			if(isChecked){
				
				String countryLabelXpath = checkBoxXpath + "/../../../../../preceding-sibling::div";
				
				String countryName = driver.findElement(By.xpath(countryLabelXpath)).getText();
				listCountriesCheckedTemp.add(countryName);
				//System.out.println("Is selected? : " + isChecked + "- Country Name: " + countryName);
				
			}
			
		}

		// This list will have the names of the countries that are selected, and WILL NOT CONTAIN **DUPLICATES**
		List<String> listCountriesChecked = new ArrayList<String>(new LinkedHashSet<String>(listCountriesCheckedTemp));
		
		/*
		for (String s: listCountriesChecked){
			System.out.println("Country: " + s);
		}
		*/	
				
		return listCountriesChecked;
		
	}

	
	// It returns a list with all the vendor names listed on the Point of View section
	public static List<WebElement> getAllVendorNames(){
		
		String xpath = ".//span[@class='md-checkbox-label']";
//		String xpath = ".//span[@class='mat-checkbox-label']";
		
		try {
			WaitForElementPresentNoThrow(By.xpath(xpath), MainTimeout);
		} catch (Exception e) {
			ShowText("No checkboxes listed.");
		}
		
		return driver.findElements(By.xpath(xpath));
		
	}
	
	

	public static void selectOneVendor(String vendorName) {
	
		// this list will have ALL the names of the vendors LISTED on the Point of View section
		List<WebElement> listVendorsLabels = getAllVendorNames();
		
		
		// Add the names of the vendors that are selected on the Point of View to the listVendorsChecked list
		for (int i = 1; i <= listVendorsLabels.size(); i++) {
			
			//int num = i + 1;
			String checkBoxXpath = ".//input[@id='input-md-checkbox-" + i + "']/../..";    
			
//			String cssSelector = "md-checkbox>label:nth-of-type(" + i + ")";   //"span.md-checkbox-label:nth-of-type(" + i + ")";
//			WebElement vendorCheckBox = driver.findElement(By.cssSelector(cssSelector));
			
			// If the vendor's name from the list matches the name in the parameter then click the checkbox, so the vendor is selected 
			if(listVendorsLabels.get(i-1).getText().equals(vendorName)){
//			if(vendorCheckBox.getText().equals(vendorName)){
//				driver.findElement(By.cssSelector(cssSelector)).click();
				listVendorsLabels.get(i-1).click();
				// driver.findElement(By.xpath(checkBoxXpath)).click();
				
			}
			
		}

	}
	

		
	// 1/2/17 - added the version of the numbers that start with "0", like "01", in case the month number received is in that format (Ana) 
	public static String convertMonthNumberToName(String month, String year){
		
		switch(month){
			case "1":
				return ("January " + year);
			case "01":
				return ("January " + year);
			case "2":
				return ("February " + year);
			case "02":
				return ("February " + year);
			case "3":
				return ("March " + year);
			case "03":
				return ("March " + year);
			case "4":
				return ("April " + year);
			case "04":
				return ("April " + year);
			case "5":
				return ("May " + year);
			case "05":
				return ("May " + year);
			case "6":
				return ("June " + year);
			case "06":
				return ("June " + year);
			case "7":
				return ("July " + year);
			case "07":
				return ("July " + year);
			case "8":
				return ("August " + year);
			case "08":
				return ("August " + year);
			case "9":
				return ("September " + year);
			case "09":
				return ("September " + year);
			case "10":
				return ("October " + year);
			case "11":
				return ("November " + year);
			case "12":
				return ("December " + year);
			default:
				return "";
		}
		
	}
	
	
	// Set the month selector value to the value specified in the parameter
	public static void selectMonthYearPulldown(String monthYear){
		
		// this list will have ALL the 'Month Year' listed on the pulldown on the Point of View section

		List<WebElement> listMonthYear = driver.findElements(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select>option")); 
		List<String> listMonthNames = new ArrayList<>();
		
		for (WebElement month: listMonthYear) {
			listMonthNames.add(month.getText());
		}
		
		for(int i = 0; i < listMonthYear.size(); i++){
			
			// If the vendor's name from the list matches the name in the parameter then click the checkbox, so the vendor is selected 
			if(listMonthNames.get(i).equals(monthYear)){
				
				driver.findElement(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select>option:nth-of-type(" + (i+1) + ")")).click(); 
				
			}
			
		}
		
	}
	
	
	// Added by Ana - the month selector variable needs to be initialized here
	public static void initializeMonthSelector(){
		
		webListPulldown = new Select(driver.findElement(By.cssSelector(pullDownCss))).getOptions();
		
	}
	

	public static List<List<WebElement>> getCountriesAndVendors() {

		
		List<List<WebElement>> countriesAndVendors = new ArrayList<>();
		
		List<WebElement> countries = driver.findElements(By.cssSelector(".tdb-povGroup__label--subhead"));
		List<WebElement> vendors = driver.findElements(By.cssSelector(".tdb-povGroup__label--subhead~div"));
		
		List<WebElement> temp = new ArrayList<WebElement>();
		
		for (int i = 0; i < countries.size(); i++){
			
			temp.add(countries.get(i));
			//System.out.println(countries.get(i).getText());
			
			List<WebElement> vendorsUnderCountry = vendors.get(i).findElements(By.cssSelector(".tdb-povGroup__label--subhead~div>div>md-checkbox>label>span")); 
			
			for (WebElement v : vendorsUnderCountry){
				temp.add(v);
				//System.out.println("  " + v.getText());
			}
			
			countriesAndVendors.add(temp);
			
		}
		
		//System.out.println("countriesAndVendors size: " + countriesAndVendors.size()); 
			
		return countriesAndVendors;
	}
	
	
	
	public static String[] getMonthYearInteger(String monthYearToConvert) throws ParseException {
		
		Date date = new SimpleDateFormat("MMM").parse(monthYearToConvert);//put your month name here
		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    
	    String monthNumber = String.valueOf(cal.get(Calendar.MONTH) + 1);
	    String yearNumber = monthYearToConvert.split(" ")[1];
	   
	    String[] monthYearNumber = {monthNumber, yearNumber};
	    
	    return monthYearNumber;
	    
	}

	// open settings panel under the gear in the top right corner.
	public static void OpenSettingsPanel()
	{
		WaitForElementClickable(By.xpath("(//div[@class='md-sidenav-content']/div/div/button)[2]"), MediumTimeout, "Couldn't find gear to click for opening settings panel.");
		driver.findElement(By.xpath("(//div[@class='md-sidenav-content']/div/div/button)[2]")).click();
	}
	
	// close open settings panel.
	public static void CloseSettingsPanel() throws Exception
	{
		// make sure the panel is open
		if(WaitForElementNotPresentNoThrow(By.cssSelector(".tdb-slideout>header>div:nth-of-type(1)"), MiniTimeout)) 
		{
			// close panel
			WaitForElementClickable(By.xpath("(//button[@class='md-primary'])[1]"), MediumTimeout, "Couldn't find button to close the settings panel.");
			driver.findElement(By.xpath("(//button[@class='md-primary'])[1]")).click();
		}
		else
		{
			ShowText("Nothing to close. Settings pane appears already closed.");
		}
	}	
	
}
