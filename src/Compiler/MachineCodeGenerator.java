package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MachineCodeGenerator {
    private ArrayList<String[]> output;
    private ArrayList<String> varIDs;
    private ArrayList<String> varTypes;
    private ArrayList<String> varAdds;
    private ArrayList<String> initVals;
    Scanner scanner;

    String[] tacLine;

    private int tempCount;

    boolean error;

    //	Operator	Opcodes
    //	-------------------
    //	=			1
    //	+			2
    //	-			3
    //	/			4
    //	*			5
    //	>			6
    //	>=			7
    //	<			8
    //	<=			9
    //	==			10
    //	!=			11
    //	if			12
    //	out			13
    //	in			14
    //	goto		15



    public MachineCodeGenerator() {
        this.output = new ArrayList<>();
        this.varIDs = new ArrayList<>();
        this.varTypes = new ArrayList<>();
        this.varAdds = new ArrayList<>();
        this.initVals = new ArrayList<>();

        this.scanner = null;
        this.tacLine = null;

        this.error = false;

        this.tempCount = 1;
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

    public boolean readSymbolTable(String fileName) {
        try {
            this.scanner = new Scanner(new File(fileName));

            String temp;
            String[] tempArr;
            scanner.nextLine();
            scanner.nextLine();
            scanner.nextLine();
            scanner.nextLine();

            int add = 0;

            while(scanner.hasNextLine()){
                temp = scanner.nextLine();

                tempArr = temp.split("\t");

                if (tempArr[0].startsWith("tempVar")){
                    this.tempCount = Integer.parseInt(tempArr[0].substring(7));
                }

                this.varIDs.add(tempArr[0]);
                this.varTypes.add(tempArr[2]);
                if(tempArr[2].equals("INT")){
                    this.initVals.add("0");
                }
                else{
                    this.initVals.add("null");
                }

                if(this.varAdds.size() == 0){
                    this.varAdds.add(Integer.toString(add));
                }
                else{
                    if (varTypes.get(this.varAdds.size()-1).equals("INT")){
                        add += 4;
                    }
                    else{
                        add += 1;
                    }
                    this.varAdds.add(Integer.toString(add));
                }
            }

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean nextLine() {
        if(scanner.hasNextLine())
        {
            String line = this.scanner.nextLine();

            line = line.substring(line.indexOf("\t")+1);

            this.tacLine = line.split(" ");
            return true;
        }
        else{
            this.tacLine = null;
            return false;
        }
    }

    public boolean generate() {
        nextLine();

        Program();

        if(this.error) {
            return false;
        }

        return true;
    }

    private void Program() {
        if(!this.error) {
            if(this.tacLine == null)
            {
                ;
            }
            else {
                D();
                Program();
            }
        }
    }

    private void D() {
        if (!this.error)
        {
            if(tacLine[0] == "FuncStart"){
                this.error = true;
                this.tacLine = null;
                System.out.println("Error: Machine code can only be generated for simple code, not functions.");
            }
            else{
                ST();
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

            if(tacLine[1].equals("=")) {
                if (tacLine.length == 3){
                    String operator = opcode(tacLine[1]);

                    String operand = tacLine[2];

                    String target = tacLine[0];

                    if (varTypes.get(varIDs.indexOf(target)).equals("INT")){
                        if(lookup(operand) == null){
                            varIDs.add("tempVar" + Integer.toString(++this.tempCount));
                            varTypes.add("INT");
                            if (varTypes.get(this.varAdds.size()-1).equals("INT")){
                                this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+4));
                            }
                            else{
                                this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+1));
                            }
                            this.initVals.add(operand);
                            operand = this.varAdds.get(this.varAdds.size()-1);
                        }
                        else{
                            operand = lookup(operand);
                        }
                    }
                    else{
                        if(lookup(operand) != null)
                        {
                            operand = lookup(operand);
                        }
                    }

                    target = lookup(target);

                    print(operator, operand, target, null);
                }
                else if (tacLine.length == 5){
                    String operator = opcode(tacLine[3]);

                    String op1 = tacLine[2];

                    String op2 = tacLine[4];

                    String target = tacLine[0];

                    if(lookup(op1) == null){
                        varIDs.add("tempVar" + Integer.toString(++this.tempCount));
                        varTypes.add("INT");
                        if (varTypes.get(this.varAdds.size()-1).equals("INT")){
                            this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+4));
                        }
                        else{
                            this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+1));
                        }
                        this.initVals.add(op1);
                        op1 = this.varAdds.get(this.varAdds.size()-1);
                    }
                    else{
                        op1 = lookup(op1);
                    }

                    if(lookup(op2) == null){
                        varIDs.add("tempVar" + Integer.toString(++this.tempCount));
                        varTypes.add("INT");
                        if (varTypes.get(this.varAdds.size()-1).equals("INT")){
                            this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+4));
                        }
                        else{
                            this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+1));
                        }
                        this.initVals.add(op2);
                        op2 = this.varAdds.get(this.varAdds.size()-1);
                    }
                    else{
                        op2 = lookup(op2);
                    }

                    target = lookup(target);

                    print(operator, op1, op2, target);
                }

            }

            else if(tacLine[0].equals("out")) {
                String operator = opcode(tacLine[0]);

                String operand;

                if(lookup(tacLine[1]) == null){
                    operand = "";
                    for (int i = 1; i < tacLine.length; i++)
                    {
                        operand += tacLine[i] + " ";
                    }
                    operand = operand.substring(0, operand.length()-1);
                }
                else{
                    operand = lookup(tacLine[1]);
                }

                print(operator, operand, null, null);
            }

            else if(tacLine[0].equals("in")) {
                String operator = opcode(tacLine[0]);

                String operand = lookup(tacLine[1]);

                print(operator, operand, null, null);
            }

            else if(tacLine[0].equals("if")) {
                String operator = opcode(tacLine[2]);

                String op1 = tacLine[1];

                String op2 = tacLine[3];

                String target = tacLine[5];

                if(lookup(op1) == null){
                    varIDs.add("tempVar" + Integer.toString(++this.tempCount));
                    varTypes.add("INT");
                    if (varTypes.get(this.varAdds.size()-1).equals("INT")){
                        this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+4));
                    }
                    else{
                        this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+1));
                    }
                    this.initVals.add(op1);
                    op1 = this.varAdds.get(this.varAdds.size()-1);
                }
                else{
                    op1 = lookup(op1);
                }

                if(lookup(op2) == null){
                    varIDs.add("tempVar" + Integer.toString(++this.tempCount));
                    varTypes.add("INT");
                    if (varTypes.get(this.varAdds.size()-1).equals("INT")){
                        this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+4));
                    }
                    else{
                        this.varAdds.add(Integer.toString(Integer.parseInt(this.varAdds.get(this.varAdds.size()-1))+1));
                    }
                    this.initVals.add(op2);
                    op2 = this.varAdds.get(this.varAdds.size()-1);
                }
                else{
                    op2 = lookup(op2);
                }

                print(operator, op1, op2, target);
            }

            else if(tacLine[0].equals("goto")){
                String operator = opcode(tacLine[0]);

                String operand = tacLine[1];

                print(operator, operand, null, null);
            }

            else{
                bool = false;
            }

            if(bool){
                nextLine();
                if (this.tacLine == null){
                    bool = false;
                }
            }

            return bool;
        }
        return false;
    }

    private void print(String param1, String param2, String param3, String param4) {
        output.add(new String[] {param1, param2, param3, param4});
    }

    public void output() throws IOException {
        FileWriter fileWriter = new FileWriter("machine-code.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (int i = 0; i < this.output.size(); i++)
        {
            for (int j = 0; j < 4; j++){
                if (this.output.get(i)[j] != null){
                    printWriter.print(this.output.get(i)[j] + "\t");
                }
                else{
                    printWriter.println();
                    break;
                }
                if(j==3){
                    printWriter.println();
                }
            }
        }
        printWriter.close();

        System.out.println("\nMachine code for the input code file written to file \"machine-code.txt\".");

        fileWriter = new FileWriter("translator-symboltable.txt");
        printWriter = new PrintWriter(fileWriter);
        printWriter.println("Symbol Table");
        printWriter.println("------------------------------------------------");
        printWriter.println("Name" + "\t\t" + "Datatype" + "\t" + "Relative Address" + "\t" + "Initial Values");
        printWriter.println("------------------------------------------------");

        for(int i = 0; i < this.varIDs.size(); i++) {
            printWriter.println(varIDs.get(i) + "\t\t" + varTypes.get(i) + "\t\t" + varAdds.get(i) + "\t\t" + initVals.get(i));
        }
        printWriter.close();

        System.out.println("Symbol-table for the input code file has been updated and written to file \"translator-symboltable.txt\".");
    }

    String opcode(String op){
        int val;
        switch(op)
        {
            case "=":
                val = 1;
                break;
            case "+":
                val = 2;
                break;
            case "-":
                val = 3;
                break;
            case "/":
                val = 4;
                break;
            case "*":
                val = 5;
                break;
            case ">":
                val = 6;
                break;
            case ">=":
                val = 7;
                break;
            case "<":
                val = 8;
                break;
            case "<=":
                val = 9;
                break;
            case "==":
                val = 10;
                break;
            case "!=":
                val = 11;
                break;
            case "out":
                val = 12;
                break;
            case "in":
                val = 13;
                break;
            case "goto":
                val = 14;
                break;
            default:
                val = 0;
                break;
        }
        return Integer.toString(val);
    }

    String lookup(String lex){
        if(varIDs.contains(lex)){
            return varAdds.get(varIDs.indexOf(lex));
        }
        else{
            return null;
        }
    }
}

