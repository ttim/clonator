package filerepository.test;

import filerepository.FileRepositoryUtils;
import filerepository.GzipFileRepository;
import filerepository.IFileRepository;

import java.io.*;
import java.util.zip.GZIPOutputStream;

import static filerepository.Utils.hashset;

public class GzipTest {
    public static void main(String[] args) throws IOException {
        IFileRepository fileRepository = new GzipFileRepository(new File("c:\\Projects\\clonator\\repo2"));
        PrintWriter output = new PrintWriter(new GZIPOutputStream(new FileOutputStream(new File("e:\\snapshot"))));
        FileRepositoryUtils.createDirOrFileSnapshot(new File("e:\\test_dir"), hashset(".git"), output,fileRepository);
        output.close();
    }
}
