package helperObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import Dash.BaseClass;


public class UsageHelper extends BaseClass{

	public final static String path = "D:/Documents/CMD Dashboard/CreateFilesProgram/ ";
	public final static String minutes = "min";
	public final static String messages = "msg";
	public final static String dataB = "B";
	public final static String dataKB = "KB";
	public final static String dataMB = "MB";
	public final static String dataGB = "GB";
	public final static String dataTB = "TB";
	
	public final static String domesticTitleVoice = "Domestic (min)"; 
	public final static String roamingTitleVoice = "Roaming (min)";
	public final static String domesticTitleDataGB = "Domestic (GB)"; 
	public final static String roamingTitleDataGB = "Roaming (GB)";
	public final static String domesticTitleMessages = "Domestic"; 
	public final static String roamingTitleMessages = "Roaming";
	
	public final static String domesticLegend = "Domestic";
	public final static String domesticOverageLegend = "Domestic Overage";
	public final static String roamingLegend = "Roaming";
	
	public final static String totalUsageByVendor = "Total Usage by Vendor - ";
	public final static String totalUsageByCountry = "Total Usage by Country - ";
	public final static String usageTrendingByVendor = "Usage by Vendor - ";	
	public final static String usageTrendingByCountry = "Usage by Country - ";
	
	public final static String colorLegendEnabled = "color: rgb(51, 51, 51)";
	public final static String colorLegendDisabled = "color: rgb(204, 204, 204)";
	
	public final static int totalUsageSection = 0;
	public final static int usageTrendingSection = 1;
	
	public final static int totalUsageDomesticChart = 0;
	public final static int totalUsageRoamingChart = 1;
	public final static int usageTrendingDomesticChart = 2;
	public final static int usageTrendingRoamingChart = 3;
	
	public final static int categoryVoice = 1;
	public final static int categoryData = 2;
	public final static int categoryMessages = 3;
	

	public static void selectVendorView(){
		
		WaitForElementClickable(By.cssSelector("#md-tab-label-1-0"), MediumTimeout, "Vendor View Selector is not clickable.");
		driver.findElement(By.cssSelector("#md-tab-label-1-0")).click();
		
	}

	
	public static void selectCountryView(){
		
		WaitForElementClickable(By.cssSelector("#md-tab-label-1-1"), MediumTimeout, "Country View Selector is not clickable.");
		driver.findElement(By.cssSelector("#md-tab-label-1-1")).click();
		
	}
	
	
	// It returns the id of the WebElement chart. E.g.: "highchart-45"
	public static String getChartId(int num){
		
		// Get all the charts listed in the page
		List<WebElement> charts = driver.findElements(By.cssSelector("chart>div"));
		
		// Get the id of specified chart 
		String chartId = charts.get(num).getAttribute("id");
		
		return chartId;
			
		
	}
	
	
	// It returns a list with one list per country. 
	// The first element of each list the country name, the remaining elements are the vendors for that country 
	public static List<List<WebElement>> getCountriesAndVendors() {

		
		List<List<WebElement>> countriesAndVendors = new ArrayList<>();
		
		List<WebElement> countries = driver.findElements(By.cssSelector(".tdb-povGroup__label--subhead"));
		List<WebElement> vendors = driver.findElements(By.cssSelector(".tdb-povGroup__label--subhead~div"));
		
		
		for (int i = 0; i < countries.size(); i++){
			
			List<WebElement> temp = new ArrayList<WebElement>();
			
			temp.add(countries.get(i));
			//System.out.println(countries.get(i).getText());
			
			List<WebElement> vendorsUnderCountry = vendors.get(i).findElements(By.cssSelector(".tdb-povGroup__label--subhead~div>div>md-checkbox>label>span")); 
			
			for (WebElement v : vendorsUnderCountry){
				temp.add(v);
				//System.out.println("  " + v.getText());
			}
			//System.out.println("  temp size: " + temp.size());
			countriesAndVendors.add(temp);
			
		}
		
		//System.out.println("countriesAndVendors size: " + countriesAndVendors.size()); 
			
		return countriesAndVendors;
	}
	
	
	
	
	public static void selectCategory(int section, int category){
		
		String sectionToSelect = "";
		if(section == totalUsageSection) sectionToSelect = "odd"; 
		if(section == usageTrendingSection) sectionToSelect = "even";
		
		WebElement categoryToSelect = driver.findElement(By.cssSelector(".tdb-boxSelector.tdb-align--right:nth-child(" + sectionToSelect + ")>div:nth-of-type(" + category + ")"));
		categoryToSelect.click();
		
	}
	
	
	// Move to chart in parameters
	public static void moveToUsageTrending(){
		
		WebElement section = driver.findElement(By.cssSelector(".tdb-card:nth-of-type(3)"));
		new Actions(driver).moveToElement(section).perform();
		
	}
	
	
	// Used to scroll down simulating the mouse wheel moves
	public static void scrollMouseToChart(int num){
		
		try{
			Robot robot = new Robot(); 
			robot.mouseWheel(num);
		}catch (AWTException e){
			e.printStackTrace();
		}
		
		
	}
	
	
	public static String removePunctuationCharacters(String string){
		
		return string.replace(".", "").replace("\"", "").replace(",",""); 
		
	}


	public static List<UsageOneMonth> addMissingMonthsForVendor(List<UsageOneMonth> valuesFromFileTmp) throws ParseException {
		
		List<UsageOneMonth> valuesFromFileNew = new ArrayList<>();
		List<WebElement> monthsList;
		
		CommonTestStepActions.initializeMonthSelector();
		monthsList = CommonTestStepActions.webListPulldown;
		
		boolean previousMonthExisted = true;
		UsageOneMonth currentMonth;
		String month = "";
		String year =  "";
		String monthYear = "";
		String vendorName = valuesFromFileTmp.get(0).getVendorName();
		int fileIndex = 0;
		
		for(int i = 0; i < 13; i++){
			
			if(previousMonthExisted){
				
				currentMonth = valuesFromFileTmp.get(fileIndex);
				month = currentMonth.getOrdinalMonth();
				year =  currentMonth.getOrdinalYear();
				monthYear = CommonTestStepActions.convertMonthNumberToName(month, year);
				
			} 
			
			if (!monthYear.equals(monthsList.get(i).getText())){
				/*System.out.println("fileIndex: " + (fileIndex));
				System.out.println("month from monthsList: " + monthsList.get(i).getText());
				System.out.println("monthYear from file: " + monthYear);  
				*/
				String[] monthYearParts = monthsList.get(i).getText().split(" "); 
				String monthNum = CommonTestStepActions.ConvertMonthToInt(monthYearParts[0]);
				if(monthNum.length() == 1){
					monthNum = "0" + monthNum;
				}
				
				UsageOneMonth usageMonth = new UsageOneMonth(vendorName, monthYearParts[1], monthNum);
				valuesFromFileNew.add(i, usageMonth);
				previousMonthExisted = false;
				
			} else {
				
				String monthTmp = valuesFromFileTmp.get(fileIndex).getOrdinalMonth();
				if(monthTmp.length() == 1){
					monthTmp = "0" + monthTmp;
				}
				
				UsageOneMonth usageMonthTmp = valuesFromFileTmp.get(fileIndex);
				usageMonthTmp.setOrdinalMonth(monthTmp);
				
				valuesFromFileNew.add(i, usageMonthTmp);
				//System.out.println("monthYear from file: " + valuesFromFileNew.get(i).getOrdinalMonth() + " " + valuesFromFileNew.get(i).getOrdinalYear());
				previousMonthExisted = true;
				fileIndex++;
			}
			
		}
		
		/*for(UsageOneMonth m: valuesFromFileNew){
			System.out.println("month: " + m.getOrdinalMonth() + " " + m.getOrdinalYear());
		}*/
		
		return valuesFromFileNew;
		
	}


	public static List<UsageOneMonth> summarizeDataVendorsSelected(List<List<UsageOneMonth>> data) throws ParseException {
		
		List<UsageOneMonth> dataSummarized = new ArrayList<>();
		
		CommonTestStepActions.initializeMonthSelector();
		List<String> months = CommonTestStepActions.YearMonthIntergerFromPulldown(); 
		
		boolean monthEqualToInvoiceMonth = false;
		
		if(data.get(0).get(0).getOrdinalMonth().equals(getMonthOfInvoiceMonth(data.get(0).get(0).getInvoiceMonth()))){
			monthEqualToInvoiceMonth = true;
		}
		
				
		for(int i = 0; i < months.size(); i++){
		
			String[] monthYear = getMonthYearSeparated(months.get(i));
			String month = monthYear[0];
			String year = monthYear[1];
			
			UsageOneMonth usageSummarized = new UsageOneMonth("", year, month);
						
			//System.out.println("Month: " + month + ", Year: " + year);
			
			for(int j = 0; j < data.size(); j++){
			
				//System.out.println("j: " + j);
				//System.out.println("data " + j + " size: " + data.get(j).size()); 
				
				boolean invoiceMonthAssigned = false;
				
				for(UsageOneMonth usage: data.get(j)){
					
					if(month.equals(usage.getOrdinalMonth()) && year.equals(usage.getOrdinalYear())){
												
						usageSummarized.setDomesticVoice(Double.toString(Double.parseDouble(usageSummarized.getDomesticVoice()) + Double.parseDouble(usage.getDomesticVoice())));
						
						usageSummarized.setDomesticOverageVoice(Double.toString(Double.parseDouble(usageSummarized.getDomesticOverageVoice()) + Double.parseDouble(usage.getDomesticOverageVoice())));
						
						usageSummarized.setDomesticMessages(Double.toString(Double.parseDouble(usageSummarized.getDomesticMessages()) + Double.parseDouble(usage.getDomesticMessages())));
						
						usageSummarized.setDomesticDataUsageKb(Double.toString(Double.parseDouble(usageSummarized.getDomesticDataUsageKb()) + Double.parseDouble(usage.getDomesticDataUsageKb())));
						
						usageSummarized.setRoamingVoice(Double.toString(Double.parseDouble(usageSummarized.getRoamingVoice()) + Double.parseDouble(usage.getRoamingVoice())));
						
						usageSummarized.setRoamingMessages(Double.toString(Double.parseDouble(usageSummarized.getRoamingMessages()) + Double.parseDouble(usage.getRoamingMessages())));
						
						usageSummarized.setRoamingDataUsageKb(Double.toString(Double.parseDouble(usageSummarized.getRoamingDataUsageKb()) + Double.parseDouble(usage.getRoamingDataUsageKb())));
						
						if(!invoiceMonthAssigned){
							usageSummarized.setInvoiceMonth(usage.getInvoiceMonth());
							invoiceMonthAssigned = true;
						}
						
						/*System.out.println("Domestic Voice previous value: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticVoice())));
						System.out.println("Domestic Voice value to be added: " + Double.toString(Double.parseDouble(usage.getDomesticVoice())));
						System.out.println("Domestic Voice summarized: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticVoice()) + Double.parseDouble(usage.getDomesticVoice())));
						
						System.out.println("Domestic Overage Voice previous value: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticOverageVoice())));
						System.out.println("Domestic Overage Voice value to be added: " + Double.toString(Double.parseDouble(usage.getDomesticOverageVoice())));
						System.out.println("Domestic Overage Voice summarized: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticOverageVoice()) + Double.parseDouble(usage.getDomesticOverageVoice())));
						
						System.out.println("Domestic Messages previous value: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticMessages())));
						System.out.println("Domestic Messages value to be added: " + Double.toString(Double.parseDouble(usage.getDomesticMessages())));
						System.out.println("Domestic Messages summarized: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticMessages()) + Double.parseDouble(usage.getDomesticMessages())));
						
						System.out.println("Domestic Data previous value: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticDataUsageKb())));
						System.out.println("Domestic Data value to be added: " + Double.toString(Double.parseDouble(usage.getDomesticDataUsageKb())));
						System.out.println("Domestic Data summarized: " + Double.toString(Double.parseDouble(usageSummarized.getDomesticDataUsageKb()) + Double.parseDouble(usage.getDomesticDataUsageKb())));
						
						System.out.println("Roaming Voice previous value: " + Double.toString(Double.parseDouble(usageSummarized.getRoamingVoice())));
						System.out.println("Roaming Voice value to be added: " + Double.toString(Double.parseDouble(usage.getRoamingVoice())));
						System.out.println("Roaming Voice summarized: " + Double.toString(Double.parseDouble(usageSummarized.getRoamingVoice()) + Double.parseDouble(usage.getRoamingVoice())));
						
						System.out.println("Roaming Messages previous value: " + Double.toString(Double.parseDouble(usageSummarized.getRoamingMessages())));
						System.out.println("Roaming Messages value to be added: " + Double.toString(Double.parseDouble(usage.getRoamingMessages())));
						System.out.println("Roaming Messages summarized: " + Double.toString(Double.parseDouble(usageSummarized.getRoamingMessages()) + Double.parseDouble(usage.getRoamingMessages())));
						
						System.out.println("Roaming Data previous value: " + Double.toString(Double.parseDouble(usageSummarized.getRoamingDataUsageKb())));
						System.out.println("Roaming Data value to be added: " + Double.toString(Double.parseDouble(usage.getRoamingDataUsageKb())));
						System.out.println("Roaming Data summarized: " + Double.toString(Double.parseDouble(usageSummarized.getRoamingDataUsageKb()) + Double.parseDouble(usage.getRoamingDataUsageKb())));
				*/		
					}
			
				}
			
			}
			
			dataSummarized.add(usageSummarized);
			 
		}
		
		dataSummarized = addMissingInvoiceMonths(dataSummarized, monthEqualToInvoiceMonth);
				
		return dataSummarized;
		
	}
	
	
	
	private static String[] getMonthYearSeparated(String date){
		
		String [] dateParts = date.split("-");
		return dateParts;
		
	}


	public static String[] getMonthYearToSelect(UsageOneMonth oneMonthData) {

		String[] monthAndYear = new String[2];
		String[] invoiceMonthParts = oneMonthData.getInvoiceMonth().split("/");
		//System.out.println("invoiceMonth: " + oneMonthData.getInvoiceMonth()); 
		//System.out.println("invoiceMonthParts[0]: " + invoiceMonthParts[0]); 
		
		// If the month in invoice_month is the month before ordinal_month
		// E.g.: invoice_month = 9/1/2016 and ordinal_month = 8
		if(!invoiceMonthParts[0].equals(oneMonthData.getOrdinalMonth()) && !oneMonthData.getInvoiceMonth().equals("")){
			
			if(oneMonthData.getOrdinalMonth().equals("1")){
				monthAndYear[0] = "12"; // month
				monthAndYear[1] = Integer.toString(Integer.parseInt(oneMonthData.getOrdinalYear()) - 1);  // year
			}else{
				monthAndYear[0] = Integer.toString((Integer.parseInt(oneMonthData.getOrdinalMonth()) - 1));  // month
				monthAndYear[1] =  oneMonthData.getOrdinalYear();  // year
			}
			//System.out.println("Invoice Month - option 1: " + oneMonthData.getInvoiceMonth());
			
		} 
		
		// Else if the month in invoice_month is the same as ordinal_month
		// E.g.: invoice_month = 9/1/2016 and ordinal_month = 9
		else if(invoiceMonthParts[0].equals(oneMonthData.getOrdinalMonth()) && !oneMonthData.getInvoiceMonth().equals("")){
		
			monthAndYear[0] = oneMonthData.getOrdinalMonth();
			monthAndYear[1] =  oneMonthData.getOrdinalYear();
			//System.out.println("Invoice Month - option 2: " + oneMonthData.getInvoiceMonth());
		}
		
		//System.out.println("InvoiceMonth: " + oneMonthData.getInvoiceMonth());
		//System.out.println("month: " + monthAndYear[0] + ", year: " + monthAndYear[1]);
		
		return monthAndYear;
		
	}
	
	
	public static String getMonthOfInvoiceMonth(String invoiceMonth){
		
		String[] invoiceMonthParts = invoiceMonth.split("/");
		return invoiceMonthParts[0];
		
	}
	
	
	public static List<UsageOneMonth> addMissingInvoiceMonths(List<UsageOneMonth> usageList, boolean monthEqualToInvoiceMonth){
		
		if(monthEqualToInvoiceMonth){
			
			for(UsageOneMonth u: usageList){
				
				if(u.getInvoiceMonth().equals("")){
					
					String date = u.getOrdinalMonth() + "/1/" + u.getOrdinalYear();
					u.setInvoiceMonth(date);
					
				}
				
			}
			
		} else {
			
			for(UsageOneMonth u: usageList){
			
				String month;
				String year;
				
				if(u.getOrdinalMonth().equals("1")){
					month = "12";
					year = Integer.toString(Integer.parseInt(u.getOrdinalYear()) - 1); 
				} else {
					month = Integer.toString(Integer.parseInt(u.getOrdinalMonth()) - 1);
					year = u.getOrdinalYear();
				}
				
				String date = month + "/1/" + year; 
				u.setInvoiceMonth(date);
			}
			
			
		}
		
		return usageList;
		
	}
	
	
}
