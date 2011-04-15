package filerepository.cmdline;

import filerepository.FileRepositoryUtils;
import filerepository.GzipFileRepository;
import filerepository.IFileRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import static filerepository.Utils.hashset;

public class CreateSnapshot {
    public static final String REPO_PATH = System.getProperty("user.home") + "/repo";

    public static void main(String[] args) throws IOException {
        // arg0 - snaphot file, arg1 - dir to snapshot
        if (args.length != 2) {
            System.out.println("arg0 - snaphot file, arg1 - dir to snapshot");
            return;
        }

        String snapshotFile = new File(args[0]).getCanonicalPath();
        String dirToSnapshot = new File(args[1]).getCanonicalPath();

        System.out.println("Create snapshot " + snapshotFile + " from " + dirToSnapshot);

        System.out.println("Repo path: " + new File(REPO_PATH).getCanonicalPath());

        IFileRepository fileRepository = new GzipFileRepository(new File(REPO_PATH));
        PrintWriter output = new PrintWriter(new GZIPOutputStream(new FileOutputStream(new File(snapshotFile))));
        FileRepositoryUtils.createDirOrFileSnapshot(new File(dirToSnapshot), hashset(".git"), output, fileRepository);
        output.close();
    }
}
