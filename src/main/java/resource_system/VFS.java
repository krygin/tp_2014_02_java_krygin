package resource_system;

import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Ivan on 18.05.2014 in 16:30.
 */
public class VFS {
    private String root;

    public VFS(String root) {
        this.root = root;
    }

    public boolean isExist(String path) {
        return new File(path).exists();
    }

    public boolean isDirectory(String path) {
        return new File(root + path).isDirectory();
    }

    public String getAbsolutePath(String file) {
        return new File(root + file).getAbsolutePath();
    }

    public String getPath(String file) {
        return new File(root + file).getPath();
    }

    public byte[] getBytes(String file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(root + file);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        byte[] bytes = new byte[dataInputStream.available()];
        dataInputStream.read(bytes);
        dataInputStream.close();
        return bytes;
    }

    public String getUTF8Text(String file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(root + file);
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);

        InputStreamReader inputStreamReader = new InputStreamReader(dataInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public Iterator<String> getIterator(String startDir) {
        return new FileIterator(root + startDir);
    }


    private class FileIterator implements Iterator<String> {
        private Queue<File> files = new LinkedList<>();

        public FileIterator(String path) {
            files.add(new File(path));
        }

        @Override
        public boolean hasNext() {
            return !files.isEmpty();
        }

        @Override
        public String next() {
            File file = files.element();
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    Collections.addAll(files, listFiles);
                }
            }
            return files.poll().getPath();
        }

        @Override
        public void remove() {

        }
    }
}
