global pk1
global pk2
import smtplib
from math import *
p=int(input('Enter prime p: '))
q=int(input('Enter prime q: '))
print("Choosen primes:\np=" + str(p) + ", q=" + str(q) + "\n")
n=p*q
pk2=n
print("n = p * q = " + str(n) + "\n")
phi=(p-1)*(q-1)
print("Euler's function (totient) [phi(n)]: " + str(phi) + "\n")
def gcd(a, b):
    while b != 0:
        c = a % b
        a = b
        b = c
    return a
def modinv(a, m):
    for x in range(1, m):
        if (a * x) % m == 1:
            return x
    return None
def coprimes(a):
    l = []
    for x in range(2, a):
        if gcd(a, x) == 1 and modinv(x,phi) != None:
            l.append(x)
    for x in l:
        if x == modinv(x,phi):
            l.remove(x)
    return l
print("Choose an value for e from a below coprimes array:\n")
print(str(coprimes(phi)) + "\n")
e=int(input())
d=modinv(e,phi)
pk1=d
print("\nYour public key is a pair of numbers (e=" + str(e) + ", n=" + str(n) + ").\n")
#print("Your private key is a pair of numbers (d=" + str(d) + ", n=" + str(n) + ").\n")
def encrypt_block(m):
    c = modinv(m**e, n)
    if c == None: print('No modular multiplicative inverse for block ' + str(m) + '.')
    return c
def decrypt_block(c):
    m = modinv(c**d, n)
    if m == None: print('No modular multiplicative inverse for block ' + str(c) + '.')
    return m
def encrypt_string(s):
    return ''.join([chr(encrypt_block(ord(x))) for x in list(s)])
import smtplib
def sendsms():
    mail=input("please enter your mail")
    s=smtplib.SMTP('smtp.gmail.com',587)
    s.starttls()
    s.login("riteshvohra42@gmail.com","clashofclan53")
    message="d="+str(pk1)+" "+"n="+str(pk2)
    s.sendmail("riteshvohra42@gmail.com",mail,message)
    s.quit()
sendsms()
def decrypt_string(s):
    return ''.join([chr(decrypt_block(ord(x))) for x in list(s)])

s = input("Enter alphabetical message to encrypt: ")
print("\nPlain message: " + s + "\n")
enc = encrypt_string(s)
print("Encrypted message: " + enc + "\n")
print("For decrypting message plz enter your private key:")
pd=int(input("Plz enter value for d="))
pn=int(input("plz enter value for n="))
if (pd==d and pn==n):
   dec = decrypt_string(enc)
   print("Decrypted message: " + dec + "\n")
