import jpython.PreprocessedData;

public class jPython {
    public static void main(String[] args) throws Exception {
        PreprocessedData pre =  new PreprocessedData();
       // pre.unZip("H:\\c.zip", "H:\\test-1");
        //pre.unTarGz("D:\\c.tar.gz","D:\\");
        String file1 = "D:\\c.tar.gz";
        String file2 = "D:\\";
        pre.unTarGz(file1,file2);
//        ArrayList<String> list = new ArrayList<>();
//        String parentFile = "H:\\test-1";
//
//        pre.functionSplit("H:\\test-1","clang-tidy");

    }
}

