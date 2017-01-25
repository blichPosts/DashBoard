package helperObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Dash.BaseClass;

public class ExpenseHelperMultipleVendors extends BaseClass{

	public final static String path = "D:/Documents/CMD Dashboard/CreateFilesProgram/ ";

	public final static int totalExpenseSection = 0;
	public final static int expenseTrendingSection = 1;
	
	public final static int expenseByVendorChart = 2;
	public final static int costPerServiceNumberChart  = 3;
	public final static int countServiceNumbersChart = 4;
	
	public final static int categoryAll = 1;
	public final static int categoryVoice = 2;
	public final static int categoryData = 3;
	public final static int categoryMessages = 4;
	public final static int categoryRoaming = 5;
	public final static int categoryEquipment = 6;
	public final static int categoryTaxes = 7;
	public final static int categoryOther = 8;
	public final static int categoryAccount = 9;
	
	
	
	public static void selectCategory(int category){
		
		WebElement categoryToSelect = driver.findElement(By.cssSelector(".tdb-inlineBlock.tdb-boxSelector__option:nth-of-type(" + category + ")"));
		categoryToSelect.click();
		
	}
	
}
