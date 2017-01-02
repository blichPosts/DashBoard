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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
