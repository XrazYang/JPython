import jpython.PreprocessedData;

import java.util.ArrayList;

public class jPython {
    public static void main(String[] args) throws Exception {
        PreprocessedData pre =  new PreprocessedData();
//        pre.xml_functionSplit("D:\\xml_to_code");
//        pre.xml_preprocess("D:\\xml_to_code");
//        pre.unTarGz("D:\\c.tar.gz","D:\\test-1");
//        pre.xml_preprocess("D:\\test-1");
//        ArrayList<String> list = new ArrayList<>();
//        pre.xml_wordToVec("D:\\test-1","H:\\h\\JPython\\source\\xml_total_vec.txt");
//        pre.xml_predictResult("D:\\test-1","H:\\h\\JPython\\source\\xml_cnn_model",list);
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }
//        String parentFile = "H:\\test-1";
//
//        pre.functionSplit("H:\\test-1","clang-tidy");

        //pre.wordToVec("C:\\Users\\腹黑熊\\AppData\\Local\\Temp\\tomcat-docbase.5905781022693366222.8080\\uploadfile\\20200522094103554","H:\\Puap\\source\\VDISC_total_vec.txt");
        ArrayList<String> list = new ArrayList<>();
 pre.predictResult("C:\\Users\\腹黑熊\\AppData\\Local\\Temp\\tomcat-docbase.5905781022693366222.8080\\uploadfile\\20200522094103554",list);
  System.out.println(list);
    }

}

