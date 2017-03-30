package sample;

import java.util.ArrayList;

/**
 * Created by gsamadova on 2/24/2016.
 */
public class Utils {

    static public ArrayList<Integer> allTN = new ArrayList<>();
    static boolean online = true;

    static boolean isStageOpen = true;

    public static void fillArray()  {
        allTN.add(702000025); allTN.add(702000066); allTN.add(702000914); allTN.add(776479120); allTN.add(702000916);
        allTN.add(702000924); allTN.add(702000922); allTN.add(702000923); allTN.add(702000927); allTN.add(702000061);
        allTN.add(776479008); allTN.add(776479125); allTN.add(776479119); allTN.add(702000026); allTN.add(702000063);
        allTN.add(702000072); allTN.add(776479123); allTN.add(776479114); allTN.add(702000064); allTN.add(702000070);
        allTN.add(702000028); allTN.add(702000062); allTN.add(776479010); allTN.add(702000022); allTN.add(776479012);
        allTN.add(776479002); allTN.add(702000879); allTN.add(776479001); allTN.add(776479003); allTN.add(702000027);
        allTN.add(702000060); allTN.add(702000621); allTN.add(702300158); allTN.add(776479005); allTN.add(776479015);
        allTN.add(776479020); allTN.add(776479022); allTN.add(776479117); allTN.add(776479126); allTN.add(702000024);
        allTN.add(702000622); allTN.add(702000625); allTN.add(702300157); allTN.add(776479019); allTN.add(776479121);
        allTN.add(702000071); allTN.add(776479021); allTN.add(702000065); allTN.add(702000883); allTN.add(702000069);
        allTN.add(702000068); allTN.add(776479286); allTN.add(776479118); allTN.add(776479127); allTN.add(776479004);
        allTN.add(776479129); allTN.add(776479116); allTN.add(776479013);
    }

    public static ArrayList<Integer> findDeactive(ArrayList<Integer> activeN)   {
        ArrayList<Integer> deactiveN = new ArrayList<>();
        for(Integer number : allTN)   {
            if(!activeN.contains(number)) deactiveN.add(number);
        }

        return deactiveN;
    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static boolean doesContain(ArrayList<TestNumberController.Item> list, TestNumberController.Item item)    {
        for(TestNumberController.Item listItem : list)  {
            if(listItem.getMsisdn().equals(item.getMsisdn()) && listItem.getEnv().equals(item.getEnv())) {
                return true;
            } else continue;
        }
        return false;
    }
}
