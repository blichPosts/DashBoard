package helperObjects;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;

public class FleetHelper extends BaseClass {

	public final static int totalExpenseSection = 0;
	public final static int expenseTrendingSection = 1;
	
	public final static int expenseTotalExpensePieChart = 0;
	public final static int expenseTotalExpenseBarChart = 1;
	public final static int expenseByVendorChart = 2;
	public final static int costPerServiceNumberChart  = 3;
	public final static int countServiceNumbersChart = 4;
	
	public final static int expenseCategoryAll = 1;
	public final static int expenseCategoryVoice = 2;
	public final static int expenseCategoryData = 3;
	public final static int expenseCategoryMessages = 4;
	public final static int expenseCategoryRoaming = 5;
	public final static int expenseCategoryEquipment = 6;
	public final static int expenseCategoryTaxes = 7;
	public final static int expenseCategoryOther = 8;
	public final static int expenseCategoryAccount = 9;

	public final static String expenseCategoryAllName = "All";
	public final static String expenseCategoryVoiceName = "Voice";
	public final static String expenseCategoryDataName = "Data";
	public final static String expenseCategoryMessagesName = "Messages";
	public final static String expenseCategoryRoamingName = "Roaming";
	public final static String expenseCategoryEquipmentName = "Equipment";
	public final static String expenseCategoryTaxesName = "Taxes";
	public final static String expenseCategoryOtherName = "Other";
	public final static String expenseCategoryAccountName = "Account";
	
//	public final static int usageCategoryVoice = 1;
//	public final static int usageCategoryData = 2;
//	public final static int usageCategoryMessages = 3;
	

	
	public static void selectCategory(int category){
		
		WebElement categoryToSelect = driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(" + category + ")"));
		categoryToSelect.click();
		
	}

	
	public static void selectCategoryUsage(int chartNum, String category) throws InterruptedException{
		
		ShowText("Category to select: " + category);
		
		String xpath = "";   // ".//div[@class='tdb-card']/div/div[text()='" + category + "']";
		
		if (chartNum == UsageHelper.totalUsageDomesticChart) {
			
			xpath = ".//div[@class='tdb-USAGE']/div/div/div[@class='tdb-card']/h3/preceding-sibling::div/div[text()='" + category + "']";
			
		} else if (chartNum == UsageHelper.usageTrendingDomesticChart) {
			
			xpath = ".//div[@class='tdb-USAGE']/div/div[@class='tdb-card']/h3/preceding-sibling::div/div[text()='" + category + "']";
			
		} else if (chartNum == UsageHelper.usageTrendingRoamingChart) {
			
			xpath = ".//div[@class='tdb-USAGE']/div/div[@class='tdb-card']/h3/following-sibling::div/div[text()='" + category + "']";
			
		} else if (chartNum == FleetHelper.expenseByVendorChart || chartNum == FleetHelper.costPerServiceNumberChart) {
			
			xpath = ".//div[@class='tdb-EXPENSE__NORMAL-VIEW']/div[@class='tdb-card']/h3[2]/preceding-sibling::div[1]/div[text()='" + category + "']";
			
		}

		WebElement categoryToSelect = driver.findElement(By.xpath(xpath));
		categoryToSelect.click();
		
		Thread.sleep(2000);
		
	}
	
	
	public static void selectCategoryExpenses(int chartNum, String category) throws InterruptedException{
		
		ShowText("Category to select: " + category);
		
		String xpath = ".//div[@class='tdb-EXPENSE__NORMAL-VIEW']/div[@class='tdb-card']/h3[2]/preceding-sibling::div[1]/div[text()='" + category + "']";
		
		WebElement categoryToSelect = driver.findElement(By.xpath(xpath));
		categoryToSelect.click();
		
		Thread.sleep(2000);
		
	}
	
	
	
	public static List<List<UsageOneMonth>> getExpenseUsageDataForTest() throws Exception {
		
		List<String> vendorNames = CommonTestStepActions.getAllVendorNames();
			
		int amountOfVendors = vendorNames.size();
		
//		int amountOfVendors = 10; //GeneralHelper.getAmountOfVendorsToSelect(vendors.size()); // If we don't want the prompt to show up, just replace the assignment with a numeric value. 
//
//		if (amountOfVendors >= vendorNames.size() || browserType.equals(BrowserType.FireFox)) {
//		
//			amountOfVendors = vendorNames.size();
//			
//		} else if (amountOfVendors < vendorNames.size()) {
//			
//			// Unselect all vendors..
//			CommonTestStepActions.UnSelectAllVendors();
//			
//			// .. and select one by one the amount of vendors in 'amountOfVendors'
//			for (int i = 0; i < amountOfVendors; i++) {
//
//				CommonTestStepActions.selectOneVendor(vendorNames.get(i));
//				
//			}
//			
//		}
		 				
		ShowText("Amount of Vendors Selected: " + amountOfVendors);
		
		
		List<List<UsageOneMonth>> listDataSelectedVendors = new ArrayList<List<UsageOneMonth>>(); 
		
		for (int i = 0; i < amountOfVendors; i++) {
			
			String vendor = vendorNames.get(i);
			
			// Read data from the ajax call response
			List<UsageOneMonth> valuesFromAjaxCall = ReadFilesHelper.getJsonDataExpenseUsage(vendor);			
			listDataSelectedVendors.add(valuesFromAjaxCall);
			
		}
		
		return listDataSelectedVendors;
				
	}


	
	public static List<List<UsageOneMonth>> getExpenseUsageDataAllMonths(List<List<UsageOneMonth>> listSelectedDataForMonthListUnified) throws ParseException {
		
		List<List<UsageOneMonth>> listVendorsSelectedData = new ArrayList<List<UsageOneMonth>>();
		
		for (List<UsageOneMonth> list: listSelectedDataForMonthListUnified) {
			
			List<UsageOneMonth> valuesFromFileOneVendor = UsageHelper.addMissingMonthsForVendor(list);
			listVendorsSelectedData.add(valuesFromFileOneVendor);
		
		}
		
		return listVendorsSelectedData;
		
	}


	public static List<List<UsageOneMonth>> getListsWithDataPerMonth(List<List<UsageOneMonth>> listVendorsSelectedData) throws ParseException {
		
		List<List<UsageOneMonth>> dataForExpenseTrending = new ArrayList<>();
		
		List<String> monthsToSelect = UsageHelper.getMonthYearListString();
		int indexMonth = 0;
		
		do {
		
			// listOneMonthData will have the data for a specific month, for all the vendors previously selected
			List<UsageOneMonth> listOneMonthData = new ArrayList<>();
			
			// 'values' has the 13 months for one vendor
			for (List<UsageOneMonth> values: listVendorsSelectedData){
				
				int indexMonthForVendorSelected = 0;
				
				while (indexMonthForVendorSelected < values.size()) {
					
					String monthYear = CommonTestStepActions.convertMonthNumberToName(values.get(indexMonthForVendorSelected).getOrdinalMonth(), values.get(indexMonthForVendorSelected).getOrdinalYear()); 
					
					if (monthsToSelect.get(indexMonth).equals(monthYear)) {
 
						listOneMonthData.add(values.get(indexMonthForVendorSelected));
						
					}
						
					indexMonthForVendorSelected++;
				}
					
			}

			dataForExpenseTrending.add(listOneMonthData);
			
			indexMonth++;
			
		} while (indexMonth < monthsToSelect.size());
		
		return dataForExpenseTrending;
		
	}


	
	public static void waitForPoVSectionToBeLoaded() throws Exception {
		
		WaitForElementPresent(By.cssSelector(".tdb-povGroup>.tdb-povGroup"), ExtremeTimeout);
		
	}

	

	// Get a list with the names of the vendors selected on the PoV section
	public static List<String> getVendorsSelected() {
		
		List<WebElement> listVendorElements = driver.findElements(By.cssSelector("md-checkbox.md-checkbox-checked>label>span"));
		List<String> listVendorNames = new ArrayList<>();
		
		for (WebElement v: listVendorElements) {
			
			listVendorNames.add(v.getText());
		}
		
		return listVendorNames;
		
	}
	
	
	
	// Get a list with the names of the countries selected on the PoV section
	public static List<String> getCountriesSelected() {
		
		List<WebElement> listCountryElements = driver.findElements(By.cssSelector(".tdb-povGroup__label--subhead"));
		List<String> listCountryNames = new ArrayList<>();
		
		
		// i = 2, because the first element is not a country name
		for (int i = 2; i <= listCountryElements.size()+1; i++) {
			
			try {
			
				WebElement checkBox = driver.findElement(By.cssSelector("div.tdb-povGroup:nth-of-type(1)>div:nth-of-type(" + i + ")>div>div>md-checkbox.md-checkbox-checked"));
				
				// If checkbox is checked (WebElement checkBox is not null)
				if (!checkBox.equals(null)) {
				
					String countryName = driver.findElement(By.xpath("//div[@class='tdb-povGroup']/div[@class='tdb-povGroup'][" + (i-1) + "]/div[@class='tdb-povGroup__label--subhead']")).getText();
					listCountryNames.add(countryName);
					
				}
				
			} catch (org.openqa.selenium.NoSuchElementException e) {
				// If checkbox is not checked (WebElement checkBox == null) then move to next checkbox
			}
			
		}
		
		return listCountryNames;
		
	}

	

	// Summarize Usage values by Country for a specific month
	public static List<UsageOneMonth> summarizeUsageValuesByCountry(List<UsageOneMonth> listOneMonthData) {
		
				
		HashMap<String, UsageOneMonth> hashmapValuesByCountry = new HashMap<>();
					
		for (UsageOneMonth usage: listOneMonthData) {
			
			UsageOneMonth usageSummarized = new UsageOneMonth();			
			String country = usage.getCountry();
			
			// ShowText("Country: " + country + " - Vendor: " + usage.getVendorName() + " - month/year: " + usage.getOrdinalMonth() + "/" + usage.getOrdinalYear());
			 
			boolean countryInMap = false; 
			
			try {
				
				hashmapValuesByCountry.get(country).getCountry();
				countryInMap = true;
				
			} catch (NullPointerException e) {
				
				countryInMap = false;
						
			}
			
			if (countryInMap) {
			
				// ShowText("Country was already in hashmap");
				
				usageSummarized = hashmapValuesByCountry.get(country);
							
				usageSummarized.setDomesticVoice(Double.toString(Double.parseDouble(usageSummarized.getDomesticVoice()) + Double.parseDouble(usage.getDomesticVoice())));
				
				usageSummarized.setDomesticOverageVoice(Double.toString(Double.parseDouble(usageSummarized.getDomesticOverageVoice()) + Double.parseDouble(usage.getDomesticOverageVoice())));
				
				usageSummarized.setDomesticMessages(Double.toString(Double.parseDouble(usageSummarized.getDomesticMessages()) + Double.parseDouble(usage.getDomesticMessages())));
				
				usageSummarized.setDomesticDataUsageKb(Double.toString(Double.parseDouble(usageSummarized.getDomesticDataUsageKb()) + Double.parseDouble(usage.getDomesticDataUsageKb())));
				
				usageSummarized.setRoamingVoice(Double.toString(Double.parseDouble(usageSummarized.getRoamingVoice()) + Double.parseDouble(usage.getRoamingVoice())));
				
				usageSummarized.setRoamingMessages(Double.toString(Double.parseDouble(usageSummarized.getRoamingMessages()) + Double.parseDouble(usage.getRoamingMessages())));
				
				usageSummarized.setRoamingDataUsageKb(Double.toString(Double.parseDouble(usageSummarized.getRoamingDataUsageKb()) + Double.parseDouble(usage.getRoamingDataUsageKb())));

				hashmapValuesByCountry.put(usageSummarized.getCountry(), usageSummarized);
				
			} else {
				
				// ShowText("Country was NOT in hashmap");
				
				hashmapValuesByCountry.put(usage.getCountry(), usage);
				
			}
	
		}
		
		List<UsageOneMonth> dataSummarized = new ArrayList<>(hashmapValuesByCountry.values());		 
	
		return dataSummarized;

	}

	
	// Summarize Expenses values by Country for a specific month
	public static List<UsageOneMonth> summarizeExpensesValuesByCountry(List<UsageOneMonth> listOneMonthData) {
		
				
		HashMap<String, UsageOneMonth> hashmapValuesByCountry = new HashMap<>();
					
		for (UsageOneMonth usage: listOneMonthData) {
			
			UsageOneMonth expensesSummarized = new UsageOneMonth();			
			String country = usage.getCountry();
			
			// ShowText("Country: " + country);
			// ShowText("Vendor: " + usage.getVendorName());
			
			boolean countryInMap = false; 
			
			try {
				
				hashmapValuesByCountry.get(country).getCountry();
				countryInMap = true;
				
			} catch (NullPointerException e) {
				
				countryInMap = false;
						
			}
			
			if (countryInMap) {
			
				// ShowText("Country was already in hashmap");
				
				expensesSummarized = hashmapValuesByCountry.get(country);
				
				expensesSummarized.setNumberOfInvoices(Double.toString(Double.parseDouble(expensesSummarized.getNumberOfInvoices()) + Double.parseDouble(usage.getNumberOfInvoices())));

				expensesSummarized.setNumberOfLines(Double.toString(Double.parseDouble(expensesSummarized.getNumberOfLines()) + Double.parseDouble(usage.getNumberOfLines())));
				
				expensesSummarized.setTotalSubscriberCharges(Double.toString(Double.parseDouble(expensesSummarized.getTotalSubscriberCharges()) + Double.parseDouble(usage.getTotalSubscriberCharges())));
				
				expensesSummarized.setVoiceCharges(Double.toString(Double.parseDouble(expensesSummarized.getVoiceCharges()) + Double.parseDouble(usage.getVoiceCharges())));
				
				expensesSummarized.setDataCharges(Double.toString(Double.parseDouble(expensesSummarized.getDataCharges()) + Double.parseDouble(usage.getDataCharges())));
				
				expensesSummarized.setMessagesCharges(Double.toString(Double.parseDouble(expensesSummarized.getMessagesCharges()) + Double.parseDouble(usage.getMessagesCharges())));
				
				expensesSummarized.setEquipmentCharges(Double.toString(Double.parseDouble(expensesSummarized.getEquipmentCharges()) + Double.parseDouble(usage.getEquipmentCharges())));
				
				expensesSummarized.setTaxCharges(Double.toString(Double.parseDouble(expensesSummarized.getTaxCharges()) + Double.parseDouble(usage.getTaxCharges())));
				
				expensesSummarized.setRoamingMsgCharges(Double.toString(Double.parseDouble(expensesSummarized.getRoamingMsgCharges()) + Double.parseDouble(usage.getRoamingMsgCharges())));
				
				expensesSummarized.setRoamingDataCharges(Double.toString(Double.parseDouble(expensesSummarized.getRoamingDataCharges()) + Double.parseDouble(usage.getRoamingDataCharges())));
				
				expensesSummarized.setRoamingVoice(Double.toString(Double.parseDouble(expensesSummarized.getRoamingVoiceCharges()) + Double.parseDouble(usage.getRoamingVoiceCharges())));
				
				expensesSummarized.setTotalAccountLevelCharges(Double.toString(Double.parseDouble(expensesSummarized.getTotalAccountLevelCharges()) + Double.parseDouble(usage.getTotalAccountLevelCharges())));
				
				expensesSummarized.setRoamingCharges(Double.toString(Double.parseDouble(expensesSummarized.getRoamingCharges()) + Double.parseDouble(usage.getRoamingCharges())));
				
				expensesSummarized.setOtherCharges(Double.toString(Double.parseDouble(expensesSummarized.getOtherCharges()) + Double.parseDouble(usage.getOtherCharges())));
				
				expensesSummarized.setTotalCharge(Double.toString(Double.parseDouble(expensesSummarized.getTotalCharge()) + Double.parseDouble(usage.getTotalCharge())));
				
				hashmapValuesByCountry.put(expensesSummarized.getCountry(), expensesSummarized);
				
			} else {
				
				// ShowText("Country was NOT in hashmap");
				
				hashmapValuesByCountry.put(usage.getCountry(), usage);
				
			}
	
		}
		
		List<UsageOneMonth> dataSummarized = new ArrayList<>(hashmapValuesByCountry.values());		 
	
		return dataSummarized;

	}
	
	

	// Gets all the data for a specific month - Used for *Total Usage* chart tests
	public static List<UsageOneMonth> getDataForOneMonth(List<List<UsageOneMonth>> listVendorsSelectedData, int indexMonth, List<String> monthsToSelect) {
		
		List<UsageOneMonth> listOneMonthData = new ArrayList<>();
		
		for (List<UsageOneMonth> values: listVendorsSelectedData) {
			
			int indexMonthForVendorSelected = 0;
			
			while (indexMonthForVendorSelected < values.size()) {
				
				String monthYear = CommonTestStepActions.convertMonthNumberToName(values.get(indexMonthForVendorSelected).getOrdinalMonth(), values.get(indexMonthForVendorSelected).getOrdinalYear()); 
				
				if (monthsToSelect.get(indexMonth).equals(monthYear)) {

					listOneMonthData.add(values.get(indexMonthForVendorSelected));
					
				}
					
				indexMonthForVendorSelected++;
			}
			
		}
			
		return listOneMonthData;
		
	}


	
	public static List<List<UsageOneMonth>> getUsageTrendingDataSummarized(List<List<UsageOneMonth>> listVendorsSelectedData) {
		
		List<List<UsageOneMonth>> dataSummarized = new ArrayList<>();
	
		for (List<UsageOneMonth> list: listVendorsSelectedData) {
			
			List<UsageOneMonth> listTmp = summarizeUsageValuesByCountry(list);
			dataSummarized.add(listTmp);
				
		}
		
		return dataSummarized;
		
	}


	public static List<List<UsageOneMonth>> getExpenseTrendingDataSummarized(List<List<UsageOneMonth>> listVendorsSelectedData) {
		
		List<List<UsageOneMonth>> dataSummarized = new ArrayList<>();
	
		for (List<UsageOneMonth> list: listVendorsSelectedData) {
			
			List<UsageOneMonth> listTmp = summarizeExpensesValuesByCountry(list);
			dataSummarized.add(listTmp);
				
		}
		
		return dataSummarized;
		
	}
	
	
	public static String getNameCategoryExpenses(int category) {
		
		switch (category) {
			
			case expenseCategoryAll:
				return expenseCategoryAllName;
				
			case expenseCategoryVoice:
				return expenseCategoryVoiceName;
				
			case expenseCategoryData:
				return expenseCategoryDataName;
				
			case expenseCategoryMessages:
				return expenseCategoryMessagesName;
								
			case expenseCategoryRoaming:
				return expenseCategoryRoamingName;
				
			case expenseCategoryEquipment:
				return expenseCategoryEquipmentName;
				
			case expenseCategoryTaxes:
				return expenseCategoryTaxesName;
				
			case expenseCategoryOther:
				return expenseCategoryOtherName;
				
			case expenseCategoryAccount:
				return expenseCategoryAccountName;
				
			default:
				return null;
				
		}
		
	}
	
	
}
