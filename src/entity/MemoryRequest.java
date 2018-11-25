package entity;

/*
 * Abstração de requisições ao gerente de memória
 */
public abstract class MemoryRequest {
	
	MemoryRequestType type;
	
	public MemoryRequest(MemoryRequestType type) {
		this.type = type;
	}
	
	public MemoryRequestType getType() {
		return type;
	}
}
