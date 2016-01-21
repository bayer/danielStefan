import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import com.pengyifan.commons.collections.tree.TreeNode;

import parser.*;



public class ParseExecuter {
	private String lksString = "5\n--\n0 1 0 0 0 \n0 0 1 0 1 \n0 0 0 1 0 \n0 1 0 0 0 \n0 0 0 0 0 \n--\n0 -> {j < i}\n1 -> {i == j}\n2 -> {i < j}\n3 -> {!(i==j)}\n4 -> {j > i}\n--\n0 -> 0\n1 -> 0\n2 -> 1\n3 -> 4\n4 -> 4\n";

	public ParseExecuter(String s) throws Exception{
			LTLTree ltlTree = parseLTL(s);
	        
			System.out.println(TreeNode.PRETTY_PRINT.apply(ltlTree.root));
 
	        TableauGenerator t = new TableauGenerator(ltlTree.root);
	        
	        System.out.println(t);
	        
	        LKSGenerator lks = parseLKS(lksString);
			        
	        System.out.println(lks);
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

        ParseTree ltlTree = ltlParser.formula(); // begin parsing at formula rule
        
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
}

