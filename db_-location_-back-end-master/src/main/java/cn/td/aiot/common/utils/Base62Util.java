package cn.td.aiot.common.utils;

public class Base62Util {
    public static final String BASE_62_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final int BASE = BASE_62_CHAR.length();

    /**
     * 转为10进制
     * @param str
     * @return
     */
    public static long toBase10(String str) {
        //从右边开始
        return toBase10(new StringBuilder(str).reverse().toString().toCharArray());
    }

    private static long toBase10(char[] chars) {
        long n = 0;
        int pow = 0;
        for(char item: chars){
            n += toBase10(BASE_62_CHAR.indexOf(item),pow);
            pow++;
        }
        return n;
    }

    private static long toBase10(int n, int pow) {
        return n * (long) Math.pow(BASE, pow);
    }

    /**
     * 转为62进制
     * @param i
     * @return
     */
    public static String fromBase10(long i) {
        StringBuilder sb = new StringBuilder("");
        if (i == 0) {
            return "a";
        }
        while (i > 0) {
            i = fromBase10(i, sb);
        }
        return sb.reverse().toString();
    }

    private static long fromBase10(long i, final StringBuilder sb) {
        int rem = (int)(i % BASE);
        sb.append(BASE_62_CHAR.charAt(rem));
        return i / BASE;
    }
}
