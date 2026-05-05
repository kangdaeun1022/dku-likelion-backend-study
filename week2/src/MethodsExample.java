public class MethodsExample {
    
    // 함수(메서드)
    // 어떠한 값이 넘겨지거나 아무 값도 넘겨지지 않았을때 작업 수행후 반환 or 반환하지 않고 종료

    public static void main(String[] args) {

        int mod_result = mod(3, 2);
        System.out.println(mod_result);

        printNum(10);

        String str = greeting();
        System.out.println(str);

        greeting_2();

    }

    // 1. 입력 O, 출력(반환) O
    static int mod (int a, int b) {
        int result = a % b;
        return result;
    }

    // 2. 입력 O, 출력(반환) X
    static void printNum (int a) {
        System.out.println(a);
    }

    // 3. 입력 X, 출력 O
    static String greeting() {
        return "Hello!";
    }


    // 4. 입력 X, 출력 X
    static void greeting_2() {
        System.out.println("Hello!");
    }




}
