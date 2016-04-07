

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by yazhoujiang on 15/8/9.
 */
public class HttpClient {
    private StringBuilder data = new StringBuilder();

    public void formatParameterAndDoPost(Map<Object, Object> map) {
        System.out.println(formatParameterToJson(map));
        try {
            doPost(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doPost(String url) throws Exception {
        data.delete(0, data.length());
        URL url1 = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
        urlConnection.connect();
    }

    private String formatParameterToJson(Map<Object, Object> map) {
        Set<Map.Entry<Object, Object>> set = map.entrySet();
        Iterator<Map.Entry<Object, Object>> entryIterator = set.iterator();
        if (entryIterator.hasNext() == false) {
            data.deleteCharAt(data.length() - 1);
            return data.toString();
        }
        data.append("{");
        Map.Entry<Object, Object> entry = entryIterator.next();
        while (entry != null) {
            formatParameter(entry, data);
            if (entryIterator.hasNext()) {
                data.append(",");
            } else {
                break;
            }
            entry = entryIterator.next();
        }
        data.append("}");
        return data.toString();
    }

    private String formatParameter(Map.Entry<Object, Object> entry, StringBuilder stringBuilder) {
        Object value = entry.getValue();
        if (value instanceof String) {
            stringBuilder.append('"' + (String) entry.getKey() + '"' + ":" + '"' + (String) value + '"');
        } else if (value instanceof Integer) {
            stringBuilder.append('"' + (String) entry.getKey() + '"' + ":" + '"' + (Integer) value + '"');
        } else if (value instanceof HashMap) {
            formatParameterToJson((HashMap) value);
        } else if (value instanceof ArrayList) {
            stringBuilder.append('"' + (String) entry.getKey() + '"' + ":");
            ArrayList<Object> arrayList = (ArrayList) value;
            stringBuilder.append("[");
            for (Object item : arrayList) {
                if (item instanceof String) {
                    stringBuilder.append('"' + (String) item + '"');
                } else if (item instanceof Integer) {
                    stringBuilder.append(item);
                } else if (item instanceof Map) {
                    formatParameterToJson((HashMap) item);
                }
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}
