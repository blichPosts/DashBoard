package usage;

import java.awt.AWTException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.FleetHelper;
import helperObjects.GeneralHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class TotalUsageValues extends BaseClass {

	
	private static HashMap<String, UsageOneMonth> usageMap;
	private static HashMap<String, String> domesticValues;
	private static HashMap<String, String> overageValues;
	private static HashMap<String, String> roamingValues;
	
	private static List<String> vendorsSelectedCheckBox;
	private static List<String> countriesSelectedCheckBox;
	
	private static List<WebElement> vendorsInChart;
	private static List<WebElement> countriesInChart;
	
	private static List<String> vendorsInChartNames;
	private static List<String> countriesInChartNames;
	
	private static int expectedAmountItemsTooltip = 4;
	private static int chartNum;
	private static int category;
	
	
	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts - FOR VENDOR VIEW
	public static void verifyTotalUsageChartTooltipByVendor(int barChartId, List<UsageOneMonth> listOneMonthData, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		chartNum = barChartId; 
		
		category = categorySelector;
		
		vendorsSelectedCheckBox = FleetHelper.getVendorsSelected();
		
		vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++){
			
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
	
		ViewType view = ViewType.vendor;
		
		getExpectedValues(listOneMonthData, view);
	
		calculateOtherExpectedValues(view);
		
		
		int indexVendorSelected = 0;
		int indexVendorInChart = 0;
		int indexHighchart = 1;
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		while (indexVendorSelected < vendorsSelectedCheckBox.size() && indexVendorInChart < vendorsInChartNames.size()){
			
			// If the vendor in vendorsSelectedCheckBox list is present in the vendorsInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next vendor
			if (vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected)) || vendorsInChartNames.get(indexVendorInChart).equals("Other")){
				
				GeneralHelper.moveMouseToBar(chartId, indexHighchart, barChartId, category);
		
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
								
				// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
				
				// For Domestic chart: 
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 Domestic:
				// 3 <Amount for Domestic>
				// 4 ? --> bullet
				// 5 Domestic Overage:
				// 6 <Amount for Domestic Overage>
				
				// For Roaming chart: 
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 Roaming:
				// 3 <Amount for Roaming>
				
				// Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify vendor name shown on the tooltip
				String vendorNameFound = tooltip.get(0).getText();
				String vendorNameExpected = vendorsInChartNames.get(indexVendorInChart);
					
				Assert.assertEquals(vendorNameFound, vendorNameExpected);
				ShowText("vendor name found: " + vendorNameFound + ", vendor name expected: " + vendorNameExpected);
				
				
				// Verify the label and the amount shown on the tooltip
				for(int i = 1; i <= legends.size(); i++){
				
					int index = i * 2 + 1;
									
					// Get the label and remove colon at the end of its text 
					String labelFound = tooltip.get(index).getText().split(":")[1].trim();
	
					// Get the value on tooltip and remove all blank spaces
					String valueFound = tooltip.get(index).getText().split(":")[2].trim();

					verifyValuesFound(labelFound, valueFound, vendorNameExpected, index);
									
				}
				
				indexHighchart++;
				indexVendorInChart++;
				
			}
			
			// Only add 1 to indexVendorSelected if the amount of checkboxes checked has not been reached  
			if ((vendorsSelectedCheckBox.size() - indexVendorSelected) > 1)
				indexVendorSelected++;
			
		}	
		
	}


	
	// **** NEW ****** June 14 ******** 
	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts - FOR COUNTRY VIEW
	public static void verifyTotalUsageChartTooltipByCountry(int barChartId, List<UsageOneMonth> listOneMonthData, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		chartNum = barChartId; 
		
		category = categorySelector;
		
		countriesSelectedCheckBox = FleetHelper.getCountriesSelected(); 
		
		countriesInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		
		countriesInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < countriesInChart.size(); i++){
			
			countriesInChartNames.add(countriesInChart.get(i).getText());
		}	
		
		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		ViewType view = ViewType.country;
		
		getExpectedValues(listOneMonthData, view);
		
		calculateOtherExpectedValues(view);
		
		
		int indexCountrySelected = 0;
		int indexCountryInChart = 0;
		int indexHighchart = 1;
		
		// Verify the info contained on each of the tooltips for all the countries listed in chart 		
		while (indexCountrySelected < countriesSelectedCheckBox.size() && indexCountryInChart < countriesInChartNames.size()){
			
			// If the country in countriesSelectedCheckBox list is present in the countriesInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next country
			if (countriesInChartNames.contains(countriesSelectedCheckBox.get(indexCountrySelected)) || countriesInChartNames.get(indexCountryInChart).equals("Other")){
				
				GeneralHelper.moveMouseToBar(chartId, indexHighchart, barChartId, category);
		
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
								
				// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
				
				// For Domestic chart: 
				// 0 <country name>
				// 1 ? -- this is for the bullet
				// 2 Domestic:
				// 3 <Amount for Domestic>
				// 4 ? --> bullet
				// 5 Domestic Overage:
				// 6 <Amount for Domestic Overage>
				
				// For Roaming chart: 
				// 0 <country name>
				// 1 ? -- this is for the bullet
				// 2 Roaming:
				// 3 <Amount for Roaming>
				
				// Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify country shown on the tooltip
				String countryNameFound = tooltip.get(0).getText();
				String countryNameExpected = countriesInChartNames.get(indexCountryInChart);
					
				Assert.assertEquals(countryNameFound, countryNameExpected);
				ShowText("country name found: " + countryNameFound + ", country name expected: " + countryNameExpected);
				
				
				// Verify the label and the amount shown on the tooltip
				for(int i = 1; i <= legends.size(); i++){
				
					int index = i * 2 + 1;
									
					// Get the label and remove colon at the end of its text 
					String labelFound = tooltip.get(index).getText().split(":")[1].trim();
	
					// Get the value on tooltip and remove all blank spaces
					String valueFound = tooltip.get(index).getText().split(":")[2].trim();

					verifyValuesFound(labelFound, valueFound, countryNameExpected, index);
									
				}
				
				indexHighchart++;
				indexCountryInChart++;
				
			}
			
			// Only add 1 to indexVendorSelected if the amount of checkboxes checked has not been reached  
			if ((countriesSelectedCheckBox.size() - indexCountrySelected) > 1)
				indexCountrySelected++;
			
		}	
		
	}

		
		
	
	private static void getExpectedValues(List<UsageOneMonth> listOneMonthData, ViewType view) {
		
		usageMap = new HashMap<String, UsageOneMonth>();
		
		for (UsageOneMonth u: listOneMonthData) {
			
			if (view.equals(ViewType.country)) {
				
				usageMap.put(u.getCountry(), u);
				
			} else if (view.equals(ViewType.vendor)) {
				
				usageMap.put(u.getVendorName(), u);
				
			}
			
		}
		
		
		// The HashMaps<vendorName/countryName, value>  will contain the expected values
		domesticValues = new HashMap<String, String>();
		overageValues = new HashMap<String, String>();
		roamingValues = new HashMap<String, String>();
		
		
		for (int i = 0; i < listOneMonthData.size(); i++) {
			
			String itemName = "";
			
			if (view.equals(ViewType.country)) {
				
				itemName = listOneMonthData.get(i).getCountry();
				
			} else if (view.equals(ViewType.vendor)) {
				
				itemName = listOneMonthData.get(i).getVendorName();
				
			}
			
					
			if (chartNum == UsageHelper.totalUsageDomesticChart) {
				
				if(category == UsageHelper.categoryVoice){
					
					domesticValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticVoice()), false));
					overageValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticOverageVoice()), false));
					expectedAmountItemsTooltip = 7;  // The amount of items expected in the tooltip is 7 only if chart is Domestic and category is Voice 
					
				} else if (category == UsageHelper.categoryData){
					
					domesticValues.put(itemName, UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(listOneMonthData.get(i).getDomesticDataUsageKb())));
					
				} else if (category == UsageHelper.categoryMessages){
					
					domesticValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDomesticMessages()), false));
					
				}
				
			} else if (chartNum == UsageHelper.totalUsageRoamingChart) {
				
				if(category == UsageHelper.categoryVoice){
					
					roamingValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingVoice()), false));
					
				} else if (category == UsageHelper.categoryData){
					
					roamingValues.put(itemName, UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(listOneMonthData.get(i).getRoamingDataUsageKb())));
					
				} else if (category == UsageHelper.categoryMessages){
					
					roamingValues.put(itemName, UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingMessages()), false));
					
				}
				
			}
			
		}
		
	}
	
	
	
	private static void calculateOtherExpectedValues(ViewType view) {
		
		int amountItemsSelected = 0;
		int amountItemsInChart = 0;
		
		List<String> itemsSelectedCheckbox = new ArrayList<>();
		List<String> itemsInChartNames = new ArrayList<>();
		
		if (view.equals(ViewType.country)) {
			
			amountItemsSelected = countriesSelectedCheckBox.size();
			amountItemsInChart = countriesInChartNames.size();
			
			itemsSelectedCheckbox.addAll(countriesSelectedCheckBox);
			itemsInChartNames.addAll(countriesInChartNames);
			
		} else if (view.equals(ViewType.vendor)) {
			
			amountItemsSelected = vendorsSelectedCheckBox.size();
			amountItemsInChart = vendorsInChartNames.size();
			
			itemsSelectedCheckbox.addAll(vendorsSelectedCheckBox);
			itemsInChartNames.addAll(vendorsInChartNames);
		}
		
		
		boolean moreThanFiveItemsSelected = (amountItemsSelected > 5);
		boolean sixItemsInChart = (amountItemsInChart == 6);
		
		String domesticValueOther = "";
		String overageValueOther = "";
		String roamingValueOther = "";
		String otherLabel = "Other";
	
		// If more than 5 vendors/countries are selected on the PoV section and there are 6 vendors/countries in chart,  
		// then the data for one or more vendors/countries is summarized in the "Other" item.
		if (moreThanFiveItemsSelected && sixItemsInChart) {
			
			double domesticTmpSum = 0;
			double overageTmpSum = 0;
			double roamingTmpSum = 0;
			
			
			for (int i = 0; i < amountItemsSelected; i++){
				
				String item = itemsSelectedCheckbox.get(i);
				
				if (!itemsInChartNames.contains(item)){
					
					// ShowText("Vendor " + v + ", is not listed in chart");
				
					UsageOneMonth usage = (UsageOneMonth) usageMap.get(item);

					// If there's data for the selected month/vendor add the values to the "other" variables, if not, move to the next vendor
					boolean usageNull;
					
					try {
						
						usage.getDomesticVoice();
						usageNull = false;
						
					} catch (NullPointerException e) {
						
						usageNull = true;
						
					}
					
					if (!usageNull) {
						
						if (chartNum == UsageHelper.totalUsageDomesticChart) {
							
							if(category == UsageHelper.categoryVoice){
								
								domesticTmpSum += Double.parseDouble(usage.getDomesticVoice());
								domesticValueOther = UsageCalculationHelper.roundNoDecimalDigits(domesticTmpSum, false);
								
								overageTmpSum += Double.parseDouble(usage.getDomesticOverageVoice());
								overageValueOther = UsageCalculationHelper.roundNoDecimalDigits(overageTmpSum, false);
								overageValues.put(otherLabel, overageValueOther);
								
							} else if (category == UsageHelper.categoryData){
								
								domesticTmpSum += Double.parseDouble(usage.getDomesticDataUsageKb());
								domesticValueOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(domesticTmpSum);
								
							} else if (category == UsageHelper.categoryMessages){
								
								domesticTmpSum += Double.parseDouble(usage.getDomesticMessages());
								domesticValueOther = UsageCalculationHelper.roundNoDecimalDigits(domesticTmpSum, false);
								
							}
							
							domesticValues.put(otherLabel, domesticValueOther);
							
							
						} else if (chartNum == UsageHelper.totalUsageRoamingChart) {
							
							if(category == UsageHelper.categoryVoice){
								
								roamingTmpSum += Double.parseDouble(usage.getRoamingVoice());
								roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum, false);
								
							} else if (category == UsageHelper.categoryData){
								
								roamingTmpSum += Double.parseDouble(usage.getRoamingDataUsageKb());
								roamingValueOther = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(roamingTmpSum);
								
							} else if (category == UsageHelper.categoryMessages){
								
								roamingTmpSum += Double.parseDouble(usage.getRoamingMessages());
								roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum, false);
								
							}
							
							roamingValues.put(otherLabel, roamingValueOther);
							
						}
						
					} 
						
				}

			}
			
		}
		
	}

	
	
	private static void verifyValuesFound(String labelFound, String valueFound, String vendorNameExpected, int index) {
		
		String valueExpected = "";
		String labelExpected = "";
		
		// Verify the labels' text and amounts shown on the tooltip 					
		if (chartNum == UsageHelper.totalUsageDomesticChart) {
			
			if (index == 3) {
				
				valueExpected = domesticValues.get(vendorNameExpected);
				labelExpected = "Domestic";
				
			}
			
			if (index == 5 && category == UsageHelper.categoryVoice) {
				
				valueExpected = overageValues.get(vendorNameExpected);
				labelExpected = "Domestic Overage";
				
			}
			
		} else if (chartNum == UsageHelper.totalUsageRoamingChart) {
			
			valueExpected = roamingValues.get(vendorNameExpected);
			labelExpected = "Roaming";
			
		}
		
		Assert.assertEquals(labelFound, labelExpected);
		
		ShowText("  label found: " + labelFound + ", label expected: " + labelExpected);
		ShowText("  value found: " + valueFound + ", value expected: " + valueExpected);

		GeneralHelper.verifyExpectedAndActualValues(valueFound, valueExpected);
		
	}

	
}
