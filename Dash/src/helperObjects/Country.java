package helperObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;

// this will hold the country name and a list of the vendors attached to it..
public class Country    
{
	public String name = "";
	public List<String> vendorList = new ArrayList<String>();
	String tempCountry = "";
	
	
	// ctor
	public Country(String name) 
	{
		this.name = name;
	}
	
	public void AddToVendorList(String vendor)
	{
		vendorList.add(vendor);
	}
	
	public void VerifyVendorListSorted(String country)
	{
		List<String> actualList = new ArrayList<String>();
		List<String> expectedList = new ArrayList<String>();
		
		for(String str : vendorList)
		{
			actualList.add(str);
			expectedList.add(str);
		}
		
		Collections.sort(expectedList);
		Assert.assertEquals(actualList, expectedList, "Failed test for vendor list being sorted in Country object in Country.VerifyVendorListSorted. Country name is: " + country);  
	}
	
	public int NumberOfVendors()
	{
		return vendorList.size();
	}
	
	public String VendorByIndexOneBased(int index)
	{
		Assert.assertTrue(index <= vendorList.size()); 
		return vendorList.get(index - 1);
	}
	
	public String FindVendor(String vendor)
	{
		
		return "";
		
	}
	
	public void ShowVendorList()
	{
		for(String str : vendorList)
		{
			System.out.println(str);
		}
	}
	
	public String CountryThatContainsVendor(String vendor)
	{
		
		
		
		return "";
	}
}
