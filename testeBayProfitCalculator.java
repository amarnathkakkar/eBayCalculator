import java.io.InputStream;
import java.util.Properties;
import java.io.ByteArrayInputStream;


public class testeBayProfitCalculator {
	
	public static void main(String[] args){
		double itemCost = 0.40;
		double shippingCost = 0.76;
		double parcelCost = 0.13;

		String simulatedUserInput = "0.99" + System.getProperty("line.separator") + System.getProperty("line.separator")
									+ "0.99" + System.getProperty("line.separator") + "11" + System.getProperty("line.separator") + "12" + System.getProperty("line.separator") + System.getProperty("line.separator")
									+ "0" + System.getProperty("line.separator") + System.getProperty("line.separator")
									+ "0" + System.getProperty("line.separator") + "11" + System.getProperty("line.separator") + "12" + System.getProperty("line.separator") + System.getProperty("line.separator")
									+ System.getProperty("line.separator");

		InputStream inputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());


		eBayProfitCalculator eBayCalc = new eBayProfitCalculator(itemCost, shippingCost, parcelCost, inputStream);

		eBayCalc.Start();
	}

}