import java.util.Scanner;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.Properties;
import java.io.ByteArrayInputStream;

import java.io.OutputStream;
import javax.swing.JTextArea;
import java.io.PrintStream;

public class eBayProfitCalculator{

	double itemCost;
	double shippingCost;
	double parcelCost;
	double vat;
	double flatPaypalFee = 0.3;
	double percPaypalFee = 0.029;
	double percEbayFee;

	boolean isValidInput;


	boolean firstLoop = false;
	String itemCostString;
	String shippingCostString;
	String parcelCostString;
	String vatString;
	String category;
	String subCategory;

	Scanner usrInput;


	public eBayProfitCalculator(double itemCostUser, double shippingCostUser, double parcelCostUser, InputStream testInput) {
		itemCost = itemCostUser;
		shippingCost = shippingCostUser;
		parcelCost = parcelCostUser;	

		vat = 0.2;

		percEbayFee = 0.09*(1+vat);

		usrInput = new Scanner(testInput);
	}


	public eBayProfitCalculator(String itemCost, String shippingString, String parcelString, String vat, String cat, String subCat) {
		itemCostString = itemCost;
		shippingCostString = shippingString;
		parcelCostString = parcelString;

		vatString = vat;

		category = cat;
		subCategory = subCat;

	}



	public eBayProfitCalculator() {
		
	}


	public static void main(String args[]) {

		eBayProfitCalculator newCalc = new eBayProfitCalculator();
		newCalc.getVariablesFromFile();
		newCalc.Start();
		
	}


	public void getVariablesFromFile() {

		try {
			File varFile = new File("Variables.txt");	
			Scanner fileScanner = new Scanner(varFile);

			int i = 0;
			String[] variableArray = new String[6];

			while(fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				int equals = line.indexOf("=");

				Pattern pattern = Pattern.compile("= ");
				Matcher matcher = pattern.matcher(line);
				if(matcher.find()) {
				    variableArray[i] = line.substring(equals+2);
				} else {
					variableArray[i] = line.substring(equals+1);
				}

				i++;
			}
			fileScanner.close();

			itemCostString = variableArray[0];
			shippingCostString = variableArray[1];
			parcelCostString = variableArray[2];
			vatString = variableArray[3];
			//percEbayFee = (1+vat)*Double.parseDouble(variableArray[4]);


			category = variableArray[4];
			subCategory = variableArray[5];


		} catch (FileNotFoundException e) {
			quitApp("File named 'Variables.txt' not found.", e);
		} 

	}


	public void printVariables() {
		System.out.println("itemCost = " + itemCost + " shippingCost = " + shippingCost + " parcelCost = " + parcelCost + " VAT = " + vat*100 + "%" + " eBayFee = " + Round(100*percEbayFee/(1+vat)) + "%");
	}


	public String getVariables() {
		return "itemCost = " + itemCost + " shippingCost = " + shippingCost + " parcelCost = " + parcelCost + " VAT = " + vat*100 + "%" + " eBayFee = " + Round(100*percEbayFee/(1+vat)) + "%";
	}


	public void checkVariables() {
		try {
			itemCost = Double.parseDouble(itemCostString);
			shippingCost = Double.parseDouble(shippingCostString);
			parcelCost = Double.parseDouble(parcelCostString);
			vat = Double.parseDouble(vatString);
			//percEbayFee = (1+vat)*Double.parseDouble(variableArray[4]);

			
			JSONReader json = new JSONReader();

			double fee = (double) json.getFee(category, subCategory);

			if (fee==-1) {
				quitApp("Category or Sub Category not found. Please check spelling.");
			} else {
				double feePercentage = (fee/100);
				percEbayFee = (1+vat)*feePercentage;
			}

		} catch (NumberFormatException e) {
			quitApp("Please ensure variables in file 'Variables.txt' are real numbers.", e);
		}
	}


	/*
	public void printVariables(String usrItemCost, String usrShippingCost, String usrParcelCost, String usrVAT, String usrCat, String usrSubCat) {
		System.out.println("itemCost = " + itemCost + " shippingCost = " + shippingCost + " parcelCost = " + parcelCost + " VAT = " + vat*100 + "%" + " eBayFee = " + Round(100*percEbayFee/(1+vat)) + "%");
	}
	*/
	
	public void setVariables(String usrItemCost, String usrShippingCost, String usrParcelCost, String usrVAT, String usrCat, String usrSubCat) {
		try {
			itemCost = Double.parseDouble(usrItemCost);
			shippingCost = Double.parseDouble(usrShippingCost);
			parcelCost = Double.parseDouble(usrParcelCost);
			vat = Double.parseDouble(usrVAT);
			//percEbayFee = (1+vat)*Double.parseDouble(variableArray[4]);

			
			JSONReader json = new JSONReader();

			double fee = (double) json.getFee(usrCat, usrSubCat);

			if (fee==-1) {
				quitApp("Category or Sub Category not found. Please check spelling.");
			} else {
				double feePercentage = (fee/100);
				percEbayFee = (1+vat)*feePercentage;
			}

		} catch (NumberFormatException e) {
			quitApp("Please ensure variables in file 'Variables.txt' are real numbers.", e);
		}
	}


	public void setInputStream(String input) {
		InputStream inputStream = new ByteArrayInputStream(input.getBytes());
		usrInput = new Scanner(inputStream);
	}


	public void setOutputStream(JTextArea textArea) {
		
		PrintStream printStream = new PrintStream(new swingOutputStream(textArea));

		System.setOut(printStream);
		System.setErr(printStream);
	}
	

	public void Start() {

		if (firstLoop == false) {
			checkVariables();
			printVariables();
			System.out.println("Type anything other than a number to exit.");
			firstLoop = true;
		}
		
		
		System.out.print("priceOfOneGood = ");
		String priceOfOneGoodString = usrInput.nextLine();


		double priceOfOneGood = 0;


		isValidInput = isValidDouble(priceOfOneGoodString);

		
		if(isValidInput) {
			priceOfOneGood = Double.parseDouble(priceOfOneGoodString);


			if (priceOfOneGood == 0) {
			
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
			else {

				ArrayList<Integer> numberOfGoodsToCheckArray = populateNumOfGoodsToCheckArray(usrInput);

				final double priceOfOneGoodFinal = priceOfOneGood;


				numberOfGoodsToCheckArray.forEach((n) -> {

					double[] sellingPriceAndProfitArray = new double[2];

					sellingPriceAndProfitArray = calcSellingPriceAndProfit(n, priceOfOneGoodFinal);
					
					printSellingAndProfit(n, sellingPriceAndProfitArray[0], sellingPriceAndProfitArray[1]);

				});

			}
		}

		Loop(isValidInput);
	}


	public void startWithSwing() {
		
		String priceOfOneGoodString = usrInput.nextLine();


		double priceOfOneGood = 0;


		isValidInput = isValidDouble(priceOfOneGoodString);

		
		if(isValidInput) {
			priceOfOneGood = Double.parseDouble(priceOfOneGoodString);

			if (priceOfOneGood == 0) {
				
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
			else {
				ArrayList<Integer> numberOfGoodsToCheckArray = populateNumOfGoodsToCheckArray(usrInput);

				final double priceOfOneGoodFinal = priceOfOneGood;


				numberOfGoodsToCheckArray.forEach((n) -> {

					double[] sellingPriceAndProfitArray = new double[2];

					sellingPriceAndProfitArray = calcSellingPriceAndProfit(n, priceOfOneGoodFinal);
					
					printSellingAndProfit(n, sellingPriceAndProfitArray[0], sellingPriceAndProfitArray[1]);

				});

			}
		}
	}


	public boolean isValidDouble(String input) {
		boolean isValid = false;

		if (input.matches("-?\\d+(\\.\\d+)?")) {
			isValid = true;
		}

		return isValid;
	}


	public ArrayList<Integer> populateNumOfGoodsToCheckArray(Scanner usrInput) {
		ArrayList<Integer> intArray = new ArrayList<Integer>();

		do {
			//System.out.println("Leave empty or type an integer greater than 0.");
			//System.out.print("numberOfGoods = ");
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


	public double[] calcSellingPriceAndProfit(int n, double priceOfOneGood) {

		double sellingPrice, costOfGoods, claimBackVAT, giveGovVAT, profit;

		//sell price of goods = price of a single good * number of goods sold
		sellingPrice = Round(priceOfOneGood*n);

		//cost of goods sold = cost of goods sold + shippingCost + parcelCost + flat paypal fee + %(ebayfee & paypalfee) of selling price
		costOfGoods = Round(itemCost*n + (shippingCost + parcelCost + flatPaypalFee) + (percPaypalFee+percEbayFee)*sellingPrice);

		//if claiming VAT from shipping, use below 'claimBackVAT' (probably once I have got a business royal mail acc? Since I can view reports)
		//double claimBackVAT = round(vat*(itemCost*n + shippingCost + percEbayFee*sellingPrice));
		claimBackVAT = Round(vat*(itemCost*n + percEbayFee*sellingPrice));
		

		giveGovVAT = Round(sellingPrice*(vat/(1+vat)));

		profit = Math.min(
				Round(sellingPrice+claimBackVAT-costOfGoods-giveGovVAT), Round(sellingPrice-costOfGoods)
		);


		double[] doubleArray = new double[2];
		doubleArray[0] = sellingPrice;
		doubleArray[1] = profit;


		return doubleArray;
	}


	public double Round(double input) {
		return Double.parseDouble(String.format("%.2f",input));
	}
	

	public void printSellingAndProfit(int n, double sellingPrice, double profit) {
		System.out.println(n + ":  s = " + sellingPrice + ", p = " + profit);
	}


	public void Loop(boolean bool) {
		if (bool == true) {
			Start();
		} else {
			quitApp();
		}
	}


	public void quitApp(String error, Exception ex) {
		ex.printStackTrace();
		System.out.println(error);
        System.exit(1);
	}


	public void quitApp(String error) {
		System.out.println("Error: " + error);
        System.exit(1);
	}


	public void quitApp() {
		try {
			System.out.print("exiting");
            Thread.sleep(200);
            System.out.print(".");
            Thread.sleep(200);
            System.out.print(".");
            Thread.sleep(200);
            System.out.print(".\n");
            Thread.sleep(200);

            System.exit(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}