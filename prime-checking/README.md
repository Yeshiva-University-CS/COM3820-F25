# Prime Checker Threading Lab (Maven, Java 21)

## Build
```powershell
mvn -DskipTests package
```

## Run (using jar)
The simplest way to run the program is using the generated jar:

```powershell
# After building, run one of:
java -jar target/prime-lab-1.jar UNBOUNDED 1000
java -jar target/prime-lab-1.jar PARTITIONED 2000 4
java -jar target/prime-lab-1.jar QUEUE 2000 4
```

## Arguments
- `UNBOUNDED <count>`: Creates one thread per number (high parallelism)
- `PARTITIONED <count> <numThreads>`: Divides work among fixed thread count
- `QUEUE <count> <numThreads>`: Producer-consumer pattern with work queue

## Examples
1. Check 1000 numbers using unbounded threads:
   ```powershell
   java -jar target/prime-lab-1.jar UNBOUNDED 1000
   ```

2. Check 2000 numbers using 4 worker threads (partitioned):
   ```powershell
   java -jar target/prime-lab-1.jar PARTITIONED 2000 4
   ```

3. Check 2000 numbers using producer-consumer with 4 workers:
   ```powershell
   java -jar target/prime-lab-1.jar QUEUE 2000 4
   ```

## Notes
- Java 21 is enforced via `maven-compiler-plugin` with `<release>21</release>`
- No external dependencies required
- Executable jar is configured via `maven-jar-plugin`
- Random numbers are generated in range [100,000 - 1,000,000]
