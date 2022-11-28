import re
import os

in_path = "./teamup-common/src/main/kotlin/cn/net/ziqiang/teamup/backend/common/pojo/entity"
out_path = "./entity_converter_out"

excluded_files = ["ExceptionLog.kt", "PermissionChecker.kt", "Report.kt", "RequestLog.kt"]

swift_header = ["import Foundation\n",
                "import HandyJSON\n\n"]

def convert_files(in_path: str):
    if not os.path.exists(in_path):
        return
    if not os.path.exists(out_path) or not os.path.isdir(out_path):
        os.makedirs(out_path)
    files = os.listdir(in_path)
    for file in files:
        if file in excluded_files:
            continue
        file_path = os.path.join(in_path, file)
        if os.path.isfile(file_path):
            filename, extension = os.path.splitext(file)
            if extension != ".kt" and extension != ".java":
                continue
            with open(file_path, "r") as source:
                lines = source.readlines()
                with open(out_path + "/" + filename + ".swift", "w") as target:
                    target.writelines(swift_header)
                    if extension == ".java":
                        for line in lines:
                            # 匹配类名
                            match_obj = re.search(r"public\s+class\s+(\w+)\s*\{", line)
                            if match_obj is not None:
                                target.write(get_swift_struct_header(match_obj.group(1)))
                                continue
                            # 匹配注释
                            match_obj = re.search(r'@Schema\(description = "(\S+)"\)', line)
                            if match_obj is not None:
                                target.write(get_swift_comment(match_obj.group(1)))
                                continue
                            # 匹配属性
                            match_obj = re.search(r"([\w<>]+)\s+(\w+)(\s+=\s+\S+)?;", line)
                            if match_obj is not None:
                                target.write(get_swift_property(match_obj.group(2), match_obj.group(1), match_obj.group(3) is None))
                                continue
                        target.write(get_swift_struct_footer())
                    elif extension == ".kt":
                        for line in lines:
                            # 匹配类名
                            match_obj = re.search(r"class\s+(\w+)\s*\(", line)
                            if match_obj is not None:
                                target.write(get_swift_struct_header(match_obj.group(1)))
                                continue
                            # 匹配注释
                            match_obj = re.search(r'@Schema\(description = "(\S+)"\)', line)
                            if match_obj is not None:
                                target.write(get_swift_comment(match_obj.group(1)))
                                continue
                            # 匹配属性
                            match_obj = re.search(r"var\s+(\w+):\s+([\w<>]+)(\?*)", line)
                            if match_obj is not None:
                                target.write(get_swift_property(match_obj.group(1), match_obj.group(2), match_obj.group(3) == "?"))
                                continue
                        target.write(get_swift_struct_footer())
        # if os.path.isdir(in_path + "/" + file):
        #     print(file + "是一个文件夹")

def get_swift_struct_header(name: str):
    return "struct " + name + ": HandyJSON {\n"

def get_swift_struct_footer():
    return "}\n"

def get_swift_comment(comment: str):
    return "    /// " + comment + "\n"

def get_swift_property(name: str, type: str, optional: bool):
    type = java_type_to_swift_type(type)
    return "    var " + name + ": " + type + ("?" if optional else "!") + "\n\n"

def java_type_to_swift_type(type: str):
    if type == "Boolean":
        return "Bool"
    elif type == "Long" or type == "Integer":
        return "Int"
    elif re.match(r"List<\w+>", type) is not None:
        return "[" + java_type_to_swift_type(type[5:-1]) + "]"
    return type

if __name__ == "__main__":
    convert_files(in_path)
