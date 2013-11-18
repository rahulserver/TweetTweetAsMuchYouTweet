import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: rahulserver
 * Date: 11/8/13
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class Account {
    String screenName;
    AccessToken accessToken;
    Twitter twitter;
    String appKey;
    String appSecret;
    long id;

    public Account(String appKey, String appSecret) throws Exception {
        getAccessTokenAfterAuthentication(appKey, appSecret);
    }

    public AccessToken getAccessTokenAfterAuthentication(String appKey, String appSecret) throws Exception {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(appKey);
        builder.setOAuthConsumerSecret(appSecret);
        Configuration configuration = builder.build();
        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();
        this.twitter = twitter;
        //twitter.setOAuthConsumer(appKey, appSecret);
        RequestToken requestToken = twitter.getOAuthRequestToken("oob");
        AccessToken accessToken = null;
        InputStreamReader isr = new java.io.InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        while (null == accessToken) {
            System.out.println("Open the following URL and grant access to your account:");
            URL url = new URL(requestToken.getAuthorizationURL());
            java.awt.Desktop.getDesktop().browse(url.toURI());
            int mc = JOptionPane.QUESTION_MESSAGE;
            String pin = JOptionPane.showInputDialog(null, "Enter PIN:", "PIN", mc);
            try {
                if (pin.length() > 0) {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                } else {
                    accessToken = twitter.getOAuthAccessToken();
                }
            } catch (TwitterException te) {
                if (401 == te.getStatusCode()) {
                    System.out.println("Unable to get the access token.");
                } else {
                    te.printStackTrace();
                }
            }
        }
        setAccessToken(accessToken);
        setScreenName(twitter.getScreenName());
        setId(twitter.getId());
        Gson gson = new Gson();
        System.out.println("gson is to jsonnnnnnnnnnn       ------------           " + gson.toJson(this));
        return accessToken;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void updateStatus(String tweet) throws TwitterException {

        Status status = twitter.updateStatus(tweet);

    }

    public AccessToken getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public boolean persistAccount() {
        try {
            Gson gson = new GsonBuilder().create();
            String saveAccessToken = accessToken.getToken();
            String saveAccessTokenSecret = accessToken.getTokenSecret();
            String userDir = System.getProperty("user.home");
            String twitterDir = userDir + File.separator + "TweetTweetAsMuchYouTweet";
            File f = new File(twitterDir);
            if (!f.exists())
                f.mkdirs();
            String fileName = twitterDir + File.separator + "Accounts.txt";
            File storeFile = new File(fileName);
            if (!storeFile.exists())
                storeFile.createNewFile();
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(storeFile, true)));
            out.println(gson.toJson(this));
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<Account> getAccountList() {
        Gson gson = new Gson();
        ArrayList<Account> accountList = new ArrayList<Account>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(System.getProperty("user.home") + File.separator
                    + "TweetTweetAsMuchYouTweet" + File.separator + "Accounts.txt")));
            String s = "";
            while ((s = br.readLine()) != null) {
                Account account = gson.fromJson(s, Account.class);
                accountList.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountList;
    }

    public static void main(String[] args) {
        try {
            new Account("key", "secret").persistAccount();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
