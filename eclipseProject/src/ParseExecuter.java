import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.pengyifan.commons.collections.tree.TreeNode;

import DataStructures.BüchiAutomaton;
import DataStructures.Tableau;
import parser.*;

public class ParseExecuter {
	// private String lksString = "5\n--\n0 1 0 0 0 \n0 0 1 0 1 \n0 0 0 1 0 \n0
	// 1 0 0 0 \n0 0 0 0 0 \n--\n0 -> {j < i}\n1 -> {i == j}\n2 -> {i < j}\n3 ->
	// {!(i==j)}\n4 -> {j > i}\n--\n0 -> 0\n1 -> 0\n2 -> 1\n3 -> 4\n4 -> 4\n";

	public ParseExecuter(String s) throws Exception {

		// Parse LTL formula
		LTLTree ltlTree = parseLTL(s);

		// read in LKS file (or TKS)
		Scanner scanner = new Scanner(
				new File("src" + File.separator + "sample files" + File.separator + "/test_no_00_01.tks"));
		String text = scanner.useDelimiter("\\A").next();
		scanner.close(); // TODO: Put this call in a finally block

		// parses LKS file into LKS structure
		LKSGenerator tinycLKS = parseLKS(text);

		// generate tableau from LTL formula
		Tableau tableau = new Tableau(ltlTree.root);

		// generate BA from tableau
		BüchiAutomaton büchiLTL = new BüchiAutomaton(tableau);
		
		// TODO: generate BA from tiny-c LKS
		BüchiAutomaton büchiTinyC = new BüchiAutomaton(tinycLKS.lks);

		// build synchronous product of tableau BA and BA from tiny c program
		BüchiAutomaton productBA = büchiLTL.product(büchiTinyC);

		System.out.println(TreeNode.PRETTY_PRINT.apply(ltlTree.root));
		System.out.println(tableau);
		System.out.println("LTL:          "+büchiLTL);
		System.out.println("Program:      "+tinycLKS.lks);
		System.out.println("and as Büchi: "+büchiTinyC);
		

	}

	/*
	 * pre: s!=null
	 */

	private LTLTree parseLTL(String s) {
		ANTLRInputStream ltlInput = new ANTLRInputStream(s);

		// create a lexer that feeds off of input CharStream
		LTLLexer ltlLexer = new LTLLexer(ltlInput);

		// create a buffer of tokens pulled from the lexer
		CommonTokenStream ltlTokens = new CommonTokenStream(ltlLexer);

		// create a parser that feeds off the tokens buffer
		LTLParser ltlParser = new LTLParser(ltlTokens);

		ParseTree ltlTree = ltlParser.formula(); // begin parsing at formula
													// rule

		ParseTreeWalker walker = new ParseTreeWalker();

		LTLTree treeGenerator = new LTLTree();
		walker.walk(treeGenerator, ltlTree);

		return treeGenerator;
	}

	/*
	 * pre: s != null
	 */
	private LKSGenerator parseLKS(String s) {
		ANTLRInputStream lksInput = new ANTLRInputStream(s);
		LKSLexer lksLexer = new LKSLexer(lksInput);
		CommonTokenStream lksTokens = new CommonTokenStream(lksLexer);
		LKSParser lksParser = new LKSParser(lksTokens);
		ParseTree lksTree = lksParser.kripke();
		ParseTreeWalker lksWalker = new ParseTreeWalker();
		LKSGenerator lks = new LKSGenerator();
		lksWalker.walk(lks, lksTree);
		return lks;
	}


	

	
	private static Set<Set<Object>> cartesianProduct(Set<?> set1, Set<?> set2) {
	    Set<Set<Object>> ret = new HashSet<Set<Object>>();
	    
        for (Object obj1 : set1) {
            for (Object obj2 : set2) {
            	Set<Object> set = new HashSet<Object>();
                set.add(obj1);
                set.add(obj2);
                ret.add(set);
            }
        }
	    return ret;
	}
	
	
}


