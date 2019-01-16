
package lexical_analysis;

import static lexical_analysis.Main.addLink_NFA;

public class Utilities {
    
    public static void addCharacterToNFA(char ch) {
        NFA_state s0 = new NFA_state();
        NFA_state s1 = new NFA_state();

        // inside hash map of s0, add link between s0 to s1 with action c
        addLink_NFA(s0, s1, ch);
        
        // create an NFA to the char c
        MyNFA temp_NFA = new MyNFA();
        
        temp_NFA.getMyNFA().addFirst(s0);
        temp_NFA.getMyNFA().addLast(s1);
        
        // push the NFA into NFA_stack
        Main.NFA_stack.push(temp_NFA);
    }
    
    public static void performOperation() {
        
        char x = Main.operator_stack.pop();
        
        switch(x) {
            case '|':
                doUnion();
                break;
            
            case '.':
                doConcatination();
                break;
                
            case '*':
                doStar();
                break;
                
            case '+':
                doPlus();
                break;
                
            default:
                System.out.println("Unknow symbol !!");
                System.exit(1);
        }
    }
    
    public static boolean precedanceRules(char c1, char c2) {
        if(c1==c2)       return true; 
        if(c1 == '*') 	return false;	
        if(c2 == '*')  	return true;	
        if(c1 == '+') 	return false;	
        if(c2 == '+')  	return true;	
        if(c1 == '.') 	return false;	
        if(c2 == '.') 	return true;	
        if(c1 == '|') 	return false;	 
        else            return true;	
    }
    
    public static void doUnion() {
        MyNFA nfa_2 = Main.NFA_stack.pop();
        MyNFA nfa_1 = Main.NFA_stack.pop();
        
        // create two states
        NFA_state startState = new NFA_state();
        NFA_state endState = new NFA_state();
        
        // add links to endState
        addLink_NFA(nfa_2.getMyNFA().getLast(), endState, '~');
        addLink_NFA(nfa_1.getMyNFA().getLast(), endState, '~');
        
        // add links from startState
        addLink_NFA(startState, nfa_2.getMyNFA().getFirst(), '~');
        addLink_NFA(startState, nfa_1.getMyNFA().getFirst(), '~');
        
        // add the actual states (startState, endState) to nfa_1 and nfa_2
        nfa_1.getMyNFA().addFirst(startState);
        nfa_2.getMyNFA().addLast(endState);
        
        // Now concatinate nfa_2 with nfa_1 to make 1 nfa
        // add each state in nfa_2 to nfa_1 in order
        for(NFA_state s : nfa_2.getMyNFA()) {
            nfa_1.getMyNFA().addLast(s);
        }
        
        // add the final nfa to the NFA_stack
        Main.NFA_stack.push(nfa_1);
        
    }
    
    public static void doConcatination() {
            
            MyNFA nfa_2 = Main.NFA_stack.pop();
            MyNFA nfa_1 = Main.NFA_stack.pop();

            // move transitions information from 1st node in nfa_2 to last node in nfa_1
            nfa_1.getMyNFA().getLast().setNextStates(nfa_2.getMyNFA().getFirst().getNextStates());
            
            // remove the first node in nfa_2 to make concatincation
            nfa_2.getMyNFA().removeFirst();
            
            // Now concatinate 2nd nfa with 1st nfa to make 1 nfa
            // add each state in nfa_2 to nfa_1 in order
            for(NFA_state s : nfa_2.getMyNFA()) {
                nfa_1.getMyNFA().addLast(s);
            }
   
            // add the final nfa to the NFA_stack
            Main.NFA_stack.push(nfa_1);
            
    }
    
    public static void doStar() {
        MyNFA n = Main.NFA_stack.pop();
        
        // create startState and endState
        NFA_state start = new NFA_state();
        NFA_state end = new NFA_state();
        
        // add link from old end state to old start state
        addLink_NFA(n.getMyNFA().getLast(), n.getMyNFA().getFirst(), '~');
        
        // add link to new end State
        addLink_NFA(n.getMyNFA().getLast(), end, '~');
        
        // add links from new start State to old start state
        addLink_NFA(start, end, '~');
        addLink_NFA(start, n.getMyNFA().getFirst(), '~');
        
        // add the actual states into the nfa
        n.getMyNFA().addFirst(start);
        n.getMyNFA().addLast(end);
        
        // add the nfa back to the NFA_stack
        Main.NFA_stack.push(n);
        
    }
    
    public static void doPlus() {
        MyNFA n = Main.NFA_stack.pop();
        
        // create startState and endState
        NFA_state start = new NFA_state();
        NFA_state end = new NFA_state();
        
        // add link from old end state to old start state
        addLink_NFA(n.getMyNFA().getLast(), n.getMyNFA().getFirst(), '~');
        
        // add link to new end State
        addLink_NFA(n.getMyNFA().getLast(), end, '~');
        
        // add links from new start State to old start state
        // addLink_NFA(start, end, '~');   
        addLink_NFA(start, n.getMyNFA().getFirst(), '~');
        
        // add the actual states into the nfa
        n.getMyNFA().addFirst(start);
        n.getMyNFA().addLast(end);
        
        // add the nfa back to the NFA_stack
        Main.NFA_stack.push(n);
        
    }
    
    
    public static String addConcatination(String RE) {
        String resultRE = "";
        
        for(int i=0; i<RE.length()-1; i++) {
            if( isInputChar(RE.charAt(i)) && isInputChar(RE.charAt(i+1)) ) {
                resultRE += RE.charAt(i) + ".";
 
            } else if( isInputChar(RE.charAt(i)) && RE.charAt(i+1) == '(' ) {
                resultRE += RE.charAt(i) + "."; 
                
            } else if( RE.charAt(i) == ')' && isInputChar(RE.charAt(i+1)) ) {
                resultRE += RE.charAt(i) + "."; 
                
            } else if( RE.charAt(i) == '*' && isInputChar(RE.charAt(i+1)) ) {
                resultRE += RE.charAt(i) + "."; 
                
            } else if( RE.charAt(i) == '*' && RE.charAt(i+1) == '(' ) {
                resultRE += RE.charAt(i) + "."; 
                
            } else if( RE.charAt(i) == '+' && isInputChar(RE.charAt(i+1)) ) {
                resultRE += RE.charAt(i) + "."; 
                
            } else if( RE.charAt(i) == '+' && RE.charAt(i+1) == '(' ) {
                resultRE += RE.charAt(i) + ".";
                
            } else if( RE.charAt(i) == ')' && RE.charAt(i+1) == '(' ) {
                resultRE += RE.charAt(i) + ".";
                
            } else {
                resultRE += RE.charAt(i);
            }
        }
        
        // add the last character to the new result  
        resultRE += RE.charAt(RE.length() - 1);
        return resultRE;
    }
    
    
    public static boolean isInputChar(char x) {
        if(x == 'a' || x == 'b' || x == 'c' || x == 'd' || x == 'e' || x == 'f' || x == 'g'
           || x == 'h' || x == 'i' || x == 'j' || x == 'k' || x == 'l' || x == 'm' || x == 'n' 
           || x == 'o' || x == 'p' || x == 'q' || x == 'r' || x == 's' || x == 't' || x == 'u'
           || x == 'v' || x == 'w' || x == 'x' || x == 'y' || x == 'z') 
            return true;
        else
            return false;
    }
    
    // ----------------------------------------------------------------------------------------------- DFA
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
