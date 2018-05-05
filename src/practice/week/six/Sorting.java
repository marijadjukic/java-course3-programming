package practice.week.six;

import java.util.*;

public class Sorting {
    //selection sort
    public static void selectionSort(int[] vals){
        int indexMin;
        for(int i = 0; i<vals.length-1; i++){
            indexMin = i;
            for(int j = i+1; j<vals.length; j++){
                if(vals[j]<vals[indexMin]){
                    indexMin = j;
                }
            }
            swap(vals, indexMin, i);
        }
    }

    public static void swap(int[]array, int index, int position){
        int min = array[index];
        int value = array[position];
        array[position] = min;
        array[index] = value;
        System.out.print(array[position] + ", ");
    }

    //insertion sort
    public static void mysterySort(int[] vals){
        int currInd;
        for(int pos=1; pos<vals.length; pos++){
            currInd = pos;
            while(currInd > 0 && vals[currInd]<vals[currInd-1]){
                swap(vals, currInd, currInd-1);
                currInd = currInd-1;
            }
        }
    }

    public static void main(String[] args){
        int array[] = {5, 12, 4, 36, 22, 13};
        selectionSort(array);
    }
}
