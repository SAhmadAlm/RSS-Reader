# RSS-Reader

RSSReader is a Java-based application designed to read and display RSS feed updates from various websites. Users can add, remove, and display RSS feed URLs, 

allowing them to keep track of the latest updates from their favorite sources.



# Features

Show Updates: Display the latest RSS feed items from selected or all saved URLs.

Add URL: Add a new RSS feed URL to the list.

Remove URL: Remove an existing RSS feed URL from the list.

Exit: Save the current list of URLs to a file and exit the application.


# How to use

Running the Application

Run the RSSReader class to start the application.

When the application starts, the main menu will be displayed with the following options:

Welcome to RSS Reader!

Type a valid number for your desired action:

[1] Show updates

[2] Add URL

[3] Remove URL

[4] Exit


Show Updates:

Displays a list of saved URLs.

Choose a specific URL or all URLs to fetch and display the latest RSS feed items.

Each feed item includes the title, link, and description.

Add URL:

Prompts for a new website URL.

The application attempts to extract the RSS feed URL from the provided website URL.

If successful, the RSS feed URL is added to the list.

Remove URL:

Prompts for a website URL to remove.

If the URL is found in the list, it is removed.

Exit:

Saves the current list of URLs to data.txt.

Exits the application.

# Requierments

Libraries:

org.jsoup 

org.w3c

javax.xml

java.io

java.net

java.util


# Note

Ensure you have an internet connection to fetch RSS feeds.
