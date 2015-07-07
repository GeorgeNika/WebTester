package ua.george_nika.webtester.dao.util;

/**
 * Created by George on 05.06.2015.
 */
public enum MySortDirection {

    NOSORT{
        @Override
        public MySortDirection getNextSort() {
            return MySortDirection.ASCENDING;
        }
    },

    ASCENDING{
        @Override
        public MySortDirection getNextSort() {
            return MySortDirection.DESCENDING;
        }
    },

    DESCENDING{
        @Override
        public MySortDirection getNextSort() {
            return MySortDirection.NOSORT;
        }
    };

    public abstract MySortDirection getNextSort();
}
