package filerepository.test;

import filerepository.FileRepositoryUtils;
import filerepository.GzipFileRepository;
import filerepository.IFileRepository;

import java.io.*;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static filerepository.Utils.hashset;

public class GzipTest2 {
    public static void main(String[] args) throws IOException {
        IFileRepository fileRepository = new GzipFileRepository(new File("c:\\Projects\\clonator\\repo2"));
        InputStream input = new GZIPInputStream(new FileInputStream(new File("e:\\snapshot")));
        FileRepositoryUtils.restoreDirOfFileFromSnapshot(new File("e:\\test_dir2"), new Scanner(input), fileRepository);
    }
}
