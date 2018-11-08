package entity;

public class DeallocationMemoryRequest extends MemoryRequest {

	public DeallocationMemoryRequest(MemoryRequestType type, int size, int memoryId) {
		super(type, size);
		super.memoryId = memoryId;
	}

}
