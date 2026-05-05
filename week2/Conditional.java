public class Conditional {

    public static void main(String[] args) {

        int a = 1;
        int b = 100;

        // 만족하는 최초의 분기만 실행
        if (a == b) {
            System.out.println("a == b");
        } else if (a < b) {
            System.out.println("a < b");
        } else if (a <= b) {
            System.out.println("a <= b");
        } else {
            System.out.println("else");
        }





        if (a == b) {
            System.out.println("a == b");
        } else {
            System.out.println("else block");
        }


        if (a != b) {
            System.out.println("a != b");
        }

        int a1 = 1;

        switch (a1 + 1) {

            case 11:
                System.out.println("a1 + 1 == 11");
                break;
            case 10:
                System.out.println("a1 + 1 == 10");
                break;
            case 9:
                System.out.println("a1 + 1 == 9");
                break;
            default:
                System.out.println("default");
        }










    }
}
