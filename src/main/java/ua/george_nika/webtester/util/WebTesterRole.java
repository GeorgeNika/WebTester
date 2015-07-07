package ua.george_nika.webtester.util;

/**
 * Created by George on 18.06.2015.
 */
public enum WebTesterRole {

    ADMINISTRATOR {
        @Override
        public int getId() {
            return 1;
        }

        @Override
        public String getName() {
            return "administrator";
        }
    },
    ADVANCE_TUTOR {
        @Override
        public int getId() {
            return 2;
        }

        @Override
        public String getName() {
            return "advance tutor";
        }
    },
    TUTOR {
        @Override
        public int getId() {
            return 3;
        }

        @Override
        public String getName() {
            return "tutor";
        }
    },
    STUDENT {
        @Override
        public int getId() {
            return 4;
        }

        @Override
        public String getName() {
            return "student";
        }
    };

    public abstract int getId();
    public abstract String getName();


}
