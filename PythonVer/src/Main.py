import re

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

NAME_REGEX = "^[a-z0-9\s'\.\-]+$"
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
def prompt2Ints():
    return

# Input file name/path
def promptInFileName():
    return

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
    nameFirst = promptNameFirst()
    nameLast = promptNameLast()