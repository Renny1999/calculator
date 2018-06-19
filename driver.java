package test;
import java.util.Scanner;

public class driver {
	public static void main(String[] args) {
		Calculator c = new Calculator();
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your expression");
		String input = scan.nextLine();
		while(!input.equalsIgnoreCase("exit")) {
			System.out.println(c.calculate(input));
			System.out.println("Enter your expression");
			input = scan.nextLine();
		}
		scan.close();
	}
}
