public class BankStatementBatchProcessor {

    // FIX: Changed primitive 'int' to 'AtomicInteger' to make the counter variable thread-safe.
    private final AtomicInteger processedCount = new AtomicInteger(0);

    public void process(List<StatementRecord> records) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (StatementRecord record : records) {
            executor.submit(() -> {
                processRecord(record);
                // FIX: Bug Explanation - The original implementation used 'processedCount++', which is not an atomic operation.
                // It consists of three separate steps: 1. Read current value, 2. Increment by 1, 3. Write value back.
                // In a multi-threaded environment (pool of 10), multiple threads were executing this read-modify-write cycle 
                // simultaneously, leading to a race condition where updates from some threads overwrote others. This caused 
                // the final count to be lower than the actual records processed.
                // Using incrementAndGet() guarantees that the increment operation happens atomically.
                processedCount.incrementAndGet();
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getProcessedCount() {
        // FIX: Updated getter to retrieve the primitive int value safely from AtomicInteger
        return processedCount.get();
    }
}