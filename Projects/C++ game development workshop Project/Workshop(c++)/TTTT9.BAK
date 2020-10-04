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
	/*outtext("TIC TAC TOE");
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
		} */
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
againinput:setviewport(0,0,638,120,0);
		clearviewport();
		setcolor(LIGHTBLUE);
		settextstyle(1,0,6);
		if(turn==0)
			outtext("        Player 1");
		else
			outtext("        Player 2");
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


		 clearviewport();
		 settextstyle(1,0,9);
		 setcolor(5);
		 if(grid[1]==grid[2]&&grid[1]==grid[3]&&grid[1]==0 || grid[4]==grid[5]&&grid[4]==grid[6]&&grid[4]==0 ||
				grid[7]==grid[8]&&grid[7]==grid[9]&&grid[7]==0 || grid[1]==grid[4]&&grid[1]==grid[7]&&grid[1]==0 ||
				grid[2]==grid[5]&&grid[2]==grid[8]&&grid[2]==0 || grid[3]==grid[6]&&grid[3]==grid[9]&&grid[3]==0 ||
				grid[1]==grid[5]&&grid[1]==grid[9]&&grid[1]==0 || grid[3]==grid[5]&&grid[3]==grid[7]&&grid[3]==0)
				{
					delay(80);
					setcolor(GREEN);
					outtext("Player1 WINS");
					win=1;
					break;
				}
		 if(grid[1]==grid[2]&&grid[1]==grid[3]&&grid[1]==1 || grid[4]==grid[5]&&grid[4]==grid[6]&&grid[4]==1 ||
				grid[7]==grid[8]&&grid[7]==grid[9]&&grid[7]==1 || grid[1]==grid[4]&&grid[1]==grid[7]&&grid[1]==1 ||
				grid[2]==grid[5]&&grid[2]==grid[8]&&grid[2]==1 || grid[3]==grid[6]&&grid[3]==grid[9]&&grid[3]==1 ||
				grid[1]==grid[5]&&grid[1]==grid[9]&&grid[1]==1 || grid[3]==grid[5]&&grid[3]==grid[7]&&grid[3]==1)
				{
					delay(80);
					setcolor(RED);
					outtext("Player2 WINS");
					win=1;
					break;
				}
				else
				{
					if(i==9)
					{
						delay(80);
						outtextxy(110,0,"Try Again");
					}
				}
		}
		 if(win==1)
		{
				setcolor(BROWN);
				setlinestyle(SOLID_LINE,1,3);

				if(grid[1]==grid[2] && grid[1]==grid[3])
					line(80,190,getmaxx()-100,190);
				if(grid[4]==grid[5] && grid[4]==grid[6])
					line(80,310,getmaxx()-100,310);
				if(grid[7]==grid[8] && grid[7]==grid[9])
					line(80,430,getmaxx()-100,430);
				if(grid[1]==grid[4] && grid[1]==grid[7])
					line(130,137,130,getmaxy());
				if(grid[2]==grid[5] && grid[2]==grid[8])
					line(300,137,300,getmaxy());
				if(grid[3]==grid[6] && grid[3]==grid[9])
					line(487,137,487,getmaxy());
				if(grid[1]==grid[5] && grid[1]==grid[9])
					line(80,148,550,getmaxy());
				if(grid[3]==grid[5] && grid[3]==grid[7])
					line(530,148,80,getmaxy());


		 }

	getch();
}
