package entity;

public abstract class MemoryRequest {
	
//	String type;
	MemoryRequestType type;
	int size;
	int memoryId;
	
	public MemoryRequest(MemoryRequestType type, int size) {
		this.type = type;
		this.size = size;
	}
	
	public int getMemoryId() {
		return memoryId;
	}
	
	public MemoryRequestType getType() {
		return type;
	}
	
	public int getSize() {
		return size;
	}
}
