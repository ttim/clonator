package filerepository;

import javax.swing.plaf.FontUIResource;
import java.io.*;
import java.util.Scanner;
import java.util.Set;

public class FileRepositoryUtils {
    private static void createDirOrFileSnapshot(File dirOrFile, Set<String> ignoreDirs, PrintWriter snapshotWriter, IFileRepository fileRepository, String relativePath) throws IOException {
        if (dirOrFile.isFile()) {
            if (!fileRepository.contains(dirOrFile)) {
                System.out.println("Adding " + dirOrFile);
                fileRepository.add(dirOrFile);
            }
            snapshotWriter.println(Utils.getFileHash(dirOrFile) + " " + relativePath);
        } else {
            for (File inner : dirOrFile.listFiles()) {
                if (!ignoreDirs.contains(inner.getName())) {
                    createDirOrFileSnapshot(inner, ignoreDirs, snapshotWriter, fileRepository, relativePath + "/" + inner.getName());
                }
            }
        }
    }

    public static void createDirOrFileSnapshot(File dirOrFile, Set<String> ignoreDirs, PrintWriter snapshotWriter, IFileRepository fileRepository) throws IOException {
        createDirOrFileSnapshot(dirOrFile, ignoreDirs, snapshotWriter, fileRepository, ".");
    }

    public static void restoreDirOfFileFromSnapshot(File destFolder, Scanner snapshotReader, IFileRepository repository) throws IOException {
        String path = destFolder.getAbsolutePath() + "/";

        while (snapshotReader.hasNextLine()) {
            String line = snapshotReader.nextLine();

            String hash = line.substring(0, line.indexOf(" "));
            String relPath = line.substring(hash.length() + 1);

            File file = new File(path+relPath);
            file.getParentFile().mkdirs();
            FileOutputStream outputStream = new FileOutputStream(file);
            System.out.println("Restore " + file.getCanonicalPath());
            Utils.pump(new ByteArrayInputStream(repository.get(hash)), outputStream);
            outputStream.close();
        }
    }
}
