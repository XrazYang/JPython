# -*- coding: UTF-8 –*-
import subprocess
import xml.etree.ElementTree as ET
import re
import os
import sys
#代码转XML
def convert_code_to_xml(srcFile, desFile):
    commond = "srcml --position " + str(srcFile) + " -o " + str(desFile)
    rc, out = subprocess.getstatusoutput(commond)
    if rc == 0:
        print( "[ "+srcFile + " ] ------ convert to ------ [ "+ desFile +"  ]  "+ " success!!")

#XML转代码
def convert_xml_to_code(srcFile, desFile):
    commond = "srcml --position " + str(srcFile) + " -o " + str(desFile)
    rc, out = subprocess.getstatusoutput(commond)
    if rc == 0:
        print("[ " + srcFile + " ] ------ convert to ------ [" + desFile + " ]  " + " success!!")
#将代码转换为类文本文件
def preprocess_source_file(src_file):
    src_file1 = all_path(src_file)
    des_file = src_file+"_xml"
    if not os.path.exists(des_file):
        os.makedirs(des_file)
    # 添加空格，用于词法分析
    for s_f in src_file1:
        print(s_f)
        d_f = des_file + "/"+os.path.basename(s_f)
        convert_code_to_xml(s_f, './code.xml')

        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():
            if child.text != None and child.get("type") != "string":
                str = child.text
                child.text = " " + str + " "
            if child.tail != None:
                str = child.tail
                child.tail = " " + str + " "
        tree.write('./code.xml', encoding="utf-8")
        # 删除注释，用于词法分析
        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():  # 深度搜索、树的先序遍历
            tag_name = child.tag
            tag_name = tag_name[tag_name.rfind("}") + 1:]
            if tag_name == "comment":
                child.text = ""
        tree.write('./code.xml', encoding="utf-8")
        convert_xml_to_code('./code.xml', d_f)
        #
        # 变量统一命名
        convert_code_to_xml(d_f, './code.xml')
        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():
            tag_name = child.tag
            tag_name = tag_name[tag_name.rfind("}") + 1:]
            Keyword = ['int', 'char', 'double', 'enum', 'float', 'long', 'short', 'signed', 'struct', 'union',
                       'unsigned', 'void', 'for',
                       'do', 'while', 'break', 'continue', 'if', 'else', 'goto', 'switch', 'case', 'default', 'return',
                       'auto', 'extern',
                       'register', 'static', 'const', 'sizeof', 'typedef', 'volatile', 'class', 'typename', 'string',
                       'printf', 'scarnf', ' endl',
                       '[]', '==', '=', '+', '()', '+=', '<', '<<', '-', '*', '%', '/', '--', "!=", '>', '>>', '>=',
                       '<=', '&&', '||', '!', '&', '|', '^', '~', '+=',
                       '-=', '*=', '/=', '%=', '<<=', '>>=', '&=', '^=', '|=', '++', 'new', 'delete','bool']
            if tag_name == "name" and child.text != None:
                if child.text not in Keyword:
                    child.text = "variable_name"

        tree.write('./code.xml', encoding="utf-8")
        convert_xml_to_code("./code.xml", d_f)

        # 参数统一命名
        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():
            tag_name = child.tag
            tag_name = tag_name[tag_name.rfind("}") + 1:]
            if tag_name == "argument":
                expr = child.getchildren()
                for e in expr:
                    name = e.getchildren()
                    for n in name:
                        c_name = n.tag
                        c_name = c_name[c_name.rfind("}") + 1:]
                        if c_name == "name" and n.text != None:
                            n.text = "argument_name"

        tree.write('./code.xml', encoding="utf-8")

        # 提取字符串中的关键字，并统一命名字符串
        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():
            if child.get("type") == "string":
                string = child.text
                child.text = "thisisstring  "
                Keyword = ['%d', '%f', '%u', '%x', '%c', '%s', '%lf', '%p']
                for k in Keyword:
                    result = re.compile(k).findall(string)
                    for r in result:
                        child.text = child.text + r + " "
        tree.write('./code.xml', encoding="utf-8")

        # 函数名统一命名
        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():
            tag_name = child.tag
            tag_name = tag_name[tag_name.rfind("}") + 1:]
            if tag_name == "function_decl":
                name = child.getchildren()
                for n in name:
                    c_name = n.tag
                    c_name = c_name[c_name.rfind("}") + 1:]
                    if c_name == "name":
                        n.text = "function_name"
            if tag_name == "function":
                name = child.getchildren()
                for n in name:
                    c_name = n.tag
                    c_name = c_name[c_name.rfind("}") + 1:]
                    if c_name == "name":
                        if n.text is None:
                            n[2].text = "function_name"
                        else:
                            n.text = "function_name"
                        if n.get("type") == "operator":
                            n.text = "operator  "

        tree.write('./code.xml', encoding="utf-8")

        # 类名统一命名
        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():
            tag_name = child.tag
            tag_name = tag_name[tag_name.rfind("}") + 1:]
            if tag_name == "class":
                name = child.getchildren()
                name[0].text = "class_name"
        tree.write('./code.xml', encoding="utf-8")

        # 函数调用统一命名
        tree = ET.parse('./code.xml')
        root = tree.getroot()
        for child in root.iter():
            tag_name = child.tag
            tag_name = tag_name[tag_name.rfind("}") + 1:]
            if tag_name == "call":
                name = child.getchildren()
                for n in name:
                    c_name = n.tag
                    c_name = c_name[c_name.rfind("}") + 1:]
                    if c_name == "name" and n.text != None:
                        n.text = "function_call_name"

        tree.write('./code.xml', encoding="utf-8")
        convert_xml_to_code('./code.xml', des_file)
        if os.path.exists("./code.xml"):
            os.remove("./code.xml")
def all_path(dirname):
    result = []
    for maindir, subdir, file_name_list in os.walk(dirname):
        for filename in file_name_list:
            apath = os.path.join(maindir, filename)
            result.append(apath)
    return result


if __name__ == '__main__':
    preprocess_source_file(sys.argv[1])
    #preprocess_source_file(r"D:\xml_to_code\code")