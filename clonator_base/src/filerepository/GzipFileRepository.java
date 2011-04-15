package filerepository;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static filerepository.Utils.getFileHash;

public class GzipFileRepository implements IFileRepository {
    private final File dir;
    private final File indexFile;
    private final File contentFile;

    private Map<String, Long> fileIdsToOffset = new HashMap<String, Long>();
    private Map<String, Long> fileIdsToLength = new HashMap<String, Long>();

    public GzipFileRepository(File fileRepositoryDir) throws IOException {
        dir = fileRepositoryDir;
        indexFile = new File(dir.getAbsolutePath() + "/" + "index");
        contentFile = new File(dir.getAbsolutePath() + "/" + "content");

        if (!indexFile.exists()) {
            indexFile.createNewFile();
        }
        if (!contentFile.exists()) {
            contentFile.createNewFile();
        }

        Scanner input = new Scanner(indexFile);
        while (input.hasNextLine()) {
            String[] line = input.nextLine().split(" ");

            fileIdsToOffset.put(line[0], Long.parseLong(line[1]));
            fileIdsToLength.put(line[0], Long.parseLong(line[2]));
        }
        input.close();
    }

    private static OutputStream getAppendOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file, true);
    }

    public String add(File file) throws IOException {
        String hash = getFileHash(file);
        if (!fileIdsToLength.containsKey(hash)) {
            PrintWriter indexWriter = new PrintWriter(getAppendOutputStream(indexFile));
            indexWriter.println(hash + " " + contentFile.length() + " " + file.length());
            indexWriter.close();

            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new GZIPOutputStream(getAppendOutputStream(contentFile));
            Utils.pump(inputStream, outputStream);
            outputStream.close();
            inputStream.close();
        }
        return hash;
    }

    public byte[] get(String id) throws IOException {
        if (fileIdsToLength.containsKey(id)) {
            long length = fileIdsToLength.get(id);
            long offset = fileIdsToOffset.get(id);

            InputStream stream = new FileInputStream(contentFile);
            stream.skip(offset);
            stream = new GZIPInputStream(stream);
            byte[] result = new byte[(int) length];
            stream.read(result);
            stream.close();

            return result;
        } else {
            return null;
        }
    }

    public boolean contains(File file) throws IOException {
        return fileIdsToLength.containsKey(getFileHash(file));
    }

    public Set<String> getFilesIds() {
        return fileIdsToLength.keySet();
    }
}
