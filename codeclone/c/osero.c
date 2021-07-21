#include<stdio.h>/* -*- Mode:C; c-file-style:"gnu"; indent-tabs-mode:nil; -*- */

// �萔
#define BOARD_SIZE 8

// �v���g�^�C�v�錾
void print_board(int board[][BOARD_SIZE]);
void init_board(int board[][BOARD_SIZE]);
int count_stone(int player_id, int board[][BOARD_SIZE]);
void place_stone(int player_id, int x, int y, int board[][BOARD_SIZE]);
int num_obtained_stone(int player_id, int x, int y, int board[][BOARD_SIZE]);
int asci_to_int_x(char string[]);
int asci_to_int_y(char string[]);
int input_place(int player_id, int board[][BOARD_SIZE]);
int computer(int player_id, int board[][BOARD_SIZE]);
void update_history(int location, int history[]);
void save_history(int history[]);

void print_board(int board[][BOARD_SIZE]){  /*�Ֆʂ��\�������֐�*/
int x, y;

   printf(" 01234567\n");

   for(y=0;y<8;y++){
   	 for(x=0;x<8;x++){
      		if(x==0){
      		    printf("%d", y);
      		}


      		if(board[x][y]==0){
	            printf(".");
      		}
      		else if(board[x][y]==1){
		    printf("o");
      		}
      		else if(board[x][y]==-1){
        	    printf("x");
      		}

      		if(x==7){
	            printf("\n");

      		}

    	 }
   }

}

void init_board(int board[][BOARD_SIZE]){  /*�Ֆʂ������������֐�*/
int x, y;

   for(y=0;y<8;y++){
 	for(x=0;x<8;x++){
  		board[x][y]=0;
 	}
    }
    board[3][3]=1;
    board[4][4]=1;
    board[3][4]=-1;
    board[4][3]=-1;

}
int count_stone(int player_id, int board[][BOARD_SIZE]){  /*Player��Computer�̐΂̐��𐔂����֐�*/
int x, y, total_stone=0;

    for(y=0;y<8;y++){
      for(x=0;x<8;x++){
        if(board[x][y]==player_id){
          total_stone++;
        }

      }
    }

 return total_stone;
}

void place_stone(int player_id, int x, int y, int board[][BOARD_SIZE]){  /*�΂𗠕Ԃ��֐�*/
int check_x, check_y, direction_x[BOARD_SIZE], direction_y[BOARD_SIZE], i, reverse;

   direction_x[0]=1;
   direction_x[1]=1;
   direction_x[2]=0;
   direction_x[3]=-1;
   direction_x[4]=-1;
   direction_x[5]=-1;
   direction_x[6]=0;
   direction_x[7]=1;
   direction_y[0]=0;
   direction_y[1]=1;
   direction_y[2]=1;
   direction_y[3]=1;
   direction_y[4]=0;
   direction_y[5]=-1;
   direction_y[6]=-1;
   direction_y[7]=-1;

   for(i=0;i<8;i++){
            reverse=0;
	    check_x=x;
            check_y=y;
            check_x+=direction_x[i];
            check_y+=direction_y[i];

            while(check_x>=0&&check_x<8&&check_y>=0&&check_y<8){
                      if(board[check_x][check_y]==0){
                            break;
                       }
                       else if(board[check_x][check_y]==player_id){
                            reverse=1;
                            break;
                       }
                       else{
                            check_x+=direction_x[i];
                            check_y+=direction_y[i];
                       }
             }



             if(reverse==1){
                       check_x=x;
                       check_y=y;
                       check_x+=direction_x[i];
                       check_y+=direction_y[i];

                       while(check_x>=0&&check_x<8&&check_y>=0&&check_y<8){
                        	if(board[check_x][check_y]==player_id){
                            		break;
                        	}
                       		else if(board[check_x][check_y]==0){
                            		break;
                        	}
                        	else{
                            		board[check_x][check_y]*=-1;
                              		check_x+=direction_x[i];
                            		check_y+=direction_y[i];
                        	}
                       }
             }

   }

}




int num_obtained_stone(int player_id, int x, int y, int board[][BOARD_SIZE]){  /*�l���ł����΂̐��𐔂����֐�*/
 int check_x, check_y, direction_x[8], direction_y[8], count=0, i, reverse;

   direction_x[0]=1;
   direction_x[1]=1;
   direction_x[2]=0;
   direction_x[3]=-1;
   direction_x[4]=-1;
   direction_x[5]=-1;
   direction_x[6]=0;
   direction_x[7]=1;
   direction_y[0]=0;
   direction_y[1]=1;
   direction_y[2]=1;
   direction_y[3]=1;
   direction_y[4]=0;
   direction_y[5]=-1;
   direction_y[6]=-1;
   direction_y[7]=-1;

   for(i=0;i<8;i++){
           reverse=0;
           check_x=x;
           check_y=y;
           check_x+=direction_x[i];
           check_y+=direction_y[i];

           while(check_x>=0&&check_x<8&&check_y>=0&&check_y<8){
     		if(board[check_x][check_y]==0){
                            break;
                }
                else if(board[check_x][check_y]==player_id){
                reverse=1;
                     	    break;

                }
                else{
                            check_x+=direction_x[i];
                            check_y+=direction_y[i];

                }
           }



           if(reverse==1){
            	 check_x=x;
                 check_y=y;
                 check_x+=direction_x[i];
                 check_y+=direction_y[i];

                      while(check_x>=0&&check_x<8&&check_y>=0&&check_y<8){
                      		if(board[check_x][check_y]==player_id){
                			break;
                       		}
                        	else if(board[check_x][check_y]==0){
     					break;
                        	}
                        	else{
                           		count++;
                              		check_x+=direction_x[i];
                            		check_y+=direction_y[i];
				}
                      }
           }


   }
   	return count;

}

int input_place(int player_id, int board[][BOARD_SIZE]){  /*Player�̎������͂����֐�*/
int x, y, i;
char string[99];

  printf("player:you\n");

  for(i=0;i<100;i++){
 	 printf("x=");
  	 scanf("%s", string);
   	 x=asci_to_int(string);

 	if(string[0]=='P'&&string[1]=='A'&&string[2]=='S'&&string[3]=='S'&&string[4]=='\0'){
     		return -1;
  	}


 	printf("y=");
 	scanf("%s", string);
 	y=asci_to_int(string);

  	if(string[0]=='P'&&string[1]=='A'&&string[2]=='S'&&string[3]=='S'&&string[4]=='\0'){
     		return -1;
  	}


 	else{
  		if(x!=-1&&y!=-1){
   			if(board[x][y]==0){
    				if(num_obtained_stone(player_id, x, y, board)!=0){
     					 break;

   				}
   			}
  		}
	}
  }
   	return x*BOARD_SIZE+y;

}

int asci_to_int(char string[]){  /*�������𐮐��^�ɕϊ������֐�*/
 int x;

   if(string[0]>='0'&&string[0]<'8'&&string[1]=='\0'){
    	x=string[0]-'0';
     	return x;
   }
   else{
     	return -1;
   }

}


int computer(int player_id, int board[][BOARD_SIZE]){  /*�R���s���[�^�̎������͂����֐�*/
int n, max=0, num, max_num=0, x, y, number;

printf("player:computer\n");

for(n=0;n<64;n++){
   x=n/BOARD_SIZE;
   y=n%BOARD_SIZE;
  if(board[x][y]==0){
   num=num_obtained_stone(player_id, x, y, board);
        if(num!=0){
           if(max_num<num){
              max_num=num;
              number=n;
           }

  	}
  }
  }


        if(max_num==0){
          return -1;
        }
        else{
           printf("x=%d\n", number/BOARD_SIZE);
           printf("y=%d\n", number%BOARD_SIZE);
           return number;
        }
  }

void update_history(int location, int history []){
int i;

   for(i=0;i<99;i++){
   	if(history[i]==-5){
      		history[i]=location;
     		 break;
     	}
   }




}

void save_history(int history[]){
 FILE *fp;
 int i;

   fp=fopen("history.txt", "w");

   if(fp==NULL){
    	printf("error");
        return;
   }

   for(i=0;i<99;i++){
   	fprintf(fp,"No.%d:%d   \n",i+1,history[i]);
   }

   fclose(fp);

}


int is_complete(int board[][BOARD_SIZE], int history[]){  /*�Q�[���̑��s�𔻒f�����֐�*/
int x, y, i;




  for(y=0;y<8;y++){
    for(x=0;x<8;x++){
       if(board[x][y]==0){

           for(i=1;i<99;i++){
               if(history[i]==history[i+1]&&history[i]==-1){
                      return 1;
               }
           }
             return 0;
        }
     }
  }





}

int next_player_id(int player_id){


  if(player_id==1){
     player_id=-1;

  }
  else if(player_id==-1){
     player_id=1;

  }
   return player_id;

}

int select_order(int player_id){  /*�����A�������I�������֐�*/
 int order;

  	printf("who is first?    1:you  2:computer\n");
  	scanf("%d", &order);

  	if(order==1){
    		return 1;
 	}
   	else if(order==2){
   		return -1;
  	}

}


void print_winner(int board[][BOARD_SIZE], int player_id){  /*���҂Ƌ���\�������֐�*/
int player1, player2;

    if(player_id==1){
       player1=count_stone(player_id, board);
       player_id=next_player_id(player_id);
       player2=count_stone(player_id, board);
    }
    else if(player_id==-1){
      player2=count_stone(player_id, board);
      player_id=next_player_id(player_id);
      player1=count_stone(player_id, board);
    }


   if(player1>player2){
     printf("you:%d  computer:%d\n", player1, player2);
      printf("winner:you");
   }
   else if(player1==player2){
     printf("you:%d  computer:%d\n", player1, player2);
     printf("draw");
   }
   else{
     printf("you:%d  computer:%d\n", player1, player2);
     printf("winner:computer");
   }

}

void final_board(int board[][BOARD_SIZE]){  /*�ŏI�̔Ֆʂ��\�������֐�*/
int x, y;

  printf(" 01234567\n");

  for(y=0;y<8;y++){
  	for(x=0;x<8;x++){
     	   if(x==0){
       		printf("%d", y);
      	   }


      	   if(board[x][y]==0){
		printf(".");
      	   }
      	   else if(board[x][y]==1){
		printf("o");
      	   }
     	   else if(board[x][y]==-1){
       		printf("x");
      	   }


           if(x==7){
		printf("\n");

     	   }

        }
  }


}

void init_history(int history[]){
int i;

    for(i=0;i<99;i++){
      history[i]=-5;
    }

}
main(){
int board[BOARD_SIZE][BOARD_SIZE], history[99];
int player_id, location, x, y;



 player_id=select_order(player_id);
 init_board(board);
 init_history(history);

 while(is_complete(board, history)==0){



  	print_board(board);
 	if(player_id==1){
  	   location=input_place(player_id, board);
	}
 	else if(player_id==-1){
  	   location=computer(player_id, board);
 	}

 	if(location==-1){
           player_id=next_player_id(player_id);
           update_history(location, history);
  	   save_history(history);
 	}
        else{
           x=location/BOARD_SIZE;
  	   y=location%BOARD_SIZE;
           board[x][y]=player_id;
           place_stone(player_id, x, y, board);
  	   update_history(location, history);
       	   save_history(history);
           player_id=next_player_id(player_id);
           }


  }
    final_board(board);
    print_winner(board, player_id);
}
