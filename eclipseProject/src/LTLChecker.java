import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import rwth.i2.ltl2ba4j.formula.IFormula;
import rwth.i2.ltl2ba4j.formula.IFormulaFactory;
import rwth.i2.ltl2ba4j.formula.impl.FormulaFactory;
import rwth.i2.ltl2ba4j.model.ITransition;

//import net.sf.javabdd.BDDFactory;
public class LTLChecker {
	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter String\n");
        String s=null;
		//try {
		//	s = br.readLine();
		//} catch (IOException e1) {
			// TODO Auto-generated catch block
		//	e1.printStackTrace();
		//}
		s = "A(a UFGx)";
        //s = "XFGx";
		s = "Aa & !b";
		s = "A!(a | d)";
		s = "A((a | !d) U (Fb -> !(c & b)))";
		s = "A !a";
		s = "A((a|!b) U !c)";
		
		try {
			ParseExecuter pe = new ParseExecuter(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		IFormulaFactory factory = new FormulaFactory();
//		IFormula formula = 
//				factory.Until(
//						factory.Or(
//						factory.Proposition("a"), factory.Not(factory.Proposition("b"))), 
//						factory.Not(factory.Proposition("c")))
//                
//              ;
//		
//		for(ITransition t: rwth.i2.ltl2ba4j.LTL2BA4J.formulaToBA(formula)) {
		for(ITransition t: rwth.i2.ltl2ba4j.LTL2BA4J.formulaToBA("[]((a||!b) U !c)")) {
		    System.out.println(t);
		}
		
	}
}
