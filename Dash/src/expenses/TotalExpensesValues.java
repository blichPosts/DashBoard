package expenses;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.ExpenseHelperMultipleVendors;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;

public class TotalExpensesValues extends BaseClass {

	
	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts
	// **** FOR ONE OR MORE VENDORS SELECTED ****
	public static void verifyTotalUsageChartTooltip(int barChartId, List<UsageOneMonth> listOneMonthData) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		List<WebElement> vendorsSelectedCheckBox = driver.findElements(By.cssSelector("md-checkbox.md-checkbox-checked>label>span"));
		
		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		
		HashMap<String, UsageOneMonth> vendorExpensesMap = new HashMap<String, UsageOneMonth>();
		for (UsageOneMonth u: listOneMonthData) {
			vendorExpensesMap.put(u.getVendorName(), u);
//				System.out.println(vendorExpensesMap.get(u.getVendorName()).getVendorName()); // + " - Domestic voice: " + u.getDomesticVoice());
		}
		
		
		Thread.sleep(2000);
		
		// The HashMap<vendorName, value>  will contain the expected values
		HashMap<String, String> voiceExpensesValues = new HashMap<String, String>();
		HashMap<String, String> dataExpensesValues = new HashMap<String, String>();
		HashMap<String, String> messagesExpensesValues = new HashMap<String, String>();
		HashMap<String, String> roamingExpensesValues = new HashMap<String, String>();
		HashMap<String, String> equipmentExpensesValues = new HashMap<String, String>();
		HashMap<String, String> taxesExpensesValues = new HashMap<String, String>();
		HashMap<String, String> otherExpensesValues = new HashMap<String, String>();
		HashMap<String, String> accountExpensesValues = new HashMap<String, String>();
		
		
		for(int i = 0; i < listOneMonthData.size(); i++){
			
			if (barChartId == ExpenseHelperMultipleVendors.expenseTotalExpenseBarChart) {
				
				voiceExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getVoiceCharges()), true));
				
				dataExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getDataCharges()), true));
				
				messagesExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getMessagesCharges()), true));
				
				roamingExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getRoamingCharges()), true));
				
				equipmentExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getEquipmentCharges()), true));
				
				taxesExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getTaxCharges()), true));
				
				otherExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getOtherCharges()), true));
				
				accountExpensesValues.put(listOneMonthData.get(i).getVendorName(), UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(listOneMonthData.get(i).getTotalAccountLevelCharges()), true));
				
			} else if (barChartId == ExpenseHelperMultipleVendors.expenseTotalExpensePieChart) {
				
				// *** TO DO *****
				
			}
			
		}
		
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> vendorsInChartNames = new ArrayList<String>();
		
		for (int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartNames.add(vendorsInChart.get(i).getText());
		}	
		
		
		boolean moreThanFiveVendorsSelected = vendorsSelectedCheckBox.size() > 5;
		boolean sixVendorsInChart = vendorsInChartNames.size() == 6;
		
		String voiceValueOther = "";
		String dataValueOther = "";
		String messagesValueOther = "";
		String roamingValueOther = "";
		String equipmentValueOther = "";
		String taxesValueOther = "";
		String otherValueOther = "";
		String accountValueOther = "";
		
		String otherVendors = "Other";
	
		// If more than 5 vendors are selected and there are 6 vendors in chart,  
		// then the vendors that have data for the selected month are summarized in the "Other" item.
		if (moreThanFiveVendorsSelected && sixVendorsInChart) {
			
			System.out.println("More Than 5 Vendors Selected: " + moreThanFiveVendorsSelected);
			System.out.println("6 Vendors in Chart: " + sixVendorsInChart);
			
			double voiceTmpSum = 0;
			double dataTmpSum = 0;
			double messagesTmpSum = 0;
			double roamingTmpSum = 0;
			double equipmentTmpSum = 0;
			double taxesTmpSum = 0;
			double otherTmpSum = 0;
			double accountTmpSum = 0;
			
			
			for (int i = 0; i < vendorsSelectedCheckBox.size(); i++){
				
				String v = vendorsSelectedCheckBox.get(i).getText();
				
				if (!vendorsInChartNames.contains(v)){
					
					System.out.println("Vendor " + v + ", is not listed in chart");
				
					UsageOneMonth usage = (UsageOneMonth) vendorExpensesMap.get(v);

					// If there's data for the selected month/vendor add the values to the "other" variables, if not, move to the next vendor
					boolean usageNull;
					
					try {
						
						usage.getDomesticVoice();
						usageNull = false;
						
					} catch (NullPointerException e) {
						
						usageNull = true;
						
					}
					
					if (!usageNull) {
						
						System.out.println("there's data for the vendor");
						
						if (barChartId == ExpenseHelperMultipleVendors.expenseTotalExpenseBarChart) {
							
							voiceTmpSum += Double.parseDouble(usage.getVoiceCharges());
							voiceValueOther = UsageCalculationHelper.roundNoDecimalDigits(voiceTmpSum, true);
							voiceExpensesValues.put(otherVendors, voiceValueOther);
							
							dataTmpSum += Double.parseDouble(usage.getDataCharges());
							dataValueOther = UsageCalculationHelper.roundNoDecimalDigits(dataTmpSum, true);
							dataExpensesValues.put(otherVendors, dataValueOther);
							
							messagesTmpSum += Double.parseDouble(usage.getMessagesCharges());
							messagesValueOther = UsageCalculationHelper.roundNoDecimalDigits(messagesTmpSum, true);
							messagesExpensesValues.put(otherVendors, messagesValueOther);
							
							roamingTmpSum += Double.parseDouble(usage.getRoamingCharges());
							roamingValueOther = UsageCalculationHelper.roundNoDecimalDigits(roamingTmpSum, true);
							roamingExpensesValues.put(otherVendors, roamingValueOther);
							
							equipmentTmpSum += Double.parseDouble(usage.getEquipmentCharges());
							equipmentValueOther = UsageCalculationHelper.roundNoDecimalDigits(equipmentTmpSum, true);
							equipmentExpensesValues.put(otherVendors, equipmentValueOther);
							
							taxesTmpSum += Double.parseDouble(usage.getTaxCharges());
							taxesValueOther = UsageCalculationHelper.roundNoDecimalDigits(taxesTmpSum, true);
							taxesExpensesValues.put(otherVendors, taxesValueOther);
							
							otherTmpSum += Double.parseDouble(usage.getOtherCharges());
							otherValueOther = UsageCalculationHelper.roundNoDecimalDigits(otherTmpSum, true);
							otherExpensesValues.put(otherVendors, otherValueOther);
							
							accountTmpSum += Double.parseDouble(usage.getTotalAccountLevelCharges());
							accountValueOther = UsageCalculationHelper.roundNoDecimalDigits(accountTmpSum, true);
							accountExpensesValues.put(otherVendors, accountValueOther);
							
							
						} else if (barChartId == ExpenseHelperMultipleVendors.expenseTotalExpensePieChart) {
							
							// **** TO DO *******
							
						}
						
					} else {
						
						System.out.println("there's NO data for the vendor");
						
					}
						
				}

			}
			
		}
		
		
		//System.out.println("domestic list size: " + domesticValue.size());
//			System.out.println("vendorsInChartNames: " + vendorsInChartNames.size()); 
		for(String s: vendorsInChartNames){
//				System.out.println("*** " + s);
		}
		
		//System.out.println("vendorsSelectedCheckBox: " + vendorsSelectedCheckBox.size()); 
		for(WebElement w: vendorsSelectedCheckBox){
//				System.out.println("*** " + w.getText());
		}

		
		int indexVendorSelected = 0;
		int indexVendorInChart = 0;
		int indexHighchart = 1;
		
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		
		while (indexVendorSelected < vendorsSelectedCheckBox.size() && indexVendorInChart < vendorsInChartNames.size()) {
			
//				System.out.println("indexVendorSelected: " + indexVendorSelected + ", vendorsSelectedCheckBox.size(): " + vendorsSelectedCheckBox.size());
//			    System.out.println("indexVendorInChart: " + indexVendorInChart + ", vendorsInChartNames.size(): " + vendorsInChartNames.size()); 
//				System.out.println("Vendor name chart: " +  vendorsInChartNames.get(indexVendorInChart));
//				System.out.println("Vendor name checkbox: " +  vendorsSelectedCheckBox.get(indexVendorSelected).getText());
//				System.out.println("Condition is: " +  vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected).getText()));
			
			// If the vendor in vendorsSelectedCheckBox list is present in the vendorsInChartList, or if the current element from chart is "Other", 
			// then run the test. Else, move to the next vendor
			if (vendorsInChartNames.contains(vendorsSelectedCheckBox.get(indexVendorSelected).getText()) || vendorsInChartNames.get(indexVendorInChart).equals("Other")) {
				
				String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
				//System.out.println("cssSelector: " + cssSelector);
				
				// The 'bar' WebElement will be used to set the position of the mouse on the chart
				WebElement bar = driver.findElement(By.cssSelector(cssSelector));
	
				// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
				Point coordinates = bar.getLocation();
				Robot robot = new Robot(); 
				robot.mouseMove((coordinates.getX()), coordinates.getY() + 70); // these coordinates work for REF APP :)
				
				if (loginType.equals(LoginType.Command) || loginType.equals(LoginType.ReferenceApp)) {
					robot.mouseMove((coordinates.getX() + 5), coordinates.getY() + 170); // these coordinates work for CMD :)
				}
				
				if (Double.parseDouble(bar.getAttribute("height").toString()) > 10.0) {   //if (!(bar.getAttribute("height").toString().equals("0"))){
					bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
					//firstBar = false;
				}
				
				if (Double.parseDouble(bar.getAttribute("height").toString()) < 10.0) {  //if (bar.getAttribute("height").toString().equals("0")){
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
					
				
				try {
					WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
					//System.out.println("Tooltip present");
				} catch (Exception e) {
					System.out.println("Tooltip NOT present");
					e.printStackTrace();
				}
				
				List<WebElement> tooltip = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"));
				
				// Verify that the amount of items in the tooltip equals to the (amount of series * 3) + 1: 
				
				// 0 <vendor/country name>
				// 1 ? -- this is for the bullet
				// 2 *spend category*: Voice, Data, Messages, Roaming, Equipment, Taxes, Other, Account
				// 3 <Amount for *spend category*>
				// ...
				// 25
				
				int expectedAmountItemsTooltip = 25; // 8 categories, 3 elements per category --> 24 + 1 vendor name = 25 items
				Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
				
				// Verify country/vendor shown on the tooltip
				String vendorNameFound = tooltip.get(0).getText();
				String vendorNameExpected = vendorsInChartNames.get(indexVendorInChart);
					
				Assert.assertEquals(vendorNameFound, vendorNameExpected);
				System.out.println("vendorNameFound: " + vendorNameFound + ", vendorNameExpected: " + vendorNameExpected);
				
				
				// Verify the label and the amount shown on the tooltip
				for (int i = 1; i <= legends.size(); i++) {
				
					int index =  i * 3 - 1;
					
					System.out.println("  index: " + index);
					
					// Get the label and remove colon at the end of its text 
					String labelFound = tooltip.get(index).getText().substring(0, tooltip.get(index).getText().length()-1);
	
					// Get the value on tooltip and remove all blank spaces
					String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
					
					String valueExpected = "";
					String labelExpected = "";
					
					// Verify the labels' text and amounts shown on the tooltip 					
					if (barChartId == ExpenseHelperMultipleVendors.expenseTotalExpenseBarChart) {
						
						switch (index) {
						
							case 2:
								valueExpected = voiceExpensesValues.get(vendorNameExpected);
								labelExpected = "Voice";
								break;
							case 5:
								valueExpected = dataExpensesValues.get(vendorNameExpected);
								labelExpected = "Data";
								break;
							case 8:
								valueExpected = messagesExpensesValues.get(vendorNameExpected);
								labelExpected = "Messages";
								break;
							case 11:
								valueExpected = roamingExpensesValues.get(vendorNameExpected);
								labelExpected = "Roaming";
								break;
							case 14:
								valueExpected = equipmentExpensesValues.get(vendorNameExpected);
								labelExpected = "Equipment";
								break;
							case 17:
								valueExpected = taxesExpensesValues.get(vendorNameExpected);
								labelExpected = "Taxes";
								break;
							case 20:
								valueExpected = otherExpensesValues.get(vendorNameExpected);
								labelExpected = "Other";
								break;
							case 23:
								valueExpected = accountExpensesValues.get(vendorNameExpected);
								labelExpected = "Account";
								break;
							
						}
						
					} else if (barChartId == ExpenseHelperMultipleVendors.expenseTotalExpensePieChart) {
						
						// *** TO DO ***
					}
					
					Assert.assertEquals(labelFound, labelExpected);
					Assert.assertEquals(valueFound, valueExpected);
					
					System.out.println("  labelFound: " + labelFound + ", labelExpected: " + labelExpected);
					System.out.println("  valueFound: " + valueFound + ", valueExpected: " + valueExpected);
	
				}
				
				indexHighchart++;
				indexVendorInChart++;
				
			}
			
			// Only add 1 to indexVendorSelected if the amount of checkboxes checked has not been reached  
			if ((vendorsSelectedCheckBox.size() - indexVendorSelected) > 1)
				indexVendorSelected++;
			
		}	
		
	}

	
}
