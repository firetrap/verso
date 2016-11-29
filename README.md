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
        String cityName = geoInfo.getCityName();
```

