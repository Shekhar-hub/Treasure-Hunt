import mysql.connector
from datetime import date
from tkinter import *
from PIL import Image,ImageTk
import os
dir = os.path.dirname(__file__)
filename = os.path.join(dir, r'my_image\2.jpg')
filename2 = os.path.join(dir, r'my_image\h.jpg')

mydb=mysql.connector.connect(host='localhost',user='root',passwd='shekhar',database='hospital_management')
today=date.today()
mycursor=mydb.cursor()
def close1():
    exit()
def close():
    starting()
def deleted1():
    query = "delete from staff_infor where name_pat='{}'".format(str(stname))
    mycursor.execute(query)
    mydb.commit()
    staff()
def updated1():
    value_up4 = "update staff_infor set name_pat=%s,gender=%s,age=%s,phone=%s,address=%s,job_name=%s,salary=%s where name_pat='{}'".format(str(stname))
    value_time4 = (stname21.get(), stgender21.get(),stage21.get(),stphone21.get(), staddress21.get(), qual21.get(),sal21.get())
    mycursor.execute(value_up4, value_time4)
    mydb.commit()
    staff()
def added1():

    valuef = "insert into staff_infor(rid_no,name_pat,gender,age,phone,address,job_name,salary,date_reg)values(%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    valuef1 = (stid21.get(), stname21.get(), stgender21.get(),stage21.get(),stphone21.get(), staddress21.get(), qual21.get(),sal21.get(),jdate21.get())
    # print(value1)
    mycursor.execute(valuef, valuef1)
    mydb.commit()
    staff()

def data1():
    global stname
    stname4 = stname2.get()
    strid4 = strid2.get()
    if stname4 == "":
        mycursor.execute("select name_pat from staff_infor where rid_no ='{}'".format(str(strid4)))
    elif strid4 == "":
        mycursor.execute("select name_pat from staff_infor where name_pat='{}'".format(str(stname4)))
    else:
        mycursor.execute("select name_pat from staff_infor where name_pat='{}'and rid_no='{}'".format(str(stname4), str(strid4)))

    myresult = mycursor.fetchone()
    for i in myresult:
        lb2.insert(ACTIVE, f"{i}")

    stname = lb2.get(ACTIVE)
    mycursor.execute("select rid_no,name_pat,gender,age,phone,address,job_name,salary,date_reg from staff_infor where name_pat='{}'".format(str(stname)))
    myresult = mycursor.fetchone()
    stid21.set(myresult[0])
    stname21.set(myresult[1])
    stgender21.set(myresult[2])
    stage21.set(myresult[3])
    stphone21.set(myresult[4])
    staddress21.set(myresult[5])
    qual21.set(myresult[6])
    sal21.set(myresult[7])
    jdate21.set(myresult[8])


def staff():
    global stname2,strid2,lb2,stage21,stname21,stid21,stgender21,qual21,sal21,stphone21,staddress21,jdate21
    f6 = Frame(window, borderwidth=10, bg="white")
    f6.grid(row=5, column=0)
    f6.place(x=400, y=100)
    l = Label(f6, text="\n\n\n", bg="white").grid()
    l30 = Label(f6, text="Staff's Name", bg="white", font=20).grid(row=0, column=0)
    stname2 = StringVar()
    stname3 = Entry(f6, textvariable=stname2).grid(row=1, column=0)
    l = Label(f6, text="\n\n\n", bg="white").grid()
    l31a = Label(f6, text="Staff's Registeration ID", bg="white", font=10).grid(row=2, column=0)
    strid2 = StringVar()
    strid3 = Entry(f6, textvariable=strid2).grid(row=3, column=0)
    but = Button(f6, text="Search",font=20,bg="blue", command=data1).grid(row=4, column=0)
    #l32 = Label(f6, text="\n\n", bg="blue", font=20).grid(row=5, column=0)
    lb2 = Listbox(f6, width=20)
    lb2.grid(row=6, column=0)
    l39 = Label(f6, text="\t\t", bg="white", font=20).grid(row=0, column=2)
    l33 = Label(f6, text="ID", bg="white", font=20).grid(row=0, column=3)
    stid21 = StringVar()
    mycursor.execute("select count(*) from staff_infor")
    myresultn = mycursor.fetchone()
    for row in myresultn:
        abc = row
    abc += 1
    stid21.set(abc)
    stid1 = Entry(f6, textvariable=stid21).grid(row=0, column=6)
    l37 = Label(f6, text="Joining Date", bg="white", font=20).grid(row=1, column=3)
    jdate21 = StringVar()
    jdate21.set(today)
    jdate1 = Entry(f6, textvariable=jdate21).grid(row=1, column=6)
    l34 = Label(f6, text="Name", bg="white", font=20).grid(row=3, column=3)
    stname21 = StringVar()
    stname1 = Entry(f6, textvariable=stname21).grid(row=3, column=6)
    l35 = Label(f6, text="Gender", bg="white", font=20).grid(row=4, column=3)
    stgender21 = StringVar()
    stgender1 = Entry(f6, textvariable=stgender21).grid(row=4, column=6)
    l36 = Label(f6, text="Age", bg="white", font=20).grid(row=5, column=3)
    stage21 = StringVar()
    stage1 = Entry(f6, textvariable=stage21).grid(row=5, column=6)
    l39 = Label(f6, text="Job", bg="white", font=20).grid(row=6, column=3)
    qual21 = StringVar()
    qual1 = Entry(f6, textvariable=qual21).grid(row=6, column=6)
    l38 = Label(f6, text="Salary", bg="white", font=20).grid(row=7, column=3)
    sal21 = StringVar()
    sal1 = Entry(f6, textvariable=sal21).grid(row=7, column=6)
    l37 = Label(f6, text="Phone", bg="white", font=20).grid(row=8, column=3)
    stphone21 = StringVar()
    stphone1 = Entry(f6, textvariable=stphone21).grid(row=8, column=6)
    l38 = Label(f6, text="Address", bg="white", font=20).grid(row=9, column=3)
    staddress21 = StringVar()
    staddress1 = Entry(f6, textvariable=staddress21).grid(row=9, column=6)

    l39 = Label(f6, text="\t\t", bg="white", font=20).grid(row=0, column=14)
    b11 = Button(f6, text="   Add   ",font=20,bg="blue",command=added1).grid(row=2, column=20)
    b12 = Button(f6, text="Update  ",font=20,bg="blue",command=updated1).grid(row=4, column=20)
    b13 = Button(f6, text="Delete  ",font=20,bg="blue",command=deleted1).grid(row=6, column=20)
    b14 = Button(f6, text="Close   ",font=20,bg="blue",command=close).grid(row=8, column=20)
    tem = Label(f6, text="\n\n\n\n\n\n\t\t\t\t\t", bg="white").grid()

def deleted():
    query="delete from patient_reg where name_pat='{}'".format(str(ptname))
    mycursor.execute(query)
    mydb.commit()
    query1="delete from room_infor where name_pat='{}'".format(str(ptname))
    mycursor.execute(query1)
    mydb.commit()
    pat_info1()
def updated():
    value_up="update patient_reg set name_pat=%s,gender=%s,age=%s,phone=%s,address=%s,disease=%s,sod=%s where name_pat='{}'".format(str(ptname))
    value_time=(name21.get(),gender21.get(),age21.get(),phone21.get(),address21.get(),disease21.get(),sod21.get())
    mycursor.execute(value_up, value_time)
    mydb.commit()
    value_up1 = "update room_infor set name_pat=%s,building=%s,room_type=%s,room_no=%s where name_pat='{}'".format(str(ptname))
    value_time1=(name21.get(),building21.get(),rtype21.get(),rno21.get())
    mycursor.execute(value_up1,value_time1)
    mydb.commit()
    pat_info()
def added():
    value = "insert into patient_reg(rid_no,date_reg,pid,name_pat,gender,age,phone,address,disease,sod)values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)"
    value1 = (no.get(),date.get(),pid.get(),name.get(),gender.get(),age.get(),phone.get(),address.get(),disease.get(),sod.get())
    #print(value1)
    mycursor.execute(value,value1)
    mydb.commit()
    value2="insert into room_infor(rid_no,name_pat,building,room_type,room_no,room_price)values(%s,%s,%s,%s,%s,%s)"
    value3=(no.get(),name.get(),building.get(),rtype.get(),rno.get(),price.get())
    mycursor.execute(value2, value3)
    mydb.commit()
    pat_reg()

def data():
    global ptname
    pname4=pname2.get()

    rid4=rid2.get()
    if pname4=="":
        mycursor.execute("select name_pat from patient_reg where rid_no ='{}'".format(str(rid4)))
    elif rid4=="":
        mycursor.execute("select name_pat from patient_reg where name_pat='{}'".format(str(pname4)))
    else:
        mycursor.execute("select name_pat from patient_reg where name_pat='{}'and rid_no='{}'".format(str(pname4),str(rid4)))
    myresult = mycursor.fetchone()
    for i in myresult:
        lb1.insert(ACTIVE,f"{i}")

    ptname=lb1.get(ACTIVE)

    mycursor.execute("select pid,name_pat,gender,age,phone,address,disease,sod,date_reg from patient_reg where name_pat='{}'".format(str(ptname)))
    myresult = mycursor.fetchone()
    pid21.set(myresult[0])
    name21.set(myresult[1])
    gender21.set(myresult[2])
    age21.set(myresult[3])
    phone21.set(myresult[4])
    address21.set(myresult[5])
    disease21.set(myresult[6])
    sod21.set(myresult[7])
    date21.set(myresult[8])

    mycursor.execute("select building,room_type,room_no from room_infor where name_pat='{}'".format(str(ptname)))
    myresult1 = mycursor.fetchone()
    building21.set(myresult1[0])
    rtype21.set(myresult1[1])
    rno21.set(myresult1[2])

def pat_info1():
    pat_info()
    l12 = Label(f4, text="Check Out", bg="white", font=20).grid(row=7, column=9)
    dateout = StringVar()
    dateout.set(today)
    date2 = Entry(f4, textvariable=dateout).grid(row=7, column=12)

    b9 = Button(f4, text=" Delete ",font=20,bg="blue", command=deleted).grid(row=8, column=12)
    b10 = Button(f4, text="Close",font=20,bg="blue",command=close).grid(row=8, column=14)



def pat_info():
    global f4,rid2,pname2,lb1,pid21,name21,gender21,age21,phone21,address21,disease21,sod21,building21,rno21,rtype21,date21
    f4 = Frame(window, borderwidth=10, bg="white")
    f4.grid(row=5, column=0)
    f4.place(x=400, y=100)
    l = Label(f4, text="\n\n\n",bg="white").grid()
    l27 = Label(f4, text="Patient's Name", bg="white",font=20).grid(row=0, column=0)
    pname2= StringVar()
    pname3 = Entry(f4, textvariable=pname2).grid(row=1, column=0)
    l=Label(f4,text="\n\n\n",bg="white").grid()
    l27a = Label(f4, text="Patient's Registeration ID", bg="white", font=10).grid(row=2, column=0)
    rid2 = StringVar()
    rid3 = Entry(f4, textvariable=rid2).grid(row=3, column=0)
    but = Button(f4, text="Search",font=20,bg="blue",command=data).grid(row=4, column=0)
    l27 = Label(f4, text="\n\n", bg="white", font=20).grid(row=5, column=0)
    lb1 = Listbox(f4, width=20)
    lb1.grid(row=6, column=0)
    l14 = Label(f4, text="PID", bg="white", font=20).grid(row=2, column=3)
    pid21 = StringVar()
    pid1 = Entry(f4, textvariable=pid21).grid(row=2, column=6)
    l12 = Label(f4, text="Name", bg="white", font=20).grid(row=3, column=3)
    name21 = StringVar()
    name1 = Entry(f4, textvariable=name21).grid(row=3, column=6)
    l15 = Label(f4, text="Gender", bg="white", font=20).grid(row=4, column=3)
    gender21 = StringVar()
    gender1 = Entry(f4, textvariable=gender21).grid(row=4, column=6)
    l16 = Label(f4, text="Age", bg="white", font=20).grid(row=5, column=3)
    age21 = StringVar()
    age1 = Entry(f4, textvariable=age21).grid(row=5, column=6)
    l17 = Label(f4, text="Phone", bg="white", font=20).grid(row=6, column=3)
    phone21 = StringVar()
    phone1 = Entry(f4, textvariable=phone21).grid(row=6, column=6)
    l18 = Label(f4, text="Address", bg="white", font=20).grid(row=7, column=3)
    address21 = StringVar()
    address1 = Entry(f4, textvariable=address21).grid(row=7, column=6)
    l19 = Label(f4, text="Disease", bg="white", font=20).grid(row=8, column=3)
    disease21 = StringVar()
    disease1 = Entry(f4, textvariable=disease21).grid(row=8, column=6)
    lt5=Label(f4,text="\t\t\t\t\t\t",bg="white").grid(row=2,column=9)
    l20 = Label(f4, text="Status Of Disease", bg="white", font=20).grid(row=2, column=9)
    sod21 = StringVar()
    sod1 = Entry(f4, textvariable=sod21).grid(row=2, column=12)
    l23 = Label(f4, text="Building", bg="white", font=20).grid(row=3, column=9)
    building21 = StringVar()
    building1 = Entry(f4, textvariable=building21).grid(row=3, column=12)
    l24 = Label(f4, text="Room No.", bg="white", font=20).grid(row=4, column=9)
    rno21 = StringVar()
    rno1 = Entry(f4, textvariable=rno21).grid(row=4, column=12)
    l25 = Label(f4, text="Room Type", bg="white", font=20).grid(row=5, column=9)
    rtype21 = StringVar()
    rtype1 = Entry(f4, textvariable=rtype21).grid(row=5, column=12)
    l26 = Label(f4, text="Check In", bg="white", font=20).grid(row=6, column=9)
    date21 = StringVar()
    date1 = Entry(f4, textvariable=date21).grid(row=6, column=12)
    b9 = Button(f4, text="Update",bg="blue", command=updated,font=20).grid(row=8, column=12)
    b10 = Button(f4, text="Close",font=20,bg="blue",command=close).grid(row=8, column=14)
    tem = Label(f4, text="\n\n\n\n\n\n\t\t\t\t\t", bg="white").grid()
    #b3 = Button(f4, text=" Add ", font=20, command=added).grid(row=14, column=12)
    #b3 = Button(f4, text="Close", font=20).grid(row=14, column=14)



def pat_reg():
    global no,date,pid,name,gender,age,phone,address,disease,sod,rtype,building,rno,rtype3,price
    f3 = Frame(window, borderwidth=5, bg="white")
    f3.grid(row=5, column=0)
    f3.place(x=400, y=100)
    t = Label(f3, text="\n\n\n\n", bg="white").grid()
    l11=Label(f3,text="Registration ID",bg="white",font=20).grid(row=6,column=0)
    t = Label(f3, text="\n", bg="white").grid()
    l12 = Label(f3, text="No.", bg="white", font=20).grid(row=8, column=0)
    no=StringVar()
    mycursor.execute("SELECT rid_no FROM patient_reg ORDER BY rid_no DESC LIMIT 1")
    myresult = mycursor.fetchone()
    if myresult is None:
        a=0
    else:
        for row in myresult:
            a= row
    a += 1
    no.set(a)

    no1=Entry(f3,textvariable=no).grid(row=8,column=3)
    l12 = Label(f3, text="Date", bg="white", font=20).grid(row=10, column=0)
    date=StringVar()
    date.set(today)
    date2=Entry(f3,textvariable=date).grid(row=10,column=3)
    #lt = Label(f3, text="", bg="blue").grid()
    l13=Label(f3,text="Patient's Information",bg="white",font=20).grid(row=15,column=0)
    lt=Label(f3,text="\n",bg="white").grid()
    l14 = Label(f3, text="PID", bg="white", font=20).grid(row=24, column=0)
    pid=StringVar()
    mycursor.execute("SELECT pid FROM patient_reg where date_reg='{}' ORDER BY pid DESC LIMIT 1".format(str(today)))
    myresult = mycursor.fetchone()
    if myresult is None:
        b=0
    else:
        for i in myresult:
            b=i
    b+=1
    pid.set(b)
    pid1 = Entry(f3, textvariable=pid).grid(row=24, column=3)

    l12 = Label(f3, text="Name", bg="white", font=20).grid(row=28, column=0)
    name = StringVar()
    name1 = Entry(f3, textvariable=name).grid(row=28, column=3)

    l15 = Label(f3, text="Gender", bg="white", font=20).grid(row=32, column=0)
    gender= StringVar()
    gender1 = Entry(f3, textvariable=gender).grid(row=32, column=3)

    l16 = Label(f3, text="Age", bg="white", font=20).grid(row=36, column=0)
    age= StringVar()
    age1 = Entry(f3, textvariable=age).grid(row=36, column=3)

    l17 = Label(f3, text="Phone", bg="white", font=20).grid(row=40, column=0)
    phone = StringVar()
    phone1 = Entry(f3, textvariable=phone).grid(row=40, column=3)

    l18 = Label(f3, text="Address", bg="white", font=20).grid(row=44, column=0)
    address = StringVar()
    address1 = Entry(f3, textvariable=address).grid(row=44, column=3,ipady=20)

    l19 = Label(f3, text="Disease", bg="white", font=20).grid(row=48, column=0)
    disease = StringVar()
    disease1 = Entry(f3, textvariable=disease).grid(row=48, column=3)
    l20 = Label(f3, text="Status Of Disease", bg="white", font=20).grid(row=52, column=0)
    sod= StringVar()
    sod1 = Entry(f3, textvariable=sod).grid(row=52, column=3)
    t = Label(f3, text="\n\n\n\n", bg="white").grid(row=0,column=14)
    t = Label(f3, text="\t\t\t\t\t\t\t", bg="white").grid(row=0, column=14)
    l21 = Label(f3, text="Room Type", bg="white",font=20).grid(row=6, column=14)
    rtype=StringVar()
    rtype1=Radiobutton(f3,text="Normal",variable=rtype,value="Normal",bg="white",font=20).grid(row=8,column=14)
    rtype2=Radiobutton(f3,text="Medium",variable=rtype,value="Medium",bg="white",font=20).grid(row=9,column=14)
    rtype4=Radiobutton(f3,text="VIP     ",variable=rtype,value="VIP",justify=LEFT, bg="white", font=20).grid(row=10, column=14)
    # t = Label(f3, text="\n\n\n\n", bg="blue").grid(row=0, column=16)
    l22 = Label(f3, text="Room's Information", bg="white", font=20).grid(row=6, column=16)
    l23 = Label(f3, text="Building", bg="white", font=20).grid(row=8, column=16)
    building = StringVar()
    building1 = Entry(f3, textvariable=building).grid(row=8, column=18)
    l24 = Label(f3, text="Room No.", bg="white",font=20).grid(row=9, column=16)
    rno = StringVar()
    rno1 = Entry(f3, textvariable=rno).grid(row=9, column=18)
    l25 = Label(f3, text="Room Type", bg="white", font=20).grid(row=10, column=16)
    rtype3 = StringVar()
    rtype12 = Entry(f3, textvariable=rtype3).grid(row=10, column=18)
    l26 = Label(f3, text="Price", bg="white", font=20).grid(row=11, column=16)
    price = StringVar()
    price1 = Entry(f3, textvariable=price).grid(row=11, column=18)
    b3=Button(f3,text=" Add ",font=20,bg="blue",command=added).grid(row=14,column=16)
    b3 = Button(f3, text="Close", font=20,bg="blue",command=close).grid(row=14, column=18)

def logged():
    if uservalue.get()=="project" and passvalue.get()=="rdbms":
        root.destroy()
        global window
        window=Tk()
        window.geometry("1550x850")
        window.title("ABC Hospital")
        starting()
def starting():
        f2= Frame(window, borderwidth=10, bg="blue")

        f2.grid(row=5, column=0)
        f2.place(x=0, y=70)
        image = Image.open(filename2)
        img = image.resize((1280,750))
        photo = ImageTk.PhotoImage(img)
        label1 = Label(image=photo)
        label1.grid()
        label1.place(x=248, y=75)
        l1=Label(window,text="ABC Hospital\t\t\t\t\t\t\t\t\t\t\t\t\t",foreground="white",bg="blue",font=("Helvetica", 20, "bold "),pady=20).grid(row=0,column=0)
        #l10 = Label(f2, text="\n\n", bg="blue", font=10).grid()
        b1=Button(f2, text="Patient Registration   ",bg="blue",borderwidth=10,relief=GROOVE,font=15,command=pat_reg).grid(row=0,column=0)
        l10= Label(f2, text="\n\n\n", bg="blue", font=10).grid()
        b3=Button(f2, text="Patient Information    ",bg="blue",borderwidth=10,relief=GROOVE,font=15,command=pat_info).grid(row=8,column=0)
        l10 = Label(f2, text="\n\n\n", bg="blue", font=10).grid()
        b4=Button(f2, text="Patient CheckOut       ",bg="blue",borderwidth=10,relief=GROOVE,font=15,command=pat_info1).grid(row=12,column=0)
        l10 = Label(f2, text="\n\n\n", bg="blue", font=10).grid()
        b7=Button(f2, text="Staff Information        ",bg="blue",borderwidth=10,relief=GROOVE,font=15,command=staff).grid(row=24,column=0)
        l10 = Label(f2, text="\n\n\n", bg="blue", font=10).grid()
        b7 = Button(f2, text="Close ", bg="blue", borderwidth=10, relief=GROOVE, font=15,command=close1).grid(row=28, column=0)
        lt = Label(f2, text="\n\n",bg="blue").grid()
        #b7 = Button(f2, text="Close ", bg="blue", borderwidth=10, relief=GROOVE, font=15).grid(row=28, column=1)
        window.mainloop()


root=Tk()
root.geometry("1550x850")
root.title("ABC Hospital")
image = Image.open(filename)
img = image.resize((1550, 850))
photo = ImageTk.PhotoImage(img)
label1 = Label(image=photo)
label1.grid()
label1.place(x=0, y=0)
f1=Frame(root,borderwidth=20,bg="white",relief=GROOVE)
f1.grid(row=5,column=0)
f1.place(x=900, y=350)
l0=Label(f1,text=" ",bg="white").grid(row=0,column=0)
l2=Label(f1,text="User Name:",bg="white",font=20)
l2.grid(row=6,column=0)
uservalue=StringVar()
passvalue=StringVar()
userentry=Entry(f1,textvariable=uservalue).grid(row=6,column=1)
l5=Label(f1,text="\n",bg="white",font=20)
l5.grid()
l3=Label(f1,text="Password:",bg="white",font=20)
l3.grid(row=8,column=0)
passentry=Entry(f1,show="*",textvariable=passvalue).grid(row=8,column=1)
l4=Label(f1,text="\n\t\t",bg="white",font=20)
l4.grid()
b1=Button(f1,text="Log-in",font="12",bg="blue",command=logged).grid(row=11,column=1)
b2=Button(f1,text="Exit  ",font="12",bg="blue",command=close1).grid(row=11,column=3)
l4=Label(f1,text="\n\t",bg="white",font=20).grid(row=11,column=4)
root.mainloop()
