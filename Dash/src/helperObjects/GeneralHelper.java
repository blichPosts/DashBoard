package helperObjects;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Dash.BaseClass;


public class GeneralHelper extends BaseClass {

	
	public static WebElement iframe;  
	public static int x_iFrame;
	public static int y_iFrame;
	
	
	
	public static void setUpiFrame() {
		
		iframe = driver.findElement(By.cssSelector("iframe#CONTENT"));
		Point iframeLoc = iframe.getLocation();
		x_iFrame = iframeLoc.getX();
        y_iFrame = iframeLoc.getY();
        
	}
	
	
	
	// It returns true if there's data for the vendor in the selected month. That means that the vendor will be displayed on the Usage Trending chart
	// Else it returns false	
	public static HashMap<String, Boolean> vendorHasDataForSelectedMonth(List<List<UsageOneMonth>> allValuesFromFile) throws ParseException {
		
		String monthYearSelected = CommonTestStepActions.GetPulldownTextSelected();
		
		String[] monthSelectedParts = monthYearSelected.split(" ");
		String monthSelected = CommonTestStepActions.ConvertMonthToInt(monthSelectedParts[0].trim());
		String yearSelected = monthSelectedParts[1].trim();
		
//		System.out.println("month/year selected: " + monthSelected + "/" + yearSelected);
		
		HashMap<String, Boolean> vendorsToBeDisplayedMap = new HashMap<>();
		
		// Initialize HashMap with all values set to false
		for (UsageOneMonth u: allValuesFromFile.get(0)) {
			vendorsToBeDisplayedMap.put(u.getVendorName(), false);
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
						
						vendorsToBeDisplayedMap.replace(usage.getVendorName(), true);
											
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
	
	
	// Get the location of the element on the UI 
	public static Point getAbsoluteLocation(WebElement element) {
		
        int x = x_iFrame;
        int y = y_iFrame;
        
        WebElement header = driver.findElement(By.cssSelector("header.tdb-flexContainer"));
		int headerHeight = header.getSize().getHeight();
        
        Point elementLoc = element.getLocation();

        if (loginType.equals(LoginType.ReferenceApp)) {
        	
            x += elementLoc.getX();
            y += elementLoc.getY() + headerHeight;
            
        	
        } else if (loginType.equals(LoginType.Command)) {
        	
        	x += elementLoc.getX();
            y += elementLoc.getY() - (headerHeight * 1.3);
            
        }
        
        Point p = new Point(x, y);
        return p; 
        
	}


	public static Point getAbsoluteLocationTopTenBar(WebElement element) {
		
        int x = x_iFrame;
        int y = y_iFrame;
        
        WebElement header = driver.findElement(By.cssSelector("header.tdb-flexContainer"));
		int headerHeight = header.getSize().getHeight();
        
        Point elementLoc = element.getLocation();

        if (loginType.equals(LoginType.ReferenceApp)) {
        	
            x += elementLoc.getX();
            y += elementLoc.getY() + headerHeight;
            
        	
        } else if (loginType.equals(LoginType.Command)) {
        	
        	x += elementLoc.getX();
            y += elementLoc.getY() + (headerHeight * 3);
            
        }
        
        Point p = new Point(x, y);
        return p; 
        
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
//			ShowText("No children on PoV section");
		} catch (TimeoutException e) {
			try {
				// If there are no children listed on the PoV section,
				// then wait for message to show up on the tile map stating there are no children 
				WaitForElementPresentNoThrow(By.cssSelector("div.tdb-charts__contentMessage"), MediumTimeout);
			} catch (Exception e2) {
//				ShowText("No message saying there's no data for selected month.");
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
		
//		System.out.println("Value actual: " + valueActual + "; Value expected: " + valueExpected);
		
		double numActual = Double.parseDouble(getNumericValue(valueActual));
		double numExpected = Double.parseDouble(getNumericValue(valueExpected));
		
//		System.out.println("numActual: " + numActual + "; numExpected: " + numExpected);
		
		Assert.assertTrue(Math.abs(numActual - numExpected) <= 1 );
		
	}
		

	private static String getNumericValue(String value) {
		
		String numericValue = value;
		
		if (numericValue.startsWith("$"))
			numericValue = numericValue.replace("$", "");
		
		if (numericValue.endsWith(" directly allocated")) 
			numericValue = numericValue.replace(" directly allocated", "");  
		
		if (numericValue.endsWith(" service numbers"))
			numericValue = numericValue.replace(" service numbers", "");

		if (numericValue.endsWith("B"))
			numericValue = numericValue.replace("B", "");
		
		if (numericValue.endsWith("K"))
			numericValue = numericValue.replace("K", "");
			
		if (numericValue.endsWith("M"))
			numericValue = numericValue.replace("M", "");
		
		if (numericValue.endsWith("G"))
			numericValue = numericValue.replace("G", "");
		
		if (numericValue.endsWith("T"))
			numericValue = numericValue.replace("T", "");
				
		return numericValue.trim();
		
	}

	
	public static boolean waitForKPIsToLoad(int timeOut) throws Exception 
	{
	    
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
	        //System.out.println(e.toString());
	        throw new Exception(e.toString());
        }	    
	    return true;
	}		
	
	

	// NEW - this is working -- We need to obtain the "y" coordinate of the center of the chart. 
	// This is to make sure that the mouse is not outside the chart, and therefore, tooltip gets displayed.
	// In order to do this, we get the "y" coordinate of all the horizontal lines displayed across the chart, and calculate the average of their "y" coordinate 
	public static int getYCoordinate(String chartId) {
		
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



	
	
	
}
