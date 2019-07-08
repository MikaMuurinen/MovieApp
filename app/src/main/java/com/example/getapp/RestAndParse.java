package com.example.getapp;

import android.util.Log;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestAndParse {

    static OkHttpClient client = new OkHttpClient();

    public static void RequestTheatres(DataStorage storage) {
        try {
            getHttpResponseTheatre(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestData(DataStorage storage) {
        try {
            getHttpResponse(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getHttpResponse(final DataStorage storage) throws IOException {

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String defaulturl = "1021";

        System.out.println("position: " + storage.cityposition);
        if (storage.cityposition != 0) defaulturl = storage.theatreID.get(storage.cityposition);
        String url = "https://www.finnkino.fi/xml/Schedule/?area=" + defaulturl + "&dt=" + storage.currentdate;

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                countDownLatch.countDown();
            }

            public boolean alreadyExisting(String movie) {
                boolean found = false;
                for (int i = 0; i < storage.movies.size(); i++) {
                    if (storage.movies.get(i).compareTo(movie) == 0) {
                        found = true;
                    }
                }
                return found;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();
                String temp = null;

                try {
                    DocumentBuilderFactory dbf =
                            DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(mMessage));

                    Document doc = db.parse(is);
                    NodeList nodes = doc.getElementsByTagName("Show");
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element element = (Element) nodes.item(i);

                        NodeList title = element.getElementsByTagName("Title");
                        Element XMLline = (Element) title.item(0);
                        System.out.println("Title: " + getCharacterDataFromElement(XMLline));

                        if (alreadyExisting(getCharacterDataFromElement(XMLline)) == false) {
                            storage.movies.add(getCharacterDataFromElement(XMLline));
                        }
                        storage.moviesAll.add(getCharacterDataFromElement(XMLline));

                        NodeList name = element.getElementsByTagName("dttmShowStart");
                        XMLline = (Element) name.item(0);
                        System.out.println("Name: " + getCharacterDataFromElement(XMLline));
                        storage.moviestart.add("Näytös alkaa: " + getCharacterDataFromElement(XMLline).substring(11, 16));

                        name = element.getElementsByTagName("dttmShowEnd");
                        XMLline = (Element) name.item(0);
                        System.out.println("Name: " + getCharacterDataFromElement(XMLline));
                        storage.movieends.add("Näytös loppuu: " +
                                getCharacterDataFromElement(XMLline).substring(11, 16));
                        storage.currentdate = getCharacterDataFromElement(XMLline).substring(0, 10);
                        storage.currentdate = storage.currentdate.substring(8, 10) + "." + storage.currentdate.substring(5, 7) + "." + storage.currentdate.substring(0, 4);

                        name = element.getElementsByTagName("TheatreAndAuditorium");
                        XMLline = (Element) name.item(0);
                        System.out.println("Name: " + getCharacterDataFromElement(XMLline));
                        storage.theatre.add(getCharacterDataFromElement(XMLline));

                        name = element.getElementsByTagName("EventMediumImagePortrait");
                        XMLline = (Element) name.item(0);
                        System.out.println("Name: " + getCharacterDataFromElement(XMLline));
                        if (getCharacterDataFromElement(XMLline) != null) {
                        storage.movieimage.add(getCharacterDataFromElement(XMLline));}
                        else {
                            System.out.println("ERROR - NO MEDIUM");
                            name = element.getElementsByTagName("EventLargeImagePortrait");
                            XMLline = (Element) name.item(0);
                            storage.movieimage.add(getCharacterDataFromElement(XMLline));
                        }

                        name = element.getElementsByTagName("LengthInMinutes");
                        XMLline = (Element) name.item(0);
                        System.out.println("Kesto: " + getCharacterDataFromElement(XMLline) + " min\n");
                        temp = "Kesto: " + getCharacterDataFromElement(XMLline) + " min\n";

                        name = element.getElementsByTagName("PresentationMethodAndLanguage");
                        XMLline = (Element) name.item(0);
                        System.out.println(getCharacterDataFromElement(XMLline));
                        temp = temp + getCharacterDataFromElement(XMLline) + "\n";

                        name = element.getElementsByTagName("Genres");
                        XMLline = (Element) name.item(0);
                        System.out.println(getCharacterDataFromElement(XMLline));
                        temp = temp + "Genre: " + getCharacterDataFromElement(XMLline) + "\n";

                        name = element.getElementsByTagName("Rating");
                        XMLline = (Element) name.item(0);
                        System.out.println(getCharacterDataFromElement(XMLline));
                        temp = temp + "Ikäraja: " + getCharacterDataFromElement(XMLline) + "\n";

                        storage.movieinfo.add(temp);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            };
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String getCharacterDataFromElement(Element e) {
        if (e != null) {
            Node child = e.getFirstChild();
            if (child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                return cd.getData();
            }
        }
        return null;
    }


    public static void getHttpResponseTheatre(final DataStorage storage) throws IOException {

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        String url = "https://www.finnkino.fi/xml/TheatreAreas/";

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();

                try {
                    DocumentBuilderFactory dbf =
                            DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    InputSource is = new InputSource();
                    is.setCharacterStream(new StringReader(mMessage));

                    Document doc = db.parse(is);
                    NodeList nodes = doc.getElementsByTagName("TheatreArea");
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element element = (Element) nodes.item(i);

                        NodeList id = element.getElementsByTagName("ID");
                        Element XMLline = (Element) id.item(0);
                        System.out.println("Title: " + getCharacterDataFromElement(XMLline));
                        storage.theatreID.add(getCharacterDataFromElement(XMLline));

                        NodeList name = element.getElementsByTagName("Name");
                        XMLline = (Element) name.item(0);
                        System.out.println("Name: " + getCharacterDataFromElement(XMLline));
                        if (i==0) storage.theatreName.add("Tampere - Valitse alue"); else
                        storage.theatreName.add(getCharacterDataFromElement(XMLline));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            };
        });
        try{
            countDownLatch.await();}
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

}
