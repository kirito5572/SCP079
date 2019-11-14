package SCP079.Objects;

public class UnixToTime {
    public static String UnixToTimeChange(Long time1, Long time2) {
        long a = (time2 - time1) / 1000L;
        if(a == 0L) {
            return "영구";
        } else {
            if (a < 10L) {
                return "영구";
            }
            if (a < 60L) {
                return a + "초";
            }
            if (a >= 60L) {
                a = a / 60L;
                return a + "분";
            }
            if (a > 59L) {
                a = a / 60L;
                return a + "시";
            }
            if (a > 23L) {
                a = a / 24L;
                return a + "일";
            }
            if (a > 29L) {
                a = a / 30L;
                return a + "월";
            }
            if (a > 11L) {
                a = a / 12L;
                return a + "년";
            }
            if (a > 50) {
                return "50년 이상";
            }
        }
        return "영구";
    }
}
