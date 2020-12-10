package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Translator {
	private ArrayList<String> output;
	private ArrayList<String> varIDs;
	private ArrayList<String> varTypes;
	Scanner scanner;
	boolean EOF;
	boolean error;
	TLPair errorCause;
	TLPair pair;
	
	private int tempCount;
	
	private String typeVal;

	public Translator() {
		this.output = new ArrayList<>();
		this.varIDs = new ArrayList<>();
		this.varTypes = new ArrayList<>();		

		this.scanner = null;
		this.pair = null;
		this.EOF = false;
		this.error = false;
		this.errorCause = null;
		
		this.tempCount = 1;
		this.typeVal = null;
	}
	
	public boolean openFile(String fileName) {
		try {
			this.scanner = new Scanner(new File(fileName));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean nextToken() {
		if(scanner.hasNextLine())
		{
			String tempString = scanner.nextLine();
			int index = tempString.indexOf(",") + 1;
			
			while(tempString.charAt(index) == ',')
			{
				index += 1;
			}
			
			if (tempString.charAt(index) != ',')
			{
				index -= 1;
			}
			
			this.pair = new TLPair(tempString.substring(1,index), tempString.substring(index+2, tempString.length()-1));		
			return true;
		}
		return false;
	}
	
	public boolean translate() {
		nextToken();
				
		P();
		
		if(this.error) {
			return false;
		}
		
		return true;
	}

	private void P() {
		if(!this.error) {
			if(pair.getLexeme().equals("") && pair.getToken().equals(""))
			{
				match("");
			}
			else {
				D();
				P();
			}
		}
	}
	
	private void D() {
		if (!this.error)
		{			
			String T = T();
			
			String id = ID();
						
			R(T, id);
		}
	}
	
	private String T() {
		if(!this.error)
		{			
			String retval = null;
			
			if(this.pair.getToken().equalsIgnoreCase("int")) {
				
				retval = match("INT");
			}
			else if(this.pair.getToken().equalsIgnoreCase("char")) {

				retval = match("CHAR");
			}
			else {
				this.error = true;
				System.out.println("Syntax error.");
			}
			return retval;
		}
		return null;
	}
	
	private String ID() {
		if(!this.error) {
			String retval = pair.getLexeme();
			
			match("ID");
			
			return retval;
		}
		return null;
	}
	
	private void R(String T, String id) {
		if(!this.error) {			
			if(this.pair.getToken().equals(",") || this.pair.getToken().equals(";")) {
				varTypes.add(T);
				varIDs.add(id);
				
				AV();
				
				match(";");

				SL();
			}
			else if(this.pair.getToken().equals("(")) {
				print("FuncStart - " + id);
				
				match("(");
				
				NPL();

				match(")");
				
				match("{");
								
				SL();
				
				match("}");

				print("FuncEnd - " + id);
			}
			else {
				this.error = true;
				System.out.println("Syntax error.");
			}
		}
	}
	
	private void NPL() {
		if(!this.error) {
			if(pair.getLexeme().equals("") && pair.getToken().equals(""))
			{
				match("");
			}
			else {
				PL();
			}
		}
	}
	
	private void PL() {
		if(!this.error) {
			String retval = T();
			varTypes.add(retval);
			
			retval = ID();
			varIDs.add(retval);
			
			OPT();
		}
	}
	
	private void OPT() {
		if(!this.error) {
			if(this.pair.getToken().equals(",")) {
				match(",");
				
				PL();
			}
			else if(pair.getLexeme().equals("") && pair.getToken().equals(""))
			{
				match("");
			}
			else {
				;
			}
		}
	}
	
	private void SL() {
		if(!this.error) {
			if(S())
			{
				SL();
			}
			else
			{
				;
			}
		}
	}

	private boolean S() {
		if(!this.error) {
			boolean bool = true;
			
			if(this.pair.getToken().equalsIgnoreCase("int") || this.pair.getToken().equalsIgnoreCase("char")) {
				String retval = T();
				varTypes.add(retval);
				
				retval = ID();
				varIDs.add(retval);
				
				AV();
				
				match(";");
			}
			else if(this.pair.getToken().equalsIgnoreCase("ID")) {
				String id = ID();

				// Reference of semantic errors
				// https://www.javatpoint.com/semantic-error

				// Semantic error check
				// Checking if variable is declared or not

				if (varIDs.contains(id)){
					match("<-");
				
					String typeID = varTypes.get(varIDs.indexOf(id));

					// TODO: Check if assigned value has same type as of variable

					String val = VAL();

					// Dealing with literal constant, id, numerical constant
					// and expression
					if (typeVal.equals("CHAR") && typeID.equals("CHAR") || typeVal.equals("INT") && typeID.equals("INT") ){
						match(";");
	
						print(id + " = " + val);

						this.typeVal = null;
					}
					else{
						System.out.println("\nSemantic error: Type incompatibility.");
						this.error = true;
						bool = false;
					}
				}
				else{
					System.out.println("\nSemantic error: Undeclared variable used.");
					this.error = true;
					bool = false;
				}
			}
			else if(this.pair.getToken().equals("jOut")) {
				match("jOut");
				
				match("(");
				
				if(this.pair.getToken().equals("STR")) {
					String printVal = STR();
					print("out \"" + printVal + "\"");
				}
				else {
					String printVal = VAL();
					print("out " + printVal);
				}
				
				match(")");
				
				match(";");
			}
			else if(this.pair.getToken().equals("jIn")) {
				match("jIn");
				
				match("(");
				
				String printVal = ID();
				print("in " + printVal);
				
				match(")");
				
				match(";");
			}
			else if(this.pair.getLexeme().equals("") && this.pair.getToken().equals(""))
			{
				bool = false;
			}
			else if(this.pair.getToken().equalsIgnoreCase("WHILE")) {
				match("WHILE");
				
				match("(");

				String rexp = REXP();
				
				int lineNo = this.output.size();
				
				print("if " + rexp + " goto " + Integer.toString((++lineNo) + 2));
				
				print("goto ");
				
				match(")");

				match("{");
				
				SL();
				
				this.output.set(lineNo, "goto " + Integer.toString(this.output.size()+2));
				
				print("goto " + Integer.toString(lineNo));
				
				match("}");
			}
			else if(this.pair.getToken().equalsIgnoreCase("IF")) {
				match("IF");
				
				match("(");
				
				// this.tempCount = 1;
				
				String rexp = REXP();
				
				int lineNo = this.output.size();
				
				print("if " + rexp + " goto " + Integer.toString((++lineNo) + 2));
				
				print("goto ");
				
				// this.tempCount = 1;
				
				match(")");
				
				match("{");
				
				SL();
				
				this.output.set(lineNo, "goto " + Integer.toString(this.output.size()+1));
				
				match("}");
				
				IE();
			}
			else if(this.pair.getToken().equalsIgnoreCase("RETURN")) {
				match("RETURN");
								
				String printVal = ID();
				print("return " + printVal + "");
				
				match(";");
				
				varIDs.add(printVal + "(RET)");
				varTypes.add(varTypes.get(varIDs.indexOf(printVal)));
			}
			else {
				bool = false;
			}

			return bool;
		}
		return false;
	}
	
	private boolean AV() {
		if(!this.error) {
			
			if(this.pair.getToken().equals(",")) {
				match(",");
				
				String retval = ID();
				varIDs.add(retval);
				varTypes.add(varTypes.get(varTypes.size()-1));
				
				AV();
				
				return true;
			}
			
			else {
				return false;
			}
		}
		return false;
	}
	
	private String STR() {
		if(!this.error) {
			String retval = pair.getLexeme();
			
			match("STR");
			
			return retval;
		}
		return null;
	}
		
	private String VAL() {
		if(!this.error) {
			if(this.pair.getToken().equals("LC")) {
				String retval = pair.getLexeme();
				
				this.typeVal = "CHAR";

				match("LC");
				
				return retval;
			}
			else if(this.pair.getToken().equals("ID") || this.pair.getToken().equals("NC") || this.pair.getToken().equals("(")) {
				// this.tempCount = 1;
				
				String retval = EXP();
				
				// this.tempCount = 1;
				
				return retval;
			}
		}
		return null;
	}
	
	private String EXP() {
		if(!this.error) {
			String op1 = VAL2();
			
			String op2= EXP2();
			
			if (op2 == null){
				return op1;
			}
			else {
				char sign = op2.charAt(0);
				op2 = op2.substring(1);

				// Semantic error check
				// Check if type is compatible or not

				String type1;
				String type2;

				int temp;

				if(!op1.startsWith("tempVar")){
					try{
						temp = Integer.parseInt(op1);
						type1 = "int";
					}
					catch(NumberFormatException e){
						type1 = varTypes.get(varIDs.indexOf(op1));
					}
				}
				else{
					type1 = "int";
				}

				if(!op2.startsWith("tempVar")){
					try{
						temp = Integer.parseInt(op2);
						type2 = "int";
					}
					catch(NumberFormatException e){
						type2 = varTypes.get(varIDs.indexOf(op2));
					}
				}
				else{
					type2 = "int";
				}

				if (type1.equalsIgnoreCase("int") && type2.equalsIgnoreCase("int")){
					print("tempVar"+ Integer.toString(this.tempCount) + " = " + op1 + " " + sign + " " + op2);

					varIDs.add("tempVar"+ Integer.toString(this.tempCount));
					varTypes.add("INT");
					
					return "tempVar" + Integer.toString(this.tempCount++);
				}
				else{
					System.out.println("\nSemantic error: Type incompatibility.");
					this.error = true;
					return null;
				}
			}
		}
		return null;
	}
	
	private String EXP2() {
		if(!this.error) {
			if(this.pair.getToken().equals("+") || this.pair.getToken().equals("-")) {
				String retval = match(this.pair.getToken());
				
				String op1 = VAL2();
				
				String op2= EXP2();
				
				if (op2 == null){
					return retval + op1;
				}
				else {
					char sign = op2.charAt(0);
					op2 = op2.substring(1);

					// Semantic error check
					// Check if type is compatible or not

					String type1;
					String type2;

					int temp;

					if(!op1.startsWith("tempVar")){
						try{
							temp = Integer.parseInt(op1);
							type1 = "int";
						}
						catch(NumberFormatException e){
							type1 = varTypes.get(varIDs.indexOf(op1));
						}
					}
					else{
						type1 = "int";
					}
	
					if(!op2.startsWith("tempVar")){
						try{
							temp = Integer.parseInt(op2);
							type2 = "int";
						}
						catch(NumberFormatException e){
							type2 = varTypes.get(varIDs.indexOf(op2));
						}
					}
					else{
						type2 = "int";
					}

					if (type1.equalsIgnoreCase("int") && type2.equalsIgnoreCase("int")){
						print("tempVar"+ Integer.toString(this.tempCount) + " = " + op1 + " " + sign + " " + op2);

						varIDs.add("tempVar"+ Integer.toString(this.tempCount));
						varTypes.add("INT");

						return retval + "tempVar" + Integer.toString(this.tempCount++);
					}
					else{
						System.out.println("\nSemantic error: Type incompatibility.");
						this.error = true;
						return null;
					}
				}
			}
			else {
				return null;
			}
		}
		return null;
	}
	
	private String REXP() {
		if(!this.error) {
			String val1 = null;
			
			String val2 = null;
			
			if(this.pair.getToken().equals("ID")) {
				val1 = ID();
			}
			else if(this.pair.getToken().equals("NC")) {
				val1 = pair.getLexeme();
				
				match("NC");
			}
			
			String op = RO();
			
			if(this.pair.getToken().equals("ID")) {
				val2 = pair.getLexeme();
				
				match("ID");
			}
			else if(this.pair.getToken().equals("NC")) {
				val2 = pair.getLexeme();
				
				match("NC");
			}
			return val1 + " " + op + " " + val2;
		}
		return null;
	}
	
	private String VAL2() {
		if(!this.error) {
			String op1 = VAL4();
			
			String op2= VAL3();
			
			if (op2 == null){
				return op1;
			}
			else {
				char sign = op2.charAt(0);
				op2 = op2.substring(1);

				// Semantic error check
				// Check if type is compatible or not

				String type1;
				String type2;

				int temp;

				if(!op1.startsWith("tempVar")){
					try{
						temp = Integer.parseInt(op1);
						type1 = "int";
					}
					catch(NumberFormatException e){
						type1 = varTypes.get(varIDs.indexOf(op1));
					}
				}
				else{
					type1 = "int";
				}

				if(!op2.startsWith("tempVar")){
					try{
						temp = Integer.parseInt(op2);
						type2 = "int";
					}
					catch(NumberFormatException e){
						type2 = varTypes.get(varIDs.indexOf(op2));
					}
				}
				else{
					type2 = "int";
				}

				if (type1.equalsIgnoreCase("int") && type2.equalsIgnoreCase("int")){
					print("tempVar"+ Integer.toString(this.tempCount) + " = " + op1 + " " + sign + " " + op2);

					varIDs.add("tempVar"+ Integer.toString(this.tempCount));
					varTypes.add("INT");

					return "tempVar" + Integer.toString(this.tempCount++);
				}
				else{
					System.out.println("\nSemantic error: Type incompatibility.");
					this.error = true;
					return null;
				}
			}
		}
		return null;
	}
	
	private String VAL3() {
		if(!this.error) {
			if(this.pair.getToken().equals("*") || this.pair.getToken().equals("/")) {
				String retval = match(this.pair.getToken());
				
				String op1 = VAL4();
				
				String op2= VAL3();
				
				if (op2 == null){
					return retval + op1;
				}
				else {
					char sign = op2.charAt(0);
					op2 = op2.substring(1);

					// Semantic error check
					// Check if type is compatible or not

					String type1;
					String type2;
	
					int temp;

					if(!op1.startsWith("tempVar")){
						try{
							temp = Integer.parseInt(op1);
							type1 = "int";
						}
						catch(NumberFormatException e){
							type1 = varTypes.get(varIDs.indexOf(op1));
						}
					}
					else{
						type1 = "int";
					}

					if(!op2.startsWith("tempVar")){
						try{
							temp = Integer.parseInt(op2);
							type2 = "int";
						}
						catch(NumberFormatException e){
							type2 = varTypes.get(varIDs.indexOf(op2));
						}
					}
					else{
						type2 = "int";
					}
	
					if (type1.equalsIgnoreCase("int") && type2.equalsIgnoreCase("int")){
						print("tempVar"+ Integer.toString(this.tempCount) + " = " + op1 + " " + sign + " " + op2);

						varIDs.add("tempVar"+ Integer.toString(this.tempCount));
						varTypes.add("INT");

						return retval + "tempVar" + Integer.toString(this.tempCount++);
					}
					else{
						System.out.println("\nSemantic error: Type incompatibility.");
						this.error = true;
						return null;
					}
				}
			}
			else {
				return null;
			}
		}
		return null;
	}

	private String VAL4() {
		if(!this.error) {
			if(this.pair.getToken().equals("ID")) {
				String id = ID();
				
				// Semantic error check
				// Checking if variable is declared or not
				if(varIDs.contains(id)){
					this.typeVal = varTypes.get(varIDs.indexOf(id));

					return id;
				}
				else{
					System.out.println("\nSemantic error: Undeclared variable used.");
					this.error = true;
					return null;
				}
				
			}
			else if(this.pair.getToken().equals("NC")) {
				String retval = pair.getLexeme();
				
				match("NC");
				
				this.typeVal = "INT";

				return retval;
			}
			else if(this.pair.getToken().equals("(")) {
				match("(");
				
				String retval = EXP();
				
				match(")");
				
				return retval;
			}
		}
		return null;
	}

	private String RO() {
		if(!this.error) {
			String retval = pair.getLexeme();
			
			match("RO");
			
			if (retval.equals("LT")){
				retval = "<";
			}
			else if (retval.equals("LE")){
				retval = "<=";
			}
			else if (retval.equals("GT")){
				retval = ">";
			}
			else if (retval.equals("GTE")){
				retval = ">=";
			}
			else if (retval.equals("EQ")){
				retval = "==";
			}
			else if (retval.equals("NE")){
				retval = "!=";
			}
			
			return retval;
		}
		return null;
	}
	
	private boolean optC() {
		if(!this.error) {
			if(this.pair.getToken().equalsIgnoreCase("IF")) {
				match("IF");
				
				match("(");

				REXP();
				
				match(")");
				
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	private boolean IE() {
		if(!this.error) {
			if(this.pair.getToken().equalsIgnoreCase("ELSE")) {
				match("ELSE");
				
				optC();
				
				match("{");
				
				SL();
				
				match("}");
				
				IE();
				
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	private String match(String token) {
		if (pair.getToken().equals(token)) {
			String retval = pair.getToken();
			if (!nextToken())
			{
				this.EOF = true;
				pair = new TLPair("", "");
			}
			return retval;
		}
		
		this.errorCause = new TLPair(pair.getToken(), pair.getLexeme());
		this.error = true;
		return "Syntax Error.";
	}
	
	private void print(String text) {
		output.add(text);
	}
	
	public void output() throws IOException {
		FileWriter fileWriter = new FileWriter("tac.txt");
	    PrintWriter printWriter = new PrintWriter(fileWriter);
		for (int i = 0; i < this.output.size(); i++)
		{
			printWriter.println(Integer.toString(i+1) + "\t" + output.get(i));
		}
		printWriter.close();
		
		System.out.println("\nThree address code for the input code file written to file \"tac.txt\".");
		
		fileWriter = new FileWriter("translator-symboltable.txt");
	    printWriter = new PrintWriter(fileWriter);
	    printWriter.println("Symbol Table");
	    printWriter.println("------------------------------------------------");
	    printWriter.println("Name" + "\t\t" + "Datatype" + "\t" + "Relative Address");
	    printWriter.println("------------------------------------------------");
		
	    int temp = 0;
	    for(int i = 0; i < this.varIDs.size(); i++) {
			
			if(i > 0 && varTypes.get(i).equals("INT")) {
				temp += 4;
			}
			else if(i > 0 && varTypes.get(i).equals("CHAR")) {
				temp ++;
			}
			printWriter.println(varIDs.get(i) + "\t\t" + varTypes.get(i) + "\t\t" + Integer.toString(temp));
		}
	    printWriter.close();
	    
	    System.out.println("Symbol-table for the input code file written to file \"translator-symboltable.txt\".");
	}
}

