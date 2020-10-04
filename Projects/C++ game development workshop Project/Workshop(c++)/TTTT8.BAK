#include<iostream.h>
#include<conio.h>
#include<stdlib.h>
#include<graphics.h>
#include<dos.h>
#include<ctype.h>
#include<stdio.h>
void main()
{
	char option,ch[1],list[10];;
	int grid[10];
	int win=0,turn=0,color,i,j;
	int driver=9,mode=2;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
	cleardevice();
	setcolor(LIGHTBLUE);
	settextstyle(8,HORIZ_DIR,9);
	outtext("TIC TAC TOE");
	int midx=getmaxx()/2;
	int midy=getmaxy()/2;
	settextstyle(8,HORIZ_DIR,5);
	outtextxy(midx-90,midy,"Loading");
	for(int pass=1;pass<=3;pass++)
		for(j=-90;j<=90;j=j+30)
		{
		setcolor(BLUE);
		setfillstyle(SOLID_FILL,BLUE);
		circle(midx+j,midy+100,12);
		circle(midx+j,midy+100,10);
		floodfill(midx+j,midy+100,BLUE);
		setcolor(BLACK);
		setfillstyle(SOLID_FILL,BLACK);
		delay(150);
		floodfill(midx+j,midy+100,BLACK);
		}
game:clrscr();
	cleardevice();
	setbkcolor(BLACK);
	settextstyle(1,0,6);
	setlinestyle(0,3,3);
	setcolor(BLUE);
	line(200,140,200,getmaxy());
	line(400,140,400,getmaxy());
	line(50,250,getmaxx()-50,250);
	line(50,370,getmaxx()-50,370);
	outtextxy(174,188,"1      2      3");
	outtextxy(174,308,"4      5      6");
	outtextxy(174,428,"7      8      9");

	for(i=1;i<=9;i++)
		grid[i]=2;

	settextstyle(1,0,10);
	for(i=1;i<=9;i++)
	{
		char temp;
		int userchoice;
againinput:setviewport(0,0,638,130,0);
		clearviewport();
		setcolor(5);
		settextstyle(1,0,1);
		outtextxy(220,100,"Press 'q' for EXIT");
		settextstyle(1,0,6);
		if(turn==0)
		{
			setcolor(GREEN);
			outtext("        Player 1");
		}
		else
		{
			setcolor(RED);
			outtext("        Player 2");
		}
		outtextxy(0,50,"Make your Move 1 to 9");
		temp=getche();
		if(temp=='q' || temp=='Q')
			exit(0);
		else if(isdigit(temp) && temp!='0')
		{	list[i]=toascii(temp)-48;
			//userchoice=toascii(temp)-48;
			userchoice=list[i];
		}
		else
			goto againinput;

		if(grid[userchoice]!=2)
				goto againinput;
		else if(turn==0)
		{
			grid[userchoice]=0;
			turn=1;
		}
		else if(turn==1)
		{
			grid[userchoice]=1;
			turn=0;
		}


		if(turn==0)
		{
			ch[0]='0';
			ch[1]='\0';
			setcolor(RED);
		}
		else if(turn==1)
		{
			ch[0]='X';
			ch[1]='\0';
			setcolor(GREEN);
		}

		settextstyle(1,0,10);
		switch(userchoice)
		{
			case 1:outtextxy(100,110,ch);
					 break;
			case 2:outtextxy(270,110,ch);
					 break;
			case 3:outtextxy(460,110,ch);
					 break;
			case 4:outtextxy(100,230,ch);
					 break;
			case 5:outtextxy(270,230,ch);
					 break;
			case 6:outtextxy(460,230,ch);
					 break;
			case 7:outtextxy(100,350,ch);
					 break;
			case 8:outtextxy(270,350,ch);
					 break;
			case 9:outtextxy(460,350,ch);
					 break;
		 }

	}
	getch();
}
