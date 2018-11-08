package manager;

import java.util.Queue;

import entity.MemoryParams;
import entity.MemoryRequest;
import entity.MemoryRequestType;

public class MemoryManager {

	private MemoryParams memoryParams;
	private Queue<MemoryRequest> memoryRequests;
	private int[] memory;

	public MemoryManager(MemoryParams memoryParams, Queue<MemoryRequest> memoryRequests) {
		this.memoryParams = memoryParams;
		this.memoryRequests = memoryRequests;
		prepareMemory();
	}

	public void handleRequests() {
		System.out.println("Memory");
		System.out.println(memoryParams.getInitialMemory());
		System.out.println(memoryParams.getFinalMemory());

		while (!memoryRequests.isEmpty()) {
			MemoryRequest nextRequest = memoryRequests.poll();

			if (nextRequest.getType() == MemoryRequestType.S) {
				System.out.println("Solicitação " + nextRequest.getMemoryId() + " tamanho = " + nextRequest.getSize());
				handleMemoryAllocation(nextRequest);
			}

			if (nextRequest.getType() == MemoryRequestType.L) {
				System.out.println("Libera " + nextRequest.getMemoryId());
				handleMemoryDeallocation(nextRequest);
			}
		}
	}

	private void prepareMemory() {
		int size = (int) Math.ceil((memoryParams.getFinalMemory() / 50)) + 1;
		this.memory = new int[size];

		for (int i = 0; i < memory.length; i++) {
			memory[i] = -1;
		}
	}

	private void handleMemoryAllocation(MemoryRequest nextRequest) {
		int memoryOcupationSize = nextRequest.getSize() / 50;
		memoryAlloc(nextRequest.getMemoryId(), memoryOcupationSize);
	}

	private void handleMemoryDeallocation(MemoryRequest nextRequest) {

	}

	/*
	 * Atualmente utilizando first fit TODO: parametrizar tipo de de alocação 1 -
	 * first fit 2 - best fit 3 - worst fit
	 */
	private void memoryAlloc(int memoryId, int memoryOcupationSize) {
		for (int i = 0; i < memory.length; i++) {
			if (memory[i] == -1) {
				memory[i] = memoryId;
			}
		}
	}
}
