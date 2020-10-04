#include<conio.h>
#include<graphics.h>
void main()
{
	int driver=9,mode=2;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
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
	getch();
	settextstyle(1,0,10);
	outtextxy(100,110,"0");
	outtextxy(270,110,"0");
	outtextxy(460,110,"0");
	outtextxy(100,230,"0");
	outtextxy(270,230,"0");
	outtextxy(460,230,"0");
	outtextxy(100,350,"0");
	outtextxy(270,350,"0");
	outtextxy(460,350,"0");
	getch();
}