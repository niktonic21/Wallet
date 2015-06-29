package sk.upjs.ics.wallet.provider;

import android.provider.BaseColumns;

/**
 * Created by Maťo21 on 9. 6. 2015.
 */
public interface Provider {
    public interface Uzivatel extends BaseColumns {
        public static final String TABLE_NAME = "uzivatel";

        public static final String MENO = "meno";

        public static final String HESLO = "heslo";

        public static final String FINANCIE = "financie";
    }

    public interface Transakcia extends BaseColumns {
        public static final String TABLE_NAME = "transakcia";

        public static final String ID_UZIVATEL = "iduziv";

        public static final String TYP = "typ";

        public static final String SUMA = "suma";

        public static final String POLOZKA = "polozka";

        public static final String YEAR = "year";

        public static final String MONTH = "month";

        public static final String DAY = "day";
    }

    public interface Bankomat extends BaseColumns{
        public static final String TABLE_NAME = "bankomat";

        public static final String LAT = "lat";

        public static final String LNG = "long";

        public static final String TITLE = "title";
    }

    public static class PhotoUri implements BaseColumns {
        public static final String TABLE_NAME = "photoUri";

        public static final String URI = "uri";

        public static final String YEAR = "year";

        public static final String MONTH = "month";

        public static final String DAY = "day";

        public static final String DESCRIPTION = "description";
    }
}
