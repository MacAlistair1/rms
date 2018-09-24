package com.mis.community.rms.Common;

public class CommonPos {

    public static int pos;
    public static int itemPos;
    public static int count;

    public static String getTableSymbol() {
        return tableSymbol;
    }

    public static void setTableSymbol(String tableSymbol) {
        CommonPos.tableSymbol = tableSymbol;
    }

    public static String tableSymbol;


    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        CommonPos.pos = pos;
    }

    public static int getItemPos() {
        return itemPos;
    }

    public static void setItemPos(int itemPos) {
        CommonPos.itemPos = itemPos;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        CommonPos.count = count;
    }
}
