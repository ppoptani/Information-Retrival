/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.model;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author pu
 */
public class WordDetails implements Comparable {

    private Set<Integer> position;
    private int wordFreq;
    private BigDecimal tfIdfFactor;

    public WordDetails() {
        position = new TreeSet<>();
      //  tfIdfFactor = new BigDecimal();
    }

    public int getWordFreq() {
        return wordFreq;
    }

    public void setWordFreq(int wordFreq) {
        this.wordFreq = wordFreq;
    }

    public Set<Integer> getPosition() {
        return position;
    }

    public void setPosition(Set<Integer> position) {
        this.position = position;
    }

    public void addPosition(int pos) {
        position.add(pos);
    }

    public BigDecimal getTfIdfFactor() {
        return tfIdfFactor;
    }

    public void setTfIdfFactor(BigDecimal tfIdfFactor) {
        this.tfIdfFactor = tfIdfFactor;
    }

    @Override
    public String toString() {
        return wordFreq + "  "+ tfIdfFactor + " " + position; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int compareTo(Object o) {
        return new Integer(this.wordFreq).compareTo(((WordDetails) o).wordFreq); //To change body of generated methods, choose Tools | Templates.
    }

}
