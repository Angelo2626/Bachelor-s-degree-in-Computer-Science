def stringapalindroma(s):
    if s == s[::-1]:
        return True
    else:
        return False
s = input()
if stringapalindroma(s) == True:
    print("YES")
else:
    print("NO")
