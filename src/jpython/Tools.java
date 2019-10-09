package jpython;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Tools {

    public static final String C99_BASE_TYPE[] = {"char", "unsigned char", "signed char", "int", "unsigned int", "short", "unsigned short", "long", "unsigned long", "float", "double", "long double", "void", "_Bool", "_Complex", "_Imaginary"};

    public static final String CPP14_BASE_TYPE[] = {"char", "unsigned char", "signed char", "unsigned int", "signed int", "short int", "unsigned short int", "signed short int", "long int", "signed long int", "unsigned long int", "float", "double", "long double", "wchar_t"};

    /**
     * %：表示格式说明的起始符号，不可缺少
     * -：有-表示左对齐输出，如省略表示右对齐输出
     * 0：有0表示指定空位填0，如省略表示指定空位不填
     * m.n：m指域宽，即对应的输出项在输出设备上所占的字符数；n指精度，用于说明输出的实型数的小数位数，未指定n时，隐含的精度为n=6位
     * l,h：l对整型指long型，对实型指double型；h用于将整型的格式字符修正为short型
     */
    public static final String C99_FORMAT_OUTPUT[] = {"%a", "%A", "%c", "%C", "%d", "%ld", "%Ld", "%hd", "%e ", "%E", "%f", "%g", "%G", "%i", "%o", "%p", "%s", "%S", "%u", "%x", "%#x", "%X", "%#X", "%%", "%lld", "%llu", "%llx", "%I64d", "%I64u", "%I64x"};

    public static final boolean isContainFORMAT_OUTPUT(String str, Vector<String> vec) {
        for (String tmp : C99_FORMAT_OUTPUT
        ) {
            int count = 0;
            Pattern p = Pattern.compile(tmp);
            Matcher m = p.matcher(str);
            while (m.find()) {
                count++;
            }
            for (int i = 0; i < count; i++) {
                vec.add(tmp);
            }
        }
        return vec.isEmpty();
    }

    /**
     * \ddd  1到3位八进制数所代表的任意字符
     * \xhh  1到2位十六进制所代表的任意字符
     */
    public static final String ESCAPE_CHARACTERS[] = {"\\a", "\\b", "\\f", "\\n", "\\r", "\\t", "\\v", "\\\\", "\\'", "\\\"", "\\0", "\\?"};

    //函数名
    public static final String FUNCTION_NAME = "function_name";

    //函数调用
    public static final String FUNCTION_CALL = "function_call_name";

    //函数形参
    public static final String FORMAL_PARAMETER = "argument_name";

    //函数实参
    public static final String ACTUAL_PARAMETER = "XXX_actual_parameter";

    //变量声明
    public static final String VARIABLE_NAMW = "variable_name";

    //变量调用
    public static final String VARIABLE_CALL = "XXX_variable_call";

    //字符串
    public static final String STRING = "\"thisisstring\"";
    public static final String CHAR = "\'C\'";

    //类名
    public static final String CLASS_NAME = "class_name";

    //模板参数
    public static final String TEMPLATE_NAME = "XXX_template_arg_name";

}
