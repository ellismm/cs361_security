from pwn import *
import random
import struct
from binascii import *
from pwnlib.log import getLogger

runs = 0
def init_cs361(is_remote, s1, s2):
    global r, student1, student2, server
    server = is_remote
    student1 = s1
    student2 = s2
    if(server):
      r = remote("34.73.178.97", 9000)
    else:
      r = process("./encrypt")

    l = getLogger(pwnlib.tubes.process.__name__)
    l.setLevel('error')

    l = getLogger(pwnlib.tubes.remote.__name__)
    l.setLevel('error')

#Makes query to remote grading server
#Sends a byte to encrypt and returns the byte encrypted using the server's key
#Accepts int [0,255] as parameter returns int [0,255]
#This query can be made at most 100 times per server key cycle
#If query is made more than 100 times the server link will die
def get_actual(byte):
    byte = "%x" % byte
    r.recvline()
    r.sendline('e')
    r.recvline()
    r.sendline(byte)
    y = r.recvline()
    if(not r.connected()):
        print("The server link died. You probably made too many remote requests.")
    global runs
    runs+=1
    print("a")
    return int(y.rstrip().decode('ascii'), 16)

#Makes query to local encryption program
#Sends a byte to encrypt and returns the byte encrypted using a user specified key
#Accepts int [0,255] as byte parameter returns int [0,255]
#Accepts int [0,65535] as key parameter
def get_test(byte, key):
    byte = "%x" % byte
    r2 = process("./encrypt_user_key")
    r2.sendline("%x" % key);
    r2.recvline();
    r2.sendline(byte);
    x = r2.recvline()
    r2.close()
    global runs
    runs+=1
    return int(x.rstrip().decode('ascii'), 16)

#Submit a key to solve the current key cycle
#If the key is incorrect the server link will die
#If it is correct the next key cycle will start
def solve(key):
    key = "%x" % key
    r.recvline()
    r.sendline('s')
    r.recvline()
    r.sendline(key)
    x = r.recvline()
    if(x != b'Solved!\n'):
        print("Incorrect guess")
        exit()

#After the 100th solved key cycle this function should be called to allow server output to be redirected to user stdout
def interactive():
    r.sendline(student1)
    r.sendline(student2)
    r.recvline()
    r.recvline()
    print(r.recvline().rstrip().decode('ascii'))
    if(not server):
        print("The server was run locally, make remote=True to run on grading server")
