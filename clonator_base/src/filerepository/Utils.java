package filerepository;

import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class Utils {
    private static MessageDigest md5Digest;
    private static BASE64Encoder md5Encoder = new BASE64Encoder();

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getFileHash(File file) throws IOException {
        md5Digest.reset();

        byte[] buffer = new byte[1024 * 8];

        FileInputStream fileInputStream = new FileInputStream(file);
        try {
            int readed = fileInputStream.read(buffer);
            while (readed != -1) {
                md5Digest.update(buffer, 0, readed);
                readed = fileInputStream.read(buffer);
            }
        } finally {
            fileInputStream.close();
        }

        return md5Encoder.encode(md5Digest.digest());
    }

    public static void pump(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buf = new byte[1024*8];
        int length = inputStream.read(buf);

        while (length != -1) {
            outputStream.write(buf, 0, length);
            length = inputStream.read(buf);
        }
    }

    public static <T> HashSet<T> hashset(T... args) {
        HashSet<T> result = new HashSet<T>();
        for (T arg : args) {
            result.add(arg);
        }
        return result;
    }
}
