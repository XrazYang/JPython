package jpython;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreprocessedData {

    public PreprocessedData() {
    }

    public String decode() {
        String project_name = null;
        //解压代码
        return project_name;
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
