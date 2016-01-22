/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.exampletest;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author pu
 */
public class TfIdfMain {
    /**
     * Main method
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        DocumentParser dp = new DocumentParser();
        dp.parseFiles("G://"); // give the location of source file
        dp.tfIdfCalculator(); //calculates tfidf
        dp.getCosineSimilarity(); //calculates cosine similarity   
    }
}
