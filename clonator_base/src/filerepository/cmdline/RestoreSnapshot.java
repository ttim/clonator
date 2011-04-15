package filerepository.cmdline;

import filerepository.FileRepositoryUtils;
import filerepository.GzipFileRepository;
import filerepository.IFileRepository;

import java.io.*;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static filerepository.Utils.hashset;

public class RestoreSnapshot {
    public static final String REPO_PATH = System.getProperty("user.home") + "/repo";

    public static void main(String[] args) throws IOException {
        // arg0 - snaphot file, arg1 - dir to restore snapshot
        if (args.length != 2) {
            System.out.println("arg0 - snaphot file, arg1 - dir to restore snapshot");
            return;
        }

        String snapshotFile = new File(args[0]).getCanonicalPath();
        String dirToRestore = new File(args[1]).getCanonicalPath();

        System.out.println("Create snapshot " + snapshotFile + " to " + dirToRestore);

        System.out.println("Repo path: " + new File(REPO_PATH).getCanonicalPath());

        IFileRepository fileRepository = new GzipFileRepository(new File(REPO_PATH));
        InputStream input = new GZIPInputStream(new FileInputStream(new File(snapshotFile)));
        FileRepositoryUtils.restoreDirOfFileFromSnapshot(new File(dirToRestore), new Scanner(input), fileRepository);
    }
}
