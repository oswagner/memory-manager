package entity;

/*
 * Requisição de liberação de memória
 */

public class DeallocationMemoryRequest extends MemoryRequest {

	private int blockId; // identificador do bloco que deve ser liberado

	public DeallocationMemoryRequest(MemoryRequestType type, int blockId) {
		super(type);
		this.blockId = blockId;
	}

	public int getBlockId() {
		return blockId;
	}
}
