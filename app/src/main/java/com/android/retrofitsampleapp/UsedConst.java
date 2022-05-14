package com.android.retrofitsampleapp;

/**
 * Константы нужно писать там (в тех классах) где они применяютяс.
 * Иногда константы можно выносить в отдельный класс, например, интернет адресах, ссылки,
 * числовые константы, наименование и т.д.
 * interface сделан для того, чтобы сгрупировать константы по тематике или по другомы обобщающему
 * признаку.
 */

public final class UsedConst {

    private UsedConst() {
        //конструктор, это для того чтобы нельзя было сделать с этим классом
    }

    public interface httpsConst {
        String GIT_CONST = "https://api.github.com/";
    }

    public interface settingTimeConst {
        int ITEM_OUT_CONST = 15;
    }
}