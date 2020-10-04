#include<conio.h>
#include<graphics.h>
void main()
{
	int driver=9,mode=2;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
	cleardevice();
	setbkcolor(BLACK);
	setcolor(BLUE);
	settextstyle(1,0,10);
	outtextxy(0,0,"X");
	getch();
}