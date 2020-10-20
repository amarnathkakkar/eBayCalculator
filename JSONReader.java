import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class JSONReader {

	public long getPercent(String cat, String subCat) {

		JSONParser jsonParser = new JSONParser();
		JSONObject returnObject = null;

		try (FileReader reader = new FileReader("finalvaluefees.json")) {

			Object obj = jsonParser.parse(reader);

			JSONArray categoryList = (JSONArray) obj;



			for (int i=0; i<categoryList.size(); i++) {

				JSONObject catObject = (JSONObject) categoryList.get(i);


				if (catObject.get("category").equals(cat)) {
					

					if (subCat != null && !subCat.isEmpty()) {

						JSONArray subCategoryList = (JSONArray) catObject.get("subcategory");


						for (int j=0; j<subCategoryList.size(); j++) {

							JSONObject subCatObject = (JSONObject) subCategoryList.get(j);

							if (subCatObject.get("subcategory").equals(subCat)) {
								returnObject = subCatObject;
								j = subCategoryList.size();
							}
						}


					} else {
						returnObject = catObject;
					}

					i = categoryList.size();
				}

			}


		} catch (FileNotFoundException | ParseException e) {
            e.printStackTrace();
            System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}	


		if (returnObject != null) {
			return (long) returnObject.get("percent");
		} else {
			return -1;
		}

	}





	/* //for testing
	public static void main(String args[]) {
		JSONReader test = new JSONReader();

		long result = test.getPercent("Art");

		System.out.println(result);
	}
	*/
	
}