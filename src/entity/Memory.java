package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Classe que representa a memória
 * Contem a marcação de segmentos de memória ocupados e livres
 * Contem listas de blocos que foram alocados e lista de blocos que já foram liberados
 */
public class Memory {

	// marcadores de utilização da memória
	static final int FREE_MEMORY = 0; 
	static final int USED_MEMORY = 1;
	
	// espaço da memória que não é utilizado para alocação de processos, é um espaço resarvado ao próprio sistema operacional
	static final int RESERVED_MEMORY = -1; 

	private int key = 1; // chave de controle para identificação dos blocos 

	private int memorySize; // tamanho total da memória
	private int initialPosition; // inicio da área útil da memória
	private int finalPosition; // final da área útil da memória

	private int[] mainMemory; // memória principal 

	private ArrayList<MemoryBlock> memoryBlocks; // lista de blocos utilizados
	private ArrayList<MemoryBlock> memoryFreeBlocks; // lista de blocos que já foram liberados

	public Memory(int initialPosition, int finalPosition) {
		this.initialPosition = initialPosition;
		this.finalPosition = finalPosition;
		this.memorySize = (finalPosition - initialPosition);
		this.mainMemory = new int[finalPosition];
		this.memoryBlocks = new ArrayList<>();
		this.memoryFreeBlocks = new ArrayList<>();
		initializeMemory();
	}

	//	processo de inicialização da memória
	private void initializeMemory() {
		// Demarcação da  área útil 
		for (int i = 0; i < initialPosition && i < mainMemory.length; i++) {
			mainMemory[i] = RESERVED_MEMORY;
		}
		// adicionando marcadores de memória liberada
		for (int i = initialPosition; i < mainMemory.length; i++) {
			mainMemory[i] = FREE_MEMORY;
		}
		
		System.out.println("== Memory Manager Initialized ==");
	}

	public int getInitialPosition() {
		return initialPosition;
	}

	public int getFinalPosition() {
		return finalPosition;
	}

	public int getMemorySize() {
		return memorySize;
	}

	// gera novo idenficador de bloco
	private int getMemoryBlockId() {
		return key++;
	}
	
	// adiciona na lista de blocos um novo bloco que foi alocado
	private void addMemoryBlock(MemoryBlock block) {
		memoryBlocks.add(block);

	}

	// adiciona os marcadores na memória principal
	private void fulfillMainMemory(int initialPosition, int finalPosition, int memoryMarker) {
		for (int i = initialPosition; i <= finalPosition; i++) {
			mainMemory[i] = memoryMarker;
		}
	}

	// processo de descoberta e alocação na memória principal
	public boolean alloc(AllocationMemoryRequest allocation) {
		int blockSize = allocation.getBlockSize(); // tamanho de bloco que precisa ser alocado
		int count = 0; // contador de segmentos de memória contíguos
		int initialFreeIndex = -1; // marcadore da posição inicial de segmentos livre contíguos  
		boolean keepCount = false; // flag para manter a contagem de segmentos livres contíguos
		boolean firstFitFinded = false; // flag de identificação de espaço contíguo na memória príncipal para alocar a requisição atual
		int maxIndex = initialFreeIndex + count; // indice do último segmento de memória (informação fornecida para o bloco alocado)

		int i = initialPosition;
		for (; i < finalPosition; i++) {
			if (mainMemory[i] == FREE_MEMORY) {
				initialFreeIndex = i;
				keepCount = true;
				while (keepCount) {
					count++; // incrementa número de segmentos livres contíguos
					maxIndex = initialFreeIndex + count; // atualiza possível índice final
					if (count >= blockSize || maxIndex >= finalPosition || mainMemory[maxIndex] == USED_MEMORY) {
						keepCount = false;
						break; // encerra a contagem de segmentos livres contíguos 
					}
				}

				if (count >= blockSize) { // verifica se o bloco cabe no espaço calculado acima
					firstFitFinded = true;
					break;
				}

				// caminha com a busca para os próximos segmentos após os que já foram contados anteriormente
				if (maxIndex >= finalPosition || mainMemory[maxIndex] == USED_MEMORY) {   
					i = maxIndex;
					count = 0;
					initialFreeIndex = -1;
				}
			}
		}
		
		// se ao ser encontrado o primeiro espaço livre contíguo que suporta o tamanho do bloco então ele é alocado 
		if (firstFitFinded) {
			MemoryBlock memoryBlock = new MemoryBlock(getMemoryBlockId(), blockSize, initialFreeIndex, maxIndex); // criação de bloco alocado
			addMemoryBlock(memoryBlock); // adição de bloco alocado na lista de blocos sem utilizados 
			fulfillMainMemory(memoryBlock.getInitialPosition(), memoryBlock.getFinalPosition(), USED_MEMORY); // marcação na memória principal
		}
		
		return firstFitFinded; // feedback da memória para o gerente se houve ou não uma alocação 
	}

	// liberação na memória
	public void free(DeallocationMemoryRequest deallocation) {
		int blockId = deallocation.getBlockId();
		// busca na lista de blocos alocados pelo bloco que será liberado
		MemoryBlock removedBlock = memoryBlocks.stream().filter(memBlock -> memBlock.getId() == blockId).findFirst().get();
		memoryBlocks.remove(removedBlock); // remoção do bloco da lista de blocos utilizados
		memoryFreeBlocks.add(removedBlock);// adição do bloco na lista de blocos já liberados
		
		fulfillMainMemory(removedBlock.getInitialPosition(), removedBlock.getFinalPosition(), FREE_MEMORY); // marcação da liberação na memória principal
	}

	// report do status da memória
	public String status() {
		StringBuilder builder = new StringBuilder();

		builder.append("============= Memory Status =============").append("\n");
		builder.append("Allocated Blocks = ").append(memoryBlocks.size()).append("\n");
		for (MemoryBlock block : memoryBlocks) {
			builder.append("============================").append("\n");
			builder.append("Block = ").append(block.getId()).append(" - ").append("Size = ").append(block.getSize())
					.append("\n");
			builder.append(block.getInitialPosition()).append(" - ").append(block.getFinalPosition()).append("\n");
		}
		builder.append("============================").append("\n");
		builder.append("Free Blocks = ").append(memoryFreeBlocks.size()).append("\n");
		for (MemoryBlock block : memoryFreeBlocks) {
			builder.append("============================").append("\n");
			builder.append("Block = ").append(block.getId()).append(" - ").append("Size = ").append(block.getSize())
					.append("\n");
			builder.append(block.getInitialPosition()).append(" - ").append(block.getFinalPosition()).append("\n");
		}

		return builder.toString();
	}
	
	// report de fragmentação ou falta de memória 
	public String fragmentation(int requestedBlockSize) {
		StringBuilder builder = new StringBuilder();
		int totalFreeMemory = memoryFreeBlocks.stream().map(memBlock -> memBlock.getSize()).reduce(0, (a, b) -> a + b);
		
		if (totalFreeMemory < requestedBlockSize) {
			builder.append("============= Memory Missing Status =============").append("\n");
			totalFreeMemory = countTotalFreeInMainMemory();
		} else {			
			builder.append("============= Memory Fragmentation Status =============").append("\n");
		}

		builder.append("Requested = ").append(requestedBlockSize).append("\n");
		builder.append("Free memory = ").append(totalFreeMemory).append("\n");
		builder.append("============================").append("\n");

		return builder.toString();
	}
	// contagem na memória principal por espaço total liberado
	private int countTotalFreeInMainMemory() {
		int total = 0;
		for (int i = initialPosition; i < finalPosition; i++) {
			if(mainMemory[i] ==  FREE_MEMORY)
				total++;
		}
		return total;
	}
}
