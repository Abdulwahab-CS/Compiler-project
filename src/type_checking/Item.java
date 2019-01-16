
package type_checking;


public class Item {
    
    // Data Members
    
    private String ident;
    private String type;
    private boolean typeFound;
    
    // Constructores
    
    Item(String ident) {
        this.ident = ident;
        this.type = null;
        this.typeFound = false;
    }
    
    Item(String ident, String type) {
        this.ident = ident;
        this.type = type;
        this.typeFound = true;
    }
    
    // Setters & Getters

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeFound(boolean typeFound) {
        this.typeFound = typeFound;
    }
    
    public String getIdent() {
        return ident;
    }

    public String getType() {
        return type;
    }

    public boolean isTypeFound() {
        return typeFound;
    }
    
    
}
