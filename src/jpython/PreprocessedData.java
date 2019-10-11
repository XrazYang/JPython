package jpython;

import com.ice.tar.TarEntry;
import com.ice.tar.TarInputStream;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PreprocessedData {

    public PreprocessedData() {
    }
    //解压.zip
    public void unZip(String file, String outputDir) throws IOException {
        ZipFile zipFile = null;
        System.out.println(file);
        try {
            Charset CP866 = Charset.forName("CP866");  //specifying alternative (non UTF-8) charset
            //ZipFile zipFile = new ZipFile(zipArchive, CP866);
            zipFile =  new ZipFile(file, CP866);
            createDirectory(outputDir,null);//创建输出目录

            Enumeration<?> enums = zipFile.entries();
            while(enums.hasMoreElements()){

                ZipEntry entry = (ZipEntry) enums.nextElement();
                System.out.println("解压." +  entry.getName());

                if(entry.isDirectory()){//是目录
                    createDirectory(outputDir,entry.getName());//创建空目录
                }else{//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/",null);//创建输出目录

                    InputStream in = null;
                    OutputStream out = null;
                    try{
                        in = zipFile.getInputStream(entry);;
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];
                        while((length = in.read(b)) != -1){
                            out.write(b, 0, length);
                        }

                    }catch(IOException ex){
                        throw ex;
                    }finally{
                        if(in!=null)
                            in.close();
                        if(out!=null)
                            out.close();
                    }
                }
            }

        } catch (IOException e) {
            throw new IOException("解压缩文件出现异常",e);
        } finally{
            try{
                if(zipFile != null){
                    zipFile.close();
                }
            }catch(IOException ex){
                throw new IOException("关闭zipFile出现异常",ex);
            }
        }
    }
    //解压.tar.gz
    public void unTarGz(String file, String outputDir) throws IOException{
        TarInputStream tarIn = null;
        try{
            tarIn = new TarInputStream(new GZIPInputStream(
                    new BufferedInputStream(new FileInputStream(file))),
                    1024 * 2);

            createDirectory(outputDir,null);//创建输出目录

            TarEntry entry = null;
            while( (entry = tarIn.getNextEntry()) != null ){

                if(entry.isDirectory()){//是目录
                    entry.getName();
                    createDirectory(outputDir,entry.getName());//创建空目录
                }else{//是文件
                    File tmpFile = new File(outputDir + "/" + entry.getName());
                    createDirectory(tmpFile.getParent() + "/",null);//创建输出目录
                    OutputStream out = null;
                    try{
                        out = new FileOutputStream(tmpFile);
                        int length = 0;

                        byte[] b = new byte[2048];

                        while((length = tarIn.read(b)) != -1){
                            out.write(b, 0, length);
                        }

                    }catch(IOException ex){
                        throw ex;
                    }finally{

                        if(out!=null)
                            out.close();
                    }
                }
            }
        }catch(IOException ex){
            throw new IOException("解压归档文件出现异常",ex);
        } finally{
            try{
                if(tarIn != null){
                    tarIn.close();
                }
            }catch(IOException ex){
                throw new IOException("关闭tarFile出现异常",ex);
            }
        }
    }
    //解压.tar
    public void unTar(String file, String outputDir){
        FileInputStream fis = null;
        OutputStream fos = null;
        TarInputStream tarInputStream = null;
        try {
            fis = new FileInputStream(file);
            tarInputStream = new TarInputStream(fis, 1024 * 2);
            // 创建输出目录
            createDirectory(outputDir, null);

            TarEntry entry = null;
            while(true){
                entry = tarInputStream.getNextEntry();
                if( entry == null){
                    break;
                }
                if(entry.isDirectory()){
                    createDirectory(outputDir, entry.getName()); // 创建子目录
                }else{
                    fos = new FileOutputStream(new File(outputDir + "/" + entry.getName()));
                    int count;
                    byte data[] = new byte[2048];
                    while ((count = tarInputStream.read(data)) != -1) {
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(fos != null){
                    fos.close();
                }
                if(tarInputStream != null){
                    tarInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
   //构建目录
    public void createDirectory(String outputDir,String subDir){
        File file = new File(outputDir);
        if(!(subDir == null || subDir.trim().equals(""))){//子目录不为空
            file = new File(outputDir + "/" + subDir);
        }
        if(!file.exists()){
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            file.mkdirs();
        }
    }

    public void functionSplit(String dir, String project_name) {
        File[] files = new File(dir + "\\" + project_name).listFiles();
        List<File> file_list = Arrays.asList(files);

        for (File file :
                file_list) {
            String name = file.getName();
            if (file.isFile() && (name.endsWith(".cpp") || name.endsWith(".h") || name.endsWith(".c") || name.endsWith(".hpp"))) {
                try {
                    ANTLRFileStream input = new ANTLRFileStream(file.getAbsolutePath());
                    CPP14Lexer lexer = new CPP14Lexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    CPP14Parser parser = new CPP14Parser(tokens);
                    ParseTree tree = parser.translationunit();

                    // Listener
                    ParseTreeWalker walker = new ParseTreeWalker();
                    FunctionSplitWithListener function = new FunctionSplitWithListener(tokens, dir, file.getName());
                    walker.walk(function, tree);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void functonPreprocess(String dir) {
        File[] files = new File(dir + "\\code").listFiles();
        List<File> file_list = Arrays.asList(files);

        for (File file :
                file_list) {
            String name = file.getName();
            if (file.isFile() && name.endsWith(".cpp")) {
                try {
                    ANTLRFileStream input = new ANTLRFileStream(file.getAbsolutePath());
                    CPP14Lexer lexer = new CPP14Lexer(input);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    CPP14Parser parser = new CPP14Parser(tokens);
                    ParseTree tree = parser.translationunit();

                    ParseTreeWalker walker = new ParseTreeWalker();
                    CPP14ValuesListener function = new CPP14ValuesListener(tokens, dir, name);
                    walker.walk(function, tree);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean wordToVec(String dir, String total_vec) {
        try {
            // define the command string
            String para1 = dir.replaceAll("\\\\", "/");
            String para2 = " " + total_vec.replaceAll("\\\\", "/") + " ";

            File[] files = new File(dir + "\\des_code").listFiles();
            List<File> file_list = Arrays.asList(files);

            for (File file :
                    file_list) {
                String name = file.getName();
                String path = "python source/WordToVec.py " + para1 + para2 + name;

                //Create a Process instance and execute commands
                Process pr = Runtime.getRuntime().exec(path);

                ////Get the result produced by executing the above commands
                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = null;
                String result = "";
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                pr.waitFor();
                if (result.equals("False")) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void predictResult(String dir, String cnn_path, ArrayList<String> result_list) {
        try {
            // define the command string
            String para1 = dir.replaceAll("\\\\", "/") + " ";
            String para2 = " " + cnn_path.replaceAll("\\\\", "/");

            File[] files = new File(dir + "\\vec").listFiles();
            List<File> file_list = Arrays.asList(files);

            for (File file :
                    file_list) {
                String name = file.getName();
                String path = "python source/PredictResult.py " + para1 + name + para2;

                //Create a Process instance and execute commands
                Process pr = Runtime.getRuntime().exec(path);

                ////Get the result produced by executing the above commands
                BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line = null;
                String result = "";
                while ((line = in.readLine()) != null) {
                    result += line;
                }
                pr.waitFor();

                result_list.add(result);
                //System.out.println(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
