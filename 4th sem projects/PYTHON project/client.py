import socket
#import sys
from time import sleep
from tkinter import *
from threading import *
from PIL import Image,ImageTk
import  os
dir = os.path.dirname(__file__)
filename = os.path.join(dir, r'project_image\pic.jpg')
root=Tk()
a=[]
def conec():
    host=uservalue.get()
    port = 8081
    s.connect((host,port))
    l3 = Label(f1, text=" Connected to chat server").grid(row=11,column=0)
    sleep(2)
    root.destroy()
    mes2()
def rec_mes2():
    # message =textbox1.get("1.0","end-1c")
    while True:
        incoming_message = s.recv(1024)
        if True:
            incoming_message1 = incoming_message.decode()
            com = ["Server:", "      {}".format(incoming_message1)]

            for i in com:
                a.append(i)
            lb1.delete(0, END)
            for j in range(len(a)):
                lb1.insert(END, a[j])
                if j % 2 == 0:
                    lb1.itemconfig(j, {'fg': 'red'})
def chatpy2():

    def send_mes2():
        message = textbox1.get("1.0", "end-1c")

        com = ["Client:", "      {}".format(message)]

        for i in com:
            a.append(i)
        lb1.delete(0, END)
        for j in range(len(a)):
            lb1.insert(END, a[j])
            if j % 2 == 0:
                lb1.itemconfig(j, {'fg': 'red'})

        message = message.encode()
        s.send(message)
    t4 = Thread(target=send_mes2)
    t4.start()

def mes2():
    global self1
    self1 = Tk()
    f2 = Frame(self1, borderwidth=20, width=0, bg="white", relief=GROOVE)
    f2.grid(row=0, column=0)
    f2.place(x=0, y=585)
    self1.geometry("500x750")
    self1.title("Py-chat2")
    name = Label(self1, text="\tWelcome to PY-CHAT", fg="dark green", font="Helvetica 20 bold")
    name.grid(row=1, column=0)
    global lb1
    lb1 = Listbox(self1, width=500, font=35, height=22)
    lb1.grid(row=9, column=0)
    lb1.place(x=0, y=58)
    # name.place(x=0,y=0)


    global textbox1
    can_button = Button(self1, text="SEND", font="5 ", command=chatpy2)
    can_button.grid(row=1, column=2)
    can_button.place(x=200, y=710)
    textbox1 = Text(f2, height=5, width=58)
    textbox1.grid(row=0, column=2)
    t3 = Thread(target=rec_mes2)
    t3.start()
    self1.mainloop()
s = socket.socket()
root.title("Client")
root.geometry("1550x850")
image = Image.open(filename)
img = image.resize((1530, 790))
photo = ImageTk.PhotoImage(img)
label1 = Label(image=photo)
label1.grid()
label1.place(x=0, y=0)
f1=Frame(root,borderwidth=20,bg="white",relief=GROOVE)
f1.grid(row=5,column=0)
f1.place(x=800, y=350)
l1 = Label(f1, text="Please enter the hostname of the server :\n ",font="Helvetica 20 bold",bg="white").grid(row=6,column=0)
uservalue=StringVar()
userentry = Entry(f1,textvariable=uservalue,width=40,relief=SOLID)
userentry.grid(row=7,column=0)
l1 = Label(f1, text="\n ",bg="white").grid(row=8,column=0)
ll=Button(f1,text="Connect",command=conec,font=20).grid(row=9,column=0)
l1 = Label(f1, text="\n ",bg="white").grid(row=10,column=0)
mainloop()