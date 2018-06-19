package test;
import java.awt.Point;
import java.util.*;

public class Calculator {
	
	public Calculator() {}
	
	//checks whether the parentheses are valid
	public boolean checkP(String input){
		Stack<Character> s = new Stack<Character>();
		for(int i = 0; i < input.length(); i++) {
			if(input.charAt(i) == '(') {
				s.push('(');
			}
			if(input.charAt(i) == ')') {
				if(s.isEmpty() == true) {
					return false;
				}
				s.pop();
			}
		}
		if(s.isEmpty() == true) {
			return true;
		}else {
			return false;
		}
	}
	
	//evaluates a simple expression without parentheses
	public double evaluate(String in) {
		try {
			//try converting the input to a double
			//if succeed, then it means that the input is a single number
			return Double.parseDouble(in);
		}catch(NumberFormatException e) {
			
			//stores the numbers
			ArrayList<Double> numbers = new ArrayList<Double>();
			//stores the operators
			ArrayList<Character> operators = new ArrayList<Character>();
			//stores the index of primary operators in the operator list
			ArrayList<Integer> indexOfPri = new ArrayList<Integer>();
			//stores the index of the most primary operators in the operator list
			ArrayList<Integer> indexOfMostPrimary = new ArrayList<Integer>();
			
			identify(in, indexOfMostPrimary, numbers, operators, indexOfPri);	
			evaluateMostPrimary(indexOfMostPrimary, numbers, operators);
			evaluatePrimary(indexOfPri, numbers, operators);
			evaluateFundamental(numbers, operators);
			
			return numbers.get(0);
		}
	}
	
	//evaluates the contents of parentheses
	public String simplify(String in) {
		String copy = new String();
		Point p = innermost(in);
		String temp = in.substring(p.x+1, p.y);
		System.out.println(in.substring(p.x, p.y+1));
		copy = in.replace(in.substring(p.x, p.y+1), evaluate(temp)+"");
		System.out.println(String.format("%.12f",evaluate(temp)));
		return copy;
	}
	
	//helper function to check whether a character is a number
	private boolean isNum(char c) {
		//here, . is a part of a number
		if(c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '0' || c == '.') {
			return true;
		}else {
			return false;
		}
	}
	
	//checks if the equation contains ()
	private boolean hasParen(String in) {
		for(int i = 0; i < in.length(); i++) {
			if(in.charAt(i) == '(') {
				return true;
			}
		}
		return false;
	}

	//evaluates exponent, sin, cos, tan, etc.
	private void evaluateMostPrimary(ArrayList<Integer> indexOfmostPrimary, ArrayList<Double> numbers, ArrayList<Character> operators) {
		int numsubtract = 0;
		//takes cares of exponent
		for(int i = 0; i < indexOfmostPrimary.size(); i++) {
			char operation = operators.get(indexOfmostPrimary.get(i)-numsubtract);
			double newNum = 0;
			double left = numbers.get(indexOfmostPrimary.get(i)-numsubtract);
			double right = numbers.get(indexOfmostPrimary.get(i)-numsubtract+1);
			if(operation == '^') {				//in case of exponent
				left = Math.pow(left, right);
				newNum = left;
				if(indexOfmostPrimary.get(i)-numsubtract - 1 >=0 && operators.get(indexOfmostPrimary.get(i)-numsubtract - 1) == 'E') {
					System.out.println("called");
				}
			}else {}
			
			int c = indexOfmostPrimary.get(i)-numsubtract;
			operators.remove(c);
			numbers.set(indexOfmostPrimary.get(i)-numsubtract, newNum);
			numbers.remove(indexOfmostPrimary.get(i)-numsubtract+1);
			indexOfmostPrimary.remove(i);
			numsubtract++;
			i--;
		}
	}
	
	//evaluates multiplication, division, scientific notation
	private void evaluatePrimary (ArrayList<Integer> indexOfPri, ArrayList<Double> numbers, ArrayList<Character> operators){
		//how much to modify the values of indexOfPri because of the shifting indices of operators
		int numsubtract = 0;
		for(int i = 0; i < indexOfPri.size(); i++) {
			char operation = operators.get(indexOfPri.get(i)-numsubtract);
			double newNum = 0;
			double left = numbers.get(indexOfPri.get(i)-numsubtract);
			double right = numbers.get(indexOfPri.get(i)-numsubtract+1);
			if(operation == '*') {				//in case of multiplication
				newNum = left*right;
			}else if(operation == '/'){			//in case of division
				newNum = left/right;
			}else if(operation == 'E') {		//in case of scientific notation
				if(right < 0) {
					for(int j = 0; j < Math.abs((int)right); j++){
						left = left/10;
					}
					newNum = left;
				}else {
					for(int j = 0; j < Math.abs((int)right); j++){
						left = left*10;
					}
					newNum = left;
				}
			}else if(operation == '^') {
				left = Math.pow(left, right);
				newNum = left;
			}
			int c = indexOfPri.get(i)-numsubtract;
			operators.remove(c);
			numbers.set(indexOfPri.get(i)-numsubtract, newNum);
			numbers.remove(indexOfPri.get(i)-numsubtract+1);
			indexOfPri.remove(i);
			numsubtract++;
			i--;
		}
	}
	
	//evaluates fundamental arithmetics 
	private void evaluateFundamental(ArrayList<Double> numbers, ArrayList<Character> operators) {
		for(int i = 0; i < operators.size(); i++) {
			char operation = operators.get(i);
			double left = numbers.get(i);
			double right = numbers.get(i+1);
			double newNum = 0;
			if(operation == '+') {
				newNum = left + right;
			}else {
				newNum = left-right;
			}
			numbers.set(i, newNum);
			numbers.remove(i+1);
			operators.remove(i);
			i--;
		}
	}
	
	//identifies numbers and operators
	private void identify(String in, ArrayList<Integer> indexOfMostPrimary, ArrayList<Double> numbers, ArrayList<Character> operators, ArrayList<Integer> indexOfPri) {
		//since number is identified as whatever that is between two non-numbers, we need to add a non-number to the end of input to make the algorithm work
		in+=" ";
		int begin  = 0;
		for(int i = 0; i < in.length(); i++) {
			//if the character is not a number
			if(!isNum(in.charAt(i))) {
				if(begin == i) {
					continue;
				}
				numbers.add(Double.parseDouble(in.substring(begin, i)));
				begin = i+1;
				
				operators.add(in.charAt(i));
				if(in.charAt(i) == '^') {
					indexOfMostPrimary.add(operators.size()-1);
				}
				if(in.charAt(i) == '/' || in.charAt(i) == '*' || in.charAt(i) == 'E') {
					indexOfPri.add(operators.size()-1);
				}
			}
		}
		operators.remove(operators.size()-1);
	}
	
	//finds the innermost ()
	public Point innermost(String in) {
		Point p = new Point(-1,-1);
		for(int i = 0; i < in.length(); i++) {
			if(in.charAt(i) == '(') {
				p.x = i;
			}
			if(in.charAt(i) == ')') {
				p.y = i;
				return p;
			}
		}
		return p;
	}

	//does the calculation
	public double calculate(String in) {
		if(checkP(in) == false) {
			System.err.println("invalid parentheses");
			return 0;
		}
		if(hasParen(in) == false) {
			return evaluate(in);
		}
		String copy = simplify(in);
		while(hasParen(copy) == true) {
			copy = simplify(copy);
		}
		return evaluate(copy);
	}
}
