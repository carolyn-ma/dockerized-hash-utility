import java.util.Arrays;
import java.util.Scanner;

public class HashUtility {

	private static int hashResult = 0;

	// For each 8 bytes in chunk, generate 64-bit integers and or with chunk index
	public static int[] chunkHash(String chunk, int chunkIndex) {
		int intCount = chunk.length()/8 + 1;
		int[] chunkInts = new int[intCount];
		for (int i = 0; i < intCount; i++) {
			chunkInts[i] = 0;
			for (int j = 0; j < 8; j++){
				char currChar = 8 * i + j < chunk.length() ? chunk.charAt(8 * i + j) : 0;
				chunkInts[i] |= currChar << ((7 - j) * 8); 
			}
			chunkInts[i] |= chunkIndex;
		}
		return chunkInts;
	}

	// Rotate the hash and xor with each integer in chunk
	public static void applyChunkHash(int[] chunk, int hash) {
		for (int curr : chunk) {
			hash = (hash >> 60) | (hash << 4);
			hash ^= curr;
		}
		hashResult ^= hash;
	}

  	public static void main(String[] args){
  		Scanner scanner = new Scanner(System.in);
  		String inputStr = "";
  		while (scanner.hasNext()) {
  			inputStr += scanner.next();
  		}
  		int threadCount = 10;
  		// !Check for default of 32768, strictly positive & multiple of 8
  		int window = 1024;

  		// Divide input into chunks based on window size
  		int chunkLen = inputStr.length()/window + 1;
  		String[] chunks = new String[chunkLen];
  		for (int i = 0; i < chunkLen; i++) {
  			int startChunk = i*window;
  			int endChunk = (i + 1)*window > inputStr.length() ? inputStr.length() : (i + 1)*window;
  			chunks[i] = inputStr.substring(startChunk, endChunk);
  		}

  		// Create multithread
  		Thread[] threads = new Thread[threadCount];
	    for (int i = 0; i < threadCount; i++) {

	    	// Slice chunks for each thread
	    	int startThread = i*chunkLen/threadCount;
	    	int endThread = (i + 1)*chunkLen/threadCount > chunkLen ? chunkLen : (i + 1)*chunkLen/threadCount;
	        String[] threadChunks = Arrays.copyOfRange(chunks, startThread, endThread);

	        // Thread function
	    	Runnable runnable = new Runnable() {
	    		private Object lockHash = new Object();
	    		@Override 
	    		public void run() {
			        try {
			        	for (int i = 0; i < threadChunks.length; i++) {
			        		int[] chunkInts = chunkHash(threadChunks[i], i);
			        		synchronized(lockHash) {
			        			applyChunkHash(chunkInts, hashResult);
			        			System.out.println("Thread " + Thread.currentThread().getName() + " is finished with current hash of 0x" + Integer.toHexString(hashResult).toUpperCase() + " -- processing " + threadChunks.length+ " chunks.");
			        		}
			        	}
			        } catch (Exception e) {
			            System.out.println ("\n*** Thread " + Thread.currentThread().getName() + " encountered an exception. ***\n");
			        }
			    }
	    	};
	        threads[i] = new Thread(runnable, Integer.toString(i));
	        threads[i].start();
	    }  

	    // Wait for all threads to finish
	 //    boolean finished = false;
	 //    while (finished == false) {
	 //    	finished = true;
		//     for (int i = 0; i < threadCount; i++) {
	 //            finished &= threads[i].isAlive(); 
	 //        } 
		// }

		// System.out.println("\n*** Finished executing all threads with final hash value of 0x" + Integer.toHexString(hashResult).toUpperCase() + " ***\n");
  	}
}