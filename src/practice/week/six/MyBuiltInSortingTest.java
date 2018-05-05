package practice.week.six;

import java.util.*;

public class MyBuiltInSortingTest {

    //optimized merge sort (fast in worst cases, no nearly sorted data, stable)
    public static void main(String[] args){
        Random random = new Random();
        List<Integer> numsToSort = new ArrayList<Integer>();
        for(int i = 0; i<5; i++){
            numsToSort.add(random.nextInt(100));
        }
        Collections.sort(numsToSort);
        System.out.println("New array after builtin sort: " + numsToSort.toString());
    }
}
