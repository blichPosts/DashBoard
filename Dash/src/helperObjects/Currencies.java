package helperObjects;

import java.util.HashMap;

public class Currencies {

	public static HashMap<String, Currency> currenciesMap;
	
	
	public static void setUpCurrencies() {
		
		Currency currency;
		currenciesMap = new HashMap<>();
		
		String currencyCode = "USD";
		String currencyName = "United States Dollar";
		String currencySymbol = "$";
	
		currency = new Currency(currencyCode, currencyName, currencySymbol);
		
		currenciesMap.put(currencyCode, currency);
		
		currencyCode = "GBP";
		currencyName = "Pound sterling";
		currencySymbol = "£";
	
		currency = new Currency(currencyCode, currencyName, currencySymbol);
		
		currenciesMap.put(currencyCode, currency);
	
		currencyCode = "EUR";
		currencyName = "Euro";
		currencySymbol = "€";
	
		currency = new Currency(currencyCode, currencyName, currencySymbol);
		
		currenciesMap.put(currencyCode, currency);
	
		currencyCode = "CAD";
		currencyName = "Canadian Dollar";
		currencySymbol = "$";
	
		currency = new Currency(currencyCode, currencyName, currencySymbol);
		
		currenciesMap.put(currencyCode, currency);
		
		
		
//		<option _ngcontent-gpb-33="" value="USD">United States Dollar</option>
//		<option _ngcontent-gpb-33="" value="GBP">Pound sterling</option>
//		<option _ngcontent-gpb-33="" value="EUR">Euro</option>
//		<option _ngcontent-gpb-33="" value="CAD">Canadian Dollar</option>
//		<option _ngcontent-gpb-33="" value="IQD">Iraqi dinar (IQD)</option>
//		<option _ngcontent-gpb-33="" value="IRR">Iranian rial (IRR)</option>
		
	}
	
		
}
