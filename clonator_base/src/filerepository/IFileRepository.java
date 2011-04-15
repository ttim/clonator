package filerepository;

import java.io.*;
import java.util.Set;

public interface IFileRepository {
    public String add(File file) throws IOException;

    public byte[] get(String id) throws IOException;

    public boolean contains(File file) throws IOException;

    public Set<String> getFilesIds();
}
