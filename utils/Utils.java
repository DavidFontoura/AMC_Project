package utils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.text.DecimalFormat;

public class Utils {

    public static void beep() throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat((float) 120000, 8, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open();
        sdl.start();
        for (int i = 0; i < 300 * (float) 44100 / 1000; i++) {
            double angle = i / ((float) 44100 / 440) * 1.5 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 100);
            sdl.write(buf, 0, 1);
        }
        for (int i = 0; i < 300 * (float) 44100 / 1000; i++) {
            double angle = i / ((float) 44100 / 440) * 1.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 100);
            sdl.write(buf, 0, 1);
        }
        for (int i = 0; i < 300 * (float) 44100 / 1000; i++) {
            double angle = i / ((float) 44100 / 440) * 2.5 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 100);
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
    }

    public static String totime(long tms) {
        DecimalFormat d = new DecimalFormat("000");
        int ms = (int) (tms % 1000);
        int t = (int) (tms / 1000);
        double x = (t) / 3600.0;
        int h = (int) x;
        double y = (x - h) * 60;
        int m = (int) y;
        double z = (y - m) * 60;
        int s = (int) z;
        String time = "";
        if (h != 0) time += h + "h ";
        if (m != 0) time += m + "m & ";
        if (s != 0 || ms != 0) time += s + "," + d.format(ms) + "s ";
        return time;
    }

}
