# -*- coding: UTF-8 –*-
import shutil
import xml.dom.minidom
import subprocess
import os
import xml.etree.ElementTree as ET
import re
import sys
#代码转XML
print (xml.__file__)
def convert_code_to_xml(srcFile, desFile):
    commond = "srcml --position " + str(srcFile) + " -o " + str(desFile)
    rc, out = subprocess.getstatusoutput(commond)
    if rc == 0:
        print( "[ "+srcFile + " ] ------ convert to ------ [ "+ desFile +"  ]  "+ " success!!")

#XML转代码
def convert_xml_to_code(srcFile, desFile):
    print(srcFile,desFile)
    commond = "srcml --position " + str(srcFile) + " -o " + str(desFile)
    rc, out = subprocess.getstatusoutput(commond)
    if rc == 0:
        print("[ " + srcFile + " ] ------ convert to ------ [" + desFile + " ]  " + " success!!")
#读取并分割XML,并还原源码
# xml路径  code路径   源文件路径
def read_xml(file_path_name,project_name):
    function_code_list = []
    function_name = []
    start_line = []
    start_column = []
    end_line = []
    end_column = []
    file_path_name1 = file_path_name+"/srcml"
    if not os.path.exists(file_path_name1):
        os.makedirs(file_path_name1)
    main(file_path_name+"/"+project_name , file_path_name1)
    print(file_path_name1)
    project_xml_file_xml_path = file_path_name1 + "/"+"xml"
    project_code_file_code_path = file_path_name1 + "/"+"codes"
    if not os.path.exists(project_xml_file_xml_path):
        os.makedirs(project_xml_file_xml_path)

    if not os.path.exists(project_code_file_code_path):
        os.makedirs(project_code_file_code_path)
    project_path = all_path(file_path_name1)
    for file in project_path:
        file_name = os.path.basename(file)
        print(file_name)
        convert_code_to_xml(file, file+".xml")
        code = xml.dom.minidom.parse(file + ".xml")
        functions = code.documentElement.getElementsByTagName("function")
        filename = code.documentElement.getAttribute("filename")
        language = code.documentElement.getAttribute("language")
        tabs = code.documentElement.getAttribute("pos:tabs")
        revision = code.documentElement.getAttribute("revision")
        xmlns_pos = code.documentElement.getAttribute("xmlns:pos")
        xmlns_cpp = code.documentElement.getAttribute("xmlns:cpp")
        xmlns = code.documentElement.getAttribute("xmlns")
        i = 1
        tree = ET.parse(file + ".xml")
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
                        41
                        #function_name.append(n.text)
            if tag_name == "function":
                name = child.getchildren()
                for n in name:
                    c_name = n.tag
                    c_name = c_name[c_name.rfind("}") + 1:]
                    if c_name == "name":
                        if n.text == None:
                            # 函数名+起始行列号
                            function_name.append(n[2].text)
                            s = re.findall(r"\d+\.?\d*", str(n[2].attrib))
                            start_line.append(s[0])
                            start_column.append(s[1])
                        else:
                            function_name.append(n.text)
                            s = re.findall(r"\d+\.?\d*", str(n.attrib))
                            start_line.append(s[0])
                            start_column.append(s[1])
                    # 终止行列号
                    if c_name == "block":
                        pos = n.getchildren()
                        e = re.findall(r"\d+\.?\d*", str(pos[len(pos) - 1].attrib))
                        end_line.append(e[0])
                        end_column.append(e[1])
        for (func,func_name, s_l, s_c, e_l, e_c) in zip(functions, function_name,start_line, start_column, end_line,
                                                         end_column):
            impl = xml.dom.minidom.getDOMImplementation()
            dom = impl.createDocument(None, 'unit', None)
            root = dom.documentElement
            # unit 标签补充完整
            root.setAttribute('language', language)
            root.setAttribute('filename', filename)
            root.setAttribute('pos:tabs', tabs)
            root.setAttribute('revision', revision)
            root.setAttribute('xmlns:pos', xmlns_pos)
            root.setAttribute('xmlns:cpp', xmlns_cpp)
            root.setAttribute('xmlns', xmlns)
            root.appendChild(func)
            # 把每个function分别存储为XML文件
            f = open(project_xml_file_xml_path + "/"+ file_name + '#'+ func_name+'@'+ str(i) + '.xml', 'w',encoding="utf8")

            dom.writexml(f)
            f.close()
            convert_xml_to_code(project_xml_file_xml_path +"/"+ file_name + '#'+  func_name +'@'+  str(i) + '.xml',
                                project_code_file_code_path + "/"+ file_name + '#'+ func_name +'@'+ str(i) +  '.cpp')

            function_code_file = project_code_file_code_path + "/"+ file_name + '#'+ func_name +'@'+  str(i) +  '.cpp'
            function_code_list.append(function_code_file)

            i = i + 1
        if os.path.exists(file_name+".xml"):
            os.remove(file_name+".xml")
    return function_code_list
def all_path(dirname):
    # 所有需要过滤的配置文件夹名
    ignore = [".idea","cmake-build-debug","cmake-build-default","cmake-build-minsizerel","cmake-build-release","cmake-build-relwithdebinfo"]
    filter = [".c", ".cpp", ".h", ".C",".CPP"]  # 设置过滤后的文件类型
    result = []
    for maindir, subdir, file_name_list in os.walk(dirname):
        # 判断路径的最后一级是否在ignore中
        if maindir[maindir.rfind("\\")+1:] not in ignore:
            for filename in file_name_list:
                apath = os.path.join(maindir, filename)  # 合并成一个完整路径
                ext = os.path.splitext(apath)[1]  # 获取文件后缀 [0]获取的是除了文件名以外的内容
                if ext in filter:
                    result.append(apath)
    return result
#拷贝目录A 的内容到目录B

A = "Local_Script"
B = "Local_back"
#通过校验MD5 判断B内的文件与A 不同
def get_MD5(file_path):
    files_md5 = os.popen('md5 %s' % file_path).read().strip()
    file_md5 = files_md5.replace('MD5 (%s) = ' % file_path, '')
    return file_md5



def main(path, out):
    out_path = out+"/"+os.path.basename(path)
    if not os.path.exists(out_path):
        os.makedirs(out_path)
    for files in os.listdir(path):
        name = os.path.join(path, files)
        back_name = os.path.join(out_path, files)
        if os.path.isfile(name):
            if os.path.isfile(back_name):
                if get_MD5(name) != get_MD5(back_name):
                    shutil.copy(name,back_name)
            else:
                shutil.copy(name, back_name)
        else:
            if not os.path.isdir(back_name):
                os.makedirs(back_name)
            main(name, back_name)
if __name__ == '__main__':
    read_xml(sys.argv[1],sys.argv[2])
    #read_xml("/home/lost","clang-tidy")
