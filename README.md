# Verso - Offline Reverse Geocode 
##### minSdkVersion 15 

##### Verso is a offline reverse geocode library based in [OfflineReverseGeocode](https://github.com/AReallyGoodName/OfflineReverseGeocode) and optimized to be used on Android.

##### Verso is capable to return the "city name", "country name" and the "country code" from a latitude and longitude provided.

##### Verso is a singleton and use a internal data text file where all the geo info is located.

#### How do I add Verso to my project?
```
dependencies {
		compile 'com.github.firetrap:verso:1.0.0'
	}
```

#### How do I use Verso?

```java
[MyApplication.java]
public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
        Verso.getInstance().setup(this);
	}
}
```

**Get geoInfo**
```
        GeoInfo geoInfo = Verso.getInstance().getGeoInfo(latitude,longitude);
        String countryName = geoInfo.getCountryName();

```

**Setup Polyline Manager and Adapter:**
```java
        mapPolylineManager = new MapPolylineManager(getActivity(), new MapPolylineAdapter() {
            @Override
            public MapPolyline transform(Object objectToTransform) {
                List<LatLng> latLngs = (List<LatLng>) objectToTransform;

                MapPolyline mapPolyline = new MapPolyline(latLngs.toArray(new LatLng[latLngs.size()]));
                mapPolyline.setColor(ResourceHelper.getColor(getActivity(), R.color.colorPrimaryDark));
                mapPolyline.setzIndex(1);

                return mapPolyline;
            }
        });
        mapConfig.setMapPolylineManager(mapPolylineManager);
```

**Setup Map Manager:**
```java
  mapManager = new MapManager(getActivity(), mapConfig, savedInstanceState);
        mapManager.setupMap();
```

**MapManager overriding activity/fragment lifecycle - without this the map will not load:**
```java
    @Override
    public void onStart() {
        super.onStart();
        mapManager.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapManager.onResume();
    }

    @Override
    public void onPause() {
        mapManager.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        mapManager.onStop();
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        mapManager.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapManager.onSaveInstanceState(outState);
    }
```

**Populate managers and notify them:**
```java
 List<LatLng> demoLocations = getDemoLocations();
            mapPoiManager.addAll(demoLocations);
            mapPoiManager.notifyDataSetChanged();

            mapPolygonManager.add(demoLocations);
            mapPolygonManager.notifyDataSetChanged();

            mapPolylineManager.add(demoLocations);
            mapPolylineManager.notifyDataSetChanged();
```
