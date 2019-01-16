
package lexical_analysis;

import java.util.LinkedList;

public class MyNFA {
    
    // Date members
    private LinkedList<NFA_state> myNFA;
    
    
    // Constructor
    MyNFA() {
        this.myNFA = new LinkedList<NFA_state>();
    }
    
    // Setters and Getters
    public void setMyNFA(LinkedList<NFA_state> myNFA) {
        this.myNFA = myNFA;
    }
    
    public LinkedList<NFA_state> getMyNFA() {
        return myNFA;
    }
    
    
}
