package manager;

import java.util.LinkedList;
import java.util.Queue;

import entity.AllocationMemoryRequest;
import entity.DeallocationMemoryRequest;
import entity.Memory;
import entity.MemoryRequest;
import entity.MemoryRequestType;

public class MemoryManager {

	private Memory memory;

	Queue<AllocationMemoryRequest> pendingAllocations = new LinkedList<AllocationMemoryRequest>();

	public MemoryManager(Memory memory) {
		this.memory = memory;
	}

	public void handleRequest(MemoryRequest request) {
		if (request.getType() == MemoryRequestType.S) {
			handleMemoryAllocation((AllocationMemoryRequest) request);
		}

		if (request.getType() == MemoryRequestType.L) {
			handleMemoryDeallocation((DeallocationMemoryRequest) request);
		}
	}

	private void handleMemoryAllocation(AllocationMemoryRequest allocation) {
		boolean allocated = memory.alloc(allocation);
		if (!allocated) {
			pendingAllocations.add(allocation);
			System.out.println(memory.fragmentation(allocation.getBlockSize()));
		}
		System.out.println(memory.status());
	}

	private void handleMemoryDeallocation(DeallocationMemoryRequest deallocation) {
		memory.free(deallocation);
		if (!pendingAllocations.isEmpty()) {
			AllocationMemoryRequest pendingRequest = pendingAllocations.poll();
			if (!memory.alloc(pendingRequest)) {
				pendingAllocations.add((AllocationMemoryRequest) pendingRequest);
			}
		}
		System.out.println(memory.status());
	}

	/*
	 * TODO: parametrizar tipo de de alocação 1 - first fit 2 - best fit 3 - worst
	 * fit
	 */
}
