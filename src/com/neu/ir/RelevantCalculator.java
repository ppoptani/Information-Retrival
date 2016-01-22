/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.ir;

import com.neu.model.StopWords;
import com.neu.model.WordDetails;
import com.neu.test.Stemmer;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pu
 */
public class RelevantCalculator {

    //steps: highly ranked doc and get relevant word
    //position matching
    public HashMap<String, HashMap<File, WordDetails>> indexRC = new HashMap<>();
    List<String> files = new ArrayList<>();
    Stemmer s = new Stemmer();
    //resultant to store documnet number and 
    Map<File, Integer> resultantRC;
    Map<File, Map<String, Set<Integer>>> brmRC;
    Map<File, Set<Integer>> resultant = new LinkedHashMap<>();
    List<String> queryWords;
    Map<File, BigDecimal> resultantDocWeight;

    public Set<Integer> positions = new HashSet<>();

    public RelevantCalculator(HashMap<String, HashMap<File, WordDetails>> index, Map<File, Map<String, Set<Integer>>> brm, Map<File, Integer> resultant, Map<File, BigDecimal> resultantDocWeight) {
        indexRC = index;
        brmRC = brm;
        resultantRC = resultant;
        this.resultantDocWeight = resultantDocWeight;
    }

    public void initial(List<String> words) {
        queryWords = words;
        for (Map.Entry pair : resultantRC.entrySet()) {
            // HashMap<File, WordDetails> idx = (HashMap<File, WordDetails>) pair.getValue();

            int docRank = (Integer) pair.getValue();
            if (docRank == 0) {
                //work on doc hvg all words
                comparePositions((File) pair.getKey());
                if (positions.size() <= words.size()) {
                    resultant.put((File) pair.getKey(), positions);
                    positions = new HashSet<>();
                }
            } else {//if (docRank == words.size() - 1) {
                //docs with just on e word
                relCalculation((File) pair.getKey());

            }
        }

        displayPositions();
    }

    public void comparePositions(File file) {

        Map<String, Set<Integer>> map = brmRC.get(file);//relevant doc
        map = sortMapAccQuery(map);

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Set<Integer> pos = (Set<Integer>) pair.getValue();
            if (pos != null) {
                if (it.hasNext()) {
                    Map.Entry pair2 = (Map.Entry) it.next();
                    Set<Integer> pos2 = (Set<Integer>) pair2.getValue();
                    if (pos != null) {
                        sortPos(pos, pos2);
                    }
                } else {
                    sortPos(pos, positions);
                }
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }

    }

    //sort common positions
    public void sortPos(Set<Integer> pos, Set<Integer> pos2) {
        for (int i : pos) {
            if (pos2.contains(i + 1)) {
                if (!positions.contains(i)) {
                    positions.add(i);
                }
                if (!positions.contains(i + 1)) {
                    positions.add(i + 1);
                }
                break;
            }
        }
    }

    public void displayPositions() {
        Iterator it = resultant.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " " + pair.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public Map<String, Set<Integer>> sortMapAccQuery(Map<String, Set<Integer>> map) {
        Map<String, Set<Integer>> sortedMap = new LinkedHashMap<>();
        for (String s : queryWords) {
            sortedMap.put(s, map.get(s));
        }

        return sortedMap;
    }

    //docs which doesnt contain whole query but part of query with relevant data. removing stop words
    public void relCalculation(File file) {
        Map<String, Set<Integer>> map = brmRC.get(file);//relevant doc
        if (containsRelWord(map) && resultantDocWeight.containsKey(file)) {
            // resultant.put(file, positions);
            comparePositions(file);
            resultant.put(file, positions);
            positions = new HashSet<>();
        }
    }

    public boolean containsRelWord(Map<String, Set<Integer>> map) {
        StopWords s = new StopWords();
        int stopCount = 0, relCount = 0;

        for (Map.Entry pair : map.entrySet()) {
            Set<Integer> pos = (Set<Integer>) pair.getValue();
            if (s.stopwords.contains(pair.getKey())) {
                stopCount++;
            } else {
                relCount++;
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }

        return relCount > 0;
    }

    public int noOfRelWords() {
        StopWords s = new StopWords();
        int stopCount = 0, relCount = 0;
        for (String w : queryWords) {
            if (s.stopwords.contains(w)) {
                stopCount++;
            } else {
                relCount++;
            }
        }

        //it.remove(); // avoids a ConcurrentModificationException
        return relCount;
    }

}
