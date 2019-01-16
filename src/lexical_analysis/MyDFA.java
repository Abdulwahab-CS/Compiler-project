
package lexical_analysis;

import java.util.LinkedList;

public class MyDFA {
    
    // Date members
    private LinkedList<DFA_state> myDFA;
    
    
    // Constructor
    MyDFA() {
        this.myDFA = new LinkedList<DFA_state>();
    }
    
    // Setters and Getters
    public void setMyDFA(LinkedList<DFA_state> myDFA) {
        this.myDFA = myDFA;
    }
    
    public LinkedList<DFA_state> getMyDFA() {
        return myDFA;
    }
}
