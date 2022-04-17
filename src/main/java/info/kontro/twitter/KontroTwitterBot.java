package info.kontro.twitter;

import info.kontro.KontroBotPlatform;
import info.kontro.Main;
import info.kontro.mongo.TicketControl;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class KontroTwitterBot implements KontroBotPlatform {

    private Twitter twitter;
    private Main main;


    public KontroTwitterBot(Main main, String twitter_consumer_key, String twitter_consumer_secret, String twitter_access_token, String twitter_acsees_token_secret) {
        this.main = main;
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(twitter_consumer_key)
                .setOAuthConsumerSecret(twitter_consumer_secret)
                .setOAuthAccessToken(twitter_access_token)
                .setOAuthAccessTokenSecret(twitter_acsees_token_secret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        try {
            String name = twitter.getScreenName();

            System.out.println("Connected successfully to \"" + name + "\" over Twitter API"
                    + "\nhttps://twitter.com/" + name);
        } catch (TwitterException e) {
            if (e.getStatusCode() == 32) {
                System.out.println("Twitter doesn't like your api keys :(");
                main.settings.changeTwitterKeys();
            }
            e.printStackTrace();
        }
    }

    public void newTicketControl(TicketControl ticketControl) {
        StringBuilder tweet = new StringBuilder();
        String hashtag = ticketControl.getHashtag();

        tweet.append(hashtag)
                .append("\n\n")
                .append(ticketControl.getDescription())
                .append("\n")
                .append(ticketControl.getTelegramChannelUrl());
        try {
            Status status = twitter.updateStatus(tweet.toString());
            ticketControl.setTweetId(status.getId());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTicketControl(TicketControl ticketControl) {
        try {
            twitter.destroyStatus(ticketControl.getTweetId());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDescription(TicketControl ticketControl) {

    }
}
