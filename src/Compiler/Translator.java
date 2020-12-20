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
	private ArrayList<String> vars_id;
	private ArrayList<String> vars_type;
	Scanner scanner;
	boolean EOF;
	boolean error;
	TLPair errorCause;
	TLPair pair;
	private int counter;
	private String typeVal;

	public Translator() {
		this.output = new ArrayList<>();
		this.vars_id = new ArrayList<>();
		this.vars_type = new ArrayList<>();
		this.scanner = null;
		this.pair = null;
		this.EOF = false;
		this.error = false;
		this.errorCause = null;
		this.counter = 1;
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
			while(tempString.charAt(index) == ',') {
				index += 1;
			}
			if (tempString.charAt(index) != ',') {
				index -= 1;
			}
			this.pair = new TLPair(tempString.substring(1,index), tempString.substring(index+2, tempString.length()-1));
			return true;
		}
		return false;
	}

	public boolean translate() {
		nextToken();
		Program();
		if(this.error) {
			return false;
		}
		return true;
	}

	private void Program() {
		if(!this.error) {
			if(pair.getLexeme().equals("") && pair.getToken().equals(""))
			{
				match("");
			}
			else {
				DataType();
				Program();
			}
		}
	}

	private void DataType() {
		if (!this.error)
		{
			String T = Type();
			if(!this.pair.getToken().equalsIgnoreCase(":")) {
				this.error = true;
				System.out.println("Syntax error.");
			}
			if (!nextToken()){
				System.out.println("Syntax error.");
			}
			String id = ID();

			R(T, id);
		}
	}

	private String Type() {
		if(!this.error)
		{
			String getValue = null;
			if(this.pair.getToken().equalsIgnoreCase("INT")) {
				getValue = match("INT");
			}
			else if(this.pair.getToken().equalsIgnoreCase(" 'CHAR'")) {
				getValue = match(" 'CHAR'");
			}
			else {
				this.error = true;
				System.out.println("Syntax error.");
			}
			return getValue;
		}
		return null;
	}

	private String ID() {
		if(!this.error) {
			String getValue;
			getValue = pair.getLexeme();
			match("ID");

			return getValue;
		}
		return null;
	}

	private void R(String T, String id) {
		if(!this.error) {
			if(this.pair.getToken().equals(",") || this.pair.getToken().equals(";")) {
				vars_type.add(T);
				vars_id.add(id);
				A();
				match(";");
				ST();
			}
			else if(this.pair.getToken().equals(" '('")) {
				print("StartOfFunction - " + id);
				match(" '('");
				NoParams();
				match(" ')'");
				match(" '{'");
				ST();
				match(" '}'");
				print("EndOfFunction - " + id);
			}
			else {
				this.error = true;
				System.out.println("Syntax error.");
			}
		}
	}

	private void NoParams() {
		if(!this.error) {
			if(pair.getLexeme().equals("") && pair.getToken().equals(""))
			{
				match("");
			}
			else {
				Params();
			}
		}
	}

	private void Params() {
		if(!this.error) {
			String getValue = Type();
			vars_type.add(getValue);
			if(!this.pair.getToken().equalsIgnoreCase(":")) {
				this.error = true;
				System.out.println("Syntax error.");
			}
			if (!nextToken()){
				System.out.println("Syntax error.");
			}
			getValue = ID();
			vars_id.add(getValue);

			Loop();
		}
	}

	private void Loop() {
		if(!this.error) {
			if(this.pair.getToken().equals(" ,")) {
				match(" ,");
				Params();
			}
			else if(pair.getLexeme().equals("") && pair.getToken().equals(""))
			{
				match("");
			}
		}
	}

	private void ST() {
		if(!this.error) {
			if(States())
			{
				ST();
			}
			else
			{
				;
			}
		}
	}

	private boolean States() {
		if(!this.error) {
			boolean bool = true;
			if(this.pair.getToken().equalsIgnoreCase("INT") || this.pair.getToken().equalsIgnoreCase(" 'CHAR'")) {
				String getValue = Type();
				vars_type.add(getValue);
				if(!this.pair.getToken().equalsIgnoreCase(":")) {
					this.error = true;
					System.out.println("Syntax error.");
				}
				if (!nextToken()){
					System.out.println("Syntax error.");
				}
				getValue = ID();
				vars_id.add(getValue);
				A();
				match(" ';'");
			}
			else if(this.pair.getToken().equalsIgnoreCase("ID")) {
				String id = ID();
				if (vars_id.contains(id)){
					match(" ':='");
					String typeID = vars_type.get(vars_id.indexOf(id));
					String val = Func1();

					if (typeVal.equals(" 'CHAR'") && typeID.equals(" 'CHAR'") || typeVal.equals("INT") && typeID.equals("INT") ){
						match(" ';'");
						print(id + " = " + val);
						this.typeVal = null;
					}
					else{
						System.out.println("\nSemantic error due to incompatible type");
						this.error = true;
						bool = false;
					}
				}
				else{
					System.out.println("\nSemantic error due to use of undeclared variable.");
					this.error = true;
					bool = false;
				}
			}
			else if(this.pair.getToken().equals(" 'WRITE'")) {
				match(" 'WRITE'");
				match(" '('");
				if(this.pair.getToken().equals(" STRING")) {
					String printVal = StringFunc();
					print("WRITE \"" + printVal + "\"");
				}
				else {
					String printVal = Func1();
					print("WRITE " + printVal);
				}
				match(" ')'");
				match(" ';'");
			}
			else if(this.pair.getToken().equals(" READ")) {
				match(" READ");
				match(" '>>'");
				String printVal = ID();
				print("READ " + printVal);
				match(" ';'");
			}
			else if(this.pair.getLexeme().equals("") && this.pair.getToken().equals("")){
				bool = false;
			}
			else if(this.pair.getToken().equalsIgnoreCase(" WHILE")) {
				match(" WHILE");
				match(" '('");
				String rexp = Expression();
				int lineNo = this.output.size();
				print("if " + rexp + " goto " + Integer.toString((++lineNo) + 2));
				print("goto ");
				match(" ')'");
				match(" '{'");
				ST();
				this.output.set(lineNo, "goto " + Integer.toString(this.output.size()+2));
				print("goto " + Integer.toString(lineNo));
				match(" '}'");
			}
			else if(this.pair.getToken().equalsIgnoreCase("IF")) {
				match("IF");
				match(" '('");
				String rexp = Expression();
				int lineNo = this.output.size();
				print("if " + rexp + " goto " + Integer.toString((++lineNo) + 2));
				print("goto ");
				match(" ')'");
				match(" '{'");
				ST();
				this.output.set(lineNo, "goto " + Integer.toString(this.output.size()+1));
				match(" '}'");
				ElseCond();
			}
			else if(this.pair.getToken().equalsIgnoreCase("RET")) {
				match("RET");
				String printVal = ID();
				print("return " + printVal + "");
				match(";");
				vars_id.add(printVal + "(RET)");
				vars_type.add(vars_type.get(vars_id.indexOf(printVal)));
			}
			else {
				bool = false;
			}
			return bool;
		}
		return false;
	}

	private boolean A() {
		if(!this.error) {
			if(this.pair.getToken().equals(" ,")) {
				match(" ,");
				String getValue = ID();
				vars_id.add(getValue);
				vars_type.add(vars_type.get(vars_type.size()-1));
				A();
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	private String StringFunc() {
		if(!this.error) {
			String getValue = pair.getLexeme();

			match(" STRING");

			return getValue;
		}
		return null;
	}

	private String ExpressionType() {
		if(!this.error) {
			String operand1 = Func2();

			String operand2= ExpressionType2();

			if (operand2 == null){
				return operand1;
			}
			else {
				char sign = operand2.charAt(2);
				operand2 = operand2.substring(4);

				String type1;
				String type2;

				int temp;

				if(!operand1.startsWith("Var")){
					try{
						temp = Integer.parseInt(operand1);
						type1 = "INT";
					}
					catch(NumberFormatException e){
						type1 = vars_type.get(vars_id.indexOf(operand1));
					}
				}
				else{
					type1 = "INT";
				}

				if(!operand2.startsWith("Var")){
					try{
						temp = Integer.parseInt(operand2.replaceAll("\\s+",""));
						type2 = "INT";
					}
					catch(NumberFormatException e){
						type2 = vars_type.get(vars_id.indexOf(operand2));
					}
				}
				else{
					type2 = "INT";
				}

				if (type1.equalsIgnoreCase("INT") && type2.equalsIgnoreCase("INT")){
					print("Var"+ Integer.toString(this.counter) + " = " + operand1 + " " + sign + " " + operand2);

					vars_id.add("Var"+ Integer.toString(this.counter));
					vars_type.add("INT");

					return "Var" + Integer.toString(this.counter++);
				}
				else{
					System.out.println("\nSemantic error due to incompatible type");
					this.error = true;
					return null;
				}
			}
		}
		return null;
	}

	private String ExpressionType2() {
		if(!this.error) {
			if(this.pair.getToken().equals(" '+'") || this.pair.getToken().equals(" '-'")) {
				String getValue = match(this.pair.getToken());

				String operand1 = Func2();

				String operand2= ExpressionType2();

				if (operand2 == null){
					return getValue + operand1;
				}
				else {
					char sign = operand2.charAt(0);
					operand2 = operand2.substring(1);

					String type1;
					String type2;

					int temp;

					if(!operand1.startsWith("Var")){
						try{
							temp = Integer.parseInt(operand1);
							type1 = "INT";
						}
						catch(NumberFormatException e){
							type1 = vars_type.get(vars_id.indexOf(operand1));
						}
					}
					else{
						type1 = "INT";
					}

					if(!operand2.startsWith("Var")){
						try{
							temp = Integer.parseInt(operand2);
							type2 = "INT";
						}
						catch(NumberFormatException e){
							type2 = vars_type.get(vars_id.indexOf(operand2));
						}
					}
					else{
						type2 = "INT";
					}

					if (type1.equalsIgnoreCase("INT") && type2.equalsIgnoreCase("INT")){
						print("Var"+ Integer.toString(this.counter) + " = " + operand1 + " " + sign + " " + operand2);

						vars_id.add("Var"+ Integer.toString(this.counter));
						vars_type.add("INT");

						return getValue + "Var" + Integer.toString(this.counter++);
					}
					else{
						System.out.println("\nSemantic error due to incompatible type");
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

	private String Expression() {
		if(!this.error) {
			String val1 = null;

			String val2 = null;

			if(this.pair.getToken().equals("ID")) {
				val1 = ID();
			}
			else if(this.pair.getToken().equals("NUM")) {
				val1 = pair.getLexeme();

				match("NUM");
			}

			String op = RO();

			if(this.pair.getToken().equals("ID")) {
				val2 = pair.getLexeme();

				match("ID");
			}
			else if(this.pair.getToken().equals("NUM")) {
				val2 = pair.getLexeme();

				match("NUM");
			}
			return val1 + " " + op + " " + val2;
		}
		return null;
	}

	private String Func1() {
		if(!this.error) {
			if(this.pair.getToken().equals(" 'LC'")) {
				String getValue = pair.getLexeme();
				this.typeVal = " 'CHAR'";
				match(" 'LC'");
				return getValue;
			}
			else if(this.pair.getToken().equals("ID") || this.pair.getToken().equals("NUM") || this.pair.getToken().equals("(")) {
				String getValue = ExpressionType();
				return getValue;
			}
		}
		return null;
	}

	private String Func2() {
		if(!this.error) {
			String operand1 = Func4();

			String operand2= Func3();

			if (operand2 == null){
				return operand1;
			}
			else {
				char sign = operand2.charAt(0);
				operand2 = operand2.substring(1);

				String type1;
				String type2;

				int temp;

				if(!operand1.startsWith("Var")){
					try{
						temp = Integer.parseInt(operand1);
						type1 = "INT";
					}
					catch(NumberFormatException e){
						type1 = vars_type.get(vars_id.indexOf(operand1));
					}
				}
				else{
					type1 = "INT";
				}

				if(!operand2.startsWith("Var")){
					try{
						temp = Integer.parseInt(operand2);
						type2 = "INT";
					}
					catch(NumberFormatException e){
						type2 = vars_type.get(vars_id.indexOf(operand2));
					}
				}
				else{
					type2 = "INT";
				}

				if (type1.equalsIgnoreCase("INT") && type2.equalsIgnoreCase("INT")){
					print("Var"+ Integer.toString(this.counter) + " = " + operand1 + " " + sign + " " + operand2);

					vars_id.add("Var"+ Integer.toString(this.counter));
					vars_type.add("INT");

					return "Var" + Integer.toString(this.counter++);
				}
				else{
					System.out.println("\nSemantic error due to incompatible type");
					this.error = true;
					return null;
				}
			}
		}
		return null;
	}

	private String Func3() {
		if(!this.error) {
			if(this.pair.getToken().equals(" '*'") || this.pair.getToken().equals(" '/'")) {
				String getValue = match(this.pair.getToken());
				String operand1 = Func4();
				String operand2= Func3();
				if (operand2 == null){
					return getValue + operand1;
				}
				else {
					char sign = operand2.charAt(0);
					operand2 = operand2.substring(1);
					String type1;
					String type2;
					int temp;
					if(!operand1.startsWith("Var")){
						try{
							temp = Integer.parseInt(operand1);
							type1 = "INT";
						}
						catch(NumberFormatException e){
							type1 = vars_type.get(vars_id.indexOf(operand1));
						}
					}
					else{
						type1 = "INT";
					}

					if(!operand2.startsWith("Var")){
						try{
							temp = Integer.parseInt(operand2);
							type2 = "INT";
						}
						catch(NumberFormatException e){
							type2 = vars_type.get(vars_id.indexOf(operand2));
						}
					}
					else{
						type2 = "INT";
					}

					if (type1.equalsIgnoreCase("INT") && type2.equalsIgnoreCase("INT")){
						print("Var"+ Integer.toString(this.counter) + " = " + operand1 + " " + sign + " " + operand2);

						vars_id.add("Var"+ Integer.toString(this.counter));
						vars_type.add("INT");

						return getValue + "Var" + Integer.toString(this.counter++);
					}
					else{
						System.out.println("\nSemantic error due to incompatible type");
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

	private String Func4() {
		if(!this.error) {
			if(this.pair.getToken().equals("ID")) {
				String id = ID();

				if(vars_id.contains(id)){
					this.typeVal = vars_type.get(vars_id.indexOf(id));

					return id;
				}
				else{
					System.out.println("\nSemantic error due to use of undeclared variable.");
					this.error = true;
					return null;
				}

			}
			else if(this.pair.getToken().equals("NUM")) {
				String getValue = pair.getLexeme();

				match("NUM");

				this.typeVal = "INT";

				return getValue;
			}
			else if(this.pair.getToken().equals("(")) {
				match("(");

				String getValue = ExpressionType();

				match(")");

				return getValue;
			}
		}
		return null;
	}

	private String RO() {
		if(!this.error) {
			String getValue = pair.getLexeme();

			match(" RO");

			return getValue;
		}
		return null;
	}

	private boolean IfCond() {
		if(!this.error) {
			if(this.pair.getToken().equalsIgnoreCase(" 'IF'")) {
				match(" 'IF'");
				match("(");
				Expression();
				match(")");
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	private boolean ElseCond() {
		if(!this.error) {
			if(this.pair.getToken().equalsIgnoreCase(" 'ELSE'")) {
				match(" 'ELSE;");
				IfCond();
				match("{");
				ST();
				match("}");
				ElseCond();
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
			String getValue = pair.getToken();
			if (!nextToken())
			{
				this.EOF = true;
				pair = new TLPair("", "");
			}
			return getValue;
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

		System.out.println("\nThree address code = \"tac.txt\".");

		fileWriter = new FileWriter("translator-symboltable.txt");
		printWriter = new PrintWriter(fileWriter);
		printWriter.println("Symbol Table");
		printWriter.println("=================================================");
		printWriter.println("Name" + "\t\t" + "Datatype" + "\t" + "Relative Address");
		printWriter.println("=================================================");

		int temp = 0;
		for(int i = 0; i < this.vars_id.size(); i++) {

			if(i > 0 && vars_type.get(i).equals("INT")) {
				temp += 4;
			}
			else if(i > 0 && vars_type.get(i).equals(" 'CHAR'")) {
				temp ++;
			}
			printWriter.println(vars_id.get(i) + "\t\t" + vars_type.get(i) + "\t\t" + Integer.toString(temp));
		}
		printWriter.close();

	}
}