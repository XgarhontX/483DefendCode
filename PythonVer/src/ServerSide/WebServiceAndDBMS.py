import bcrypt
import hashlib
import re

class DBMS:
    def __init__(self):
        self.dbms_passwordHashed = ""
        self.dbms_passwordSalt = ""

myDBMS = DBMS()

REGEX_PASSWORD = "^(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*[\d]+)(?=.*[^A-za-z\d]+)[ -~]{8,50}$"
def doRegex(password):
    regexc = re.compile(REGEX_PASSWORD)
    result = regexc.match(password)
    if result == None:
        print("No match found.")
        return False
    return True
# https://www.geeksforgeeks.org/how-to-hash-passwords-in-python/
def authPost(password):
    # regex check
    if not (password):
        return "{\n" + "  \"success\": false,\n" + "  \"message\": \"Password doesn't meet requirements.\"\n" + "}"
    # passwrod to bytes
    password = bytes(password, 'utf-8')
    # salt
    salt = bcrypt.gensalt()
    myDBMS.dbms_passwordSalt = salt
    # hash password
    hash = bcrypt.hashpw(password, salt)
    myDBMS.dbms_passwordHashed = hash
    return "{\n" +  "  \"success\": true,\n" +"  \"message\": \"Password stored.\"\n" + "}"
def authGET(password):
    failedResponse = "{\n" + "  \"success\": false,\n" + "  \"message\": \"Password doesn't match.\"\n" + "}"
    # regex check
    if not doRegex(password):
        return failedResponse
    # hask and check
    password = bytes(password, 'utf-8')
    hash = bcrypt.hashpw(password, myDBMS.dbms_passwordSalt)
    if hash != myDBMS.dbms_passwordHashed:
        return failedResponse
    return "{\n" + "  \"success\": true,\n" + "  \"message\": \"Password matched.\"\n" + "}";