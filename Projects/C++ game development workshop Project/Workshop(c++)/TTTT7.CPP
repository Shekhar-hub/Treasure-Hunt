#include<conio.h>
#include<graphics.h>
#include<dos.h>
void main()
{
	int driver=DETECT, mode;
	initgraph(&driver,&mode,"c:\\tc\\bgi");
	cleardevice();
	getch();
	setcolor(LIGHTBLUE);
	settextstyle(8,HORIZ_DIR,9);
	outtext("TIC TAC TOE");
	int midx=getmaxx()/2;
	int midy=getmaxy()/2;
	settextstyle(8,HORIZ_DIR,5);
	outtextxy(midx-90,midy,"Loading");
	for(int pass=1;pass<=6;pass++)
		for(int j=-90;j<=90;j=j+30)
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
		getch();

		cleardevice();
		setcolor(LIGHTBLUE);
		settextstyle(8,HORIZ_DIR,9);
		outtext("Select Game");
		settextstyle(8,HORIZ_DIR,6);
		outtextxy(20,160,"1 - Single Player");
		outtextxy(20,260,"2 - Double Player");
		outtextxy(20,360,"Q - Quit");
		getch();

}