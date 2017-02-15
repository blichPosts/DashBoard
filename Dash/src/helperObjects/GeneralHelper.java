package helperObjects;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

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
		
		for (UsageOneMonth u: allValuesFromFile.get(0)) {
//			System.out.println("Vendor: " + u.getVendorName() + ", value: " + vendorsToBeDisplayedMap.get(u.getVendorName()));
		}
		
		
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
		for (UsageOneMonth u: allValuesFromFile.get(0)) {
//			System.out.println("Vendor: " + u.getVendorName() + ", value: " + vendorsToBeDisplayedMap.get(u.getVendorName()));
		}
		
		
		return vendorsToBeDisplayedMap;
		
	} 	
	
	
	// Get the location of the element on the UI 
	public static Point getAbsoluteLocation(WebElement element) {
		
        int x = x_iFrame;
        int y = y_iFrame;
        
        WebElement header = driver.findElement(By.cssSelector("header.tdb-flexContainer"));
		int headerHeight = header.getSize().getHeight();
        
        Point elementLoc = element.getLocation();
        x += elementLoc.getX();
        y += elementLoc.getY() + headerHeight;
        
        Point p = new Point(x, y);
        return p; 
        
	}



	public static void selectFirstMonth() {

		CommonTestStepActions.initializeMonthSelector();
		List<WebElement> months = CommonTestStepActions.webListPulldown;
		String monthToSelect = months.get(0).getText();
		CommonTestStepActions.selectMonthYearPulldown(monthToSelect);
		
	}
	
}
