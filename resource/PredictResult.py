# -*- coding: UTF-8 –*-
import numpy as np
from tensorflow import keras
import sys

data_max_line = 120
data_max_row = 13


def read_vec(dir, file_name):
    data_list = []
    f = open(dir + "/vec/" + file_name)
    line = f.readline()
    while line:
        num = list(map(str, line.split()))
        data_list.append(num)
        line = f.readline()
    f.close()
    data = np.matrix(data_list)

    # 数据填充
    if len(data) < data_max_line:
        arr = np.zeros((data_max_line, data_max_row), dtype="float32")
        for i in range(0, len(data)):
            arr[i] = data[i]
        data = np.matrix(arr)

    return data


def predict_code_result(dir, file_name, model_path):
    data = read_vec(dir, file_name)
    test = np.zeros(shape=(1, data_max_line, data_max_row), dtype="float32")
    test[0] = data
    test = test.reshape(-1, data_max_line, data_max_row, 1)

    # predict
    result = []
    for i in range(1, 6):
        model = model_path + "/CNN_label-" + str(i) + ".h5"
        load_model = keras.models.load_model(model)
        predictions = load_model.predict(test)
        result.append(np.argmax(predictions))

    return file_name + " : " + str(result)


if __name__ == '__main__':
    # print(predict_code_result("D:\\test-1\\result", "test__0.cpp"))
    result = predict_code_result(sys.argv[1], sys.argv[2], sys.argv[3])
    print (result)
