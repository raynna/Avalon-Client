package com.jagex;/* com.jagex.Class106 - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */

public class Class106 {
    static Class113 aClass113_1308;
    public static int anInt1309;

    Class106() throws Throwable {
	throw new Error();
    }

    public static final void method1137(int i) {
	try {
	    if (!GameClient.aBoolean8761) {
		GameClient.aFloat8760 += (12.0F - GameClient.aFloat8760) / 2.0F;
		GameClient.aBoolean8763 = true;
		GameClient.aBoolean8761 = true;
	    }
	}
	catch (RuntimeException runtimeexception) {
	    throw Class346.method4175(runtimeexception, new StringBuilder().append("ei.hf(").append(')').toString());
	}
    }
}
