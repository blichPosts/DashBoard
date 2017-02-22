package expenseHierarchy;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import Dash.BaseClass;
import helperObjects.GeneralHelper;
import helperObjects.HierarchyHelper;
import helperObjects.UsageCalculationHelper;
import helperObjects.UsageHelper;
import helperObjects.UsageOneMonth;


public class HierarchyTreeMap extends BaseClass {
	
	
	public static void hoverThroughTiles() throws AWTException, InterruptedException {
		
		String chartId = UsageHelper.getChartId(HierarchyHelper.treeMapChart);
		
		WebElement treeMapChart = driver.findElement(By.cssSelector("#" + chartId));
		new Actions(driver).moveToElement(treeMapChart).perform();
		
		driver.findElement(By.cssSelector(".tdb-h3>div>span>select>option:last-child"));
		
		List<WebElement> tiles = driver.findElements(By.cssSelector("#" + chartId + ">svg>g.highcharts-series-group>g>g>rect"));
		
		for (int i = 1; i <= tiles.size(); i++) {
			
			WebElement tileNumber = driver.findElement(By.cssSelector("#" + chartId + ">svg>g>g.highcharts-label:nth-of-type(" + i + ")"));
			
			Point p = GeneralHelper.getAbsoluteLocation(tileNumber);
			
			int x_offset = tileNumber.getSize().getHeight() / 2;
			int y_offset = tileNumber.getSize().getWidth() / 2;
			
			int x = p.getX() + x_offset;
			int y = p.getY() + y_offset;
			
			Robot robot = new Robot();
			robot.mouseMove(x, y);
					
			Thread.sleep(2000);
		
			WebElement tooltip = driver.findElement(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip"));
			System.out.println(i + ") Tooltip text: " + tooltip.getText());
			
		}
		 
		
		
	}
	
	
	
	
	
	
	// Verifies the content of the tooltips displayed on charts under Total Usage Domestic and Roaming charts
	// **** ONLY FOR ONE VENDOR SELECTED ****
	public static void verifyTotalUsageChartTooltip(int barChartId, UsageOneMonth oneMonthData, int categorySelector) throws ParseException, InterruptedException, AWTException {
		
		String chartId = UsageHelper.getChartId(barChartId);
		
		// It gets the legends for "Domestic" and "Domestic Overage" or "Roaming"
		List<WebElement> legends = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-legend>g>g>g>text"));
		
		int indexHighchart = 1;
		
		List<WebElement> vendorsInChart = driver.findElements(By.cssSelector("#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text>tspan"));
		List<String> vendorsInChartList = new ArrayList<String>();
		
		for(int i = 0; i < vendorsInChart.size(); i++){
			vendorsInChartList.add(vendorsInChart.get(i).getText());
		}		
				
//			Thread.sleep(2000);
		
		String domesticValue = "";
		String overageValue = "";
		String roamingValue = "";
		int expectedAmountItemsTooltip = 4;
		
		// Get the data and set up the expected values to be compared to the values found on the tooltips
		if (barChartId == 0) {
		
			if(categorySelector == UsageHelper.categoryVoice){
				
				domesticValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getDomesticVoice()), false);
				overageValue = oneMonthData.getDomesticOverageVoice();
				expectedAmountItemsTooltip = 7;  // The amount of items expected in the tooltip is 7 only if chart is Domestic and category is Voice 
				
			} else if (categorySelector == UsageHelper.categoryData){
				
				domesticValue = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(oneMonthData.getDomesticDataUsageKb()));
				
			} else if (categorySelector == UsageHelper.categoryMessages){
				
				domesticValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getDomesticMessages()), false);
				
			}
			
		} else if (barChartId == 1) {
			
			if(categorySelector == UsageHelper.categoryVoice){
				
				roamingValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getRoamingVoice()), false);
				
			} else if (categorySelector == UsageHelper.categoryData){
				
				roamingValue = UsageCalculationHelper.convertDataUnitToGbNoDecimalPoint(Double.parseDouble(oneMonthData.getRoamingDataUsageKb()));
				
			} else if (categorySelector == UsageHelper.categoryMessages){
				
				roamingValue = UsageCalculationHelper.roundNoDecimalDigits(Double.parseDouble(oneMonthData.getRoamingMessages()), false);
				
			}
			
		}
		
		
		// Verify the info contained on each of the tooltips for all the vendors listed in chart 		
		while(indexHighchart <= vendorsInChartList.size()){
			
			String cssSelector = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
			//System.out.println("cssSelector: " + cssSelector);
			
			// The 'bar' WebElement will be used to set the position of the mouse on the chart
			WebElement bar = driver.findElement(By.cssSelector(cssSelector));

			// Get the location of the series located at the bottom of the chart, to simulate the mouse hover so the tooltip is displayed
			Point coordinates = bar.getLocation();
			Robot robot = new Robot(); 
			robot.mouseMove((coordinates.getX() + 5), coordinates.getY() + 70); // these coordinates work for REF APP :)
			
			if (loginType.equals(LoginType.Command) || loginType.equals(LoginType.ReferenceApp)) {
				robot.mouseMove((coordinates.getX() + 5), coordinates.getY() + 200); // these coordinates work for CMD :)
			}
			
			if (!(bar.getAttribute("height").toString().equals("0"))){
				bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed

			}
			
			if (bar.getAttribute("height").toString().equals("0")){
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
			
			Thread.sleep(2000);
			Assert.assertEquals(tooltip.size(), expectedAmountItemsTooltip);
			
			
//				for(WebElement w : tooltip){
//					System.out.println("Item tooltip: " + w.getText());
//				}
//				
			
			// Verify country/vendor shown on the tooltip
			//System.out.println("Tooltip text: " + tooltip.get(0).getText());
						
			String vendorNameFound = tooltip.get(0).getText();
			String vendorNameExpected = oneMonthData.getVendorName();
			
			System.out.println("firstLineFound: " + vendorNameFound + ", firstLineExpected: " + vendorNameExpected);
			Assert.assertEquals(vendorNameFound, vendorNameExpected);
									
			
			// Verify the label and the amount shown on the tooltip
			for(int i = 1; i <= legends.size(); i++){
			
				int index =  i * 3 - 1;
								
				// Get the label and remove colon at the end of its text 
				String labelFound = tooltip.get(index).getText().replace(":", "");   //substring(0, tooltip.get(index).getText().length()-1);

				// Get the value on tooltip and remove all blank spaces
				String valueFound = tooltip.get(index+1).getText().trim().replace(" ", "");
				
				System.out.println("label: " + labelFound + ", value: " + valueFound); 
							
				// Verify the labels' text and amounts shown on the tooltip 					
				if (barChartId == UsageHelper.totalUsageDomesticChart) {
					
					if (index == 2) {
						Assert.assertEquals(labelFound, "Domestic");
						Assert.assertEquals(valueFound, domesticValue);
					}
					
					if (categorySelector == UsageHelper.categoryVoice && index == 5) {
						
						Assert.assertEquals(labelFound, "Domestic Overage"); 
						Assert.assertEquals(valueFound, overageValue);
					}
					
				} else if (barChartId == UsageHelper.totalUsageRoamingChart) {
					
					Assert.assertEquals(labelFound, "Roaming"); 
					Assert.assertEquals(valueFound, roamingValue);
					
				}

			}
			
			indexHighchart++;
			
		}
		
	}


}
