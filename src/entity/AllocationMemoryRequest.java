package entity;

/*
 * Requisição de alocacão de memória
 */
public class AllocationMemoryRequest extends MemoryRequest {

	private int blockSize; // informação de tamanho necessário para a alocação

	public AllocationMemoryRequest(MemoryRequestType type, int blockSize) {
		super(type);
		this.blockSize = blockSize;
	}

	public int getBlockSize() {
		return blockSize;
	}

}