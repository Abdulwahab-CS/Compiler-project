
package lexical_analysis;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class DFA_state {
    
    // Data Members
    
    private static int count_ID = 0;
    private int ID;
    private Map<Character, ArrayList<DFA_state>> nextStates;
    private Set<NFA_state> states;
    private boolean isEndState;

    
    // Constructor
    
    public DFA_state(Set<NFA_state> states) {
        this.ID = count_ID++;
        this.nextStates = new HashMap<>();
        this.states = states;
        this.isEndState = false;
    }
    
    
    // Setters and Getters

    public static void setCount_ID(int count_ID) {
        DFA_state.count_ID = count_ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNextStates(Map<Character, ArrayList<DFA_state>> nextStates) {
        this.nextStates = nextStates;
    }

    public void setStates(Set<NFA_state> states) {
        this.states = states;
    }

    public void setIsEndState(boolean isEndState) {
        this.isEndState = isEndState;
    }

    public static int getCount_ID() {
        return count_ID;
    }
    
    public int getID() {
        return ID;
    }

    public Map<Character, ArrayList<DFA_state>> getNextStates() {
        return nextStates;
    }

    public Set<NFA_state> getStates() {
        return states;
    }

    public boolean isIsEndState() {
        return isEndState;
    }
    
    
    
    
}
