package com.football_system.football_system.FMserver.ServiceLayer;

public enum Interest {
    Players {
        @Override
        public String toString() {
            return "players";
        }
    },
    Teams{
        @Override
        public String toString() {
            return "teams";
        }
    },
    Leagues{
        @Override
        public String toString() {
            return "leagues";
        }
    },
    Games{
        @Override
        public String toString() {
            return "games";
        }
    },
    Seasons{
        @Override
        public String toString() {
            return "seasons";
        }
    },
    Coaches{
        @Override
        public String toString() {
            return "coaches";
        }
    },
    Owners{
        @Override
        public String toString() {
            return "owners";
        }
    },
    Managers{
        @Override
        public String toString() {
            return "managers";
        }
    }
}
