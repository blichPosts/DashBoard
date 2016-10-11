package helperObjects;

import java.util.logging.ErrorManager;

import org.openqa.selenium.By;
import org.testng.Assert;

import Dash.BaseClass;
// import junit.framework.Assert;

public class ExpenseHelper extends BaseClass
{
	public static void VerifyTotalExpenseCost() 
	{
		String errMessage = "Failure in ExpenseHelper.VerifyTotalExpenseCost";
		String tmpStr = driver.findElement(By.cssSelector(".tdb-kpi:nth-of-type(1) > div> div:nth-of-type(1)")).getText();
		Assert.assertTrue(tmpStr.contains("$"), errMessage);
		Assert.assertTrue(tmpStr.contains("K"), errMessage);	
		tmpStr = tmpStr.replace("K","").replace("$", "").replace(".","");
		
		VerifyInteger(tmpStr, errMessage);
	}
	
	// //////////////////////////////////////////////////////////////////////	
	// 								helpers
	// //////////////////////////////////////////////////////////////////////
	public static void VerifyInteger(String tmpStr, String errMessage)
	{
		try
		{
			int num = Integer.parseInt(tmpStr);
		} 
		catch (NumberFormatException e) 
		{
			Assert.fail(errMessage + e.getMessage());
		}
	}
	

};


