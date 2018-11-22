package entity;

public abstract class MemoryRequest {
	
	MemoryRequestType type;
	
	public MemoryRequest(MemoryRequestType type) {
		this.type = type;
	}
	
	public MemoryRequestType getType() {
		return type;
	}
}
