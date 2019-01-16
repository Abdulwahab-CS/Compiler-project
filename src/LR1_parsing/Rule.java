
package LR1_parsing;


public class Rule {
    
    private String leftPart;
    private String rightPart;

    Rule(String leftPart, String rightPart) {
        this.leftPart = leftPart;
        this.rightPart = rightPart;
    }
    
    
    
    // Setters & Getters
    
    public void setLeftPart(String leftPart) {
        this.leftPart = leftPart;
    }

    public void setRightPart(String rightPart) {
        this.rightPart = rightPart;
    }

    public String getLeftPart() {
        return leftPart;
    }

    public String getRightPart() {
        return rightPart;
    }
    
    
}
