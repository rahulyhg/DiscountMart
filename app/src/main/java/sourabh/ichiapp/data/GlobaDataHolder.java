package sourabh.ichiapp.data;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobaDataHolder {

	private static GlobaDataHolder globaDataHolder;

	public static GlobaDataHolder getGlobaDataHolder() {

		if (null == globaDataHolder) {
			globaDataHolder = new GlobaDataHolder();
		}
		return globaDataHolder;
	}

	private ArrayList<ShoppingCategoryData> listOfCategory = new ArrayList<ShoppingCategoryData>();

	private ConcurrentHashMap<String, ArrayList<ProductData>> productMap = new ConcurrentHashMap<String, ArrayList<ProductData>>();

	private List<ProductVarientData> shoppingList = Collections.synchronizedList( new ArrayList<ProductVarientData>());

	public List<ProductVarientData> getShoppingList() {
		return shoppingList;
	}

	public void setShoppingList(ArrayList<ProductVarientData> getShoppingList) {
		this.shoppingList = getShoppingList;
	}

	public Map<String, ArrayList<ProductData>> getProductMap() {
		return productMap;
	}

	public void setProductMap(ConcurrentHashMap<String, ArrayList<ProductData>> productMap) {
		this.productMap = productMap;
	}

	public ArrayList<ShoppingCategoryData> getListOfCategory() {
		return listOfCategory;
	}

	public void setListOfCategory(ArrayList<ShoppingCategoryData> listOfCategory) {
		this.listOfCategory = listOfCategory;
	}

	public Map<String, ArrayList<ProductData>> getElectricProductMap() {
		return productMap;
	}

	public void setElectricProductMap(
			ConcurrentHashMap<String, ArrayList<ProductData>> electricProductMap) {
		this.productMap = electricProductMap;
	}

}
