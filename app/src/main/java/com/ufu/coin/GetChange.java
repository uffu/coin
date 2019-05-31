package com.ufu.coin;

public class GetChange {
    private final static double[] CHANGE_VALUES = {2, 1, 0.5, 0.2, 0.1};
    private final static String[] CHANGE_STR = {"2€", "1€", "50cent", "20cent", "1cent"};


    private static int getOptions_helper(double value)
    {
        if(value < 0.1)
            return 1;
        int ret = 0;
        for(int i =0; i<CHANGE_VALUES.length; i++)
            if(value > CHANGE_VALUES[i])
                ret += getOptions_helper(value-CHANGE_VALUES[i]);
        return ret;
    }

    public static int getNumOfOptions(double value)
    {
        return getOptions_helper(value);
    }

    public static String getLeastNumOfCoins(double value)
    {
        String ret = "";
        if(value < 0.1)
            return "-";

        for(int i =0; i<CHANGE_VALUES.length; i++)
        {
            int amount = (int)(value / CHANGE_VALUES[i]);
            if(amount > 0)
                ret += amount + " x " + CHANGE_STR[i] + ", ";
            value %= CHANGE_VALUES[i];
        }

        return ret;
    }
}
