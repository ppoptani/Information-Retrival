/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.ir;

import com.neu.model.WordDetails;
import java.util.Map;

/**
 *
 * @author pu
 */
public class ValueComparator {    
    
    Map<Integer, WordDetails> base;
    public ValueComparator(Map<Integer, WordDetails> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Integer a, Integer b) {
        return ((WordDetails)base.get(a)).compareTo(base.get(b));
    }

}
