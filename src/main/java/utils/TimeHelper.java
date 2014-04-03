package utils;

/**
 * Created by Ivan on 27.03.2014 in 19:44.
 */
public class TimeHelper {
    @SuppressWarnings("SameParameterValue")
    public static void sleep(int period) {
        try {
            Thread.sleep(period);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
