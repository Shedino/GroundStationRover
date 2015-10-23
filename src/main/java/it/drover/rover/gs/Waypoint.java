package it.drover.rover.gs;



public class Waypoint {
    private String latStr;
    private String lonStr;
    private String velStr;
    private String hdgStr;

    public Waypoint() {
    }

    public String getLatStr() {
        return latStr;
    }

    public void setLatStr(final String latStr) {
        this.latStr = latStr;
    }

    public String getLonStr() {
        return lonStr;
    }

    public void setLonStr(final String lonStr) {
        this.lonStr = lonStr;
    }

    public String getVelStr() {
        return velStr;
    }

    public void setVelStr(final String velStr) {
        this.velStr = velStr;
    }

    public String getHdgStr() {
        return hdgStr;
    }

    public void setHdgStr(final String hdgStr) {
        this.hdgStr = hdgStr;
    }


    public int getLatMavlink(){
        //Parse string and *10e7
        return 0;
    }
    public int getLonMavlink(){
        //Parse string and *10e7
        return 0;
    }
    public short getVelMavlink(){
        //Parse string and *10e2
        return 0;
    }
    public short getHdgMavlink(){
        //Parse string and *10e2
        return 0;
    }


}