package Compiler;

import javax.swing.*;
import java.io.*;
import java.util.Scanner; // Import the Scanner class to read text files



public class Lexical_Analyzer_A2 {


    private static int c;
    private static int c2;
    private static int varFlag = 0;
    private static int state = 0;
    public Lexical_Analyzer_A2() throws FileNotFoundException {
        System.out.println("File not found");
    }

    public static void main(String ...args){

        try {
            File f=new File("text.cmm");         //Creation of File Descriptor for input file
            File f2=new File("text.cmm");         //Creation of File Descriptor for input file
            FileReader fr=new FileReader(f);              //Creation of File Reader object
            FileReader fr2=new FileReader(f2);              //Creation of File Reader object
            BufferedReader br=new BufferedReader(fr);    //Creation of BufferedReader object
            BufferedReader br2= new BufferedReader(fr2);
            StringBuilder myStr = new StringBuilder();
            StringBuilder mynum = new StringBuilder();
            StringBuilder myRo = new StringBuilder();

            state = 0;
            Boolean Is_Symbol_after_letter = false;
            Boolean Is_Identifier_after_num = false;
            Boolean Is_Num = false;

            Boolean Is_RO_found = false;


            while((c = br.read()) != -1){                 //Read char by Char
                c2 = br2.read();
                char character = (char) c;              //converting integer to char
                char character2 = (char) c2;
                switch(state){

                    case 0:

                        if (Is_RO_found){
                            if(Character.isDigit(character) || Character.isLetter(character)){
                                Is_RO_found = false;
                                System.out.print("( RO, " + myRo + " ), ");
                            }
                        }

                        if (!Character.isDigit(character)){

                            if(Is_Num){
                                System.out.println( "(NUM," + mynum + " ), ");
                                Is_Num = false;
                                mynum.setLength(0);
                            }
                        }

                        if (Character.isDigit(character)){
                            Is_Num = true;
                            mynum.append(character);
                            //state = 3000;//for numbers
                        }
                        else if (Character.isLetter(character)){

                            switch(character){

                                case 'i':          //Data types
                                    //agla wala character wo aghar (!letter aur digit)symbol hai tou state = 0
                                    //else aghar symbol nahi hai tou state1
                                    state = 1;
                                    myStr.append(character);
                                    break;
                                case 'e':
                                    state = 5;
                                    myStr.append(character);
                                    break;
                                case 'w':
                                    state = 9;
                                    myStr.append(character);
                                    break;
                                case 'r':
                                    state = 18;
                                    myStr.append(character);
                                    break;
                                case 'c':
                                    state = 23;
                                    myStr.append(character);
                                    break;
                                default:
                                    state = 900;//id
                                    myStr.append(character);
                                    break;

                            }//switch(character) end

                        }//is letter
                        else{
                            state = 9000;
                            Is_Symbol_after_letter = true;
                        }//else for symbols

                        if (Is_Symbol_after_letter == true){

                        }
                        else{
                            break;
                        }

                    case 1:             //We have I
                        if ( Is_Symbol_after_letter == true ){
                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'n':
                                        state = 2;
                                        myStr.append(character);
                                        break;
                                    case 'f':
                                        state = 4;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"i\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }

                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }

                    case 2:             //We have IN

                        if ( Is_Symbol_after_letter == true ){
                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 't':
                                        state = 91;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"in\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }

                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }

                    case 4:    //We have IF

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'IF', ^ ),");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'IF', ^ ),");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }

                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }

                    case 8:    //We have ELSE

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'ELSE', ^ ),");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'ELSE', ^ ),");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }

                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }

                    case 13:   //We have WHILE

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'WHILE', ^ ),");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'WHILE', ^ ),");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }

                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }


                    case 17:   //We have WRITE

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'WRITE', ^ ),");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'WRITE', ^ ),");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }


                    case 21:   //We have READ

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'READ', ^ ), ");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'READ', ^ ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }

                    case 22:   //We have RET

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'RET', ^ ),");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'RET', ^ ),");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }


                    case 5:   //We have E

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'l':
                                        state = 6;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"e\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }

                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 6:   //We have EL

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 's':
                                        state = 7;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"el\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 7:   //We have ELS

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'e':
                                        state = 8;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"els\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 9:   //We have W

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'h':
                                        state = 10;
                                        myStr.append(character);
                                        break;
                                    case 'r':
                                        state = 14;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"w\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 10:   //We have WH

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'i':
                                        state = 11;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"wh\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 11:   //We have WHI

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'l':
                                        state = 12;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"whi\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 12:   //We have WHIL

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'e':
                                        state = 13;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"whil\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 14:   //We have WR

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'i':
                                        state = 15;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"wr\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }


                    case 15:   //We have WRI

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 't':
                                        state = 16;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"wri\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 16:   //We have WRIT

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'e':
                                        state = 17;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"writ\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }


                    case 18:   //We have R

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'e':
                                        state = 19;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"r\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }

                    case 19:   //We have RE

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'a':
                                        state = 20;
                                        myStr.append(character);
                                        break;
                                    case 't':
                                        state = 22;
                                        myStr.append(character);
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"re\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 20:   //We have REA

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'd':
                                        state = 21;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"rea\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 23:   //We have C

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'h':
                                        state = 24;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"c\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 24:   //We have CH

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'a':
                                        state = 25;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"ch\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 25:   //We have CHA

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isLetter(character)){
                                switch( character){
                                    case 'r':
                                        state = 90;
                                        myStr.append(character);
                                        break;
                                    default:
                                        state = 900;//id
                                        myStr.append(character);
                                        break;
                                }
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.print("(ID, \"cha\" ), ");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 90: //WE have CHAR

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'CHAR', ^ ),");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'CHAR', ^ ),");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 91: //We have INT

                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Character.isSpaceChar(character)){
                                System.out.println("( 'INT', ^ ),");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character)) {
                                state = 900;//id
                                myStr.append(character);
                            }
                            else if (Character.isDigit(character)){
                                state = 900;//id
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println("( 'INT', ^ ),");
                                myStr.setLength(0);
                                state = 9000;
                                Is_Symbol_after_letter = true;
                            }
                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }




                    case 900:
                        //id here
                        if ( Is_Symbol_after_letter == true ){

                        }
                        else{
                            if (Is_Identifier_after_num == true){
                                Is_Identifier_after_num = false;
                            }
                            if (Character.isSpaceChar(character)){
                                System.out.println( "(ID, \" " + myStr + " \"), ");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character) || Character.isDigit(character)) {
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                System.out.println( "(ID, \" " + myStr + " \"), ");
                                myStr.setLength(0);
                                state = 0;
                                Is_Symbol_after_letter = true;
                            }

                            if (Is_Symbol_after_letter == true){

                            }
                            else{
                                break;
                            }
                        }



                    case 9000:

                        if (Is_Symbol_after_letter == true){
                            Is_Symbol_after_letter = false;
                        }

                        switch (character) {

                            case '(':                                         //Additional operators
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( '(', ^ ), ");
                                state = 0;
                                break;
                            case ')':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( ')', ^ ), ");
                                state = 0;
                                break;
                            case '{':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( '{', ^ ), ");
                                state = 0;
                                break;
                            case '}':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( '}', ^ ), ");
                                state = 0;
                                break;
                            case '[':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( '[', ^ ), ");
                                state = 0;
                                break;
                            case ']':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( ']', ^ ), ");
                                state = 0;
                                break;
                            case ',':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( ',', ^ ), ");
                                state = 0;
                                break;
                            case ';':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( ';', ^ ), ");
                                state = 0;
                                break;
                            case '"':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                myStr.setLength(0);
                                myStr.append(character);
                                while ((c = br.read()) != -1) {
                                    character = (char) c;
                                    if (character != '"') {
                                        myStr.append(character);
                                    }
                                    else if (character == '"'){
                                        myStr.append(character);
                                        System.out.println("( 'STRING', "+myStr+"), ");
                                        state = 0;
                                        break;
                                    }
                                }
                                myStr.setLength(0);
                                state = 0;
                                break;
                            case '/':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                myStr.setLength(0);
                                br2=br;
                                c2 = br2.read();
                                character = (char) c2;
                                if (character == ' '){
                                    System.out.println("( '/', ^ ), ");
                                }
                                else if(character == '*'){
//                                             System.out.println("( '/*', ^ )");
                                    while ((c = br.read()) != -1) {
                                        character = (char) c;
                                        if (character != '*') {
                                            myStr.append(character);
                                        }
                                        else if (character == '*'){
                                            br2=br;
                                            c2 = br2.read();
                                            character = (char) c2;
                                            if (character == '/'){
//                                                            System.out.println("( 'COMMENT', "+myStr+")");
//                                                            System.out.println("( '*/', ^ )");
                                                myStr.setLength(0);
                                                state = 0;
                                                break;

                                            }
                                            myStr.append(character);
                                        }
                                    }
                                }


                                myStr.setLength(0);
                                state = 0;
                                break;

                            case '+':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }//Arithmetic operators
                                System.out.println("( '+', ^ ), ");
                                state = 0;
                                break;
                            case '-':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( '-', ^ ), ");
                                state = 0;
                                break;
                            case '*':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                System.out.println("( '*', ^ ), ");
                                state = 0;
                                break;
                            case '<':                                        //Relational Operators
                                c2 = br2.read();
                                character2 = (char) c2;
                                if(character2 == '='){
                                    System.out.println("( '<=', ^ ), ");
                                    br.read();
                                }
                                        /*else if(character2 == ','){
                                             System.out.println("( '<', ^ ), ");
                                             System.out.println("( ',', ^ ), ");
                                             br.read();
                                        }*/
                                else {
                                    System.out.println("( '<', ^ ), ");
                                }
                                state = 0;
                                br2=br;
                                break;

                            case '>':
                                br2=br;
                                c2 = br2.read();
                                character = (char) c2;
                                if(character == '='){
                                    System.out.println("( '>=', ^ ), ");
                                    br.read();
                                }
                                else if(character == ','){
                                    System.out.println("( '>', ^ ), ");
                                    System.out.println("( ',', ^ ), ");
                                    br.read();
                                }
                                else if(character == '>'){
                                    System.out.println("( '>>', ^ ), ");
                                    br.read();
                                }
                                else {
                                    System.out.println("( '>', ^ ), ");
                                }
                                state = 0;
                                break;
                            case '=':
                                br2=br;
                                c2 = br2.read();
                                character = (char) c2;
                                if(character == '='){
                                    System.out.println("( '==', ^ ), ");
                                }
                                else {
                                    System.out.println("(Syntax Error, '=' cannot be understood), ");
                                }
                                state = 0;
                                break;
                            case '!':
                                br2=br;
                                c2 = br2.read();
                                character = (char) c2;
                                if(character == '='){
                                    System.out.println("( '!=', ^ )");
                                }
                                else {
                                    System.out.println("(Syntax Error, '!' cannot be understood), ");
                                }
                                state = 0;
                                break;





                            case '‘':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                myStr.append(character);
                                c = br.read();
                                character = (char) c;
                                myStr.append(character);
                                c = br.read();
                                character = (char) c;
                                myStr.append(character);
                                System.out.println("( 'LC', "+myStr+"), ");
                                myStr.setLength(0);
                                state = 0;
                                break;
                            case ':':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    System.out.print("( RO, " + myRo + " ), ");
                                }
                                if ((c = br.read()) != -1) {
                                    character = (char) c;
                                    if (character == '=') {
                                        System.out.println("( ':=', ^ ), ");
                                    } else {
                                        System.out.println("( ':', ^ ), ");
                                    }
                                }
                                state = 0;
                                myStr.setLength(0);
                                state = 0;

                                break;
                            default:
                                state = 0;
//                                           myStr.append(character);
                                //Wrong symbol
                                break;
                        }//switch(character) end
                        break;
                              /*
                         case 200:   //Additional characters after INT
                              switch (character) {
                                   case ' ':
                                        System.out.print("( 'INT', ^ ), ");
                                        state = 0;
                                        break;
                                   case ':':
                                        if((c = br.read()) != -1){
                                             character = (char) c;
                                             System.out.print("( 'INT', ^ ), ");
                                             if(character == '='){
                                                  System.out.print("( ':=', ^ ), ");
                                             }
                                             else {
                                                  System.out.print("( ':', ^ ), ");
                                             }
                                        }
                                        state = 0;
                                        myStr.setLength(0);
                                        break;
                                   default:
                                        state = 9000;
                                        break;
                              }

                              break;
                         case 201:   //Additional characters after CHAR
                              switch (character) {
                                   case ' ':
                                        System.out.print("( 'CHAR', ^ ), ");
                                        state = 0;
                                        break;
                                   case ':':
                                        if((c = br.read()) != -1){
                                             character = (char) c;
                                             System.out.print("( 'CHAR', ^ ), ");
                                             if(character == '='){
                                                  System.out.print("( ':=', ^ ), ");
                                             }
                                             else {
                                                  System.out.print("( ':', ^ ), ");
                                             }
                                        }
                                        state = 0;
                                        myStr.setLength(0);
                                        break;
                                   default:
                                        state = 9000;
                                        break;
                              }

                              break; */
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//1. File reading (Input mai file aye gi which will contain code for our lex)
