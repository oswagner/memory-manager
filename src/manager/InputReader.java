package manager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import entity.AllocationMemoryRequest;
import entity.DeallocationMemoryRequest;
import entity.Memory;
import entity.MemoryRequest;
import entity.MemoryRequestType;

public class InputReader {

	static final String FILENAME = "input-sample.txt";

	public static void main(String[] args) {
		FileReader file = null;
		Memory memory = null;
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
			
			int initialMemory = Integer.parseInt(bufferReader.readLine().trim());
			int finalMemory = Integer.parseInt(bufferReader.readLine().trim());
			memory = new Memory(initialMemory, finalMemory);
			
			while ((line = bufferReader.readLine()) != null) {
				String[] allocationAttempts = line.split(" ");
				String type = allocationAttempts[0];
				String size = allocationAttempts[1];
				if (type.equalsIgnoreCase("S")) {
					memoryRequests.add(new AllocationMemoryRequest(MemoryRequestType.S, Integer.parseInt(size.trim())));
				}
				if (type.equalsIgnoreCase("L")) {
					memoryRequests.add(new DeallocationMemoryRequest(MemoryRequestType.L, Integer.parseInt(size.trim())));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		MemoryManager memoryManager = new MemoryManager(memory);
		System.out.println("Start processing memory requests...");
		while (!memoryRequests.isEmpty()) {
			MemoryRequest nextRequest = memoryRequests.poll();
			memoryManager.handleRequest(nextRequest);
		}
		System.out.println("Finishing processing memory requests!!!");
	}
}