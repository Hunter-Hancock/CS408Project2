package edu.jsu.mcis.cs408.crosswordmagic.model.dao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HttpsURLConnection;

import edu.jsu.mcis.cs408.crosswordmagic.model.Puzzle;
import edu.jsu.mcis.cs408.crosswordmagic.model.PuzzleListItem;
import edu.jsu.mcis.cs408.crosswordmagic.model.Word;

public class WebServiceDAO {

    private final String TAG = "WebServiceDAO";
    private final DAOFactory daoFactory;
    WebServiceDAO(DAOFactory daoFactory) { this.daoFactory = daoFactory; }
    private static final String HTTP_METHOD = "GET";
    private static final String ROOT_URL = "http://ec2-3-142-171-53.us-east-2.compute.amazonaws.com:8080/CrosswordMagicServer/puzzle";
    private String requestUrl;
    private ExecutorService pool;

    public ArrayList<PuzzleListItem> list() {
        requestUrl = ROOT_URL;

        ArrayList<PuzzleListItem> result =  new ArrayList<>();

        try {
            pool = Executors.newSingleThreadExecutor();
            Future<String> pending = pool.submit(new CallableHTTPRequest());
            String response = pending.get();

            JSONArray items = new JSONArray(response);

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int id = item.getInt("id");
                String name = item.getString("name");

                getPuzzle(id);

                PuzzleListItem menuItem = new PuzzleListItem(id, name);

                result.add(menuItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private void getPuzzle(int puzzleId) {
        PuzzleDAO puzzleDAO = daoFactory.getPuzzleDAO();
        requestUrl = ROOT_URL + "?id=" + puzzleId;

        try {
            pool = Executors.newSingleThreadExecutor();
            Future<String> pending = pool.submit(new CallableHTTPRequest());
            String response = pending.get();

            JSONObject json = new JSONObject(response);
            String name = json.getString("name");
            String description = json.getString("description");
            int width = json.getInt("width");
            int height = json.getInt("height");

            PuzzleListItem[] existingPuzzles = puzzleDAO.list();
            for (PuzzleListItem item : existingPuzzles) {
                if (item.getName().equals(name)) {
                    return;
                }
            }

            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("description", description);
            params.put("width", String.valueOf(width));
            params.put("height", String.valueOf(height));

            Puzzle puzzle = new Puzzle(params);

            int insertedId = puzzleDAO.create(puzzle);

            getWords(insertedId, json.getJSONArray("puzzle"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWords(int puzzleId, JSONArray words) {
        WordDAO wordDAO = daoFactory.getWordDAO();

        try {
            for (int i = 0; i < words.length(); i++) {
                JSONObject w = words.getJSONObject(i);
                String clue = w.getString("clue");
                String wordName = w.getString("word");
                int column = w.getInt("column");
                int row = w.getInt("row");
                int box = w.getInt("box");
                int direction = w.getInt("direction");

                HashMap<String, String> wordParams = new HashMap<>();
                wordParams.put("puzzleid", String.valueOf(puzzleId));
                wordParams.put("clue", clue);
                wordParams.put("word", wordName);
                wordParams.put("column", String.valueOf(column));
                wordParams.put("row", String.valueOf(row));
                wordParams.put("box", String.valueOf(box));
                wordParams.put("direction", String.valueOf(direction));

                Word newWord = new Word(wordParams);

                wordDAO.create(newWord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class CallableHTTPRequest implements Callable<String> {
        @Override
        public String call() {
            StringBuilder s  = new StringBuilder();
            String line;
            HttpURLConnection conn;

            try {
                URL url = new URL(requestUrl);

                conn = (HttpURLConnection)url.openConnection();

                conn.setRequestMethod(HTTP_METHOD);
                conn.setConnectTimeout(15000); // 15 seconds
                conn.setReadTimeout(10000); // 10 seconds
                conn.setDoInput(true);
                conn.connect();

                int code = conn.getResponseCode();

                if (code == HttpsURLConnection.HTTP_OK || code == HttpsURLConnection.HTTP_CREATED) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while ((line = reader.readLine()) != null) {
                        s.append(line);
                    }
                    reader.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return s.toString().trim();
        }
    }
}
