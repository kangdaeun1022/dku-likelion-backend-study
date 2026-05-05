public class TypeCasting {

    public static void main(String[] args) {


        // 강제 형변환
        int a = 128;
        short b = (short) a;
        byte c = (byte) a;
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);

        // 자동 형변환
        short x = 10;
        int y = x;

        System.out.println(x);
        System.out.println(y);

        int m = 80;
        double n = m;
        System.out.println(m);
        System.out.println(n);

        double c2 = 10.33325232523523;
        float d = (float) c2;
        System.out.println(c2);
        System.out.println(d);

        int e = 10;
        short f = 20;

        short g = (short) (e + f);

        System.out.println(g);







    }
}
