import os
import platform
import re
import subprocess
import tkinter.filedialog
import ServerSide
import ServerSide.WebServiceAndDBMS
import json  # https://www.w3schools.com/python/python_json.asp


def doRetreiveInput(prompt, description, regex, pattern_param=None):
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

    return result.string


def doInputPrompting(prompt, description):
    # print
    print()
    print(prompt)
    if description != None: print(description)
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
REGEX_NAME = "^(?=.*[a-z]+.*)[a-z0-9\s'\.\-]{1,50}$"


# First Name
def promptNameFirst():
    return doRetreiveInput(
        "Enter a first name.",
        "50 chars max: [A-Z, 0-9, ', ., -]",
        REGEX_NAME,
        re.IGNORECASE
    )


# Last Name
def promptNameLast():
    return doRetreiveInput(
        "Enter a last name.",
        "50 chars max: [A-Z, 0-9, ', ., -]",
        REGEX_NAME,
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
    file = None
    path = None
    while file == None:
        print("\nSelect the input file\n(A File Browser was opened.)", end="")
        path = tkinter.filedialog.askopenfilename(initialdir="./", title="Select the input file.")

        # handle cancel
        if (path == None or path == ""):
            print("\n> null\nNo file selected.")
            continue
        print("\n> " + path)

        file = open(path, 'r')
        if not file.readable():
            file = None
        file.close()

    return path


# Output file name/path
def promptOutFileName(inPath):
    file = None
    path = None
    while file == None:
        print("\nSelect the output file.\n(A File Browser was opened.)", end="")
        path = tkinter.filedialog.asksaveasfilename(initialdir="./", title="Select the output file.")

        # handle cancel
        if (path == None or path == ""):
            print("\n> null\nNo file selected.")
            continue
        print("\n> " + path)

        # check if same as input
        if path == inPath:
            path = None
            print("Output file was the same as input.")
            continue

        # try to write to validate
        file = open(path, 'w')
        if not file.writable():
            file = None

        # cleanup
        file.close()

    return path


REGEX_PASSWORD = "^(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*[\d]+)(?=.*[^A-za-z\d]+)[ -~]{8,50}$"


# Password, hashed and salted
def promptPassword():
    success = False
    while success == False:
        # regex
        password = doRetreiveInput(
            "Ender a password",
            "8 to 50 chars & must include:\n\t-Only ASCII\n\t-An uppercase letter\n\t-An lowers letter\n\t-A number\n\t-A special char",
            REGEX_PASSWORD,
            None
        )

        # extremely true to life async HTTPS request and response parsing
        response = json.loads(ServerSide.WebServiceAndDBMS.authPost(password))
        if response["success"] == True and response["message"] == "Password stored.":
            success = True
        else:
            print("No match found.")
    return


# Password verification by hashed and salted then compare
def promptPasswordRetype():
    success = False
    while success == False:
        # regex
        password = doRetreiveInput(
            "Retype your password",
            None,
            REGEX_PASSWORD
        )

        # extremely true to life async HTTPS request and response parsing
        response = json.loads(ServerSide.WebServiceAndDBMS.authGET(password))
        if response["success"] == True and response["message"] == "Password matched.":
            success = True
        else:
            print("No match found.")
    return


# -writes the user's name
# -writes the result of adding the two integer values (no overflow should occur)
# -writes the result of multiplying the two integer values (no overflow should occur)
# -writes the contents of the input file
# -Each thing written should be clearly labeled (e.g. First name, Last name, First Integer, Second Integer, Sum, Product, Input File Name, Input file contents)
def writeOutput(nameFirst, nameLast, int1, int2, inPath, outPath):
    try:
        f = open(outPath, 'w')
        f.write("First Name: " + nameFirst + "\n\n")
        f.write("Last Name: " + nameLast + "\n\n")
        f.write("int1 + int2: " + str(int1 + int2) + "\n\n")  # ints are arbitrary, int1+int2 will not exceed
        f.write("int1 * int2: " + str(int1 * int2) + "\n\n")  # ints are arbitrary, int1*int2 will not exceed
        f.write("Copy of " + inPath + ":\n")
        f.close()

        # copy in to out by byte
        f = open(outPath, 'ab')
        fIn = open(inPath, 'rb')
        f.write(fIn.read())
        f.close()
        fIn.close()
    except Exception as e:
        print("Error while writing to " + outPath)
        print(e)

    # open preview of out
    try: # https://stackoverflow.com/questions/434597/open-document-with-default-os-application-in-python-both-in-windows-and-mac-os
        if platform.system() == 'Darwin':       # macOS
            subprocess.call(('open', outPath))
        elif platform.system() == 'Windows':    # Windows
            os.startfile(outPath)
        else:                                   # linux variants
            subprocess.call(('xdg-open', outPath))
    except:
        print("Error opening a preview of " + outPath)
    return


if __name__ == '__main__':
    print("\nPYTHON VERSION")

    nameFirst = promptNameFirst()
    nameLast = promptNameLast()

    int1 = prompt2Ints1()
    int2 = prompt2Ints2()

    inPath = promptInFileName()
    outPath = promptOutFileName(inPath)

    promptPassword()
    promptPasswordRetype()

    writeOutput(nameFirst, nameLast, int1, int2, inPath, outPath)
