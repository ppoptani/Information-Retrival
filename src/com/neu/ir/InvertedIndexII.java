/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.ir;

import com.neu.model.WordDetails;
import com.neu.test.Stemmer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author pu
 */
public class InvertedIndexII {

    HashMap<String, HashMap<File, WordDetails>> index = new HashMap<>();
    List<String> files = new ArrayList<>();
    Stemmer s = new Stemmer();
    Map<File, Integer> resultant;
    VectorModel vm = new VectorModel();
    Map<File,Integer> noOfWords = new HashMap<>();

    public void indexFile(File file) throws IOException {
        int fileno = files.indexOf(file.getPath());
        if (fileno == -1) {
            files.add(file.getPath());
            fileno = files.size() - 1;
        }

        int pos = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            for (String _word : line.split("\\W+")) {
                String word = _word.toLowerCase();
                pos++;

                HashMap<File, WordDetails> idx = index.get(stemming(word));
                if (idx == null) {
                    idx = new HashMap<>();
                    index.put(word, idx);
                    idx.put(file, initializeObject(new WordDetails(), pos, 1));
                } else if (idx != null) {
                    WordDetails wd = idx.get(file);
                    if (wd != null) {
                        wd = initializeObject(wd, pos, wd.getWordFreq() + 1);
                    } else if (wd == null) {
                        idx.put(file, initializeObject(new WordDetails(), pos, 1));
                    }
                }
            }
        }
        
        noOfWords.put(file,pos);
        System.out.println("indexed " + file.getPath() + " " + pos + " words");
    }

    public WordDetails initializeObject(WordDetails wd, int pos, int freq) {
        wd.addPosition(pos);
        wd.setWordFreq(freq);
        return wd;
    }

    public String stemming(String word) {
        for (char c : word.toCharArray()) {
            s.add(c);
        }
        s.stem();
        return s.toString();
    }

    public void search2(List<String> words) {

        HashMap<File, WordDetails> searchedDoc = null;
        for (String _word : words) {
            String word = _word.toLowerCase();
            HashMap<File, WordDetails> idx = index.get(word);
            

            if (idx != null) {
               // searchedDoc = reHash(idx);              
                resultantSet(idx, words.size());
                vectorModel(word,idx);
            }
        }

        printResult();
        // return searchedDoc;
    }
    
      public void vectorModel(String word,HashMap<File, WordDetails> idx){
        HashMap<File,BigDecimal> vectorScore = new HashMap<>();
        
        Iterator it = idx.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            WordDetails wd = (WordDetails) pair.getValue();
            if (wd != null) {
                BigDecimal tf;
                tf = (BigDecimal)vm.tfCalculator(wd.getWordFreq(), noOfWords.get(pair.getKey()));
                BigDecimal idf =(BigDecimal) vm.idfCalculator(noOfWords.size(), idx.size());        
                BigDecimal result = tf.multiply(idf).setScale(5, RoundingMode.HALF_UP);
                vectorScore.put((File) pair.getKey(), result);
                vectorScore = (HashMap<File, BigDecimal>) sortByValue(vectorScore);
                printVectorResult(vectorScore);
            }             
        }        
    }
      
      public void printVectorResult(HashMap<File,BigDecimal> vectorScore) {
        //entriesSortedByValues(mp);
        Iterator it = vectorScore.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            File f = (File) pair.getKey();
            System.out.println(f.getName() + " = " + pair.getValue() + " ,");
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    
    public static HashMap<Integer, WordDetails> reHash(HashMap<File, WordDetails> idx) {
        HashMap<Integer, WordDetails> temp = new HashMap<>();

        Iterator it = idx.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            WordDetails wd = (WordDetails) pair.getValue();
            if (wd != null) {
                //temp.put((Integer) pair.getKey(), wd.getWordFreq());
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }

        return temp;
    }
    
  

    public void resultantSet(HashMap<File, WordDetails> idx, int wordSize) {
        if (resultant == null) {
            resultant = new TreeMap<>();
        }

        Iterator it = idx.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (resultant.containsKey(pair.getKey())) {
                resultant.put((File) pair.getKey(), resultant.get(pair.getKey()) - 1);
            } else {
                resultant.put((File) pair.getKey(), wordSize);
            }
        }

    }

    public void printResult() {
        //entriesSortedByValues(mp);
        Iterator it = resultant.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            File f = (File) pair.getKey();
            System.out.print(f.getName() + " = " + pair.getValue() + " ,");
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    
      public static Map sortByValue(Map unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

}
