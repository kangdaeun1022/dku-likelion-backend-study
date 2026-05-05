import java.util.Scanner;

public class ConsoleIO {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("아이디를 입력해주세요 : ");
        String username = sc.nextLine();

        System.out.print("생년월일을 입력해주세요 : ");
        int birthDate = sc.nextInt();


        System.out.printf("%s\t%d", username, birthDate);
    }
}
