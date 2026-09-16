import ast
myinput = ast.literal_eval(input())

result = {value: key for key, value in myinput.items()}

print(result)
