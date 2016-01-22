/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.ir;

import com.neu.model.StopWords;
import com.neu.model.Tokenize;
import com.neu.model.WordDetails;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.neu.test.Stemmer;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author pu
 */
public class InvertedIndex {

    public HashMap<String, HashMap<File, WordDetails>> index = new HashMap<>();
    List<String> files = new ArrayList<>();
    Stemmer s = new Stemmer();
    //resultant to store documnet number and 
    Map<File, Integer> resultant;
    Map<File, BigDecimal> resultantDocWeight;
    Map<File, Map<String, Set<Integer>>> brm;
    VectorModel vm = new VectorModel();
    Map<File, Integer> noOfWords = new HashMap<>();
    HashMap<String, Integer> searchWords;

    //Creating Inverted index
    public void indexFile(File file) throws IOException {
        int fileno = files.indexOf(file.getPath());
        if (fileno == -1) {
            files.add(file.getPath());
            fileno = files.size() - 1;
        }

        int pos = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            for (String _word : line.split("\\W+")) { //Tokenize.Tokenize(line)
                String word = stemming(_word.toLowerCase());
                pos++;

                HashMap<File, WordDetails> idx = index.get(word);
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

        noOfWords.put(file, pos);
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

    public HashMap<File, Integer> search(List<String> words) {
        long totalDuration = 0;
        HashMap<File, Integer> searchedDoc = null;
        for (String _word : words) {
            long startTime = System.nanoTime();
            Set<String> answer = new HashSet<>();
            String word = _word.toLowerCase();
            HashMap<File, WordDetails> idx = index.get(word);
            if (idx != null) {

                System.out.print(word + " ");
                searchedDoc = reHash(idx);
                printMap(searchedDoc);
            }

            System.out.println("");
            long stopTime = System.nanoTime();
            long duration = (stopTime - startTime);
            totalDuration = totalDuration + duration;
        }
        System.out.println("Duration " + totalDuration);
        return searchedDoc;
    }

    public static void printMap(Map mp) {
        //entriesSortedByValues(mp);
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue() + " ,");
            // it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public static HashMap<File, Integer> reHash(HashMap<File, WordDetails> idx) {
        HashMap<File, Integer> temp = new HashMap<>();

        Iterator it = idx.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            WordDetails wd = (WordDetails) pair.getValue();
            if (wd != null) {
                temp.put((File) pair.getKey(), wd.getWordFreq());
            }
            //it.remove(); // avoids a ConcurrentModificationException
        }

        return (HashMap<File, Integer>) sortByValue(temp);
    }

    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
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

    public void booleanModel() {

    }

    public Map<File, Set<Integer>> search2(String line) throws IOException {
        HashMap<File, Integer> searchedDoc = null;
        searchWords = new HashMap<>();
        List<String> words = new ArrayList<>();
        for (String _word : line.split("\\W+")) {
            String word = stemming(_word.toLowerCase());
            words.add(word);
            if (searchWords.containsKey(word)) {
                searchWords.put(word, searchWords.get(word) + 1);
            } else {
                searchWords.put(word, 1);
            }
        }
        for (String _word : words) {
            String word = _word.toLowerCase();
            HashMap<File, WordDetails> idx = index.get(word);
            if (idx != null) {
                searchedDoc = reHash(idx);
                resultantSet(searchedDoc, words.size());
                StopWords w = new StopWords();
                resultantDocSet();
            }
        }

        getTheRelaventDoc(words);

        if ((brm != null && brm.size()>0) || resultantDocWeight != null) {
             printMap(brm);
            printMap(resultantDocWeight);
            printResult();
            RelevantCalculator rc = new RelevantCalculator(index, brm, resultant, resultantDocWeight);
            rc.initial(words);
            rc.displayPositions();
            return rc.resultant;
        } else {
            return null;
        }
    }

    public void resultantSet(HashMap<File, Integer> idx, int wordSize) {
        if (resultant == null) {
            resultant = new HashMap<>();
        }

        Iterator it = idx.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            if (resultant.containsKey(pair.getKey())) {
                resultant.put((File) pair.getKey(), resultant.get(pair.getKey()) - 1);
            } else {
                resultant.put((File) pair.getKey(), wordSize - 1);
            }
        }

        resultant = sortByValue(resultant);

    }

    public Map<File, Set<Integer>> builtOutput(RelevantCalculator rc) {
        return rc.resultant;
    }

    public void getTheRelaventDoc(List<String> words) {
        brm = new HashMap<>();
        for (String _word : words) {
            String word = _word.toLowerCase();
            HashMap<File, WordDetails> idx = index.get(word);

            if (idx != null) {
                for (Map.Entry pair : resultant.entrySet()) {
                    WordDetails wd = idx.get(pair.getKey());
                    if (wd != null) {
                        drawIndex(word, wd, (File) pair.getKey());
                    }
                }
            }
        }
    }

    public void drawIndex(String word, WordDetails wd, File key) {
        Map<String, Set<Integer>> idx = brm.get(key);
        if (idx == null) {
            idx = new HashMap<>();
            idx.put(word, wd.getPosition());
            brm.put(key, idx);
            //idx.put(fileno + 1, initializeObject(new WordDetails(), pos, 1));
        } else if (idx != null) {
            if (!idx.containsKey(word)) {
                idx.put(word, wd.getPosition());
            }
        }
    }

//    public ResultantDetails initializeBRM(String word, WordDetails wd){
//        ResultantDetails rd =new ResultantDetails();
//        rd.setWord(word);
//        rd.setPositions(wd.getPosition());
//        return rd;
//    }
    public void printResult() {
        //entriesSortedByValues(mp);
        Iterator it = resultant.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.print(pair.getKey() + " = " + pair.getValue() + " ,");
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }

    public void updateTheScore() {
        for (Map.Entry pair : index.entrySet()) {
            HashMap<File, WordDetails> idx = (HashMap<File, WordDetails>) pair.getValue();
            if (idx != null) {
                vectorModel((String) pair.getKey(), idx);
            }
        }
    }

    public void vectorModel(String word, HashMap<File, WordDetails> idx) {
        HashMap<File, BigDecimal> vectorScore = new HashMap<>();

        Iterator it = idx.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            WordDetails wd = (WordDetails) pair.getValue();
            if (wd != null) {
                BigDecimal result = calWeight(idx, wd, pair);
                vectorScore.put((File) pair.getKey(), result);
                wd.setTfIdfFactor((BigDecimal) result);
                vectorScore = (HashMap<File, BigDecimal>) sortByValue(vectorScore);
                idx.put((File) pair.getKey(), wd);
                //update(idx,word);
            }
        }
    }

    public BigDecimal calWeight(HashMap<File, WordDetails> idx, WordDetails wd, Map.Entry pair) {
        BigDecimal tf = (BigDecimal) vm.tfCalculator(wd.getWordFreq(), noOfWords.get(pair.getKey()));
        BigDecimal idf = (BigDecimal) vm.idfCalculator(noOfWords.size(), idx.size());
        return vm.tfidf(tf, idf);
    }

    public void update(HashMap<File, WordDetails> idx, String word) {
        index.put(word, idx);
    }

    //with doc weight
    public void resultantDocSet() {
        resultantDocWeight = new HashMap<>();
        for (Map.Entry pair : searchWords.entrySet()) {
            if (index.containsKey(pair.getKey())) {
                HashMap<File, WordDetails> idx = index.get(pair.getKey());
                BigDecimal result = new BigDecimal(0);
                Iterator it = idx.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair1 = (Map.Entry) it.next();
                    WordDetails wd = (WordDetails) pair1.getValue();
                    if (wd != null) {
                        result = result.add(calDocWeight(idx, wd, pair1, (int) pair.getValue()));
                        resultantDocWeight.put((File) pair1.getKey(), result);

                    }
                }

            }
        }
    }

    public BigDecimal calDocWeight(HashMap<File, WordDetails> idx, WordDetails wd, Map.Entry pair, int maxFreq) {
        BigDecimal tf = (BigDecimal) vm.tfCalculator(wd.getWordFreq(), noOfWords.get(pair.getKey()));
        BigDecimal idf = (BigDecimal) vm.idfCalculator(noOfWords.size(), idx.size());
        BigDecimal result = vm.tfidf(tf, idf);
        return vm.calculateSimilarity(tf, idf, new BigDecimal(maxFreq));
    }

}
