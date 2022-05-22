import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException  {
		
		Scanner in = new Scanner(System.in);
        int choise;
        
        Index2 index = new Index2();
        String phrase = "";

        index.buildIndex(new String[]{
           
            "C:\\docs\\100.txt",
            "C:\\docs\\101.txt",
            "C:\\docs\\102.txt",
            "C:\\docs\\103.txt",
            "C:\\docs\\104.txt",
            "C:\\docs\\105.txt",
            "C:\\docs\\106.txt",
            "C:\\docs\\107.txt",
            "C:\\docs\\108.txt",
            "C:\\docs\\109.txt"
          
        });
        
        boolean flag = true;
        while (flag) {
            System.out.println("Enter Your Search Number: "+"\n");
            System.out.println("1- And between two terms");
            System.out.println("2- OR between two terms");
            System.out.println("3- Not term");
            System.out.println("4- Term1 and Term2 or Term3");
            System.out.println("5- Term1 and Term2 or not Term3");
            System.out.println("6- Term1 and Term2 or Term3 and not Term4");
            System.out.println("7- find word");
            System.out.println("0- Exit");
            choise = in.nextInt();
            switch (choise) {
            
            case 0: {
                flag = false;
                break;
            }
            case 1:{
            	System.out.println("Enter your phrase: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                phrase = input.readLine();
                System.out.println(index.And2Lists(phrase));
            	 break;
            }
            case 2: {
            	System.out.println("Enter your phrase: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                phrase = input.readLine();
                System.out.println(index.OR2Lists(phrase));
                break;
            }
            case 3: {
            	System.out.println("Enter your phrase: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                phrase = input.readLine();
                System.out.println(index.NotList(phrase));
                break;
            }
            case 4: {
            	System.out.println("Enter your phrase: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                phrase = input.readLine();
                System.out.println(index.And_OR(phrase));
                break;

            }
            case 5: {
            	System.out.println("Enter your phrase: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                phrase = input.readLine();
                System.out.println(index.And_OR_Not(phrase));
                break;

            }
            case 6: {
            	System.out.println("Enter your phrase: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                phrase = input.readLine();
                System.out.println(index.And_OR_And_Not(phrase));
                break;

            }
            case 7: {
            	System.out.println("Enter your phrase: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                phrase = input.readLine();
                System.out.println(index.find(phrase));
                break;

            }
            	
         }
     }
		
}

}
