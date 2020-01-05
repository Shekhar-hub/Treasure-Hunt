#include<iostream.h>
#include<fstream.h>
#include<conio.h>
#include<stdio.h>
#include<process.h>
#include<dos.h>

fstream f1,f2,f3,f4,f6,f7;
class student
{
public:
char name[50],fname[30],address[500],department[30];
long int rn, reg,pno,sem,l;
void input()
{
cout<<"Enter Student Name:";
gets(name);
cout<<"Enter Father Name:";
gets(fname);
cout<<"Enter Student Roll No:";
cin>>rn;
cout<<"Enter Address:";
gets(address);
cout<<"Enter Registeration No:";
cin>>reg;
cout<<"Enter Department:";
gets(department);
cout<<"Enter Phone No:";
cin>>pno;
cout<<"Enter Semester:";
cin>>sem;
}
void display()
{
cout<<"NAME                :";
puts(name);
cout<<"FATHER-NAME         :";
puts(fname);
cout<<"ROLL-NO             :"<<rn;
cout<<"\nADDRESS             :";
puts(address);
cout<<"REGISTERATION-No    :"<<reg;
cout<<"\nDEPARTMENT          :";
puts(department);
cout<<"PHONE-NO            :"<<pno;
cout<<"\nSEMESTER            :"<<sem;
}
};
void main()
{
student A;
long int n,rn1,m,i,ch,s,g,j,y;
char d,u[5]="abc",r[5],p[5]="acet",p1[5];
textbackground(WHITE);
textcolor(RED);
clrscr();
cout<<"################################################################################\n";
cout<<"\n\n";
cout<<"\t  ///////////    ///////////   ////////////    /////////////// \n";
cout<<"\t  //      //     //            //                    //         \n";
cout<<"\t  //      //     //            //                    //        \n";
cout<<"\t  //      //     //            //                    //        \n";
cout<<"\t  //      //     //            ////////////          //        \n";
cout<<"\t  /////////      //            //                    //        \n";
cout<<"\t  //      //     //            //                    //        \n";
cout<<"\t  //      //     //            //                    //        \n";
cout<<"\t  //      //     ///////////   ////////////          //        \n";
cout<<"\n\n\n";
cout<<"\t\t    ///////////////////////////////// \n";
cout<<"\t\t    //  STUDENT DATABASE MANAGEMENT SYSTEM   //\n";
cout<<"\t\t    ///////////////////////////////// \n";
cout<<"\n";
cout<<"\t\t       Press Enter to Continue.......\n";
cout<<"\n################################################################################\n";
getch();
mno:
textbackground(WHITE);
textcolor(BLUE);
clrscr();
cout<<endl<<"\t\t\t====================\n\t\t\tDASHBOARD LOGIN"<<endl<<"\t\t\t===================="<<endl<<endl;
cout<<endl<<endl<<endl<<"\t\tUSERNAME::";
gets(r);
cout<<endl<<endl<<"\t\tPASSWORD::" ;
//j=p;
i=0;
while(i<4)
{
p1[i]=getch();
cout<<"*";
i++;}
int v=0,q=0;
for(i=0;i<4;i++)
{ if(p1[i]==p[i])
{v++;}}
for(i=0;i<3;i++)
{ if(r[i]==u[i])
{q++;}}

if(v==4 && q==3)
{ cout<<"\n";
goto sd;
}
else
{
cout<<endl<<endl<<endl<<"\t\t\t\t::!!!Invalid Password/Username";
cout<<"\n ..::Try again?\n\n\t[1] Yes\t\t[0] No\n\t";
cin>>s;
if(s==1)
{
goto mno;
}
else
{
exit(1);
}
}
sd:
cout<<endl<<endl<<endl<<"\t\t\t\t..::SUCCESSFULLY LOGIN..........";
delay(1000);
 textbackground(WHITE);
 textcolor(RED);
clrscr();
cout<<"\n\n\n\n\n\n\n\n\n\n\n\t\t**** Welcome to Student Record Management System ****";
cout<<endl<<endl<<endl<<"\tLoading:";
for(i=0; i<20; i++)
{      cout<<"...";
       delay(50);
	}
abc:
 textbackground(WHITE);
 textcolor(RED);
clrscr();
cout<<"\n\t **** Welcome to Student Record Management System ****";
cout<<"\n\n\n\t\t\tMAIN MENU\n\t\t=====================\n\t\t[1] Add Record\n\t\t[2] Search Record\n\t\t[3] View All Records\n\t\t[4] Update Record\n\t\t[5] Delete Record\n\t\t[6] Exit\n\t\t=================\n\t\t";
cout<<"Enter Your Choice:";
cin>>n;
switch(n)
{
case 777:
abd:
textbackground(RED);
textcolor(WHITE);
cout<<endl<<"\t\t::Enter Your Choice:";
cout<<endl<<endl<<endl<<endl<<"     [0] Main Menu                 [5] Exit";
cout<<endl;
cin>>ch;
clrscr();
if(ch==0)
{
goto abc;
}
else
{
 textbackground(WHITE);
 textcolor(RED);
cout<<"\n################################################################################\n";
cout<<endl<<endl<<endl<<"\t\tAre You Sure You Want To Exit(y/n):     ";
cin>>d;
//cout<<"\n\n\n\n\n\n\n################################################################################\n";
if(d=='y')
{
cout<<"\n\n\n\n\t\t**************THANK YOU*********************\n\n\n\n\n";
for(i=0; i<70; i++)
{
cout<<"#";
       delay(40);
	}
exit(1);
}}
goto abc;
break;
case 1:
textbackground(WHITE);
textcolor(RED);
clrscr();
cout<<"\n\t\t================================\n\t\t\tADD RECORD\n\t\t================================\n\n";
cout<<"Enter No of Records To Add::::";
cin>>m;
textbackground(WHITE);
textcolor(RED);
clrscr();
cout<<"\n\t\t================================\n\t\t\tADD RECORD\n\t\t================================\n\n";
f4.open("def12.dat",ios::app|ios::binary);
for(i=0;i<m;i++)
{
A.input();
f4.write((char *)&A,sizeof(A));
cout<<endl<<endl<<"\t\tData Saved Successfully"<<endl<<endl;
clrscr();
}
f4.close();
goto abd;
break;
case 2:
cde:
textbackground(WHITE);
textcolor(RED);
clrscr();
f1.open("def12.dat",ios::in|ios::binary);
cout<<"\n\n\t..::RECORD SEARCH\n\t===========================\n\t..::Roll No of Student to search: ";
cin>>rn1;
clrscr();
f1.seekg(0);
while(f1.read((char *)&A,sizeof(A)))
{
if(rn1==A.rn)
{
textbackground(WHITE);
textcolor(RED);
cout<<"\n\n..::Search result for\t<<" <<rn1<<">>\n===================================================\n";
A.display();
getch();
f1.close();
goto abd;
getch();
exit(1);
}}
f1.close();
textbackground(WHITE);
textcolor(RED);
cout<<"\n\n..::Search result for\t"<<rn1<<"\n===================================================\n";
cout<<"\n..::No match found!";
cout<<"\n ..::Try again?\n\n\t[1] Yes\t\t[0] No\n\t";
cin>>s;
if(s==1)
{
goto cde;
}
else
{
f1.close();
goto abd;
}
break;
case 3:
clrscr();
f6.open("def12.dat",ios::in|ios::binary);
cout<<"\n\t\t================================\n\t\t\tLIST OF RECORDS\n\t\t================================";
//f1.seekg(0);
while(f6.read((char *)&A,sizeof(A)))
{
textbackground(WHITE);
textcolor(RED);
cout<<"\n=================================================================\n\n";
A.display();
getch();
}
f6.close();
goto abd;
break;
case 4:
wer:
textbackground(WHITE);
textcolor(RED);
clrscr();
f7.open("def12.dat",ios::in|ios::out|ios::binary);
cout<<"\n\n\t..::RECORD UPDATE\n\t===========================\n\t..::Roll No of Student to Update Record: ";
cin>>rn1;
clrscr();
while(f7.read((char *)&A,sizeof(A)))
{
f7.seekg(0,ios::cur);
if(rn1==A.rn)
{
textbackground(WHITE);
textcolor(RED);
int pos=f7.tellg()-sizeof(A);
f7.seekp(pos,ios::beg);
cout<<endl<<"Write To Update Data:"<<endl<<endl<<endl;
A.input();
f7.write((char *)&A,sizeof(A));
cout<<endl<<endl<<endl<<"Record Updated Successfully"<<endl<<endl;
f7.close();
goto abd;
getch();
exit(1);
}}
textbackground(WHITE);
textcolor(RED);
cout<<"\n\n..::Search result for  \n===================================================\n";
cout<<"\n..::No match found!";
cout<<"\n ..::Try again?\n\n\t[1] Yes\t\t[0] No\n\t";
cin>>s;
if(s==1)
{
goto wer;
}
else
{
f7.close();
goto abd;
}
break;
case 5:
//int rn1;
textbackground(WHITE);
textcolor(RED);
clrscr();
//fstream f2;
f3.open("def12.dat",ios::in|ios::binary);
f2.open("temp125.dat",ios::out|ios::binary|ios::ate|ios::in);
cout<<"\n\n\t..::RECORD DELETE\n\t===========================\n\t..::Roll No of Student to Delete Record: ";
cin>>rn1;
clrscr();
while(f3.read((char*)&A,sizeof(A)))
{
if(rn1!=A.rn)
{
textbackground(WHITE);
textcolor(RED);
//ofstream f1;
//f1.open("record3.txt");
//A.display();
f2.write((char*)&A,sizeof(A));
}}
f3.close();
f2.close();
remove("def12.dat");
rename("temp125.dat","def12.dat");
cout<<endl<<"Record Deleted Successfully"<<endl<<endl;
goto abd;
break;
case 6:
cout<<endl<<endl<<endl<<"\t\tAre You Sure You Want To Exit(y/n):      ";
cin>>d;
if(d=='y')
{
exit(1);
}
else
{
clrscr();
goto abc;
}
break;
}
getch();
}



























