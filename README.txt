eBayProfitCalculator


Before using, hardcode the following into eBayProfitCalculator.java file:

itemCost,
shippingCost,
parcelCost,
vat (if different to default of 20%)

Run 'javac eBayProfitCalculator.java'
Run 'java eBayProfitCalculator'


Functionalities:

1. Displays listing price, and profit or loss of listing:

	i. Choose selling price of one good (a number up to 2 d.p)
		ii. Choose number of goods you would like to sell together (note: list price = price of one good * number of goods),
		or
		iii. Or leave blank (number of goods defaulted from 1 to 10)
	iv. Press enter


2. Displays minimum listing price to achieve profit greater than (or equal to) 0:

	i. Set price of one good to 0
	ii. Follow steps ii or iii, and iv above


Format of results:

	x: s = y, p = z
	...

where x = number of goods, s = list price, p = profit/loss of listing (One sale of the listing)