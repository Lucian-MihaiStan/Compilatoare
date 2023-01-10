public class IdSymbol extends Symbol {
    // Fiecare identificator posedă un tip.
    protected TypeSymbol type;

    /*
     * TODO 5: definiți un parametru care să indice dacă:
     * 	simbolul e global
     * 			sau
     *  simbolul e al unui formal, și offset-ul față de $fp
     */
    int offset;
    boolean IsFormal;

    public boolean isFormal() {
        return IsFormal;
    }

    public void setFormal(boolean formal) {
        IsFormal = formal;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public IdSymbol(String name) {
        super(name);
    }

    public void setType(TypeSymbol type) {
        this.type = type;
    }

    public TypeSymbol getType() {
        return type;
    }
}