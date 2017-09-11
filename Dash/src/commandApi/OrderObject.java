package commandApi;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderObject 
{
	public String user = "";
	public String department = "";
	public String email = "";
	public String login = "";
	public String userId = "";
	public String orderId = "";
	public String fulFiller = "";
	public String orderDate = "";
	public String orderType = "";
	public String serviceNumber = "";
	public String statusDate = "";
	public String status = "";
	public String requestedBy = "";
	
	public static String OutputHeader = "FORMAT: User, Department, Email, Login, UserId, OrderId, FullFiller, OrderDate, OrderType, ServiceNumber, StatusDate, Status, RequestedBy";
	
	public void Show()
	{
		System.out.println("-------------");
		System.out.println("User: " + user);
		System.out.println("Department: " + department);
		System.out.println("Email: " + email);
		System.out.println("Login: " + login);
		System.out.println("User Id: " + userId);
		System.out.println("Order Id: " + orderId);
		System.out.println("FullFiller: " + fulFiller);
		System.out.println("Order Date: " + orderDate);
		System.out.println("Order Type: " + orderType);	
		System.out.println("Service Number: " + serviceNumber);
		System.out.println("Status Date: " + statusDate);
		System.out.println("Status: " + status);
		System.out.println("RequestedBy: " + requestedBy);
	}
	
	
	
	
	public void OutputObject(Writer outFile) throws IOException
	{
		outFile.write(user + ";" + department + ";" + email + ";" + login + ";" + userId + ";" + orderId + ";" + fulFiller +  ";" + 
	                  orderDate +  ";" + orderType +  ";" + serviceNumber + ";" + statusDate + ";" + status + ";" +  requestedBy  + "\n");
	}
	
	public static void OutputHeaderAndDate(Writer outFile) throws IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		outFile.write("Output Date: " + dateFormat.format(date) + "\n");
		outFile.write(OutputHeader + "\n");		
	}	
	
}
