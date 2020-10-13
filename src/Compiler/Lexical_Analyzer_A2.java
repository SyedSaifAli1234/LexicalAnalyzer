package Compiler;

import javax.swing.*;
import java.io.*;
import java.util.Scanner; // Import the Scanner class to read text files



public class Lexical_Analyzer_A2 {


    private static int c;
    private static int c2;
    private static int varFlag = 0;
    private static int state = 0;

    public Lexical_Analyzer_A2() throws IOException {

    }

    public static void main(String ...args){

        try {
            FileWriter myWriter = new FileWriter("First_File.txt");
            FileWriter myWriter2 = new FileWriter("Second_File.txt");
            File f=new File("text.cmm");         //Creation of File Descriptor for input file
            File f2=new File("text.cmm");         //Creation of File Descriptor for input file
            FileReader fr=new FileReader(f);              //Creation of File Reader object
            FileReader fr2=new FileReader(f2);              //Creation of File Reader object
            BufferedReader br=new BufferedReader(fr);    //Creation of BufferedReader object
            BufferedReader br2= new BufferedReader(fr2);
            StringBuilder myStr = new StringBuilder();
            StringBuilder mynum = new StringBuilder();
            char myRo = '0';

            state = 0;
            Boolean Is_Symbol_after_letter = false;
            Boolean Is_Identifier_after_num = false;
            Boolean Is_Num = false;

            Boolean Is_RO_found = false;


            while((c = br.read()) != -1){                 //Read char by Char
                //c2 = br2.read();
                char character = (char) c;              //converting integer to char
                //char character2 = (char) c2;
                switch(state){

                    case 0:

                        if (Is_RO_found){
                            if(Character.isDigit(character) || Character.isLetter(character)){
                                Is_RO_found = false;
                                myWriter.write("( RO, " + myRo + " ), ");
                            }
                        }

                        if (!Character.isDigit(character)){

                            if(Is_Num){
                                myWriter.write( "(NUM," + mynum + " ), ");
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
                                myWriter.write("(ID, \"i\" ), ");
                                myWriter2.write("i");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"in\" ), ");
                                myWriter2.write("in");
                                myWriter2.write("\n");
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
                                myWriter.write("( 'IF', ^ ),");
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
                                myWriter.write("( 'IF', ^ ),");
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
                                myWriter.write("( 'ELSE', ^ ),");
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
                                myWriter.write("( 'ELSE', ^ ),");
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
                                myWriter.write("( WHILE, ^ ),");
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
                                myWriter.write("( 'WHILE', ^ ),");
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
                                myWriter.write("( WRITE, ^ ),");
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
                                myWriter.write("( 'WRITE', ^ ),");
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
                                myWriter.write("( READ, ^ ), ");
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
                                myWriter.write("( 'READ', ^ ), ");
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
                                myWriter.write("( 'RET', ^ ),");
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
                                myWriter.write("( 'RET', ^ ),");
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
                                myWriter.write("(ID, \"e\" ), ");
                                myWriter2.write("e");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"el\" ), ");
                                myWriter2.write("el");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"els\" ), ");
                                myWriter2.write("els");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"w\" ), ");
                                myWriter2.write("w");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"wh\" ), ");
                                myWriter2.write("wh");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"whi\" ), ");
                                myWriter2.write("whi");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"whil\" ), ");
                                myWriter2.write("whil");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"wr\" ), ");
                                myWriter2.write("wr");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"wri\" ), ");
                                myWriter2.write("wri");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"writ\" ), ");
                                myWriter2.write("writ");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"r\" ), ");
                                myWriter2.write("r");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"re\" ), ");
                                myWriter2.write("re");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"rea\" ), ");
                                myWriter2.write("rea");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"c\" ), ");
                                myWriter2.write("c");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"ch\" ), ");
                                myWriter2.write("ch");
                                myWriter2.write("\n");
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
                                myWriter.write("(ID, \"cha\" ), ");
                                myWriter2.write("cha");
                                myWriter2.write("\n");
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
                                myWriter.write("( 'CHAR', ^ ),");
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
                                myWriter.write("( 'CHAR', ^ ),");
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
                                myWriter.write("( 'INT', ^ ),");
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
                                myWriter.write("( 'INT', ^ ),");
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
                                myWriter.write( "(ID, \" " + myStr + " \"), ");
                                myWriter2.write(""+myStr+"");
                                myWriter2.write("\n");
                                myStr.setLength(0);
                                state = 0;
                            }
                            else if (Character.isLetter(character) || Character.isDigit(character)) {
                                myStr.append(character);
                            }
                            else{//symbol
                                //Function for symbols check
                                myWriter.write( "(ID, \" " + myStr + " \"), ");
                                myWriter2.write( ""+myStr+"");
                                myWriter2.write("\n");
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
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( '(', ^ ), ");
                                state = 0;
                                break;
                            case ')':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( ')', ^ ), ");
                                state = 0;
                                break;
                            case '{':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( '{', ^ ), ");
                                state = 0;
                                break;
                            case '}':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( '}', ^ ), ");
                                state = 0;
                                break;
                            case '[':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( '[', ^ ), ");
                                state = 0;
                                break;
                            case ']':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( ']', ^ ), ");
                                state = 0;
                                break;
                            case ',':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( ',', ^ ), ");
                                state = 0;
                                break;
                            case ';':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( ';', ^ ), ");
                                state = 0;
                                break;
                            case '"':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
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
                                        myWriter.write("( STRING, "+myStr+"), ");
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
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myStr.setLength(0);
                                br2=br;
                                c2 = br2.read();
                                character = (char) c2;
                                if (character == ' '){
                                    myWriter.write("( '/', ^ ), ");
                                }
                                else if(character == '*'){
//                                             myWriter.write("( '/*', ^ )");
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
//                                                            myWriter.write("( 'COMMENT', "+myStr+")");
//                                                            myWriter.write("( '*/', ^ )");
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
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }//Arithmetic operators
                                myWriter.write("( '+', ^ ), ");
                                state = 0;
                                break;
                            case '-':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( '-', ^ ), ");
                                state = 0;
                                break;
                            case '*':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myWriter.write("( '*', ^ ), ");
                                state = 0;
                                break;

                            case '<':                                        //Relational Operators

                                if( Is_RO_found ){
                                    Is_RO_found = false;

                                    if(Character.compare(myRo, '<') == 0){
                                        myWriter.write("( '<<', ^), ");
                                    }
                                    else{
                                        myWriter.write("( SYNTAX ERROR, " + myRo + "<), ");
                                    }
                                }
                                else{
                                    Is_RO_found = true;
                                    myRo = '<';
                                }
                                state = 0;
                                break;

                            case '>':

                                if( Is_RO_found ){
                                    Is_RO_found = false;

                                    if (Character.compare(myRo, '=') == 0 ){
                                        myWriter.write("( RO, GE), ");
                                    }
                                    else{
                                        myWriter.write("( SYNTAX ERROR, " + myRo + ">), ");
                                    }
                                }
                                else{
                                    Is_RO_found = true;
                                    myRo = '>';
                                }
                                state = 0;

                                break;

                            case '=':

                                if( Is_RO_found ){
                                    Is_RO_found = false;

                                    if (Character.compare(myRo, '=') == 0 ){
                                        myWriter.write("( RO, EQ), ");
                                    }
                                    else if (Character.compare(myRo, '<') == 0 ){
                                        myWriter.write("( RO, LE), ");
                                    }
                                    else if (Character.compare(myRo, '>') == 0 ){
                                        myWriter.write("( RO, GE), ");
                                    }
                                    else if (Character.compare(myRo, '!') == 0 ){
                                        myWriter.write("( RO, NE), ");
                                    }
                                    else{
                                        myWriter.write("( SYNTAX ERROR, " + myRo + "=), ");
                                    }

                                }
                                else{
                                    Is_RO_found = true;
                                    myRo = '=';
                                }
                                state = 0;
                                break;

                            case '!':

                                if( Is_RO_found ){

                                    Is_RO_found = false;
                                    myWriter.write("( SYNTAX ERROR, " + myRo + "!), ");
                                }
                                else{
                                    Is_RO_found = true;
                                    myRo = '!';
                                }
                                state = 0;
                                break;


                            case '‘':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                myStr.append(character);
                                c = br.read();
                                character = (char) c;
                                myStr.append(character);
                                c = br.read();
                                character = (char) c;
                                myStr.append(character);
                                myWriter.write("( 'LC', "+myStr+"), ");
                                myStr.setLength(0);
                                state = 0;
                                break;
                            case ':':
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
                                if ((c = br.read()) != -1) {
                                    character = (char) c;
                                    if (character == '=') {
                                        myWriter.write("( ':=', ^ ), ");
                                    } else {
                                        myWriter.write("( ':', ^ ), ");
                                    }
                                }
                                state = 0;
                                myStr.setLength(0);
                                state = 0;

                                break;
                            default:
                                if( Is_RO_found ){
                                    Is_RO_found = false;
                                    myWriter.write("( RO, " + myRo + " ), ");
                                }
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
                                        myWriter.write("( 'INT', ^ ), ");
                                        state = 0;
                                        break;
                                   case ':':
                                        if((c = br.read()) != -1){
                                             character = (char) c;
                                             myWriter.write("( 'INT', ^ ), ");
                                             if(character == '='){
                                                  myWriter.write("( ':=', ^ ), ");
                                             }
                                             else {
                                                  myWriter.write("( ':', ^ ), ");
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
                                        myWriter.write("( 'CHAR', ^ ), ");
                                        state = 0;
                                        break;
                                   case ':':
                                        if((c = br.read()) != -1){
                                             character = (char) c;
                                             myWriter.write("( 'CHAR', ^ ), ");
                                             if(character == '='){
                                                  myWriter.write("( ':=', ^ ), ");
                                             }
                                             else {
                                                  myWriter.write("( ':', ^ ), ");
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
            myWriter.close();
            myWriter2.close();

                    // PrintWriter object for output.txt
                    PrintWriter pw = new PrintWriter("SecondFile.txt");

                    // BufferedReader object for input.txt
                    BufferedReader br1 = new BufferedReader(new FileReader("Second_File.txt"));

                    String line1 = br1.readLine();

                    // loop for each line of input.txt
                    while(line1 != null)
                    {
                        boolean flag = false;

                        // BufferedReader object for output.txt
                        BufferedReader br3 = new BufferedReader(new FileReader("SecondFile.txt"));

                        String line2 = br3.readLine();

                        // loop for each line of output.txt
                        while(line2 != null)
                        {

                            if(line1.equals(line2))
                            {
                                flag = true;
                                break;
                            }

                            line2 = br3.readLine();

                        }

                        // if flag = false
                        // write line of input.txt to output.txt
                        if(!flag){
                            pw.println(line1);

                            // flushing is important here
                            pw.flush();
                        }

                        line1 = br1.readLine();

                    }

                    // closing resources
                    br1.close();
                    pw.close();

                    System.out.println("File operation performed successfully");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//1. File reading (Input mai file aye gi which will contain code for our lex)
