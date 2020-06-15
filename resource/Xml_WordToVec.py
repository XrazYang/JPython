# -*- coding: UTF-8 –*-
import gensim
import sys
import os


# 将预处理后的代码生成向量矩阵
def word_to_vec(dir, model, file_name):
    file_path = dir + "/codes_xml/" + file_name
    if not os.path.exists(file_path):
        return False

    input = open(file_path).read()
    result = []
    for line in input.split():
        if line not in result:
            result.append(line)

    vec_path = dir + "/vec"
    if not os.path.exists(vec_path):
        os.makedirs(vec_path)

    vec_name = vec_path + "/" + file_name + ".txt"
    if os.path.exists(vec_name):
        os.remove(vec_name)

    output = open(vec_name, "a")
    for r in result:
        s = str(model[r])
        s = s.strip('[')
        s = s.strip(']')
        s = s.replace('\n', '')
        output.write(s + "\n")

    if os.path.exists(vec_name):
        return True
    else:
        return False


if __name__ == '__main__':
    # total_vec = "D:\\CnnPredict\\resource\\VDISC_total_vec.txt"
    total_vec = sys.argv[2]
    # dir = "D:\\test-1\\result" + "\\des_code\\"
    dir = sys.argv[1] + "/codes_xml/"
    if os.path.exists(total_vec):
        model = gensim.models.KeyedVectors.load_word2vec_format(total_vec, binary=False)
        file_list = os.listdir(dir)
        for name in file_list:
            # word_to_vec("D:\\test-1\\result", model, name)
            word_to_vec(sys.argv[1], model, name)