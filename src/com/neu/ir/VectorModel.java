/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.ir;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 *
 * @author pu
 */
public class VectorModel {
    int scale = 5;
    public BigDecimal  tfCalculator(int wordFreq, int totalTermDoc){
        BigDecimal wordCount = new BigDecimal(wordFreq);
        BigDecimal totalCount = new BigDecimal(totalTermDoc);
      //  BigDecimal tf =new BigDecimal(wordFreq/totalTermDoc);
        return wordCount.divide(totalCount,scale, RoundingMode.HALF_UP);
    }
    
    public BigDecimal  idfCalculator(int numberOfDoc, int noOfDocWithTermt){
        BigDecimal docNumbers = new BigDecimal(numberOfDoc);
        BigDecimal docNoTerm = new BigDecimal(noOfDocWithTermt);
      //  BigDecimal tf =new BigDecimal(wordFreq/totalTermDoc);
       // return wordCount.divide(totalCount,scale, RoundingMode.HALF_UP);
                
        return new BigDecimal(1+Math.log(numberOfDoc / noOfDocWithTermt));
    }
    
    public BigDecimal calculateDocNormalize(BigDecimal tf, BigDecimal idf, BigDecimal maxFreq){
        //    maxfreqj = the maximum frequency of any term in query maxfreqq
        //    IDFi = the IDF of term i in the entire collection
        //    freqij = the frequency of term i in document j
        //    C should be set to low values (near 0) for automatically indexed collections, and to higher values such as 1 for manually indexed collections.
        //    K should be set to low values (0.3 was used by Croft) for collections with long (35 or more terms) documents, and to higher values (0.5 or higher) for collections with short documents, reducing the role of within-document frequency.
        //cfreqi,j = K+(1+K)freqij/maxfreqj
        //similarity = sum((C+IDFi)*cfreqi,j)
        
        //considering C = 0.5
       //double d = 0.5 + (1-0.5)*(tf*idf);        
       
        BigDecimal result = tf.multiply(maxFreq).setScale(5, RoundingMode.HALF_UP);
        BigDecimal c= result.multiply(new BigDecimal(0.3));
        //BigDecimal similarity = idf.multiply(c).setScale(5,RoundingMode.HALF_UP);//(0+idf)*c;
        
        return c.add(new BigDecimal(0.5));
        
    }
    
    public BigDecimal calculateSimilarity(BigDecimal idf,BigDecimal tf,BigDecimal maxFreq){
        BigDecimal cfreq = calculateDocNormalize(tf, idf,maxFreq);
        BigDecimal firstTerm = idf.add(new BigDecimal(1));
        return firstTerm.multiply(cfreq);
    }
    
    public BigDecimal tfidf(BigDecimal tf, BigDecimal idf){
        return tf.multiply(idf).setScale(5, RoundingMode.HALF_UP);
    }
    
}
