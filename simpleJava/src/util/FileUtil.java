package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fdrama
 * @date 2021/10/22
 **/
public class FileUtil {

    private FileUtil() {

    }

    /**
     * 获取文件夹下的文件
     *
     * @param path 文件夹路径
     * @return 文件列表
     */
    public static List<File> getFiles(String path) {
        File root = new File(path);
        List<File> files = new ArrayList<>();
        File[] subFiles = root.listFiles();
        if (!root.isDirectory()) {
            files.add(root);
        } else {
            if (subFiles != null && subFiles.length > 0) {
                for (File f : subFiles) {
                    files.addAll(getFiles(f.getAbsolutePath()));
                }
            }
        }
        return files;
    }

    /**
     * 按行读取文件内容到集合
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> readFile(File file) throws IOException {
        long start = System.currentTimeMillis();
        long count = Files.lines(Paths.get(file.getPath())).count();
        List<String> list = new LinkedList();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String content;
            while ((content = br.readLine()) != null) {
                list.add(content);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();

        return list;
    }

    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {

        long start = System.currentTimeMillis();
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取字节数组
        byte[] bytes = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        long end = System.currentTimeMillis();
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }


}
