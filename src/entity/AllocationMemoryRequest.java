package entity;

public class AllocationMemoryRequest extends MemoryRequest {

	public AllocationMemoryRequest(MemoryRequestType type, int size, int memoryId) {
		super(type, size);
		super.memoryId = memoryId;
	}

}