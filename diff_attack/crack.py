from cs361utils import *
from random import randint

#Fill in your name and your partner name here
#If you have no partner make name2 = "N/A"
student_name_1 = "Alica Apples"
student_name_2 = "Bob Bananas"

#Sbox from the server encryption algorithm
#To convert a byte x to sbox(x) look at the xth number in the array
#sbox(x) = sbox[x]
sbox = [99, 244, 87, 39, 15, 37, 137, 143, 195, 38, 242, 203, 156, 124, 58, 69, 122, 116, 82, 226, 177, 184, 165, 11, 241, 57, 205, 253, 232, 146, 26, 95, 222, 1, 75, 148, 186, 129, 2, 255, 194, 121, 46, 27, 214, 206, 35, 73, 48, 200, 16, 170, 252, 18, 155, 227, 106, 96, 44, 78, 239, 158, 104, 196, 93, 79, 160, 29, 139, 166, 159, 212, 172, 204, 151, 91, 175, 84, 225, 213, 173, 237, 185, 76, 81, 198, 33, 230, 4, 149, 53, 115, 118, 80, 72, 64, 74, 240, 218, 162, 238, 215, 220, 251, 152, 130, 45, 191, 83, 65, 145, 51, 90, 62, 243, 114, 43, 88, 6, 236, 234, 85, 71, 28, 63, 192, 135, 233, 66, 126, 56, 178, 52, 157, 216, 217, 24, 164, 229, 193, 249, 60, 187, 8, 197, 67, 181, 100, 10, 107, 211, 127, 210, 89, 209, 55, 21, 20, 161, 188, 25, 32, 183, 36, 23, 47, 86, 132, 219, 19, 199, 59, 9, 201, 176, 190, 13, 117, 97, 40, 0, 3, 123, 248, 42, 250, 169, 30, 133, 140, 61, 101, 254, 108, 125, 113, 150, 22, 77, 202, 228, 7, 167, 182, 223, 70, 147, 41, 34, 5, 105, 94, 102, 189, 98, 154, 134, 235, 163, 14, 207, 109, 68, 247, 17, 245, 128, 120, 221, 92, 168, 103, 142, 138, 54, 208, 110, 49, 231, 179, 246, 153, 50, 31, 112, 111, 141, 136, 12, 144, 224, 174, 171, 180, 119, 131];

#Delete this function and write something better :)
def guess_rand():
    #Generate random guess for the server's key
    guess = randint(0, 65536)

    #Generate random plaintext
    test_plaintext = randint(0, 256)

    #Get server's encryption of test_plaintext
    #This function can be called 100 times per key cycle
    real_cipher = get_actual(test_plaintext)

    #Get encryption of test_plaintext when guess is used as key
    #Does not make a request to server
    fake_cipher = get_test(test_plaintext, guess)

    print("%x %x %x" % (real_cipher, fake_cipher, guess))

    #Submit guess for key to server
    #If this is correct the server will generate a new key
    #If this is incorrect the server link will die and your program will stop
    solve(guess)

#This function is called once for every key cycle
#Each key cycle allows 100 server queries
#The solve() function should be called EXACTLY ONCE on execution of this function
def diff_attack():
    guess_rand()

#If this is false the server will be simulated locally on your machine for offline testing
#If true your code will interact with the remote grading server
#Remote testing is much slower, rely on local testing until your code works
remote = False

#Starts off the key solving
#Do not change code below this comment :)
init_cs361(remote, student_name_1, student_name_2)

with log.progress('Checking your code') as prog:
    for i in range(10):
       diff_attack()
interactive()
