import org.jsoup.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RSSReader {

    private static final int MAX_ITEMS = 5;
    private static final String DATA_FILE = "data.txt";

    public static void main(String[] args) {
        System.out.println("Welcome to RSS Reader!");
        try {
            Scanner scanner = new Scanner(System.in);
            List<String> urls = readURLsFromFile(DATA_FILE);

            while (true) {
                System.out.println("Type a valid number for your desired action:");
                System.out.println("[1] Show updates");
                System.out.println("[2] Add URL");
                System.out.println("[3] Remove URL");
                System.out.println("[4] Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        showUpdates(urls);
                        break;
                    case 2:
                        addURL(urls, scanner);
                        break;
                    case 3:
                        removeURL(urls, scanner);
                        break;
                    case 4:
                        saveURLsToFile(urls, DATA_FILE);
                        System.out.println("See you next time!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showUpdates(List<String> urls) {
        System.out.println("Choose a website:");
        int index = 1;
        System.out.println("[0] All websites");
        for (String url : urls) {
            System.out.println("[" + index + "] " + url);
            index++;
        }
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice >= 1 && choice <= urls.size()) {
            retrieveRssContent(urls.get(choice - 1));
        } else if (choice == 0) {
            for (String url : urls) {
                retrieveRssContent(url);
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }


    private static void addURL(List<String> urls, Scanner scanner) {
        System.out.println("Please enter website URL to add:");
        String newURL = scanner.nextLine();
        try {
            String rssUrl = extractRssUrl(newURL);
            if (urls.contains(rssUrl)) {
                System.out.println("This URL already exists.");
            } else {
                urls.add(rssUrl);
                System.out.println("URL added successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error: Unable to extract RSS URL.");
        }
    }

    private static void removeURL(List<String> urls, Scanner scanner) {
        System.out.println("Please enter website URL to remove:");
        String urlToRemove = scanner.nextLine();
        if (urls.remove(urlToRemove)) {
            System.out.println("URL removed successfully.");
        } else {
            System.out.println("URL not found.");
        }
    }

    public static void retrieveRssContent(String rssUrl) {
        try {
            String rssXml = fetchPageSource(rssUrl);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append(rssXml);
            ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
            Document doc = documentBuilder.parse(input);
            NodeList itemNodes = doc.getElementsByTagName("item");

            for (int i = 0; i < Math.min(MAX_ITEMS, itemNodes.getLength()); ++i) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) itemNode;
                    System.out.println("Title: " + element.getElementsByTagName("title").item(0).getTextContent());
                    System.out.println("Link: " + element.getElementsByTagName("link").item(0).getTextContent());
                    System.out.println("Description: " + element.getElementsByTagName("description").item(0).getTextContent());
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Error in retrieving RSS content for " + rssUrl + ": " + e.getMessage());
        }
    }

    public static String extractRssUrl(String url) throws IOException {
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        return doc.select("[type='application/rss+xml']").attr("abs:href");
    }

    public static String fetchPageSource(String urlString) throws Exception {
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML , like Gecko) Chrome/108.0.0.0 Safari/537.36");
        return toString(urlConnection.getInputStream());
    }

    private static String toString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null)
            stringBuilder.append(inputLine);

        return stringBuilder.toString();
    }

    private static List<String> readURLsFromFile(String fileName) throws IOException {
        List<String> urls = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists())
            return urls;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            urls.add(line);
        }
        reader.close();
        return urls;
    }

    private static void saveURLsToFile(List<String> urls, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (String url : urls) {
            writer.write(url);
            writer.newLine();
        }
        writer.close();
    }
}
