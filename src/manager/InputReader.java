package manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import entity.AllocationMemoryRequest;
import entity.DeallocationMemoryRequest;
import entity.MemoryParams;
import entity.MemoryRequest;
import entity.MemoryRequestType;

public class InputReader {

	static final String FILENAME = "input-sample.txt";

	public static void main(String[] args) {
		FileReader file = null;
		MemoryParams memoryParams = new MemoryParams();
		Queue<MemoryRequest> memoryRequests = new LinkedList<MemoryRequest>();

		try {
			file = new FileReader(FILENAME);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader bufferReader = new BufferedReader(file);
		String line;

		try {
			int managerType = Integer.parseInt(bufferReader.readLine().trim());
			System.out.println("Manager type = " + managerType);
			int initialMemory = Integer.parseInt(bufferReader.readLine().trim());
			System.out.println("Initial Memory = " + initialMemory);
			int finalMemory = Integer.parseInt(bufferReader.readLine().trim());
			System.out.println("Final Memory = " + finalMemory);
			memoryParams.setInitialMemory(initialMemory);
			memoryParams.setFinalMemory(finalMemory);
			System.out.println("Allocation / Deallocation attemps");
			int i = 1;
			while ((line = bufferReader.readLine()) != null) {
				String[] allocationAttempts = line.split(" ");
				String type = allocationAttempts[0];
				String size = allocationAttempts[1];
				if (type.equalsIgnoreCase("S")) {
					memoryRequests.add(new AllocationMemoryRequest(MemoryRequestType.S, Integer.parseInt(size.trim()), i++));
				}
				if (type.equalsIgnoreCase("L")) {
					memoryRequests.add(new DeallocationMemoryRequest(MemoryRequestType.L, -1, Integer.parseInt(size.trim())));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		MemoryManager memoryManager = new MemoryManager(memoryParams, memoryRequests);
		memoryManager.handleRequests();
	}
}