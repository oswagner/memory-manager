package entity;

public class DeallocationMemoryRequest extends MemoryRequest {

	private int blockId;

	public DeallocationMemoryRequest(MemoryRequestType type, int blockId) {
		super(type);
		this.blockId = blockId;
	}

	public int getBlockId() {
		return blockId;
	}
}
