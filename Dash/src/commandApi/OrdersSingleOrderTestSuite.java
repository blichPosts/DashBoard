package commandApi;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Dash.BaseClass;

public class OrdersSingleOrderTestSuite extends BaseClass
{

	@BeforeClass
	public static void setUp() throws Exception
	{
		ShowText("Go");
		setUpDriver();
		MainLogin();
	}
	
	@Test
	public static void OrdersTest() throws Exception
	{
		CommandHelper.GoToOrders();
		OrderStatus.SearchUserByLastName();
		CommandHelper.GetAndOutputFirstOrderListed();
	}	
	
	
	@AfterClass
	public static void closeDriver() throws Exception
	{
		System.out.println("Close Browser.");
		Pause("");
	    // JOptionPane.showMessageDialog(frame, "Select OK. This is Ana edit. TEST");
		driver.close();
		driver.quit();
	}		
	
	
}
