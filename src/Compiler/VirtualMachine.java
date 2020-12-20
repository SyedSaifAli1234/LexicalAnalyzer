package Compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class VirtualMachine {
    private ArrayList<String[]> quad;
    private ArrayList<String> ds;
    Scanner scanner;

    String[] tacLine;

    boolean error;

    public VirtualMachine() {
        this.quad = new ArrayList<>();
        this.ds = new ArrayList<>();

        this.scanner = null;

        error = false;
    }

    public boolean readSymbolTable(String fileName) {
        try {
            this.scanner = new Scanner(new File(fileName));

            String line;
            String[] lineSplit;
            scanner.nextLine();
            scanner.nextLine();
            scanner.nextLine();
            scanner.nextLine();

            String initVal;
            String type;

            while(scanner.hasNextLine()){
                line = scanner.nextLine();

                lineSplit = line.split("\t\t");

                type = lineSplit[1];
                initVal = lineSplit[3];

                this.ds.add(initVal);

                if(type.equals("INT")){
                    this.ds.add("-");
                    this.ds.add("-");
                    this.ds.add("-");
                }
            }
            this.scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.scanner.close();
        return false;
    }

    public boolean readMachineCode(String fileName) {
        try {
            this.scanner = new Scanner(new File(fileName));

            String temp;
            String[] tempArr;

            while(scanner.hasNextLine()){
                temp = scanner.nextLine();

                tempArr = temp.split("\t");

                this.quad.add(tempArr);
            }
            this.scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.scanner.close();
        return false;
    }

    public void execute(){
        int i, j , k;

        System.out.println("\nCode execution after dashed line\n\n-------------------------------------\n");

        this.scanner = new Scanner(System.in);

        for (int pc = 0; pc < quad.size() && !this.error; ++pc) {
            switch (operator(quad.get(pc)[0])) {
                // "=" op target
                case "=":
                    i = Integer.parseInt(quad.get(pc)[2]);

                    String val;

                    if(quad.get(pc)[1].startsWith("'")){
                        val = quad.get(pc)[1].substring(1, 2);
                    }
                    else{
                        j = Integer.parseInt(quad.get(pc)[1]);
                        val = this.ds.get(j);
                    }

                    this.ds.set(i, val);
                    break;

                // "+" op1 op2 target
                case "+":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    int sum = Integer.parseInt(this.ds.get(i)) + Integer.parseInt(this.ds.get(j));

                    this.ds.set(k, Integer.toString(sum));
                    break;

                // "-" op1 op2 target
                case "-":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    int difference = Integer.parseInt(this.ds.get(i)) - Integer.parseInt(this.ds.get(j));

                    this.ds.set(k, Integer.toString(difference));
                    break;

                // "/" op1 op2 target
                case "/":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    int quotient = Integer.parseInt(this.ds.get(i)) / Integer.parseInt(this.ds.get(j));

                    this.ds.set(k, Integer.toString(quotient));
                    break;

                // "*" op1 op2 target
                case "*":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    int product = Integer.parseInt(this.ds.get(i)) * Integer.parseInt(this.ds.get(j));

                    this.ds.set(k, Integer.toString(product));
                    break;

                // ">" op1 op2 target
                case ">":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    if (Integer.parseInt(this.ds.get(i)) > Integer.parseInt(this.ds.get(j))){
                        pc = k-2;
                    }
                    break;

                // ">=" op1 op2 target
                case ">=":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    if (Integer.parseInt(this.ds.get(i)) >= Integer.parseInt(this.ds.get(j))){
                        pc = k-2;
                    }
                    break;

                // "<" op1 op2 target
                case "<":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    if (Integer.parseInt(this.ds.get(i)) < Integer.parseInt(this.ds.get(j))){
                        pc = k-2;
                    }
                    break;

                // "<=" op1 op2 target
                case "<=":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    if (Integer.parseInt(this.ds.get(i)) <= Integer.parseInt(this.ds.get(j))){
                        pc = k-2;
                    }
                    break;

                // "==" op1 op2 target
                case "==":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    if (Integer.parseInt(this.ds.get(i)) == Integer.parseInt(this.ds.get(j))){
                        pc = k-2;
                    }
                    break;

                // "!=" op1 op2 target
                case "!=":
                    i = Integer.parseInt(quad.get(pc)[1]);
                    j = Integer.parseInt(quad.get(pc)[2]);
                    k = Integer.parseInt(quad.get(pc)[3]);

                    if (Integer.parseInt(this.ds.get(i)) != Integer.parseInt(this.ds.get(j))){
                        pc = k-2;
                    }
                    break;

                // "out" op
                case "out":
                    try{
                        i = Integer.parseInt(quad.get(pc)[1]);
                        System.out.print(this.ds.get(i));
                    }
                    catch (NumberFormatException e){
                        String t1 = quad.get(pc)[1].substring(1, quad.get(pc)[1].length()-1);
                        t1 = t1.replaceAll( "\\\\n", System.lineSeparator());
                        t1 = t1.replaceAll( "\\\\t", "\t");
                        System.out.print(t1);
                    }

                    break;

                // "in" op
                case "in":
                    i = Integer.parseInt(quad.get(pc)[1]);

                    char temp;
                    int num;

                    if(this.ds.get(i).equals("null")){
                        temp = scanner.next().charAt(0);
                        if((temp >= 'A' && temp <= 'Z') || (temp >= 'a' && temp <= 'z')){
                            this.ds.set(i, Character.toString(temp));
                        }
                        else{
                            System.out.println("Input Mismatch Exception: You can only enter character values.");
                            error = true;
                            break;
                        }
                    }
                    else{
                        try{
                            num = scanner.nextInt();
                            this.ds.set(i, Integer.toString(num));
                        }
                        catch(java.util.InputMismatchException e)
                        {
                            System.out.println("Input Mismatch Exception: You can only enter integral values.");
                            error = true;
                            break;
                        }
                    }

                    break;

                // "goto" op
                case "goto":
                    i = Integer.parseInt(quad.get(pc)[1]);

                    pc = i-2;

                    break;
            }
        }

        this.scanner.close();
    }

    String operator(String opcode){
        String val;
        switch(opcode)
        {
            case "1":
                val = "=";
                break;
            case "2":
                val = "+";
                break;
            case "3":
                val = "-";
                break;
            case "4":
                val = "/";
                break;
            case "5":
                val = "*";
                break;
            case "6":
                val = ">";
                break;
            case "7=":
                val = ">=";
                break;
            case "8":
                val = "<";
                break;
            case "9":
                val = "<=";
                break;
            case "10":
                val = "==";
                break;
            case "11":
                val = "!=";
                break;
            case "12":
                val = "out";
                break;
            case "13":
                val = "in";
                break;
            case "14":
                val = "goto";
                break;
            default:
                val = null;
                break;
        }
        return val;
    }

}

