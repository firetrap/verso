/*
The MIT License (MIT)
[OSI Approved License]
The MIT License (MIT)

Copyright (c) 2014 Daniel Glasson

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.firetrap.verso.model;


import com.firetrap.verso.kdtree.KDNodeComparator;

import java.util.Comparator;
import java.util.Locale;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

/**
 * Created by Daniel Glasson on 18/05/2014.
 * This class works with a placenames files from http://download.geonames.org/export/dump/
 * Refactored to android by firetrap
 */

public class GeoInfo extends KDNodeComparator<GeoInfo> {
    private String cityName;
    private boolean majorPlace; // Major or minor place
    private double latitude;
    private double longitude;
    private double point[] = new double[3]; // The 3D coordinates of the point
    private String countryCode;
    private String countryName;

    GeoInfo(String data) {
        String[] names = data.split("\t");
        cityName = names[1];
        majorPlace = names[6].equals("P");
        latitude = Double.parseDouble(names[4]);
        longitude = Double.parseDouble(names[5]);
        setPoint();
        countryCode = names[8];
        countryName = setCountryDisplayName(countryCode);
    }

    GeoInfo(Double latitude, Double longitude) {
        cityName = countryCode = "Search";
        this.latitude = latitude;
        this.longitude = longitude;
        setPoint();
    }

    private void setPoint() {
        point[0] = cos(toRadians(latitude)) * cos(toRadians(longitude));
        point[1] = cos(toRadians(latitude)) * sin(toRadians(longitude));
        point[2] = sin(toRadians(latitude));
    }

    private String setCountryDisplayName(String isoCode) {
        Locale locale = new Locale("", isoCode);

        return locale.getDisplayCountry();
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isMajorPlace() {
        return majorPlace;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    @Override
    public String toString() {
        return cityName;
    }

    @Override
    protected double squaredDistance(GeoInfo other) {
        double x = this.point[0] - other.point[0];
        double y = this.point[1] - other.point[1];
        double z = this.point[2] - other.point[2];
        return (x * x) + (y * y) + (z * z);
    }

    @Override
    protected double axisSquaredDistance(GeoInfo other, int axis) {
        double distance = point[axis] - other.point[axis];
        return distance * distance;
    }

    @Override
    protected Comparator<GeoInfo> getComparator(int axis) {
        return GeoNameComparator.values()[axis];
    }

    protected static enum GeoNameComparator implements Comparator<GeoInfo> {
        x {
            @Override
            public int compare(GeoInfo a, GeoInfo b) {
                return Double.compare(a.point[0], b.point[0]);
            }
        },
        y {
            @Override
            public int compare(GeoInfo a, GeoInfo b) {
                return Double.compare(a.point[1], b.point[1]);
            }
        },
        z {
            @Override
            public int compare(GeoInfo a, GeoInfo b) {
                return Double.compare(a.point[2], b.point[2]);
            }
        };
    }
}
