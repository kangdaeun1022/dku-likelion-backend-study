public class StringExample {

    public static void main(String[] args) {


        String str = "안녕하세요!";
        String str_2 = new String("안녕하세요!"); // 생성자
        String str_3 = "안녕하세요!";

        if (str == str_2) {
            System.out.println("str == str_2");
        }

        if (str == str_3) {
            System.out.println("str == str_3");
        }

        if (str_2 == str_3) {
            System.out.println("str_2 == str_3");
        }


        if (str.equals(str_2)) {
            System.out.println("str.equals(str_2)");
        }

        if (str.equals(str_3)) {
            System.out.println("str.equals(str_3)");
        }

        if (str_2.equals(str_3)) {
            System.out.println("str_2.equals(str_3)");
        }



        // 1. '+' 연산자
        String str_1 = "Hello,";
        String str_12 = "World!";

        System.out.println(str_1 + " " + str_12);


        // 2. StringBuilder
        StringBuilder strBdr_1 = new StringBuilder("Hello,");

        strBdr_1.append(" ");
        strBdr_1.append("World!");

        String str_new = strBdr_1.toString();

        System.out.println(str_new);

        // 문자열 슬라이스

        String str_11 = "이름: 강대운";
        System.out.println(str_11.indexOf("이"));
        System.out.println(str_11.indexOf("름"));
        String str_name = str_11.substring(4, 7);
        System.out.println(str_name);


        // 문자열 대소문자 변환

        String str_111 = "abc";
        String str_222 = "ABC";

        str_111 = str_111.toUpperCase();
        str_222 = str_222.toLowerCase();

        System.out.println(str_111);
        System.out.println(str_222);


        if (str_111.equals(str_222)) {
            System.out.println("str_111.equals(str_222)");
        }

        if (str_111.equalsIgnoreCase(str_222)) {
            System.out.println("str_111.equalsIgnoreCase(str_222)");
        }




         //1. 양쪽 끝 공백
        String str_1_1 = "      Hello   ";
        str_1_1 = str_1_1.trim();
        System.out.println(str_1);


        // 2. 문자열 중간 공백
        String str_22 = "   Hel   lo  ";
       // str_22 = str_22.trim();
        str_22 = str_22.replace(" ", "");
        System.out.println(str_22);




























    }
}
