package commandApi;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import Dash.BaseClass;

public class OrderListOfOrdersTestSuites extends BaseClass 
{

	@BeforeSuite
	public void StartupAndLogin() throws Exception
	{
		ShowText("Go");
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public void AllActions() throws Exception
	{
		OrderStatus.GoToOrderStatus();

		// NOTE: this is for API tests OrdersMain
		OrderStatus.SearchUserByLastName();		
		OrderStatus.SetupListOfOrders(10); 
		
		// NOTE: this is for API tests OrdersShort
		//OrderStatus.StartDefaultSearch();
		//OrderStatus.SetupListOfOrdersAllCommandData(10);
		
		//OrderStatus.GetFirstOrderListed();
	}
	
	@AfterSuite public static void CloseBrowser() 
	{
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Select OK to stop the webdriver and browser in ORDER INFO.");
		driver.close();
		driver.quit();
	}				
	
}


