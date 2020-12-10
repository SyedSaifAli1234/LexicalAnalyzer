package Compiler;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    private ArrayList<String> parsetree;
    private ArrayList<String> vars_id;
    private ArrayList<String> vars_type;
    int tabs;
    Scanner scanner;
    TLPair pair;
    boolean EOF;
    boolean error;
    TLPair errorCause;
    int flag = 0;
    int count = 1;
    int end = 0;

    public Parser() throws IOException {
        this.parsetree = new ArrayList<>();
        this.vars_id = new ArrayList<>();
        this.vars_type = new ArrayList<>();
        this.tabs = 0;
        this.scanner = null;
        this.pair = null;
        this.EOF = false;
        this.error = false;
        this.errorCause = null;
    }

    public boolean openFile(String fileName) {
        try {
            this.scanner = new Scanner(new File(fileName));
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private boolean nextToken() {
        if(scanner.hasNextLine()) {
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

    public boolean parse() {

        nextToken();
        Program();

        if(this.error) {
            return false;
        }

        return true;
    }

    private void Program() {
        if(!this.error) {
            print("Program()");
            this.tabs++;
            if(pair.getLexeme().equals("") && pair.getToken().equals(""))
            {
                String getValue = match("");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
            }
            else {
                this.tabs++;
                DataType();
            }
        }
    }

    private void DataType() {
        if (!this.error)
        {
            print("DataType()");
            this.tabs++;

            Type();
            vars_type.add("FUNC");

            String getValue2 = match(":");
            if(!getValue2.equals("Syntax Error."))
            {
                print(getValue2);
                this.tabs--;
            }

            String getValue = ID();
            vars_id.add(getValue);

            Params();

            this.tabs--;
        }
    }

    private String Type() {
        if(!this.error)
        {
            String getValue = null;

            if(this.pair.getToken().equalsIgnoreCase("INT")) {

                getValue = match("INT");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }

                this.tabs--;
            }
            else if(this.pair.getToken().equalsIgnoreCase(" 'CHAR'")) {

                getValue = match(" 'CHAR'");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }

                this.tabs--;
            }
            else {
                this.error = true;
                System.out.println("Syntax error.");
            }
            //this.tabs--;
            return getValue;
        }
        return null;
    }

    private String ID() {
        if(!this.error) {
            this.tabs++;
            print("ID()");
            this.tabs++;

            String getValue = pair.getLexeme();

            String getValue2 = match("ID");
            if(!getValue2.equals("Syntax Error."))
            {
                print("(" + getValue + ")");

                this.tabs--;
            }

            this.tabs--;
            return getValue;
        }
        return null;
    }

    private void Params() {
        if(!this.error) {
            this.tabs++;

            if(this.pair.getToken().equals(";")) {

                String getValue = match(";");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
            }
            else if(this.pair.getToken().equals(" '('")) {
                String getValue = match(" '('");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
                A();

                getValue = match(" ')'");
                this.tabs++;
                if(!getValue.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue);
                }

                this.tabs--;

                getValue = match(" '{'");
                if(!getValue.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue);
                }

                this.tabs--;

                ST();

                getValue = match("}");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }

                this.tabs--;
            }
            else {
                this.error = true;
                System.out.println("Syntax error.");
            }
            this.tabs--;
        }
    }

    private void A() {
        if(!this.error) {
            this.tabs++;
            print("A()");
            this.tabs++;

            if(pair.getLexeme().equals("") && pair.getToken().equals(""))
            {
                String getValue = match("");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
            }
            else {
                print("DataType()");
                this.tabs++;
                String getValue = Type();
                vars_type.add(getValue);

                String getValue2 = match(":");
                if(!getValue2.equals("Syntax Error."))
                {
                    print(getValue2);
                    this.tabs--;
                }

                getValue = ID();
                vars_id.add(getValue);

                A_Dash();

                this.tabs--;
            }

            this.tabs--;
        }
    }

    private void A_Dash() {
        if(!this.error) {
            this.tabs++;
            print("A_Dash()");
            this.tabs++;

            if(this.pair.getToken().equals(" ,")) {
                String getValue = match(" ,");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;

                A();
            }
            else if(pair.getLexeme().equals("") && pair.getToken().equals(""))
            {
                String getValue = match("");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
            }
            else {
                ;
            }

            this.tabs--;
        }
    }

    private void ST() {
        if(!this.error) {
            this.tabs++;
            print("ST()");
            this.tabs++;

            if(States() && end==0)
            {
                ST();
            }
            else
            {

            }

            this.tabs--;
        }
    }

    private boolean States() {
        if(flag == 1){
            count++;
        }
        if(!this.error) {
            print("States()");
            this.tabs++;

            boolean bool = true;

            if(this.pair.getToken().equalsIgnoreCase("INT") || this.pair.getToken().equalsIgnoreCase(" 'CHAR'")) {
                print("DataType()");
                this.tabs++;
                String getValue = Type();
                vars_type.add(getValue);

                String getValue2 = match(":");
                if(!getValue2.equals("Syntax Error."))
                {
                    print(getValue2);
                    this.tabs--;
                }

                getValue = ID();
                vars_id.add(getValue);

                StatesLF3();

                getValue = match(" ';'");
                if(!getValue.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue);
                }
                this.tabs--;
            }
            else if(this.pair.getToken().equalsIgnoreCase("ID")) {
                String getValue;
                int localFlag = 0;
                this.tabs--;
                ID();
                if(this.pair.getToken().equalsIgnoreCase(" RO")){
                    localFlag = 1;
                    this.tabs++;
                    this.tabs++;
                    RO();
                    if(this.pair.getToken().equals(" '+'") || this.pair.getToken().equals(" '-'") || this.pair.getToken().equals("*") || this.pair.getToken().equals("/")) {
                        getValue = match(this.pair.getToken());
                        if(!getValue.equals("Syntax Error."))
                        {
                            this.tabs++;
                            print(getValue);
                        }

                        this.tabs--;
                        TypeCheck2();
                    }
                }
                else {
                    getValue = match(" ':='");
                    if (!getValue.equals("Syntax Error.")) {
                        this.tabs++;
                        print(getValue);
                    }
                }

                    this.tabs--;
                if(localFlag == 0) {
                    TypeCheck();
                }
                    getValue = match(" ';'");
                    if (!getValue.equals("Syntax Error.")) {
                        this.tabs++;
                        print(getValue);
                    }
                    this.tabs--;

            }
            else if(this.pair.getToken().equals(" 'WRITE'")) {
                String getValue = match(" 'WRITE'");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;

                getValue = match(" '('");
                if(!getValue.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue);
                }

                this.tabs--;

                if(this.pair.getToken().equals(" STRING")) {
                    this.tabs++;
                    Data1();
                }
                else if(this.pair.getToken().equalsIgnoreCase("ID")) {
                    this.tabs++;
                    ID();
                }

                getValue = match(" ')'");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }

                getValue = match(" ';'");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }

                this.tabs--;
            }
            else if(this.pair.getToken().equals(" READ")) {
                String getValue = match(" READ");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                getValue = match(" '>>'");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
                ID();

                getValue = match(" ';'");
                if(!getValue.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue);
                }

                this.tabs--;
            }
            else if(this.pair.getLexeme().equals("") && this.pair.getToken().equals("")) {
                String getValue = match("");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                end = 1;
                this.tabs--;
            }
            else if(this.pair.getToken().equalsIgnoreCase(" WHILE")) {
                flag = 1;
                String getValue = match(" WHILE");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                getValue = match(" '('");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
                ROCon();
                getValue = match(" ')'");
                if(!getValue.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue);
                }
                getValue = match(" '{'");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
                ST();
                for (int h = 0; h<count; h++){
                    this.tabs++;
                }
                getValue = match(" '}'");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                flag = 0;
                count = 0;
                this.tabs--;
            }
            else if(this.pair.getToken().equalsIgnoreCase("RETURN")) {
                String getValue = match("RETURN");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
                getValue = match("ID");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
                getValue = match(";");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;
            }
            else {
                bool = false;
            }

            this.tabs--;
            this.tabs--;
            return bool;
        }
        return false;
    }

    private boolean StatesLF3() {
        if(!this.error) {
//            print("StatesLF3()");
              this.tabs++;

            if(this.pair.getToken().equals(" ,")) {
                String getValue = match(" ,");
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }
                this.tabs--;

                getValue = ID();
                vars_id.add(getValue);
                vars_type.add(vars_type.get(vars_type.size()-1));

                StatesLF3();
                //this.tabs--;
                return true;
            }

            else {
                this.tabs--;
                return false;
            }
        }
        return false;
    }

    private void Data1() {
        if(!this.error) {
            print("Data1()");
            this.tabs++;

            String getValue = pair.getLexeme();

            String getValue2 = match(" STRING");
            if(!getValue2.equals("Syntax Error."))
            {
                print(getValue2 + "(" + getValue + ")");
            }

            this.tabs--;
        }
    }

    private void TypeCheck() {
        if(!this.error) {

            if(this.pair.getToken().equals(" 'LC'")) {
                String getValue = pair.getLexeme();

                String getValue2 = match(" 'LC'");
                if(!getValue2.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue2 + "(" + getValue + ")");
                }

                //this.tabs--;
            }
            else if(this.pair.getToken().equals("ID") || this.pair.getToken().equals("NUM")) {
                Expression();
            }

            this.tabs--;
        }
    }

    private void Expression() {
        if(!this.error) {
            this.tabs++;

            TypeCheck2();
            if(this.pair.getToken().equals(" '+'") || this.pair.getToken().equals(" '-'") || this.pair.getToken().equals("*") || this.pair.getToken().equals("/")) {
                String getValue = match(this.pair.getToken());
                if(!getValue.equals("Syntax Error."))
                {
                    print(getValue);
                }

                this.tabs--;
                TypeCheck2();
            }
            else {
                ;
            }
        }
    }

    private void ROCon() {
        if(!this.error) {
            this.tabs++;
            print("ROCon()");
            this.tabs++;

            TypeCheck2();
            RO();
            TypeCheck2();

            this.tabs--;
        }
    }

    private void TypeCheck2() {
        if(!this.error) {

            if(this.pair.getToken().equals("ID")) {
                String getValue = pair.getLexeme();

                String getValue2 = match("ID");
                if(!getValue2.equals("Syntax Error."))
                {
                    print(getValue2 + "(" + getValue + ")");
                }
            }
            else if(this.pair.getToken().equals("NUM")) {
                String getValue = pair.getLexeme();

                String getValue2 = match("NUM");
                if(!getValue2.equals("Syntax Error."))
                {
                    this.tabs++;
                    print(getValue2 + "(" + getValue + ")");
                }
                this.tabs--;
            }

            //this.tabs--;
        }
    }

    private void RO() {
        if(!this.error) {
            print("RO()");
            this.tabs++;

            String getValue = pair.getLexeme();

            String getValue2 = match(" RO");
            if(!getValue2.equals("Syntax Error."))
            {
                print(getValue2 + "(" + getValue + ")");
            }
            this.tabs--;

            this.tabs--;
        }
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
        String temp = "";
        for (int i = 0; i < tabs-1; i++) {
            temp += "|\t";
        }
        temp += "|__" + text;
        parsetree.add(temp);
        System.out.println(parsetree);

    }

    public void output() throws IOException {
            FileWriter fileWriter = new FileWriter("parsetree.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < parsetree.size(); i++)
            {
                printWriter.println(parsetree.get(i));
            }
            printWriter.close();
            fileWriter = new FileWriter("parser-symboltable.txt");
            printWriter = new PrintWriter(fileWriter);
            printWriter.println("Identifier: Data Type");

            for (int i = 0; i < vars_type.size(); i++)
            {
                printWriter.println(vars_id.get(i) + ": " + vars_type.get(i));
            }
            printWriter.close();
    }
}
