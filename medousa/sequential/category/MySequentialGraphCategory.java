package medousa.sequential.category;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;

public class MySequentialGraphCategory {

    private String name;
    private double min = 0.0D;
    private double max = 0.0D;
    private int minBound = 0;
    private int maxBound = 0;
    private int interval = 0;
    private int wholeRange = 0;
    private int headerIndex = 0;
    private int numOfCategories = 0;
    private ArrayList<Integer> categoryIntervals = new ArrayList<Integer>();
    public MySequentialGraphCategory() {}
    public MySequentialGraphCategory(String name, double min, double max, int headerIndex, int numOfCategories) {
        this.name = name.trim().toUpperCase();
        this.min = min;
        this.max = max;
        this.headerIndex = headerIndex;
        this.numOfCategories = numOfCategories;
    }
    public MySequentialGraphCategory(double min, double max, int numOfCategories) {
        this.min = min;
        this.max = max;
        this.numOfCategories = numOfCategories;
    }
    public int getHeaderIndex() { return this.headerIndex; }
    public void setName(String name) { this.name = name.toUpperCase(); }
    public String getName() { return this.name; }
    public void setMin(double min) {
        if (this.min == 0.0) this.min = min;
        else if (this.min > min) this.min = min;
    }
    public void setMax(double max) {
        if ( this.max == 0.0 ) this.max = max;
        else if ( this.max < max ) this.max = max;
    }
    public void setCategoryIntervals() {
        this.maxBound = (int)Math.ceil(this.max);
        this.minBound = (int)(this.min);
        this.wholeRange = this.maxBound - this.minBound;
        this.interval = (int)Math.ceil((double)this.wholeRange/this.numOfCategories);
        for (int i=0; i < this.numOfCategories; i++) {
            if (i > 0) {
                this.categoryIntervals.add((this.minBound + (this.interval * (i + 1))) + i);
            } else {
                this.categoryIntervals.add(this.minBound + (this.interval * (i + 1)));
            }
        }
    }

    public void setCategoryIntervals(List<Long> columnValues) {
        this.maxBound = (int)Math.ceil(this.max);
        this.minBound = (int)(this.min);
        this.wholeRange = this.maxBound - this.minBound;
        this.interval = (int)Math.ceil((double)this.wholeRange/this.numOfCategories);
        for (int i=0; i < this.numOfCategories; i++) {
            if (i > 0) {
                this.categoryIntervals.add((this.minBound + (this.interval * (i + 1))) + i);
            } else {
                this.categoryIntervals.add(this.minBound + (this.interval * (i + 1)));
            }
        }
    }
    public String getCategory(String columnValue) {
        for (int i=0; i < this.categoryIntervals.size(); i++) {
            int inputValue = Integer.valueOf(columnValue);
            if (inputValue <= this.categoryIntervals.get(i)) {
                String inputValueCategory = String.valueOf((this.categoryIntervals.get(i) - this.interval))+"~"+
                        String.valueOf(this.categoryIntervals.get(i));
                return inputValueCategory;
            }
        }
        return null;
    }

    public ArrayList<Integer> getCategoryIntervals() {return categoryIntervals;}

    public void display() {
        System.out.println("Category name: " + this.name);
        System.out.println("Category header index: " + this.headerIndex);
        System.out.println("Category minimum: " + this.min);
        System.out.println("Category maximum: " + this.max);
        System.out.println("Number of categories: " + this.numOfCategories);
        System.out.println("Interval: " + this.interval);
        System.out.print("Category intevals: ");
        for (Integer interval : this.categoryIntervals) {
            System.out.print(" [ " + interval + " ] ");
        }
    }

}
