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


import com.firetrap.verso.kdtree.KDTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Daniel Glasson on 18/05/2014.
 * Uses KD-trees to quickly find the nearest point
 * <p>
 * ReverseGeoCode reverseGeoCode = new ReverseGeoCode(new FileInputStream("c:\\AU.txt"), true);
 * System.out.println("Nearest to -23.456, 123.456 is " + geocode.nearestPlace(-23.456, 123.456));
 */
public class ReverseGeoCode {
    private KDTree<GeoInfo> kdTree;

    /**
     * Parse the zipped file.
     *
     * @param zipInputStream a {@link ZipInputStream} zip file downloaded from http://download.geonames.org/export/dump/; can not be null.
     * @param majorOnly      only include major cities in KD-tree.
     * @throws IOException          if there is a problem reading the {@link ZipInputStream}.
     * @throws NullPointerException if zippedPlacenames is {@code null}.
     */
    public ReverseGeoCode(ZipInputStream zipInputStream, boolean majorOnly) throws IOException {
        //depending on which zip file is given, country specific zip files have read me files that we should ignore
        ZipEntry entry;
        do {

            entry = zipInputStream.getNextEntry();
        } while (entry.getName().equals("readme.txt"));

        createKdTree(zipInputStream, majorOnly);
    }

    /**
     * Parse the raw text  file.
     *
     * @param inputStream the text file downloaded from http://download.geonames.org/export/dump/; can not be null.
     * @param majorOnly   only include major cities in KD-tree.
     * @throws IOException          if there is a problem reading the stream.
     * @throws NullPointerException if zippedPlacenames is {@code null}.
     */
    public ReverseGeoCode(InputStream inputStream, boolean majorOnly) throws IOException {

        createKdTree(inputStream, majorOnly);
    }

    private void createKdTree(InputStream placenames, boolean majorCitiesOnly) throws IOException {
        ArrayList<GeoInfo> geoInfoArrayList = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(placenames));// Read the geoNames file in the directory
        String str;
        try {
            while ((str = in.readLine()) != null) {

                GeoInfo newPlace = new GeoInfo(str);
                if (!majorCitiesOnly || newPlace.isMajorPlace()) {

                    geoInfoArrayList.add(newPlace);
                }
            }
        } catch (IOException ex) {

            throw ex;
        } finally {

            in.close();
        }

        kdTree = new KDTree<>(geoInfoArrayList);
    }

    public GeoInfo nearestPlace(double latitude, double longitude) {
        return kdTree.findNearest(new GeoInfo(latitude, longitude));
    }
}
