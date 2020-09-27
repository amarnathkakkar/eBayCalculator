import java.util.Scanner;
import java.util.ArrayList;

public class eBayProfitCalculator{

	static double itemCost;
	static double shippingCost;
	static double parcelCost;
	static double vat;
	static double flatPaypalFee;
	static double percEbayFee;
	static double percPaypalFee;


	public eBayProfitCalculator() {
		itemCost = 0.40;
		shippingCost = 0.76;
		parcelCost = 0.13;

		vat = 0.2;

		flatPaypalFee = 0.3;
		percEbayFee = 0.09*(1+vat);
		percPaypalFee = 0.029;
	}


	public static void main(String args[]) {
		eBayProfitCalculator newCalc = new eBayProfitCalculator();
		System.out.println("Type anything other than a number to exit.");
		newCalc.Start();
	}


	public static double Round(double input) {
		return Double.parseDouble(String.format("%.2f",input));
	}


	public static void Exit() {
		try {
			System.out.print("exiting");
            Thread.sleep(200);
            System.out.print(".");
            Thread.sleep(200);
            System.out.print(".");
            Thread.sleep(200);
            System.out.print(".\n");
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}


	public static ArrayList<Integer> populateNumOfGoodsToCheckArray(Scanner usrInput) {
		ArrayList<Integer> intArray = new ArrayList<Integer>();

		do {
			//System.out.println("Leave empty or type an integer greater than 0.");
			System.out.print("numberOfGoods = ");
			String numberOfGoodsString = usrInput.nextLine();

			if (isValidDouble(numberOfGoodsString)) {
				intArray.add(Integer.parseInt(numberOfGoodsString));
			} else {
				break;
			}
		} while (true);

		
		if (intArray.size() < 1) {
			for (int k=1; k<=10; k++) {
				intArray.add(k);
			}
		}

		return intArray;
	}


	public static void printSellingAndProfit(int n, double sellingPrice, double profit) {
		System.out.println(n + ":  s = " + sellingPrice + ", p = " + profit);
	}


	public static void Loop(boolean bool) {
		if (bool == true) {
			Start();
		} else {
			Exit();
		}
	}


	public static boolean isValidDouble(String input) {
		boolean isValid = false;

		if (input.matches("-?\\d+(\\.\\d+)?")) {
			isValid = true;
		}

		return isValid;
	}


	public static double[] calcSellingPriceAndProfit(int n, double priceOfOneGood) {
		//sell price of goods = price of a single good * number of goods sold
		double sellingPrice = Round(priceOfOneGood*n);
		//cost of goods sold = cost of goods sold + shippingCost + parcelCost + flat paypal fee + %(ebayfee & paypalfee) of selling price
		double costOfGoods = Round(itemCost*n + (shippingCost + parcelCost + flatPaypalFee) + (percPaypalFee+percEbayFee)*sellingPrice);

		double claimBackVAT = Round(vat*(itemCost*n + percEbayFee*sellingPrice));
		//if claiming VAT from shipping, use below 'claimBackVAT' (probably once I have got a business royal mail acc? Since I can view reports)
		//double claimBackVAT = round(vat*(itemCost*n + shippingCost + percEbayFee*sellingPrice));
		double giveGovVAT = Round(vat*sellingPrice);

		double profit = Math.min(
				Round(sellingPrice+claimBackVAT-costOfGoods-giveGovVAT), Round(sellingPrice-costOfGoods)
		);


		double[] doubleArray = new double[2];
		doubleArray[0] = sellingPrice;
		doubleArray[1] = profit;


		return doubleArray;
	}


	public static void Start() {

		double priceOfOneGood = 0;
		
		System.out.print("priceOfOneGood = ");

		Scanner usrInput = new Scanner(System.in);
		String priceOfOneGoodString = usrInput.nextLine();
		boolean isValidDouble = false;

		if (isValidDouble(priceOfOneGoodString)) {
			priceOfOneGood = Double.parseDouble(priceOfOneGoodString);
			isValidDouble = true;
		}


		if (isValidDouble && priceOfOneGood == 0) {
			
			ArrayList<Integer> numberOfGoodsToCheckArray = populateNumOfGoodsToCheckArray(usrInput);


			numberOfGoodsToCheckArray.forEach((n) -> {

				double[] sellingPriceAndProfitArray = new double[2];

				double iteratePriceOfOneGood = itemCost;

				do {
					
					sellingPriceAndProfitArray = calcSellingPriceAndProfit(n, iteratePriceOfOneGood);

					iteratePriceOfOneGood += 0.01;

				} while (sellingPriceAndProfitArray[1] < 0);


				printSellingAndProfit(n, sellingPriceAndProfitArray[0], sellingPriceAndProfitArray[1]);
			});

		}
		else if (isValidDouble) {

			ArrayList<Integer> numberOfGoodsToCheckArray = populateNumOfGoodsToCheckArray(usrInput);

			final double priceOfOneGoodFinal = priceOfOneGood;


			numberOfGoodsToCheckArray.forEach((n) -> {

				double[] sellingPriceAndProfitArray = new double[2];

				sellingPriceAndProfitArray = calcSellingPriceAndProfit(n, priceOfOneGoodFinal);
				
				printSellingAndProfit(n, sellingPriceAndProfitArray[0], sellingPriceAndProfitArray[1]);

			});

		}


		Loop(isValidDouble);
	}
}