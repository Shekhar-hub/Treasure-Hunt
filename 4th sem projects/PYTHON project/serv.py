from tkinter import *
from time import sleep
import socket
import sys
from PIL import Image,ImageTk
from threading import Thread
import os
dir = os.path.dirname(__file__)
filename = os.path.join(dir, r'project_image\pic.jpg')
s = socket.socket()
host =socket.gethostname()
print(host)
port=8081
s.bind((host, port))
a=[]
def rec_mes():

    while True:
        incoming_message = conn.recv(1024)
        # sleep(5)
        if True:
            incoming_message1 = incoming_message.decode()
            com = ["Client:", "      {}".format(incoming_message1)]

            for i in com:
                a.append(i)
            lb1.delete(0,END)
            for j in range(len(a)):
                lb1.insert(END, a[j])
                if j % 2 == 0:
                    lb1.itemconfig(j, {'fg': 'red'})
def thread_control():

        def send_mes():


            message = textbox1.get("1.0", "end-1c")
            print(message)
            com=["Server:", "      {}".format(message)]


            for i in com:
                a.append(i)
            lb1.delete(0, END)
            for j in range(len(a)):
                lb1.insert(END,a[j])
                if j % 2 == 0:
                    lb1.itemconfig(j, {'fg': 'red'})

            print(a)

            message = message.encode()
            conn.send(message)

        t2 = Thread(target=send_mes)
        t2.start()


def mess():
    global self1
    self1 = Tk()
    f2 = Frame(self1,borderwidth=20,width=0, bg="white", relief=GROOVE)
    f2.grid(row=0, column=0)
    f2.place(x=0, y=585)
    self1.geometry("500x750")
    self1.title("Py-chat")
    name = Label(self1, text="\tWelcome to PY-CHAT", fg="dark green", font="Helvetica 20 bold")
    name.grid(row=1,column=0)
    global lb1
    lb1 = Listbox(self1, width=500, font=35, height=22)
    lb1.grid(row=9, column=0)
    lb1.place(x=0, y=58)
    #name.place(x=0,y=0)

    global textbox1
    can_button = Button(self1, text="SEND", font="5 ", command=thread_control)
    can_button.grid(row=1,column=2)
    can_button.place(x=200,y=710)
    textbox1 = Text(f2, height=5, width=58)
    textbox1.grid(row=0,column=2)
    #textbox1.place(x=0,y=570)
    t31= Thread(target=rec_mes)
    t31.start()
    self1.mainloop()


def waiting():
    global conn, addr
    #a=None
    while True:
        a=s.listen(1)

        if True:

            global conn,addr
            conn, addr = s.accept()
            mess()

def liste():
    t1=Thread(target=waiting)
    t1.start()


#mess()

self=Tk()
self.title("Server")
self.geometry("1550x850")
image = Image.open(filename)
img = image.resize((1530, 790))
photo = ImageTk.PhotoImage(img)
label1 = Label(image=photo)
label1.grid()
label1.place(x=0, y=0)
f2=Frame(self,borderwidth=10)
f2.grid(row=5,column=0)
f2.place(x=670, y=650)
f1=Frame(self,bg="black")
f1.grid(row=5,column=0)
f1.place(x=40, y=60)
b1=Button(f2,text="connect",command=liste,font=40,bg="black",fg="white").grid(row=9,column=0)
self.mainloop()