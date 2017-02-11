package expenseHierarchy;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import Dash.BaseClass;
import helperObjects.ExpenseHelper;

public class VisualPageLoad extends BaseClass 
{
	public static String []  strArrayOne;
	public static String []  strArrayTwo;
	
	public static void SelectAndWaitForPageLoad() throws Exception
	{
		WaitForElementClickable(By.xpath("//a[text()='View by Hierarchy']"), MediumTimeout, "");
		driver.findElement(By.xpath("//a[text()='View by Hierarchy']")).click();
		WaitForElementVisible(By.xpath("//h2[text()='" + ExpenseHelper.desiredMonth + "']"), MediumTimeout); // this is month in top left corner tiles.
		WaitForElementVisible(By.xpath("//span[text()='Total Expense']"), MediumTimeout); // this is text in top left corner tiles. 
		WaitForElementVisible(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select"), MediumTimeout); // this is drop down in top left corner POV.
	}
	
	// verify for desired month in  pulldown is desired month
	// String selectedOption = new Select(driver.findElement(By.xpath("Type the xpath of the drop-down element"))).getFirstSelectedOption().getText(); 
	//
	// verify --- above KPI tiles "Expenses for 0:0 Hewson, Ed:3579086421 and its dependent units". 
	// verify -- pulldown value - maximum displayed is 10 in 2 places. 
	// verify in tiles - "Top 10 (out of 100) dependent units of 0:0 Hewson, Ed:3579086421 - Total Expense"
	// verify css '.tdb-inlineBlock.tdb-boxSelector__option.tdb-boxSelector__option--selected' is enabled  -- this is one of 3 tabs default value.
	public static void VerifyInitialStatesAfterPageLoad() 
	{
		Assert.assertEquals(new Select(driver.findElement(By.cssSelector(".tdb-flexContainer.tdb-flexContainer--center>select"))).getFirstSelectedOption().getText(), ExpenseHelper.desiredMonth,
				              							  "");
		
		
		
		
	}
	
	
	
	
	
	public static void ConsoleOutForFileDiffActualValues() throws Exception
	{
		
		List<WebElement> eleList = 	 driver.findElements(By.cssSelector(".tdb-pov__itemList>li")); // get web element list that holds items in the POV.

		int localCntr = 0;
		
		for(WebElement ele : eleList)
		{

			// ShowText("ELE " + " " + ele.getText()); // DEBUG
			
			strArrayOne = ele.getText().split(" ");
			
			String id = strArrayOne[0]; // "1:81"
			
			String lastNameSection = strArrayOne[1]; // "Zhang,"
			
			// remove the "\n" from array element [2]. 
			// element[2] now looks like this ' Sandra:86644221Total:$82390'.
			strArrayOne[2] =   strArrayOne[2].replace("\n", "");    
			
			String firstName = strArrayOne[2].split(":")[0].trim(); 
			
			String cost = strArrayOne[2].split(":")[2].replace("$","").trim();
			
			localCntr++;
			
			System.out.println(localCntr + " " + lastNameSection + " " + firstName + " " + cost);
		}
	}
}
