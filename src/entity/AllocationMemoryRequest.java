package entity;

public class AllocationMemoryRequest extends MemoryRequest {

	private int blockSize;

	public AllocationMemoryRequest(MemoryRequestType type, int blockSize) {
		super(type);
		this.blockSize = blockSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

}