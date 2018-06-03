package com.milkzs.android.snowflow;

public class Snow {

    private int snow_x;
    private int snow_y;
    private int speed;

    private Snow() {
    }

    private Snow(Snow snow) {
        this.snow_x = snow.snow_x;
        this.snow_y = snow.snow_y;
        this.speed = snow.speed;
    }

    public int getSnow_x() {
        return snow_x;
    }

    public int getSnow_y() {
        return snow_y;
    }

    public int getSpeed() {
        return speed;
    }

    public static class Builder {

        private Snow snow;

        public Builder() {
            this.snow = new Snow();
        }

        public Builder addsnow_x(int snow_x) {
            this.snow.snow_x = snow_x;
            return this;
        }


        public Builder addsnow_y(int snow_y) {
            this.snow.snow_y = snow_y;
            return this;
        }

        public Builder addspeed(int speed) {
            this.snow.speed = speed;
            return this;
        }

        public Snow build() {
            return new Snow(snow);
        }
    }
}
