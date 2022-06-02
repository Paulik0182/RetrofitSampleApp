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

    public interface imageConst {
        int DEFAULT_IMAGE_CONST = R.drawable.ic_launcher_foreground;
    }

    public interface bdConstKey {
        String USERS_BD_KEY = "USERS_BD_KEY";
        String PROJECT_BD_KEY = "PROJECT_BD_KEY";
    }
}
