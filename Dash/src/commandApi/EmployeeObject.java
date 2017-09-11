package commandApi;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeObject 
{

	// properties in employees list for the employee.
	public String employeeId =  "";
	public String employeeLastFirstName =  "";
	public String employeeDepartment =  "";	
	public String employeeStatus =  "";
	public String companyEmployeeId =  "";	
	
	// properties in employee User Profile for shipping address. 
	public String addressLine1 =  "";	
	public String addressLine2 =  "";
	public String addressLine3 =  "";
	public String city =  "";
	public String state =  "";
	public String zipCode =  "";
	public String country =  "";
	public String emptyFieldIdentifier = "%"; 

	public String shippingAddressValue = ""; // this is string holding all shipping address items (separated by commas). 
	List<String> listShippingAddressItems; // this is list used to create 
	public static String OutputHeader =  "EXPECTED_EMPLOYEE_ITEMS_FORMAT: EmployeeId,EmployeeLastFirstName,EmployeeDepartment,Status,CompanyEmployeeId,ShippingValue,Country";
	
	// ctor
	public EmployeeObject()
	{
		listShippingAddressItems = new ArrayList<String>();		
	}
	
	// * this static method is used to output the date it is called and output the static string "OutputHeader".
	// * static string "OutputHeader" is a predefined string that is included in the output file. it tells the 
	//   semicolon format of the object strings that are output in the output file.
	public static void OutputHeaderAndDate(Writer outFile) throws IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		outFile.write("Output Date: " + dateFormat.format(date) + "\n");
		outFile.write(OutputHeader + "\n");		
	}
	
	// this is for showing to console window.
	public void Show()
	{
		System.out.println("------------- Object Details ------------");
		System.out.println("employeeId: " + employeeId);
		System.out.println("employeeLastFirstName: " + employeeLastFirstName);		
		System.out.println("employeeDepartment: " + employeeDepartment);
		System.out.println("employeeStatus: " + employeeStatus);		
		System.out.println("companyEmployeeId: " + companyEmployeeId);	
		//System.out.println("addressLine1: " + addressLine1);		
		//System.out.println("addressLine2: " + addressLine2);
		//System.out.println("addressLine3: " + addressLine3);	
		//System.out.println("city: " + city);	
		//System.out.println("state: " + state);		
		//System.out.println("zipCode: " + zipCode);
		System.out.println("country: " + country);
		System.out.println("shippingAddressValue: " + shippingAddressValue);		
	}
	
	public void OutputObject(Writer outFile) throws IOException
	{
		outFile.write(employeeId + ";" + employeeLastFirstName + ";" + employeeDepartment + ";" + employeeStatus + ";" + companyEmployeeId + ";" + shippingAddressValue + ";" + country + "\n");
	}
	
	// this cleans up properties for file output or showing to console.
	public void OrganizeForFileOutput()
	{
		String tempStrOne = "";
		String tempStrTwo = "";
		
		// employee id taken directly from selenium looks like this. Only need the integer part.
		// https://qa1cmd.tangoe.com/manage/organize/userprofile.trq?am=view&employeeId=28720012&hierarchyId=48068833
		if(employeeId.length() != 0)
		{
			tempStrOne = employeeId.split("=")[2].split("&")[0].trim();
			
			//tempStrOne = employeeId.trim().replaceFirst("employeeSelected\\(", "");
			//employeeId = tempStrOne.trim().replaceFirst("\\)",""); // this sets employeeId to the integer part.
			
			employeeId = tempStrOne;
			
		}
		
		// employeeId taken directly from selenium looks like this "id: sn.admin.xx1". only need 'sn.admin.xx1'.
		if(companyEmployeeId.contains("id:"))
		{
			tempStrOne =  companyEmployeeId.replace("id: ","");
			companyEmployeeId = tempStrOne; 	
		}
		else
		{
			companyEmployeeId = emptyFieldIdentifier; // indicate no entry.
		}
		
		if(country.length() == 0)
		{
			country = emptyFieldIdentifier; // indicate no entry.
		}
		
		SetupShippingAddressItems();
	}
	
	public void SetupShippingAddressItems()
	{
		boolean FoundShippingAddressItem = false;
		
		// setup a list to holding all of the shipping address info 
		listShippingAddressItems.add(addressLine1);
		listShippingAddressItems.add(addressLine2);
		listShippingAddressItems.add(addressLine3);
		listShippingAddressItems.add(city);
		listShippingAddressItems.add(state);
		listShippingAddressItems.add(zipCode);

		// * go through each item in shipping address items to see if any of them has a value. 
		// * if an item has a value, add item to shippingAddressValue string and add a "," to the added item.
		// * if an item is found, FoundShippingAddressItem boolean is set true. FoundShippingAddressItem boolean is 
		//   used later to decide of shippingAddressValue string needs the last "," character removed.
		for(String str: listShippingAddressItems)
		{
			if(str.length() != 0)			
			{
				FoundShippingAddressItem = true; 
				shippingAddressValue += str + ", ";
			}
		}
		
		// if something has been found in shipping address items, need to remove "," at end of shippingAddressValue string.
		if(FoundShippingAddressItem)  
		{
			// now remove the last "," character.
			int tempInt = shippingAddressValue.lastIndexOf(',');
			String tempStr = shippingAddressValue.substring(0, tempInt);
			shippingAddressValue = tempStr;
		}
		else // nothing there, put in identifier to show it's empty.
		{
			shippingAddressValue = emptyFieldIdentifier;
		}
	}	
	
	
	
}
