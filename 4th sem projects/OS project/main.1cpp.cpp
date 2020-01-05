#include<iostream>
#include<graphics.h>
#include<conio.h>

using namespace std;

class fifo_alg
{
    char ref[100];
    int frame,fault,front,rear;
    char *cir_que;
public:
    fifo_alg()
    {
        front=rear=-1;
        fault=0;
    }
     page_intro();
    void getdata();
    void page_fault();
    void page_hit();

};


void fifo_alg::getdata()
{
    cout<<"Enter Page Reference String : ";
    cin.getline(ref,50);
    cout<<"Enter Number Of frames : ";
    cin>>frame;
}
void fifo_alg::page_fault()
{
    cir_que=new char[frame];
    int flag=0;
    for(int i=0;ref[i]!=0;i++)
    {
        if(ref[i]==' ')
            continue;
        if(front==-1)
        {
            cir_que[0]=ref[i];
            front=rear=0;
            fault++;

            cout<<  "\nAfter Inserting\t"<<cir_que[0]<<"\tPage Fault\t";

          //  cout<<"\tpage fault\t";
        }
        else
        {
            for(int y=front%frame;y!=rear;y=(y+1)%frame)
                if(cir_que[y]==ref[i])
                {
                 cout<<  "\nAfter Inserting\t"<<cir_que[y]<<"\tPage Hit\t";

                // cout<<"\tpage hit\t";
                        flag=1;
                        break;
                }
                if(cir_que[rear]==ref[i])
                        flag=1;
            if(flag==0)
            {
                if((rear+1)%frame==front)
                    front=(front+1)%frame;
                rear=(rear+1)%frame;
                cir_que[rear]=ref[i];

                cout<<  "\nAfter Inserting\t"<<cir_que[rear]<<"\tPage Fault\t";

              //  cout<<"\tpage fault\t";
                fault++;
            }
            flag=0;
        }
    }
    cout<<"\nNumber Of Page Faults : "<<fault<<endl;
}
void fifo_alg::page_hit()
{ int n,hit;
for(int i=0;ref[i]!=0;i++)
  {

    if(ref[i]==' ')
        continue;
    else
        n+=1;
    }
hit=n-fault;
cout<<"Number Of Hit : "<<hit;
}
 fifo_alg::page_intro()
 {

{   cout<<"------------------------------------------------------------------------------------------------------------------------";
    cout<<"------------------------------------------------------------------------------------------------------------------------";
    cout<<"          *              *** ******* *******  * * * *   * * * *          *       ********   *******     *** **********"<<endl;
    cout<<"         * *            *    *          *     *      *  *      *       *   *            *   *          *        *     "<<endl;
    cout<<"        *   *          *     *          *     *      *  *      *     *       *          *   *         *         *     "<<endl;
    cout<<"       *     *        *      *          *     *     *   *     *    *          *         *   *        *          *     "<<endl;
    cout<<"      *       *      *       *          *     *    *    *    *    *            *        *   *       *           *     "<<endl;
    cout<<"     *         *     *       ******     *     * * *     * * *     *            *        *   ******  *           *     "<<endl;
    cout<<"    * * * * * * *     *      *          *     *         *    *     *          *  *      *   *        *          *     "<<endl;
    cout<<"   *             *     *     *          *     *         *     *      *       *   *      *   *         *         *     "<<endl;
    cout<<"  *               *     *    *          *     *         *      *       *   *     *      *   *          *        *     "<<endl;
    cout<<" *                 *     *** ******     *     *         *       *        *       ********   *******     ***     *     "<<endl;
    cout<<"------------------------------------------------------------------------------------------------------------------------";
    cout<<"------------------------------------------------------------------------------------------------------------------------"<<endl<<endl<<endl;

    cout<<"                                 *******  *********    ********     *  *                           "<<endl;
    cout<<"                                 *            *        *          *      *                                   "<<endl;
    cout<<"                                 *            *        *         *        *                            "<<endl;
    cout<<"                                 ******       *        *******  *          *                     "<<endl;
    cout<<"                                 *            *        *        *          *                            "<<endl;
    cout<<"                                 *            *        *         *        *                         "<<endl;
    cout<<"                                 *            *        *           *     *                             "<<endl;
    cout<<"                                 *        *********    *             * *                              "<<endl<<endl<<endl;

 }
 }


int main()
{
    fifo_alg page;
    page.page_intro();

    delay(3000);
    cout<<"                                            Press Any key                                   "<<endl;

    _getche();


    system("cls");

    page.getdata();
    page.page_fault();

    page.page_hit();
    _getch();
    return 0;
}
