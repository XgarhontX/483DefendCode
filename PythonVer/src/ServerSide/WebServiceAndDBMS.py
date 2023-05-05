import bcrypt
import hashlib
import re

REGEX_PASSWORD = "^(?=.*[A-Z]+)(?=.*[a-z]+)(?=.*[\d]+)(?=.*[^A-za-z\d]+)[ -~]{8,50}$"
class DBMS:
    def __init__(self):
        self.dbms_passwordHashed = ""
        self.dbms_passwordSalt = ""
class WebServiceConnection:
    def __init__(self):
        self.myDBMS = DBMS()

    def doRegex(self, password):
        regexc = re.compile(REGEX_PASSWORD)
        result = regexc.match(password)
        if result == None:
            print("No match found.")
            return False
        return True

    # https://www.geeksforgeeks.org/how-to-hash-passwords-in-python/
    def authPost(self, password):
        # regex check
        if not self.doRegex(password):
            return "{\n" + "  \"success\": false,\n" + "  \"message\": \"Password doesn't meet requirements.\"\n" + "}"

        # passwrod to bytes
        password = bytes(password, 'utf-8')
        # salt
        salt = bcrypt.gensalt()
        self.myDBMS.dbms_passwordSalt = salt
        # hash password
        hash = bcrypt.hashpw(password, salt)
        self.myDBMS.dbms_passwordHashed = hash

        return "{\n" +  "  \"success\": true,\n" +"  \"message\": \"Password stored.\"\n" + "}"

    def authGET(self, password):
        failedResponse = "{\n" + "  \"success\": false,\n" + "  \"message\": \"Password doesn't match.\"\n" + "}"

        # regex check
        if not self.doRegex(password):
            return failedResponse

        # hask and check
        password = bytes(password, 'utf-8')
        hash = bcrypt.hashpw(password, self.myDBMS.dbms_passwordSalt)
        if hash != self.myDBMS.dbms_passwordHashed:
            return failedResponse

        return "{\n" + "  \"success\": true,\n" + "  \"message\": \"Password matched.\"\n" + "}";

# print(authPost("WAsdwasd123!"))
# print(str(myDBMS.dbms_passwordHashed) + ", " + str(myDBMS.dbms_passwordSalt))
# print(authGET("WAsdwasd123!1"))
# print(authGET("WAsdwasd123!"))