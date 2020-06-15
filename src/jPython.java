import jpython.PreprocessedData;

import java.util.ArrayList;


public class jPython {
    public static void main(String[] args) throws Exception {
        PreprocessedData pre =  new PreprocessedData();

    //    pre.unZip("/tmp/tomcat-docbase.3221568988904081851.8080/uploadfile/20200522180426643/clang-tidy.zip","/tmp/tomcat-docbase.3221568988904081851.8080/uploadfile/20200522180426643");
//        pre.functionSplit("/home/sd/Test_items","staticTest");
//        pre.functonPreprocess("/home/sd/Test_items");
//
//        pre.wordToVec("/home/sd/Test_items","/home/sd/JPython/resource/WordToVec.py","/home/sd/JPython/resource/VDISC_total_vec.txt");
//
//        ArrayList<String> list = new ArrayList<>();
//        pre.predictResult("/home/sd/Test_items","/home/sd/JPython/resource/PredictResult.py","/home/sd/JPython/resource/cnn_model",list);
//        System.out.println(list);
//
        pre.xml_functionSplit("/home/sd/Test_items","/home/sd/JPython/resource/xml_to_code.py","staticTest");
        pre.xml_preprocess("/home/sd/Test_items/srcml","/home/sd/JPython/resource/preprocess.py");

        pre.xml_wordToVec("/home/sd/Test_items/srcml","/home/sd/JPython/resource/Xml_WordToVec.py","/home/sd/IdeaProjects/Puap/resource/xml_total_vec.txt");
        ArrayList<String> list = new ArrayList<>();
       pre.xml_predictResult("/home/sd/Test_items/srcml","/home/sd/JPython/resource/Xml_PredictResult.py","/home/sd/IdeaProjects/Puap/resource/xml_cnn_model",list);
        System.out.println(list);
    }


}

