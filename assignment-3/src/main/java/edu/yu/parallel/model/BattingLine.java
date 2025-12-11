package edu.yu.parallel.model;

/**
 * Represents a single line from the Batting.csv file.
 */
public class BattingLine {
    private String playerID;
    private int yearID;
    private int stint;
    private String teamID;
    private String lgID;
    private int G;
    private int AB;
    private int R;
    private int H;
    private int twoB;  // 2B
    private int threeB;  // 3B
    private int HR;
    private int RBI;
    private int SB;
    private int CS;
    private int BB;
    private int SO;
    private int IBB;
    private int HBP;
    private int SH;
    private int SF;

    public BattingLine(String playerID, int yearID, int stint, String teamID, String lgID,
                       int G, int AB, int R, int H, int twoB, int threeB, int HR, int RBI,
                       int SB, int CS, int BB, int SO, int IBB, int HBP, int SH, int SF) {
        this.playerID = playerID;
        this.yearID = yearID;
        this.stint = stint;
        this.teamID = teamID;
        this.lgID = lgID;
        this.G = G;
        this.AB = AB;
        this.R = R;
        this.H = H;
        this.twoB = twoB;
        this.threeB = threeB;
        this.HR = HR;
        this.RBI = RBI;
        this.SB = SB;
        this.CS = CS;
        this.BB = BB;
        this.SO = SO;
        this.IBB = IBB;
        this.HBP = HBP;
        this.SH = SH;
        this.SF = SF;
    }

    // Getters
    public String getPlayerID() {
        return playerID;
    }

    public int getYearID() {
        return yearID;
    }

    public int getStint() {
        return stint;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getLgID() {
        return lgID;
    }

    public int getG() {
        return G;
    }

    public int getAB() {
        return AB;
    }

    public int getR() {
        return R;
    }

    public int getH() {
        return H;
    }

    public int get2B() {
        return twoB;
    }

    public int get3B() {
        return threeB;
    }

    public int getHR() {
        return HR;
    }

    public int getRBI() {
        return RBI;
    }

    public int getSB() {
        return SB;
    }

    public int getCS() {
        return CS;
    }

    public int getBB() {
        return BB;
    }

    public int getSO() {
        return SO;
    }

    public int getIBB() {
        return IBB;
    }

    public int getHBP() {
        return HBP;
    }

    public int getSH() {
        return SH;
    }

    public int getSF() {
        return SF;
    }

    @Override
    public String toString() {
        return "BattingLine{" +
                "playerID='" + playerID + '\'' +
                ", yearID=" + yearID +
                ", stint=" + stint +
                ", teamID='" + teamID + '\'' +
                ", lgID='" + lgID + '\'' +
                ", G=" + G +
                '}';
    }
}
