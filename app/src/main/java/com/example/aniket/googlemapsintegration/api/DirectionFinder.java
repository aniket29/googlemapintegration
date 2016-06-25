package com.example.aniket.googlemapsintegration.api;

import android.os.AsyncTask;

import com.example.aniket.googlemapsintegration.model.DirectionFinderListener;
import com.example.aniket.googlemapsintegration.model.Distance;
import com.example.aniket.googlemapsintegration.model.Route;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import java.util.List;

import okhttp3.Request;
import retrofit.http.GET;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;


/**
 * Created by aniket on 2
 * 3/6/16.
 */
public class DirectionFinder
{
        private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
        private String GOOGLE_API_KEY = "AIzaSyAIcU0oKPrLjsGLWPCYVyHRVgjdhKG0O50";
        private DirectionFinderListener listener;
        private String origin;
        private String destination;

        public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
            this.listener = listener;
            this.origin = origin;
            this.destination = destination;
        }

    public interface MyApiRequestInterface {

        @retrofit2.http.GET("/maps/api/directions/json")
        Call<JsonElement> loadRepo(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
//        public void getJson(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key, Callback<JsonElement>callback);
    }
        public void execute() throws UnsupportedEncodingException {
            listener.onDirectionFinderStart();
//            RestAdapter restAdapter = new RestAdapter.Builder()
//                    .setLogLevel(RestAdapter.LogLevel.FULL)
////                    .setConverter(new StringConverter())
//                    .setEndpoint("https://maps.googleapis.com").build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

MyApiRequestInterface r=retrofit.create(MyApiRequestInterface.class);
            Call<JsonElement> call=r.loadRepo(origin,destination,GOOGLE_API_KEY);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    JsonElement j=response.body();

//                    String t=response.body();
                    try {
                        parseJSon(j.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                }
            });

//MyApiRequestInterface r=restAdapter.create(MyApiRequestInterface.class);
//            r.getJson(origin, destination,GOOGLE_API_KEY, new Callback<JsonElement>() {
//
//            r.getJson(origin, destination, GOOGLE_API_KEY, new Callback<JsonElement>() {
//                        @Override
//                        public void success(JsonElement s, Response response) {
//                            System.out.println("dekho"+response.toString());
//                            try {
//                                parseJSon(s.toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
////                            Toast.makeText(DirectionFinder.this, "", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void failure(RetrofitError error) {
//                            try{
//                            System.out.println(error.toString()+"mass"+URLEncoder.encode(origin, "utf-8"));
//                            }
//                            catch (Exception e){};
//
//                        }
//                    });
//                @Override
//                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//
//                }
//
//                @Override
//                public void onFailure(Call<JsonElement> call, Throwable t) {
//
//                }
//            });
                    //  new DownloadRawData().execute(createUrl());
        }


        private String createUrl() throws UnsupportedEncodingException {
            String urlOrigin = URLEncoder.encode(origin, "utf-8");
            String urlDestination = URLEncoder.encode(destination, "utf-8");

            return DIRECTION_URL_API + "origin=" + urlOrigin + "&destination=" + urlDestination + "&key=" + GOOGLE_API_KEY;
        }
private class DownloadRawData extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String link = params[0];
        try {
            URL url = new URL(link);
            HttpParams httpParameters = new BasicHttpParams();

            HttpConnectionParams
                    .setConnectionTimeout(httpParameters, 30000);

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpGet geth=new HttpGet(url.toString());

//            StringEntity entity = new StringEntity(payload.toString());

//                httppost.setEntity(entity);
            HttpResponse response = client.execute(geth);
            HttpEntity ent = response.getEntity();
            String responseString = EntityUtils.toString(ent, "UTF-8");
            InputStream is = url.openConnection().getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
return responseString;
         //   return buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        try {
            parseJSon(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

    private void parseJSon(String data) throws JSONException {
        if (data == null)
            return;

        List<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.duration = new com.example.aniket.googlemapsintegration.model.Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            route.endAddress = jsonLeg.getString("end_address");
            route.startAddress = jsonLeg.getString("start_address");
            route.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
            route.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
            route.points = decodePolyLine(overview_polylineJson.getString("points"));

            routes.add(route);
        }

        listener.onDirectionFinderSuccess(routes);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
