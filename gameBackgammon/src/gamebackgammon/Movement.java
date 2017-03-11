/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamebackgammon;

import java.util.ArrayList;

/**
 *
 * @author suleyman
 */
public class Movement {
    char character;
    int now;
    int after;
    int point;
    
    
    Movement(int now, int after,char character){
        this.now=now;
        this.after=after;
        this.character=character;
    }
    Movement(){
        this.now=-1;
        this.after=-1;
        this.character='N';
    }
    void setMoveChar(char ch) { character=ch; }   
    void setMoveNow(int nowTemp) { now=nowTemp; }
    void setMoveAfter(int afterTemp) { after=afterTemp; }  
    void setPoint(int point) { this.point=point; }
    char getMoveChar() { return character; }
    int getMoveNow() { return now; }   
    int getMoveAfter() { return after; }
    int getPoint() { return point; }
    void addPoint(int point) {this.point+=point; }
    
    @Override
    public String toString(){
        return getMoveChar() + ":" + getMoveNow()+" => " + getMoveAfter() + " puan:"+getPoint();
    }
}
