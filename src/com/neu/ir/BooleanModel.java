/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neu.ir;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pu
 */
public class BooleanModel {
    
    //process the boolean query
/*public static ArrayList<Integer> ProcessQuery(String[] query)
{
    //query boolean operator
    String bitWiseOp = ""; 
    //string[] queryTerm = RemoveStopsWords(query.ToUpper().Split(''));
    //remove query term that doesnot appears on document collection
    //FilterQueryTerm(ref queryTerm);
    ArrayList<Integer> previousTermIncidenceV = null;
    ArrayList<Integer> nextTermsIncidenceV = null;
    //holds the bitwise operation result
    List<Integer> resultSet = null;
    //On query X AND Y, X is previousTerm term and Y is nextTerm
    Boolean hasPreviousTerm = false; 
    Boolean hasNotOperation = false;
    for(String term : query)
    {
        //is a term
        if (!booleanOperator.Contains(term)&&!term.equals("BUT"))
        {
            //query case: structure AND NOT analysis
            if (hasNotOperation) 
            {
                
                if (hasPreviousTerm)
                {
                    nextTermsIncidenceV = ProcessBooleanOperator("NOT", 
                      GetTermIncidenc eVector(term), nextTermsIncidenceV);
                }
                //query case: eg.NOT analysis
                else 
                {
                    previousTermIncidenceV = ProcessBooleanOperator("NOT", 
                      GetTermIncid enceVector(term), nextTermsIncidenceV);
                    resultSet = previousTermIncidenceV; 
                }
                hasNotOperation = false;
            }
            else if (!hasPreviousTerm)
            {
                previousTermIncidenceV = GetTermIncidenceVector(term);
                resultSet = previousTermIncidenceV;
                hasPreviousTerm = true; //
            }
            else
            {
                
                nextTermsIncidenceV = GetTermIncidenceVector(term);
            }
        }
        else if (term.Equals("NOT"))
        {
            //indicates that the  term in the next iteration should be complemented.
            hasNotOperation = true;
        }
        else
        {
            //'BUT' also should be evaluated as AND eg. structure BUT
            //NOT semantic should be evaluated as structure AND NOT semantic
            if (term.Equals("BUT")) 
            {
                bitWiseOp = "AND";
            }
            else
            bitWiseOp = term;
        }

        if (nextTermsIncidenceV != null && !hasNotOperation)
        {
            resultSet = ProcessBooleanOperator(bitWiseOp, 
                             previousTermIncidenceV, nextT ermsIncidenceV);
            previousTermIncidenceV = resultSet;
            hasPreviousTerm = true;
            nextTermsIncidenceV = null;
        }
    }

    return resultSet;
} 

public static List<int> ProcessBooleanOperator(String op, 
          List<int> previousTermV,List<int> nextTermV)
{
    List<int> resultSet = new List<int>();
    if(op.Equals("NOT"))
    {
        foreach(int a in previousTermV)
        {
            if (a == 1)
            {
                resultSet.Add(0);
            }
            else
            {
                resultSet.Add(1);
            }
        }
    }
    else if (op.ToUpper().Equals("AND")) //bitwise AND operation
    {
        for (int a = 0; a < previousTermV.Count; a++)
        {
            if (previousTermV[a] == 1 && nextTermV[a] == 1)
            {
                resultSet.Add(1);
            }
            else
            {
                resultSet.Add(0);
            }
        }
    }
    else if (op.ToUpper().Equals("OR")) //bitwise OR operation
    {
        for (int a = 0; a < previousTermV.Count; a++)
        {
            if (previousTermV[a] == 0 && nextTermV[a] == 0)
            {
                resultSet.Add(0);
            }
            else
            {
                resultSet.Add(1);
            }
        }
    }
    return resultSet;
}*/
    
}
