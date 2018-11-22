package entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Memory {

	static final int FREE_MEMORY = -1;
	static final int USED_MEMORY = 1;
	static final int RESERVED_MEMORY = 999;

	private int key = 1;

	private int memorySize;
	private int initialPosition;
	private int finalPosition;

	private int[] mainMemory;

	private ArrayList<MemoryBlock> memoryBlocks;
	private ArrayList<MemoryBlock> memoryFreeBlocks;

	public Memory(int initialPosition, int finalPosition) {
		this.initialPosition = initialPosition;
		this.finalPosition = finalPosition;
		this.memorySize = (finalPosition - initialPosition);
		this.mainMemory = new int[finalPosition];
		this.memoryBlocks = new ArrayList<>();
		this.memoryFreeBlocks = new ArrayList<>();
		initializeMemory();
	}

	private void initializeMemory() {
		for (int i = 0; i < initialPosition && i < mainMemory.length; i++) {
			mainMemory[i] = RESERVED_MEMORY;
		}

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

	private int getMemoryBlockId() {
		return key++;
	}

	private void addMemoryBlock(MemoryBlock block) {
		memoryBlocks.add(block);

	}

	private void fulfillMainMemory(int initialPosition, int finalPosition, int memoryMarker) {
		for (int i = initialPosition; i <= finalPosition; i++) {
			mainMemory[i] = memoryMarker;
		}
	}

	public boolean alloc(AllocationMemoryRequest allocation) {
		int blockSize = allocation.getBlockSize();
		int count = 0;
		int initialFreeIndex = -1;
		boolean keepCount = false;
		boolean firstFitFinded = false;
		int maxIndex = initialFreeIndex + count;

		int i = initialPosition;
		for (; i < finalPosition; i++) {
			if (mainMemory[i] == FREE_MEMORY) {
				initialFreeIndex = i;
				keepCount = true;
				while (keepCount) {
					count++;
					maxIndex = initialFreeIndex + count;
					if (count >= blockSize || maxIndex >= finalPosition || mainMemory[maxIndex] == USED_MEMORY) {
						keepCount = false;
						break;
					}
				}

				if (count >= blockSize) {
					firstFitFinded = true;
					break;
				}

				if (maxIndex >= finalPosition || mainMemory[maxIndex] == USED_MEMORY) {
					i = maxIndex;
					count = 0;
					initialFreeIndex = -1;
				}
			}
		}
		
		if (firstFitFinded) {
			MemoryBlock memoryBlock = new MemoryBlock(getMemoryBlockId(), blockSize, initialFreeIndex, maxIndex);
			addMemoryBlock(memoryBlock);
			fulfillMainMemory(memoryBlock.getInitialPosition(), memoryBlock.getFinalPosition(), USED_MEMORY);
		}
		
		return firstFitFinded;
	}

	public void free(DeallocationMemoryRequest deallocation) {
		int blockId = deallocation.getBlockId();
		MemoryBlock removedBlock = memoryBlocks.stream().filter(memBlock -> memBlock.getId() == blockId).findFirst()
				.orElse(null);
		memoryBlocks.remove(removedBlock);
		memoryFreeBlocks.add(removedBlock);
//		groupFreeBlocks();
		fulfillMainMemory(removedBlock.getInitialPosition(), removedBlock.getFinalPosition(), FREE_MEMORY);
	}

	// TODO: Agrupar blocos vizinhos
	private void groupFreeBlocks() {
		List<MemoryBlock> sortedList = memoryFreeBlocks.stream().sorted(Comparator.comparing(MemoryBlock::getId))
				.collect(Collectors.toList());

		for (int i = 0; i < sortedList.size() - 1; i++) {
			MemoryBlock current = sortedList.get(i);
			MemoryBlock next = sortedList.get(i + 1);

			if (current.getFinalPosition() + 1 == next.getInitialPosition()) {
				current.setFinalPosition(next.getFinalPosition());
			}
		}
	}

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

	public String fragmentation(int requestedBlockSize) {
		StringBuilder builder = new StringBuilder();
		builder.append("============= Memory Fragmentation Status =============").append("\n");
		int totalFreeMemory = memoryFreeBlocks.stream().map(memBlock -> memBlock.getSize()).reduce(0, (a, b) -> a + b);

		builder.append("Requested = ").append(requestedBlockSize).append("\n");
		builder.append("Free memory = ").append(totalFreeMemory).append("\n");
		builder.append("============================").append("\n");

		return builder.toString();
	}
}
