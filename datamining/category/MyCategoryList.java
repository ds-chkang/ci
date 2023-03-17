package datamining.category;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCategoryList
extends ArrayList<MyCategory> {

    // Utility variable for searching category index in a large nodeTableData set.
    private static HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();

    public MyCategoryList() {}

    public void createCategoriesForRealVariable(MyCategory category) {
        if (this.size() > 0) this.categoryMap.put(category.getName().trim().toUpperCase(), (this.size()-1));
        else this.categoryMap.put(category.getName().trim().toUpperCase(), this.size());
        this.add(category);
    }

    public void display() {
        for (MyCategory category : this) {
            category.display();
        }
    }
    public MyCategory getCategory(int categoryIndex) { return this.get(categoryIndex); }
    public MyCategory getCategory(String categoryName) {
        return this.get(this.categoryMap.get(categoryName.trim().toUpperCase()));
    }
    public static boolean isVariableExists(String variable) {
        return categoryMap.containsKey(variable); }

}
