package com.cats_app;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;

public class CatsService {

  public static void showCats() throws IOException {

    Cats cats = getCatsApi();

    // Resize Image;
    Image image = null;

    try {
      URL url = new URL(cats.getUrl());
      image = ImageIO.read(url);

      ImageIcon catBackground = generateImage(image);

      showMenu(cats, catBackground);

    } catch (IOException e) {
      System.out.println("Invalid URL" + e);
    }

  }

  public static Response addFavorite(Cats cat) throws IOException {
    OkHttpClient client = new OkHttpClient();
    JsonObject body = new JsonObject();

    body.addProperty("image_id", cat.getId());

    Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/favourites")
        .post(RequestBody.create(MediaType.parse("application/json"), body.toString()))
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", cat.getApiKey())
        .build();

    Response response = client.newCall(request).execute();

    return response;
  }

  private static Cats getCatsApi() throws IOException {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/images/search").get().build();

    Response response = client.newCall(request).execute();
    String json = response.body().string();

    json = json.substring(1, json.length());
    json = json.substring(0, json.length() - 1);

    // Convert to JSON
    Gson gson = new Gson();
    Cats cats = gson.fromJson(json, Cats.class);
    return cats;
  }

  public static void getFavorites(String apiKey) throws IOException {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url("https://api.thecatapi.com/v1/favourites")
        .get()
        .addHeader("Content-Type", "application/json")
        .addHeader("x-api-key", apiKey).build();

      Response res = client.newCall(request).execute();

      String json = res.body().string();

      Gson gson = new Gson();
      CatsFav[] cats = gson.fromJson(json, CatsFav[].class);

      if(cats.length > 0) {
        int min = 1;
        int max = cats.length;
        int random = (int) (Math.random() * ((max - min) - 1)) + min;

        int index = random - 1;

        CatsFav cat = cats[index];

      }

  }

  private static ImageIcon generateImage(Image image) {
    ImageIcon catBackground = new ImageIcon(image);

    if (catBackground.getIconWidth() > 800) {
      Image background = catBackground.getImage();
      Image modify = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
      catBackground = new ImageIcon(modify);
    }
    return catBackground;
  }

  private static void showMenu(Cats cats, ImageIcon catBackground) throws IOException {
    String menu = "Options: \n 1. Show other image \n 2. Add to favorite \n 3. Back to menu";

    String[] buttons = { " 1. Show other image", " 2. Add to favorite", " 3. Back to menu" };

    String idCat = cats.getId();

    String option = (String) JOptionPane.showInputDialog(null, menu, idCat,
        JOptionPane.INFORMATION_MESSAGE, catBackground, buttons, buttons[0]);

    int optionMenu = -1;

    for (int i = 0; i < buttons.length; i++) {
      if (option.equals(buttons[i])) {
        optionMenu = i;
      }
    }

    switch (optionMenu) {
      case 0:
        showCats();
        break;

      case 1:
        addFavorite(cats);
        break;

      case 2:
        System.exit(0);
        break;
      default:
        System.out.println("Invalid option");
        break;
    }
  }

}
