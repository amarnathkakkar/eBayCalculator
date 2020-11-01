import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;




public class JSONReader {



	public long getFee(String cat, String subCat) {

		JSONObject returnObject = null;

		JSONArray categoryList = getJSONArray("finalvaluefees.json");;


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

		if (returnObject != null) {
			return (long) returnObject.get("percent");
		} else {
			return -1;
		}

	}



	public String[] getCategories() {

		ArrayList<String> catList = new ArrayList<String>();

		JSONArray categoryJSONArray = getJSONArray("finalvaluefees.json");


		for(int i = 0; i < categoryJSONArray.size(); i++){

			JSONObject catObject = (JSONObject) categoryJSONArray.get(i);

			catList.add((String)catObject.get("category"));
		}

		return catList.toArray(new String[0]);
	}



	public String[] getSubCat(String cat) {

		ArrayList<String> subCatList = new ArrayList<String>();
		subCatList.add("");

		JSONArray categoryJSONArray = getJSONArray("finalvaluefees.json");

		for(int i=0; i<categoryJSONArray.size(); i++){

			JSONObject catObject = (JSONObject) categoryJSONArray.get(i);

			if (catObject.get("category").equals(cat) && catObject.containsKey("subcategory")) {

				JSONArray subCategoryList = (JSONArray) catObject.get("subcategory");

				for (int j=0; j<subCategoryList.size(); j++) {

					JSONObject subCatObject = (JSONObject) subCategoryList.get(j);

					subCatList.add((String)subCatObject.get("subcategory"));
				}

				i = categoryJSONArray.size();
			}
		}
		
		return subCatList.toArray(new String[0]);
	}



	public JSONArray getJSONArray(String fileName) {

		try (FileReader reader = new FileReader(fileName)) {
			JSONParser jsonParser = new JSONParser();
			Object obj = jsonParser.parse(reader);
			JSONArray fileJSONArray = (JSONArray) obj;
			return fileJSONArray;
		} catch (ParseException | FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}


	/*
	public static void main(String args[]) {
		JSONReader test = new JSONReader();

		String testCat[] = test.getCategories();
		for (int i=0; i<testCat.length; i++) {
			System.out.println(testCat[i]);
		}
		
		String testsubCat[] = test.getSubCat("Baby");
		for (int i=0; i<testsubCat.length; i++) {
			System.out.println(testsubCat[i]);
		}
		
		Long testfee = test.getFee("Baby", "Bath Toys");
		System.out.println(testfee);
	}
	*/
	
	
	
}