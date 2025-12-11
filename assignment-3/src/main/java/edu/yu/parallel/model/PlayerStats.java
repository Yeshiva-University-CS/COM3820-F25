package edu.yu.parallel.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import edu.yu.parallel.Utils;

/**
 * Represents aggregated batting statistics for a player in a single season.
 * If a player played for multiple teams in a season, all teams are tracked in a
 * Set.
 */
public class PlayerStats {
    private String playerID;
    private int year;
    private Set<String> teams;
    private int G; // games
    private int AB; // at-bats
    private int R; // runs
    private int H; // hits
    private int twoB; // doubles
    private int threeB; // triples
    private int HR; // home runs
    private int RBI; // RBIs
    private int SB; // stolen bases
    private int CS; // caught stealing
    private int BB; // walks
    private int SO; // strikeouts
    private int IBB; // intentional walks
    private int HBP; // hit by pitch
    private int SH; // sacrifice hits
    private int SF; // sacrifice flies

    // Derived statistics
    private int PA; // Plate Appearances
    private int singles; // Singles
    private int TB; // Total Bases
    private double BA; // Batting Average
    private double OBP; // On-Base Percentage
    private double SLG; // Slugging Percentage
    private double OPS; // On-base Plus Slugging

    public PlayerStats(String playerID, int year) {
        this.playerID = playerID;
        this.year = year;
        this.teams = new HashSet<>();
        this.G = 0;
        this.AB = 0;
        this.R = 0;
        this.H = 0;
        this.twoB = 0;
        this.threeB = 0;
        this.HR = 0;
        this.RBI = 0;
        this.SB = 0;
        this.CS = 0;
        this.BB = 0;
        this.SO = 0;
        this.IBB = 0;
        this.HBP = 0;
        this.SH = 0;
        this.SF = 0;
    }

    /**
     * Adds batting data from a BattingLine to this PlayerSeason aggregate.
     *
     * @param battingLine The batting data to add
     */
    public void addBattingLine(BattingLine battingLine) {
        teams.add(battingLine.getTeamID());
        G += battingLine.getG();
        AB += battingLine.getAB();
        R += battingLine.getR();
        H += battingLine.getH();
        twoB += battingLine.get2B();
        threeB += battingLine.get3B();
        HR += battingLine.getHR();
        RBI += battingLine.getRBI();
        SB += battingLine.getSB();
        CS += battingLine.getCS();
        BB += battingLine.getBB();
        SO += battingLine.getSO();
        IBB += battingLine.getIBB();
        HBP += battingLine.getHBP();
        SH += battingLine.getSH();
        SF += battingLine.getSF();

        // Recalculate derived statistics
        calculateDerivedStats();
    }

    /**
     * Calculates all derived statistics based on current batting totals.
     */
    private void calculateDerivedStats() {
        // PA = AB + BB + HBP + SF
        PA = AB + BB + HBP + SF;

        // singles = H - 2B - 3B - HR
        singles = H - twoB - threeB - HR;

        // TB = 1B + 2*2B + 3*3B + 4*HR
        TB = singles + (2 * twoB) + (3 * threeB) + (4 * HR);

        // BA = H / AB (if AB > 0, otherwise 0)
        BA = AB > 0 ? (double) H / AB : 0.0;

        // OBP = (H + BB + HBP) / (AB + BB + HBP + SF) (if denominator > 0, otherwise 0)
        int obpDenominator = AB + BB + HBP + SF;
        OBP = obpDenominator > 0 ? (double) (H + BB + HBP) / obpDenominator : 0.0;

        // SLG = TB / AB (if AB > 0, otherwise 0)
        SLG = AB > 0 ? (double) TB / AB : 0.0;

        // OPS = OBP + SLG
        OPS = OBP + SLG;
    }

    // Getters
    public String getPlayerID() {
        return playerID;
    }

    public int getyear() {
        return year;
    }

    public Set<String> getteams() {
        return new HashSet<>(teams);
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

    public int getPA() {
        return PA;
    }

    public int getSingles() {
        return singles;
    }

    public int getTB() {
        return TB;
    }

    public double getBA() {
        return BA;
    }

    public double getOBP() {
        return OBP;
    }

    public double getSLG() {
        return SLG;
    }

    public double getOPS() {
        return OPS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PlayerStats that = (PlayerStats) o;
        return year == that.year &&
                playerID.equals(that.playerID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, year);
    }

    @Override
    public String toString() {
        return String.format("%s %d %s/%s/%s",
                playerID, year,
                Utils.statFmt(BA),
                Utils.statFmt(OBP),
                Utils.statFmt(SLG));
    }
}
