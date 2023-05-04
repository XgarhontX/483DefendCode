import re
import tkinter.filedialog

def doRetreiveInput(prompt, description, regex, pattern_param):
    result = None
    while result == None:
        # get input
        string = doInputPrompting(prompt, description)

        # compile regex
        if pattern_param == None:
            regexc = re.compile(regex)
        else:
            regexc = re.compile(regex, pattern_param)
        # match
        result = regexc.match(string)
        if result == None: print("No match found.")

    return result
def doInputPrompting(prompt, description):
    # print
    print()
    print(prompt)
    print(description)
    print('>', end=" ")

    # read
    result = str(input())

    return result.strip()
########################################################################################################################
def doRetreiveInputInt(prompt, description):
    result = None
    while result == None:
        result = doInputPromptingInt(prompt, description)
    return result
def doInputPromptingInt(prompt, description):
    # print
    print()
    print(prompt)
    print(description)
    print('>', end=" ")

    # read
    try:
        result = int(input())
    except:
        print("No match found.")
        return None

    # check range
    if (result < -2147483648 or result > 2147483647):
        print("No match found.")
        return None

    return result
########################################################################################################################
NAME_REGEX = "^(?=.*[a-z]+.*)[a-z0-9\s'\.\-]{1,50}$"
# First Name
def promptNameFirst():
    return doRetreiveInput(
        "Enter a first name.",
        "50 chars max: [A-Z, 0-9, ', ., -]",
        NAME_REGEX,
        re.IGNORECASE
    )

# Last Name
def promptNameLast():
    return doRetreiveInput(
        "Enter a last name.",
        "50 chars max: [A-Z, 0-9, ', ., -]",
        NAME_REGEX,
        re.IGNORECASE
    )

# 2 Ints
def prompt2Ints1():
    return doRetreiveInputInt(
        "Enter the 1st int.",
        "Range: (-2147483648, 2147483647)",
    )
    return
def prompt2Ints2():
    return doRetreiveInputInt(
        "Enter the 2nd int.",
        "Range: (-2147483648, 2147483647)",
    )

# Input file name/path
def promptInFileName():
    print("\nSelect the input file.\n> ", end="")

    file = None
    path = None
    while file == None:
        path = tkinter.filedialog.askopenfilename(initialdir="./", title="Select the input file.")
        file = open(path, 'r')
        if not file.readable():
            file = None
        file.close()

    print(path)
    return path

# Output file name/path
def promptOutFileName():
    return

# Password, hashed and salted
def promptPassword():
    return

# Password verification by hashed and salted then compare
def promptPasswordRetype():
    return

# -writes the user's name
# -writes the result of adding the two integer values (no overflow should occur)
# -writes the result of multiplying the two integer values (no overflow should occur)
# -writes the contents of the input file
# -Each thing written should be clearly labeled (e.g. First name, Last name, First Integer, Second Integer, Sum, Product, Input File Name, Input file contents)
def writeOutput():
    return

if __name__ == '__main__':
    print("\nPYTHON VERSION")

    nameFirst = promptNameFirst()
    nameLast = promptNameLast()

    int1 = prompt2Ints1()
    int2 = prompt2Ints2()

    inPath = promptInFileName()
