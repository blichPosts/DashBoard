package helperObjects;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Dash.BaseClass;


public class GeneralHelper extends BaseClass {

	
	public static WebElement iframe;  
	public static int x_iFrame = 0;
	public static int y_iFrame = 0;
	
	
	// Sets up the x and y coordinates of the CONTENT frame
	public static void setUpiFrame() {
		
		iframe = driver.findElement(By.cssSelector("iframe#CONTENT"));
		Point iframeLoc = iframe.getLocation();
		x_iFrame = iframeLoc.getX();
        y_iFrame = iframeLoc.getY();
        
	}
	
	
	// Switches to top frame
	public static void switchToTop() {
		
		driver.switchTo().defaultContent(); 
		
	}
	
	
	// Switches to content frame
	public static void switchToContentFrame() throws InterruptedException {
		
		if (loginType.equals(LoginType.Command)) {
	    
	    	driver.switchTo().frame(driver.findElement(By.id("CONTENT")));
		    
		    // this timeout is here because when at frame id "CONTENT" there is no DOM element to wait for.    
		    DebugTimeout(1, ""); 
		    
		    // this will get to dashboard frame. At this point the dashboard test code will wait for the dash page to load. 
	    	driver.switchTo().frame(driver.findElement(By.id("dashboard_iframe")));
	    	
	    }
	    
		if (loginType.equals(LoginType.MatrixPortal)) {
			
			driver.switchTo().frame(driver.findElement(By.id("iframe_MATRIX_ANALYTIC_DASHBOARDS")));
			
		}
	    
	}
	
	
	// Get the location of the element on the UI 
	public static Point getAbsoluteLocation(WebElement element) throws InterruptedException {
		
		int x = x_iFrame;
        int y = y_iFrame;
        
        int headerHeight = 0;
        
        if (loginType.equals(LoginType.Command)) {
        	
        	WebElement header = driver.findElement(By.cssSelector("header.tdb-flexContainer"));
        	headerHeight = header.getSize().getHeight();
        	
        } else if (loginType.equals(LoginType.MatrixPortal)) {
        
        	switchToTop();
        	
        	WebElement header = driver.findElement(By.id("headerPanel1"));
        	WebElement menuBar = driver.findElement(By.id("menuPanel"));
        	
        	headerHeight = header.getSize().getHeight() + menuBar.getSize().getHeight()*3;
        	
        	switchToContentFrame();
        }
        
        Point elementLoc = element.getLocation();
        
        x += elementLoc.getX();
        y += elementLoc.getY() + headerHeight;
        
        Point p = new Point(x, y);
        
        return p; 
        
	}

	
	// Gets the "y" coordinate of scroll's position on the screen 
	// -->  ** The secret of getting the exact coordinates was getting the "y" coordinate of the scroll bar **
	public static long getScrollPosition() throws InterruptedException {
		
		JavascriptExecutor je = (JavascriptExecutor)driver;
		long scrollHeight = (long) je.executeScript("return window.pageYOffset;");
		
		// In Command the scroll bar is outside of the CONTENT frame, 
		// so we need to switch to TOP frame to be able to get the scroll offset 
		if (loginType.equals(LoginType.Command)) {
		
			switchToTop();
			scrollHeight = (long) je.executeScript("return window.pageYOffset;");
			switchToContentFrame();
			
		} 
		// In Matrix Portal the scroll bar is inside of the CONTENT frame, 
		// so there's no need to switch to TOP frame to be able to get the scroll offset		
		else if (loginType.equals(LoginType.MatrixPortal)) {
		
			scrollHeight = (long) je.executeScript("return window.pageYOffset;");
			
		}
		
		return -scrollHeight;
		
	}
	

	// It returns true if there's data for the vendor in the selected month. That means that the vendor will be displayed on the Usage Trending chart
	// Else it returns false	
	public static HashMap<String, Boolean> isThereDataForSelectedMonth(List<List<UsageOneMonth>> allValuesFromFile, ViewType view) throws ParseException {
		
		String monthYearSelected = CommonTestStepActions.GetPulldownTextSelected();
		
		String[] monthSelectedParts = monthYearSelected.split(" ");
		String monthSelected = CommonTestStepActions.ConvertMonthToInt(monthSelectedParts[0].trim());
		String yearSelected = monthSelectedParts[1].trim();
		
//		System.out.println("month/year selected: " + monthSelected + "/" + yearSelected);
		
		HashMap<String, Boolean> vendorsToBeDisplayedMap = new HashMap<>();
		
		// Initialize HashMap with all values set to false
		for (UsageOneMonth u: allValuesFromFile.get(0)) {
		
			if (view.equals(ViewType.country)) {
				
				vendorsToBeDisplayedMap.put(u.getCountry(), false);

			} else if (view.equals(ViewType.vendor)) {
					
				vendorsToBeDisplayedMap.put(u.getVendorName(), false);
			}
			
		}
		
//		System.out.println("Initial values: ");
//		for (UsageOneMonth u: allValuesFromFile.get(0)) {
//			System.out.println("Vendor: " + u.getVendorName() + ", value: " + vendorsToBeDisplayedMap.get(u.getVendorName()));
//		}
		
		
		for (int i = 0; i < allValuesFromFile.size(); i++) {
			
			for (int j = 0; j < allValuesFromFile.get(i).size(); j++) {
				
				UsageOneMonth usage = allValuesFromFile.get(i).get(j);
				
				if(monthSelected.equals(usage.getOrdinalMonth()) && yearSelected.equals(usage.getOrdinalYear())) {
					
					if (!usage.getInvoiceMonth().equals("")) {
						
						if (view.equals(ViewType.country)) {
							
							vendorsToBeDisplayedMap.replace(usage.getCountry(), true);

						} else if (view.equals(ViewType.vendor)) {
								
							vendorsToBeDisplayedMap.replace(usage.getVendorName(), true);
						}
											
					}
					
				}
					
			}
			
		}
		
//		System.out.println("Updated values: ");
//		for (UsageOneMonth u: allValuesFromFile.get(0)) {
//			System.out.println("Vendor: " + u.getVendorName() + ", value: " + vendorsToBeDisplayedMap.get(u.getVendorName()));
//		}
		
		
		return vendorsToBeDisplayedMap;
		
	} 	
	
	
	
	public static void selectFirstMonth() throws Exception {

		CommonTestStepActions.initializeMonthSelector();
		List<WebElement> months = CommonTestStepActions.webListPulldown;
		
		String monthToSelect = months.get(0).getText();
		CommonTestStepActions.selectMonthYearPulldown(monthToSelect);
		
	}

	
	public static void waitForDataToBeLoaded() throws Exception {
		
		try {
			
			// Wait for children to be listed on the PoV section -- this is to give time to new data to be loaded
			WaitForElementPresentNoThrow(By.cssSelector("li.tdb-pov__item:nth-child(1)"), MediumTimeout);
			// ShowText("No children on PoV section");
			
		} catch (TimeoutException e) {
			
			try {
				// If there are no children listed on the PoV section,
				// then wait for message to show up on the tile map stating there are no children 
				WaitForElementPresentNoThrow(By.cssSelector("div.tdb-charts__contentMessage"), MediumTimeout);
				
			} catch (Exception e2) {
				// ShowText("No message saying there's no data for selected month.");
			}
		}
		
	}
	
	
	// Verifies that the difference between the expected value and the value found is less than one.
	// Due to rounding there may be some case where the value found and the value expected differ on 1, e.g.: Value found = 28, Value expected = 27
	public static void verifyExpectedAndActualValues(double valueActual, double valueExpected) {
		
		Assert.assertTrue(Math.abs(valueActual - valueExpected) <= 1 );
		
	}
	
	
	public static void verifyExpectedAndActualValues(long valueActual, long valueExpected) {
		
		Assert.assertTrue(Math.abs(valueActual - valueExpected) <= 1 );
				
	}
	
	
	public static void verifyExpectedAndActualValues(String valueActual, String valueExpected) {
		
		// ShowText("Value actual: " + valueActual + "; Value expected: " + valueExpected);
		
		double numActual = Double.parseDouble(getNumericValue(valueActual));
		double numExpected = Double.parseDouble(getNumericValue(valueExpected));
		
		// ShowText("numActual: " + numActual + "; numExpected: " + numExpected);
		
		Assert.assertTrue(Math.abs(numActual - numExpected) <= 1 );
		
	}
		

	public static String getNumericValue(String value) {
		
		String numericValue = value;

		// Get rid of currency symbols
		numericValue = numericValue.replace("$", "");
 		numericValue = numericValue.replace("€", "");
		numericValue = numericValue.replace("£", "");
		
		// Get rid of language tags
		numericValue = numericValue.replace("[es]", "");
		numericValue = numericValue.replace("[ja]", "");
		numericValue = numericValue.replace("[de]", "");
		
		numericValue = numericValue.replace(" directly allocated", "");  

		numericValue = numericValue.replace(" service numbers", "");

		numericValue = numericValue.replace("(GB)", "");
		
		numericValue = numericValue.replace("(min)", "");
		
		numericValue = numericValue.replace("B", "");

		numericValue = numericValue.replace("K", "");

		numericValue = numericValue.replace("M", "");

		numericValue = numericValue.replace("G", "");

		numericValue = numericValue.replace("T", "");
			
		numericValue = numericValue.replace(",", "");
		
		return numericValue.trim();
		
	}

	
	public static boolean waitForKPIsToLoad(int timeOut) throws Exception {
	    
		By byMainKPIvalue = By.cssSelector(".tdb-kpi__statistic");
		By bySecondaryValue = By.cssSelector(".tdb-kpi__statistic--secondary");
		
		try
	    {
			WebDriverWait wait = new WebDriverWait(driver, timeOut);
			
		    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(byMainKPIvalue, "text", "$")));
		    wait.until(ExpectedConditions.not(ExpectedConditions.attributeToBe(bySecondaryValue, "text", "$ directly allocated")));
		    
	    }
        catch (Exception e)
        {
	        throw new Exception(e.toString());
        }	    
	    return true;
	}		
	
	
	public static void waitForChartToLoad(int timeOut) throws Exception {
	    
		WaitForElementPresent(By.cssSelector("chart>div"), timeOut);
		
	}	
	
	
	

	// NEW - this is working -- We need to obtain the "y" coordinate of the center of the chart. 
	// This is to make sure that the mouse is not outside the chart, and therefore, tooltip gets displayed.
	// In order to do this, we get the "y" coordinate of all the horizontal lines displayed across the chart, and calculate the average of their "y" coordinate 
	public static int getYCoordinate(String chartId) throws InterruptedException {
		
		int y = 0;
			
		String cssLine = "#" + chartId + ">svg>g.highcharts-grid.highcharts-yaxis-grid>path";
		List<WebElement> lines = driver.findElements(By.cssSelector(cssLine));
		
		int coordinatesYTemp = 0;
		
		for (int i = 0; i < lines.size(); i++)  {
			
			Point coordinatesLine = GeneralHelper.getAbsoluteLocation(lines.get(i));
			coordinatesYTemp += coordinatesLine.getY();
			
		}
		
		y = coordinatesYTemp / lines.size();
		
		return y;
		
	}



	public static int getAmountOfVendorsToSelect(int totalVendorsAmount) {
		
		String amountTmp = JOptionPane.showInputDialog("Specify amount of vendors to select.");
		int amountOfVendors;
		
		try {
			
			amountOfVendors = Integer.parseInt(amountTmp);
			
		} catch (NumberFormatException e) {
			
			amountOfVendors = totalVendorsAmount;
		} 
		
		if (amountOfVendors < 1 || amountOfVendors > totalVendorsAmount)
			amountOfVendors = totalVendorsAmount;
		
		return amountOfVendors;
		
	}
	
	
	
	// Used in TRENDING chart tests
	public static void moveMouseToBar(boolean fleetExpense, boolean firstBar, int chartNum, String chartId, int indexHighchart) throws InterruptedException, AWTException {
		
		String cssBar = "";
		
		if (fleetExpense && chartNum == FleetHelper.costPerServiceNumberChart) {
			
			cssBar = "#" + chartId + ">svg>.highcharts-axis-labels.highcharts-xaxis-labels>text:nth-of-type(" + indexHighchart + ")";
			
		} else {
			
			cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
			
		}
		
		// WebElement 'bar' will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));

		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		
		Point coordinates = GeneralHelper.getAbsoluteLocation(bar);
		
		int x_offset = bar.getSize().getWidth() / 2;
		int y_offset = (int) GeneralHelper.getScrollPosition();
		
		int x = coordinates.getX() + x_offset;
		int y = GeneralHelper.getYCoordinate(chartId) + y_offset;;
		
		Robot robot = new Robot();
		
		if (firstBar) {
			robot.mouseMove(x - x_offset, y);
			Thread.sleep(500);
		}
		
		robot.mouseMove(x, y);
		Thread.sleep(500);
		
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

		
		try {
			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>.highcharts-tooltip>text>tspan"), MainTimeout);
			//System.out.println("Tooltip present");
		} catch (Exception e) {
			System.out.println("Tooltip NOT present");
			e.printStackTrace();
		}
		
	}
	
	
	// Used in TOP TEN chart tests
	public static void moveMouseToBar(String chartId, int indexHighchart, boolean isTopTenChart) throws InterruptedException, AWTException {
		
		
		String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-0>rect:nth-of-type(" + indexHighchart + ")";
		
		// WebElement 'bar' will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));

		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point coordinates = getAbsoluteLocation(bar);
		
		int height = bar.getSize().getHeight();
		int width = bar.getSize().getWidth();
		
		ShowText("height: " + height + ", " + "width: " + width);
		
		double factor = 1;
		if (!isTopTenChart) factor = 0.5; 
		
		int x_offset = (int) (width * 0.5);
		int y_offset = (int) (height * factor) + (int) getScrollPosition();
		
		ShowText("x_offset: " + x_offset + ", " + "y_offset: " + y_offset);
		
		int x = coordinates.getX() + x_offset + 2;
		int y = coordinates.getY() + y_offset;
		
		ShowText("x: " + x + ", " + "y: " + y);
		
		Robot robot = new Robot();
		robot.mouseMove(x, y);
		
		if (!isTopTenChart) {
			
			Thread.sleep(500);
			robot.mouseMove(x + 5, y);
			
			if (width > 10.0) {
				bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
			}
			
			if (width < 10.0) {
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			}
						
		}
		
		Thread.sleep(500);
		// robot.mouseMove(x + 10, y);  // <-- If needed, uncomment it. It replaces the mouse press and release, since in this chart the mouse click on the bar should redirect to a different page in CMD.
		
		
		try {
			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"), MainTimeout);
			// System.out.println("Tooltip present");
		} catch (Exception e) {
			System.out.println("Tooltip NOT present");
			e.printStackTrace();
		}
		
	}


	
	// Used in TOTAL USAGE/EXPENSE chart tests -- New ****
	public static void moveMouseToBar(String chartId, int indexHighchart, int chartNum, int category) throws InterruptedException, AWTException {
		
		int line = 0;
		
		if (loginType.equals(LoginType.MatrixPortal) && chartNum == UsageHelper.totalUsageDomesticChart && category == UsageHelper.categoryVoice) line = 1;
		
		String cssBar = "#" + chartId + ">svg>.highcharts-series-group>.highcharts-series.highcharts-series-" + line + ">rect:nth-of-type(" + indexHighchart + ")";
		
		// WebElement 'bar' will be used to set the position of the mouse on the chart
		WebElement bar = driver.findElement(By.cssSelector(cssBar));

		// Get the location of the series located at the bottom of the chart -> to get the "x" coordinate
		// These coordinates will be used to put the mouse pointer over the chart and simulate the mouse hover, so the tooltip is displayed
		Point coordinates = getAbsoluteLocation(bar);
		
		int height = bar.getSize().getHeight();
		int width = bar.getSize().getWidth();
				
		int x_offset = (int) (width * 0.5);
		int y_offset = (int) height + (int) getScrollPosition();
		
		int x = coordinates.getX() + x_offset + 2;
		int y = coordinates.getY() + y_offset;
		
		
		Robot robot = new Robot();
		robot.mouseMove(x, y);
		
		Thread.sleep(500);
		robot.mouseMove(x + 5, y);
		
		if (width > 10.0) {
			bar.click();  // The click on the bar helps to simulate the mouse movement so the tooltip is displayed
		}
		
		if (width < 10.0) {
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		
		Thread.sleep(500);
	
		
		try {
			
			WaitForElementPresent(By.cssSelector("#" + chartId + ">svg>g.highcharts-label.highcharts-tooltip>text>tspan"), MainTimeout);
			
		} catch (Exception e) {
			
			ShowText("Tooltip NOT present");
			e.printStackTrace();
			
		}
		
	}
	
	
	// Get the last month listed in month-year selector
	public static String getLastMonthFromSelector() {

		return driver.findElement(By.cssSelector(".tdb-pov__monthPicker>div>select>option:last-of-type")).getText();
		
	}

	
}
