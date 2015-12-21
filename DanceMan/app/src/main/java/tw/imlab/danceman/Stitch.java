package tw.imlab.danceman;

public class Stitch {
    private int deviceID = 0;
    private final int stitchTable[][] = {
        {0, 0}
        , {0, 1232}
        , {0, 2464}
        , {0, 3696}
        , {0, 4928}
        , {0, 6160}
        , {0, 7392}
        , {0, 8624}
        , {0, 9856}
        , {0, 11088}
        , {0, 12320}
        , {0, 13552}};

    public void setDeviceID(int id) {
        deviceID = id;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public int getStitchX(int index) {
        return stitchTable[index][0];
    }

    public int getStitchY(int index) {
        return stitchTable[index][1];
    }
}
