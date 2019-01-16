
package lexical_analysis;

import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;



public class Main {
       

    /* NFA data */
    
    // to store the complete and final NFA
    public static MyNFA final_nfa = new MyNFA();
    
    // to keep track of NFA's that have been created during processing of converting Regular Expression to NFA
    public static Stack<MyNFA> NFA_stack = new Stack();
    
    // to keep track of operators during processing of converting Regular Expression to NFA
    public static Stack<Character> operator_stack = new Stack();
  
    /* DFA data */
    
    // to store the complete and final DFA
    public static MyDFA final_dfa = new MyDFA();
       
    /* General */
    
    public static ArrayList<String> resultsPhrases_NFA = new ArrayList<>();
    public static ArrayList<String> resultsPhrases_DFA = new ArrayList<>();
    
    public static boolean errorFlag = false;
    
    // ---------------------------------------------------------------------------------------------------------------------------------- Start main 
    public static void main(String[] args) {
        
        
        MainFrame m = new MainFrame();
        m.setVisible(true);
        m.setLocationRelativeTo(null);
        
        
        /*
        
        
        Scanner s = new Scanner(System.in);;
        String input;
        
        
        try{
            
            System.out.println("------------------------------------------------ Lexical Analysis ------------------------------------------------");
            System.out.println("Note : ");
            System.out.println("\t- Allowed characters: a - z ");
            System.out.println("\t- Allowed operators: * , ) , ( , | , + , . ");
            
            System.out.println("_________________________________________________________");
            // get Regular Expression from the user
            System.out.print("\nEnter Regular Expression : ");
            input = s.next();

            boolean result = isValidInput(input);

            if(result){
                // ----------------- NFA section --------------- 

                // Generate NFA
                final_nfa.setMyNFA(create_NFA(input).getMyNFA()); 

                
                // print the NFA graph on the console
                printOutputNFA();



                // ----------------- DFA section --------------- 

                // Generate DFA
                final_dfa.setMyDFA(create_DFA(final_nfa).getMyDFA()); 

                // print the DFA graph on the console
                printOutputDFA();
        
            } else {

                System.out.println("\n!! Error Input Format\n");
                resultsPhrases_NFA.add("!! Error Input Format");
                resultsPhrases_DFA.add("!! Error Input Format");
                //exit(0);
                errorFlag = true;
            }
            
        }catch(Exception e) {
            
            System.out.println("\n" + e.getMessage());
            System.out.println("\n!! Error Input Format OR something wrong during excution\n");
            resultsPhrases_NFA.add("!! Error Input Format OR something wrong during excutiont");
            resultsPhrases_DFA.add("!! Error Input Format OR something wrong during excution");
            errorFlag = true;
        }
      
        */
        
    }
    // -------------------------------------------------------------------------------------------------------------------------------------- End main
    
    
    // function for validating the inputs from user
    public static boolean isValidInput(String input){
        
            // match if its a valid regular expression format - by using built in method
            try {
                Pattern.compile(input);
                
            } catch (PatternSyntaxException exception) {
                
                return false;
            }
            
            if(hasStrangeSymbol(input) || hasCapitalLetters(input) || hasExtraSpaces(input)){
                
                return false;  
            }
        
        return true;
    }
    
    public static boolean hasStrangeSymbol(String s){
        
        String allSymbols = s.replaceAll("[a-zA-Z]+", ""); // remove everything apart from "a-z and A-Z";
        allSymbols = allSymbols.replaceAll("\\s+","");  // remove the spaces in between
        
        for(int i=0; i<allSymbols.length(); i++){
            if(allSymbols.charAt(i) != '(' && allSymbols.charAt(i) != ')' && allSymbols.charAt(i) != '|' && allSymbols.charAt(i) != '.' 
               && allSymbols.charAt(i) != '*' && allSymbols.charAt(i) != '+') {
                
               return true;
            }
        }
        
        return false;
    }
    
    public static boolean hasCapitalLetters(String s){
        for(int i=0; i<s.length(); i++){
            if(Character.isUpperCase(s.charAt(i))){
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasExtraSpaces(String s){
        
        for(int i=0; i<s.length(); i++){
            if(s.charAt(i) == ' ')
                return true;
        }
        return false;
    }
    
    // Create DFA
    public static MyDFA create_DFA(MyNFA nfa) {
       
       // this is the complete dfa that you should return
       MyDFA dfa = new MyDFA(); 
        
       // this list contians all dfa states that newly added to dfa, but we don't calcutate thier paths yet
       // the reason for choosing linked list because we want to remove from front later, normal array will do shifts to all elememts so, it's bad
       LinkedList<DFA_state> unFinished = new LinkedList<>(); 
        
       // get first state in NFA , we put it in a set so that we can use the function epsilon_closure which is takes a set (just for that)
       Set<NFA_state> first_nfa_state = new HashSet<>();
       first_nfa_state.add(nfa.getMyNFA().getFirst());
       
       // 1st step ) now you get all paths from first state with only epsilon actions (all paths with '~' only)
       Set<NFA_state> firstDFAState = epsilon_closure(first_nfa_state);
       
       // create first state in the DFA
       DFA_state startState = new DFA_state(firstDFAState);
       
       // add it to the final dfa 
       dfa.getMyDFA().addLast(startState);
       unFinished.add(startState);
       
       
       // Now I will add arrows in each dfa state
       while(!unFinished.isEmpty()) {
   
           
           // get last element in the list
           DFA_state state = unFinished.removeFirst();

           for(char ch='a'; ch<='z'; ch++) {
               
               Set<NFA_state> temp = move(state, ch);
               
               // in case there is empty set when you calculate the move , (avoid add arrow with epsilon)
               if(temp.isEmpty()){
                   continue;
               }
               
               Set<NFA_state> result = epsilon_closure(temp);
                             
               
               // check if the new dfa state is already in the dfa diagram or not
               boolean found = false;
               DFA_state foundedDFA = null;

               for(DFA_state t: dfa.getMyDFA()){
                   if(t.getStates().equals(result)){
                       found = true;
                       foundedDFA = t;
                       break;
                   }
               }
     
              
               if(found){
                   // Already in DFA
                   
                   // add arrow to the state itself
                   addLink_DFA(state, foundedDFA, ch);
                   

               } else {
                   // add new dfa state & link
                   DFA_state newState = new DFA_state(result);
                   addLink_DFA(state, newState, ch);
                   dfa.getMyDFA().addLast(newState);
                   
                   // add the new dfa state to the unFinished to calc its arrows
                   unFinished.addLast(newState);
               }
               
           }
           
       }
       
       // Determining the end states
       NFA_state endState = nfa.getMyNFA().getLast();
       for(DFA_state var : dfa.getMyDFA()){
           
           if(var.getStates().contains(endState)){
               var.setIsEndState(true);
           }
       }     
     
       return dfa;
    } 
    
    public static Set<NFA_state> epsilon_closure(Set<NFA_state> inputSet) {
        
        Set<NFA_state> resultSet = new HashSet<>();
        Stack<NFA_state> stack = new Stack<>();   // stack containes states to be processed // in case the node has nother path with epsillon
        
        for(NFA_state s : inputSet) {
            resultSet.add(s);
            stack.push(s);
            
            while(!stack.isEmpty()) {
                NFA_state temp = stack.pop();
                
                // get 'first arrow' only of '~' starting with temp,
                ArrayList<NFA_state> tempList = getAllLinks_NFA(temp, '~');
                
                // check if one of state '~' has another path with '~', so add it to the stack to explor it
                for(NFA_state st : tempList) {
                    
                    if(!resultSet.contains(st)){
                        resultSet.add(st);
                        stack.push(st);
                    }
                    
                }
                
            }
            
        }    

        
        return resultSet;
    }
      
    public static Set<NFA_state> move(DFA_state state, char symbol){
        // result set to be returned
        Set<NFA_state> result = new HashSet<>();
      
        // the set of states that I want to start from them to find a path with given symbol 
        Set<NFA_state> toCheckList = state.getStates();
        
        for(NFA_state st : toCheckList){
            
           ArrayList<NFA_state> allStates = getAllLinks_NFA(st, symbol);
           
           if(allStates != null) {
             for(NFA_state s : allStates) {
               result.add(s);
             }
           }
          
        }
        
        return result;
    }
  

    // creare NFA code
    public static MyNFA create_NFA(String RE) {
        
        // first add the concatination
        RE = Utilities.addConcatination(RE);
        //System.out.println("\nRegular Expressoin after adding concatination = " + RE);
        
        // Cleaning the stacks
        NFA_stack.clear();
        operator_stack.clear();
        
        
        // Loop on the Regular Expression
        for(int i=0; i<RE.length(); i++) {
            if( Utilities.isInputChar( RE.charAt(i) ) ) {
                
                // create an NFA to the character and add it to NFA_stack
                Utilities.addCharacterToNFA(RE.charAt(i));
                
            } else if( operator_stack.empty() ) {
                
                // add the operator to operator_stack
                operator_stack.add(RE.charAt(i));
                
            } else if( RE.charAt(i) == '(' ) {
                
                operator_stack.add(RE.charAt(i));
                
            } else if( RE.charAt(i) == ')' ) {
                
                while(operator_stack.get(operator_stack.size()-1) != '(' ) {
                    Utilities.performOperation();
                }
                // remove the '(' from the stack
                operator_stack.pop();
            } else {
                while( !operator_stack.isEmpty() && Utilities.precedanceRules(RE.charAt(i), operator_stack.get(operator_stack.size()-1)) ) {
                    Utilities.performOperation();
                }
                operator_stack.push(RE.charAt(i));
            }
            
            
        }   // End for loop
        
        
        // perform the remaining operations in the operator_stack 
        while(!Main.operator_stack.isEmpty()) {
            Utilities.performOperation();
        }
        
        // dertermine the end state in the final NFA
        Main.NFA_stack.get(Main.NFA_stack.size()-1).getMyNFA().getLast().setIsEndState(true);
        
        MyNFA final_NFA = Main.NFA_stack.pop();
        
        return final_NFA;
    } 


    // (( In NFA )) Add a link from state-1 to state-2 with symbol x
    public static void addLink_NFA(NFA_state s1, NFA_state s2, Character symbol) {
       
        ArrayList<NFA_state> tempList = s1.getNextStates().get(symbol); 
        
        // add the state to the array list
        if(tempList != null) {
            tempList.add(s2);
            
        } else {
            // for the first time
            tempList = new ArrayList<NFA_state>();
            tempList.add(s2);
        }
        
        // add it to the map
        s1.getNextStates().put(symbol, tempList);
    }
    
    // (( In NFA )) Add a link from state-1 to state-2 with symbol x
    public static void addLink_DFA(DFA_state s1, DFA_state s2, Character symbol) {
        ArrayList<DFA_state> tempList = s1.getNextStates().get(symbol);
        
        if(tempList != null) {
            tempList.add(s2);
            
        } else {
            tempList = new ArrayList<DFA_state>();
            tempList.add(s2);
        }
        s1.getNextStates().put(symbol, tempList);
    }
    
    // (( In NFA )) get All linkes from a state based on a specific symbol
    public static ArrayList<NFA_state> getAllLinks_NFA(NFA_state state, char symbol) {
        if(state.getNextStates().get(symbol) == null) {
            return new ArrayList<NFA_state>();
        } else {
            return state.getNextStates().get(symbol);
        }
    }
    
    // (( In DFA )) get All linkes from a state based on a specific symbol
    public static ArrayList<DFA_state> getAllLinks_DFA(DFA_state state, char symbol) {
        if(state.getNextStates().get(symbol) == null) {
            return new ArrayList<DFA_state>();
        } else {
            return state.getNextStates().get(symbol);
        }
    }
    
    
    // print section
    
    public static void printOutputNFA() {
        System.out.println("\n(( NFA )) ************************************************************\n");
        System.out.println("---------------------------------------------------------");
        resultsPhrases_NFA.add("\n");
        for(NFA_state s : final_nfa.getMyNFA()) {
                printNFAStateInfo(s);
        }
        resultsPhrases_NFA.add("\n");
    }
    
    public static void printNFAStateInfo(NFA_state s) {
                
		System.out.print("State-" + s.getID());
		resultsPhrases_NFA.add("State-" + s.getID());
                
		// to print (Start) on the first node in the NFA 
		if(final_nfa.getMyNFA().getFirst() == s) {
			System.out.print(" (Start) ");
                        resultsPhrases_NFA.add(" (Start) ");
		}
		
		// to print (End) on the end node/nodes in the NFA 
		if(s.isIsEndState() == true) {
			System.out.println(" (End) ");
                        resultsPhrases_NFA.add(" (End) \n");
		} else {
			System.out.println();
                        resultsPhrases_NFA.add("\n");
		}
		
		// print all Transitions that a state can make either with action a or b or EPSILON(e)
		System.out.print("All Transitions : ");
		resultsPhrases_NFA.add(" ---> All Transitions : ");
                
                for(char ch='a'; ch<='z'; ch++){
                    for(NFA_state x : getAllLinks_NFA(s, ch)) 
                    { 
                        System.out.print("[ "+ ch + " , S" + x.getID() + " ] ");
                        resultsPhrases_NFA.add("[ "+ ch + " , S" + x.getID() + " ] ");
                    }
                }
                // for Epsilon links
                for(NFA_state x : getAllLinks_NFA(s, '~')) { 
                    System.out.print("[ ~ , S" + x.getID() + " ] ");
                    resultsPhrases_NFA.add("[ ~ , S" + x.getID() + " ] ");
                }
                
                resultsPhrases_NFA.add("\n\n");
		System.out.println("\n---------------------------------------------------------");
	}
    
    public static void printOutputDFA() {
        System.out.println("\n(( DFA )) ************************************************************\n");
        System.out.println("---------------------------------------------------------");
        resultsPhrases_DFA.add("\n");
        for(DFA_state s : final_dfa.getMyDFA()) {
                printDFAStateInfo(s);
        }
        resultsPhrases_DFA.add("\n");
    }
    
    public static void printDFAStateInfo(DFA_state s) {
		System.out.print("State-" + s.getID());
		resultsPhrases_DFA.add("State-" + s.getID());
                
		// to print (Start) on the first node in the NFA 
		if(final_dfa.getMyDFA().getFirst() == s) {
			System.out.print(" (Start) ");
                        resultsPhrases_DFA.add(" (Start) ");
		}
		
		// to print (End) on the end node/nodes in the NFA 
		if(s.isIsEndState() == true) {
			System.out.println(" (End) ");
                        resultsPhrases_DFA.add(" (End) \n");
		} else {
			System.out.println();
                        resultsPhrases_DFA.add("\n");
		}
		
		// print all Transitions that a state can make either with action a or b or EPSILON(e)
		System.out.print("All Transitions : ");
		resultsPhrases_DFA.add(" ---> All Transitions : ");
                
                for(char ch='a'; ch<='z'; ch++){
                    for(DFA_state x : getAllLinks_DFA(s, ch)) { 
                        System.out.print("[ "+ ch + " , S" + x.getID() + " ] "); 
                        resultsPhrases_DFA.add("[ "+ ch + " , S" + x.getID() + " ] ");
                    }
                }
                // for Epsilon links
                for(DFA_state x : getAllLinks_DFA(s, '~')) { 
                    System.out.print("[ ~ , S" + x.getID() + " ] ");
                    resultsPhrases_DFA.add("[ ~ , S" + x.getID() + " ] ");
                }

		resultsPhrases_DFA.add("\n\n");
		System.out.println("\n---------------------------------------------------------");
	}
    
    public static void practiseRegEx(){
        
        Scanner s = new Scanner(System.in);;
        String input;
       
        String regex = "[ac]";  // a|c   
        while(true){
            System.out.print("Enter Regular Expression : ");
            input = s.next();
            
            if(input.matches(regex)){
                System.out.println("\n ------------> Match");
            } else {
                System.out.println("\n ------------> Not Match");
            }
            System.out.println();
        }
        
    }
    
    // ------------------------------------------------------------------------------------------------------
}
