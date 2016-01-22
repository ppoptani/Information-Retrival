/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.test;

import com.neu.ir.InvertedIndex;
import com.neu.ir.RelevantCalculator;
import java.io.File;
import static java.lang.reflect.Array.set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author pu
 */
public class IITest {

    public static void main(String[] args) {

//        RelevantCalculator rc = new RelevantCalculator(null,null,null);
//        Set<Integer> list = new HashSet<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//        list.add(5);
//        Set<Integer> list1 = new HashSet<>();
//        list1.add(2);
//        list1.add(3);
//        list1.add(5);
//        list1.add(6);
//        list1.add(7);
//        Set<Integer> list2 = new HashSet<>();
//        list2.add(5);
//        list2.add(2);
//        list2.add(3);
//        list2.add(7);
//        list2.add(8);
//        Set<Integer> list3 = new HashSet<>();
//        list3.add(9);
//        list3.add(2);
//        list3.add(3);
//        list3.add(7);
//        list3.add(8);
//
//        Set<Set<Integer>> pos = new HashSet<>();
//        pos.add(list);
//        pos.add(list1);
//        pos.add(list2);
//        pos.add(list3);
//
//        Iterator iter = pos.iterator();
//        while (iter.hasNext()) {
//            Set<Integer> pos1 = (Set<Integer>) iter.next();
//            Set<Integer> pos2 = null;
//            if (iter.hasNext()) {
//                pos2 = (Set<Integer>) iter.next();
//            } else {
//                pos2 = (Set<Integer>) rc.positions;
//            }
//            rc.sortPos(pos1, pos2);
//        }
//        rc.displayPositions();

        try {
            ArrayList<String> fileNames = new ArrayList<>();
            fileNames.add("amaz.txt");
            fileNames.add("amaz0.txt");
            fileNames.add("amaz1.txt");
            InvertedIndex idx = new InvertedIndex();
            for (int i = 0; i < fileNames.size(); i++) {
                idx.indexFile(new File(fileNames.get(i)));
            }

            idx.updateTheScore();
            idx.printMap(idx.index);
            String[] str = {"oregon", "drops", "dad", "am", "amoeba", "coffee", "she", "should", "design", "success", "guffaw", "please", "teddy", "calendar", "pointing", "snow", "a", "juilliard", ""};

            idx.search(Arrays.asList(str));

            String[] str1 = {"drop", "from", "the", "skyand", "drop"};
            idx.search2("Drops from the skyand");
            //RelevantCalculator rc1 = new RelevantCalculator(idx);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
