package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.application.publishers.Intelligence;

public class Services {
    private int M;
    private int Moneypenny;
    private Intelligence[] intelligence;
    private int time;

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public int getMoneypenny() {
        return Moneypenny;
    }

    public void setMoneypenny(int moneypenny) {
        Moneypenny = moneypenny;
    }

    public Intelligence[] getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Intelligence[] intelligence) {
        this.intelligence = intelligence;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) { this.time = time; }
}
