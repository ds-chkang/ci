package medousa.direct.category;

import java.util.ArrayList;
import java.util.HashMap;

public class MyDirectGraphCategoryList
extends ArrayList<MyDirectGraphCategory> {

    // Utility variable for searching category index in a large nodeTableData set.
    private static HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();

    public MyDirectGraphCategoryList() {}

    public void createCategoriesForRealVariable(MyDirectGraphCategory category) {
        if (this.size() > 0) this.categoryMap.put(category.getName().trim().toUpperCase(), (this.size()-1));
        else this.categoryMap.put(category.getName().trim().toUpperCase(), this.size());
        this.add(category);
    }

    public void display() {
        for (MyDirectGraphCategory category : this) {
            category.display();
        }
    }
    public MyDirectGraphCategory getCategory(int categoryIndex) { return this.get(categoryIndex); }
    public MyDirectGraphCategory getCategory(String categoryName) {
        return this.get(this.categoryMap.get(categoryName.trim().toUpperCase()));
    }
    public static boolean isVariableExists(String variable) {
        return categoryMap.containsKey(variable); }

}
