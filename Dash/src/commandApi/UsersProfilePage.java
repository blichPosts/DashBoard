package commandApi;

import org.openqa.selenium.By;

import Dash.BaseClass;

public class UsersProfilePage extends BaseClass 
{

	public static int numElementsForNonEditedPage = 8;
	public static boolean leaveEarly = false; 
	
	// this loads the items in the shipping address area, in the User Profile page, into the employee object
	// passed in. If none of the entries in the user profile page shipping address area are populated the 
	// program just returns. if any of the entries are populated, the entrie(s) are loaded into the employee object.
	// the program returns by selecting the "modify working set" button. this returns control from the 
	// user profile page to the employee list.
	public static void LoadEmployeeObjectAndReturn(EmployeeObject empObject) throws Exception
	{
		leaveEarly = false;
		
		// wait for some elements.
		WaitForElementPresent(By.xpath("//td[contains(text(),'Line1')]/following-sibling :: td"), MainTimeout);
		WaitForElementPresent(By.xpath("//td[contains(text(),'Line2')]/following-sibling :: td"), MainTimeout);
		WaitForElementPresent(By.xpath("//td[contains(text(),'Line3')]/following-sibling :: td"), MainTimeout);
		
		// this means nothing has been added to the section of interest. 
		if(driver.findElements(By.xpath("//td[contains(text(),'Line1')]/../../tr")).size() == numElementsForNonEditedPage)
		{
			// use "Modify Working Set" button to return to employees page. 
			// bladdxx - removed on 2/9/16 when using FF
			//DebugTimeout(1); //unknown error: Element is not clickable at point (1563, 220). Other element would receive the click: <iframe src="javascript:(function(){document.open();document.close();})()" id="PLEASE_WAIT_MODAL_WINDOW_FRAME" scrolling="no" frameborder="0" style="display: block; position: absolute; top: 30px; left: 0px; width: 100%; height: 740px; z-index: 99999;"></iframe>
			leaveEarly = true;
			WaitForElementPresent(By.xpath("//td/input[@name='modifyWorkingSetBtn']"), MainTimeout);
			DebugTimeout(3, ""); // frame click error.
			driver.findElement(By.xpath("//td/input[@name='modifyWorkingSetBtn']")).click();
		}

		if(!leaveEarly) //  there is at least one entry in the shipping address area.
		{		
			empObject.addressLine1 = driver.findElement(By.xpath("//td[contains(text(),'Line1')]/following-sibling :: td")).getText();
			empObject.addressLine2 = driver.findElement(By.xpath("//td[contains(text(),'Line2')]/following-sibling :: td")).getText();
			empObject.addressLine3 = driver.findElement(By.xpath("//td[contains(text(),'Line3')]/following-sibling :: td")).getText();
			empObject.city = driver.findElement(By.xpath("//td[contains(text(),'City')]/following-sibling :: td")).getText();
			empObject.state = driver.findElement(By.xpath("//td[contains(text(),'State')]/following-sibling :: td")).getText();
			empObject.zipCode = driver.findElement(By.xpath("//td[contains(text(),'Zip Code')]/following-sibling :: td")).getText();
			empObject.country = driver.findElement(By.xpath("//td[contains(text(),'Country')]/following-sibling :: td")).getText();		

			// use "Modify Working Set" button to return to employees page. 
			WaitForElementPresent(By.xpath("//td/input[@name='modifyWorkingSetBtn']"), MainTimeout);
			driver.findElement(By.xpath("//td/input[@name='modifyWorkingSetBtn']")).click();
		}
	}
	
	
	
	
}
