package LR1_parsing;

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    private static final Map<Integer, Rule> grammer = new HashMap<Integer, Rule>() {
        {
            put(1, new Rule("S", "L=R"));
            put(2, new Rule("S", "R"));
            put(3, new Rule("L", "*R"));
            put(4, new Rule("L", "id"));
            put(5, new Rule("R", "L"));
        }
    };

    private static final String[][] LR1_table = {
        //    N |  id |  *  |  =  |  $ |   S |  L |  R
        {"0", "s5", "s4", null, null, "1", "2", "3"},
        {"1", null, null, null, "acc", null, null, null},
        {"2", null, null, "s6", "r5", null, null, null},
        {"3", null, null, null, "r2", null, null, null},
        {"4", "s5", "s4", null, null, null, "8", "7"},
        {"5", null, null, "r4", "r4", null, null, null},
        {"6", "s12", "s11", null, null, null, "10", "9"},
        {"7", null, null, "r3", "r3", null, null, null},
        {"8", null, null, "r5", "r5", null, null, null},
        {"9", null, null, null, "r1", null, null, null},
        {"10", null, null, null, "r5", null, null, null},
        {"11", "s12", "s11", null, null, null, "10", "13"},
        {"12", null, null, null, "r4", null, null, null},
        {"13", null, null, null, "r3", null, null, null},};

    // valid symbols in any word generated from the grammer
    private static final String[] valid_symbols = {"id", "*", "="};

    public static Stack<String> stack = new Stack<>();
    public static LinkedList<String> input = new LinkedList<>();
    public static Stack<Rule> output = new Stack<>();
    
    // array that containes all the results that should appear in the results excution area in the GUI
    public static ArrayList<String> resultPhrases = new ArrayList<>();
    
    // ----------------------------------------------------------------------- Main.
    public static void main(String[] args) {
        
        // GUI code
        LR1_MainFrame m = new LR1_MainFrame();
        m.setVisible(true);
        m.setLocationRelativeTo(null);
        // --------------------------- 
        
        
        // excute program on console
        
        System.out.println("**************************** LR(1) Parsing ****************************\n");
        System.out.println("The Grammer is:");
        printGrammer();

        Scanner s = new Scanner(System.in);
        String input;
        System.out.print("\nEnter a word that can be generated from the above grammer : ");
        input = s.next();

        boolean result = checkInput(input);

        if (result) {
            System.out.println("--------------------------------------------------------------------------------------------");
            System.out.println("\nYour Input is CORRECT.\n");
            resultPhrases.add("\n\nYour Input is CORRECT.\n");
            resultPhrases.add("\n");
            System.out.println("The Derivation of the word : " + input);
            resultPhrases.add("\nThe Derivation of the word : " + input + "\n");
            showDerivation(input);
            System.out.println();
            resultPhrases.add("\n");
        } else if (isValidSymbols(input)) {
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println("\nSo, Your Input is WRONG !!\n");
            resultPhrases.add("\nSo, Your Input is WRONG !!\n");
        } else {
            System.out.println("\nSorry, !! Invalid Symbols\n");
            resultPhrases.add("\nSorry, !! Invalid Symbols\n");
        }
    }
    // ---------------------------------------------------------------------- End Main

    public static boolean checkInput(String s) {

        // check symbols 
        if (!isValidSymbols(s)) {
            return false;
        }

        /* ------------------------------- start the algorithm ------------------------------ */
        System.out.println("\nThe Algorithm Execution ..");
        System.out.println("--------------------------------------------------------------------------------------------");
        
        // prepare the stack
        String startState = LR1_table[0][0];
        stack.push(startState);

        // prepare the input colom by taking the user input and put it in the input linked list
        // make symbols separated from each other. then put them into the input linked list. And at the end add $
        String[] symbols = getSymbolsAsArray(s);
        
        
        // add the symbols into the input stack
        for (int i = 0; i < symbols.length; i++) {
            System.out.print(symbols[i] + " ");
            
            input.addLast(symbols[i]);
        }
        
        input.addLast("$");

        String x = "", y = "";
        String intersectionResult;

        boolean acc = false;

        do {

            x = stack.peek();
            y = input.getFirst();

            intersectionResult = getIntersection(x, y);
            
            /* -------------------------------- stuff for displaying ------------------------------- */

            // print stack content
            System.out.print("\t\t\t\t\t  stack : ");
            resultPhrases.add("\t\t\t\t\t  stack : ");
            for (int i = 0; i < stack.size(); i++) {
                System.out.print(stack.get(i) + "");
                resultPhrases.add(stack.get(i) + "");
            }
            
            if(stack.size()>=6){
              System.out.print("    \t ");
              resultPhrases.add("    \t ");
            }
            else {
              System.out.print("\t\t  ");
              resultPhrases.add("\t\t  ");
            }
            
            // print input content
            for (int i = 0; i < input.size(); i++) {
                System.out.print(input.get(i) + "");
                resultPhrases.add(input.get(i) + "");
            }

            System.out.println(" : input");
            resultPhrases.add(" : input");
            
            // --------------------------------------------- for display issues
            
            
            // to know the value of intersection
            String val;
            if (intersectionResult == null){
                
                val = "EMPTY!";
                
            } else if (intersectionResult.equals("acc")) {
               
                val = "ACCEPT";
            } else {
     
                val = intersectionResult;
            }
                
            
            
            System.out.println("\nIntersection of (" + x + ") and (" + y + ") is : " + val);
            resultPhrases.add("\nIntersection of (" + x + ") and (" + y + ") is : " + val + "\n");
            
            /* -------------------------------- stuff for displaying ------------------------------- */
            
            // check if the intersection is empty
            if (intersectionResult == null) {
                return false;
            }

            switch (intersectionResult.charAt(0)) {
                case 's':
                    doShift(intersectionResult);
                    break;

                case 'r':
                    doReduce(intersectionResult);
                    break;

                case 'a':
                    acc = true;
                    //break;
            }

        } while (acc == false);

        return acc;
    }

    public static void doShift(String in) {

       
        // push first symbol of the input into the stack
        String temp = input.removeFirst();
        stack.push(temp);

        // get the shift number by take the number after char s
        String shiftNumber = in.substring(1, in.length());

        // push the shift number into the stack
        stack.push(shiftNumber);
        
        System.out.println("Shift " + shiftNumber);
        resultPhrases.add("Shift " + shiftNumber +"\n");
    }

    public static void doReduce(String in) {
        
        // get the reduce number by take the number after char r
        String reduceNumber = in.substring(1, in.length());

        // convert reduce number into integer
        int reduceNum = Integer.parseInt(reduceNumber);

        // get the rule to reduce by
        Rule rule = grammer.get(reduceNum);

        // add the rule to the output colom
        output.add(rule);
        
        

        /* -------------- Starting reduce ------------- */
        
        // get the right Side of the rule
        String rSide = rule.getRightPart();

        int popsNumber = getSymbolsAsArray(rSide).length * 2;

        // remove from stack
        for (int i = 0; i < popsNumber; i++) {
            stack.pop();
        }

        // get the left side and add it to the stack
        String lSide = rule.getLeftPart();
        stack.add(lSide);

        // take the intersection between last two elements in the stack
        String intersectionResult;

        String x = stack.get(stack.size() - 2);
        String y = stack.peek();

        intersectionResult = getIntersection(x, y);

        // put the intersection into the stack
        stack.push(intersectionResult);
        
        System.out.println("Reduce by : " + reduceNum + ") " + lSide + " -> " + rSide);
        resultPhrases.add("Reduce by : " + reduceNum + ") " + lSide + " -> " + rSide +"\n");

    }

    public static boolean isValidSymbols(String input) {

        String ch = "";
        boolean flag = false;

        for (int i = 0; i < input.length(); i++) {

            flag = false;

            // take a symbol
            if (i < input.length() - 1) {
                if (input.charAt(i) == 'i' && input.charAt(i + 1) == 'd') {
                    ch = "id";
                    i++;
                }
            }
            
            if (input.charAt(i) != 'i' && input.charAt(i) != 'd') {
                ch = input.charAt(i) + "";
            }

            // check is it a valid symbol 
            for (int c = 0; c < valid_symbols.length; c++) {

                if (valid_symbols[c].equals(ch)) {
                    flag = true;
                    break;
                }
            }

            if (flag == false) {
                break;
            }
        }
        
        return flag;
    }

    public static String[] getSymbolsAsArray(String input) {

        String ch = "";
        ArrayList<String> symbols = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {

            // take a symbol
            if (i < input.length() - 1) {
                if (input.charAt(i) == 'i' && input.charAt(i + 1) == 'd') {
                    ch = "id";
                    i++;
                }
            }
            if (input.charAt(i) != 'i' && input.charAt(i) != 'd') {
                ch = input.charAt(i) + "";
            }

            symbols.add(ch);
        }

        // convert array list to normal array
        String[] result = symbols.toArray(new String[symbols.size()]);

        return result;
    }

    public static String getIntersection(String i, String Symbol) {

        int row = Integer.parseInt(i);

        int col = 0;
        switch (Symbol) {
            case "id":
                col = 1;
                break;
            case "*":
                col = 2;
                break;
            case "=":
                col = 3;
                break;
            case "$":
                col = 4;
                break;
            case "S":
                col = 5;
                break;
            case "L":
                col = 6;
                break;
            case "R":
                col = 7;
                break;
            default:
                break;
        }

        return LR1_table[row][col];
    }

    // use it to show the derivation for the correct input
    public static void showDerivation(String input) {

        for (int i = 0; i < output.size(); i++) {
            System.out.println(output.get(i).getLeftPart() + " -> " + output.get(i).getRightPart());
            resultPhrases.add("\n"+output.get(i).getLeftPart() + " -> " + output.get(i).getRightPart());
        }
    }

    public static void printGrammer() {

        for (int i = 1; i <= grammer.size(); i++) {
            System.out.println(i + "- " + grammer.get(i).getLeftPart() + " -> " + grammer.get(i).getRightPart());
        }
        
    }
}
