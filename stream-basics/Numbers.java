import java.util.*;
import java.util.stream.*;

public class Numbers {
    public static void main(String[] args) {

        int[] numbers = IntStream.rangeClosed(1, 100).toArray();

        // Exercise 1: Sum all numbers
        int sum = 0;

        System.out.println("Sum: " + sum);

        // Exercise 2: Find all even numbers
        List<Integer> evenNumbers = new ArrayList<>();

        System.out.println("Even Numbers: " + evenNumbers);

        // Exercise 3: Square each number and find the average of the squared numbers
        double averageSquared = 0;

        System.out.println("Average Squared: " + averageSquared);

        // Exercise 4: Count how many numbers are divisible by 3
        int countDivBy3 = 0;

        System.out.println("Count Divisible by 3: " + countDivBy3);
    }

}
