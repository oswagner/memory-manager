package entity;

/*
 * Tipos de requisições ao gerente de memória
 */
public enum MemoryRequestType {
    S("S"), // S = requisição de alocação 
    L("L"); // L = requisição de liberação

    private final String value;

    /**
     * @param value
     */
    private MemoryRequestType(final String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}