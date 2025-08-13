package com.jagex.maths;

public interface JagexScale {
    double TAU = Math.PI * 2;
    int ANGLE_BASE = 11;
    int ANGLE_SCALE = 0;
    int ANGLE_BITS = ANGLE_BASE + ANGLE_SCALE;
    int ANGLE_FULL = 1 << ANGLE_BITS;
    int ANGLE_HALF = ANGLE_FULL / 2;
    int ANGLE_MASK = ANGLE_FULL - 1;
    int TILE_SCALE = 0;
    int TILE_BITS = 7 + TILE_SCALE;
    int TILE_UNIT = 1 << TILE_BITS;
    int TILE_MASK = TILE_UNIT - 1;
    int TILE_HALF_UNIT = TILE_UNIT / 2;
    int TILE_QUARTER_UNIT = TILE_UNIT / 4;
    double JAG2RAD = TAU / ANGLE_FULL;
    double RAD2JAG = ANGLE_FULL / TAU;

    static int decodeAngle(int angle, int bits) {
        int diff = bits - ANGLE_BITS;
        if (diff > 0) {
            return angle >> diff;
        } else {
            return angle << -diff;
        }
    }

    static int encodeAngle(int angle, int bits) {
        int diff = ANGLE_BITS - bits;
        if (diff > 0) {
            return angle >> diff;
        } else {
            return angle << -diff;
        }
    }
}
