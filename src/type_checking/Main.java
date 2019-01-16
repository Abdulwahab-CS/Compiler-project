
package type_checking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    
    // Main Scope
    static ArrayList<Item> scope = new ArrayList();
    
    // container all senetence that will be shown to the user
    static ArrayList<String> resultsPhrases = new ArrayList();
    
    static boolean ErrorFlag = false;
  
    
    public static void main(String[] args) {
    

        MainFrame m = new MainFrame();
        m.setVisible(true);
        m.setLocationRelativeTo(null);
    
        
    /* // to run the program in the console. also you have to uncomment all the (system.out.println() ) phrases from entire program
        
        System.out.println("------------------------------------ Some Rules you have to follow when you're writing a program ------------------------------------ \n");
        System.out.println("1- Assuming you are in main function.");
        System.out.println("2- DO NOT type in a space unless you need it, EXTRA SPACES IS DISALLOWED");
        System.out.println("3- Each line can have at most ONE operation. except the following form : a=b+c");
        System.out.println("4- No decleration and initilization within the same line");
        System.out.println("5- Don't use the reserved words (int, double, String, boolean) OR reserved symbols (+, =) as a values");
        
        System.out.println("\nEnter your program : ");
        System.out.println("---------------------------------------------------");
        

        
        String fileName = "input_program.txt";  

        // ------------------------- start writing the input program to text file
              
        try {
            PrintWriter outputStream = new PrintWriter(fileName);

            String line;
            int lineCount = 0;
            Scanner keyboard = new Scanner(System.in);

            while (keyboard.hasNextLine()) {

              line = keyboard.nextLine();
              lineCount++;

              if (line.isEmpty()) break;

              outputStream.println(lineCount + " - " + line);
            }
            
            outputStream.close(); // close the file
            
            
        } catch (FileNotFoundException ex) {
            System.out.println("something went wrong !!");
        }
        // ---------------------------- end writing the input program to text file
        
        
        // printing the input program into console
        System.out.println("Your Program : ");
        System.out.println("----------------------------------------------------");
        print_input_program(fileName);
        System.out.println("----------------------------------------------------\n");
        
        
        System.out.println("Your Program Excution : ");
        System.out.println("----------------------------------------------------\n");
        // ---------------------------- start reading the program from the text file
          
        readFile(fileName);
        
        // ---------------------------- end reading the program from text file
            
            
       System.out.println(); 


    */        
        
       
    } // End Main Function
    
    public static void readFile(String fileName) {
        
        String tempArr[], line, lineContent;
        int lineNumber;
        
        ArrayList<String> usedRules = new ArrayList(); // list of used rules in each line 
        
        try {
            Scanner input = new Scanner(new File(fileName));
            
            // loop on the file line by line
            while(input.hasNextLine()){
                
                line = input.nextLine();
                
                // first split the line content and line number
                tempArr = line.split(" - ");
                lineNumber = Integer.parseInt(tempArr[0]);
                lineContent = tempArr[1];
                
                
                if( lineContent.contains("int ") || lineContent.contains("double ") || lineContent.contains("String ") || lineContent.contains("boolean ") ) {
                    
                    // should be a decleration
                    
                    if(isValidInitFormat(lineContent)){
                        
                        // define a variable & and add it to scope
                        defineVariable(lineContent, lineNumber);
                    } else {
                        
                        //System.out.println("\n!! Error in line " + lineNumber + ", Invalid decleration format \n");
                        resultsPhrases.add("\n!! Error in line " + lineNumber + ", Invalid decleration format \n");
                        ErrorFlag = true;
                    }

                } else if( lineContent.contains("=") ) {

                    // do assign operation & and try to give a type to this line
                    do_assign_operation(lineContent, lineNumber, usedRules);
                    
                    // ifcorrect, print used rules
                    //System.out.println("\t Used Rules : ");
                    print_usedRules(usedRules);
                    
                    usedRules.clear();

                } else if ( lineContent.contains("+") ) {

                     // do plus operation & and try to give a type to this line
                    do_plus_operation(lineContent, lineNumber, usedRules);
                    
                    // ifcorrect, print used rules
                    
                    // print used rules
                    print_usedRules(usedRules);

                    usedRules.clear();
                } else {

                    //System.out.println("\n!! Error in line " + lineNumber + " Unknown data type OR symbol is used \n");
                    resultsPhrases.add("\n!! Error in line " + lineNumber + " Unknown data type OR symbol is used \n");
                    ErrorFlag = true;
                    //exit(0);
                } 
                
            }

         } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("!! Error, File Not Found !!");
             ErrorFlag = true;
        }
        
    }
    
    public static boolean isValidInitFormat(String line) {
        
        String tempArr[] = line.split(" ");
        String left, right;
        boolean is_valid_type, is_valid_var_name;
        
        if(tempArr.length == 2){
            
            left = tempArr[0];
            right = tempArr[1];
            
            is_valid_type = isValidType(left);
            is_valid_var_name = isValidVarName(right);
            
            if(is_valid_type == true && is_valid_var_name == true) {
                return true;
            }
            
        }
        
        return false;
    }
    
    public static boolean isValidType(String type){
        if(type.equals("int") || type.equals("double") || type.equals("String") || type.equals("boolean"))
            return true;
        
        return false;
    }
    
    public static boolean isValidVarName(String var) {
        
        // Regular Expression for a variable name
        if( var.matches("^[a-zA-Z_]\\w*$") || var.matches("^[a-zA-Z_]\\w*;$") ){    //      \w = [a-zA-Z_0-9]
            return true;
        }
        
        return false;
    }
    
    public static void print_usedRules(ArrayList<String> r) {
        
        if(!ErrorFlag) {
            //System.out.println("   Used Rules : \n");
            resultsPhrases.add("   Used Rules : \n");

            // remove duplicate elements
            Set<String> hs = new HashSet<>();
            hs.addAll(r);
            r.clear();
            r.addAll(hs);

            for(int i=0; i<r.size(); i++) {
                //System.out.println(" - " + r.get(i));
                resultsPhrases.add("   - " + r.get(i)+ "\n");
            } 
            System.out.println();
            resultsPhrases.add("\n");
        }
    }
    
    public static void print_input_program(String fileName){
        
        String tempArr[], line, lineContent;
        int lineNumber;
        try {
            Scanner input = new Scanner(new File(fileName));
            while(input.hasNextLine()){
                
                line = input.nextLine();
                
                // first split the line content and line number
                tempArr = line.split(" - ");
                lineNumber = Integer.parseInt(tempArr[0]);
                lineContent = tempArr[1];

                System.out.println(lineNumber + " - " + lineContent);    
                
            }

         } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("!! Error, File Not Found !!");
             ErrorFlag = true;
        }
      
    }

    public static void defineVariable(String line, int lineNum) {
        
        String tempArr []; 
        
        // remove the ( ; ) from the last
        if(line.charAt(line.length()-1) == ';')
            line = line.substring(0, line.length()-1);

        // create an item if the variable name is allowed
        tempArr = line.split(" ");
        
        String variable = tempArr[1];
        String type = tempArr[0];
        
        if( variable.equals("int") || variable.equals("double") || variable.equals("boolean") || variable.equals("String")) {
            
            //System.out.println("\n!! Error in variable name, Don't use reserved keywords as variable names \n");
            resultsPhrases.add("\n!! Error in variable name, Don't use reserved keywords as variable names \n");
            ErrorFlag = true;
            
            //exit(0);
        }
        else
        {
            // create an item and add it to the scope
            Item item = new Item(variable, type);
            
            scope.add(item);
            //System.out.println("\n-> line " + lineNum + ", variable : " + tempArr[1] + ", has been defined successfuly  \n");
            resultsPhrases.add("\n-> line " + lineNum + ", variable " + tempArr[1] + ", has been defined successfuly  \n");
        }
    }
    
    public static void do_assign_operation(String line, int lineNumber, ArrayList<String> usedRules) {
        // we use the line count just to displaying the line number, in Error case
        // add assignment rule to the list of used rules
        usedRules.add("Assignment rule");
        
        String tempArr []; 
        String leftPart, rightPart;
        
        // remove the ( ; ) from the last if it's there
        if(line.charAt(line.length()-1) == ';')
            line = line.substring(0, line.length()-1);

        // determine the left part and the right part
        tempArr = line.split("=");
        leftPart = tempArr[0];
        rightPart = tempArr[1];
        
        // the left part is an identifire that it should be defined in the (scope), check that
        if(!checkInScope(leftPart)) {
            
            //System.out.println("\n\n Type Error in line ( " + lineNumber + " ) : " + leftPart + " is NOT defined !! \n");
            resultsPhrases.add("\n\n Type Error in line ( " + lineNumber + " ) : " + leftPart + " is NOT defined !! \n");
            ErrorFlag = true;
            //exit(0);
            
        } else {
            
           
            
            // ----------------------------- Start applying the assignment rule --------------------------
            
            // get the type of left part of assignment , must be in the scope
            String T1 = get_IdentType_fromScope(leftPart);
            
            
            // in case no (+) operation in the right part
            if( !rightPart.contains("+") ) {
                
                // if the right part is a direct value , not another variable !
                if(isVariableName(rightPart)){
                    
                    // get the type of the right part. can be (int, double, String or boolean)
                    String T2 = get_IdentType_fromScope(rightPart); 
                    //System.out.println("--------------> Type T1: "+ T1);
                    //System.out.println("--------------> Type T2: "+ T2);
                    
                    boolean result = is_T2_lessThatOrEqual_T1(T1, T2);

                    if(result == true) {

                        // all assignment conditions all verified. so, S |- e1 = e2 : T2
                        //System.out.println("\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T2);
                        resultsPhrases.add("\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T2 + "\n");
                    } else {

                        //System.out.println("\n!! Type Error in line " + lineNumber + ", Types of two variables are different. Error happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        resultsPhrases.add("\n!! Type Error in line " + lineNumber + ", Types of two variables are different. \nError happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        //exit(0);
                        ErrorFlag = true;
                        
                    }
                    
                }
                else // in case the right part is not a current variable name
                {
                    // get the type of the right part. can be (int, double, String or boolean)
                    String T2 = getTextType(rightPart, lineNumber, usedRules); 

                    // then complete the assignment rule .. check wether T2 <= T1 
                    // T2 = Type of right part, T1 = type of left part

                    //System.out.println("-----------------> T1=" + T1);
                    //System.out.println("-----------------> T2=" + T2);
                    boolean result = is_T2_lessThatOrEqual_T1(T1, T2);

                    if(result == true) {

                        // all assignment conditions all verified. so, S |- e1 = e2 : T2
                        //System.out.println("\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T2);
                        resultsPhrases.add("\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T2 + "\n");
                    } else {

                        //System.out.println("\n!! Type Error in line " + lineNumber + ", Error happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        resultsPhrases.add("\n!! Type Error in line " + lineNumber + ", Types of two variables are different. Error happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        //exit(0);
                        ErrorFlag = true;
                    }

                }
                
                
                
         
              // if the right part of the assignment has a (+) operation, + must compute it first, then assignment   
            } else if( rightPart.contains("+") ) {
                
                // start applying the (+) rule, then apply the (=) rule
                String type_of_plusOp  = do_plus_operation(rightPart, lineNumber, usedRules);
                String demoText;
                
                if(!type_of_plusOp.equals("null")){
                    
                    switch(type_of_plusOp) {
                        case "int":
                            demoText = "5"; // any integer value;
                            break;
                        case "double": 
                            demoText = "5.2"; // any double value
                            break;
                        case "String": 
                            demoText = "Some Text";
                            break;
                        case "boolean": 
                            demoText = "true";
                            break;
                        default: 
                            demoText = "null";
                    }
                    
                    
                    
                    // Now, do the assign operation
                    do_assign_operation2(leftPart, demoText, lineNumber, usedRules);
                    
                    
                    
                } else {
                    //System.out.println("\n!! Type Error in line " + lineNumber + ", Error happend from Plus Rule \n");
                    resultsPhrases.add("\n!! Type Error in line " + lineNumber + ", Error happend from Plus Rule \n");
                    //exit(0);
                    ErrorFlag = true;
                }
                
            }
             
        }
        
        
    }
    
    
    public static String do_plus_operation(String line, int lineNumber, ArrayList<String> usedRules) {
        
        // we use the line count just to displaying the line number, in Error case
        
        
        String leftPart, rightPart;
        String T1, T2;
        String resultType = "null"; // if the operation iscorrect, the type either be 'int' or 'double' otherwise, null
        
        // remove the ( ; ) from the last if it's there
        if(line.charAt(line.length()-1) == ';')
            line = line.substring(0, line.length()-1);

        
        // determine the left part and the right part
        int indexOfPlusOperator = line.indexOf("+");
        leftPart = line.substring(0, indexOfPlusOperator);
        rightPart = line.substring(indexOfPlusOperator+1, line.length());
        
        // inside the getTextType, i checked if the text is a variable name or not
        T1 = getTextType(leftPart, lineNumber, usedRules);
        T2 = getTextType(rightPart, lineNumber, usedRules);
        
        boolean result = is_T1_equal_T2(T1, T2); // T1, T2 can be either 'int' or 'double' to apply the plus operation
        if(result == true && ( (T1.equals("int")) || (T1.equals("double")) ) ) {
            
            if(T1.equals("int")) {
                usedRules.add("Addition of (int) Rule");
                resultType = "int";
                
            } else if(T1.equals("double")) {
                
                usedRules.add("Addition of (double) Rule");
                resultType = "double";
            }
            
            //System.out.println("\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T1);
            resultsPhrases.add("\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T1 + "\n");
            
        } else if (result == true && ( (T1.equals("String")) || (T1.equals("boolean")) )) {
            
            //System.out.println("\n!! Type Error in line " + lineNumber + ", Error happend from Plus Rule, You can't add two values of type : " + T1 + "\n");
            resultsPhrases.add("\n!! Type Error in line " + lineNumber + ", Error happend from Plus Rule, You can't add two values of type : " + T1 + "\n");
            //exit(0);
            ErrorFlag = true;
            
        } else if(result == false) {
            
            //System.out.println("\n!! Type Error in line " + lineNumber + ", Error happend from Plus Rule, We can't fine a TYPE! of this line\n");
            resultsPhrases.add("\n!! Type Error in line " + lineNumber + ", Error happend from Plus Rule, We can't find a TYPE! of this line\n");
            //exit(0);
            ErrorFlag = true;
            
        }
        
        return resultType;
    }  
    
    
    public static String do_assign_operation2(String leftPart, String rightPart, int lineNumber, ArrayList<String> usedRules) {
        
        // we use the line count just to displaying the line number, in Error case
        // add assignment rule to the list of used rules
        
        //usedRules.add("Assignment rule");
        
        String resultType = "null"; // if the operation iscorrect, the type either be 'int' or 'double' otherwise, null
       
        // remove the ( ; ) from the last if it's there
        if(rightPart.charAt(rightPart.length()-1) == ';')
            rightPart = rightPart.substring(0, rightPart.length()-1);

        
        // the left part is an identifire that it should be defined in the (scope), check that
        if(!checkInScope(leftPart)) {
            
            //System.out.println("\n\n Type Error in line ( " + lineNumber + " ) : " + leftPart + " is NOT defined !! \n");
            resultsPhrases.add("\n\n Type Error in line ( " + lineNumber + " ) : " + leftPart + " is NOT defined !! \n");
            //exit(0);
            ErrorFlag = true;
            
        } else {
            
            // ----------------------------- Start applying the assignment rule --------------------------
            
            // get the type of left part of assignment , must be in the scope
            String T1 = get_IdentType_fromScope(leftPart);
            
            
            // in case no (+) operation in the right part
            if( !rightPart.contains("+") ) {
                
                
                // if the right part is a direct value , not another variable !
                if(isVariableName(rightPart)){
                    
                    // get the type of the right part. can be (int, double, String or boolean)
                    String T2 = get_IdentType_fromScope(rightPart); 
                    //System.out.println("--------------> Type T1: "+ T1);
                    //System.out.println("--------------> Type T2: "+ T2);
                    
                    boolean result = is_T2_lessThatOrEqual_T1(T1, T2);

                    if(result == true) {

                        // all assignment conditions all verified. so, S |- e1 = e2 : T2
                        //System.out.println("------\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T2);
                        resultType = T2;
                        
                    } else {

                        //System.out.println("\n!! Type Error in line " + lineNumber + ", Types of two variables are different. Error happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        resultsPhrases.add("\n!! Type Error in line " + lineNumber + ", Types of two variables are different. Error happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        //exit(0);
                        ErrorFlag = true;
                    }
                    
                }
                else // in case the right part is not a current variable name
                {
                    // get the type of the right part. can be (int, double, String or boolean)
                    String T2 = getTextType(rightPart, lineNumber, usedRules); 

                    // then complete the assignment rule .. check wether T2 <= T1 
                    // T2 = Type of right part, T1 = type of left part

                    //System.out.println("-----------------> T1=" + T1);
                    //System.out.println("-----------------> T2=" + T2);
                    boolean result = is_T2_lessThatOrEqual_T1(T1, T2);

                    if(result == true) {

                        // all assignment conditions all verified. so, S |- e1 = e2 : T2
                        //System.out.println("------\n-> line " + lineNumber + ", is correct, We can get a TYPE of this line which is : " + T2);
                        resultType = T2;
                        
                    } else {

                        //System.out.println("\n!! Type Error in line " + lineNumber + ", Error happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        resultsPhrases.add("\n!! Type Error in line " + lineNumber + ", Error happend from Assignment Rule, We can't assign a TYPE! to this line\n");
                        //exit(0);
                        ErrorFlag = true;
                    }

                }
                
                
                
         
              // if the right part of the assignment has a (+) operation, + must compute it first, then assignment   
            } else if( rightPart.contains("+") ) {
                
                /* not used code ..
                
                // start applying the (+) rule, then apply the (=) rule
                String type_of_plusOp  = do_plus_operation(rightPart, lineNumber, usedRules);
                
                if(!type_of_plusOp.equals("null")){
                         
                    
                } else {
                    System.out.println("\n!! Type Error in line " + lineNumber + ", Error happend from Plus Rule \n");
                    //exit(0);
                }

                */
                
            }
             
        }
        
        
        return resultType;
    }
    
    public static boolean checkInScope(Object var) {
        boolean flag = false;
        
        for(int i=0; i<scope.size(); i++){
            if(scope.get(i).getIdent().equals(var)){
                flag = true;
            }
        }
        return flag;
    }
    
    // return a type of some text in the program. if the text is variable name, return the variable type. if text is not variable name, return the type of it (int, double, String boolean)
    public static String getTextType(String val, int lineCount, ArrayList<String> usedRules) {
        String type = "null";
        
        if(isVariableName(val)){
            
            type = getType_of_variable(val);
        }
        else
        {
            if(isInt(val)) {
                type = "int";
                usedRules.add("int constant Rule");
                
            } else if(isDouble(val)) {
                type = "double";
                usedRules.add("double constant Rule");
                
            } else if(isBoolean(val)) {
                type = "boolean";
                usedRules.add("boolean constant Rule");
                
            } else if(isString(val)) {
                type = "String";
                usedRules.add("String constant Rule");
                
            } else {

                // Error !! Type Not Defined
                //System.out.println("\n !! Error, Undefined Type of " + val + " in line " + lineCount + "\n");
                resultsPhrases.add("\n !! Error, Undefined Type of " + val + " in line " + lineCount + "\n");
                //exit(0);
                ErrorFlag = true;
            }

           
        }
        
     
        return type;
    }
    
    public static boolean isInt(String x) {
        try {  
          int d = Integer.parseInt(x);  
        }  
        catch(NumberFormatException nfe) {  
          return false;  
        }  
        return true;  
    }
    
    public static boolean isDouble(String x) {
        try {  
          double d = Double.parseDouble(x);  
        }  
        catch(NumberFormatException nfe) {  
          return false;  
        }  
        return true;  
    }
    
    public static boolean isBoolean(String x) {
        if(x.equals("true") || x.equals("false"))
            return true;
        else
            return false;
    }
    
    public static boolean isString(String x) {
        String r = x.getClass().getName();  // because it will return : Java.lang.TYPE
        r = r.substring(10);
        
        if(r.equals("String"))
            return true;
        else
            return false;
    }

    // T1 is the type of the left hand side of the assignment operations , remember the T1 in the assignment rule
    public static String get_IdentType_fromScope(String ident) {
        String r = null;
        
        for(int i=0; i<scope.size(); i++){
            if(scope.get(i).getIdent().equals(ident)) {
                r = scope.get(i).getType();
            }
        }
        return r;
    }
    
    // the last condition for the assignment rule
    public static boolean is_T2_lessThatOrEqual_T1(String T1, String T2){
        
        if( (T2.equals("int") && T1.equals("double")) || (T2.equals("int") && T1.equals("int")) || (T2.equals("double") && T1.equals("double")) )
            return true;
        if( (T2.equals("boolean") && T1.equals("boolean")) || (T2.equals("String") && T1.equals("String")) )
            return true;
        
        return false;
    }
    
    public static boolean is_T1_equal_T2(String T1, String T2) {
        
        if( (T1.equals("int") && T2.equals("int")) || (T1.equals("double") && T2.equals("double")) || (T1.equals("String") && T2.equals("String")) || (T1.equals("boolean") && T2.equals("boolean")) )
            return true;
        
        return false;
    }
    
    public static boolean isVariableName(String rp) {
        
        // check if there is an exist variable with same name in scope
        for(int i=0; i<scope.size(); i++){
            if(scope.get(i).getIdent().equals(rp))
                return true;
        }
        
        return false;
    }
    
    public static String getType_of_variable(String rp) {
        
        for(int i=0; i<scope.size(); i++){
            if(scope.get(i).getIdent().equals(rp))
                return scope.get(i).getType();
        }
        return null;
    }
    
    
} // End of class

