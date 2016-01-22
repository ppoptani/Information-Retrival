/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.test;

import com.neu.ir.InvertedIndexII;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author pu
 */
public class IITTestII {

    public static void main(String[] args) {

        try {
            ArrayList<String> fileNames = new ArrayList<>();
            fileNames.add("amaz.txt");
            fileNames.add("amaz0.txt");
            fileNames.add("amaz1.txt");
            String filePath = "C:\\Users\\priya\\Desktop\\Files";
            InvertedIndexII idx = new InvertedIndexII();

            File[] allfiles = new File(filePath).listFiles();
            for (File f : allfiles) {
                if (f.getName().endsWith(".txt")) {
                    idx.indexFile(f);
                }
            }

//            for (int i = 0; i < fileNames.size(); i++) {
//                idx.indexFile(new File(fileNames.get(i)));
//            }
            String[] str = {"oregon", "drops", "dad", "am", "amoeba", "coffee", "she", "should", "design", "success", "guffaw", "please", "teddy", "calendar", "pointing", "snow", "a", "juilliard", ""};

            //idx.search2(Arrays.asList(str));
            String[] str1 = {"am", "design", "success", "a",};
            idx.search2(Arrays.asList(str1));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
