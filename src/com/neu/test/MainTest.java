/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author pu
 */
public class MainTest {  

    public static void main(String[] args) {
        char[] w = new char[501];
        Stemmer s = new Stemmer();
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add("amaz.txt");
        fileNames.add("amaz0.txt");
        fileNames.add("amaz1.txt");
       // fileNames.add("amaz3.docx");
        for (int i = 0; i < fileNames.size(); i++) {
            try {            
                
                FileInputStream in = new FileInputStream(fileNames.get(i));               

                try {
                    while (true) {
                        int ch = in.read();
                        if (Character.isLetter((char) ch)) {
                            int j = 0;
                            while (true) {
                                ch = Character.toLowerCase((char) ch);
                                w[j] = (char) ch;
                                if (j < 500) {
                                    j++;
                                }
                                ch = in.read();
                                if (!Character.isLetter((char) ch)) {
                                    /* to test add(char ch) */
                                    for (int c = 0; c < j; c++) {
                                        s.add(w[c]);
                                    }

                                    /* or, to test add(char[] w, int j) */
                                    /* s.add(w, j); */
                                    s.stem();
                                    {
                                        String u;

                                        /* and now, to test toString() : */
                                        u = s.toString();

                                        /* to test getResultBuffer(), getResultLength() : */
                                        /* u =                                         System.out.print(u);
new String(s.getResultBuffer(), 0, s.getResultLength()); */
                                    }
                                    break;
                                }
                            }
                        }
                        if (ch < 0) {
                            break;
                        }
                        System.out.print((char) ch);
                    }
                } catch (IOException e) {
                    System.out.println("error reading " + args[i]);
                    break;
                }
            } catch (FileNotFoundException e) {
                System.out.println("file " + args[i] + " not found");
                break;
            }
        }
    }

}
