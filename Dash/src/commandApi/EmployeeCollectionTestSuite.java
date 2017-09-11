package commandApi;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import javax.swing.JOptionPane;

import org.testng.annotations.AfterTest;

import Dash.BaseClass;

public class EmployeeCollectionTestSuite extends BaseClass 
{
  @BeforeTest
  public void beforeTest() throws Exception 
  {
	ShowText("Go");
	setUpDriver();
	MainLogin();
  }

  @Test
  public void GetEmployeesCollection() throws Exception 
  {
	  CommandHelper.GoToEmployeesPageForEdit();
	  CommandHelper.LoadListOfEmployeeObjects();

  }

  
  
  @AfterTest
  public void afterTest() 
  {
		System.out.println("Close Browser.");		
	    JOptionPane.showMessageDialog(frame, "Employee collection done.");
		driver.close();
		driver.quit();
  }
}
