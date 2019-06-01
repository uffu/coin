package com.ufu.coin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Locale;

class GetChange
{
    private final static BigDecimal[] CHANGE_VALUES = {
            new BigDecimal("2.0"),
            new BigDecimal("1.0"),
            new BigDecimal("0.5"),
            new BigDecimal("0.2"),
            new BigDecimal("0.1")};
    private final static String[] CHANGE_STR = {"2€", "1€", "50¢", "20¢", "10¢"};

    private final static int MAX_ALL_OPTIONS_SIZE = 100;

    private static int getNumOfOptions_helper(BigDecimal value, int index)
    {
        if(value.compareTo(BigDecimal.ZERO) == 0)
            return 1;
        int ret = 0;
        for(int i=index; i<CHANGE_VALUES.length; i++)
            if(value.compareTo(CHANGE_VALUES[i]) >= 0)
                ret += getNumOfOptions_helper(value.subtract(CHANGE_VALUES[i]), i);
        return ret;
    }

    static int getNumOfOptions(BigDecimal value)
    {
        return getNumOfOptions_helper(value, 0);
    }


    private static void getAllOptions_helper(BigDecimal value, int index, int[] amount, ArrayList<int[]> amounts)
    {
        if(value.compareTo(BigDecimal.ZERO) == 0)
        {
            amounts.add(amount);
            return;
        }
        if(amounts.size() >= MAX_ALL_OPTIONS_SIZE)
            return;

        for(int i =index; i<CHANGE_VALUES.length; i++)
            if(value.compareTo(CHANGE_VALUES[i]) >= 0)
            {
                int[] tmp = amount.clone();
                tmp[i]++;
                getAllOptions_helper(value.subtract(CHANGE_VALUES[i]), i, tmp, amounts);
            }
    }

    static ArrayList<String> getAllOptions(BigDecimal value)
    {
        ArrayList<String> ret = new ArrayList<>();
        ArrayList<int[]> amounts = new ArrayList<>();

        int[] amount = new int[CHANGE_VALUES.length];
        getAllOptions_helper(value, 0, amount, amounts);

        // create strings
        for(int[] counts : amounts)
        {
            StringBuilder str = new StringBuilder();
            for(int i =0; i<CHANGE_VALUES.length; i++)
                if(counts[i] > 0)
                    str.append(String.format(Locale.getDefault(),"%dx %s, ", counts[i], CHANGE_STR[i]));
            if(str.length() > 1)
                ret.add(str.substring(0, str.length() - 2));
        }

        return ret;
    }

    static String getAllOptions_str(BigDecimal value)
    {
        StringBuilder str = new StringBuilder();
        for(String change_option : GetChange.getAllOptions(value))
        {
            str.append(change_option);
            str.append("\r\n");
        }
        return  str.toString();
    }


    static String getLeastNumOfCoins(BigDecimal value)
    {
        StringBuilder str = new StringBuilder();
        if(value.compareTo(BigDecimal.ZERO) == 0)
            return "-";

        for(int i =0; i<CHANGE_VALUES.length; i++)
        {
            int amount = value.divide(CHANGE_VALUES[i],0, RoundingMode.FLOOR).intValue();
            if(amount > 0)
                str.append(String.format(Locale.getDefault(),"%dx %s, ", amount, CHANGE_STR[i]));

            value = value.remainder(CHANGE_VALUES[i]);
        }

        if(str.length() > 1)
            return str.substring(0, str.length() - 2);
        else
            return "";
    }
}
