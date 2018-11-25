package entity;

/*
 * Bloco de memória
 */
public class MemoryBlock {

	private int id; // identificador
	private int size; // tamanho do bloco

	private int initialPosition; // posição inicial na memória principal
	private int finalPosition; // posição final na memória principal

	public MemoryBlock(int id, int size, int initialPosition, int finalPosition) {
		this.id = id;
		this.size = size;
		this.initialPosition = initialPosition;
		this.finalPosition = finalPosition;
	}

	public int getInitialPosition() {
		return initialPosition;
	}

	public int getFinalPosition() {
		return finalPosition;
	}

	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	public void setFinalPosition(int finalPosition) {
		this.finalPosition = finalPosition;
	}

	public void setInitialPosition(int initialPosition) {
		this.initialPosition = initialPosition;
	}
}
