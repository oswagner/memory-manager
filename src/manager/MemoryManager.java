package manager;

import java.util.LinkedList;
import java.util.Queue;

import entity.AllocationMemoryRequest;
import entity.DeallocationMemoryRequest;
import entity.Memory;
import entity.MemoryRequest;
import entity.MemoryRequestType;

/*
 * Classe que representa o gerentede memória, recebe as requisições de alocação e liberação de memória. 
 * Quando um pedido de alocação não pode ser atendido naquele instante, então é redirecionado a uma fila de espera
 * que está sobre o controle do gerente de memória para então quando houver uma liberação verificar a possibilidade
 * de atender as requisições pendentes 
 */
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
		boolean allocated = memory.alloc(allocation); // tentativa de alocar espaço na memória
		if (!allocated) { // se não houve espaço adiciona o processo de alocação na fila de pendentes
			pendingAllocations.add(allocation);
			System.out.println(memory.fragmentation(allocation.getBlockSize())); // imprime status de fragmentação
		}
		System.out.println(memory.status()); // imprime status da memória blocos alocados e blocos que já foram liberados
	}

	private void handleMemoryDeallocation(DeallocationMemoryRequest deallocation) {
		memory.free(deallocation); // liberação de espaço na memória
		if (!pendingAllocations.isEmpty()) { // verifica se ainda existem requisições pendentes
			AllocationMemoryRequest pendingRequest = pendingAllocations.poll();
			boolean allocated = memory.alloc(pendingRequest);
			if (!allocated) { // se ainda não há espaço na memória o processo volta para a fila
				pendingAllocations.add((AllocationMemoryRequest) pendingRequest);
			}
		}
		System.out.println(memory.status()); // imprime status da memória blocos alocados e blocos que já foram liberados
	}

	/*
	 * TODO: parametrizar tipo de de alocação 1 - first fit 2 - best fit 3 - worst
	 * fit
	 */
}
