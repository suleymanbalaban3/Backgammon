/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebackgammon;

import static gamebackgammon.Artificial.game1;
import static gamebackgammon.char_client.game1;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import static javafx.scene.input.KeyCode.T;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author suleyman
 */
public class Backgammon {
    private String Name;
    private int eatenX=0;
    private int eatenY=0;
    private int yourTurn=1;
    ArrayList<Integer> zarList = new ArrayList<Integer>();
    Movement bestMovement;
    board myBoard;
    String input="";
    public static final String[] selection = { "Artficial Intellgince",
        "With frind on this Pc", "With frind on different Pc" };
    int [] zar = new int[2];
    

    Backgammon(String name) {
        Name=name;
        myBoard = new board();
    }
    Backgammon(String name,char myStone) {
        Name=name;
        myBoard = new board(myStone);
    }
    board getBoard() {
        return myBoard;
    }
    int getYourTurn() {
        return yourTurn;
    }
    void setYourTurn(int order) {
        yourTurn=order;
    }
   /* String getCordianeM() {
        return cordinateM;
    }
    void setCordianeM(String co) {
        cordinateM=co;
    }*/
    void MakeMove(char stone, int isArtificial) throws IOException {
        int NowColumn=-1, NowRow=-1;
        int AfterColumn=-1, AfterRow=-1;
        int Now,After;
        String message="";
        int []Coordinate=new int [2];
        int []moveLocations=new int [4];
        ArrayList<Movement> movements= new ArrayList<Movement>();
        int zarIndex;
        int flag = 0;
        boolean status=true;
        
        if(stone==getBoard().getMyColor())
            setYourTurn(1);
        else
            setYourTurn(0);
        
        movements = generateMovement();
        if(!checkIsPass()) {
            if(isArtificial==1) {       //artificial
                moveLocations = findLocation(bestMovement.getMoveNow(), bestMovement.getMoveAfter());
                flag = isWrongMovement(moveLocations,stone);
                zarIndex=checkZarDistance(bestMovement.getMoveNow(), bestMovement.getMoveAfter(),stone);
                zarList.remove(zarIndex);
            }else {                     //else
                do{
                    do{  
                        do{
                        int i=0;
                            message="";//new
                            JFrame frame = new JFrame("JOptionPane showMessageDialog example");
                            String zarNow ="Move : [";
                            for (int k = 0; k < zarList.size(); k++) {
                                if( k == 0)
                                    zarNow += zarList.get(k);
                                else
                                    zarNow += ", "+zarList.get(k);
                            }
                            zarNow += "]\n";
                            message =  JOptionPane.showInputDialog(frame,zarNow+"User "+stone+" Enter a movement"); 
                            if(message==null) {
                                int input = JOptionPane.showOptionDialog(null, "Do you want to save the game", "Selection Of Game Beginning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);

                                if(input == JOptionPane.OK_OPTION)                               
                                    getBoard().encyrptionForSavingGame();
                                System.err.println("Closing the game...");
                                System.exit(0);   
                            }
                            if(!message.equals("")) {
                                StringTokenizer st = new StringTokenizer(message," ");   
                                while (st.hasMoreElements()) {
                                    Coordinate[i]=Integer.parseInt(st.nextToken());
                                    i++;
                                }
                            }
                        }while(!(Coordinate[0]<25&&Coordinate[0]>0)||!(Coordinate[1]<25&&Coordinate[1]>0));
                        
                        Now = Coordinate[0];
                        After = Coordinate[1];
                        zarIndex=checkZarDistance(Now,After,stone);
                        if(zarIndex==-1){
                            JFrame frame1 = new JFrame("JOptionPane showMessageDialog example");
                            JOptionPane.showMessageDialog(frame1, "Wrong input!");
                            
                            System.out.println("----Wrong input!----");
                        }
                    }while(zarIndex==-1);
                    moveLocations = findLocation(Now, After);       
                    Movement mov = new Movement(Now, After, stone);
                    status = checkIsWrongMovement(movements, mov);
                    flag = isWrongMovement(moveLocations,stone);
                    if(status) {
                        JFrame frame = new JFrame("JOptionPane showMessageDialog example");
                        JOptionPane.showMessageDialog(frame, "Wrong Movement!");
                        System.out.println("----Wrong Movement!----");
                    }
                            
                }while(status);
                zarList.remove(zarIndex);
            }
            //zarList.remove(zarIndex);
            input+=message+" ";
            moveCoin(moveLocations, flag, stone); 
            getBoard().copyTable();
            getBoard().PrintTable();
            if(zarList.size()!=0)
                MakeMove(stone, isArtificial);
        }
    }
    void makeOpponentMove(String inputCoordinate, char color) {
        int flag = 0;
        int zarIndex=0,i=0,j=0;
        int []moves = new int [8];
        int []moveLocations=new int [4];
        StringTokenizer st = new StringTokenizer(inputCoordinate," ,");
        while (st.hasMoreElements()) {
            moves[i]=Integer.parseInt((String) st.nextElement());
            i++;
        }
        while (i>j) {      
            System.out.println("move :"+moves[j]+"-"+moves[j+1]);
            
            zarIndex=checkZarDistance(25-moves[j],25-moves[j+1],color);
            moveLocations = findLocation(25-moves[j],25-moves[j+1]);  
            flag = isWrongMovement(moveLocations,color);
            moveCoin(moveLocations, flag, color); 
            
            j+=2;
        }
        getBoard().copyTable();
        getBoard().PrintTable();
    }
    void moveCoin(int [] moveLocations, int flag, char ch) {
        if(flag==2){
            getBoard().table[moveLocations[0]][moveLocations[1]] = '.';
            getBoard().table[moveLocations[2]-1][moveLocations[3]] = ch;
        }else if(flag==3) {
            getBoard().table[moveLocations[0]][moveLocations[1]] = '.';
            getBoard().table[moveLocations[2]+1][moveLocations[3]] = ch;
        }else if(flag==5){                  //tas yer
            if(ch=='X')
                eatenX++;
            else
                eatenY++;
            getBoard().table[moveLocations[0]][moveLocations[1]] = '.';
            getBoard().table[moveLocations[2]][moveLocations[3]] = ch;
        }else {
            getBoard().table[moveLocations[0]][moveLocations[1]] = '.';
            getBoard().table[moveLocations[2]][moveLocations[3]] = ch;
        }    
    }
    int isWrongMovement(int [] moveLocations, char ch) {
        int status = checkMovementToGo(moveLocations[2],moveLocations[3],ch);
        if(!checkMovementSelf(moveLocations[0],moveLocations[1],ch)||status==0)
            return 1;
        return status;
    }
    int [] findLocation(int Now, int After) {
        int NowColumn=-1, NowRow=-1;
        int AfterColumn=-1, AfterRow=-1;        
        int [] locations = new int[4];
        
        if(Now>24||Now<1||After<1||After>24)
            return locations;
        if (Now > 12)                
            NowColumn = 12 - ((Now - 1) % 12)-1;
        else if(Now < 13)
            NowColumn = (Now - 1) % 12;  
        if(After > 12)
            AfterColumn = 12 - ((After - 1) % 12)-1;
        else if (After < 13)
            AfterColumn = (After - 1) % 12;           //CONTROLE yolla


        if (Now > 12)
        {
            for (int i = 0; i < 30; i++)
            {
                if (getBoard().table[i][NowColumn] == '.')
                {
                    if(i==0)
                        NowRow = i;
                    else
                        NowRow = i-1;
                    break;
                }
            }
        }
        else if (Now < 13)
        {
            for (int i = 29; i >0; i--)
            {
                if (getBoard().table[i][NowColumn] == '.')
                {
                    if(i==29)
                        NowRow = i;
                    else
                        NowRow = i + 1;
                    break;
                }
            }
        }
        if (After > 12)
        {
            for (int i = 0; i < 30; i++)
            {
                if (getBoard().table[i][AfterColumn] == '.')
                {
                    if(i==0)
                        AfterRow = i;
                    else
                        AfterRow = i - 1;
                    break;
                }
            }
        }
        else if (After < 13)
        {
            for (int i = 29; i > 0; i--)
            {
                if (getBoard().table[i][AfterColumn] == '.')
                {
                    if(i==29)
                        AfterRow = i;
                    else
                        AfterRow = i + 1;
                    break;
                }
            }
        }
        locations[0] = NowRow;
        locations[1] = NowColumn;
        locations[2] = AfterRow;
        locations[3] = AfterColumn;
        return locations;
    }
    int checkZarDistance(int now,int after,char stone) {
        int distance=now-after;
       
        for (int i = 0; i < zarList.size(); i++) {
            if((zarList.get(i)==distance)&&(stone==getBoard().getMyColor()))
                return i;
            else if((zarList.get(i)==(distance*-1))&&(stone==getBoard().getYourColor())) {
                return i;   
            }
                    
        }
        return -1;
    }
    boolean checkMovementSelf(int nowRow, int nowColumn, char ch) {
        if(getBoard().table[nowRow][nowColumn]!=ch) {
            System.out.println("It is not your stone!");
            return false;
        }   
        return true;
    }
    int checkMovementToGo(int AfterRow, int AfterColumn, char ch) {
        if(getBoard().table[AfterRow][AfterColumn]=='.')       //bosluga gider
            return 4; 
        else if(getBoard().table[AfterRow][AfterColumn]==ch) {
            if(AfterRow>14){
                if(getBoard().table[AfterRow-1][AfterColumn]=='.') 
                    return 2;                 //asagi dogru kendi tasinin ustune
            }else {
                if(getBoard().table[AfterRow+1][AfterColumn]=='.') 
                    return 3;                //yukari dogru kendi tasinin ustune
            }                
        }
        else if(getBoard().table[AfterRow][AfterColumn]!=ch) {
            if(AfterRow>12&&isClosed(AfterRow+1,AfterColumn,ch)==true){
                return 0;
            }    
            else if(AfterRow<13&&isClosed(AfterRow-1,AfterColumn,ch)==true){
                return 0;
            }               
            else
                return 5;           //tas yer
        }
        return 0;
    }
    boolean isClosed(int AfterRow, int AfterColumn, char ch) {
        if(AfterColumn<0||AfterColumn>13||AfterRow<0||AfterRow>29)
            return false;
        if(getBoard().table[AfterRow][AfterColumn]!= ch)
            return true;
        return false;
    }
    int [] input() {
        int[] Coordinate = new int[2];
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter Now coordinate :");
        Coordinate[0] = in.nextInt();
        System.out.print("Please enter After coordinate :");
        Coordinate[1] = in.nextInt();
        
        return Coordinate;
    }
    void generateZar() {
        int firstNumber,secondNumber;
        Random rnd = new Random();
        zar[0] = 1 + rnd.nextInt(6);
        zar[1] = 1 + rnd.nextInt(6);
        
        if(zar[0]==zar[1]) {
            zarList.add(zar[0]);
            zarList.add(zar[0]);
            zarList.add(zar[0]);
            zarList.add(zar[0]);
        }else {
            zarList.add(zar[0]);
            zarList.add(zar[1]);
        }
        System.out.print("zar :"+zar[0]+"-"+zar[1]+"\n----\n");
        
    }
    ArrayList<Movement> generateMovement() {
        ArrayList<Movement> movePoss = new ArrayList<Movement>();        
        movePoss = generatePossibilities();
        
        for(int i=0;i<movePoss.size();i++) {
            int [] locs = new int[4];
            locs = findLocation(movePoss.get(i).getMoveNow(), movePoss.get(i).getMoveAfter());
            int flag = isWrongMovement(locs,movePoss.get(i).getMoveChar());
            moveCoin(locs, flag, movePoss.get(i).getMoveChar());
            givePoint(movePoss.get(i));
            getBoard().reCopyTable();
            
        }
        findBestMovement(movePoss);
        return movePoss;
    }
    boolean checkIsWrongMovement(ArrayList<Movement> movements, Movement moving) {
        for (int i = 0; i < movements.size(); i++) {
            if(isEqualMovements(movements.get(i),moving))
                return false;
        }
        return true;        
    }
    boolean checkIsPass() {
        if(bestMovement.getMoveNow()==-1)
            return true;
        return false;
    }
    boolean isEqualMovements(Movement moving1, Movement moving2) {
        if(moving1.getMoveNow()==moving2.getMoveNow()&&
                moving1.getMoveAfter()==moving2.getMoveAfter()&&
                moving1.getMoveChar()==moving2.getMoveChar())
            return true;
        return false;
    }
    ArrayList<Movement> generatePossibilities() {
        ArrayList<Movement> movementPossibilities = new ArrayList<Movement>();
        ArrayList<Integer> updateZar = new ArrayList<Integer>();
   
        if(getYourTurn()==1) {
            for (int i = 0; i < getBoard().getFilledMy().size(); i++) {
                for (int j = 0; j < zarList.size(); j++) {
                    int noww=getBoard().getFilledMy().get(i);
                    Movement mov1 = new Movement(noww,noww - zarList.get(j),getBoard().getMyColor()); 
                    if(isCorrectMove(mov1)) {
                        if(updateZar.indexOf(zarList.get(j))>-1)
                            updateZar.add(zarList.get(j));
                        movementPossibilities.add(mov1);
                        //System.out.println(mov1.toString()+",1");
                    }                           
                }              
            }
        }else {
            for (int i = 0; i < getBoard().getFilledYour().size(); i++) {
                for (int j = 0; j < zarList.size(); j++) {
                    int noww=getBoard().getFilledYour().get(i);
                    Movement mov2 = new Movement(noww,noww + zarList.get(j),getBoard().getYourColor()); 
                    //System.out.println(mov2);
                    if(isCorrectMove(mov2)) {                       
                        if(updateZar.indexOf(zarList.get(j))>-1)
                            updateZar.add(zarList.get(j));                    
                        movementPossibilities.add(mov2);
                        //System.out.println(mov2.toString()+",2");
                    }
                }
            }
        }
        zarList.addAll(updateZar);
        System.out.println(zarList);
        if(movementPossibilities.size()==0)
            System.err.println("gele attınız !!");
        return movementPossibilities;
   
    }  
    boolean isCorrectMove(Movement moving) {
        int status;
        int [] movingLocations = new int[4];
        movingLocations = findLocation(moving.getMoveNow(), moving.getMoveAfter());
        if(movingLocations==null)
            return false;
        status = isWrongMovement(movingLocations,moving.getMoveChar());
        if(status==1)
            return false;
        return true;
    }
    void givePoint(Movement moving) {
        int count=0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 1; j++) {
                if(moving.getMoveChar()==getBoard().table[j][i]&&getBoard().table[j+1][i]=='.')
                    count++;      
            } 
            for (int j = 29; j > 28; j--) {
                if(moving.getMoveChar()==getBoard().table[j][i]&&getBoard().table[j-1][i]=='.')
                    count++;
            }
        }
        moving.setPoint(count*-10);
    } 
    void findBestMovement(ArrayList<Movement> movements) {
        int bestCount=-1000,resIndis = -1;
        for (int i = 0; i < movements.size(); i++) {
            if(movements.get(i).getPoint() > bestCount) {
                bestCount = movements.get(i).getPoint();
                resIndis = i;
            }
        }
        if(resIndis!=-1) {
            bestMovement = new Movement(movements.get(resIndis).getMoveNow(),
                    movements.get(resIndis).getMoveAfter(),movements.get(resIndis).getMoveChar());
        }
        else
            bestMovement = new Movement();
    }
    boolean isFinish(){
        int myStones=0,yourStone=0;
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 12; j++) {
                if(getBoard().table[i][j]==getBoard().getMyColor())
                    myStones++;
                else if(getBoard().table[i][j]==getBoard().getYourColor())
                    yourStone++;
            }
        }
        if(myStones==0||yourStone==0)
            return true;
        else
            return false;
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        Backgammon game1 = new Backgammon("slymn");
        
        game1.getBoard().LoadTable();
        game1.getBoard().PrintTable();
        JFrame frame = new JFrame("Input Dialog Example 3");
        String gameSelection = (String) JOptionPane.showInputDialog(frame, 
        "How do you begin to game?",
        "Game Selection",
        JOptionPane.QUESTION_MESSAGE, 
        null, 
        selection, 
        selection[0]);
        if(gameSelection=="Artficial Intellgince") {
            Artificial.main(args);            
            System.err.println("Artficial Intellgince");        
        }else if(gameSelection=="With frind on this Pc") {
            Manual.main(args);
        }else if(gameSelection=="With frind on different Pc") {
            chat_server.main(args);     
            System.err.println("With frind on different Pc");
        }else {
            System.err.println("Exitting..");
        }        
        System.exit(0);
    }
}
