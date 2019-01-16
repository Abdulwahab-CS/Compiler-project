
package lexical_analysis;


import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class NFA_state {
    
    // Data Members
    
    private static int count_ID = 0; // the global counter
    
    private int ID;
    private Map<Character, ArrayList<NFA_state>> nextStates;
    private boolean isEndState;
    
    // Constructor
    
    NFA_state() {
        this.ID = count_ID++;
        this.nextStates = new HashMap<Character, ArrayList<NFA_state>>();
        this.isEndState = false;
    }
    
    
    // Setters and Getters

    public static void setCount_ID(int count_ID) {
        NFA_state.count_ID = count_ID;
    }
    
    public void setID(int ID) {
        this.ID = ID;
    }

    public void setNextStates(Map<Character, ArrayList<NFA_state>> nextStates) {
        this.nextStates = nextStates;
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

    public Map<Character, ArrayList<NFA_state>> getNextStates() {
        return nextStates;
    }

    public boolean isIsEndState() {
        return isEndState;
    }
    
    
    
}
