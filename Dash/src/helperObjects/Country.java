package helperObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// this will hold the country name and a list of the vendors attached to it.
public class Country 
{

	public String name = "";
	public List<String> vendorList = new ArrayList<String>();
	
	// ctor
	public Country(String name) 
	{
		this.name = name;
	}
	
	public void AddToVendorList(String vendor)
	{
		vendorList.add(vendor);
	}
	
	// verify the vendor list is sorted.
	public void VerifyVendorListSorted() 
	{
		List<String> sampleList = vendorList;
		Collections.sort(sampleList);
		// Assert.assertEquals(sampleList, vendorList);
	}
	
	public void ShowVendorList()
	{
		for(String str : vendorList)
		{
			System.out.println(str);
		}
	}
}
